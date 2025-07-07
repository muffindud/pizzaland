package com.github.muffindud.view;

import com.github.muffindud.model.Pizza;
import com.github.muffindud.model.PizzaMenu;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PizzaMenuView {
    public static String getMenu(PizzaMenu pizzaMenu, int page, int count) {
        final List<Pizza> pizzaMenuSubslice = pizzaMenu.getPizzas().subList(page * count, (page + 1) * count);

        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Pizza pizza = pizzaMenuSubslice.get(i);
                    return "[" + (i + 1) + "]. " + pizza.getName() + ": " + pizza.getPrice() + "\n";
                })
                .collect(Collectors.joining());
    }

    public static String getMenu(PizzaMenu pizzaMenu) {
        return IntStream.range(0, pizzaMenu.getPizzas().size())
                .mapToObj(i -> {
                    Pizza pizza = pizzaMenu.getPizzas().get(i);
                    return "[" + (i + 1) + "]. " + pizza.getName() + ": " + pizza.getPrice() + "\n";
                })
                .collect(Collectors.joining());
    }
}
