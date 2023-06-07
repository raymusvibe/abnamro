package com.abn.amro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abn.amro.dto.request.IngredientRequestDto;
import com.abn.amro.dto.response.CreateEntityResponseDto;
import com.abn.amro.dto.response.IngredientResponseDto;
import com.abn.amro.service.abstractions.IngredientService;
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

@WebMvcTest(IngredientController.class)
public class IngredientControllerUnitTest {
    @MockBean
    IngredientService ingredientService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void IngredientController_WhenGetIngredients_ListSizeResponseCorrect() throws Exception {
        IngredientResponseDto firstIngredientResponseDto = new IngredientResponseDto(1L, "vegetables");
        IngredientResponseDto secondIngredientResponseDto = new IngredientResponseDto(1L, "salsa");
        List<IngredientResponseDto> ingredientResponseDtoList =
                List.of(firstIngredientResponseDto, secondIngredientResponseDto);
        Mockito.when(ingredientService.getIngredients(anyInt(), anyInt())).thenReturn(ingredientResponseDtoList);

        MvcResult result = mockMvc.perform(get("/api/v1/ingredient"))
                .andExpect(status().isOk())
                .andReturn();

        List<IngredientResponseDto> fetchedIngredientResponseDto =
                UtilityConvertor.getResponseAsList(result, IngredientResponseDto.class);
        assertEquals(ingredientResponseDtoList.size(), fetchedIngredientResponseDto.size());
    }

    @Test
    public void IngredientController_WhenGetIngredientById_ResponseMatches() throws Exception {
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(1L, "tomato");
        Mockito.when(ingredientService.getIngredientById(anyLong())).thenReturn(ingredientResponseDto);

        MvcResult result = mockMvc.perform(get("/api/v1/ingredient/1"))
                .andExpect(status().isOk())
                .andReturn();

        IngredientResponseDto fetchedIngredientResponseDto =
                UtilityConvertor.getResponse(result, IngredientResponseDto.class);
        assertEquals(ingredientResponseDto.getIngredient(), fetchedIngredientResponseDto.getIngredient());
    }

    @Test
    public void IngredientController_WhenCreateIngredient_201Response() throws Exception {
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(1L, "fish");
        Mockito.when(ingredientService.createIngredient(any())).thenReturn(new CreateEntityResponseDto(1L));

        MvcResult result = mockMvc.perform(post("/api/v1/ingredient")
                        .content(UtilityConvertor.convertToJson(ingredientResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CreateEntityResponseDto createEntityResponseDTO =
                UtilityConvertor.getResponse(result, CreateEntityResponseDto.class);
        assertEquals(ingredientResponseDto.getId(), createEntityResponseDTO.getId());
    }

    @Test
    public void IngredientController_WhenUpdateIngredient_200Response() throws Exception {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("eggs");
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(1L, "eggs");
        Mockito.when(ingredientService.updateIngredient(anyLong(), any())).thenReturn(ingredientResponseDto);

        mockMvc.perform(put("/api/v1/ingredient/1")
                        .content(UtilityConvertor.convertToJson(ingredientRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void IngredientController_WhenInvalidRequest_404Response() throws Exception {
        IngredientRequestDto invalidIngredientRequestDto = new IngredientRequestDto(null);

        mockMvc.perform(post("/api/v1/ingredient")
                        .content(UtilityConvertor.convertToJson(invalidIngredientRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void IngredientController_WhenDeleteIngredient_200Response() throws Exception {
        doNothing().when(ingredientService).deleteIngredient(anyLong());

        mockMvc.perform(delete("/api/v1/ingredient/1"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
