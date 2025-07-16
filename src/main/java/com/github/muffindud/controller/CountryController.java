package com.github.muffindud.controller;

import com.github.muffindud.enums.Country;
import com.github.muffindud.enums.MenuContext;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CountryController extends BaseController {
    public CountryController() {
        super();
    }

    private void sendCountryMenu() {
        String message = "Select country:\n"
                + IntStream.range(1, Country.values().length + 1)
                        .mapToObj(i -> {
                            Country country = Country.values()[i - 1];
                            return "[" + i + "]. " + country.name() + "\n";
                        })
                        .collect(Collectors.joining())
                + "\n[0]. Back\n";

        System.out.println(message);
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
