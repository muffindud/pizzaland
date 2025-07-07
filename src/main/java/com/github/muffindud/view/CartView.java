package com.github.muffindud.view;

import com.github.muffindud.model.Cart;
import com.github.muffindud.model.Product;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CartView {
    public static String getCartComposition(Cart cart) {
        final Iterator<Map.Entry<Product, Integer>> cartEntryIterator = cart.getProductQty().entrySet().iterator();

        return  "You currently have: \n" +
                IntStream.range(1, cart.getProductQty().size() + 1)
                        .mapToObj(i -> {
                            Map.Entry<Product, Integer> entry = cartEntryIterator.next();
                            return "[" + i + "]. " + entry.getValue() + " x " + entry.getKey().getName() + ": " + entry.getKey().getPrice() * entry.getValue() + "\n";
                        })
                        .collect(Collectors.joining()) +
                "Total price: " + cart.getPrice();
    }
}
