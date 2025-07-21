package com.github.muffindud.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public abstract class BaseController {
    // Should be passed from the service
    @Setter private static Scanner scanner;

    private final static Map<String, Runnable> navigationMenu = new HashMap<>();
    private static String navigationMenuMessage = "";

    /**
     * Read the input from the console screen
     *
     * @return user input
     */
    protected static String readInput() {
        return BaseController.scanner.nextLine();
    }

    /**
     * Execute the function for the current context passing the input from the console and constraining within the rules
     * declared in `isOperationPermitted`
     */
    public void handleInput() {
        BaseController.runWithInputCheck(
                this::isOperationPermitted,
                "Selection: ",
                "The operation is not permitted",
                this::contextInputHandler
        );
    }

    /**
     * Execute the respective endpoint at the specified key.
     * Reruns if the key is not defined
     */
    private static void handleNavigationMenuInput() {
        System.out.print("Selection: ");

        // Go to the selected menu (if it exists and it's declared)
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
        // Add an exit endpoint and message
        BaseController.navigationMenu.put("0", () -> {
            log.info("Exiting...");
            System.out.println("\nGoodbye!");
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
     * Format the endpoint and message for printing to the screen.
     * Used when handling the input within the overridden context
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
    public static int getNonNegativeIntegerInput() {
        AtomicReference<Integer> result = new AtomicReference<>();
        BaseController.runWithInputCheck(
                input -> input.matches("\\d+"),
                "Count: ",
                "Please type a positive integer number",
                input -> result.set(Integer.parseInt(input))
        );
        return result.get();
    }

    /**
     * Run the `methodToRun` function with the user input as parameter.
     * The user input is checked using the Predicate `checker` executed on the user input.
     * When reading the input `promptMessage` is displayed to the screen.
     * If the input is not valid `errorMessage` is displayed and the user is prompted again.
     *
     * @param checker predicate function to check the user input
     * @param promptMessage to display before reading the input
     * @param errorMessage to display if the input is not valid
     * @param methodToRun to run with the validated user input
     */
    public static void runWithInputCheck(
            Predicate<String> checker,
            String promptMessage,
            String errorMessage,
            Consumer<String> methodToRun
    ) {
        String input;
        boolean inputValid;

        do {
            System.out.print(promptMessage);
            input = BaseController.readInput();
            inputValid = checker.test(input);
            if (!inputValid) {
                System.out.println(errorMessage);
            }
        } while (!inputValid);

        methodToRun.accept(input);
    }

    public static void sendDelimiter() {
        System.out.println("================================================================");
    }

    BaseController() {
        // Add the endpoint key and call to handle within the overridden context
        BaseController.navigationMenu.put(this.actionKey(), () -> {
            BaseController.sendDelimiter();
            this.sendMenuAndHandleInput();
        });

        // Format the navigation message menu to add all the endpoints from the navigaion menu
        BaseController.navigationMenuMessage += BaseController.formatMenuMessageOption(this.actionKey(), this.actionName());
        log.info("Added \"{}\" action with key \"{}\"", this.actionName(), this.actionKey());
    }
}
