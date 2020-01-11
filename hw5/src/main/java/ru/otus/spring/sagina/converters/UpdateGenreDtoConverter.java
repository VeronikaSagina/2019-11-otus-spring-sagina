package ru.otus.spring.sagina.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;

@Component
public class UpdateGenreDtoConverter implements Converter<String, UpdateGenreDto> {

    private final ObjectMapper objectMapper;

    public UpdateGenreDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UpdateGenreDto convert(String source) {
        try {
            return objectMapper.readValue(source, UpdateGenreDto.class);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }
}
