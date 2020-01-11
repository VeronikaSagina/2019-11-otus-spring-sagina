package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.CreateBookDto;

@Component
public class CreateBookDtoConverter implements Converter<String, CreateBookDto> {

    private final ObjectMapper objectMapper;

    public CreateBookDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CreateBookDto convert(String source) {
        try {
            return objectMapper.readValue(source, CreateBookDto.class);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }
}
