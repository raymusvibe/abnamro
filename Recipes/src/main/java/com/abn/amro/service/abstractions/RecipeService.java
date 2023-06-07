package com.abn.amro.service.abstractions;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.RecipeSearchRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import java.util.List;

public interface RecipeService {
    public List<RecipeResponseDto> getRecipes(int page, int size);

    public CreateEntityResponseDto createRecipe(RecipeRequestDto recipeRequest);

    public RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequest);

    public void deleteRecipe(Long recipeId);

    public RecipeResponseDto getRecipeById(Long recipeId);

    public List<RecipeResponseDto> findBySearchCriteria(RecipeSearchRequestDto searchRequest, int page, int size);
}
