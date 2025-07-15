package com.github.muffindud.service;

import com.github.muffindud.controller.BaseController;

import java.util.Scanner;

public abstract class BaseService {
    /**
     * Start the service
     */
    public final void run() {
        try (Scanner s = new Scanner(System.in)) {
            BaseController.setScanner(s);
            this.entrypoint();
            BaseController.handleNavigationMenuInput();
            // TODO: Add logic continuation
        }
    }

    /**
     * The initial menu print
     */
    protected abstract void entrypoint();
}
