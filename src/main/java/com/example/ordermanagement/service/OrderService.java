package com.example.ordermanagement.service;

import com.example.ordermanagement.client.MerchantManagementClient;
import com.example.ordermanagement.client.RedEnvelopeManagementClient;
import com.example.ordermanagement.domainModel.OrderModel;
import com.example.ordermanagement.domainModel.RedEnvelopeDeductionModel;
import com.example.ordermanagement.dto.MealDetailDto;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderMeal;
import com.example.ordermanagement.entity.RedEnvelopeDeduction;
import com.example.ordermanagement.enums.DeductionStatus;
import com.example.ordermanagement.exception.NotFoundException;
import com.example.ordermanagement.exception.ServerUnavailableException;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.repository.RedEnvelopeDeductionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ordermanagement.mapper.OrderMapper.ORDER_MAPPER;
import static com.example.ordermanagement.mapper.RedEnvelopeDeductionMapper.RED_ENVELOPE_DEDUCTION_MAPPER;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final MerchantManagementClient merchantManagementClient;
    private final RedEnvelopeManagementClient redEnvelopeManagementClient;
    private final RedEnvelopeDeductionRepository redEnvelopeDeductionRepository;


    public String createOrder(OrderModel orderModel) {
        Order order = ORDER_MAPPER.toOrder(orderModel);
        List<String> mealIds = order.getMeals().stream().map(OrderMeal::getMealId).collect(Collectors.toList());
        List<MealDetailDto> availableMealDetails;
        try {
            availableMealDetails = merchantManagementClient.getMealDetail(mealIds);
        } catch (RuntimeException exception) {
            log.error("merchant management server unavailable", exception);
            throw new ServerUnavailableException();
        }
        validateMealMatch(mealIds, availableMealDetails);

        order.setTotalPrice(getTotalPrice(availableMealDetails));
        Order orderSaved = orderRepository.save(order);
        return orderSaved.getId();
    }

    private static void validateMealMatch(List<String> mealIds, List<MealDetailDto> availableMealDetails) {
        List<String> availableMealIds = availableMealDetails.stream()
                .map(MealDetailDto::getMealId).collect(Collectors.toList());
        mealIds.forEach(mealId -> {
            if (!availableMealIds.contains(mealId)) {
                log.debug(String.format("meal not found,mealId:%s", mealId));
                throw new NotFoundException();
            }
        });
    }

    private static BigDecimal getTotalPrice(List<MealDetailDto> mealDetails) {
        return mealDetails.stream()
                .map(meal -> meal.getPrice().multiply(BigDecimal.valueOf(meal.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void redEnvelopeDeduction(String orderId, RedEnvelopeDeductionModel redEnvelopeDeductionModel) {
        Order order = orderRepository.findById(orderId).get();
        try{
            redEnvelopeManagementClient.deduction(redEnvelopeDeductionModel.getRedEnvelopeId());
        } catch (RuntimeException exception) {
            RedEnvelopeDeduction redEnvelopeDeduction = RED_ENVELOPE_DEDUCTION_MAPPER
                    .toRedEnvelopeDeduction(redEnvelopeDeductionModel,orderId,
                    exception.getMessage(), DeductionStatus.FAIL);
            redEnvelopeDeductionRepository.save(redEnvelopeDeduction);
        }
        BigDecimal newTotalPrice = order.getTotalPrice().subtract(redEnvelopeDeductionModel.getDeductionAmount());
        order.setTotalPrice(newTotalPrice);
        orderRepository.save(order);
    }

    public void retryFailRedEnvelopeDeduction() {
        List<RedEnvelopeDeduction> retryFailRedEnvelopeDeductions = redEnvelopeDeductionRepository.findRetryRedEnvelopeDeduction();
        if (retryFailRedEnvelopeDeductions.isEmpty()) {
            return;
        }
        retryFailRedEnvelopeDeductions.forEach(redEnvelopeDeduction -> {
            try {
                redEnvelopeManagementClient.deduction(redEnvelopeDeduction.getRedEnvelopeId());
                redEnvelopeDeduction.setStatus(DeductionStatus.SUCCESS);
            } catch (RuntimeException exception) {
                redEnvelopeDeduction.setFailReason(exception.getMessage());
                int retryTimes = redEnvelopeDeduction.getRetryTimes();
                redEnvelopeDeduction.setRetryTimes(retryTimes + 1);
            }
            redEnvelopeDeductionRepository.save(redEnvelopeDeduction);
        });
    }
}
