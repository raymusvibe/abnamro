package com.abn.amro.dto.response;

import com.abn.amro.model.MealType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecipeResponseDTO {
    private Long id;
    private String name;
    private String preparation;

    @JsonProperty("ingredients")
    private Set<IngredientResponseDTO> recipeIngredients;

    private int numberOfServings;
    private MealType mealType;
}
