package com.abn.amro.search.abstractions;

import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class SearchRule {
    protected SearchOperation searchOperationForRule;

    public boolean canRuleBeApplied(SearchOperation operation) {
        return searchOperationForRule == operation;
    }

    public abstract Predicate applyRule(
            CriteriaBuilder cb,
            SearchCriteriaRequestDto searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot);
}
