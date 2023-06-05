package com.abn.amro.controller;

import com.abn.amro.dto.request.RecipeRequestDTO;
import com.abn.amro.dto.request.search.RecipeSearchDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.RecipeResponseDTO;
import com.abn.amro.service.abstractions.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/recipe")
@Tag(name = "Recipe Controller", description = "Create, read, update, delete operations for recipes")
@Validated
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Operation(
            summary = "List recipes",
            description = "Get a paged list of recipes",
            responses = {
                @ApiResponse(responseCode = "200"),
                @ApiResponse(responseCode = "400", description = "Bad request"),
            })
    @RequestMapping(method = RequestMethod.GET)
    public List<RecipeResponseDTO> getRecipes(
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size) {
        logger.info("Getting recipes");
        return recipeService.getRecipes(page, size);
    }

    @Operation(
            summary = "Get recipe by id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Operation successful"),
                @ApiResponse(responseCode = "400", description = "Bad request"),
                @ApiResponse(responseCode = "404", description = "Recipe not found")
            })
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public RecipeResponseDTO getRecipe(
            @Parameter(description = "Recipe id", required = true) @PathVariable(name = "id") @Positive Long id) {
        logger.info("Getting recipe by id {}", id);
        return recipeService.getRecipeById(id);
    }

    @Operation(
            summary = "Create a recipe",
            responses = {
                @ApiResponse(responseCode = "201", description = "Recipe created"),
                @ApiResponse(responseCode = "400", description = "Bad request")
            })
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEntityResponseDTO createRecipe(
            @Parameter(description = "Properties of new recipe", required = true) @Valid @RequestBody
                    RecipeRequestDTO request) {
        logger.info("Creating a new recipe {}", request);
        return recipeService.createRecipe(request);
    }

    @Operation(
            summary = "Updating a recipe",
            responses = {
                @ApiResponse(responseCode = "200", description = "Recipe updated"),
                @ApiResponse(responseCode = "400", description = "Bad request"),
                @ApiResponse(responseCode = "404", description = "Recipe not found")
            })
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public RecipeResponseDTO updateRecipe(
            @Parameter(description = "Recipe id", required = true) @PathVariable(name = "id") @Positive Long id,
            @Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody
                    RecipeRequestDTO updateRequest) {
        logger.info("Updating a recipe {}", updateRequest);
        return recipeService.updateRecipe(id, updateRequest);
    }

    @Operation(
            summary = "Delete a recipe",
            responses = {
                @ApiResponse(responseCode = "200", description = "Recipe deleted"),
                @ApiResponse(responseCode = "400", description = "Bad request"),
                @ApiResponse(responseCode = "404", description = "Recipe not found")
            })
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void deleteRecipe(
            @Parameter(description = "Recipe id", required = true) @PathVariable(name = "id") @Positive Long id) {
        logger.info("Deleting a recipe with id {}", id);
        recipeService.deleteRecipe(id);
    }

    @Operation(
            summary = "Searching recipes using provided criteria",
            responses = {@ApiResponse(responseCode = "200", description = "Successful search request")})
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    public List<RecipeResponseDTO> searchRecipe(
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size,
            @RequestBody @Valid RecipeSearchDTO searchRequest) {
        logger.info("Searching recipes using provided criteria");
        return recipeService.findBySearchCriteria(searchRequest, page, size);
    }
}
