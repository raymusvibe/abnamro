package com.abn.amro.service;

import com.abn.amro.dto.request.IngredientRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.IngredientResponseDto;
import com.abn.amro.exceptions.NotFoundException;
import com.abn.amro.model.Ingredient;
import com.abn.amro.repository.IngredientRepository;
import com.abn.amro.service.abstractions.IngredientService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<IngredientResponseDto> getIngredients(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        List<Ingredient> ingredients = ingredientRepository.findAll(pageRequest).getContent();
        return ingredients.stream().map(this::mapIngredientToResponseDto).toList();
    }

    @Override
    public CreateEntityResponseDto createIngredient(IngredientRequestDto ingredientDTO) {
        Ingredient ingredient = modelMapper.map(ingredientDTO, Ingredient.class);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return new CreateEntityResponseDto(savedIngredient.getId());
    }

    @Override
    public IngredientResponseDto updateIngredient(Long ingredientId, IngredientRequestDto ingredientDTO) {
        Ingredient ingredient = findIngredientById(ingredientId);
        ingredient.setIngredientName(ingredientDTO.getIngredientName());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return mapIngredientToResponseDto(savedIngredient);
    }

    @Override
    public void deleteIngredient(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new NotFoundException("Ingredient id not found: " + ingredientId);
        }
        ingredientRepository.deleteById(ingredientId);
    }

    @Override
    public IngredientResponseDto getIngredientById(Long ingredientId) {
        Ingredient ingredient = findIngredientById(ingredientId);
        return new IngredientResponseDto(ingredientId, ingredient.getIngredientName());
    }

    private IngredientResponseDto mapIngredientToResponseDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientResponseDto.class);
    }

    private Ingredient findIngredientById(Long ingredientId) {
        return ingredientRepository
                .findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingredient id not found: " + ingredientId));
    }
}
