package com.irent.donation_backend.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.util.text.AES256TextEncryptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoTest {

    @Test
    public void testHashPasswordAndEncryptDecrypt() {
        // 1. 使用SHA-256哈希处理密码
        String originalPassword = "用户密码123";
        String hashedPassword = DigestUtils.sha256Hex(originalPassword);

        // 确保哈希结果是预期的64个字符长度(SHA-256输出)
        assertEquals(64, hashedPassword.length());

        // 2. 使用哈希后的密码作为加密密钥
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(hashedPassword);

        // 3. 加密数据
        String sensitiveData = "这是需要加密的敏感数据";
        String encryptedData = encryptor.encrypt(sensitiveData);

        // 4. 解密数据并验证
        String decryptedData = encryptor.decrypt(encryptedData);
        assertEquals(sensitiveData, decryptedData, "解密数据应与原始数据匹配");
    }
}
