package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.Country;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.factory.PizzaFactory;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.*;
import com.github.muffindud.publisher.EventManager;
import com.github.muffindud.utils.PizzaMessage;
import com.github.muffindud.view.IngredientView;
import com.github.muffindud.view.PizzaMenuView;
import com.github.muffindud.view.PizzaView;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
public final class PizzaMenuController extends BaseController implements EventListener {
    private final Map<NotificationTopic, Consumer<Object>> notificationHandler = new HashMap<>();
    private final PizzaMenu pizzaMenu;

    private final EventManager eventManager;

    private Country country = Country.USA;
    private PizzaFactory pizzaFactory = PizzaFactory.getFactory(this.country);

    public PizzaMenuController(PizzaMenu pizzaMenu, EventManager eventManager) {
        super();

        this.notificationHandler.put(NotificationTopic.DISCOUNT_APPLIED, this::sendDiscountApplied);
        this.notificationHandler.put(NotificationTopic.DISCOUNT_NOT_APPLIED, this::sendDiscountNotApplied);
        this.notificationHandler.put(NotificationTopic.COUNTRY_CHANGE, this::changeCountry);

        this.pizzaMenu = pizzaMenu;
        this.eventManager = eventManager;

        this.updatePizzaMenu();
    }

    private void handlePizzaSelect(int pizzaSelection) {
        Pizza pizza = this.pizzaMenu.getPizzas().get(pizzaSelection);

        System.out.println(PizzaView.getPizzaInfo(pizza, IngredientView::getIngredientUnitPrice));

        int count = promptForCount();

        this.eventManager.notifySubscribers(NotificationTopic.CART_ITEM_ADDED, new PizzaMessage(pizza, count));
        System.out.println("Added " + count + " x " + pizza.getName());

        this.sendPizzaMenuMenu();
        this.handleInput();
    }

    private void handlePizzaMenuInput(String input) {
        log.info("Received {}", input);

        if (Objects.equals(input, "0")) {
            log.info("Returning to navigation menu");
            BaseController.sendAndHandleNavigationMenu();
        } else {
            handlePizzaSelect(Integer.parseInt(input) - 1);
        }
    }

    private void sendPizzaMenuMenu() {
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

    private void sendDiscountApplied(Object message) {
        System.out.printf("You're about to spend %.2f. You won %.2f%% discount\n",
                ConfigProvider.getDiscount().discountPrice(),
                ConfigProvider.getDiscount().discountRate() * 100);
    }

    private void sendDiscountNotApplied(Object message) {
        System.out.printf("You're no longer over %.2f. %.2f%% discount no longer applied\n",
                ConfigProvider.getDiscount().discountPrice(),
                ConfigProvider.getDiscount().discountPrice() * 100);
    }

    private void changeCountry(Object message) {
        this.country = (Country) message;
        log.info("Set country to {}", country.name());
        this.pizzaFactory = PizzaFactory.getFactory(this.country);
        log.info("Set pizza factory to {}", this.pizzaFactory.getClass().getName());
        this.updatePizzaMenu();
    }

    private void updatePizzaMenu() {
        this.pizzaMenu.getPizzas().clear();
        this.pizzaMenu.getPizzas().add(pizzaFactory.createSausagePizza());
        this.pizzaMenu.getPizzas().add(pizzaFactory.createVegetarianPizza());
        this.pizzaMenu.getPizzas().add(pizzaFactory.createVeganPizza());
        log.info("Added pizzas to the menu");
    }

    @Override
    public void update(NotificationTopic topic, Object message) {
        this.notificationHandler.get(topic).accept(message);
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
    protected boolean isOperationPermitted(String input) {
        boolean operationPermitted;
        try {
            int selection = Integer.parseInt(input);
            operationPermitted = this.pizzaMenu.getPizzas().size() >= selection;
        } catch (NumberFormatException e) {
            operationPermitted = false;
        }

        return operationPermitted;
    }

    @Override
    protected MenuContext contextName() {
        return MenuContext.PIZZA_MENU;
    }
}
