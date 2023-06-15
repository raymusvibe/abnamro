package com.abn.amro.search;

import com.abn.amro.config.DatabaseConfig;
import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.search.abstractions.SearchRule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ContainRule extends SearchRule {
    public ContainRule() {
        this.searchOperationForRule = SearchOperation.CONTAIN;
    }

    @Override
    public Predicate applyRule(
            CriteriaBuilder criteriaBuilder,
            SearchCriteriaRequestDto searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot) {
        if (searchCriteria.getFilterKey().equals(DatabaseConfig.INGREDIENTS_COLUMN_NAME)) {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(
                            joinedRoot.get(searchCriteria.getFilterKey().toString())),
                    "%" + filterValue + "%");
        } else {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(recipeRoot
                            .get(searchCriteria.getFilterKey().toString())
                            .as(String.class)),
                    "%" + filterValue + "%");
        }
    }
}
