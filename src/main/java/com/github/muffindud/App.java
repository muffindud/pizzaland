package com.github.muffindud;

import com.github.muffindud.controller.MenuController;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.enums.Unit;
import com.github.muffindud.model.Cart;
import com.github.muffindud.model.Ingredient;
import com.github.muffindud.model.Pizza;
import com.github.muffindud.model.PizzaMenu;
import com.github.muffindud.publisher.EventManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App 
{
    public static final EventManager eventManager = new EventManager();

    public static final Cart cart = new Cart(eventManager);
    public static final Ingredient testIngredient = new Ingredient();
    public static final Pizza testPizza = new Pizza();
    public static final PizzaMenu testPizzaMenu = new PizzaMenu();

    public static final MenuController menuController = new MenuController(testPizzaMenu, cart);

    public static void main( String[] args ) {
        eventManager.subscribe(menuController, NotificationTopic.DISCOUNT_APPLIED);
        eventManager.subscribe(menuController, NotificationTopic.DISCOUNT_NOT_APPLIED);

        testIngredient.setUnit(Unit.GRAM);
        testIngredient.setName("Unobtainium");
        testIngredient.setPrice(10);

        testPizza.getIngredientQty().put(testIngredient, 10F);
        testPizza.setName("Pizza with unobtainium");

        testPizzaMenu.getPizzas().add(testPizza);

        menuController.run();
    }
}
