package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.ErrorResult;
import com.example.ordermanagement.dto.OrderCreateRequest;
import com.example.ordermanagement.dto.OrderCreateResponse;
import com.example.ordermanagement.enums.ErrorCode;
import com.example.ordermanagement.exception.NotFoundException;
import com.example.ordermanagement.exception.ServerUnavailableException;
import com.example.ordermanagement.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        try {
            String orderId = orderService.createOrder(ORDER_MAPPER.toOrderModel(orderCreateRequest));
            OrderCreateResponse orderCreateResponse = OrderCreateResponse.builder().orderId(orderId).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(orderCreateResponse);
        } catch (NotFoundException e) {
            ErrorCode errorCode = e.getErrorCode();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResult(errorCode.name(), errorCode.getMessage()));
        } catch (ServerUnavailableException e) {
            ErrorCode errorCode = e.getErrorCode();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResult(errorCode.name(), errorCode.getMessage()));
        }
    }

}
