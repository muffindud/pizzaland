package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.Cart;
import com.github.muffindud.model.PizzaMenu;
import com.github.muffindud.view.CartView;
import com.github.muffindud.view.PizzaMenuView;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public final class MenuController implements EventListener {
    private final Map<MenuContext, Consumer<String>> inputHandler = new HashMap<>();
    private final Map<NotificationTopic, Runnable> notificationHandler = new HashMap<>();

    private Scanner scanner;

    private final Cart cart;
    private final PizzaMenu pizzaMenu;

    public MenuController(PizzaMenu pizzaMenu, Cart cart) {
        inputHandler.put(MenuContext.BASE_MENU, this::handleNavigationMenuInput);
        inputHandler.put(MenuContext.CART, this::handleCartInput);
        inputHandler.put(MenuContext.COUNTRY_SELECTOR, this::handleCountryInput);
        inputHandler.put(MenuContext.PIZZA_MENU, this::handlePizzaMenuInput);

        notificationHandler.put(NotificationTopic.DISCOUNT_APPLIED, this::sendDiscountApplied);
        notificationHandler.put(NotificationTopic.DISCOUNT_NOT_APPLIED, this::sendDiscountNotApplied);

        this.pizzaMenu = pizzaMenu;
        this.cart = cart;
    }

    public void run() {
        try (Scanner s = new Scanner(System.in)) {
            this.scanner = s;
            sendNavigationMenu();
        }
    }

    private String readInput() {
        return scanner.nextLine();
    }

    private void handleInput(String input, MenuContext context) {
        this.inputHandler.get(context).accept(input);
    }

    private void handleNavigationMenuInput(String input) {
        // TODO: Switch to a map
        switch (input) {
            case "1":
                sendPizzaMenuMenu();
            case "2":
                sendCartMenu();
            case "3":
                sendCountryMenu();
            case "0":
                System.exit(0);
            default:
                // TODO: Implement loopback logic
                System.out.println("Nope");
                handleNavigationMenuInput(readInput());
        }
    }

    private void handleCartInput(String input) {
        throw new UnsupportedOperationException("not yet " + input);
    }

    private void handleCountryInput(String input) {
        throw new UnsupportedOperationException("not yet " + input);
    }

    private void handlePizzaMenuInput(String input) {
        throw new UnsupportedOperationException("not yet " + input);
    }

    private void sendNavigationMenu() {
        System.out.println("[1]. Show menu");
        System.out.println("[2]. My cart");
        System.out.println("[3]. Set current country");
        System.out.println("[0]. Exit");

        handleInput(readInput(), MenuContext.BASE_MENU);
    }

    private void sendCartMenu() {
        System.out.println(CartView.getCartComposition(cart));
        handleInput(readInput(), MenuContext.CART);
    }

    private void sendPizzaMenuMenu() {
        System.out.println(PizzaMenuView.getMenu(pizzaMenu));
        handleInput(readInput(), MenuContext.PIZZA_MENU);
    }

    private void sendCountryMenu() {
        // TODO
        handleInput(readInput(), MenuContext.COUNTRY_SELECTOR);
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
