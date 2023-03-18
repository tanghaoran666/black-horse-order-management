package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.OrderCreateRequest;
import com.example.ordermanagement.dto.OrderMealDto;
import com.example.ordermanagement.enums.ErrorCode;
import com.example.ordermanagement.exception.NotFoundException;
import com.example.ordermanagement.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void should_create_order_success() throws Exception {
        OrderCreateRequest createRequest = OrderCreateRequest.builder()
                .meals(Collections.singletonList(OrderMealDto.builder().mealId("m1").quantity(2).build()))
                .build();

        String orderId = "o1";
        when(orderService.createOrder(any())).thenReturn(orderId);

        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId));
    }

    @Test
    void should_retunr_error_result_when_meal_not_found() throws Exception {
        OrderCreateRequest createRequest = OrderCreateRequest.builder()
                .meals(Collections.singletonList(OrderMealDto.builder().mealId("m2").quantity(2).build()))
                .build();

        when(orderService.createOrder(any())).thenThrow(new NotFoundException(ErrorCode.MEAL_NOT_FOUND));

        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.MEAL_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.MEAL_NOT_FOUND.getMessage()));
    }
}
