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
    private final Map<String, Runnable> navigationMenu = new HashMap<>();

    private Scanner scanner;

    private final Cart cart;
    private final PizzaMenu pizzaMenu;

    public MenuController(PizzaMenu pizzaMenu, Cart cart) {
        this.inputHandler.put(MenuContext.BASE_MENU, this::handleNavigationMenuInput);
        this.inputHandler.put(MenuContext.CART, this::handleCartInput);
        this.inputHandler.put(MenuContext.COUNTRY_SELECTOR, this::handleCountryInput);
        this.inputHandler.put(MenuContext.PIZZA_MENU, this::handlePizzaMenuInput);

        this.notificationHandler.put(NotificationTopic.DISCOUNT_APPLIED, this::sendDiscountApplied);
        this.notificationHandler.put(NotificationTopic.DISCOUNT_NOT_APPLIED, this::sendDiscountNotApplied);

        navigationMenu.put("1", this::sendPizzaMenuMenu);
        navigationMenu.put("2", this::sendCartMenu);
        navigationMenu.put("3", this::sendCountryMenu);
        navigationMenu.put("0", () -> System.exit(0));
        
        this.pizzaMenu = pizzaMenu;
        this.cart = cart;
    }

    public void run() {
        try (Scanner s = new Scanner(System.in)) {
            this.scanner = s;
            this.sendNavigationMenu();
        }
    }

    private String readInput() {
        return this.scanner.nextLine();
    }

    private void handleInput(String input, MenuContext context) {
        this.inputHandler.get(context).accept(input);
    }

    private void handleNavigationMenuInput(String input) {
        this.navigationMenu.getOrDefault(input, () -> {
            System.out.println("Nope");
            this.handleNavigationMenuInput(this.readInput());
        }).run();            
    }

    private void handleCartInput(String input) {
//        throw new UnsupportedOperationException("not yet " + input);
        System.out.println(CartView.getCartComposition(this.cart));

        this.sendNavigationMenu();
    }

    private void handleCountryInput(String input) {
        throw new UnsupportedOperationException("not yet " + input);
    }

    private void handlePizzaMenuInput(String input) {
//        throw new UnsupportedOperationException("not yet " + input);
        this.cart.add(pizzaMenu.getPizzas().get(Integer.parseInt(input) - 1), 1);

        this.sendNavigationMenu();
    }

    private void sendNavigationMenu() {
        System.out.println("[1]. Show menu");
        System.out.println("[2]. My cart");
        System.out.println("[3]. Set current country");
        System.out.println("[0]. Exit");

        this.handleInput(this.readInput(), MenuContext.BASE_MENU);
    }

    private void sendCartMenu() {
        System.out.println(CartView.getCartComposition(cart));
        this.handleInput(this.readInput(), MenuContext.CART);
    }

    private void sendPizzaMenuMenu() {
        System.out.println(PizzaMenuView.getMenu(pizzaMenu));
        this.handleInput(this.readInput(), MenuContext.PIZZA_MENU);
    }

    private void sendCountryMenu() {
        // TODO
        this.handleInput(this.readInput(), MenuContext.COUNTRY_SELECTOR);
    }

    private void sendDiscountApplied() {
        System.out.printf("You're about to spend %.2f. You won %.2f%%\n",
                ConfigProvider.getDiscount().discountPrice(),
                ConfigProvider.getDiscount().discountRate() * 100);
    }

    private void sendDiscountNotApplied() {
        System.out.printf("You're no longer over %.2f. %.2f%% discount no longer applied\n",
                ConfigProvider.getDiscount().discountPrice(),
                ConfigProvider.getDiscount().discountPrice() * 100);
    }

    @Override
    public void update(NotificationTopic topic) {
        this.notificationHandler.get(topic).run();
    }
}
