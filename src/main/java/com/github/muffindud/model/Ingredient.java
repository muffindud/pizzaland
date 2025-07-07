package com.github.muffindud.model;

import com.github.muffindud.enums.Unit;
import lombok.Getter;
import lombok.Setter;

// Using a composite pattern in case I want to make the ingredients complex
public final class Ingredient extends Product {
    @Getter @Setter private Unit unit;

    @Override
    public float getPrice() {
        return this.price;
    }
}
