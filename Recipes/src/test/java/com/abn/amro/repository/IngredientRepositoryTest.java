package com.abn.amro.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.abn.amro.model.Ingredient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class IngredientRepositoryTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void IngredientRepository_WhenIngredientSaved_CanBeRetrieved() {
        String ingredient = "salsa";
        Ingredient newIngredient = new Ingredient(ingredient);

        Ingredient savedIngredient = ingredientRepository.save(newIngredient);

        assertNotNull(savedIngredient);
        assert (savedIngredient.getIngredient()).equals(ingredient);
    }

    @Test
    public void IngredientRepository_WhenMultipleIngredientsSaved_CanBeRetrieved() {
        String firstIngredientName = "salsa";
        String secondIngredientName = "tomato";
        Ingredient firstIngredient = new Ingredient(firstIngredientName);
        Ingredient secondIngredient = new Ingredient(secondIngredientName);

        ingredientRepository.save(firstIngredient);
        ingredientRepository.save(secondIngredient);

        assertEquals(2, ingredientRepository.findAll().size());
    }

    @Test
    public void IngredientRepository_WhenSameIngredientName_DataIntegrityViolationException() {
        String ingredient = "tomato";
        Ingredient firstIngredient = new Ingredient(ingredient);
        Ingredient secondIngredient = new Ingredient(ingredient);

        ingredientRepository.save(firstIngredient);

        assertThrows(DataIntegrityViolationException.class, () -> ingredientRepository.saveAndFlush(secondIngredient));
    }
}
