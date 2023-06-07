package com.abn.amro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.abn.amro.model.Recipe;
import com.abn.amro.utils.RecipeTestObjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @AfterEach
    public void after() {
        recipeRepository.deleteAll();
    }

    @Test
    void RecipeRepository_WhenRecipeSaved_CanBeRetrieved() {
        String recipeName = "Beef stew";
        Recipe recipe = RecipeTestObjectBuilder.createTestRecipeWithName(recipeName);

        Recipe saveRecipe = recipeRepository.save(recipe);

        assertNotNull(saveRecipe);
        assert (saveRecipe.getName()).equals(recipeName);
    }

    @Test
    void RecipeRepository_WhenMultipleRecipesSaved_ListCanBeRetrieved() {
        String firstRecipeName = "Beef stew";
        String secondRecipeName = "Lamb stew";
        Recipe firstRecipe = RecipeTestObjectBuilder.createTestRecipeWithName(firstRecipeName);
        Recipe secondRecipe = RecipeTestObjectBuilder.createTestRecipeWithName(secondRecipeName);

        recipeRepository.save(firstRecipe);
        recipeRepository.save(secondRecipe);

        assertEquals(2, recipeRepository.findAll().size());
    }
}
