package org.apache.tomcat.util.buf;

import java.io.ByteArrayOutputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/buf/UDecoder.class */
public final class UDecoder {
    private static final StringManager sm = StringManager.getManager((Class<?>) UDecoder.class);
    private static final Log log = LogFactory.getLog((Class<?>) UDecoder.class);
    public static final boolean ALLOW_ENCODED_SLASH = Boolean.parseBoolean(System.getProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "false"));
    private static final IOException EXCEPTION_EOF = new DecodeException("EOF");
    private static final IOException EXCEPTION_NOT_HEX_DIGIT = new DecodeException("isHexDigit");
    private static final IOException EXCEPTION_SLASH = new DecodeException("noSlash");

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/buf/UDecoder$DecodeException.class */
    private static class DecodeException extends CharConversionException {
        private static final long serialVersionUID = 1;

        public DecodeException(String s) {
            super(s);
        }

        @Override // java.lang.Throwable
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    public void convert(ByteChunk mb, boolean query) throws IOException {
        int start = mb.getOffset();
        byte[] buff = mb.getBytes();
        int end = mb.getEnd();
        int idx = ByteChunk.findByte(buff, start, end, (byte) 37);
        int idx2 = -1;
        if (query) {
            idx2 = ByteChunk.findByte(buff, start, idx >= 0 ? idx : end, (byte) 43);
        }
        if (idx < 0 && idx2 < 0) {
            return;
        }
        if ((idx2 >= 0 && idx2 < idx) || idx < 0) {
            idx = idx2;
        }
        boolean noSlash = (ALLOW_ENCODED_SLASH || query) ? false : true;
        int j = idx;
        while (j < end) {
            if (buff[j] == 43 && query) {
                buff[idx] = 32;
            } else if (buff[j] != 37) {
                buff[idx] = buff[j];
            } else {
                if (j + 2 >= end) {
                    throw EXCEPTION_EOF;
                }
                byte b1 = buff[j + 1];
                byte b2 = buff[j + 2];
                if (!isHexDigit(b1) || !isHexDigit(b2)) {
                    throw EXCEPTION_NOT_HEX_DIGIT;
                }
                j += 2;
                int res = x2c(b1, b2);
                if (noSlash && res == 47) {
                    throw EXCEPTION_SLASH;
                }
                buff[idx] = (byte) res;
            }
            j++;
            idx++;
        }
        mb.setEnd(idx);
    }

    public void convert(CharChunk mb, boolean query) throws IOException {
        int start = mb.getOffset();
        char[] buff = mb.getBuffer();
        int cend = mb.getEnd();
        int idx = CharChunk.indexOf(buff, start, cend, '%');
        int idx2 = -1;
        if (query) {
            idx2 = CharChunk.indexOf(buff, start, idx >= 0 ? idx : cend, '+');
        }
        if (idx < 0 && idx2 < 0) {
            return;
        }
        if ((idx2 >= 0 && idx2 < idx) || idx < 0) {
            idx = idx2;
        }
        boolean noSlash = (ALLOW_ENCODED_SLASH || query) ? false : true;
        int j = idx;
        while (j < cend) {
            if (buff[j] == '+' && query) {
                buff[idx] = ' ';
            } else if (buff[j] != '%') {
                buff[idx] = buff[j];
            } else {
                if (j + 2 >= cend) {
                    throw EXCEPTION_EOF;
                }
                char b1 = buff[j + 1];
                char b2 = buff[j + 2];
                if (!isHexDigit(b1) || !isHexDigit(b2)) {
                    throw EXCEPTION_NOT_HEX_DIGIT;
                }
                j += 2;
                int res = x2c(b1, b2);
                if (noSlash && res == 47) {
                    throw EXCEPTION_SLASH;
                }
                buff[idx] = (char) res;
            }
            j++;
            idx++;
        }
        mb.setEnd(idx);
    }

    public void convert(MessageBytes mb, boolean query) throws IOException {
        switch (mb.getType()) {
            case 1:
                String strValue = mb.toString();
                if (strValue == null) {
                    return;
                }
                try {
                    mb.setString(convert(strValue, query));
                    return;
                } catch (RuntimeException ex) {
                    throw new DecodeException(ex.getMessage());
                }
            case 2:
                ByteChunk bytesC = mb.getByteChunk();
                convert(bytesC, query);
                return;
            case 3:
                CharChunk charC = mb.getCharChunk();
                convert(charC, query);
                return;
            default:
                return;
        }
    }

    public final String convert(String str, boolean query) {
        char laChar;
        if (str == null) {
            return null;
        }
        if ((!query || str.indexOf(43) < 0) && str.indexOf(37) < 0) {
            return str;
        }
        boolean noSlash = (ALLOW_ENCODED_SLASH || query) ? false : true;
        StringBuilder dec = new StringBuilder();
        int strPos = 0;
        int strLen = str.length();
        dec.ensureCapacity(str.length());
        while (strPos < strLen) {
            int laPos = strPos;
            while (laPos < strLen && (((laChar = str.charAt(laPos)) != '+' || !query) && laChar != '%')) {
                laPos++;
            }
            if (laPos > strPos) {
                dec.append(str.substring(strPos, laPos));
                strPos = laPos;
            }
            if (strPos >= strLen) {
                break;
            }
            char metaChar = str.charAt(strPos);
            if (metaChar == '+') {
                dec.append(' ');
                strPos++;
            } else if (metaChar == '%') {
                char res = (char) Integer.parseInt(str.substring(strPos + 1, strPos + 3), 16);
                if (noSlash && res == '/') {
                    throw new IllegalArgumentException("noSlash");
                }
                dec.append(res);
                strPos += 3;
            } else {
                continue;
            }
        }
        return dec.toString();
    }

    public static String URLDecode(String str) {
        return URLDecode(str, StandardCharsets.ISO_8859_1);
    }

    @Deprecated
    public static String URLDecode(String str, String enc) {
        return URLDecode(str, enc, false);
    }

    public static String URLDecode(String str, Charset charset) {
        return URLDecode(str, charset, false);
    }

    @Deprecated
    public static String URLDecode(String str, String enc, boolean isQuery) {
        Charset charset = null;
        if (enc != null) {
            try {
                charset = B2CConverter.getCharset(enc);
            } catch (UnsupportedEncodingException uee) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("uDecoder.urlDecode.uee", enc), uee);
                }
            }
        }
        return URLDecode(str, charset, isQuery);
    }

    @Deprecated
    public static String URLDecode(byte[] bytes, String enc, boolean isQuery) {
        throw new IllegalArgumentException(sm.getString("udecoder.urlDecode.iae"));
    }

    private static String URLDecode(String str, Charset charset, boolean isQuery) throws IOException {
        if (str == null) {
            return null;
        }
        if (str.indexOf(37) == -1) {
            return str;
        }
        if (charset == null) {
            charset = StandardCharsets.ISO_8859_1;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() * 2);
        OutputStreamWriter osw = new OutputStreamWriter(baos, charset);
        char[] sourceChars = str.toCharArray();
        int len = sourceChars.length;
        int ix = 0;
        while (ix < len) {
            try {
                int i = ix;
                ix++;
                char c = sourceChars[i];
                if (c == '%') {
                    osw.flush();
                    if (ix + 2 > len) {
                        throw new IllegalArgumentException(sm.getString("uDecoder.urlDecode.missingDigit", str));
                    }
                    int ix2 = ix + 1;
                    char c1 = sourceChars[ix];
                    ix = ix2 + 1;
                    char c2 = sourceChars[ix2];
                    if (isHexDigit(c1) && isHexDigit(c2)) {
                        baos.write(x2c(c1, c2));
                    } else {
                        throw new IllegalArgumentException(sm.getString("uDecoder.urlDecode.missingDigit", str));
                    }
                } else if (c == '+' && isQuery) {
                    osw.append(' ');
                } else {
                    osw.append(c);
                }
            } catch (IOException ioe) {
                throw new IllegalArgumentException(sm.getString("uDecoder.urlDecode.conversionError", str, charset.name()), ioe);
            }
        }
        osw.flush();
        return baos.toString(charset.name());
    }

    private static boolean isHexDigit(int c) {
        return (c >= 48 && c <= 57) || (c >= 97 && c <= 102) || (c >= 65 && c <= 70);
    }

    private static int x2c(byte b1, byte b2) {
        int digit = b1 >= 65 ? ((b1 & 223) - 65) + 10 : b1 - 48;
        return (digit * 16) + (b2 >= 65 ? ((b2 & 223) - 65) + 10 : b2 - 48);
    }

    private static int x2c(char b1, char b2) {
        int digit = b1 >= 'A' ? ((b1 & 223) - 65) + 10 : b1 - '0';
        return (digit * 16) + (b2 >= 'A' ? ((b2 & 223) - 65) + 10 : b2 - '0');
    }
}
