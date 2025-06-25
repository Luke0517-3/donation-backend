package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "多維表格-查詢記錄")
public class NGOEnvItem {
    @JsonAlias("PATH")
    @Schema(description = "請求查詢路徑")
    private List<MultilineText> path;

    @JsonAlias("STORE_KEY")
    @Schema(description = "商店Key")
    private List<MultilineText> storeKey;

    @JsonAlias("STORE_NAME")
    @Schema(description = "商店名稱")
    private List<MultilineText> storeName;

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
    private Integer defaultMonths;

    @JsonAlias("DEFAULT_AMOUNT")
    @Schema(description = "預設金額")
    private BigDecimal defaultAmount;
}
