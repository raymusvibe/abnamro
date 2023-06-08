package com.abn.amro.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abn.amro.controller.RecipeController;
import com.abn.amro.dto.request.RecipeRequestDto;
import com.abn.amro.dto.request.search.RecipeSearchRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.RecipeResponseDto;
import com.abn.amro.service.abstractions.RecipeService;
import com.abn.amro.utils.RecipeTestObjectBuilder;
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
class RecipeControllerUnitTest {
    @MockBean
    RecipeService recipeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void RecipeController_WhenGetRecipes_ListResponseIsCorrect() throws Exception {
        RecipeResponseDto recipeDTO = RecipeTestObjectBuilder.createTestRecipeResponseDto("Rendang");
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
    void RecipeController_WhenGetRecipeById_ResponseMatches() throws Exception {
        RecipeResponseDto recipeDTO = RecipeTestObjectBuilder.createTestRecipeResponseDto("Sushi");
        Mockito.when(recipeService.getRecipeById(1L)).thenReturn(recipeDTO);

        MvcResult result = mockMvc.perform(get("/api/v1/recipe/1"))
                .andExpect(status().isOk())
                .andReturn();

        RecipeResponseDto createdRecipeResponseDto = UtilityConvertor.getResponse(result, RecipeResponseDto.class);
        assertEquals(createdRecipeResponseDto.getName(), recipeDTO.getName());
    }

    @Test
    void RecipeController_WhenCreateRecipe_201Response() throws Exception {
        RecipeRequestDto recipeRequestDTO = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Meatloaf");
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
    void RecipeController_WhenUpdateRecipe_200Response() throws Exception {
        RecipeResponseDto recipeResponseDTO = RecipeTestObjectBuilder.createTestRecipeResponseDto("Sushi");
        RecipeRequestDto recipeRequestDTO = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName("Sushi");
        Mockito.when(recipeService.updateRecipe(anyLong(), any())).thenReturn(recipeResponseDTO);

        mockMvc.perform(put("/api/v1/recipe/1")
                        .content(UtilityConvertor.convertToJson(recipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void RecipeController_WhenInvalidRequest_400Response() throws Exception {
        RecipeRequestDto invalidRecipeRequestDTO = RecipeTestObjectBuilder.createTestRecipeRequestDtoWithName(null);

        mockMvc.perform(post("/api/v1/recipe")
                        .content(UtilityConvertor.convertToJson(invalidRecipeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void RecipeController_WhenDeleteRecipe_200Response() throws Exception {
        doNothing().when(recipeService).deleteRecipe(anyLong());

        mockMvc.perform(delete("/api/v1/recipe/1")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void RecipeController_WhenValidSearchRequest_200Response() throws Exception {
        RecipeSearchRequestDto recipeSearchDto = RecipeTestObjectBuilder.createBlankTestRecipeSearchRequestDto();

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
    void RecipeController_WhenInvalidSearchRequest_400Response() throws Exception {
        RecipeSearchRequestDto invalidRecipeSearchDto =
                RecipeTestObjectBuilder.createInvalidTestRecipeSearchRequestDto();

        mockMvc.perform(post("/api/v1/recipe/search")
                        .content(UtilityConvertor.convertToJson(invalidRecipeSearchDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
