package com.abn.amro.service.abstractions;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.RecipeSearchDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import java.util.List;

public interface RecipeService {
    public List<RecipeResponseDto> getRecipes(int page, int size);

    public CreateEntityResponseDto createRecipe(RecipeRequestDto recipeRequest);

    public RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequest);

    public void deleteRecipe(Long recipeId);

    public RecipeResponseDto getRecipeById(Long recipeId);

    public List<RecipeResponseDto> findBySearchCriteria(RecipeSearchDto searchRequest, int page, int size);
}
