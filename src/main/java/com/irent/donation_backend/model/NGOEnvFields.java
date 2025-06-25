package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NGOEnvFields {
    @JsonAlias("PATH")
    @Schema(description = "請求查詢路徑")
    private String path;

    @JsonAlias("STORE_KEY")
    @Schema(description = "商店Key")
    private String storeKey;

    @JsonAlias("STORE_NAME")
    @Schema(description = "商店名稱")
    private String storeName;

    @JsonAlias("STORE_VALUE")
    @Schema(description = "商店Value")
    private String storeValue;

    @JsonAlias("IS_PRESET_PERIODIC_PAYMENT_MONTH")
    @Schema(description = "是否有預設定期定額月數")
    private Boolean hasDefaultPeriodMonths;

    @JsonAlias("IS_PRESET_AMOUNT")
    @Schema(description = "是否有預設金額")
    private Boolean hasDefaultAmount;

    @JsonAlias("DEFAULT_MONTH")
    @Schema(description = "預設月數")
    private String defaultMonths;

    @JsonAlias("DEFAULT_AMOUNT")
    @Schema(description = "預設金額")
    private String defaultAmount;
}
