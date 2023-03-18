package com.example.ordermanagement.service;

import com.example.ordermanagement.client.MerchantManagementClient;
import com.example.ordermanagement.domainModel.OrderMealModel;
import com.example.ordermanagement.domainModel.OrderModel;
import com.example.ordermanagement.dto.MealDetailDto;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.exception.NotFoundException;
import com.example.ordermanagement.exception.ServerUnavailableException;
import com.example.ordermanagement.repository.OrderRepository;
import com.rabbitmq.http.client.HttpException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                Collections.singletonList(MealDetailDto.builder().mealId("m1")
                        .price(BigDecimal.TEN).build()));
        when(orderRepository.save(any())).thenReturn(Order.builder().id("o1").build());

        OrderModel orderBO = OrderModel.builder()
                .meals(Collections.singletonList(
                        OrderMealModel.builder().mealId("m1").quantity(1).build()))
                .build();

        String orderId = orderService.createOrder(orderBO);
        assertNotNull(orderId);
    }

    @Test
    void should_throw_not_found_exception_when_user_choos_no_exist_food() {
        when(merchantManagementClient.getMealDetail(any())).thenReturn(
                Collections.singletonList(MealDetailDto.builder().mealId("m1")
                        .price(BigDecimal.TEN).build()));

        OrderModel orderModel = OrderModel.builder()
                .meals(Arrays.asList(
                        OrderMealModel.builder().mealId("m1").quantity(1).build(),
                        OrderMealModel.builder().mealId("m2").quantity(1).build()))
                .build();

        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderModel));
    }

    @Test
    void should_throw_server_unavailable_exception_when_merchant_management_server_unavailable() {
        when(merchantManagementClient.getMealDetail(any())).thenThrow(
                new HttpException("503 merchant management server unavailable"));

        OrderModel orderModel = OrderModel.builder()
                .meals(Arrays.asList(
                        OrderMealModel.builder().mealId("m1").quantity(1).build(),
                        OrderMealModel.builder().mealId("m2").quantity(1).build()))
                .build();

        assertThrows(ServerUnavailableException.class, () -> orderService.createOrder(orderModel));
    }

}
