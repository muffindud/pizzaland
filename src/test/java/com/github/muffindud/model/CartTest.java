package com.github.muffindud.model;

import com.github.muffindud.config.ConfigProvider;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartTest {
    private static final float UNDER_THRESHOLD_TGT = 20F;

    private static final float THRESHOLD = ConfigProvider.getDiscount().discountPrice();
    private static final float THRESHOLD_DISCOUNTED =
            CartTest.THRESHOLD - CartTest.THRESHOLD * ConfigProvider.getDiscount().discountRate();

    private static final float OVER_THRESHOLD_TGT = CartTest.THRESHOLD + 20F;
    private static final float OVER_THRESHOLD_DISCOUNTED =
            CartTest.OVER_THRESHOLD_TGT - CartTest.OVER_THRESHOLD_TGT * ConfigProvider.getDiscount().discountRate();

    private static Cart createEmptyCart() {
        Cart cart = new Cart();
        Product product = mock(Product.class);

        when(product.getPrice()).thenReturn(0F);
        cart.getProductQty().put(product, 1);

        return cart;
    }

    private static Cart createCartUnderThreshold() {
        Cart cart = new Cart();
        Product product = mock(Product.class);

        when(product.getPrice()).thenReturn(CartTest.UNDER_THRESHOLD_TGT);
        cart.getProductQty().put(product, 1);

        return cart;
    }

    private static Cart createCartAtThreshold() {
        Cart cart = new Cart();
        Product product = mock(Product.class);

        when(product.getPrice()).thenReturn(CartTest.THRESHOLD);
        cart.getProductQty().put(product, 1);

        return cart;
    }

    private static Cart createCartOverThreshold() {
        Cart cart = new Cart();
        Product product = mock(Product.class);

        when(product.getPrice()).thenReturn(CartTest.OVER_THRESHOLD_TGT);
        cart.getProductQty().put(product, 1);

        return cart;
    }

    @Test
    public void testGetPriceZero() {
        Assertions.assertThat(CartTest.createEmptyCart().getPrice())
                .isEqualTo(0F);
    }

    @Test
    public void testGetPriceUnderThreshold() {
        Assertions.assertThat(CartTest.createCartUnderThreshold().getPrice())
                .isEqualTo(CartTest.UNDER_THRESHOLD_TGT);
    }

    @Test
    public void testGetPriceAtThreshold() {
        Assertions.assertThat(CartTest.createCartAtThreshold().getPrice())
                .isEqualTo(CartTest.THRESHOLD_DISCOUNTED);
    }

    @Test
    public void testGetPriceOverThreshold() {
        Assertions.assertThat(CartTest.createCartOverThreshold().getPrice())
                .isEqualTo(CartTest.OVER_THRESHOLD_DISCOUNTED);
    }

    @Test
    public void isPriceEqualOrOverThresholdZero() {
        Assertions.assertThat(CartTest.createEmptyCart().isPriceEqualOrOverThreshold(THRESHOLD))
                .isFalse();
    }

    @Test
    public void isPriceEqualOrOverThresholdUnderThreshold() {
        Assertions.assertThat(CartTest.createCartUnderThreshold().isPriceEqualOrOverThreshold(THRESHOLD))
                .isFalse();
    }

    @Test
    public void isPriceEqualOrOverThresholdAtThreshold() {
        Assertions.assertThat(CartTest.createCartAtThreshold().isPriceEqualOrOverThreshold(THRESHOLD))
                .isTrue();
    }

    @Test
    public void isPriceEqualOrOverThresholdOverThreshold() {
        Assertions.assertThat(CartTest.createCartOverThreshold().isPriceEqualOrOverThreshold(THRESHOLD))
                .isTrue();
    }
}
