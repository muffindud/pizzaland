package com.github.muffindud.view;

import com.github.muffindud.model.Ingredient;

public final class IngredientView {
    public String getIngredientUnitPrice(Ingredient ingredient) {
        final StringBuilder sb = new StringBuilder();

        sb.append("Ingredient: ")
                .append(ingredient.getName())
                .append("\n");
        sb.append("Unit: ")
                .append(ingredient.getUnit().getUnitName())
                .append("\n");
        sb.append("Price per unit: ")
                .append(ingredient.getPrice())
                .append("\n");

        return sb.toString();
    }
}
