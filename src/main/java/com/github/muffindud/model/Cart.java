package com.github.muffindud.model;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.publisher.EventManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class Cart extends Product {
    @Getter private final Map<Product, Integer> productQty = new HashMap<>();
    private boolean thresholdOver = false;
    private final EventManager eventManager;
    private static final float DISCOUNT_PRICE = ConfigProvider.getDiscount().discountPrice();
    private static final float DISCOUNT_RATE = ConfigProvider.getDiscount().discountRate();

    public Cart(EventManager eventManager) {
        this.eventManager = eventManager;
    }

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

        if (isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            totalPrice *= (1F - DISCOUNT_RATE);
        }

        return totalPrice;
    }

    private boolean isPriceEqualOrOverThreshold(float threshold) {
        return this.getContentsPrice() >= threshold;
    }

    private void notifyIfThresholdCrossed() {
        if (this.thresholdOver && !isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            this.eventManager.notifySubscribers(NotificationTopic.DISCOUNT_NOT_APPLIED);
            this.thresholdOver = false;
        } else if (!this.thresholdOver && isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            this.eventManager.notifySubscribers(NotificationTopic.DISCOUNT_APPLIED);
            this.thresholdOver = true;
        }
    }

    public Cart add(Pizza pizza, int quantity) {
        if (this.productQty.containsKey(pizza)) {
            this.productQty.put(pizza, this.productQty.get(pizza) + quantity);
        } else {
            this.productQty.put(pizza, quantity);
        }

        this.notifyIfThresholdCrossed();

        return this;
    }

    public Cart add(Pizza pizza) {
        this.add(pizza, 1);
        return this;
    }

    public Cart remove(Pizza pizza, int quantity) {
        if (this.productQty.getOrDefault(pizza, 0) <= quantity) {
            this.productQty.remove(pizza);
        } else {
            this.productQty.put(pizza, this.productQty.get(pizza) + quantity);
        }

        this.notifyIfThresholdCrossed();

        return this;
    }

    public Cart removeOne(Pizza pizza) {
        this.remove(pizza, 1);
        return this;
    }

    public Cart removeAll(Pizza pizza) {
        this.remove(pizza, this.getProductQty().getOrDefault(pizza, 0));
        return this;
    }

    public Cart empty() {
        this.productQty.clear();
        this.notifyIfThresholdCrossed();
        return this;
    }
}
