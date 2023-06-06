package com.abn.amro.controller;

import com.abn.amro.service.abstractions.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(IngredientController.class)
public class IngredientControllerUnitTest {
    @MockBean
    IngredientService ingredientService;

    @Autowired
    MockMvc mockMvc;


}
