package com.abn.amro.controller.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.dto.request.IngredientRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.IngredientResponseDto;
import com.abn.amro.model.Ingredient;
import com.abn.amro.repository.IngredientRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IngredientControllerIntegrationTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";
    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeEach() {
        baseUrl = baseUrl + ":" + port + "/api/v1/ingredient";
    }

    @AfterEach
    public void afterEach() {
        ingredientRepository.deleteAll();
    }

    @Test
    void IngredientController_WhenNoIngredientsAdded_EmptyResponseList() {
        List<IngredientResponseDto> recipesResponseList = restTemplate.getForObject(baseUrl, List.class);

        assertTrue(recipesResponseList.isEmpty());
    }

    @Test
    void IngredientController_WhenIngredientsAdded_ResponseListSizeMatches() {
        Ingredient firstIngredient = new Ingredient("Potatoes");
        Ingredient secondIngredient = new Ingredient("Eggs");
        List<Ingredient> ingredientList = List.of(firstIngredient, secondIngredient);
        ingredientRepository.saveAll(ingredientList);

        List<IngredientResponseDto> ingredientResponseDtoList = restTemplate.getForObject(baseUrl, List.class);

        assertEquals(ingredientList.size(), ingredientResponseDtoList.size());
    }

    @Test
    void IngredientController_WhenGetInvalidIngredientById_NotFoundException() {
        Long invalidId = 34L;

        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restTemplate.getForObject(baseUrl + "/" + invalidId, IngredientResponseDto.class));
    }

    @Test
    void IngredientController_WhenCreateIngredient_ValidIngredientIdCreated() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Curry");

        CreateEntityResponseDto createEntityResponseDtoResponseDto =
                restTemplate.postForObject(baseUrl, ingredientRequestDto, CreateEntityResponseDto.class);

        assertNotNull(createEntityResponseDtoResponseDto.getId());
    }

    @Test
    void IngredientController_WhenDeleteValidIngredient_NoException() {
        Ingredient ingredient = new Ingredient("Potatoes");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        assertDoesNotThrow(
                () -> restTemplate.delete(baseUrl + "/" + savedIngredient.getId()), "NotFoundException not thrown");
    }
}
