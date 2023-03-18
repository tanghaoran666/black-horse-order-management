package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.RedEnvelopeDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RedEnvelopeDeductionRepository extends JpaRepository<RedEnvelopeDeduction, Long> {
    @Query("select r from RedEnvelopeDeduction r where r.status = 'FAIL' and r.retryTimes < 10 ")
    List<RedEnvelopeDeduction> findRetryRedEnvelopeDeduction();
}
