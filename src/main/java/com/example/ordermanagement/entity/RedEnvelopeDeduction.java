package com.example.ordermanagement.entity;

import com.example.ordermanagement.enums.DeductionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "red_envelope_deduction")
public class RedEnvelopeDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String redEnvelopeId;
    private BigDecimal deductionAmount;
    private String orderId;
    @Enumerated(EnumType.STRING)
    private DeductionStatus status;
    private String failReason;
    private int retryTimes;
    @CreatedDate
    private Date createdTime;
    @LastModifiedDate
    private Date updatedTime;
}
