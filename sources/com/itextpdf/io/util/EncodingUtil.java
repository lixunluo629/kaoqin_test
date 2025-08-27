package com.itextpdf.io.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/EncodingUtil.class */
public final class EncodingUtil {
    private EncodingUtil() {
    }

    public static byte[] convertToBytes(char[] chars, String encoding) throws CharacterCodingException {
        Charset cc = Charset.forName(encoding);
        CharsetEncoder ce = cc.newEncoder();
        ce.onUnmappableCharacter(CodingErrorAction.IGNORE);
        ByteBuffer bb = ce.encode(CharBuffer.wrap(chars));
        bb.rewind();
        int lim = bb.limit();
        int offset = "UTF-8".equals(encoding) ? 3 : 0;
        byte[] br = new byte[lim + offset];
        if ("UTF-8".equals(encoding)) {
            br[0] = -17;
            br[1] = -69;
            br[2] = -65;
        }
        bb.get(br, offset, lim);
        return br;
    }

    public static String convertToString(byte[] bytes, String encoding) throws UnsupportedEncodingException {
        if (encoding.equals("UTF-8") && bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65) {
            return new String(bytes, 3, bytes.length - 3, "UTF-8");
        }
        return new String(bytes, encoding);
    }
}
