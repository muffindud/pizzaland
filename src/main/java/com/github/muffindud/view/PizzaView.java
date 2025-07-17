package com.github.muffindud.view;

import com.github.muffindud.model.Ingredient;
import com.github.muffindud.model.Pizza;

import java.util.function.Function;
import java.util.stream.Collectors;

public final class PizzaView {
    public static String getPizzaInfo(Pizza pizza, Function<Ingredient, String> ingredientView) {
        return "Pizza: " + pizza.getName() + "\n" +
                "    Base: " + ingredientView.apply(pizza.getBaseQty().getLeft()) +
                "       Quantity: " + pizza.getBaseQty().getRight() + "\n" +
                "   Crust: " + pizza.getCrust().name().toLowerCase() + "\n" +
                "   Sauce: " + ingredientView.apply(pizza.getSauceQty().getLeft()) +
                "       Quantity: " + pizza.getSauceQty().getRight() + "\n" +
                pizza.getToppingsQty().entrySet().stream()
                        .map(toppingQty ->
                                "   Topping: " + ingredientView.apply(toppingQty.getKey()) +
                                "       Quantity: " + toppingQty.getValue() + "\n"
                        )
                        .collect(Collectors.joining());
    }
}
