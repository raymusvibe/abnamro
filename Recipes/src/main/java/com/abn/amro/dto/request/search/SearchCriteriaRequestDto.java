package com.abn.amro.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriteriaRequestDto {
    @NotBlank
    @Schema(
            description = "The parameter to search: name, numberOfServings, mealType, instructions or ingredientName",
            example = "mealType")
    private String filterKey;

    @NotBlank
    @Schema(description = "The filter value to match with in the search", example = "VEGETARIAN")
    private Object value;

    @Schema(
            description = "The search operation to perform: CONTAINS, DOES_NOT_CONTAIN, EQUAL or NOT_EQUAL",
            example = "CONTAINS")
    private SearchOperation operation;

    @Schema(description = "The data option: ANY or ALL", example = "ALL", hidden = true)
    private DataOption dataOption;

    public SearchCriteriaRequestDto(String filterKey, SearchOperation operation, Object value) {
        this.filterKey = filterKey;
        this.operation = operation;
        this.value = value;
    }
}
