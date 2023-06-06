package com.abn.amro.dto.request;

import com.abn.amro.model.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeRequestDto {
    @NotBlank
    @Size(max = 30, message = "Recipe name too long")
    @Schema(description = "The name of the recipe", example = "Lasagne")
    private String name;

    @NotNull @Schema(description = "List of ingredient id's", example = "[1,2]")
    private Set<Long> ingredientIds;

    @NotBlank
    @Size(max = 255, message = "Instructions too long")
    @Schema(description = "Instructions for preparing the dish", example = "Fry the meat for 35 minutes.")
    private String preparation;

    @Positive @Schema(description = "Number of servings for the meal", example = "4")
    private Integer numberOfServings;

    @Schema(description = "The type of meal you are preparing. VEGETARIAN, VEGAN, or OTHER", example = "VEGETARIAN")
    private MealType mealType;
}
