package com.github.muffindud.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public abstract class BaseController {
    @Getter @Setter private static Scanner scanner;

    private final static Map<String, Runnable> navigationMenu = new HashMap<>();
    private static String navigationMenuMessage = "";

    protected static String readInput() {
        return BaseController.scanner.nextLine();
    }

    /**
     * Execute the function for the current context passing the input from the console and constraining within the rules
     * declared in `isOperationPermitted`
     */
    public void handleInput() {
        String input;
        boolean operationPermitted;
        do {
            System.out.print("Selection: ");
            input = BaseController.readInput();
            operationPermitted = this.isOperationPermitted(input);
            if (!operationPermitted) {
                System.out.println("The operation is not permitted: " + input);
            }
        } while (!operationPermitted);

        this.contextInputHandler(input);
    }

    /**
     * Execute the respective endpoint at the specified key.
     * Reruns if the key is not defined
     */
    private static void handleNavigationMenuInput() {
        System.out.print("Selection: ");
        BaseController.navigationMenu.getOrDefault(BaseController.readInput(), () -> {
            System.out.println("The key is no defined");
            BaseController.handleNavigationMenuInput();
        }).run();
    }

    /**
     * Send the navigation menu and handle the input
     */
    public static void sendAndHandleNavigationMenu() {
        BaseController.sendDelimiter();
        System.out.println("Select your option:");
        System.out.println(BaseController.navigationMenuMessage);
        BaseController.handleNavigationMenuInput();
    }

    static {
        BaseController.navigationMenu.put("0", () -> {
            log.info("Exiting...");
            System.out.println("\nGoodbye!");
            System.exit(0);
        });
        BaseController.navigationMenuMessage += BaseController.formatMenuMessageOption("0", "Exit");
    }

    /**
     * Handle the input received in the contexts the function is overridden in
     *
     * @param input the passed String from the user, should be validated using `isOperationPermitted`
     */
    protected abstract void contextInputHandler(String input);

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
     * Send the formated message for the menu contexts it's overridden for
     */
    protected abstract void sendMenuMessage();

    /**
     * Checker for the input handler to verify if the input is a supported operation
     *
     * @param input to check
     * @return `true` if operation is supported, `false` if otherwise
     */
    protected abstract boolean isOperationPermitted(String input);

    /**
     * Format the endpoint and message for printing to the screen
     *
     * @param input endpoint key
     * @param message endpoint message
     * @return formated string row
     */
    public static String formatMenuMessageOption(String input, String message) {
        return "[" + input + "]. " + message + "\n";
    }

    /**
     * Send the context specific menu message and listen for the user input
     */
    protected void sendMenuAndHandleInput() {
        this.sendMenuMessage();
        this.handleInput();
    }

    /**
     * Get the user input that is enforced to be an int
     *
     * @return user input as an integer number
     */
    public static int getNonNegativeNumericalInput() {
        String input;
        boolean inputIsNumber;

        do {
            System.out.print("Count: ");
            input = BaseController.readInput();
            inputIsNumber = input.matches("\\d+");
            if (!inputIsNumber) {
                System.out.println("Please type a positive integer number");
            }
        } while (!inputIsNumber);

        return Integer.parseInt(input);
    }

    public static void sendDelimiter() {
        System.out.println("================================================================");
    }

    BaseController() {
        BaseController.navigationMenu.put(this.actionKey(), () -> {
            BaseController.sendDelimiter();
            this.sendMenuAndHandleInput();
        });
        BaseController.navigationMenuMessage += BaseController.formatMenuMessageOption(this.actionKey(), this.actionName());
        log.info("Added \"{}\" action with key \"{}\"", this.actionName(), this.actionKey());
    }
}
