package com.github.muffindud.view;

import com.github.muffindud.model.Cart;
import com.github.muffindud.model.Product;

import java.util.Map;

public class CartView {
    public String getCartComposition(Cart cart) {
        StringBuilder sb = new StringBuilder();
        sb.append("You currently have: \n");

        for (Map.Entry<Product, Integer> productQty : cart.getProductQty().entrySet()) {
            sb.append(productQty.getValue())
                    .append(" x ")
                    .append(productQty.getKey().getName())
                    .append("\n");
        }

        sb.append("Total price: ")
                .append(cart.getPrice());

        return sb.toString();
    }
}
