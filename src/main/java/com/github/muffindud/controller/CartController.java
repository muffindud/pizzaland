package com.github.muffindud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.dao.OrderDao;
import com.github.muffindud.dto.OrderDto;
import com.github.muffindud.enums.MenuContext;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import com.github.muffindud.model.Cart;
import com.github.muffindud.model.Pizza;
import com.github.muffindud.model.Product;
import com.github.muffindud.publisher.EventManager;
import com.github.muffindud.utils.PizzaMessage;
import com.github.muffindud.view.CartView;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public final class CartController extends BaseController implements EventListener {
    private final Map<NotificationTopic, Consumer<Object>> notificationHandler = new HashMap<>();
    private final Map<String, Runnable> orderMenuInputHandler = new HashMap<>();
    private String orderMenuMessage = "";

    private final Cart cart;
    private final EventManager eventManager;
    private boolean thresholdOver = false;

    private static final float DISCOUNT_PRICE = ConfigProvider.getDiscount().discountPrice();

    public CartController(Cart cart, EventManager eventManager) {
        super();

        this.notificationHandler.put(NotificationTopic.CART_ITEM_ADDED, this::handlePizzaAdd);
        this.notificationHandler.put(NotificationTopic.CART_ITEM_REMOVED, this::handlePizzaRemove);
        this.notificationHandler.put(NotificationTopic.CART_ITEM_SET, this::handlePizzaCountSet);

        this.addOrderMenuInput("O", "Create Order", this::makeOrder);
        this.addOrderMenuInput("C", "Clear Cart", this::empty);

        this.cart = cart;
        this.eventManager = eventManager;
    }

    private void addOrderMenuInput(String input, String message, Runnable action) {
        this.orderMenuInputHandler.put(input, action);
        this.orderMenuMessage += "[" + input + "]. " + message + "\n";
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

    private void add(Pizza pizza, int quantity) {
        if (quantity == 0) {
            return;
        }

        if (this.cart.getProductQty().containsKey(pizza)) {
            this.cart.getProductQty().put(pizza, this.cart.getProductQty().get(pizza) + quantity);
        } else {
            this.cart.getProductQty().put(pizza, quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    private void add(Pizza pizza) {
        this.add(pizza, 1);
    }

    private void set(Pizza pizza, int quantity) {
        if (quantity == 0) {
            this.removeAll(pizza);
        } else {
            this.cart.getProductQty().put(pizza, quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    private void remove(Pizza pizza, int quantity) {
        if (this.cart.getProductQty().getOrDefault(pizza, 0) <= quantity) {
            this.cart.getProductQty().remove(pizza);
        } else {
            this.cart.getProductQty().put(pizza, this.cart.getProductQty().get(pizza) - quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    private void removeOne(Pizza pizza) {
        this.remove(pizza, 1);
    }

    private void removeAll(Pizza pizza) {
        this.remove(pizza, this.cart.getProductQty().getOrDefault(pizza, 0));
    }

    private void empty() {
        this.cart.getProductQty().clear();
        this.notifyIfThresholdCrossed();
    }

    private void sendCartMenu() {
        System.out.println(this.orderMenuMessage);
        System.out.println(CartView.getCartComposition(cart));
        System.out.println("\n[0]. Back");
    }

    private void handleCartInput(String input) {
        log.info("Received {}", input);

        if (Objects.equals(input, "0")) {
            log.info("Returning to navigation menu");
            BaseController.sendAndHandleNavigationMenu();
        } else if (this.orderMenuInputHandler.containsKey(input)) {
            this.orderMenuInputHandler.get(input).run();
            BaseController.sendAndHandleNavigationMenu();
        } else {
            Map.Entry<Product, Integer> pizzaEntry = new ArrayList<>(this.cart.getProductQty().entrySet()).get(Integer.parseInt(input) - 1);
            Pizza pizza = (Pizza) pizzaEntry.getKey();
            int count = pizzaEntry.getValue();
            System.out.println(pizza.getName() + " x " + count + " = " + pizza.getPrice() * count);

            // TODO: Handle selected item

            this.sendCartMenu();
            this.handleInput();
        }
    }

    private void makeOrder() {
        OrderDto order = OrderDao.createOrder(this.cart);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json;
        try {
            json = ow.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        File file = new File("order_" + UUID.randomUUID() + ".json");
        try {
            file.createNewFile();
            log.info("Created file {}", file.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter fileWriter = new FileWriter(file.getName());
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.resetCart();
    }

    private void resetCart() {
        this.cart.getProductQty().clear();
        this.thresholdOver = false;
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
    protected boolean isOperationPermitted(String input) {
        boolean operationPermitted;

        if (this.orderMenuInputHandler.containsKey(input)) {
            operationPermitted = true;
        } else {
            try {
                int selection = Integer.parseInt(input);
                operationPermitted = this.cart.getProductQty().size() >= selection;
            } catch (NumberFormatException e) {
                operationPermitted = false;
            }
        }

        return operationPermitted;
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

    private void handlePizzaCountSet(Object message) {
        PizzaMessage pizzaMessage = (PizzaMessage) message;
        this.set(pizzaMessage.getPizza(), pizzaMessage.getCount());
    }
}
