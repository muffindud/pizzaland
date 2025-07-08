package com.github.muffindud.service;

import com.github.muffindud.controller.BaseController;

import java.util.Scanner;

public abstract class BaseService {
    public final void run() {
        try (Scanner s = new Scanner(System.in)) {
            BaseController.setScanner(s);
            this.entrypoint();
            BaseController.handleNavigationMenuInput();
            // TODO: Add logic continuation
        }
    }

    protected abstract void entrypoint();
}
