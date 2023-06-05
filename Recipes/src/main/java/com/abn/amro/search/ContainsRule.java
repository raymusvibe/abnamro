package com.abn.amro.search;

import com.abn.amro.dto.request.search.FilterKey;
import com.abn.amro.dto.request.search.SearchCriteriaDTO;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.search.abstractions.SearchRule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ContainsRule implements SearchRule {
    @Override
    public boolean canBeApplied(SearchOperation operation) {
        return operation == SearchOperation.CONTAINS;
    }

    @Override
    public Predicate applyRule(
            CriteriaBuilder cb,
            SearchCriteriaDTO searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot) {
        if (searchCriteria.getFilterKey().equals(FilterKey.ingredient)) {
            return cb.like(cb.lower(joinedRoot.get(searchCriteria.getFilterKey().toString())), "%" + filterValue + "%");
        }
        return cb.like(cb.lower(recipeRoot.get(searchCriteria.getFilterKey().toString())), "%" + filterValue + "%");
    }
}
