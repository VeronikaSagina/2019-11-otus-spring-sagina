package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;

@Component
public class CreateGenreDtoConverter implements Converter<String, CreateGenreDto> {

    private final ObjectMapper objectMapper;

    public CreateGenreDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CreateGenreDto convert(String source) {
        try {
            return objectMapper.readValue(source, CreateGenreDto.class);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }
}
