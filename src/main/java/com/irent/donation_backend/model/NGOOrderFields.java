package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "捐款訂單資訊")
public class NGOOrderFields {

    @JsonAlias({"DONOR_NAME", "donorName"})
    @JsonProperty("DONOR_NAME")
    @Schema(description = "捐款人姓名(公司名稱)")
    private String donorName;

    @JsonAlias({"ID_NUMBER", "idNumber"})
    @JsonProperty("ID_NUMBER")
    @Schema(description = "身分證字號(統一編號)")
    private String idNumber;

    @JsonAlias({"CONTACT_PHONE", "contactPhone"})
    @JsonProperty("CONTACT_PHONE")
    @Schema(description = "聯絡電話(含區碼市話或行動電話)")
    private String contactPhone;

    @JsonAlias({"EMAIL", "email"})
    @JsonProperty("EMAIL")
    @Schema(description = "E-Mail (當選擇信用卡付款時，E-Mail 為必填)")
    private String email;

    @JsonAlias({"HOUSEHOLD_ADDRESS", "householdAddress"})
    @JsonProperty("HOUSEHOLD_ADDRESS")
    @Schema(description = "戶籍地址")
    private String householdAddress;

    @JsonAlias({"CONTACT_ADDRESS", "contactAddress"})
    @JsonProperty("CONTACT_ADDRESS")
    @Schema(description = "通訊地址")
    private String contactAddress;

    @JsonAlias({"MESSAGE", "message"})
    @JsonProperty("MESSAGE")
    @Schema(description = "想說的話")
    private String message;
}
