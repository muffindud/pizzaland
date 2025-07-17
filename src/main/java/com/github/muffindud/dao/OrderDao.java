package com.github.muffindud.dao;

import com.github.muffindud.dto.OrderDto;
import com.github.muffindud.dto.OrderItemDto;
import com.github.muffindud.model.Cart;
import com.github.muffindud.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDao {
    public static OrderDto createOrder(Cart cart) {
        OrderDto order = new OrderDto();
        List<OrderItemDto> orderItems = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : cart.getProductQty().entrySet()) {
            OrderItemDto orderItem = new OrderItemDto();
            orderItem.itemName = entry.getKey().getName();
            orderItem.quantity = entry.getValue();
            orderItem.totalPrice = entry.getKey().getPrice() * orderItem.quantity;
            orderItems.add(orderItem);
        }

        order.items = orderItems.toArray(new OrderItemDto[0]);
        order.totalCost = cart.getPrice();

        return order;
    }
}
