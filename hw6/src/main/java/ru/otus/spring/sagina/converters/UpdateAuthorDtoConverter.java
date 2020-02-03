package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;

@Component
public class UpdateAuthorDtoConverter implements Converter<String, UpdateAuthorDto> {

    private final ObjectMapper objectMapper;

    public UpdateAuthorDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UpdateAuthorDto convert(String source) {
        try {
            return objectMapper.readValue(source, UpdateAuthorDto.class);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }
}
