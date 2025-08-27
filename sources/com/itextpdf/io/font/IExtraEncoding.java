package com.itextpdf.io.font;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/IExtraEncoding.class */
public interface IExtraEncoding {
    byte[] charToByte(String str, String str2);

    byte[] charToByte(char c, String str);

    String byteToChar(byte[] bArr, String str);
}
