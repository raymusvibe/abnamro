package com.abn.amro.service;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.*;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import com.abn.amro.exceptions.NotFoundException;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.MealType;
import com.abn.amro.model.Recipe;
import com.abn.amro.repository.IngredientRepository;
import com.abn.amro.repository.RecipeRepository;
import com.abn.amro.service.abstractions.RecipeService;
import com.abn.amro.utils.RecipeTestObjectBuilder;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeServiceIntegrationTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private RecipeService recipeService;

    @BeforeAll
    void initialise() {
        recipeService = new RecipeServiceImpl(recipeRepository, ingredientRepository, new ModelMapper());
    }

    @BeforeEach
    void deleteAll() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    void RecipeService_WhenNoRecipesAdded_EmptyListOfRecipesResponse() {
        List<RecipeResponseDto> recipeResponseDtoList = recipeService.getRecipes(0, 10);

        assertTrue(recipeResponseDtoList.isEmpty());
    }

    @Test
    void RecipeService_WhenRecipesAdded_ResponseListSizeMatches() {
        Recipe firstRecipe = RecipeTestObjectBuilder.createTestRecipeWithName("Lasagne");
        Recipe secondRecipe = RecipeTestObjectBuilder.createTestRecipeWithName("MeatLoaf");
        List<Recipe> recipeList = List.of(firstRecipe, secondRecipe);
        recipeRepository.saveAll(recipeList);

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.getRecipes(0, 10);

        assertEquals(recipeList.size(), recipeResponseDtoList.size());
    }

    @Test
    void RecipeService_WhenGetInvalidRecipeById_NotFoundException() {
        assertThrows(NotFoundException.class, () -> recipeService.getRecipeById(1L));
    }

    @Test
    void RecipeService_WhenGetValidRecipeById_RecipeCanBeFound() {
        Recipe recipe = RecipeTestObjectBuilder.createTestRecipeWithName("Tuna Steak");
        Recipe savedRecipe = recipeRepository.save(recipe);

        RecipeResponseDto recipeResponseDto = recipeService.getRecipeById(savedRecipe.getId());

        assertEquals(recipe.getName(), recipeResponseDto.getName());
        assertEquals(recipe.getMealType(), recipeResponseDto.getMealType());
        assertEquals(recipe.getNumberOfServings(), recipeResponseDto.getNumberOfServings());
    }

    @Test
    void RecipeService_WhenCreateRecipe_ValidRecipeIdCreated() {
        RecipeRequestDto recipeRequestDto = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Burger");
        recipeRequestDto.setIngredientIds(Set.of());

        CreateEntityResponseDto createEntityResponseDtoResponseDto = recipeService.createRecipe(recipeRequestDto);

        assertNotNull(createEntityResponseDtoResponseDto.getId());
    }

    @Test
    void RecipeService_WhenCreateRecipeWithInvalidIngredients_NotFoundException() {
        RecipeRequestDto recipeRequestDto = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Burger");
        recipeRequestDto.setIngredientIds(Set.of(3L));

        assertThrows(NotFoundException.class, () -> recipeService.createRecipe(recipeRequestDto));
    }

    @Test
    void RecipeService_WhenUpdateRecipe_FieldsMatch() {
        String originalRecipeName = "Pizza";
        String updatedRecipeName = "Vegan Pizza";
        Recipe recipe = RecipeTestObjectBuilder.createTestRecipeWithName(originalRecipeName);
        Recipe savedRecipe = recipeRepository.save(recipe);
        RecipeRequestDto recipeRequestDto =
                RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName(updatedRecipeName);
        recipeRequestDto.setIngredientIds(Set.of());

        RecipeResponseDto recipeResponseDto = recipeService.updateRecipe(savedRecipe.getId(), recipeRequestDto);

        assertNotEquals(originalRecipeName, recipeResponseDto.getName());
        assertEquals(recipeRequestDto.getName(), recipeResponseDto.getName());
        assertEquals(recipeRequestDto.getMealType(), recipeResponseDto.getMealType());
        assertEquals(recipeRequestDto.getNumberOfServings(), recipeResponseDto.getNumberOfServings());
    }

    @Test
    void RecipeService_WhenDeleteInvalidRecipe_NotFoundException() {
        Long invalidId = 14L;

        assertThrows(NotFoundException.class, () -> recipeService.deleteRecipe(invalidId));
    }

    @Test
    void RecipeService_WhenSearchByBlankCriteria_ReturnAllRecords() {
        Recipe firstRecipe = RecipeTestObjectBuilder.createTestRecipeWithName("Lasagne");
        Recipe secondRecipe = RecipeTestObjectBuilder.createTestRecipeWithName("MeatLoaf");
        List<Recipe> recipeList = List.of(firstRecipe, secondRecipe);
        recipeRepository.saveAll(recipeList);
        RecipeSearchRequestDto recipeSearchWithBlankCriteriaDto =
                RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();

        List<RecipeResponseDto> recipeResponseDtoList =
                recipeService.findBySearchCriteria(recipeSearchWithBlankCriteriaDto, 0, 10);

        assertEquals(recipeList.size(), recipeResponseDtoList.size());
    }

    @Test
    void RecipeService_WhenContainsSearchFilterByName_ReturnMatchingRecords() {
        String filterValue = "Lasagne";
        RecipeRequestDto firstRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Lasagne");
        RecipeRequestDto secondRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("MeatLoaf");
        Ingredient ingredient = new Ingredient("Beef");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        firstRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        secondRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        recipeService.createRecipe(firstRecipeRequest);
        recipeService.createRecipe(secondRecipeRequest);
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();
        SearchCriteriaRequestDto searchCriteriaDto =
                new SearchCriteriaRequestDto(FilterKey.name, SearchOperation.CONTAINS, filterValue);
        recipeSearchDto.setSearchCriteria(List.of(searchCriteriaDto));

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findBySearchCriteria(recipeSearchDto, 0, 10);

        assertEquals(filterValue, recipeResponseDtoList.get(0).getName());
    }

    @Test
    void RecipeService_WhenDoesNotContainSearchFilterByMealType_ReturnMatchingRecords() {
        MealType filterValue = MealType.VEGETARIAN;
        RecipeRequestDto firstRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Lasagne");
        firstRecipeRequest.setMealType(MealType.OTHER);
        RecipeRequestDto secondRecipeRequest =
                RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Vegetarian Meatballs");
        secondRecipeRequest.setMealType(MealType.VEGETARIAN);
        Ingredient ingredient = new Ingredient("Tomato");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        firstRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        secondRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        recipeService.createRecipe(firstRecipeRequest);
        recipeService.createRecipe(secondRecipeRequest);
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();
        SearchCriteriaRequestDto searchCriteriaDto =
                new SearchCriteriaRequestDto(FilterKey.mealType, SearchOperation.DOES_NOT_CONTAIN, filterValue);
        recipeSearchDto.setSearchCriteria(List.of(searchCriteriaDto));

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findBySearchCriteria(recipeSearchDto, 0, 10);

        recipeResponseDtoList.stream()
                .map(RecipeResponseDto::getMealType)
                .forEach(mealType -> assertNotEquals(filterValue, mealType));
    }

    @Test
    void RecipeService_WhenEqualsSearchFilterByServings_ReturnMatchingRecords() {
        int filterValue = 4;
        RecipeRequestDto firstRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Schnitzel");
        firstRecipeRequest.setNumberOfServings(4);
        RecipeRequestDto secondRecipeRequest =
                RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Chicken and Chorizo");
        secondRecipeRequest.setNumberOfServings(3);
        Ingredient ingredient = new Ingredient("Chicken");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        firstRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        secondRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        recipeService.createRecipe(firstRecipeRequest);
        recipeService.createRecipe(secondRecipeRequest);
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();
        SearchCriteriaRequestDto searchCriteriaDto =
                new SearchCriteriaRequestDto(FilterKey.numberOfServings, SearchOperation.EQUAL, filterValue);
        recipeSearchDto.setSearchCriteria(List.of(searchCriteriaDto));

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findBySearchCriteria(recipeSearchDto, 0, 10);

        recipeResponseDtoList.stream()
                .map(RecipeResponseDto::getNumberOfServings)
                .forEach(numberOfServings -> assertEquals(filterValue, numberOfServings));
    }

    @Test
    void RecipeService_WhenNotEqualsSearchFilterByServings_NoMatchingRecord() {
        String filterValue = "Curry";
        RecipeRequestDto firstRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Beef Curry");
        RecipeRequestDto secondRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Lamb Curry");
        Ingredient ingredient = new Ingredient("Curry");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        firstRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        secondRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        recipeService.createRecipe(firstRecipeRequest);
        recipeService.createRecipe(secondRecipeRequest);
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();
        SearchCriteriaRequestDto searchCriteriaDto =
                new SearchCriteriaRequestDto(FilterKey.ingredient, SearchOperation.NOT_EQUAL, filterValue);
        recipeSearchDto.setSearchCriteria(List.of(searchCriteriaDto));

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findBySearchCriteria(recipeSearchDto, 0, 10);

        assertTrue(recipeResponseDtoList.isEmpty());
    }

    @Test
    void RecipeService_MultipleSearchCriteria_ReturnMatchingRecords() {
        String firstFilterValue = "Stamppot";
        int secondFilterValue = 3;
        RecipeRequestDto firstRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Stamppot");
        firstRecipeRequest.setNumberOfServings(3);
        RecipeRequestDto secondRecipeRequest = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Oliebollen");
        secondRecipeRequest.setNumberOfServings(3);
        Ingredient ingredient = new Ingredient("Tomato");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        firstRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        secondRecipeRequest.setIngredientIds(Set.of(savedIngredient.getId()));
        recipeService.createRecipe(firstRecipeRequest);
        recipeService.createRecipe(secondRecipeRequest);
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();
        recipeSearchDto.setDataOption(DataOption.ALL);
        SearchCriteriaRequestDto firstSearchCriteriaDto =
                new SearchCriteriaRequestDto(FilterKey.name, SearchOperation.CONTAINS, firstFilterValue);
        SearchCriteriaRequestDto secondSearchCriteriaDto =
                new SearchCriteriaRequestDto(FilterKey.numberOfServings, SearchOperation.EQUAL, secondFilterValue);
        recipeSearchDto.setSearchCriteria(List.of(firstSearchCriteriaDto, secondSearchCriteriaDto));

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findBySearchCriteria(recipeSearchDto, 0, 10);

        recipeResponseDtoList.stream()
                .map(RecipeResponseDto::getName)
                .forEach(name -> assertEquals(firstFilterValue, name));
        recipeResponseDtoList.stream()
                .map(RecipeResponseDto::getNumberOfServings)
                .forEach(numberOfServings -> assertEquals(secondFilterValue, numberOfServings));
    }
}
