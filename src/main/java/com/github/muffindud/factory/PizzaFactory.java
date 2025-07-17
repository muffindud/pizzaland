package com.github.muffindud.factory;

import com.github.muffindud.enums.Country;
import com.github.muffindud.factory.impl.AmericanPizzaFactory;
import com.github.muffindud.factory.impl.CanadianPizzaFactory;
import com.github.muffindud.factory.impl.EnglishPizzaFactory;
import com.github.muffindud.factory.impl.MexicanPizzaFactory;
import com.github.muffindud.model.Pizza;

public interface PizzaFactory {
    Pizza createSausagePizza();

    Pizza createVegetarianPizza();

    Pizza createVeganPizza();

    static PizzaFactory getFactory(Country country) {
        switch (country) {
            case USA:
                return new AmericanPizzaFactory();
            case UK:
                return new EnglishPizzaFactory();
            case Canada:
                return new CanadianPizzaFactory();
            case Mexico:
                return new MexicanPizzaFactory();
            default:
                throw new UnsupportedOperationException("Country not suppoerted: " + country.name());
        }
    }
}
