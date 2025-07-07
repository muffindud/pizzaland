package com.github.muffindud.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class PizzaMenu {
    @Getter private final List<Pizza> pizzas = new ArrayList<>();
}
