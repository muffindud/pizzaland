package com.github.muffindud.controller;

import com.github.muffindud.enums.MenuContext;

import java.util.function.Consumer;

public final class CountryController extends BaseController {
    public CountryController() {
        super();
    }

    private void sendCountryMenu() {
        // TODO: Show the list of countries
    }

    private void handleCountryInput(String input) {
        // TODO: Select country (with an observer that changes the pizza factory?)
    }

    @Override
    protected MenuContext contextName() {
        return MenuContext.COUNTRY_SELECTOR;
    }

    @Override
    protected Consumer<String> contextInputHandler() {
        return this::handleCountryInput;
    }

    @Override
    protected String actionKey() {
        return "3";
    }

    @Override
    protected String actionName() {
        return "Set current country";
    }

    @Override
    protected Runnable action() {
        return this::sendCountryMenu;
    }
}
