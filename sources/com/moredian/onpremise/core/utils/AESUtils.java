package com.moredian.onpremise.core.utils;

import ch.qos.logback.core.net.ssl.SSL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/AESUtils.class */
public class AESUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AESUtils.class);
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static String encode(String encodeKey, String content) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchProviderException {
        byte[] cache;
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(RSAUtils.AES_KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(SSL.DEFAULT_SECURE_RANDOM_ALGORITHM);
            random.setSeed(encodeKey.getBytes());
            keygen.init(128, random);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, RSAUtils.AES_KEY_ALGORITHM);
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(RSAUtils.AES_KEY_ALGORITHM, "BC");
            cipher.init(1, key);
            byte[] data = content.getBytes("utf-8");
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(data, offSet, 117);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return MyBase64Utils.encodeStringForString(encryptedData);
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String encode(String encodeKey, Map<String, Object> content) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchProviderException {
        byte[] cache;
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(RSAUtils.AES_KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(SSL.DEFAULT_SECURE_RANDOM_ALGORITHM, "SUN");
            random.setSeed(encodeKey.getBytes());
            keygen.init(128, random);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, RSAUtils.AES_KEY_ALGORITHM);
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(RSAUtils.AES_KEY_ALGORITHM, "BC");
            cipher.init(1, key);
            byte[] data = JsonUtils.toJson(content).getBytes();
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(data, offSet, 117);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return MyBase64Utils.encodeStringForString(encryptedData);
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static String decode(String encodeKey, String decodeContent) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchProviderException {
        byte[] cache;
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(RSAUtils.AES_KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(SSL.DEFAULT_SECURE_RANDOM_ALGORITHM, "SUN");
            random.setSeed(encodeKey.getBytes());
            keygen.init(128, random);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, RSAUtils.AES_KEY_ALGORITHM);
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(RSAUtils.AES_KEY_ALGORITHM, "BC");
            cipher.init(2, key);
            byte[] encryptedData = MyBase64Utils.decodeStringForByte(decodeContent);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(encryptedData, offSet, 128);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }
}
