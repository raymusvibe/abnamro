package com.abn.amro.search;

import com.abn.amro.dto.request.search.DataOption;
import com.abn.amro.dto.request.search.FilterKey;
import com.abn.amro.dto.request.search.SearchCriteriaDto;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecificationBuilder {
    private final List<SearchCriteriaDto> parameters;

    public RecipeSpecificationBuilder() {
        this.parameters = new ArrayList<>();
    }

    public final RecipeSpecificationBuilder with(FilterKey key, SearchOperation operation, Object value) {
        parameters.add(new SearchCriteriaDto(key, operation, value));
        return this;
    }

    public final RecipeSpecificationBuilder with(SearchCriteriaDto searchCriteria) {
        parameters.add(searchCriteria);
        return this;
    }

    public Specification<Recipe> build() {
        if (parameters.size() == 0) {
            return null;
        }

        Specification<Recipe> result = new RecipeSpecification(parameters.get(0));
        for (int index = 1; index < parameters.size(); index++) {
            SearchCriteriaDto criteria = parameters.get(index);
            result = criteria.getDataOption() == DataOption.ALL
                    ? Specification.where(result).and(new RecipeSpecification(criteria))
                    : Specification.where(result).or(new RecipeSpecification(criteria));
        }
        return result;
    }
}
