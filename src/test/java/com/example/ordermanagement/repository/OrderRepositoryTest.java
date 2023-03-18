package com.example.ordermanagement.repository;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.ordermanagement.OrderManagementApplication;
import java.util.Collections;

import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderMeal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderManagementApplication.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void should_save_order_success() {
        Order order = Order.builder().id("oid").build();
        OrderMeal orderMeal = OrderMeal.builder().mealId("A01").quantity(1).order(order).build();
        order.setMeals(Collections.singletonList(orderMeal));
        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());
    }
}
