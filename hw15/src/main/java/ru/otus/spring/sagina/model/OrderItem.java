package ru.otus.spring.sagina.model;

import ru.otus.spring.sagina.enums.PizzaSize;

public class OrderItem {
    private String name;
    private PizzaSize size;

    public OrderItem(String name, PizzaSize size){
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public PizzaSize getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "'" + name + "' " + size.getValue();
    }
}
