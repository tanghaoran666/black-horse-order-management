package com.example.ordermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Order {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    @OneToMany
    private List<OrderMeal> meals;

    private String address;
    private String phoneNumber;
    private String contactName;
    private String remark;
    private String createdBy;
    private BigDecimal totalPrice;
    @CreatedDate
    private Date createdTime;
    @LastModifiedDate
    private Date updatedTime;
}
