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

    private static String readInput() {
        return BaseController.scanner.nextLine();
    }

    public static void handleInput(MenuContext context) {
        BaseController.inputHandler.get(context).accept(readInput());
    }

    public static void sendNavigationMenu() {
        System.out.println(BaseController.navigationMenuMessage);
    }

    public static void handleNavigationMenuInput() {
        BaseController.navigationMenu.getOrDefault(BaseController.readInput(), () -> {
            System.out.println("Nope");
            BaseController.handleNavigationMenuInput();
        }).run();
    }

    static {
        BaseController.navigationMenu.put("0", () -> {
            log.info("Exiting...");
            System.exit(0);
        });
        BaseController.navigationMenuMessage += "[0]. Exit \n";
    }

    protected abstract MenuContext contextName();
    protected abstract Consumer<String> contextInputHandler();
    protected abstract String actionKey();
    protected abstract String actionName();
    protected abstract Runnable action();

    BaseController() {
        BaseController.inputHandler.put(this.contextName(), this.contextInputHandler());
        log.info("Added handler for \"{}\"", this.contextName());

        BaseController.navigationMenu.put(this.actionKey(), this.action());
        BaseController.navigationMenuMessage += "[" + this.actionKey() + "]. " + this.actionName() + "\n";
        log.info("Added \"{}\" action with key \"{}\"", this.actionName(), this.actionKey());
    }
}
