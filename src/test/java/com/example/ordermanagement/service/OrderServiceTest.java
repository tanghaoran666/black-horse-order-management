package com.example.ordermanagement.service;

import com.example.ordermanagement.client.MerchantManagementClient;
import com.example.ordermanagement.domainModel.OrderMealModel;
import com.example.ordermanagement.domainModel.OrderModel;
import com.example.ordermanagement.dto.MealDetailDto;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MerchantManagementClient merchantManagementClient;

    @Test
    void should_create_order_success() {
        when(merchantManagementClient.getMealDetail(any())).thenReturn(
                Collections.singletonList(MealDetailDto.builder().mealId(1L)
                        .price(BigDecimal.TEN).build()));
        when(orderRepository.save(any())).thenReturn(Order.builder().id("o1").build());

        OrderModel orderBO = OrderModel.builder()
                .meals(Collections.singletonList(
                        OrderMealModel.builder().mealId("1L").quantity(1).build()))
                .build();

        String orderId = orderService.createOrder(orderBO);
        assertNotNull(orderId);
    }


}
