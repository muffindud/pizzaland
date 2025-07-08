package com.github.muffindud.service.impl;

import com.github.muffindud.controller.BaseController;
import com.github.muffindud.controller.CartController;
import com.github.muffindud.controller.CountryController;
import com.github.muffindud.controller.PizzaMenuController;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.model.Cart;
import com.github.muffindud.model.PizzaMenu;
import com.github.muffindud.publisher.EventManager;
import com.github.muffindud.service.BaseService;

public final class PizzaLand extends BaseService {
    private final EventManager eventManager = new EventManager();

    private final PizzaMenu pizzaMenu = new PizzaMenu();
    private final Cart cart = new Cart();

    private final PizzaMenuController pizzaMenuController = new PizzaMenuController(pizzaMenu);
    private final CartController cartController = new CartController(cart, eventManager);
    private final CountryController countryController = new CountryController();

    public PizzaLand() {
        this.eventManager.subscribe(this.pizzaMenuController, NotificationTopic.DISCOUNT_APPLIED);
        this.eventManager.subscribe(this.pizzaMenuController, NotificationTopic.DISCOUNT_NOT_APPLIED);

//        this.inputHandler.put(MenuContext.BASE_MENU, this::handleNavigationMenuInput);
//        this.inputHandler.put(MenuContext.COUNTRY_SELECTOR, this::handleCountryInput);

//        this.navigationMenu.put("3", this::sendCountryMenu);
//        this.navigationMenu.put("0", () -> System.exit(0));
    }

    @Override
    protected void entrypoint() {
        BaseController.sendNavigationMenu();
    }
}
