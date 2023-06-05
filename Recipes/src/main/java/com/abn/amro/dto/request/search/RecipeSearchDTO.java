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
                    "The data option (determines wether to use OR or AND when there is more than one search criteria): ANY or ALL",
            example = "ALL")
    private DataOption dataOption;
}
