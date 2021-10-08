package com.example.bepro;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FridgeCode {
    private static byte[] iv = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16 };

    private String key = "20211008KeyBePro";

    public String getCode(int fridgeIdx) throws Exception {
        return encByKey(key,Integer.toString(fridgeIdx));
    }
    public int getFriIdx(String fridgeCode) throws Exception {
        return Integer.parseInt(decByKey(key,fridgeCode));
    }

    // 사용자 지정 키로 AES256 암호화
    private static String encByKey(String key, String value) throws Exception {
        return encByKey(key.getBytes(), value.getBytes());
    }

    // 사용자 지정 키로 AES256 복호화
    private static String encByKey(byte[] key, byte[] value) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] randomKey = cipher.doFinal(value);
        return Base64.encodeToString(randomKey, 0);
    }

    // 사용자 지정 키로 AES256 복호화
    private static String decByKey(String key, String plainText) throws Exception {
        return decByKey(key.getBytes(), Base64.decode(plainText, 0));
    }

    // 사용자 지정 키로 AES256 복호화
    private static String decByKey(byte[] key, byte[] encText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] secureKey = cipher.doFinal(encText);
        return new String(secureKey);
    }
}