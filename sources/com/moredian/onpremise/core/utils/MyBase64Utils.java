package com.moredian.onpremise.core.utils;

import org.apache.tomcat.util.codec.binary.Base64;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyBase64Utils.class */
public class MyBase64Utils {
    public static byte[] encodeStringForByte(String source) {
        return Base64.encodeBase64(source.getBytes());
    }

    public static byte[] encodeStringForByte(byte[] source) {
        return Base64.encodeBase64(source);
    }

    public static String encodeStringForString(String source) {
        return new String(Base64.encodeBase64(source.getBytes()));
    }

    public static String encodeStringForString(byte[] source) {
        return new String(Base64.encodeBase64(source));
    }

    public static byte[] decodeStringForByte(String source) {
        return Base64.decodeBase64(source.getBytes());
    }

    public static byte[] decodeStringForByte(byte[] source) {
        return Base64.decodeBase64(source);
    }

    public static String decodeStringForString(String source) {
        return new String(Base64.decodeBase64(source.getBytes()));
    }

    public static String decodeStringForString(byte[] source) {
        return new String(Base64.decodeBase64(source));
    }
}
