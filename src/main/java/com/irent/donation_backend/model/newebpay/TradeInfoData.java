package com.irent.donation_backend.model.newebpay;

import lombok.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TradeInfoData {
    /**
     * 商店代號 (必填)
     * 型態: String(15)
     * 說明: 藍新金流商店代號，由藍新金流提供
     */
    private String merchantID;

    /**
     * 回傳格式 (必填)
     * 型態: String(6)
     * 說明: 回傳資料格式，僅能填入 "JSON" 或 "String"
     */
    private String respondType;

    /**
     * 時間戳記 (必填)
     * 型態: String(50)
     * 說明: 自從Unix纪元（格林威治時間1970年1月1日00:00:00）到當前時間的秒數
     * 須確實帶入自Unix紀元到當前時間的秒數以避免交易失敗 (容許誤差值120秒)
     */
    private String timeStamp;

    /**
     * 串接程式版本 (必填)
     * 型態: String(5)
     * 說明: 請帶入 "2.0"
     */
    private String version;

    /**
     * 商店訂單編號 (必填)
     * 型態: String(30)
     * 說明:
     * 1. 商店自訂訂單編號，限英、數字、"_" 格式 例：201406010001
     * 2. 長度限制為30字元
     * 3. 同一商店中此編號不可重覆
     */
    private String merchantOrderNo;

    /**
     * 訂單金額 (必填)
     * 型態: int(10)
     * 說明:
     * 1. 純數字不含符號，例：1000
     * 2. 幣別：新台幣
     */
    private int amt;

    /**
     * 商品資訊 (必填)
     * 型態: String(50)
     * 說明:
     * 1. 限制長度為50字元
     * 2. 編碼為Utf-8格式
     * 3. 請勿使用斷行符號、單引號等特殊符號，避免無法顯示完整付款頁面
     * 4. 若使用特殊符號，系統將自動過濾
     */
    private String itemDesc;

    // 選填參數
    /**
     * 語系 (選填)
     * 型態: String(5)
     * 說明:
     * 1. 設定MPG頁面顯示的文字語系：
     * 英文版 = en
     * 繁體中文版 = zh-tw
     * 日文版 = jp
     * 2. 當未提供此參數或此參數數值錯誤時，將預設為繁體中文版
     */
    private String langType;

    /**
     * 交易有效時間 (選填)
     * 型態: Int(3)
     * 說明:
     * 1. 於交易有效時間內未完成交易，則視為交易失敗
     * 2. 僅可接受數字格式
     * 3. 秒數下限為60秒，小於60秒以60秒計算
     * 4. 秒數上限為900秒，大於900秒以900秒計算
     * 5. 若未帶此參數，或是為0時，會視作為不啟用交易限制秒數
     */
    private Integer tradeLimit;

    /**
     * 繳費有效期限 (選填)
     * 型態: String(10)
     * 說明:
     * 1. 僅適用於非即時支付
     * 2. 格式為 date('Ymd')，例：20140620
     * 3. 此參數若為空值，系統預設為7天。自取號時間起算至第7天23:59:59
     * 例：2014-06-23 14:35:51完成取號，則繳費有效期限為2014-06-29 23:59:59
     * 4. 可接受最大值為180天
     */
    private String expireDate;

    /**
     * 繳費截止時間 (選填)
     * 型態: String(6)
     * 說明:
     * 1. 僅適用於超商代碼繳費
     * 2. 格式為date('His')，例：235959
     * 3. 此參數若為空值，系統預設為235959
     * 4. 建議最小值設定在1小時以上，避免付款人來不及於期限內付款
     */
    private String expireTime;

    /**
     * 支付完成返回商店網址 (選填)
     * 型態: String(200)
     * 說明:
     * 1. 交易完成後，以Form Post方式導回商店頁面
     * 2. 若支付工具為玉山Wallet、台灣Pay或本欄位為空值，於交易完成後，消費者將停留在藍新金流付款或取號結果頁面
     * 3. 只接受80與443 Port
     */
    private String returnURL;

    /**
     * 支付通知網址 (選填)
     * 型態: String(200)
     * 說明:
     * 1. 以幕後方式回傳給商店相關支付結果資料
     * 2. 只接受80與443 Port
     */
    private String notifyURL;

    /**
     * 商店取號網址 (選填)
     * 型態: String(200)
     * 說明:
     * 1. 系統取號後以form post方式將結果導回商店指定的網址
     * 2. 此參數若為空值，則會顯示取號結果在藍新金流頁面
     */
    private String customerURL;

    /**
     * 返回商店網址 (選填)
     * 型態: String(200)
     * 說明:
     * 1. 在藍新支付頁或藍新交易結果頁面上所呈現之返回鈕，我方將依據此參數之設定值進行設定，引導商店消費者依以此參數網址返回商店指定的頁面
     * 2. 此參數若為空值時，則無返回鈕
     */
    private String clientBackURL;

    /**
     * 付款人電子信箱 (選填)
     * 型態: String(50)
     * 說明: 於交易完成或付款完成時，通知付款人使用
     */
    private String email;

    /**
     * 付款人電子信箱是否開放修改 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定於MPG頁面，付款人電子信箱欄位是否開放讓付款人修改
     * 1=可修改
     * 0=不可修改
     * 2. 當未提供此參數時，將預設為可修改
     */
    private Integer emailModify;

    /**
     * 商店備註 (選填)
     * 型態: String(300)
     * 說明:
     * 1. 限制長度為300字
     * 2. 若有提供此參數，將會於MPG頁面呈現商店備註內容
     */
    private String orderComment;

    // 支付方式參數
    /**
     * 信用卡一次付清啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用信用卡一次付清支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer credit;

    /**
     * Apple Pay啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用Apple Pay支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer applePay;

    /**
     * Google Pay啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用Google Pay支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer androidPay;

    /**
     * Samsung Pay啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用Samsung Pay支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer samsungPay;

    /**
     * LINE Pay (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用LINE Pay支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     */
    private Integer linePay;

    /**
     * LINE Pay產品圖檔連結網址 (選填)
     * 型態: String(200)
     * 說明:
     * 1. LINE Pay[啟用]時，會員(商店)視需求傳遞此參數
     * 2. 此連結的圖檔將顯示於LINE Pay付款前的產品圖片區，若無產品圖檔連結網址，會使用藍新系統預設圖檔
     * 3. 圖片建議使用84*84像素(若大於或小於該尺寸有可能造成破圖或變形)
     * 4. 檔案類型僅支援jpg或png
     */
    private String imageUrl;

    /**
     * 信用卡分期付款啟用 (選填)
     * 型態: String(18)
     * 說明:
     * 1. 此欄位值=1時，即代表開啟所有分期期別，且不可帶入其他期別參數
     * 2. 此欄位值為下列數值時，即代表開啟該分期期別
     * 3=分3期功能
     * 6=分6期功能
     * 8=分8期功能(閘道商店使用)
     * 12=分12期功能
     * 18=分18期功能
     * 24=分24期功能
     * 30=分30期功能
     * 3. 同時開啟多期別時，將此參數用","(半形)分隔，例如：3,6,12，代表開啟分3、6、12期的功能
     * 4. 此欄位值=０或無值時，即代表不開啟分期
     */
    private String instFlag;

    /**
     * 信用卡紅利啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用信用卡紅利支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer creditRed;

    /**
     * 信用卡銀聯卡啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用銀聯卡支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer unionPay;

    /**
     * 信用卡美國運通卡啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用美國運通卡支付方式
     * 1=啟用
     * 0或者未有此參數=不啟用
     */
    private Integer creditAE;

    /**
     * WEBATM啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用WEBATM支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     * 2. 當訂單金額超過49,999元或消費者使用手機裝置付款時，即使啟用WebATM，MPG支付頁仍不會顯示此支付方式
     * 3. 若只有啟用WebATM，消費者於MPG支付頁無法點選此支付方式
     */
    private Integer webatm;

    /**
     * ATM轉帳啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用ATM轉帳支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     * 2. 當該筆訂單金額超過49,999元時，即使此參數設定為啟用，MPG付款頁面仍不會顯示此支付方式選項
     */
    private Integer vacc;

    /**
     * 金融機構 (選填)
     * 型態: String(26)
     * 說明:
     * 1. 指定銀行對應參數值如下：
     * BOT=台灣銀行
     * HNCB=華南銀行
     * 2. 若未帶值，則預設值為支援所有指定銀行
     * 3. 此為[WEBATM]與[ATM轉帳]可供付款人選擇轉帳銀行，將顯示於MPG頁上。為共用此參數值，無法個別分開指定
     * 4. 可指定1個以上的銀行，若指定1個以上，則用半形［,］分隔，例如：BOT,HNCB
     */
    private String bankType;

    /**
     * 超商代碼繳費啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用超商代碼繳費支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     * 2. 當該筆訂單金額小於30元或超過2萬元時，即使此參數設定為啟用，MPG付款頁面仍不會顯示此支付方式選項
     */
    private Integer cvs;

    /**
     * 超商條碼繳費啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用超商條碼繳費支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     * 2. 當該筆訂單金額小於20元或超過4萬元時，即使此參數設定為啟用，MPG付款頁面仍不會顯示此支付方式選項
     */
    private Integer barcode;

    /**
     * 玉山Wallet (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用玉山Wallet支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     */
    private Integer esunWallet;

    /**
     * 台灣Pay (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用台灣Pay支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     * 2. 當該筆訂單金額超過49,999元時，即使此參數設定為啟用，MPG付款頁面仍不會顯示此支付方式選項
     */
    private Integer taiwanPay;

    /**
     * BitoPay (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用BitoPay支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     * 2. 當該筆訂單金額小於100元或超過49,999元時，即使此參數設定為啟用，MPG付款頁面仍不會顯示此支付方式選項
     */
    private Integer bitoPay;

    /**
     * 物流啟用 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 使用前，須先登入藍新金流會員專區啟用物流並設定退貨門市與取貨人相關資訊
     * 1=啟用超商取貨不付款
     * 2=啟用超商取貨付款
     * 3=啟用超商取貨不付款及超商取貨付款
     * 0或者未有此參數，即代表不開啟
     * 2. 當該筆訂單金額大於2萬元時，因金額限制關係，無法使用物流服務
     */
    private Integer cvscom;

    /**
     * 簡單付電子錢包 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用簡單付電子錢包支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     */
    private Integer ezPay;

    /**
     * 簡單付微信支付 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用簡單付微信支付支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     */
    private Integer ezpWechat;

    /**
     * 簡單付支付寶 (選填)
     * 型態: Int(1)
     * 說明:
     * 1. 設定是否啟用簡單付支付寶支付方式
     * 1=啟用
     * 0或者未有此參數，即代表不開啟
     */
    private Integer ezpAlipay;

    /**
     * 物流型態 (選填)
     * 型態: String(3)
     * 說明:
     * 1. 帶入參數值說明：
     * B2C＝大宗寄倉(目前僅支援7-ELEVEN)
     * C2C＝店到店(支援7-ELEVEN、全家、萊爾富、OK mart)
     * 2. 若商店未帶入此參數，則系統預設值說明如下：
     * a.系統優先啟用［B2C大宗寄倉］
     * b.若商店設定中未啟用［B2C大宗寄倉］則系統將會啟用［C2C店到店］
     * c.若商店設定中，［B2C大宗寄倉］與［C2C店到店］皆未啟用，則支付頁面中將不會出現物流選項
     */
    private String lgsType;

    /**
     * 電子支付模式 (選填)
     * 型態: Int(1)
     * 說明: 設定是否指定電子支付工具（包含：LINE Pay、玉山Wallet、台灣Pay）的付款方式
     * 1=固定顯示QR Code，無論用戶設備類型，始終顯示QR Code供掃描支付
     * 0或者未有此參數，根據用戶的設備自動選擇支付方式，行動裝置跳轉至對應APP，桌面瀏覽器顯示QR Code
     */
    private Integer walletDisplayMode;

    /**
     * 生成 TradeInfo 參數字符串
     */
    public String generateTradeInfoString() {
        StringBuilder tradeInfo = new StringBuilder();

        // Helper method to add parameters efficiently
        BiConsumer<String, String> appendParam = (key, value) -> {
            if (value != null) {
                if (!tradeInfo.isEmpty()) {
                    tradeInfo.append("&");
                }
                tradeInfo.append(key).append("=").append(value);
            }
        };

        // 加入必填參數
        appendParam.accept("MerchantID", merchantID);
        appendParam.accept("RespondType", respondType);
        appendParam.accept("TimeStamp", timeStamp);
        appendParam.accept("Version", version);
        appendParam.accept("MerchantOrderNo", merchantOrderNo);
        appendParam.accept("Amt", String.valueOf(amt));
        appendParam.accept("ItemDesc", itemDesc);

        // 加入選填參數
        appendParam.accept("LangType", langType);
        appendParam.accept("TradeLimit", tradeLimit != null ? String.valueOf(tradeLimit) : null);
        appendParam.accept("ExpireDate", expireDate);
        appendParam.accept("ExpireTime", expireTime);
        appendParam.accept("ReturnURL", returnURL);
        appendParam.accept("NotifyURL", notifyURL);
        appendParam.accept("CustomerURL", customerURL);
        appendParam.accept("ClientBackURL", clientBackURL);
        appendParam.accept("Email", email);
        appendParam.accept("EmailModify", emailModify != null ? String.valueOf(emailModify) : null);
        appendParam.accept("OrderComment", orderComment);

        // 加入支付方式參數
        appendParam.accept("CREDIT", credit != null ? String.valueOf(credit) : null);
        appendParam.accept("APPLEPAY", applePay != null ? String.valueOf(applePay) : null);
        appendParam.accept("ANDROIDPAY", androidPay != null ? String.valueOf(androidPay) : null);
        appendParam.accept("SAMSUNGPAY", samsungPay != null ? String.valueOf(samsungPay) : null);
        appendParam.accept("LINEPAY", linePay != null ? String.valueOf(linePay) : null);
        appendParam.accept("ImageUrl", imageUrl);
        appendParam.accept("InstFlag", instFlag);
        appendParam.accept("CreditRed", creditRed != null ? String.valueOf(creditRed) : null);
        appendParam.accept("UNIONPAY", unionPay != null ? String.valueOf(unionPay) : null);
        appendParam.accept("CREDITAE", creditAE != null ? String.valueOf(creditAE) : null);
        appendParam.accept("WEBATM", webatm != null ? String.valueOf(webatm) : null);
        appendParam.accept("VACC", vacc != null ? String.valueOf(vacc) : null);
        appendParam.accept("BankType", bankType);
        appendParam.accept("CVS", cvs != null ? String.valueOf(cvs) : null);
        appendParam.accept("BARCODE", barcode != null ? String.valueOf(barcode) : null);
        appendParam.accept("ESUNWALLET", esunWallet != null ? String.valueOf(esunWallet) : null);
        appendParam.accept("TAIWANPAY", taiwanPay != null ? String.valueOf(taiwanPay) : null);
        appendParam.accept("BITOPAY", bitoPay != null ? String.valueOf(bitoPay) : null);
        appendParam.accept("CVSCOM", cvscom != null ? String.valueOf(cvscom) : null);
        appendParam.accept("EZPAY", ezPay != null ? String.valueOf(ezPay) : null);
        appendParam.accept("EZPWECHAT", ezpWechat != null ? String.valueOf(ezpWechat) : null);
        appendParam.accept("EZPALIPAY", ezpAlipay != null ? String.valueOf(ezpAlipay) : null);
        appendParam.accept("LgsType", lgsType);
        appendParam.accept("WalletDisplayMode", walletDisplayMode != null ? String.valueOf(walletDisplayMode) : null);

        return tradeInfo.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TradeInfoData{");
        boolean first = true;

        // 必填字段
        first = appendField(sb, "merchantID", merchantID, first);
        first = appendField(sb, "respondType", respondType, first);
        first = appendField(sb, "timeStamp", timeStamp, first);
        first = appendField(sb, "version", version, first);
        first = appendField(sb, "merchantOrderNo", merchantOrderNo, first);
        first = appendField(sb, "amt", amt, first);
        first = appendField(sb, "itemDesc", itemDesc, first);

        // 選填字段
        first = appendField(sb, "langType", langType, first);
        first = appendField(sb, "tradeLimit", tradeLimit, first);
        first = appendField(sb, "expireDate", expireDate, first);
        first = appendField(sb, "expireTime", expireTime, first);
        first = appendField(sb, "returnURL", returnURL, first);
        first = appendField(sb, "notifyURL", notifyURL, first);
        first = appendField(sb, "customerURL", customerURL, first);
        first = appendField(sb, "clientBackURL", clientBackURL, first);
        first = appendField(sb, "email", email, first);
        first = appendField(sb, "emailModify", emailModify, first);
        first = appendField(sb, "orderComment", orderComment, first);

        // 支付方式參數
        first = appendField(sb, "credit", credit, first);
        first = appendField(sb, "applePay", applePay, first);
        first = appendField(sb, "androidPay", androidPay, first);
        first = appendField(sb, "samsungPay", samsungPay, first);
        first = appendField(sb, "linePay", linePay, first);
        first = appendField(sb, "imageUrl", imageUrl, first);
        first = appendField(sb, "instFlag", instFlag, first);
        first = appendField(sb, "creditRed", creditRed, first);
        first = appendField(sb, "unionPay", unionPay, first);
        first = appendField(sb, "creditAE", creditAE, first);
        first = appendField(sb, "webatm", webatm, first);
        first = appendField(sb, "vacc", vacc, first);
        first = appendField(sb, "bankType", bankType, first);
        first = appendField(sb, "cvs", cvs, first);
        first = appendField(sb, "barcode", barcode, first);
        first = appendField(sb, "esunWallet", esunWallet, first);
        first = appendField(sb, "taiwanPay", taiwanPay, first);
        first = appendField(sb, "bitoPay", bitoPay, first);
        first = appendField(sb, "cvscom", cvscom, first);
        first = appendField(sb, "ezPay", ezPay, first);
        first = appendField(sb, "ezpWechat", ezpWechat, first);
        first = appendField(sb, "ezpAlipay", ezpAlipay, first);
        first = appendField(sb, "lgsType", lgsType, first);
        first = appendField(sb, "walletDisplayMode", walletDisplayMode, first);

        sb.append('}');
        return sb.toString();
    }

    private static final Set<String> STRING_FIELDS = new HashSet<>(Arrays.asList(
            "merchantID", "respondType", "timeStamp", "version", "merchantOrderNo",
            "itemDesc", "langType", "expireDate", "expireTime", "returnURL",
            "notifyURL", "customerURL", "clientBackURL", "email", "orderComment",
            "imageUrl", "instFlag", "bankType", "lgsType"
    ));

    private boolean appendField(StringBuilder sb, String name, Object value, boolean isFirstField) {
        if (!isFirstField) sb.append(", ");

        sb.append(name).append('=');

        // 簡化的條件表達式
        if (value == null) {
            sb.append(STRING_FIELDS.contains(name) ? "'null'" : "null");
        } else if (value instanceof String) {
            sb.append('\'').append(value).append('\'');
        } else {
            sb.append(value);
        }

        return false;
    }

}
