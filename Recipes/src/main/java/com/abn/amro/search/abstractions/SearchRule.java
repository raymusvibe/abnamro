package com.abn.amro.search.abstractions;

import com.abn.amro.dto.request.search.SearchCriteriaDTO;
import com.abn.amro.dto.request.search.SearchOperation;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface SearchRule {
    public boolean canBeApplied(SearchOperation operation);

    public Predicate applyRule(
            CriteriaBuilder cb,
            SearchCriteriaDTO searchCriteria,
            String filterValue,
            Root<Recipe> recipeRoot,
            Join<Recipe, Ingredient> joinedRoot);
}
