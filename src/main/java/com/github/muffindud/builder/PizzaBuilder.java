package com.github.muffindud.builder;

import com.github.muffindud.model.Pizza;
import com.github.muffindud.model.PizzaBase;
import com.github.muffindud.model.PizzaSauce;
import com.github.muffindud.model.PizzaTopping;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PizzaBuilder {
    private String pizzaName;
    private Pair<PizzaBase, Float> baseQty;
    private Pair<PizzaSauce, Float> sauceQty;
    private Map<PizzaTopping, Float> toppingsQty;
    private int cookTempCelsius;
    private int cookTimeMinutes;

    public PizzaBuilder setName(String name) {
        this.pizzaName = name;
        return this;
    }

    public PizzaBuilder setBase(PizzaBase base, float qty) {
        this.baseQty = new ImmutablePair<>(base, qty);
        return this;
    }

    public PizzaBuilder setSauce(PizzaSauce sauce, float qty) {
        this.sauceQty = new ImmutablePair<>(sauce, qty);
        return this;
    }

    public PizzaBuilder addTopping(PizzaTopping topping, float qty) {
        if (Objects.isNull(this.toppingsQty)) {
            this.toppingsQty = new HashMap<>();
        }

        this.toppingsQty.put(topping, qty);

        return this;
    }

    public PizzaBuilder cook(int celsius, int minutes) {
        this.cookTempCelsius = celsius;
        this.cookTimeMinutes = minutes;
        return this;
    }

    public Pizza build() {
        return new Pizza(this.pizzaName, this.baseQty, this.sauceQty, this.toppingsQty, this.cookTempCelsius, this.cookTimeMinutes);
    }
}
