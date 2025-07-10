package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.Ingredient;
import com.github.muffindud.model.Pizza;
import com.github.muffindud.model.PizzaMenu;
import com.github.muffindud.publisher.EventManager;
import com.github.muffindud.utils.PizzaMessage;
import com.github.muffindud.view.PizzaMenuView;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
public final class PizzaMenuController extends BaseController implements EventListener {
    private final Map<NotificationTopic, Runnable> notificationHandler = new HashMap<>();
    private final PizzaMenu pizzaMenu;

    private final EventManager eventManager;

    public PizzaMenuController(PizzaMenu pizzaMenu, EventManager eventManager) {
        super();

        this.notificationHandler.put(NotificationTopic.DISCOUNT_APPLIED, this::sendDiscountApplied);
        this.notificationHandler.put(NotificationTopic.DISCOUNT_NOT_APPLIED, this::sendDiscountNotApplied);

        // TODO: Remove when implementing factories
        Ingredient unobtainium = new Ingredient();
        unobtainium.setPrice(10F);
        unobtainium.setName("Unobtainium");

        Ingredient unobtainium2 = new Ingredient();
        unobtainium2.setPrice(15F);
        unobtainium2.setName("Unobtainium2");

        Pizza testPizza = new Pizza();
        testPizza.setName("Unobtainium pizza");
        testPizza.getIngredientQty().put(unobtainium, 9.9F);

        Pizza testPizza2 = new Pizza();
        testPizza2.setName("Unobtainium2 pizza");
        testPizza2.getIngredientQty().put(unobtainium2, 9.9F);


        pizzaMenu.getPizzas().add(testPizza);
        pizzaMenu.getPizzas().add(testPizza2);

        this.pizzaMenu = pizzaMenu;
        this.eventManager = eventManager;
    }

    public void handlePizzaMenuInput(String input) {
        log.info("Received {}", input);

        if (Objects.equals(input, "0")) {
            log.info("Returning to navigation menu");
            BaseController.sendNavigationMenu();
            BaseController.handleNavigationMenuInput();
        } else {
            Pizza pizza = this.pizzaMenu.getPizzas().get(Integer.parseInt(input) - 1);
            int count = promptForCount();

            this.eventManager.notifySubscribers(NotificationTopic.CART_ITEM_ADDED, new PizzaMessage(pizza, count));
            System.out.println("Added " + count + " x " + pizza.getName());

            this.sendPizzaMenuMenu();
            this.handleInput();
        }

        // TODO: Add the selected pizza to cart (perhaps observer with cart as listener?)
    }

    public void sendPizzaMenuMenu() {
        log.info("Sending pizza menu");
        System.out.println(PizzaMenuView.getMenu(this.pizzaMenu));
        System.out.println("[0]. Back");
    }

    private int promptForCount() {
        System.out.print("Count: ");
        int count = Integer.parseInt(BaseController.readInput());
        System.out.println();

        return count;
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
    public void update(NotificationTopic topic, Object message) {
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
