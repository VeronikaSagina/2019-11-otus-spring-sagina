package ru.otus.spring.sagina.model;

import java.util.List;

public class Order {
    private int number;
    private int orderPrice;
    private String phoneNumber;
    private String address;
    private List<OrderItem> orderItems;

    public Order(int number, int orderPrice, String phoneNumber, String address, List<OrderItem> orderItems) {
        this.number = number;
        this.orderPrice = orderPrice;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderItems = orderItems;
    }

    public int getNumber() {
        return number;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Заказ - номер: " + number + "\n"
                + "сумма: " + orderPrice + "\n"
                + "телефон: '" + phoneNumber + "'\n"
                + "адрес: " + address + "\n"
                + "пицца: " + orderItems;
    }
}
