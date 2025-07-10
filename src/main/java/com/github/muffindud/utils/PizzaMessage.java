package com.github.muffindud.utils;

import com.github.muffindud.model.Pizza;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class PizzaMessage {
    @Getter @Setter private Pizza pizza;
    @Getter @Setter private int count;
}
