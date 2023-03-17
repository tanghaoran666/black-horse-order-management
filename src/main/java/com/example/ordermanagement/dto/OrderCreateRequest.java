package com.example.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    private String address;
    private String phoneNumber;
    private String contactName;
    private String remark;
    private List<OrderMealDto> meals;
}
