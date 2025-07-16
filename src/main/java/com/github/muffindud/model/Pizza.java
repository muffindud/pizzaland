package com.github.muffindud.model;

import com.github.muffindud.enums.Crust;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public final class Pizza extends Product {
    @Getter @Setter private float basePrice;

    @Getter private final Pair<PizzaBase, Float> baseQty;
    @Getter private final Crust crust;
    @Getter private final Pair<PizzaSauce, Float> sauceQty;
    @Getter private final Map<PizzaTopping, Float> toppingsQty;
    @Getter private final int cookTempCelsius;
    @Getter private final int cookTimeMinutes;

    public Pizza(
            String name,
            Pair<PizzaBase, Float> baseQty,
            Crust crust,
            Pair<PizzaSauce, Float> sauceQty,
            Map<PizzaTopping, Float> toppingsQty,
            int cookTempCelsius,
            int cookTimeMinutes) {
        this.name = name;
        this.baseQty = baseQty;
        this.crust = crust;
        this.sauceQty = sauceQty;
        this.toppingsQty = toppingsQty;
        this.cookTempCelsius = cookTempCelsius;
        this.cookTimeMinutes = cookTimeMinutes;
    }

    @Override
    public float getPrice() {
        return this.price
                + baseQty.getLeft().getPrice() * baseQty.getRight()
                + sauceQty.getLeft().getPrice() * sauceQty.getRight()
                + toppingsQty.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0F, Float::sum);
    }
}
