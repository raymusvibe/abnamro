package com.abn.amro.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchDTO {
    @Schema(description = "List of search criteria to search on")
    private List<SearchCriteriaDTO> searchCriteria;

    @Schema(
            description =
                    "The data option (determines whether to use OR or AND when there are multiple criteria): ANY or ALL",
            example = "ALL")
    private DataOption dataOption;
}
