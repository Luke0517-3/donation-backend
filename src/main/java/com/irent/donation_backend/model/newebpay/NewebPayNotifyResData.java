package com.irent.donation_backend.model.newebpay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewebPayNotifyResData {

    @JsonProperty("Status")
    private String Status;              // 回傳狀態  

    @JsonProperty("Message")
    private String Message;             // 回傳訊息  

    @JsonProperty("Result")
    private Result Result;              // 回傳參數

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        // 所有支付方式共同參數  
        @JsonProperty("MerchantID")
        private String MerchantID;        // 商店代號  

        @JsonProperty("Amt")
        private Integer Amt;              // 交易金額  

        @JsonProperty("TradeNo")
        private String TradeNo;           // 藍新金流交易序號  

        @JsonProperty("MerchantOrderNo")
        private String MerchantOrderNo;   // 商店訂單編號  

        @JsonProperty("PaymentType")
        private String PaymentType;       // 支付方式  

        @JsonProperty("RespondType")
        private String RespondType;       // 回傳格式  

        @JsonProperty("PayTime")
        private String PayTime;           // 支付完成時間  

        @JsonProperty("IP")
        private String IP;                // 交易IP  

        @JsonProperty("EscrowBank")
        private String EscrowBank;        // 款項保管銀行  

        // 信用卡支付回傳參數  
        @JsonProperty("AuthBank")
        private String AuthBank;          // 收單金融機構  

        @JsonProperty("CardBank")
        private String CardBank;          // 發卡金融機構  

        @JsonProperty("RespondCode")
        private String RespondCode;       // 金融機構回應碼  

        @JsonProperty("Auth")
        private String Auth;              // 授權碼  

        @JsonProperty("Card6No")
        private String Card6No;           // 卡號前六碼  

        @JsonProperty("Card4No")
        private String Card4No;           // 卡號末四碼  

        @JsonProperty("Inst")
        private Integer Inst;             // 分期-期別  

        @JsonProperty("InstFirst")
        private Integer InstFirst;        // 分期-首期金額  

        @JsonProperty("InstEach")
        private Integer InstEach;         // 分期-每期金額  

        @JsonProperty("ECI")
        private String ECI;               // ECI值  

        @JsonProperty("TokenUseStatus")
        private Integer TokenUseStatus;   // 信用卡快速結帳使用狀態  

        @JsonProperty("RedAmt")
        private Integer RedAmt;           // 紅利折抵後實際金額  

        @JsonProperty("PaymentMethod")
        private String PaymentMethod;     // 交易類別  

        // DCC相關參數  
        @JsonProperty("DCC_Amt")
        private Float DCC_Amt;            // 外幣金額  

        @JsonProperty("DCC_Rate")
        private Float DCC_Rate;           // 匯率  

        @JsonProperty("DCC_Markup")
        private Float DCC_Markup;         // 風險匯率  

        @JsonProperty("DCC_Currency")
        private String DCC_Currency;      // 幣別  

        @JsonProperty("DCC_Currency_Code")
        private Integer DCC_Currency_Code; // 幣別代碼  

        // WEBATM、ATM繳費回傳參數  
        @JsonProperty("PayBankCode")
        private String PayBankCode;       // 付款人金融機構代碼  

        @JsonProperty("PayerAccount5Code")
        private String PayerAccount5Code; // 付款人金融機構帳號末五碼  

        // 超商代碼繳費回傳參數  
        @JsonProperty("CodeNo")
        private String CodeNo;            // 繳費代碼  

        @JsonProperty("StoreType")
        private Integer StoreType;        // 繳費門市類別  

        @JsonProperty("StoreID")
        private String StoreID;           // 繳費門市代號  

        // 超商條碼繳費回傳參數  
        @JsonProperty("Barcode_1")
        private String Barcode_1;         // 第一段條碼  

        @JsonProperty("Barcode_2")
        private String Barcode_2;         // 第二段條碼  

        @JsonProperty("Barcode_3")
        private String Barcode_3;         // 第三段條碼  

        @JsonProperty("RepayTimes")
        private Integer RepayTimes;       // 付款次數  

        @JsonProperty("PayStore")
        private String PayStore;          // 繳費超商  

        // 超商物流回傳參數  
        @JsonProperty("StoreCode")
        private String StoreCode;         // 超商門市編號  

        @JsonProperty("StoreName")
        private String StoreName;         // 超商門市名稱  

        @JsonProperty("StoreAddr")
        private String StoreAddr;         // 超商門市地址  

        @JsonProperty("TradeType")
        private Integer TradeType;        // 取件交易方式  

        @JsonProperty("CVSCOMName")
        private String CVSCOMName;        // 取貨人  

        @JsonProperty("CVSCOMPhone")
        private String CVSCOMPhone;       // 取貨人手機號碼  

        @JsonProperty("LgsNo")
        private String LgsNo;             // 物流寄件單號  

        @JsonProperty("LgsType")
        private String LgsType;           // 物流型態  

        // 跨境支付回傳參數  
        @JsonProperty("ChannelID")
        private String ChannelID;         // 跨境通路類型  

        @JsonProperty("ChannelNo")
        private String ChannelNo;         // 跨境通路交易序號  

        // 其他支付回傳參數  
        @JsonProperty("PayAmt")
        private Integer PayAmt;           // 實際付款金額  

        @JsonProperty("RedDisAmt")
        private Integer RedDisAmt;        // 紅利折抵金額  

        // BitoPay回傳參數  
        @JsonProperty("CryptoCurrency")
        private String CryptoCurrency;    // 加密貨幣代號  

        @JsonProperty("CryptoAmount")
        private String CryptoAmount;      // 加密貨幣數量  

        @JsonProperty("CryptoRate")
        private String CryptoRate;        // 加密貨幣匯率
    }
}