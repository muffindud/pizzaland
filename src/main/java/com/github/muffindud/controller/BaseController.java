package com.github.muffindud.controller;

import com.github.muffindud.enums.MenuContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

@Slf4j
public abstract class BaseController {
    @Getter @Setter private static Scanner scanner;

    private final static Map<MenuContext, Consumer<String>> inputHandler = new HashMap<>();
    private final static Map<String, Runnable> navigationMenu = new HashMap<>();
    private static String navigationMenuMessage = "";

    protected static String readInput() {
        return BaseController.scanner.nextLine();
    }

    /**
     * Execute the function for the current context passing the input from the console
     */
    public void handleInput() {
        String input;
        boolean operationPermitted;
        do {
            input = BaseController.readInput();
            operationPermitted = this.isOperationPermitted(input);
            if (!operationPermitted) {
                System.out.println("The operation is not permitted: " + input);
            }
        } while (!operationPermitted);

        BaseController.inputHandler.get(this.contextName()).accept(input);
    }

    /**
     * Execute the respective endpoint at the specified key.
     * Reruns if the key is not defined
     */
    private static void handleNavigationMenuInput() {
        BaseController.navigationMenu.getOrDefault(BaseController.readInput(), () -> {
            System.out.println("The key is no defined");
            BaseController.handleNavigationMenuInput();
        }).run();
    }

    /**
     * Send the navigation menu and handle the input
     */
    public static void sendAndHandleNavigationMenu() {
        System.out.println(BaseController.navigationMenuMessage);
        BaseController.handleNavigationMenuInput();
    }

    static {
        BaseController.navigationMenu.put("0", () -> {
            log.info("Exiting...");
            System.exit(0);
        });
        BaseController.navigationMenuMessage += BaseController.formatMenuMessageOption("0", "Exit");
    }

    /**
     * The context in which the input should be handled
     *
     * @return context
     */
    protected abstract MenuContext contextName();

    /**
     * The consumer function which will be executed in it's defined context.
     * The function takes as parameter the input from the console
     *
     * @return function with one String parameter
     */
    protected abstract Consumer<String> contextInputHandler();

    /**
     * The endpoint key which is used to access the context from the navigation menu
     *
     * @return endpoint key
     */
    protected abstract String actionKey();

    /**
     * The name of the endpoint used to display in the navigation menu
     *
     * @return endpoint name
     */
    protected abstract String actionName();

    /**
     * The method that will be run upon accessing the respective endpoint key.
     * Should have menu print with key handler
     *
     * @return endpoint method
     */
    protected abstract Runnable action();

    /**
     * Checker for the input handler to verify if the input is a supported operation
     *
     * @param input to check
     * @return `true` if operation is supported, `false` if otherwise
     */
    protected abstract boolean isOperationPermitted(String input);

    public static String formatMenuMessageOption(String input, String message) {
        return "[" + input + "]. " + message + "\n";
    }

    BaseController() {
        BaseController.inputHandler.put(this.contextName(), this.contextInputHandler());
        log.info("Added handler for \"{}\"", this.contextName());

        BaseController.navigationMenu.put(this.actionKey(), () -> {
            this.action().run();
            this.handleInput();
        });
        BaseController.navigationMenuMessage += BaseController.formatMenuMessageOption(this.actionKey(), this.actionName());
        log.info("Added \"{}\" action with key \"{}\"", this.actionName(), this.actionKey());
    }
}
