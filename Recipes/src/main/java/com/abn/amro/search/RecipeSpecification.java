package com.abn.amro.search;

import com.abn.amro.config.DatabaseConfig;
import com.abn.amro.dto.request.search.SearchCriteriaRequestDto;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.search.abstractions.SearchRule;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification implements Specification<Recipe> {
    private final transient SearchCriteriaRequestDto searchCriteria;

    private static final List<SearchRule> searchRules = new ArrayList<>();

    public RecipeSpecification(SearchCriteriaRequestDto searchCriteria) {
        this.searchCriteria = searchCriteria;
        populateRuleList();
    }

    @Override
    public Predicate toPredicate(Root<Recipe> recipeRoot, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        String filterValue = searchCriteria.getValue().toString().toLowerCase();
        Join<Recipe, Ingredient> joinedRoot = recipeRoot.join(DatabaseConfig.INGREDIENTS_JOIN_TABLE_NAME);
        return searchRules.stream()
                .filter(searchRule -> searchRule.canRuleBeApplied(searchCriteria.getOperation()))
                .findFirst()
                .map(searchRule ->
                        searchRule.applyRule(criteriaBuilder, searchCriteria, filterValue, recipeRoot, joinedRoot))
                .orElse(null);
    }

    private void populateRuleList() {
        searchRules.add(new ContainsRule());
        searchRules.add(new DoesNotContainRule());
        searchRules.add(new EqualRule());
        searchRules.add(new NotEqualRule());
    }
}
