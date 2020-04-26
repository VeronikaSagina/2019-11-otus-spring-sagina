package ru.otus.spring.sagina;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.sagina.gateway.PizzaGateway;
import ru.otus.spring.sagina.model.Order;
import ru.otus.spring.sagina.model.ReadyOrder;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.otus.spring.sagina.PizzaApplication.getOrder;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PizzaApplication.class)
public class IntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger("pizza.log");
    private final Random random = new Random();
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Autowired
    private PizzaGateway pizzaProcess;
    @Autowired
    private PublishSubscribeChannel outputChannel;

    @Test
    void test() {
        int orderForExecute = 85;
        List<Order> expected = new ArrayList<>();
        List<ReadyOrder> actual = new ArrayList<>();

        AtomicInteger actualOrderCount = new AtomicInteger();
        outputChannel.subscribe(message -> {
            LOG.info("Доставлен заказ {}", message);
            actualOrderCount.incrementAndGet();
            actual.addAll((Collection<ReadyOrder>) message.getPayload());
        });

        List<Future<List<ReadyOrder>>> futures = IntStream.range(0, orderForExecute)
                .mapToObj(this::getOrders)
                .peek(expected::addAll)
                .map(orders -> executorService.submit(() -> pizzaProcess.process(orders)))
                .collect(Collectors.toList());
        futures.forEach(it -> {
            try {
                it.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        actual.sort(Comparator.comparing(ReadyOrder::getNumber));

        Assertions.assertEquals(orderForExecute, actualOrderCount.get());
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getNumber(), actual.get(i).getNumber());
            Assertions.assertEquals(expected.get(i).getPhoneNumber(), actual.get(i).getPhoneNumber());
        }
    }

    private List<Order> getOrders(int number) {
        return IntStream.rangeClosed(0, random.nextInt(3))
                .mapToObj(it -> getOrder(number))
                .collect(Collectors.toList());
    }
}
