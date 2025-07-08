package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.PizzaMenu;
import com.github.muffindud.view.PizzaMenuView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class PizzaMenuController extends BaseController implements EventListener {
    private final Map<NotificationTopic, Runnable> notificationHandler = new HashMap<>();
    private final PizzaMenu pizzaMenu;

    public PizzaMenuController(PizzaMenu pizzaMenu) {
        super();

        this.notificationHandler.put(NotificationTopic.DISCOUNT_APPLIED, this::sendDiscountApplied);
        this.notificationHandler.put(NotificationTopic.DISCOUNT_NOT_APPLIED, this::sendDiscountNotApplied);

        this.pizzaMenu = pizzaMenu;
    }

    public void handlePizzaMenuInput(String input) {
        // Add the selected pizza to cart (perhaps observer with cart as listener?)
    }

    public void sendPizzaMenuMenu() {
        System.out.println(PizzaMenuView.getMenu(pizzaMenu));
        handleInput(MenuContext.PIZZA_MENU);
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

    @Override
    protected Consumer<String> contextInputHandler() {
        return this::handlePizzaMenuInput;
    }

    @Override
    protected String actionKey() {
        return "1";
    }

    @Override
    protected String actionName() {
        return "Show menu";
    }

    @Override
    protected Runnable action() {
        return this::sendPizzaMenuMenu;
    }

    @Override
    protected MenuContext contextName() {
        return MenuContext.PIZZA_MENU;
    }
}
