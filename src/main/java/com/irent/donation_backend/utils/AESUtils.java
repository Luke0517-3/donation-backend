package com.irent.donation_backend.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "aes", ignoreInvalidFields = true)
public class AESUtils {

    private final String AES = "AES";
    private final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private final String AES_CBC_NOPAD = "AES/CBC/NoPadding";
    private final String ALGORITHM_SHA256 = "SHA-256";
    private String DEFAULT_KEY;
    private String DEFAULT_IV;

    /**
     * 初始化 AES Cipher 對象
     *
     * @param key        密鑰
     * @param iv         初始化向量
     * @param mode       Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @param usePadding 是否使用 PKCS5Padding
     * @return 初始化完成的 Cipher 對象
     */
    private Cipher initAESCipher(String key, String iv, int mode, boolean usePadding)
            throws Exception {
        // 檢查參數
        if (key == null || iv == null) {
            throw new IllegalArgumentException("密鑰或IV不能為空");
        }

        // 創建密鑰規格
        SecretKeySpec keySpec = new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8), AES);

        // 創建IV參數規格
        IvParameterSpec ivSpec = new IvParameterSpec(
                iv.getBytes(StandardCharsets.UTF_8));

        // 獲取並初始化Cipher
        String transformation = usePadding ? AES_CBC_PKCS5 : AES_CBC_NOPAD;
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(mode, keySpec, ivSpec);

        return cipher;
    }

    /**
     * AES256 加密 (使用默認金鑰和向量)
     *
     * @param content 要加密的內容
     * @return 16進制的小寫加密字串
     */
    public String encrypt(String content) throws Exception {
        return encrypt(content, DEFAULT_KEY, DEFAULT_IV);
    }

    /**
     * AES256 加密
     *
     * @param content 要加密的內容
     * @param key     金鑰
     * @param iv      向量
     * @return 16進制的小寫加密字串
     */
    public String encrypt(String content, String key, String iv) throws Exception {
        if (content == null) {
            throw new IllegalArgumentException("待加密內容不能為空");
        }

        // 使用共用方法初始化Cipher
        Cipher cipher = initAESCipher(key, iv, Cipher.ENCRYPT_MODE, true);

        // 執行加密
        byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // 轉換為16進制字串
        return Hex.encodeHexString(encrypted);
    }

    /**
     * AES256 解密 (使用默認金鑰和向量)
     *
     * @param encryptedHex 16進制的加密字串
     * @return 解密後的內容
     */
    public String decrypt(String encryptedHex) throws Exception {
        return decrypt(encryptedHex, DEFAULT_KEY, DEFAULT_IV);
    }

    /**
     * AES256 解密
     *
     * @param encryptedHex 16進制的加密字串
     * @param key          金鑰
     * @param iv           向量
     * @return 解密後的內容
     */
    public String decrypt(String encryptedHex, String key, String iv) throws Exception {
        if (encryptedHex == null) {
            throw new IllegalArgumentException("待解密內容不能為空");
        }

        byte[] encrypted = Hex.decodeHex(encryptedHex.replaceAll("\\s+", ""));

        // 使用共用方法初始化Cipher
        Cipher cipher = initAESCipher(key, iv, Cipher.DECRYPT_MODE, true);

        // 執行解密
        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 藍新金流專用 - AES256加密
     *
     * @param key  加密金鑰
     * @param iv   初始化向量
     * @param data 待加密數據
     * @return 加密後的十六進制字串
     */
    public String encryptPay2go(String key, String iv, String data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("待加密數據不能為空");
        }

        // 使用共用方法初始化Cipher
        Cipher cipher = initAESCipher(key, iv, Cipher.ENCRYPT_MODE, true);

        // 加密
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 轉換為十六進制小寫字串
        return Hex.encodeHexString(encryptedBytes);
    }

    /**
     * 藍新金流專用 - AES256解密
     * 需要手動移除PKCS7 padding
     *
     * @param key  解密金鑰
     * @param iv   初始化向量
     * @param data 待解密的十六進制字串
     * @return 解密後的字串
     */
    public String decryptPay2go(String key, String iv, String data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("待解密數據不能為空");
        }

        // 使用共用方法初始化Cipher (不使用padding，藍新金流需要手動處理)
        Cipher cipher = initAESCipher(key, iv, Cipher.DECRYPT_MODE, false);

        // 解密
        byte[] encryptedBytes = Hex.decodeHex(data.replaceAll("\\s+", ""));
        byte[] decryptedWithPadding = cipher.doFinal(encryptedBytes);

        // 藍新金流要求：需要先除去PKCS7的Padding後再進行AES解密運算
        byte[] decryptedBytes = removePadding(decryptedWithPadding);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 去除PKCS7 Padding (藍新金流專用)
     */
    private byte[] removePadding(byte[] paddedData) {
        if (paddedData == null || paddedData.length == 0) {
            throw new IllegalArgumentException("輸入數據不能為空");
        }

        int padLength = paddedData[paddedData.length - 1] & 0xFF;

        // 檢查padding是否有效
        if (padLength <= 0 || padLength > paddedData.length) {
            throw new IllegalArgumentException("無效的padding值");
        }

        // 驗證padding有效性
        for (int i = paddedData.length - padLength; i < paddedData.length; i++) {
            if ((paddedData[i] & 0xFF) != padLength) {
                throw new IllegalArgumentException("無效的PKCS7 padding");
            }
        }

        byte[] result = new byte[paddedData.length - padLength];
        System.arraycopy(paddedData, 0, result, 0, result.length);
        return result;
    }


    /**
     * SHA-256雜湊
     */
    public String sha256(String key, String iv, String content) {
        if (key == null || iv == null || content == null) {
            throw new IllegalArgumentException("參數不能為空");
        }

        String shaData = "HashKey=" + key + "&" + content + "&HashIV=" + iv;

        return DigestUtils.sha256Hex(shaData).toUpperCase();
    }

    /**
     * URL編碼
     *
     * @param value 待編碼值
     * @return 編碼後的值
     */
    public String urlEncode(String value) {
        if (value == null) {
            return "";
        }

        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8)
                    .replace("+", "%20");
        } catch (Exception e) {
            return value;
        }
    }

}