package com.github.muffindud.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class Cart extends Product {
    @Getter private final Map<Product, Integer> productQty = new HashMap<>();
    private static final float DISCOUNT_PRICE = 100F;
    private static final float DISCOUNT_AMOUNT = 0.05F;

    private float getContentsPrice() {
        return this.productQty.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0F, Float::sum);
    }

    @Override
    public float getPrice() {
        float totalPrice = this.getContentsPrice();

        if (isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            totalPrice *= (100F - DISCOUNT_AMOUNT);
        }

        return totalPrice;
    }

    private boolean isPriceEqualOrOverThreshold(float threshold) {
        return this.getPrice() >= threshold;
    }

    public Cart add(Pizza pizza, int quantity) {
        if (this.productQty.containsKey(pizza)) {
            this.productQty.put(pizza, this.productQty.get(pizza) + quantity);
        } else {
            this.productQty.put(pizza, quantity);
        }

        return this;
    }

    public Cart add(Pizza pizza) {
        this.add(pizza, 1);
        return this;
    }

    public Cart remove(Pizza pizza, int quantity) {
        if (this.productQty.getOrDefault(pizza, 0) <= quantity) {
            this.removeAll(pizza);
        } else {
            this.productQty.put(pizza, this.productQty.get(pizza) + quantity);
        }
        return this;
    }

    public Cart removeOne(Pizza pizza) {
        this.remove(pizza, 1);
        return this;
    }

    public Cart removeAll(Pizza pizza) {
        this.productQty.remove(pizza);
        return this;
    }
}
