package com.github.muffindud.model;

import lombok.Getter;

public abstract class Product {
    @Getter protected String name;
    protected float price;

    public abstract float getPrice();
}
