package com.moredian.onpremise.core.utils;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MD5Utils.class */
public class MD5Utils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MD5Utils.class);
    private static char[] md5Chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String[] STR_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", ExcelXmlConstants.CELL_TAG, DateTokenConverter.CONVERTER_KEY, "e", ExcelXmlConstants.CELL_FORMULA_TAG};

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = md5Chars[(bt & 240) >> 4];
        char c1 = md5Chars[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String toMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            messagedigest.update(str.getBytes("UTF-8"));
            return bufferToHex(messagedigest.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STR_DIGITS[iD1] + STR_DIGITS[iD2];
    }

    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (byte b : bByte) {
            sBuffer.append(byteToArrayString(b));
        }
        return sBuffer.toString();
    }

    public static String getMD5Code(String strObj) throws NoSuchAlgorithmException {
        String resultString = null;
        try {
            new String(strObj);
            MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}
