package com.github.muffindud.view;

import com.github.muffindud.model.Ingredient;

public final class IngredientView {
    public static String getIngredientUnitPrice(Ingredient ingredient) {
        return  "Ingredient: " + ingredient.getName() + "\n" +
                "   Unit: " + ingredient.getUnit().getUnitName() + "\n" +
                "   Price per unit: " + ingredient.getPrice() + "\n";
    }
}
