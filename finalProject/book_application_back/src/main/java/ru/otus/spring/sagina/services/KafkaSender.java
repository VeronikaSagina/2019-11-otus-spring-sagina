package ru.otus.spring.sagina.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.dto.kafka.EmailDto;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "kafka.enable", havingValue = "true")
public class KafkaSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String topic;

    public KafkaSender(ObjectMapper objectMapper,
                       KafkaTemplate<String, byte[]> kafkaTemplate,
                       @Value("${kafka.producer.topic}") String topic) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendTask(EmailDto emailDto) throws Exception {
        LOGGER.debug("отправка задания в kafka");
        Message<byte[]> message = MessageBuilder
                .withPayload(objectMapper.writeValueAsBytes(emailDto))
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        var listenableFuture = kafkaTemplate.send(message);
        listenableFuture.get(10, TimeUnit.SECONDS);
    }
}
