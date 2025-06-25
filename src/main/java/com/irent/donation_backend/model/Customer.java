package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    private NGOEnvItem fields;

    @Schema(description = "商店key")
    @JsonAlias("record_id")
    private String recordId;
}
