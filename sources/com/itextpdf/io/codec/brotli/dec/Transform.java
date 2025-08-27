package com.itextpdf.io.codec.brotli.dec;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.nio.ByteBuffer;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/Transform.class */
final class Transform {
    private final byte[] prefix;
    private final int type;
    private final byte[] suffix;
    static final Transform[] TRANSFORMS = {new Transform("", 0, ""), new Transform("", 0, SymbolConstants.SPACE_SYMBOL), new Transform(SymbolConstants.SPACE_SYMBOL, 0, SymbolConstants.SPACE_SYMBOL), new Transform("", 12, ""), new Transform("", 10, SymbolConstants.SPACE_SYMBOL), new Transform("", 0, " the "), new Transform(SymbolConstants.SPACE_SYMBOL, 0, ""), new Transform("s ", 0, SymbolConstants.SPACE_SYMBOL), new Transform("", 0, " of "), new Transform("", 10, ""), new Transform("", 0, " and "), new Transform("", 13, ""), new Transform("", 1, ""), new Transform(", ", 0, SymbolConstants.SPACE_SYMBOL), new Transform("", 0, ", "), new Transform(SymbolConstants.SPACE_SYMBOL, 10, SymbolConstants.SPACE_SYMBOL), new Transform("", 0, " in "), new Transform("", 0, " to "), new Transform("e ", 0, SymbolConstants.SPACE_SYMBOL), new Transform("", 0, SymbolConstants.QUOTES_SYMBOL), new Transform("", 0, "."), new Transform("", 0, "\">"), new Transform("", 0, ScriptUtils.FALLBACK_STATEMENT_SEPARATOR), new Transform("", 3, ""), new Transform("", 0, "]"), new Transform("", 0, " for "), new Transform("", 14, ""), new Transform("", 2, ""), new Transform("", 0, " a "), new Transform("", 0, " that "), new Transform(SymbolConstants.SPACE_SYMBOL, 10, ""), new Transform("", 0, ". "), new Transform(".", 0, ""), new Transform(SymbolConstants.SPACE_SYMBOL, 0, ", "), new Transform("", 15, ""), new Transform("", 0, " with "), new Transform("", 0, "'"), new Transform("", 0, " from "), new Transform("", 0, " by "), new Transform("", 16, ""), new Transform("", 17, ""), new Transform(" the ", 0, ""), new Transform("", 4, ""), new Transform("", 0, ". The "), new Transform("", 11, ""), new Transform("", 0, " on "), new Transform("", 0, " as "), new Transform("", 0, " is "), new Transform("", 7, ""), new Transform("", 1, "ing "), new Transform("", 0, "\n\t"), new Transform("", 0, ":"), new Transform(SymbolConstants.SPACE_SYMBOL, 0, ". "), new Transform("", 0, "ed "), new Transform("", 20, ""), new Transform("", 18, ""), new Transform("", 6, ""), new Transform("", 0, "("), new Transform("", 10, ", "), new Transform("", 8, ""), new Transform("", 0, " at "), new Transform("", 0, "ly "), new Transform(" the ", 0, " of "), new Transform("", 5, ""), new Transform("", 9, ""), new Transform(SymbolConstants.SPACE_SYMBOL, 10, ", "), new Transform("", 10, SymbolConstants.QUOTES_SYMBOL), new Transform(".", 0, "("), new Transform("", 11, SymbolConstants.SPACE_SYMBOL), new Transform("", 10, "\">"), new Transform("", 0, "=\""), new Transform(SymbolConstants.SPACE_SYMBOL, 0, "."), new Transform(".com/", 0, ""), new Transform(" the ", 0, " of the "), new Transform("", 10, "'"), new Transform("", 0, ". This "), new Transform("", 0, ","), new Transform(".", 0, SymbolConstants.SPACE_SYMBOL), new Transform("", 10, "("), new Transform("", 10, "."), new Transform("", 0, " not "), new Transform(SymbolConstants.SPACE_SYMBOL, 0, "=\""), new Transform("", 0, "er "), new Transform(SymbolConstants.SPACE_SYMBOL, 11, SymbolConstants.SPACE_SYMBOL), new Transform("", 0, "al "), new Transform(SymbolConstants.SPACE_SYMBOL, 11, ""), new Transform("", 0, "='"), new Transform("", 11, SymbolConstants.QUOTES_SYMBOL), new Transform("", 10, ". "), new Transform(SymbolConstants.SPACE_SYMBOL, 0, "("), new Transform("", 0, "ful "), new Transform(SymbolConstants.SPACE_SYMBOL, 10, ". "), new Transform("", 0, "ive "), new Transform("", 0, "less "), new Transform("", 11, "'"), new Transform("", 0, "est "), new Transform(SymbolConstants.SPACE_SYMBOL, 10, "."), new Transform("", 11, "\">"), new Transform(SymbolConstants.SPACE_SYMBOL, 0, "='"), new Transform("", 10, ","), new Transform("", 0, "ize "), new Transform("", 11, "."), new Transform("Â ", 0, ""), new Transform(SymbolConstants.SPACE_SYMBOL, 0, ","), new Transform("", 10, "=\""), new Transform("", 11, "=\""), new Transform("", 0, "ous "), new Transform("", 11, ", "), new Transform("", 10, "='"), new Transform(SymbolConstants.SPACE_SYMBOL, 10, ","), new Transform(SymbolConstants.SPACE_SYMBOL, 11, "=\""), new Transform(SymbolConstants.SPACE_SYMBOL, 11, ", "), new Transform("", 11, ","), new Transform("", 11, "("), new Transform("", 11, ". "), new Transform(SymbolConstants.SPACE_SYMBOL, 11, "."), new Transform("", 11, "='"), new Transform(SymbolConstants.SPACE_SYMBOL, 11, ". "), new Transform(SymbolConstants.SPACE_SYMBOL, 10, "=\""), new Transform(SymbolConstants.SPACE_SYMBOL, 11, "='"), new Transform(SymbolConstants.SPACE_SYMBOL, 10, "='")};

    Transform(String prefix, int type, String suffix) {
        this.prefix = readUniBytes(prefix);
        this.type = type;
        this.suffix = readUniBytes(suffix);
    }

    static byte[] readUniBytes(String uniBytes) {
        byte[] result = new byte[uniBytes.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) uniBytes.charAt(i);
        }
        return result;
    }

    static int transformDictionaryWord(byte[] dst, int dstOffset, ByteBuffer data, int wordOffset, int len, Transform transform) {
        int offset = dstOffset;
        byte[] string = transform.prefix;
        int tmp = string.length;
        int i = 0;
        while (i < tmp) {
            int i2 = offset;
            offset++;
            int i3 = i;
            i++;
            dst[i2] = string[i3];
        }
        int op = transform.type;
        int tmp2 = WordTransformType.getOmitFirst(op);
        if (tmp2 > len) {
            tmp2 = len;
        }
        int wordOffset2 = wordOffset + tmp2;
        int len2 = (len - tmp2) - WordTransformType.getOmitLast(op);
        for (int i4 = len2; i4 > 0; i4--) {
            int i5 = offset;
            offset++;
            int i6 = wordOffset2;
            wordOffset2++;
            dst[i5] = data.get(i6);
        }
        if (op == 11 || op == 10) {
            int uppercaseOffset = offset - len2;
            if (op == 10) {
                len2 = 1;
            }
            while (len2 > 0) {
                int tmp3 = dst[uppercaseOffset] & 255;
                if (tmp3 < 192) {
                    if (tmp3 >= 97 && tmp3 <= 122) {
                        int i7 = uppercaseOffset;
                        dst[i7] = (byte) (dst[i7] ^ 32);
                    }
                    uppercaseOffset++;
                    len2--;
                } else if (tmp3 < 224) {
                    int i8 = uppercaseOffset + 1;
                    dst[i8] = (byte) (dst[i8] ^ 32);
                    uppercaseOffset += 2;
                    len2 -= 2;
                } else {
                    int i9 = uppercaseOffset + 2;
                    dst[i9] = (byte) (dst[i9] ^ 5);
                    uppercaseOffset += 3;
                    len2 -= 3;
                }
            }
        }
        byte[] string2 = transform.suffix;
        int tmp4 = string2.length;
        int i10 = 0;
        while (i10 < tmp4) {
            int i11 = offset;
            offset++;
            int i12 = i10;
            i10++;
            dst[i11] = string2[i12];
        }
        return offset - dstOffset;
    }
}
