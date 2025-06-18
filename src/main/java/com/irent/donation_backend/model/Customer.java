package com.irent.donation_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String id;

    private String name;

    private String value;

    private Boolean isDefault;

    private BigDecimal amount;
}
