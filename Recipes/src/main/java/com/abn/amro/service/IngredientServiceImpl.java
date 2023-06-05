package com.abn.amro.service;

import com.abn.amro.dto.request.IngredientRequestDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.IngredientResponseDTO;
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
    public List<IngredientResponseDTO> getIngredients(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        List<Ingredient> ingredients = ingredientRepository.findAll(pageRequest).getContent();
        return ingredients.stream().map(this::mapIngredientToResponseDto).toList();
    }

    @Override
    public CreateEntityResponseDTO createIngredient(IngredientRequestDTO ingredientDTO) {
        Ingredient ingredient = modelMapper.map(ingredientDTO, Ingredient.class);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return new CreateEntityResponseDTO(savedIngredient.getId());
    }

    @Override
    public IngredientResponseDTO updateIngredient(Long ingredientId, IngredientRequestDTO ingredientDTO) {
        Ingredient ingredient = findIngredientById(ingredientId);
        ingredient.setIngredient(ingredientDTO.getIngredient());
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
    public IngredientResponseDTO getIngredientById(Long ingredientId) {
        Ingredient ingredient = findIngredientById(ingredientId);
        return new IngredientResponseDTO(ingredientId, ingredient.getIngredient());
    }

    private IngredientResponseDTO mapIngredientToResponseDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientResponseDTO.class);
    }

    private Ingredient findIngredientById(Long ingredientId) {
        return ingredientRepository
                .findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingredient id not found: " + ingredientId));
    }
}
