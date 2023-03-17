package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.OrderCreateRequest;
import com.example.ordermanagement.dto.OrderCreateResponse;
import com.example.ordermanagement.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ordermanagement.mapper.OrderMapper.ORDER_MAPPER;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreateResponse createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        String orderId = orderService.createOrder(ORDER_MAPPER.toOrderModel(orderCreateRequest));
        return OrderCreateResponse.builder().orderId(orderId).build();
    }

}
