package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;

@Component
public class CreateAuthorDtoConverter implements Converter<String, CreateAuthorDto> {

    private final ObjectMapper objectMapper;

    public CreateAuthorDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CreateAuthorDto convert(String source) {
        try {
            return objectMapper.readValue(source, CreateAuthorDto.class);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }
}
