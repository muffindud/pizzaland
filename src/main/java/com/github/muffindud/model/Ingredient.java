package com.github.muffindud.model;

// Using a composite pattern in case I want to make the ingredients complex
public final class Ingredient extends Product {
    @Override
    public float getPrice() {
        return this.price;
    }
}
