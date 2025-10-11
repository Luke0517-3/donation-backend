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
@Schema(description = "NewebPay 請求參數")
public class NewebPayReqDTO {

    @Schema(description = """
                商店代號：
                藍新金流商店代號
            """, example = "MS12345678")
    private String merchantID;

    @Schema(description = """
                交易資料 AES 加密：
                將交易資料參數（下方列表中參數）透過商店 Key 及 IV 進行 AES 加密
            """, example = "encryptedTradeInfo")
    private String tradeInfo;

    @Schema(description = """
                交易資料 SHA256 加密：
                將交易資料經過上述 AES 加密過的字串，透過商店 Key 及 IV 進行 SHA256 加密
            """, example = "encryptedTradeSha")
    private String tradeSha;

    @Schema(description = """
                串接程式版本：
                2.2
            """, example = "2.2")
    private String version;
}
