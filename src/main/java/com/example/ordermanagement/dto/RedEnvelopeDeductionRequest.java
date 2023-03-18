package com.example.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedEnvelopeDeductionRequest {
    private String redEnvelopeId;
    private BigDecimal deductionAmount;
}
