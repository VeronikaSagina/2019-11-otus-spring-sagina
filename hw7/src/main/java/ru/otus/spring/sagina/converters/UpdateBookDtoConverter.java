package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;

@Component
public class UpdateBookDtoConverter implements Converter<String, UpdateBookDto> {

    private final ObjectMapper objectMapper;

    public UpdateBookDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UpdateBookDto convert(String source) {
        try {
            return objectMapper.readValue(source, UpdateBookDto.class);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }
}
