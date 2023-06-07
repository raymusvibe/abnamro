package com.abn.amro.search;

import com.abn.amro.dto.request.search.DataOption;
import com.abn.amro.dto.request.search.SearchCriteriaDto;
import com.abn.amro.model.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecificationBuilder {
    private final List<SearchCriteriaDto> parameters;

    public RecipeSpecificationBuilder() {
        this.parameters = new ArrayList<>();
    }

    public RecipeSpecificationBuilder(List<SearchCriteriaDto> searchCriterionRequests) {
        this.parameters = searchCriterionRequests;
    }

    public final RecipeSpecificationBuilder with(SearchCriteriaDto searchCriteria) {
        parameters.add(searchCriteria);
        return this;
    }

    public Specification<Recipe> build() {
        if (parameters.size() == 0) {
            return null;
        }

        Specification<Recipe> specification = new RecipeSpecification(parameters.get(0));
        for (int i = 1; i < parameters.size(); i++) {
            SearchCriteriaDto criteria = parameters.get(i);
            specification = criteria.getDataOption() == DataOption.ALL
                    ? Specification.where(specification).and(new RecipeSpecification(criteria))
                    : Specification.where(specification).or(new RecipeSpecification(criteria));
        }
        return specification;
    }
}
