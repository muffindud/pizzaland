package com.github.muffindud.collection;

import com.github.muffindud.enums.Unit;
import com.github.muffindud.model.PizzaBase;
import com.github.muffindud.model.PizzaSauce;
import com.github.muffindud.model.PizzaTopping;

public interface IngredientCollection {
    PizzaBase wholeWheatDough = new PizzaBase("Whole Wheat Dough", 0.01F, Unit.GRAM);
    PizzaBase glutenFreeDough = new PizzaBase("Gluten Free Dough", 0.03F, Unit.GRAM);
    PizzaBase breadFlourDough = new PizzaBase("Bread Flour Dough", 0.02F, Unit.GRAM);

    PizzaSauce tomatoSauce = new PizzaSauce("Tomato Sauce", 0.05F, Unit.GRAM);
    PizzaSauce spicyTomatoSauce = new PizzaSauce("Spicy Tomato Sauce", 0.07F, Unit.GRAM);
    PizzaSauce alfredoSauce = new PizzaSauce("Alfredo Sauce", 0.06F, Unit.GRAM);
    PizzaSauce mapleSyrup = new PizzaSauce("Maple Syrup", 0.03F, Unit.GRAM);

    PizzaTopping americanSausages = new PizzaTopping("American Sausages", 0.1F, Unit.GRAM);
    PizzaTopping mexicanSausages = new PizzaTopping("Mexican Sausages", 0.07F, Unit.GRAM);
    PizzaTopping prosciutto = new PizzaTopping("Prosciutto", 0.15F, Unit.GRAM);
    PizzaTopping americanCheese = new PizzaTopping("American Cheese", 0.05F, Unit.GRAM);
    PizzaTopping mozzarellaCheese = new PizzaTopping("Mozzarella Cheese", 0.08F, Unit.GRAM);
    PizzaTopping spices = new PizzaTopping("Spices", 0.8F, Unit.GRAM);
    PizzaTopping mushrooms = new PizzaTopping("Champignon Mushrooms", 0.2F, Unit.GRAM);
    PizzaTopping pineapple = new PizzaTopping("Pineapple", 0.15F, Unit.GRAM);
}
