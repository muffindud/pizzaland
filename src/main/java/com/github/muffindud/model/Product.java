package com.github.muffindud.model;

import lombok.Getter;
import lombok.Setter;

public abstract class Product {
    @Getter @Setter protected String name;
    @Setter protected float price;

    public abstract float getPrice();
}
