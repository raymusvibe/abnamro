package com.abn.amro.service;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.dto.request.IngredientRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.IngredientResponseDto;
import com.abn.amro.exceptions.NotFoundException;
import com.abn.amro.model.Ingredient;
import com.abn.amro.repository.IngredientRepository;
import com.abn.amro.service.abstractions.IngredientService;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngredientServiceIntegrationTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    private IngredientService ingredientService;

    @BeforeAll
    void initialise() {
        ingredientService = new IngredientServiceImpl(ingredientRepository, new ModelMapper());
    }

    @BeforeEach
    void deleteAll() {
        ingredientRepository.deleteAll();
    }

    @Test
    void IngredientService_WhenNoIngredientsAdded_EmptyResponseList() {
        List<IngredientResponseDto> ingredientResponseDtoList = ingredientService.getIngredients(0, 10);

        assertTrue(ingredientResponseDtoList.isEmpty());
    }

    @Test
    void IngredientService_WhenIngredientsAdded_ResponseListSizeMatches() {
        Ingredient firstIngredient = new Ingredient("Potatoes");
        Ingredient secondIngredient = new Ingredient("Eggs");
        List<Ingredient> ingredientList = List.of(firstIngredient, secondIngredient);
        ingredientRepository.saveAll(ingredientList);

        List<IngredientResponseDto> ingredientResponseDtoList = ingredientService.getIngredients(0, 10);

        assertEquals(ingredientList.size(), ingredientResponseDtoList.size());
    }

    @Test
    void IngredientService_WhenGetInvalidIngredientById_NotFoundException() {
        assertThrows(NotFoundException.class, () -> ingredientService.getIngredientById(1L));
    }

    @Test
    void IngredientService_WhenGetValidIngredientById_IngredientCanBeFound() {
        Ingredient ingredient = new Ingredient("Bacon");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        IngredientResponseDto ingredientResponseDto = ingredientService.getIngredientById(savedIngredient.getId());

        assertEquals(ingredient.getIngredient(), ingredientResponseDto.getIngredient());
    }

    @Test
    void IngredientService_WhenCreateIngredient_ValidIngredientIdCreated() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Curry");

        CreateEntityResponseDto createEntityResponseDtoResponseDto =
                ingredientService.createIngredient(ingredientRequestDto);

        assertNotNull(createEntityResponseDtoResponseDto.getId());
    }

    @Test
    void IngredientService_WhenUpdateIngredient_FieldsMatch() {
        String ingredientName = "Cabbages";
        String updatedIngredientName = "Cabbage";
        Ingredient ingredient = new Ingredient(ingredientName);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto(updatedIngredientName);

        IngredientResponseDto ingredientResponseDto =
                ingredientService.updateIngredient(savedIngredient.getId(), ingredientRequestDto);

        assertEquals(updatedIngredientName, ingredientResponseDto.getIngredient());
    }

    @Test
    void IngredientService_WhenDeleteInvalidIngredient_NotFoundException() {
        Long invalidId = 14L;

        assertThrows(NotFoundException.class, () -> ingredientService.deleteIngredient(invalidId));
    }
}
