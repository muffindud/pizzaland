package com.github.muffindud.model;

import java.util.HashMap;
import java.util.Map;

public final class Cart implements Product {
    private final Map<Product, Integer> productQty = new HashMap<>();

    @Override
    public float getPrice() {
        float totalPrice = 0;

        for (Map.Entry<Product, Integer> productQtyEntry : this.productQty.entrySet()) {
            totalPrice += productQtyEntry.getKey().getPrice() * productQtyEntry.getValue();
        }

        return totalPrice;
    }
}
