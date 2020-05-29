package ru.otus.spring.sagina.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.EmailDto;

@Service
public class KafkaListenerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaListenerService.class);
    private final ObjectMapper objectMapper;
    private final MailService mailService;

    public KafkaListenerService(ObjectMapper objectMapper, MailService mailService) {
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "${kafka.consumer.topic}")
    public void listener(ConsumerRecord<String, byte[]> consumerRecord) {
        try {
            EmailDto emailDto = objectMapper.readValue(consumerRecord.value(), EmailDto.class);
            mailService.sendEmail(emailDto);
        } catch (Exception e) {
            LOGGER.error("Ошибка при приеме сообщения из kafka", e);
        }
    }
}
