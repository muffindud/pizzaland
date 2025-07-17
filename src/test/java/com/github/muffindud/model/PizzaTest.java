package com.github.muffindud.model;

import com.github.muffindud.builder.PizzaBuilder;
import com.github.muffindud.enums.Crust;
import com.github.muffindud.enums.Unit;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class PizzaTest {
    private static final float FREE_PIZZA_PRICE = 0F;
    private static final float PIZZA_BASE_PRICE = 10F;

    private static final float BASIC_PIZZA_PRICE = 23.5F;
    private static final float BASIC_PIZZA_PRICE_WITH_BASE_PRICE =
            PizzaTest.BASIC_PIZZA_PRICE + PizzaTest.PIZZA_BASE_PRICE;

    private static Pizza createFreePizzaNoBasePrice() {
        PizzaBase freeBase = new PizzaBase("Free Base", 0F, Unit.GRAM);
        PizzaSauce freeSauce = new PizzaSauce("Free Sauce", 0F, Unit.GRAM);
        PizzaTopping freeTopping = new PizzaTopping("Free Topping", 0F, Unit.GRAM);

        return new PizzaBuilder()
                .setName("Free Pizza")
                .setBase(freeBase, 10F)
                .setSauce(freeSauce, 15F)
                .setCrust(Crust.THIN)
                .addTopping(freeTopping, 5F)
                .cook(0, 0)
                .build();
    }

    private static Pizza createFreePizzaWithBasePrice() {
        Pizza pizza = PizzaTest.createFreePizzaNoBasePrice();
        pizza.setBasePrice(PizzaTest.PIZZA_BASE_PRICE);
        return pizza;
    }

    private static Pizza createBasicPizzaNoBasePrice() {
        PizzaBase basicBase = new PizzaBase("Basic Base", 0.5F, Unit.GRAM);
        PizzaSauce basicSauce = new PizzaSauce("Basic Sauce", 0.8F, Unit.GRAM);
        PizzaTopping basicTopping = new PizzaTopping("Basic Topping", 1.3F, Unit.GRAM);

        return new PizzaBuilder()
                .setName("Free Pizza")
                .setBase(basicBase, 10F) // 5
                .setSauce(basicSauce, 15F) // 12
                .setCrust(Crust.THIN)
                .addTopping(basicTopping, 5F) // 6.5
                .cook(150, 10)
                .build(); // Total: 23.5
    }

    private static Pizza createBasicPizzaWithBasePrice() {
        Pizza pizza = PizzaTest.createBasicPizzaNoBasePrice();
        pizza.setBasePrice(PizzaTest.PIZZA_BASE_PRICE); // Total: 33.5
        return pizza;
    }

    @Test
    public void testGetPriceFreePizzaNoBasePrice() {
        Assertions.assertThat(PizzaTest.createFreePizzaNoBasePrice().getPrice())
                .isEqualTo(PizzaTest.FREE_PIZZA_PRICE);
    }

    @Test
    public void testGetPriceFreePizzaWithBasePrice() {
        Assertions.assertThat(PizzaTest.createFreePizzaWithBasePrice().getPrice())
                .isEqualTo(PizzaTest.PIZZA_BASE_PRICE);
    }

    @Test
    public void testGetPriceBasicPizzaNoBasePrice() {
        Assertions.assertThat(PizzaTest.createBasicPizzaNoBasePrice().getPrice())
                .isEqualTo(PizzaTest.BASIC_PIZZA_PRICE);
    }

    @Test
    public void testGetPriceBasicPizzaWithBasePrice() {
        Assertions.assertThat(PizzaTest.createBasicPizzaWithBasePrice().getPrice())
                .isEqualTo(PizzaTest.BASIC_PIZZA_PRICE_WITH_BASE_PRICE);
    }
}
