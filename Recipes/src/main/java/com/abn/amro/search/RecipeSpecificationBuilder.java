package com.abn.amro.search;

import com.abn.amro.dto.request.search.DataOption;
import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.model.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecificationBuilder {
    private final List<SearchCriteriaRequestDto> parameters;

    public RecipeSpecificationBuilder() {
        this.parameters = new ArrayList<>();
    }

    public RecipeSpecificationBuilder(List<SearchCriteriaRequestDto> searchCriterionRequests) {
        this.parameters = searchCriterionRequests;
    }

    public final RecipeSpecificationBuilder with(SearchCriteriaRequestDto searchCriteria) {
        parameters.add(searchCriteria);
        return this;
    }

    public Specification<Recipe> build() {
        if (parameters.isEmpty()) {
            return null;
        }

        Specification<Recipe> specification = new RecipeSpecification(parameters.get(0));
        for (int i = 1; i < parameters.size(); i++) {
            SearchCriteriaRequestDto criteria = parameters.get(i);
            specification = criteria.getDataOption() == DataOption.ALL
                    ? Specification.where(specification).and(new RecipeSpecification(criteria))
                    : Specification.where(specification).or(new RecipeSpecification(criteria));
        }
        return specification;
    }
}
