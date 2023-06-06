package com.abn.amro.service;

import com.abn.amro.dto.request.RecipeRequestDTO;
import com.abn.amro.dto.request.search.RecipeSearchDTO;
import com.abn.amro.dto.request.search.SearchCriteriaDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.RecipeResponseDTO;
import com.abn.amro.exceptions.NotFoundException;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.repository.IngredientRepository;
import com.abn.amro.repository.RecipeRepository;
import com.abn.amro.search.RecipeSpecificationBuilder;
import com.abn.amro.service.abstractions.RecipeService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private final RecipeRepository recipeRepository;

    @Autowired
    private final IngredientRepository ingredientRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public List<RecipeResponseDTO> getRecipes(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        List<Recipe> recipes = recipeRepository.findAll(pageRequest).getContent();
        return recipes.stream().map(this::mapRecipeToResponseDto).toList();
    }

    @Override
    public CreateEntityResponseDTO createRecipe(RecipeRequestDTO recipeDto) {
        Recipe recipe = mapRecipeRequestDtoToEntity(recipeDto);
        Set<Ingredient> ingredients = getIngredientsByIds(recipeDto.getIngredientIds());
        recipe.setRecipeIngredients(ingredients);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return new CreateEntityResponseDTO(savedRecipe.getId());
    }

    @Override
    public RecipeResponseDTO updateRecipe(Long recipeId, RecipeRequestDTO recipeDto) {
        Recipe recipe = findRecipeById(recipeId);
        Set<Ingredient> ingredients = getIngredientsByIds(recipeDto.getIngredientIds());
        recipe.setName(recipeDto.getName());
        recipe.setPreparation(recipeDto.getPreparation());
        recipe.setNumberOfServings(recipeDto.getNumberOfServings());
        recipe.setRecipeIngredients(ingredients);
        recipe.setMealType(recipeDto.getMealType());
        Recipe savedRecipe = recipeRepository.save(recipe);
        return mapRecipeToResponseDto(savedRecipe);
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new NotFoundException("Recipe id not found: " + recipeId);
        }
        recipeRepository.deleteById(recipeId);
    }

    @Override
    public RecipeResponseDTO getRecipeById(Long recipeId) {
        Recipe recipe = findRecipeById(recipeId);
        return mapRecipeToResponseDto(recipe);
    }

    @Override
    public List<RecipeResponseDTO> findBySearchCriteria(RecipeSearchDTO searchRequest, int page, int size) {
        RecipeSpecificationBuilder specificationBuilder = new RecipeSpecificationBuilder();
        List<SearchCriteriaDTO> criteriaList = searchRequest.getSearchCriteria();
        if (criteriaList != null) {
            criteriaList.forEach(criteria -> {
                criteria.setDataOption(searchRequest.getDataOption());
                specificationBuilder.with(criteria);
            });
        }
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        List<Recipe> searchResult = recipeRepository
                .findAll(specificationBuilder.build(), pageRequest)
                .getContent();
        return searchResult.stream().map(this::mapRecipeToResponseDto).toList();
    }

    private Recipe findRecipeById(Long recipeId) {
        return recipeRepository
                .findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe id not found: " + recipeId));
    }

    private Set<Ingredient> getIngredientsByIds(List<Long> ingredientIds) {
        return ingredientIds.stream().map(this::findIngredientById).collect(Collectors.toSet());
    }

    private Ingredient findIngredientById(Long id) {
        return ingredientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Ingredient id not found: " + id));
    }

    private RecipeResponseDTO mapRecipeToResponseDto(Recipe recipe) {
        return modelMapper.map(recipe, RecipeResponseDTO.class);
    }

    private Recipe mapRecipeRequestDtoToEntity(RecipeRequestDTO recipe) {
        return modelMapper.map(recipe, Recipe.class);
    }
}
