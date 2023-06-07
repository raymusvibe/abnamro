package com.abn.amro.search;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.model.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecificationBuilderUnitTest {
    private List<SearchCriteriaRequestDto> parameters;

    @BeforeEach
    void before() {
        parameters = new ArrayList<>();
    }

    @Test
    public void RecipeSpecificationBuilder_WhenParamsAreEmpty_SpecificationIsNotPresent() {
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder(parameters);
        Specification<Recipe> specification = builder.build();
        assertNull(specification);
    }

    @Test
    public void RecipeSpecificationBuilder_WhenParamsNotEmpty_SpecificationIsNotPresent() {
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder(parameters);
        SearchCriteriaRequestDto searchCriteria = new SearchCriteriaRequestDto();
        parameters.add(searchCriteria);
        Specification<Recipe> specification = builder.build();
        assertNotNull(specification);
    }
}
