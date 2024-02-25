package com.juancnuno.maxavailable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;

import jakarta.json.JsonObject;

record Category(String name, int balance) implements Comparable<Category> {

    private static final Comparator<Category> comparator = Comparator.comparing(Category::balance)
            .thenComparing(Category::name);

    Category(JsonObject category) {
        this(category.getString("name"), category.getInt("balance"));
    }

    @Override
    public String toString() {
        var decimal = new BigDecimal(this.balance).movePointLeft(3);
        return name + "    " + NumberFormat.getCurrencyInstance().format(decimal);
    }

    @Override
    public int compareTo(Category category) {
        return comparator.compare(this, category);
    }
}
