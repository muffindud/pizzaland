package com.github.muffindud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.muffindud.config.ConfigProvider;
import com.github.muffindud.dao.OrderDao;
import com.github.muffindud.dto.OrderDto;
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
    // Hold the consumer for each subscribed topic
    private final Map<NotificationTopic, Consumer<Object>> notificationHandler = new HashMap<>();

    // Hold the endpoints to the actions within the cart
    private final Map<String, Runnable> orderMenuInputHandler = new HashMap<>();
    private String orderMenuMessage = "";

    // Hold the endpoints for manipulating a specified item in the cart
    private final Map<String, Consumer<Pizza>> cartItemHandler = new HashMap<>();
    private String cartItemMessage = "";

    // State to check if the discount threshold before checking the total price
    private boolean thresholdOver = false;

    private final Cart cart;
    private final EventManager eventManager;

    private static final float DISCOUNT_PRICE = ConfigProvider.getDiscount().discountPrice();

    public CartController(Cart cart, EventManager eventManager) {
        super();

        // Declare the consumers that will run for the specified topics
        this.notificationHandler.put(NotificationTopic.CART_ITEM_ADDED, this::handlePizzaAdd);
        this.notificationHandler.put(NotificationTopic.CART_ITEM_REMOVED, this::handlePizzaRemove);
        this.notificationHandler.put(NotificationTopic.CART_ITEM_SET, this::handlePizzaCountSet);

        // Declare the endpoints for the cart options
        this.addOrderMenuInput("O", "Create Order", this::makeOrder);
        this.addOrderMenuInput("C", "Clear Cart", this::empty);

        // Declare the endpoints for the item manipulation options
        this.addCartItemOption("R", "Remove all", this::removeAll);
        this.addCartItemOption("A", "Add custom quantity", this::handleAddCustomQty);
        this.addCartItemOption("+", "Add one", this::add);
        this.addCartItemOption("D", "Decrease custom quantity", this::handleDecreaseCustomQty);
        this.addCartItemOption("-", "Decrease one", this::removeOne);
        this.addCartItemOption("S", "Set quantity", this::handleSetCustomQty);

        this.cart = cart;
        this.eventManager = eventManager;
    }

    private void addOrderMenuInput(String input, String message, Runnable action) {
        this.orderMenuInputHandler.put(input, action);
        this.orderMenuMessage += BaseController.formatMenuMessageOption(input, message);
    }

    private void addCartItemOption(String input, String message, Consumer<Pizza> action) {
        this.cartItemHandler.put(input, action);
        this.cartItemMessage += BaseController.formatMenuMessageOption(input, message);
    }

    private void notifyIfThresholdCrossed() {
        if (this.thresholdOver && !this.cart.isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            // If price goes under threshold
            this.eventManager.notifySubscribers(NotificationTopic.DISCOUNT_NOT_APPLIED, null);
            this.thresholdOver = false;
        } else if (!this.thresholdOver && this.cart.isPriceEqualOrOverThreshold(DISCOUNT_PRICE)) {
            // If price goes over threshold
            this.eventManager.notifySubscribers(NotificationTopic.DISCOUNT_APPLIED, null);
            this.thresholdOver = true;
        }
    }

    // Base pizza add
    private void add(Pizza pizza, int quantity) {
        if (quantity == 0) {
            return;
        }

        if (this.cart.getProductQty().containsKey(pizza)) {
            // Add to count if pizza already exists
            this.cart.getProductQty().put(pizza, this.cart.getProductQty().get(pizza) + quantity);
        } else {
            // Create new entry if pizza does not exists
            this.cart.getProductQty().put(pizza, quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    private void add(Pizza pizza) {
        this.add(pizza, 1);
    }

    // Base pizza set
    private void set(Pizza pizza, int quantity) {
        if (quantity == 0) {
            this.removeAll(pizza);
        } else {
            this.cart.getProductQty().put(pizza, quantity);
        }

        this.notifyIfThresholdCrossed();
    }

    // Base pizza remove
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

    // Base clear cart
    private void empty() {
        this.cart.getProductQty().clear();
        this.notifyIfThresholdCrossed();
    }

    private void handleSelectedItem(String input) {
        // Get the pizza and qty by index
        Map.Entry<Product, Integer> pizzaEntry = new ArrayList<>(this.cart.getProductQty().entrySet()).get(Integer.parseInt(input) - 1);
        Pizza pizza = (Pizza) pizzaEntry.getKey();
        int count = pizzaEntry.getValue();

        System.out.println("\n" + pizza.getName() + " x " + count + " = " + pizza.getPrice() * count);
        System.out.println(this.cartItemMessage);

        BaseController.runWithInputCheck(
                this.cartItemHandler::containsKey,
                "Selection: ",
                "Operation is not supported",
                this.cartItemHandler::get
        );
    }

    private void makeOrder() {
        OrderDto order = OrderDao.createOrder(this.cart);
        String json = CartController.makeJson(order);
        File file = CartController.createFile("order_" + UUID.randomUUID() + ".json");
        CartController.writeToFile(file, json);
        this.resetCart();
    }

    private static String makeJson(OrderDto order) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json;
        try {
            json = ow.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    private static File createFile(String name) {
        File file = new File(name);
        try {
            file.createNewFile();
            log.info("Created file {}", file.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    private static void writeToFile(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file.getName());
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddCustomQty(Pizza pizza) {
        this.add(pizza, BaseController.getNonNegativeIntegerInput());
    }

    private void handleDecreaseCustomQty(Pizza pizza) {
        this.remove(pizza, BaseController.getNonNegativeIntegerInput());
    }

    private void handleSetCustomQty(Pizza pizza) {
        this.set(pizza, BaseController.getNonNegativeIntegerInput());
    }

    private void resetCart() {
        this.cart.getProductQty().clear();
        this.thresholdOver = false;
    }

    @Override
    protected void contextInputHandler(String input) {
        log.info("Received {}", input);

        if (Objects.equals(input, "0")) {
            log.info("Returning to navigation menu");
            BaseController.sendAndHandleNavigationMenu();
        } else if (this.orderMenuInputHandler.containsKey(input)) {
            this.orderMenuInputHandler.get(input).run();
            BaseController.sendAndHandleNavigationMenu();
        } else {
            this.handleSelectedItem(input);

            this.sendMenuAndHandleInput();
        }
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
    protected void sendMenuMessage() {
        System.out.println(this.orderMenuMessage);
        System.out.println(CartView.getCartComposition(cart));
        System.out.println("\n" + BaseController.formatMenuMessageOption("0", "Back"));
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
