package com.abn.amro.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchDto {
    @NotNull @Schema(description = "List of search criteria to search on")
    private List<SearchCriteriaDto> searchCriteria;

    @NotNull @Schema(
            description =
                    "The data option (determines whether to use OR or AND when there are multiple criteria): ANY or ALL",
            example = "ALL")
    private DataOption dataOption;
}
