package com.abn.amro.service.abstractions;

import com.abn.amro.dto.request.IngredientRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.IngredientResponseDto;
import java.util.List;

public interface IngredientService {
    public List<IngredientResponseDto> getIngredients(int page, int size);

    public CreateEntityResponseDto createIngredient(IngredientRequestDto ingredientDTO);

    public IngredientResponseDto updateIngredient(Long ingredientId, IngredientRequestDto ingredientDTO);

    public void deleteIngredient(Long id);

    public IngredientResponseDto getIngredientById(Long id);
}
