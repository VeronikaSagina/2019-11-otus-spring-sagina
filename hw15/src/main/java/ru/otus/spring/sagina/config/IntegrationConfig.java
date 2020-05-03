package ru.otus.spring.sagina.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.sagina.model.ReadyOrder;

@Configuration
public class IntegrationConfig {
    @Bean
    public QueueChannel orderChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel outputChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers
                .fixedRate(100)
                .maxMessagesPerPoll(3)
                .get();
    }

    @Bean
    public IntegrationFlow orderFlow() {
        return IntegrationFlows.from("orderChannel")
                .split()
                .handle("orderService", "checkOrder")
                .handle("pizzaService", "makePizza")
                .handle("deliveryService", "way")
                .transform(it -> {
                    ReadyOrder readyOrder = (ReadyOrder) it;
                    if (readyOrder.getPhoneNumber().startsWith("5")) {
                        readyOrder.setBonus("500 баллов");
                    }
                    return readyOrder;
                })
                .aggregate()
                .channel("outputChannel")
                .get();
    }
}
