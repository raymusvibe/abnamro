package com.abn.amro.utils;

import static java.util.Collections.emptySet;

import com.abn.amro.dto.request.RecipeRequestDTO;
import com.abn.amro.dto.response.RecipeResponseDTO;
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

    public static RecipeRequestDTO createTestRecipeRequestDTO(String recipeName) {
        RecipeRequestDTO recipeRequest = new RecipeRequestDTO();
        recipeRequest.setName(recipeName);
        recipeRequest.setMealType(MealType.OTHER);
        recipeRequest.setPreparation("Some other instructions");
        recipeRequest.setIngredientIds(Set.of(1L));
        recipeRequest.setNumberOfServings(4);
        return recipeRequest;
    }
    public static RecipeResponseDTO createTestRecipeResponseDTO(String recipeName) {
        RecipeResponseDTO recipeResponse = new RecipeResponseDTO();
        recipeResponse.setName(recipeName);
        recipeResponse.setMealType(MealType.OTHER);
        recipeResponse.setPreparation("Some instructions");
        recipeResponse.setRecipeIngredients(emptySet());
        recipeResponse.setNumberOfServings(2);
        return recipeResponse;
    }
}
