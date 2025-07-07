package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.Cart;
import com.github.muffindud.view.CartView;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public final class MenuController implements EventListener {
    private final Map<MenuContext, Consumer<String>> inputHandler = new HashMap<>();
    private final Map<NotificationTopic, Runnable> notificationHandler = new HashMap<>();

    private Cart cart;
    private CartView cartView;

    public MenuController() {
        inputHandler.put(MenuContext.BASE_MENU, this::handleNavigationMenuInput);
        inputHandler.put(MenuContext.CART, this::handleCartInput);
        inputHandler.put(MenuContext.COUNTRY_SELECTOR, this::handleCountryInput);
        inputHandler.put(MenuContext.PIZZA_MENU, this::handlePizzaMenuInput);

        notificationHandler.put(NotificationTopic.DISCOUNT_APPLIED, this::sendDiscountApplied);
        notificationHandler.put(NotificationTopic.DISCOUNT_NOT_APPLIED, this::sendDiscountNotApplied);
    }

    public void run() {
        sendNavigationMenu();
    }

    private String readInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

    private void handleInput(String input, MenuContext context) {
        this.inputHandler.get(context).accept(input);
    }

    private void handleNavigationMenuInput(String input) {

    }

    private void handleCartInput(String input) {

    }

    private void handleCountryInput(String input) {

    }

    private void handlePizzaMenuInput(String input) {

    }

    private void sendNavigationMenu() {
        System.out.println("[1]. Show menu");
        System.out.println("[2]. My cart");
        System.out.println("[3]. Set current country");
        System.out.println("[0]. Exit");

        handleInput(readInput(), MenuContext.BASE_MENU);
    }

    private void sendCartMenu() {
        System.out.println(cartView.getCartComposition(cart));
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
        this.notificationHandler.get(topic).run();
    }
}
