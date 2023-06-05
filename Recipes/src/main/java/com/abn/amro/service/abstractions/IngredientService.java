package com.abn.amro.service.abstractions;

import com.abn.amro.dto.request.IngredientRequestDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.IngredientResponseDTO;
import java.util.List;

public interface IngredientService {
    public List<IngredientResponseDTO> getIngredients(int page, int size);

    public CreateEntityResponseDTO createIngredient(IngredientRequestDTO ingredientDTO);

    public IngredientResponseDTO updateIngredient(Long ingredientId, IngredientRequestDTO ingredientDTO);

    public void deleteIngredient(Long id);

    public IngredientResponseDTO getIngredientById(Long id);
}
