package ru.otus.spring.sagina.model;

import ru.otus.spring.sagina.enums.PizzaSize;

public class Pizza {
   private String name;
   private PizzaSize size;

    public Pizza(String name, PizzaSize size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String toString() {
        return "'" + name + "' " + size.getValue();
    }
}
