package com.abn.amro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.RecipeSearchDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
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
        RecipeResponseDto recipeDTO = RecipeTestBuilder.createTestRecipeResponseDto("Rendang");
        List<RecipeResponseDto> recipesDTOList = List.of(recipeDTO);
        Mockito.when(recipeService.getRecipes(anyInt(), anyInt())).thenReturn(recipesDTOList);

        MvcResult result = mockMvc.perform(get("/api/v1/recipe"))
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponseDto> recipeResponseDtoList =
                UtilityConvertor.getResponseAsList(result, RecipeResponseDto.class);
        assertEquals(recipesDTOList.size(), recipeResponseDtoList.size());
    }

    @Test
    public void RecipeController_WhenGetRecipeById_ResponseMatches() throws Exception {
        RecipeResponseDto recipeDTO = RecipeTestBuilder.createTestRecipeResponseDto("Sushi");
        Mockito.when(recipeService.getRecipeById(1L)).thenReturn(recipeDTO);

        MvcResult result = mockMvc.perform(get("/api/v1/recipe/1"))
                .andExpect(status().isOk())
                .andReturn();

        RecipeResponseDto createdRecipeResponseDto = UtilityConvertor.getResponse(result, RecipeResponseDto.class);
        assertEquals(createdRecipeResponseDto.getName(), recipeDTO.getName());
    }

    @Test
    public void RecipeController_WhenCreateRecipe_201Response() throws Exception {
        RecipeRequestDto recipeRequestDTO = RecipeTestBuilder.createTestRecipeRequestDto("Meatloaf");
        Mockito.when(recipeService.createRecipe(any())).thenReturn(new CreateEntityResponseDto(1L));

        MvcResult result = mockMvc.perform(post("/api/v1/recipe")
                        .content(UtilityConvertor.convertToJson(recipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CreateEntityResponseDto createEntityResponseDTO =
                UtilityConvertor.getResponse(result, CreateEntityResponseDto.class);
        assertEquals(1L, createEntityResponseDTO.getId());
    }

    @Test
    public void RecipeController_WhenUpdateRecipe_200Response() throws Exception {
        RecipeResponseDto recipeResponseDTO = RecipeTestBuilder.createTestRecipeResponseDto("Sushi");
        RecipeRequestDto recipeRequestDTO = RecipeTestBuilder.createTestRecipeRequestDto("Sushi");
        Mockito.when(recipeService.updateRecipe(anyLong(), any())).thenReturn(recipeResponseDTO);

        mockMvc.perform(put("/api/v1/recipe/1")
                        .content(UtilityConvertor.convertToJson(recipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void RecipeController_WhenInvalidRequest_404Response() throws Exception {
        RecipeRequestDto invalidRecipeRequestDTO = RecipeTestBuilder.createTestRecipeRequestDto(null);

        mockMvc.perform(post("/api/v1/recipe")
                        .content(UtilityConvertor.convertToJson(invalidRecipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void RecipeController_WhenDeleteRecipe_200Response() throws Exception {
        doNothing().when(recipeService).deleteRecipe(anyLong());

        mockMvc.perform(delete("/api/v1/recipe/1")).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void RecipeController_ValidSearchRequest_200Response() throws Exception {
        RecipeSearchDto recipeSearchDto = RecipeTestBuilder.createBlankRecipeSearchDto();

        MvcResult result = mockMvc.perform(post("/api/v1/recipe/search")
                        .content(UtilityConvertor.convertToJson(recipeSearchDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponseDto> recipeResponseDtoList =
                UtilityConvertor.getResponseAsList(result, RecipeResponseDto.class);
        assertTrue(recipeResponseDtoList.isEmpty());
    }

    @Test
    public void RecipeController_InvalidSearchRequest_400Response() throws Exception {
        RecipeSearchDto invalidRecipeSearchDto = RecipeTestBuilder.createInvalidRecipeSearchDto();

        mockMvc.perform(post("/api/v1/recipe/search")
                        .content(UtilityConvertor.convertToJson(invalidRecipeSearchDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
