package com.example.ordermanagement.service;

import com.example.ordermanagement.client.MerchantManagementClient;
import com.example.ordermanagement.domainModel.OrderModel;
import com.example.ordermanagement.dto.MealDetailDto;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderMeal;
import com.example.ordermanagement.exception.NotFoundException;
import com.example.ordermanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ordermanagement.mapper.OrderMapper.ORDER_MAPPER;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MerchantManagementClient merchantManagementClient;


    public String createOrder(OrderModel orderModel) {
        Order order = ORDER_MAPPER.toOrder(orderModel);
        List<String> mealIds = order.getMeals().stream().map(OrderMeal::getMealId).collect(Collectors.toList());

        List<MealDetailDto> availableMealDetails = merchantManagementClient.getMealDetail(mealIds);
        validateMealMatch(mealIds, availableMealDetails);

        order.setTotalPrice(getTotalPrice(availableMealDetails));
        Order orderSaved = orderRepository.save(order);
        return orderSaved.getId();
    }

    private static void validateMealMatch(List<String> mealIds, List<MealDetailDto> availableMealDetails) {
        List<String> availableMealIds = availableMealDetails.stream()
                .map(MealDetailDto::getMealId).collect(Collectors.toList());
        boolean anyFoodNotFound = mealIds.stream().anyMatch(mealId -> !availableMealIds.contains(mealId));
        if (anyFoodNotFound) {
            throw new NotFoundException();
        }
    }

    private static BigDecimal getTotalPrice(List<MealDetailDto> mealDetails) {
        return mealDetails.stream()
                .map(meal -> meal.getPrice().multiply(BigDecimal.valueOf(meal.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
