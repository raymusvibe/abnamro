package com.abn.amro.service;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import com.abn.amro.exceptions.NotFoundException;
import com.abn.amro.model.Recipe;
import com.abn.amro.repository.IngredientRepository;
import com.abn.amro.repository.RecipeRepository;
import com.abn.amro.service.abstractions.RecipeService;
import com.abn.amro.utils.RecipeTestBuilder;
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

        assertEquals(0, recipeResponseDtoList.size());
    }

    @Test
    void RecipeService_WhenRecipesAdded_ResponseListSizeMatches() {
        Recipe firstRecipe = RecipeTestBuilder.createTestRecipe("Lasagne");
        Recipe secondRecipe = RecipeTestBuilder.createTestRecipe("MeatLoaf");
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
        Recipe recipe = RecipeTestBuilder.createTestRecipe("Tuna Steak");
        Recipe savedRecipe = recipeRepository.save(recipe);

        RecipeResponseDto recipeResponseDto = recipeService.getRecipeById(savedRecipe.getId());

        assertEquals(recipe.getName(), recipeResponseDto.getName());
        assertEquals(recipe.getMealType(), recipeResponseDto.getMealType());
        assertEquals(recipe.getNumberOfServings(), recipeResponseDto.getNumberOfServings());
    }

    @Test
    void RecipeService_WhenCreateRecipe_ValidRecipeIdCreated() {
        RecipeRequestDto recipeRequestDto = RecipeTestBuilder.createTestRecipeRequestDto("Burger");
        recipeRequestDto.setIngredientIds(Set.of());

        CreateEntityResponseDto createEntityResponseDtoResponseDto = recipeService.createRecipe(recipeRequestDto);

        assertNotNull(createEntityResponseDtoResponseDto.getId());
    }

    @Test
    void RecipeService_WhenCreateRecipeWithInvalidIngredients_NotFoundException() {
        RecipeRequestDto recipeRequestDto = RecipeTestBuilder.createTestRecipeRequestDto("Burger");
        recipeRequestDto.setIngredientIds(Set.of(3L));

        assertThrows(NotFoundException.class, () -> recipeService.createRecipe(recipeRequestDto));
    }

    @Test
    void RecipeService_WhenUpdateRecipe_FieldsMatch() {
        String originalRecipeName = "Pizza";
        String updatedRecipeName = "Vegan Pizza";
        Recipe recipe = RecipeTestBuilder.createTestRecipe(originalRecipeName);
        Recipe savedRecipe = recipeRepository.save(recipe);
        RecipeRequestDto recipeRequestDto = RecipeTestBuilder.createTestRecipeRequestDto(updatedRecipeName);
        recipeRequestDto.setIngredientIds(Set.of());

        RecipeResponseDto recipeResponseDto = recipeService.updateRecipe(savedRecipe.getId(), recipeRequestDto);

        assertNotEquals(originalRecipeName, recipeResponseDto.getName());
        assertEquals(recipeRequestDto.getName(), recipeResponseDto.getName());
        assertEquals(recipeRequestDto.getMealType(), recipeResponseDto.getMealType());
        assertEquals(recipeRequestDto.getNumberOfServings(), recipeResponseDto.getNumberOfServings());
    }

    @Test
    void RecipeService_WhenDeleteInvalidRecipe_NotFoundException() {
        assertThrows(NotFoundException.class, () -> recipeService.deleteRecipe(14L));
    }
}
