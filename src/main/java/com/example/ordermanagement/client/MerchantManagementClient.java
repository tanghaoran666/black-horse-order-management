package com.example.ordermanagement.client;

import com.example.ordermanagement.dto.MealDetailDto;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "merchant-management", url = "http:localhost:8082")
public interface MerchantManagementClient {
    List<MealDetailDto> getMealDetail(List<String> mealIds);
}
