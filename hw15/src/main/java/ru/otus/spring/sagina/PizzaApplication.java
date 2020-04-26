package ru.otus.spring.sagina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.spring.sagina.enums.PizzaPrice;
import ru.otus.spring.sagina.gateway.PizzaGateway;
import ru.otus.spring.sagina.model.Order;
import ru.otus.spring.sagina.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.otus.spring.sagina.enums.PizzaSize.getRandomPizzaSize;

@SpringBootApplication
public class PizzaApplication {
    private static final Random RANDOM = new Random();
    private static final List<String> PIZZA_NAMES = List.of(
            "Карбонара", "Маргарита", "Сицилийская", "Дьябола", "Кальцоне",
            "Капричиоза", "Неаполитанская", "Четыре сыра", "Вегетарианская", "Грибная");
    private static final List<String> ADDRESSES = List.of(
            "Центральная", "Молодёжная", "Школьная", "Советская", "Садовая", "Лесная", "Ленина");

    private static final Integer COUNT_ORDERS = 5;
    private static final List<Integer> PHONE_NUMBERS = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PizzaApplication.class, args);
        PizzaGateway pizzaProcess = context.getBean(PizzaGateway.class);
        int orderNumber = 0;
        while (true) {
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i <= RANDOM.nextInt(COUNT_ORDERS); i++) {
                orders.add(getOrder(++orderNumber));
            }
            pizzaProcess.process(orders)
                    .forEach(it -> System.out.println("доставлен заказ: " + it.getNumber()));
        }
    }

    public static Order getOrder(int number) {
        List<OrderItem> orderItems = IntStream.rangeClosed(0, RANDOM.nextInt(PIZZA_NAMES.size()))
                .mapToObj(it -> new OrderItem(PIZZA_NAMES.get(RANDOM.nextInt(PIZZA_NAMES.size())), getRandomPizzaSize()))
                .collect(Collectors.toList());

        return new Order(number,
                getOrderCheckSum(orderItems),
                getPhoneNumber(),
                ADDRESSES.get(RANDOM.nextInt(ADDRESSES.size())),
                orderItems);
    }

    private static int getOrderCheckSum(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getSize)
                .mapToInt(PizzaPrice::getPrice)
                .sum();
    }

    private static String getPhoneNumber() {
        String num = IntStream.range(0, 6)
                .mapToObj(it -> String.valueOf(PHONE_NUMBERS.get(RANDOM.nextInt(PHONE_NUMBERS.size()))))
                .collect(Collectors.joining());
        return num.substring(0, 3) + "-" + num.substring(3, 4) + "-" + num.substring(4);
    }
}
