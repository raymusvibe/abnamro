package com.abn.amro.search;

import com.abn.amro.dto.request.search.SearchCriteriaDTO;
import com.abn.amro.model.Ingredient;
import com.abn.amro.model.Recipe;
import com.abn.amro.search.abstractions.SearchRule;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification implements Specification<Recipe> {
    private final SearchCriteriaDTO searchCriteria;
    private static final List<SearchRule> searchRules = new ArrayList<>();

    @Autowired
    private ContainsRule containsSearchRule;

    @Autowired
    private DoesNotContainRule doesNotContainSearchRule;

    @Autowired
    private EqualRule equalSearchRule;

    @Autowired
    private NotEqualRule notEqualRule;

    public RecipeSpecification(SearchCriteriaDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
        populateRuleList();
    }

    @Override
    public Predicate toPredicate(Root<Recipe> recipeRoot, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        String filterValue = searchCriteria.getValue().toString().toLowerCase();
        Join<Recipe, Ingredient> joinedRoot = recipeRoot.join("recipeIngredients", JoinType.INNER);
        return searchRules.stream()
                .filter(searchRule -> searchRule.canBeApplied(searchCriteria.getOperation()))
                .findFirst()
                .map(searchRule ->
                        searchRule.applyRule(criteriaBuilder, searchCriteria, filterValue, recipeRoot, joinedRoot))
                .orElse(null);
    }

    private void populateRuleList() {
        searchRules.add(containsSearchRule);
        searchRules.add(doesNotContainSearchRule);
        searchRules.add(equalSearchRule);
        searchRules.add(notEqualRule);
    }
}
