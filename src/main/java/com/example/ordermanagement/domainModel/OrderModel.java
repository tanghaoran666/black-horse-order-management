package com.example.ordermanagement.domainModel;

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
public class OrderModel {
    private String address;
    private String phoneNumber;
    private String contactName;
    private String remark;
    private List<OrderMealModel> meals;
}
