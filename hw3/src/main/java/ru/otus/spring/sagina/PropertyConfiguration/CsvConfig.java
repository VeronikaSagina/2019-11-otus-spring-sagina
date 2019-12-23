package ru.otus.spring.sagina.PropertyConfiguration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("csv")
public class CsvConfig {
    private String fileNameEn;
    private String fileNameRu;
}
