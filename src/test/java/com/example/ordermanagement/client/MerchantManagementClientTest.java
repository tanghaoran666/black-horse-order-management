package com.example.ordermanagement.client;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.ordermanagement.OrderManagementApplication;
import java.util.Collections;
import java.util.List;

import com.example.ordermanagement.dto.MealDetailDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(stubs = "classpath:/stubs", port = 8082)
@SpringBootTest(classes = OrderManagementApplication.class)
class MerchantManagementClientTest {

    @Autowired
    private MerchantManagementClient merchantManagementClient;

    @Test
    void should_find_food_by_codes_success() {
        List<MealDetailDto> mealDetails = merchantManagementClient.getMealDetail(Collections.singletonList("M01"));
        assertEquals(mealDetails.size(), 1);
    }
}