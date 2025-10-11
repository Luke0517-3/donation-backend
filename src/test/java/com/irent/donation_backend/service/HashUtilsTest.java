package com.irent.donation_backend.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(MockitoExtension.class)
public class HashUtilsTest {

    @Test
    public void testSha256Hash() {
        // 已知输入和预期输出
        String input = "测试文本";
        String expectedHash = "61f7cd72f6163b5add3b2de5f0a1a98e775dab04dec17e8fb5e4c606ed42fc3a";

        // 使用Commons Codec进行哈希
        String actualHash = DigestUtils.sha256Hex(input);

        // 验证结果
        assertEquals(expectedHash, actualHash, "SHA-256 hash should match the expected value");
    }

    @Test
    public void testSha256ConsistentResults() {
        String input = "Hello, World!";

        // 同一输入应产生相同哈希值
        String hash1 = DigestUtils.sha256Hex(input);
        String hash2 = DigestUtils.sha256Hex(input);

        assertEquals(hash1, hash2, "SHA-256 should produce consistent results for the same input");
    }

    @Test
    public void testDifferentInputsProduceDifferentHashes() {
        String input1 = "Hello";
        String input2 = "Hello!";

        String hash1 = DigestUtils.sha256Hex(input1);
        String hash2 = DigestUtils.sha256Hex(input2);

        assertNotEquals(hash1, hash2, "diff input should produce different hash");
    }
}
