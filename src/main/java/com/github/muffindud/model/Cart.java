package com.github.muffindud.model;

import com.github.muffindud.config.ConfigProvider;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class Cart extends Product {
    @Getter private final Map<Product, Integer> productQty = new HashMap<>();

    private float getContentsPrice() {
        return this.productQty.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0F, Float::sum);
    }

    /**
     * @return total cost of cart with discount rules applied
     */
    @Override
    public float getPrice() {
        float totalPrice = this.getContentsPrice();

        if (this.isPriceEqualOrOverThreshold(ConfigProvider.getDiscount().discountPrice())) {
            totalPrice *= (1F - ConfigProvider.getDiscount().discountRate());
        }

        return totalPrice;
    }

    /**
     * Check if the contents of the cart is greater or equal than the specified threshold
     *
     * @param threshold to check the price against
     * @return `true` if price is equal or greater than the threshold, `false` if otherwise
     */
    public boolean isPriceEqualOrOverThreshold(float threshold) {
        return this.getContentsPrice() >= threshold;
    }
}
