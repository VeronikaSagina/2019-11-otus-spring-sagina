package ru.otus.spring.sagina.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.enums.PizzaPrice;
import ru.otus.spring.sagina.exception.PizzaMakeException;
import ru.otus.spring.sagina.model.Order;
import ru.otus.spring.sagina.model.OrderItem;

@Service
public class OrderService {
    private static final Logger LOG = LoggerFactory.getLogger("order service");

    public Order checkOrder(Order order) {
        LOG.info("\nПринят {}", order);
        checkPrice(order);
        checkPhone(order.getPhoneNumber());
        return order;
    }

    private void checkPhone(String phoneNumber) {
        if (phoneNumber == null) {
            throw new PizzaMakeException("В заказе не указан номер, приготовление остановленно.");
        }
    }

    private void checkPrice(Order order) {
        int orderPrice = order.getOrderPrice();
        int totalPrice = order.getOrderItems().stream()
                .map(OrderItem::getSize)
                .mapToInt(PizzaPrice::getPrice)
                .sum();
        if (orderPrice != totalPrice) {
            throw new PizzaMakeException("Счет выставлен не правильно, необходимо связаться с клиентом");
        }
    }
}
