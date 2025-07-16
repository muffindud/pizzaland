package com.github.muffindud.factory.impl;

import com.github.muffindud.builder.PizzaBuilder;
import com.github.muffindud.collection.IngredientCollection;
import com.github.muffindud.enums.Crust;
import com.github.muffindud.factory.PizzaFactory;
import com.github.muffindud.model.Pizza;

public final class AmericanPizzaFactory implements PizzaFactory {
    @Override
    public Pizza createSausagePizza() {
        return new PizzaBuilder()
                .setName("American Sausage Pizza")
                .setBase(IngredientCollection.wholeWheatDough, 100)
                .setCrust(Crust.THICK)
                .setSauce(IngredientCollection.tomatoSauce, 50)
                .addTopping(IngredientCollection.americanSausages, 100)
                .addTopping(IngredientCollection.mushrooms, 20)
                .cook(150, 20)
                .build();
    }

    @Override
    public Pizza createVegetarianPizza() {
        return new PizzaBuilder()
                .setName("American Vegetarian Pizza")
                .setBase(IngredientCollection.glutenFreeDough, 100)
                .setCrust(Crust.THICK)
                .setSauce(IngredientCollection.tomatoSauce, 50)
                .addTopping(IngredientCollection.americanCheese, 100)
                .addTopping(IngredientCollection.pineapple, 100)
                .addTopping(IngredientCollection.mushrooms, 20)
                .cook(150, 15)
                .build();
    }

    @Override
    public Pizza createVeganPizza() {
        return new PizzaBuilder()
                .setName("American Vegan Pizza")
                .setBase(IngredientCollection.glutenFreeDough, 100)
                .setCrust(Crust.THIN)
                .setSauce(IngredientCollection.tomatoSauce, 50)
                .addTopping(IngredientCollection.pineapple, 200)
                .addTopping(IngredientCollection.mushrooms, 30)
                .cook(150, 15)
                .build();
    }
}
