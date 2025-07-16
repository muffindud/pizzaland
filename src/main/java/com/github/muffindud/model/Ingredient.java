package com.github.muffindud.model;

import com.github.muffindud.enums.Unit;
import lombok.Getter;

// Using a composite pattern in case I want to make the ingredients complex
public abstract class Ingredient extends Product {
    public Ingredient(String name, float price, Unit unit) {
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    @Getter private final Unit unit;

    @Override
    public final float getPrice() {
        return this.price;
    }
}
