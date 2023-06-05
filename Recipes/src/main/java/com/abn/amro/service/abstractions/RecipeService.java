package com.abn.amro.service.abstractions;

import com.abn.amro.dto.request.RecipeRequestDTO;
import com.abn.amro.dto.request.search.RecipeSearchDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.RecipeResponseDTO;
import java.util.List;

public interface RecipeService {
    public List<RecipeResponseDTO> getRecipes(int page, int size);

    public CreateEntityResponseDTO createRecipe(RecipeRequestDTO recipe);

    public RecipeResponseDTO updateRecipe(Long recipeId, RecipeRequestDTO recipe);

    public void deleteRecipe(Long recipeId);

    public RecipeResponseDTO getRecipeById(Long recipeId);

    public List<RecipeResponseDTO> findBySearchCriteria(RecipeSearchDTO searchRequest, int page, int size);
}
