package ru.otus.spring.sagina.model;

import java.util.List;

public class ReadyOrder {
    private int number;
    private int orderPrice;
    private String phoneNumber;
    private String address;
    private List<Pizza> pizzas;
    private String bonus;

    public ReadyOrder(int number, int orderPrice, String phoneNumber, String address, List<Pizza> pizzas) {
        this.number = number;
        this.orderPrice = orderPrice;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.pizzas = pizzas;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return "Готовый заказ - номер: " + number + "\n"
                + "сумма: " + orderPrice + "\n"
                + "телефон: '" + phoneNumber + "'\n"
                + "адрес: " + address + "\n"
                + (bonus == null ? "" : "подарок за заказ: " + bonus + "\n")
                + "пицца: " + pizzas;
    }
}
