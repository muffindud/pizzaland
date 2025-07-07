package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;

public final class MenuController implements EventListener {
    public void run() {

    }

    private void sendDiscountApplied() {
        System.out.printf("You're about to spend %.2f. You won %.2f%%",
                ConfigProvider.getDiscount().discountPrice(),
                ConfigProvider.getDiscount().discountRate() * 100);
    }

    private void sendDiscountNotApplied() {
        System.out.printf("You're no longer over %.2f. %.2f%% discount no longer applied",
                ConfigProvider.getDiscount().discountPrice(),
                ConfigProvider.getDiscount().discountPrice() * 100);
    }

    @Override
    public void update(NotificationTopic topic) {
        switch (topic) {
            case DISCOUNT_APPLIED:
                sendDiscountApplied();
            case DISCOUNT_NOT_APPLIED:
                sendDiscountNotApplied();
            default:
                throw new RuntimeException("Operation not supported: " + topic);
        }
    }
}
