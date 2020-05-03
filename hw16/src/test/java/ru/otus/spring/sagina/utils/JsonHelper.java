package ru.otus.spring.sagina.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.enums.Type;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class JsonHelper {
    private static final ObjectMapper objectMapper
            = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static <T> List<T> getResultList(ResultActions action, Type type)
            throws UnsupportedEncodingException, JsonProcessingException {
        String content = getContent(action);
        JsonNode embedded = objectMapper.readValue(content, JsonNode.class)
                .get("_embedded")
                .get(type.getValue());
        return (List<T>) Arrays.asList(objectMapper.treeToValue(embedded, getByType(type)));
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

    private static Class<? extends Object[]> getByType(Type type) {
        switch (type) {
            case AUTHORS:
                return Author[].class;
            case BOOKS:
                return Book[].class;
            case COMMENTS:
                return BookComment[].class;
            case GENRES:
                return Genre[].class;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
