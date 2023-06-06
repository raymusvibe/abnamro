package com.abn.amro.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.test.web.servlet.MvcResult;

public class UtilityConvertor {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T getResponse(MvcResult result, Class<T> clazz) throws IOException {
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructType(clazz));
    }

    public static <T> List<T> getResponseAsList(MvcResult result, Class<T> clazz) throws IOException {
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
