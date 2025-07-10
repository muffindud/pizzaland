package com.github.muffindud.controller;

import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.Cart;
import com.github.muffindud.model.Pizza;
import com.github.muffindud.publisher.EventManager;
import com.github.muffindud.utils.PizzaMessage;
import com.github.muffindud.view.CartView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class CartController extends BaseController implements EventListener {
    private final Map<NotificationTopic, Consumer<Object>> notificationHandler = new HashMap<>();

    private final Cart cart;
    private final EventManager eventManager;
    private boolean thresholdOver = false;

    private static final float DISCOUNT_PRICE = ConfigProvider.getDiscount().discountPrice();

    public CartController(Cart cart, EventManager eventManager) {
        super();

        this.notificationHandler.put(NotificationTopic.CART_ITEM_ADDED, this::handlePizzaAdd);
        this.notificationHandler.put(NotificationTopic.CART_ITEM_REMOVED, this::handlePizzaRemove);

        this.cart = cart;
        this.eventManager = eventManager;
    }

    private void notifyIfThresholdCrossed() {
        if (this.thresholdOver && !this.cart.isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            this.eventManager.notifySubscribers(NotificationTopic.DISCOUNT_NOT_APPLIED, null);
            this.thresholdOver = false;
        } else if (!this.thresholdOver && this.cart.isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            this.eventManager.notifySubscribers(NotificationTopic.DISCOUNT_APPLIED, null);
            this.thresholdOver = true;
        }
    }

    public void add(Pizza pizza, int quantity) {
        if (this.cart.getProductQty().containsKey(pizza)) {
            this.cart.getProductQty().put(pizza, this.cart.getProductQty().get(pizza) + quantity);
        } else {
            this.cart.getProductQty().put(pizza, quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    public void add(Pizza pizza) {
        this.add(pizza, 1);
    }

    public void remove(Pizza pizza, int quantity) {
        if (this.cart.getProductQty().getOrDefault(pizza, 0) <= quantity) {
            this.cart.getProductQty().remove(pizza);
        } else {
            this.cart.getProductQty().put(pizza, this.cart.getProductQty().get(pizza) + quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    public void removeOne(Pizza pizza) {
        this.remove(pizza, 1);
    }

    public void removeAll(Pizza pizza) {
        this.remove(pizza, this.cart.getProductQty().getOrDefault(pizza, 0));
    }

    public void empty() {
        this.cart.getProductQty().clear();
        this.notifyIfThresholdCrossed();
    }

    private void sendCartMenu() {
        System.out.println(CartView.getCartComposition(cart));
    }

    // TODO
    private void handleCartInput(String input) {
        // Handle the selected item in cart
    }

    @Override
    protected MenuContext contextName() {
        return MenuContext.CART;
    }

    @Override
    protected Consumer<String> contextInputHandler() {
        return this::handleCartInput;
    }

    @Override
    protected String actionKey() {
        return "2";
    }

    @Override
    protected String actionName() {
        return "My cart";
    }

    @Override
    protected Runnable action() {
        return this::sendCartMenu;
    }

    @Override
    public void update(NotificationTopic topic, Object message) {
        this.notificationHandler.get(topic).accept(message);
    }

    private void handlePizzaAdd(Object message) {
        PizzaMessage pizzaMessage = (PizzaMessage) message;
        this.add(pizzaMessage.getPizza(), pizzaMessage.getCount());
    }

    private void handlePizzaRemove(Object message) {
        PizzaMessage pizzaMessage = (PizzaMessage) message;
        this.remove(pizzaMessage.getPizza(), pizzaMessage.getCount());
    }
}
