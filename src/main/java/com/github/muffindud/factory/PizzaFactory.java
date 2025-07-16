package com.github.muffindud.factory;

import com.github.muffindud.model.Pizza;

public interface PizzaFactory {
    Pizza createSausagePizza();

    Pizza createVegetarianPizza();

    Pizza createVeganPizza();
}
