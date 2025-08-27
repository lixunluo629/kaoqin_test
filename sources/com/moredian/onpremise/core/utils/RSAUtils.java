package com.moredian.onpremise.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/RSAUtils.class */
public class RSAUtils {
    public static final String RSA_KEY_ALGORITHM = "RSA";
    public static final String AES_KEY_ALGORITHM = "AES";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) RSAUtils.class);
    private static String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOCVC/SLPFJKtXxSVafzNtH1Ayl3FTZBKIASFNYQqZzfr/GF8q60s0yiOTqDC2GcrlHL8Mi45n25qfwkPNeY70ZRkEjjKlTIwDxBOQonQr/snYQDkUEMuBTIoBSwByY0RNng4z9LEntdJt2NAgp0SgKQ6b1i+clEOQ8VZzcFTB4LAgMBAAECgYEAunZlh7N28OhAiKkfnyNLN8VSy8GoZfRxeOwKSrVo7eu2/DP3i2BSoeTq2Q7mizP4c8d88lq6XFajsMoxjdvXhJ+HsvgO3dyMx1JXxaAk4q5qWrQkmjMJ8vAfmVlRZq5WUWzhBIJo0RJ5URSXmb8ie68+z+MqYl+dWfuCPi4fNvkCQQD0IJqHCWeCUQLLYBpOEX3mWGabJ0/vN6YZq0cYpT4v1gl5tMDtFYx9JrzxeA91EyRV6DS/WA/jGxIzBJWDA6RVAkEA64Ea7F4sfH5r4qdmkZQx8TTkbk/p3LifE4NjUhZVc4MgKXGWGvea5MnBr09UUDSl9n4zJi2W3+qXGixZTtgY3wJAF4vBe77DdgU8QMNfJiJ3wyzJkLNk+Uyjv5wOemkNq214cn2lkfhfGX8QADY1P3R+L676z2298oic48DQbtd+FQJAF9BXS51+PByAhkvNKF1m63AgxTLZgBM/Krb3fR57B5Iz110AA7wT8pygNDz+VpnsSk+alnq3re7H1sKzSTC62wJBAKD8STj408bmkZrTIqlEBMHD04mLydf63o1lGDtWc3MFjS+wATDRxaIeXuX9/W6fqdf4F8Ze6uOCBueit6hxvrQ=";
    private static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDglQv0izxSSrV8UlWn8zbR9QMpdxU2QSiAEhTWEKmc36/xhfKutLNMojk6gwthnK5Ry/DIuOZ9uan8JDzXmO9GUZBI4ypUyMA8QTkKJ0K/7J2EA5FBDLgUyKAUsAcmNETZ4OM/SxJ7XSbdjQIKdEoCkOm9YvnJRDkPFWc3BUweCwIDAQAB";

    public static String sign(byte[] data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        try {
            byte[] keyBytes = MyBase64Utils.decodeStringForByte(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data);
            return MyBase64Utils.encodeStringForString(signature.sign());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean verify(byte[] data, String publicKey, String sign) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        try {
            byte[] keyBytes = MyBase64Utils.decodeStringForByte(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(MyBase64Utils.decodeStringForByte(sign));
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return false;
        }
    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] cache;
        try {
            byte[] keyBytes = MyBase64Utils.decodeStringForByte(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(2, privateK);
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
            return decryptedData;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] cache;
        try {
            byte[] keyBytes = MyBase64Utils.decodeStringForByte(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(2, publicK);
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
            return decryptedData;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] cache;
        try {
            byte[] keyBytes = MyBase64Utils.decodeStringForByte(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(1, publicK);
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
            return encryptedData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] cache;
        try {
            byte[] keyBytes = MyBase64Utils.decodeStringForByte(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(1, privateK);
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
            return encryptedData;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    public static KeyPair genKeyPair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }
}
