package com.abn.amro.utils;

import static java.util.Collections.emptySet;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import com.abn.amro.model.MealType;
import com.abn.amro.model.Recipe;
import java.util.Set;

public class RecipeTestBuilder {
    public static Recipe createTestRecipe(String recipeName) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setMealType(MealType.OTHER);
        recipe.setPreparation("Some cooking instructions");
        recipe.setRecipeIngredients(emptySet());
        return recipe;
    }

    public static RecipeRequestDto createTestRecipeRequestDTO(String recipeName) {
        RecipeRequestDto recipeRequest = new RecipeRequestDto();
        recipeRequest.setName(recipeName);
        recipeRequest.setMealType(MealType.OTHER);
        recipeRequest.setPreparation("Some other instructions");
        recipeRequest.setIngredientIds(Set.of(1L));
        recipeRequest.setNumberOfServings(4);
        return recipeRequest;
    }

    public static RecipeResponseDto createTestRecipeResponseDTO(String recipeName) {
        RecipeResponseDto recipeResponse = new RecipeResponseDto();
        recipeResponse.setName(recipeName);
        recipeResponse.setMealType(MealType.OTHER);
        recipeResponse.setPreparation("Some instructions");
        recipeResponse.setRecipeIngredients(emptySet());
        recipeResponse.setNumberOfServings(2);
        return recipeResponse;
    }
}
