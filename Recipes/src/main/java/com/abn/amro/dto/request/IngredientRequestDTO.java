package com.abn.amro.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IngredientRequestDTO {
    @NotBlank
    @Size(max = 30, message = "Ingredient name too long")
    @Schema(description = "The name of the ingredient", example = "Egg")
    private String ingredient;
}
