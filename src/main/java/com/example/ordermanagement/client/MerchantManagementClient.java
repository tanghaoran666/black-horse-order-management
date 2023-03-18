package com.example.ordermanagement.client;

import com.example.ordermanagement.dto.MealDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "merchant-management", url = "http://localhost:8082")
public interface MerchantManagementClient {
    @GetMapping
    List<MealDetailDto> getMealDetail(@RequestParam(value = "meal_ids")List<String> mealIds);
}
