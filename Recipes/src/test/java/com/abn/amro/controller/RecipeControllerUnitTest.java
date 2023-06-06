package com.abn.amro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abn.amro.dto.request.RecipeRequestDTO;
import com.abn.amro.dto.response.CreateEntityResponseDTO;
import com.abn.amro.dto.response.RecipeResponseDTO;
import com.abn.amro.service.abstractions.RecipeService;
import com.abn.amro.utils.RecipeTestBuilder;
import com.abn.amro.utils.UtilityConvertor;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(RecipeController.class)
public class RecipeControllerUnitTest {
    @MockBean
    RecipeService recipeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void RecipeController_WhenGetRecipes_ListResponseIsCorrect() throws Exception {
        RecipeResponseDTO recipeDTO = RecipeTestBuilder.createTestRecipeResponseDTO("Rendang");
        List<RecipeResponseDTO> recipesDTOList = List.of(recipeDTO);
        Mockito.when(recipeService.getRecipes(anyInt(), anyInt())).thenReturn(recipesDTOList);

        MvcResult result = mockMvc.perform(get("/api/v1/recipe"))
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponseDTO> recipeResponseDTOList = UtilityConvertor.getResponseAsList(result, RecipeResponseDTO.class);
        assertEquals(recipesDTOList.size(), recipeResponseDTOList.size());
    }

    @Test
    public void RecipeController_WhenGetRecipeById_ResponseMatches() throws Exception {
        RecipeResponseDTO recipeDTO = RecipeTestBuilder.createTestRecipeResponseDTO("Sushi");
        Mockito.when(recipeService.getRecipeById(1L)).thenReturn(recipeDTO);

        MvcResult result = mockMvc.perform(get("/api/v1/recipe/1"))
                .andExpect(status().isOk())
                .andReturn();

        RecipeResponseDTO createdRecipeResponseDTO = UtilityConvertor.getResponse(result, RecipeResponseDTO.class);
        assertEquals(createdRecipeResponseDTO.getName(), recipeDTO.getName());
    }

    @Test
    public void RecipeController_WhenCreateRecipe_201Response() throws Exception {
        RecipeRequestDTO recipeRequestDTO = RecipeTestBuilder.createTestRecipeRequestDTO("Meatloaf");
        Mockito.when(recipeService.createRecipe(any())).thenReturn(new CreateEntityResponseDTO(1L));

        MvcResult result = mockMvc.perform(post("/api/v1/recipe")
                        .content(UtilityConvertor.convertToJson(recipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CreateEntityResponseDTO createEntityResponseDTO =
                UtilityConvertor.getResponse(result, CreateEntityResponseDTO.class);
        assertEquals(createEntityResponseDTO.getId(), 1L);
    }

    @Test
    public void RecipeController_WhenUpdateRecipe_200Response() throws Exception {
        RecipeResponseDTO recipeResponseDTO = RecipeTestBuilder.createTestRecipeResponseDTO("Sushi");
        RecipeRequestDTO recipeRequestDTO = RecipeTestBuilder.createTestRecipeRequestDTO("Sushi");
        Mockito.when(recipeService.updateRecipe(anyLong(), any())).thenReturn(recipeResponseDTO);

        mockMvc.perform(put("/api/v1/recipe/1")
                        .content(UtilityConvertor.convertToJson(recipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void RecipeController_WhenInvalidRequest_404Response() throws Exception {
        RecipeRequestDTO recipeRequestDTO = RecipeTestBuilder.createTestRecipeRequestDTO("Burgers");
        recipeRequestDTO.setName(null);

        mockMvc.perform(post("/api/v1/recipe")
                        .content(UtilityConvertor.convertToJson(recipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void RecipeController_WhenDeleteRecipe_200Response() throws Exception {
        doNothing().when(recipeService).deleteRecipe(anyLong());

        mockMvc.perform(delete("/api/v1/recipe/1")).andExpect(status().isOk()).andReturn();
    }
}
