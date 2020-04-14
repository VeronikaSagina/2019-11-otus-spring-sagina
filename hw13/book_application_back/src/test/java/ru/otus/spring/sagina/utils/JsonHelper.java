package ru.otus.spring.sagina.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class JsonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> getResultList(ResultActions action, Class<T> clazz)
            throws UnsupportedEncodingException, JsonProcessingException {
        String content = getContent(action);
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    public static <T> T getResult(ResultActions action, Class<T> clazz)
            throws UnsupportedEncodingException, JsonProcessingException {
        String content = getContent(action);
        return objectMapper.readValue(content, clazz);
    }

    public static String write(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    private static String getContent(ResultActions action) throws UnsupportedEncodingException {
        MockHttpServletResponse response = action.andReturn().getResponse();
        response.setCharacterEncoding("UTF-8");
        return response.getContentAsString();
    }
}
