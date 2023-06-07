package com.abn.amro.utils;

import static java.util.Collections.emptySet;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.DataOption;
import com.abn.amro.dto.request.search.RecipeSearchRequestDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import com.abn.amro.model.MealType;
import com.abn.amro.model.Recipe;
import java.util.List;
import java.util.Set;

public class RecipeTestObjectBuilder {
    public static Recipe createTestRecipeWithName(String recipeName) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setMealType(MealType.VEGETARIAN);
        recipe.setPreparation("Some instructions");
        recipe.setRecipeIngredients(emptySet());
        recipe.setNumberOfServings(3);
        return recipe;
    }

    public static RecipeRequestDto createTestRecipeRequestDtoWithName(String recipeName) {
        RecipeRequestDto recipeRequest = new RecipeRequestDto();
        recipeRequest.setName(recipeName);
        recipeRequest.setMealType(MealType.VEGAN);
        recipeRequest.setPreparation("Some other instructions");
        recipeRequest.setIngredientIds(Set.of());
        recipeRequest.setNumberOfServings(4);
        return recipeRequest;
    }

    public static RecipeResponseDto createTestRecipeResponseDto(String recipeName) {
        RecipeResponseDto recipeResponse = new RecipeResponseDto();
        recipeResponse.setId(1L);
        recipeResponse.setName(recipeName);
        recipeResponse.setMealType(MealType.OTHER);
        recipeResponse.setPreparation("Other instructions");
        recipeResponse.setRecipeIngredients(emptySet());
        recipeResponse.setNumberOfServings(2);
        return recipeResponse;
    }

    public static RecipeSearchRequestDto createBlankTestRecipeSearchRequestDto() {
        RecipeSearchRequestDto recipeSearchDto = new RecipeSearchRequestDto();
        recipeSearchDto.setSearchCriteria(List.of());
        recipeSearchDto.setDataOption(DataOption.ANY);
        return recipeSearchDto;
    }

    public static RecipeSearchRequestDto createInvalidTestRecipeSearchRequestDto() {
        RecipeSearchRequestDto recipeSearchDto = new RecipeSearchRequestDto();
        recipeSearchDto.setSearchCriteria(List.of());
        recipeSearchDto.setDataOption(null);
        return recipeSearchDto;
    }
}
