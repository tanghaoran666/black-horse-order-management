package com.example.ordermanagement.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@FeignClient(name = "red_envelope-management", url = "http://localhost:8083")
public interface RedEnvelopeManagementClient {
    @PostMapping("red-envelopes/{rid}/deduction/")
    BigDecimal deduction(@PathVariable("rid") String redEnvelopeId);

}
