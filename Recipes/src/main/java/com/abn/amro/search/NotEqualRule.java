package com.abn.amro.search;

import com.abn.amro.dto.request.search.SearchCriteriaDto;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.search.abstractions.SearchRule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class NotEqualRule implements SearchRule {
    @Override
    public boolean canRuleBeApplied(SearchOperation operation) {
        return operation == SearchOperation.NOT_EQUAL;
    }

    @Override
    public Predicate applyRule(
            CriteriaBuilder cb,
            SearchCriteriaDto searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot) {
        switch (searchCriteria.getFilterKey()) {
            case ingredient:
                return cb.notEqual(
                        cb.lower(joinedRoot.get(searchCriteria.getFilterKey().toString())), filterValue);
            default:
                return cb.notEqual(
                        cb.lower(recipeRoot
                                .get(searchCriteria.getFilterKey().toString())
                                .as(String.class)),
                        filterValue);
        }
    }
}
