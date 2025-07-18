package com.github.muffindud.controller;

import com.github.muffindud.enums.Country;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.publisher.EventManager;
import com.github.muffindud.view.CountryView;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class CountryController extends BaseController {
    private final EventManager eventManager;

    public CountryController(EventManager eventManager) {
        super();

        this.eventManager = eventManager;
    }

    @Override
    protected void contextInputHandler(String input) {
        log.info("Received {}", input);

        if (Objects.equals(input, "0")) {
            log.info("Returning to navigation menu");
        } else {
            Country country = Country.values()[Integer.parseInt(input) - 1];
            log.info("Changed country to {}", country.name());
            eventManager.notifySubscribers(NotificationTopic.COUNTRY_CHANGE, country);
        }

        BaseController.sendAndHandleNavigationMenu();
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
    protected void sendMenuMessage() {
        System.out.println(CountryView.getCountryMenuView());
    }

    @Override
    protected boolean isOperationPermitted(String input) {
        boolean operationPermitted;
        try {
            int selection = Integer.parseInt(input);
            operationPermitted = Country.values().length >= selection;
        } catch (NumberFormatException e) {
            operationPermitted = false;
        }

        return operationPermitted;
    }
}
