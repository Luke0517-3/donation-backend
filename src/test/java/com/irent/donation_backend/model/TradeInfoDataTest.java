package com.irent.donation_backend.model;

import com.irent.donation_backend.model.newebpay.TradeInfoData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TradeInfoDataTest {
    @Test
    void testToStringEquality() {
        TradeInfoData original = createTestData();

        // toString()
        String originalOutput = original.toString();

        System.out.println("Original: " + originalOutput);

        // 測試邊界情況: 設置所有可選字段為 null
        original = createMinimalTestData();

        originalOutput = original.toString();


        System.out.println("\n=== 邊界情況: 只有必填字段 ===");
        System.out.println("Original: " + originalOutput);
    }

    @Test
    void timestampTest() {
        long now = System.currentTimeMillis();
        System.out.println("now: " + now);
        String timestamp = String.valueOf(now / 1000);
        System.out.println("timestamp: " + timestamp);
    }

    // 創建具有所有字段值的測試數據
    private TradeInfoData createTestData() {
        TradeInfoData data = new TradeInfoData();

        // 設置必填字段
        data.setMerchantID("TestMerchantID123");
        data.setRespondType("JSON");
        data.setTimeStamp("1634567890");
        data.setVersion("2.0");
        data.setMerchantOrderNo("Order20251011001");
        data.setAmt(1000);
        data.setItemDesc("Test Product");

        // 設置選填字段
        data.setLangType("zh-tw");
        data.setTradeLimit(300);
        data.setExpireDate("20251020");
        data.setExpireTime("235959");
        data.setReturnURL("https://example.com/return");
        data.setNotifyURL("https://example.com/notify");
        data.setCustomerURL("https://example.com/customer");
        data.setClientBackURL("https://example.com/back");
        data.setEmail("customer@example.com");
        data.setEmailModify(1);
        data.setOrderComment("This is a test order");

        // 設置支付方式
        data.setCredit(1);
        data.setApplePay(1);
        data.setAndroidPay(0);
        data.setSamsungPay(0);
        data.setLinePay(1);
        data.setImageUrl("https://example.com/image.jpg");
        data.setInstFlag("3,6,12");
        data.setCreditRed(0);
        data.setUnionPay(1);
        data.setCreditAE(1);
        data.setWebatm(1);
        data.setVacc(1);
        data.setBankType("BOT,HNCB");
        data.setCvs(1);
        data.setBarcode(1);
        data.setEsunWallet(0);
        data.setTaiwanPay(1);
        data.setBitoPay(0);
        data.setCvscom(0);
        data.setEzPay(1);
        data.setEzpWechat(0);
        data.setEzpAlipay(0);
        data.setLgsType("B2C");
        data.setWalletDisplayMode(1);

        return data;
    }

    // 創建只有必填字段的測試數據
    private TradeInfoData createMinimalTestData() {
        TradeInfoData data = new TradeInfoData();

        // 只設置必填字段
        data.setMerchantID("TestMerchantID123");
        data.setRespondType("JSON");
        data.setTimeStamp("1634567890");
        data.setVersion("2.0");
        data.setMerchantOrderNo("Order20251011001");
        data.setAmt(1000);
        data.setItemDesc("Test Product");

        return data;
    }
}
