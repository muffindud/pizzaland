package com.github.muffindud.model;

import lombok.Getter;
import lombok.Setter;

// Using a composite pattern in case I want to make the ingredients complex
public final class Ingredient implements Product {
    @Getter
    @Setter
    private String name;

    private float unitPrice;

    @Override
    public float getPrice() {
        return this.unitPrice;
    }
}
