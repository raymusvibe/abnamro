package com.abn.amro.controller.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.DataOption;
import com.abn.amro.dto.request.search.RecipeSearchRequestDto;
import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import com.abn.amro.model.Recipe;
import com.abn.amro.repository.RecipeRepository;
import com.abn.amro.utils.RecipeTestObjectBuilder;
import java.util.List;
import java.util.Set;
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
public class RecipeControllerIntegrationTest {
    @Autowired
    private RecipeRepository recipeRepository;

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
        baseUrl = baseUrl + ":" + port + "/api/v1/recipe";
    }

    @AfterEach
    public void afterEach() {
        recipeRepository.deleteAll();
    }

    @Test
    void RecipeController_ListRecipes_GetsCorrectRecipesFromDatabase() {
        Recipe recipe = RecipeTestObjectBuilder.createTestRecipeWithName("Sushi");
        List<Recipe> recipeList = List.of(recipe);
        recipeRepository.saveAll(recipeList);

        List<RecipeResponseDto> recipesResponseList = restTemplate.getForObject(baseUrl, List.class);

        assertEquals(recipeList.size(), recipesResponseList.size());
    }

    @Test
    void RecipeController_GetRecipeById_ResponseMatches() {
        Recipe recipe = RecipeTestObjectBuilder.createTestRecipeWithName("Rendang");
        Recipe savedRecipe = recipeRepository.save(recipe);

        RecipeResponseDto recipesResponse =
                restTemplate.getForObject(baseUrl + "/" + savedRecipe.getId(), RecipeResponseDto.class);

        assertEquals(recipe.getName(), recipesResponse.getName());
        assertEquals(recipe.getMealType(), recipesResponse.getMealType());
        assertEquals(recipe.getNumberOfServings(), recipesResponse.getNumberOfServings());
    }

    @Test
    void RecipeController_WhenCreateRecipe_ValidRecipeIdCreated() {
        RecipeRequestDto recipeRequestDto = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Burger");
        recipeRequestDto.setIngredientIds(Set.of());

        CreateEntityResponseDto createEntityResponseDtoResponseDto =
                restTemplate.postForObject(baseUrl, recipeRequestDto, CreateEntityResponseDto.class);

        assertNotNull(createEntityResponseDtoResponseDto.getId());
    }

    @Test
    void RecipeController_GetRecipeByInvalidId_NotFoundException() {
        Long invalidId = 34L;

        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restTemplate.getForObject(baseUrl + "/" + invalidId, RecipeResponseDto.class));
    }

    @Test
    void RecipeController_WhenDeleteInvalidRecipe_NotFoundException() {
        Long invalidId = 14L;

        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.delete(baseUrl + "/" + invalidId));
    }

    @Test
    void RecipeController_WhenSearchByBlankCriteria_ReturnAllRecords() {
        Recipe firstRecipe = RecipeTestObjectBuilder.createTestRecipeWithName("Lasagne");
        Recipe secondRecipe = RecipeTestObjectBuilder.createTestRecipeWithName("MeatLoaf");
        List<Recipe> recipeList = List.of(firstRecipe, secondRecipe);
        recipeRepository.saveAll(recipeList);
        RecipeSearchRequestDto recipeSearchWithBlankCriteriaDto =
                RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();

        List<RecipeResponseDto> recipesResponseList =
                restTemplate.postForObject(baseUrl + "/" + "search", recipeSearchWithBlankCriteriaDto, List.class);

        assertEquals(recipeList.size(), recipesResponseList.size());
    }

    @Test
    void RecipeController_MultipleSearchCriteria_ReturnMatchingRecords() {
        String firstFilterValue = "Stamppot";
        int secondFilterValue = 3;
        Recipe firstRecipeRequest = RecipeTestObjectBuilder.createTestRecipeWithName("Stamppot");
        firstRecipeRequest.setNumberOfServings(3);
        Recipe secondRecipeRequest = RecipeTestObjectBuilder.createTestRecipeWithName("Oliebollen");
        secondRecipeRequest.setNumberOfServings(3);
        recipeRepository.saveAll(List.of(firstRecipeRequest, secondRecipeRequest));
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();
        recipeSearchDto.setDataOption(DataOption.ALL);
        SearchCriteriaRequestDto firstSearchCriteriaDto =
                new SearchCriteriaRequestDto("name", SearchOperation.CONTAINS, firstFilterValue);
        SearchCriteriaRequestDto secondSearchCriteriaDto =
                new SearchCriteriaRequestDto("numberOfServings", SearchOperation.EQUAL, secondFilterValue);
        recipeSearchDto.setSearchCriteria(List.of(firstSearchCriteriaDto, secondSearchCriteriaDto));

        List<RecipeResponseDto> recipeResponseDtoList =
                restTemplate.postForObject(baseUrl + "/" + "search", recipeSearchDto, List.class);

        recipeResponseDtoList.stream()
                .map(RecipeResponseDto::getName)
                .forEach(name -> assertEquals(firstFilterValue, name));
        recipeResponseDtoList.stream()
                .map(RecipeResponseDto::getNumberOfServings)
                .forEach(numberOfServings -> assertEquals(secondFilterValue, numberOfServings));
    }
}
