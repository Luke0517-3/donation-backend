package com.irent.donation_backend.service;

import org.jasypt.util.text.AES256TextEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionUtilsTest {

    private AES256TextEncryptor encryptor;
    private final String password = "TestKey12345";

    @BeforeEach
    public void setUp() {
        encryptor = new AES256TextEncryptor();
        encryptor.setPassword(password);
    }

    @Test
    public void testEncryptionDecryption() {
        // 测试加密和解密的完整流程
        String originalText = "这是一段需要加密的敏感信息";

        // 加密
        String encryptedText = encryptor.encrypt(originalText);

        // 确保加密后的文本与原始文本不同
        assertNotEquals(originalText, encryptedText);

        // 解密
        String decryptedText = encryptor.decrypt(encryptedText);

        // 确保解密后的文本与原始文本相同
        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testEncryptionConsistency() {
        String originalText = "测试文本";

        // 两次加密同一文本应产生不同的加密结果(因为随机IV)
        String encrypted1 = encryptor.encrypt(originalText);
        String encrypted2 = encryptor.encrypt(originalText);

        assertNotEquals(encrypted1, encrypted2, "加密结果应该不同(随机IV)");

        // 但都应该能正确解密回原文
        assertEquals(originalText, encryptor.decrypt(encrypted1));
        assertEquals(originalText, encryptor.decrypt(encrypted2));
    }

    @Test
    public void testWrongPassword() {
        String originalText = "机密数据";
        String encryptedText = encryptor.encrypt(originalText);

        // 创建使用错误密钥的解密器
        AES256TextEncryptor wrongEncryptor = new AES256TextEncryptor();
        wrongEncryptor.setPassword("错误的密钥");

        // 使用错误密钥应该导致解密失败
        assertThrows(Exception.class, () -> {
            wrongEncryptor.decrypt(encryptedText);
        });
    }
}
