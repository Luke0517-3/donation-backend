package com.irent.donation_backend.model.newebpay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "訂單資訊")
public class OrderInfoDTO {

    @Schema(description = """
                訂單編號：
                商店系統產生的唯一訂單編號
            """, example = "ORDER123456789")
    private String orderId;

    @Schema(description = """
                商品名稱：
                此次交易的商品名稱
            """, example = "愛心捐款")
    private String itemDesc;

    @Schema(description = """
                交易金額：
                此次交易的總金額，單位為新台幣（TWD）
            """, example = "1000")
    private Integer amount;

    @Schema(description = """
                語系類型：
                指定付款頁面顯示的語言
                可選值：
                - zh-tw：繁體中文
                - en：英文
                - jp：日文
            """, example = "zh-tw", allowableValues = {"zh-tw", "en", "jp"})
    private String langType = "zh-tw";

    @Schema(description = """
                付款人email：
                付款人的電子郵件地址
            """, example = "abc123@qoo.com")
    private String email;

    @Schema(description = """
                機構名稱：
                付款人選擇捐款的機構名稱
            """, example = "TzuChi")
    private String customerName;

}
