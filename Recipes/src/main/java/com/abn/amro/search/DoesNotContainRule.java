package com.abn.amro.search;

import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.search.abstractions.SearchRule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DoesNotContainRule implements SearchRule {
    @Override
    public boolean canRuleBeApplied(SearchOperation operation) {
        return operation == SearchOperation.DOES_NOT_CONTAIN;
    }

    @Override
    public Predicate applyRule(
            CriteriaBuilder cb,
            SearchCriteriaRequestDto searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot) {
        if (searchCriteria.getFilterKey().equals("ingredientName")) {
            return cb.notLike(
                    cb.lower(joinedRoot.get(searchCriteria.getFilterKey().toString())), "%" + filterValue + "%");
        } else {
            return cb.notLike(
                    cb.lower(recipeRoot
                            .get(searchCriteria.getFilterKey().toString())
                            .as(String.class)),
                    "%" + filterValue + "%");
        }
    }
}
