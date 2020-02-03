package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;

@Component
public class BookCommentDtoConverter  implements Converter<String, CreateBookCommentDto> {

    private final ObjectMapper objectMapper;

    public BookCommentDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CreateBookCommentDto convert(String source) {
        try {
            return objectMapper.readValue(source, CreateBookCommentDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
