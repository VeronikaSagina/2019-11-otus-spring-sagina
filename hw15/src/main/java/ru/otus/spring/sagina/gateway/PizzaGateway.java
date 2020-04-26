package ru.otus.spring.sagina.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.sagina.model.Order;
import ru.otus.spring.sagina.model.ReadyOrder;

import java.util.List;

@MessagingGateway
public interface PizzaGateway {

    @Gateway(requestChannel = "orderChannel", replyChannel = "outputChannel")
    List<ReadyOrder> process(List<Order> orders);
}
