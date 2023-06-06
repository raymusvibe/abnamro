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

public class EqualRule implements SearchRule {

    @Override
    public boolean ruleCanBeApplied(SearchOperation operation) {
        return operation == SearchOperation.EQUAL;
    }

    @Override
    public Predicate applyRule(
            CriteriaBuilder cb,
            SearchCriteriaDTO searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot) {
        if (searchCriteria.getFilterKey().equals(FilterKey.ingredient)) {
            return cb.equal(
                    cb.lower(joinedRoot.get(searchCriteria.getFilterKey().toString())), filterValue);
        }
        return cb.equal(
                cb.lower(
                        recipeRoot.get(searchCriteria.getFilterKey().toString()).as(String.class)),
                filterValue);
    }
}
