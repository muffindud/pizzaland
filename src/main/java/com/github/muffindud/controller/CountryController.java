package com.github.muffindud.controller;

import com.github.muffindud.enums.Country;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.publisher.EventManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        // TODO: Use CountryView here
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
