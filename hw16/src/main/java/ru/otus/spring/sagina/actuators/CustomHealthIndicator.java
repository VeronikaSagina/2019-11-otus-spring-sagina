package ru.otus.spring.sagina.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        long total = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        return total < 300
                ? Health.up().withDetails(Map.of("application say", "everything is all right", "total heap size", total)).build()
                : Health.down().withDetails(Map.of("application say", "heap greater then 300mb", "total heap size", total)).build();
    }
}
