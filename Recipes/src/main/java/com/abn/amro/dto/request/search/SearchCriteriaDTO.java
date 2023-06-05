package com.abn.amro.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDTO {
    @Schema(
            description = "The parameter to search: name, numberOfServings, mealType, instructions or ingredient",
            example = "mealType")
    private FilterKey filterKey;

    @Schema(description = "The value to match with in the search", example = "VEGETARIAN")
    private Object value;

    @Schema(
            description = "The search operation to perform: CONTAINS, DOES_NOT_CONTAIN, EQUAL or NOT_EQUAL",
            example = "CONTAINS")
    private SearchOperation operation;

    @Schema(description = "The data option: ANY or ALL", example = "ALL", hidden = true)
    private DataOption dataOption;

    public SearchCriteriaDTO(FilterKey filterKey, SearchOperation operation, Object value) {
        super();
        this.filterKey = filterKey;
        this.operation = operation;
        this.value = value;
    }
}
