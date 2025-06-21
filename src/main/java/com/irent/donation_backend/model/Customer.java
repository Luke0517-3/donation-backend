package com.irent.donation_backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "捐款目標資訊")
public class Customer {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "商店key")
    private String name;

    @Schema(description = "商店value")
    private String value;

    @Schema(description = "是否有預設金額")
    private Boolean isDefault;

    @Schema(description = "預設金額")
    private BigDecimal amount;
}
