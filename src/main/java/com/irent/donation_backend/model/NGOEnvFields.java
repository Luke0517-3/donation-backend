package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NGOEnvFields {
    @JsonProperty("PATH")
    private String path;

    @JsonProperty("STORE_KEY")
    private String storeKey;

    @JsonProperty("STORE_NAME")
    private String storeName;

    @JsonProperty("STORE_VALUE")
    private String storeValue;

    @JsonProperty("IS_PRESET_PERIODIC_PAYMENT_MONTH")
    private Boolean hasDefaultPeriodMonths;

    @JsonProperty("IS_PRESET_AMOUNT")
    private Boolean hasDefaultAmount;

    @JsonProperty("DEFAULT_MONTH")
    private String defaultMonths;

    @JsonProperty("DEFAULT_AMOUNT")
    private String defaultAmount;
}
