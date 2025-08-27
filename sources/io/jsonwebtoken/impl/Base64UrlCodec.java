package io.jsonwebtoken.impl;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/Base64UrlCodec.class */
public class Base64UrlCodec extends AbstractTextCodec {
    @Override // io.jsonwebtoken.impl.TextCodec
    public String encode(byte[] data) {
        String base64Text = TextCodec.BASE64.encode(data);
        byte[] bytes = removePadding(base64Text.getBytes(US_ASCII));
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 43) {
                bytes[i] = 45;
            } else if (bytes[i] == 47) {
                bytes[i] = 95;
            }
        }
        return new String(bytes, US_ASCII);
    }

    protected byte[] removePadding(byte[] bytes) {
        byte[] result = bytes;
        int paddingCount = 0;
        for (int i = bytes.length - 1; i > 0 && bytes[i] == 61; i--) {
            paddingCount++;
        }
        if (paddingCount > 0) {
            result = new byte[bytes.length - paddingCount];
            System.arraycopy(bytes, 0, result, 0, bytes.length - paddingCount);
        }
        return result;
    }

    @Override // io.jsonwebtoken.impl.TextCodec
    public byte[] decode(String encoded) {
        char[] chars = ensurePadding(encoded.toCharArray());
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '-') {
                chars[i] = '+';
            } else if (chars[i] == '_') {
                chars[i] = '/';
            }
        }
        String base64Text = new String(chars);
        return TextCodec.BASE64.decode(base64Text);
    }

    protected char[] ensurePadding(char[] chars) {
        char[] result = chars;
        int paddingCount = 0;
        int remainder = chars.length % 4;
        if (remainder == 2 || remainder == 3) {
            paddingCount = 4 - remainder;
        }
        if (paddingCount > 0) {
            result = new char[chars.length + paddingCount];
            System.arraycopy(chars, 0, result, 0, chars.length);
            for (int i = 0; i < paddingCount; i++) {
                result[chars.length + i] = '=';
            }
        }
        return result;
    }
}
