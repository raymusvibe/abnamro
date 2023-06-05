package com.abn.amro.controller;

import com.abn.amro.dto.request.IngredientRequestDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.IngredientResponseDTO;
import com.abn.amro.service.abstractions.IngredientService;
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
@RequestMapping(value = "api/v1/ingredient")
@Tag(name = "Ingredient Controller", description = "Create, read, update, delete operations for ingredients")
@Validated
public class IngredientController {
    private final Logger logger = LoggerFactory.getLogger(IngredientController.class);

    @Autowired
    private IngredientService ingredientService;

    @Operation(
            summary = "List ingredients",
            description = "Get paged list of ingredients",
            responses = {@ApiResponse(responseCode = "200")})
    @RequestMapping(method = RequestMethod.GET)
    public List<IngredientResponseDTO> getIngredientList(
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size) {
        logger.info("Getting ingredients");
        return ingredientService.getIngredients(page, size);
    }

    @Operation(
            summary = "Get ingredients by id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Operation successful"),
                @ApiResponse(responseCode = "404", description = "Ingredient not found")
            })
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public IngredientResponseDTO getIngredient(
            @Parameter(description = "Ingredient id", required = true) @PathVariable(name = "id") @Positive Long id) {
        logger.info("Getting ingredients by id {}", id);
        return ingredientService.getIngredientById(id);
    }

    @Operation(
            summary = "Create an ingredient",
            responses = {
                @ApiResponse(responseCode = "201", description = "Operation successful"),
                @ApiResponse(responseCode = "400", description = "Bad request")
            })
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEntityResponseDTO createIngredient(
            @Parameter(description = "Properties of Ingredient", required = true) @Valid @RequestBody
                    IngredientRequestDTO request) {
        logger.info("Creating ingredient {}", request);
        return ingredientService.createIngredient(request);
    }

    @Operation(
            summary = "Updating ingredient",
            responses = {
                @ApiResponse(responseCode = "200", description = "Ingredient updated"),
                @ApiResponse(responseCode = "400", description = "Bad request"),
                @ApiResponse(responseCode = "404", description = "Ingredient not found")
            })
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public IngredientResponseDTO updateIngredient(
            @Parameter(description = "Ingredient id", required = true) @PathVariable(name = "id") @Positive Long id,
            @Parameter(description = "Properties of the ingredient", required = true) @Valid @RequestBody
                    IngredientRequestDTO updateRequest) {
        logger.info("Updating an ingredient {}", updateRequest);
        return ingredientService.updateIngredient(id, updateRequest);
    }

    @Operation(
            summary = "Delete ingredient",
            responses = {
                @ApiResponse(responseCode = "200", description = "Ingredient deleted"),
                @ApiResponse(responseCode = "400", description = "Bad request"),
                @ApiResponse(responseCode = "404", description = "Ingredient not found")
            })
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void deleteIngredient(
            @Parameter(description = "Ingredient id", required = true) @PathVariable(name = "id") @Positive Long id) {
        logger.info("Deleting ingredient by id: {}", id);
        ingredientService.deleteIngredient(id);
    }
}
