package com.github.muffindud.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public final class Pizza implements Product {
    @Getter @Setter private float basePrice;

    @Getter private final Map<Product, Float> ingredientQty = new HashMap<>();

    @Override
    public float getPrice() {
        return this.basePrice + ingredientQty.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0F, Float::sum);
    }
}
