package com.irent.donation_backend.model.newebpay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Schema(description = "NewebPayNotifyReqDTO 回傳參數")
public class NewebPayNotifyReqDTO {

    @JsonProperty("Status")
    @Schema(description = """
                回傳狀態：
                1.若交易付款成功，則回傳 SUCCESS。
                2.若交易付款失敗，則回傳錯誤代碼。
                錯誤代碼請參考 5. 錯誤代碼表。
            """, example = "SUCCESS")
    private String status;

    @JsonProperty("MerchantID")
    @Schema(description = """
                回傳訊息：
                商店代號。
            """, example = "MS12345678")
    private String merchantID;

    @JsonProperty("TradeInfo")
    @Schema(description = """
                交易資料 AES 加密：
                將交易資料參數（下方列表中參數）透過商店 Key 及 IV 進行 AES 加密
            """, example = "encryptedTradeInfo")
    private String tradeInfo;

    @JsonProperty("TradeSha")
    @Schema(description = """
                交易資料 SHA256 加密：
                將交易資料經過上述 AES 加密過的字串，透過商店 Key 及 IV 進行 SHA256 加密
            """, example = "encryptedTradeSha")
    private String tradeSha;

    @JsonProperty("Version")
    @Schema(description = """
                串接程式版本
            """, example = "2.2")
    private String version;

    @JsonProperty("EncryptType")
    @Schema(description = """
                加密模式：
                1. 若商店設定 EncryptType 為 1，則會回傳此參數
                2. 若商店設定 EncryptType 為 0 或未有此參數，則不會回傳此參數
            """, example = "2.2")
    private Integer encryptType;
}
