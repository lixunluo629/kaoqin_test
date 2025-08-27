package com.itextpdf.io.font.cmap;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.io.util.TextUtil;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapToUnicode.class */
public class CMapToUnicode extends AbstractCMap {
    private static final long serialVersionUID = 1037675640549795312L;
    public static CMapToUnicode EmptyCMapToUnicodeMap = new CMapToUnicode(true);
    private Map<Integer, char[]> byteMappings;

    private CMapToUnicode(boolean emptyCMap) {
        this.byteMappings = Collections.emptyMap();
    }

    public CMapToUnicode() {
        this.byteMappings = new HashMap();
    }

    public static CMapToUnicode getIdentity() {
        CMapToUnicode uni = new CMapToUnicode();
        for (int i = 0; i < 65537; i++) {
            uni.addChar(i, TextUtil.convertFromUtf32(i));
        }
        return uni;
    }

    public boolean hasByteMappings() {
        return this.byteMappings.size() != 0;
    }

    public char[] lookup(byte[] code, int offset, int length) {
        char[] result = null;
        if (length == 1) {
            int key = code[offset] & 255;
            result = this.byteMappings.get(Integer.valueOf(key));
        } else if (length == 2) {
            int intKey = code[offset] & 255;
            result = this.byteMappings.get(Integer.valueOf((intKey << 8) + (code[offset + 1] & 255)));
        }
        return result;
    }

    public char[] lookup(byte[] code) {
        return lookup(code, 0, code.length);
    }

    public char[] lookup(int code) {
        return this.byteMappings.get(Integer.valueOf(code));
    }

    public Set<Integer> getCodes() {
        return this.byteMappings.keySet();
    }

    public IntHashtable createDirectMapping() {
        IntHashtable result = new IntHashtable();
        for (Map.Entry<Integer, char[]> entry : this.byteMappings.entrySet()) {
            if (entry.getValue().length == 1) {
                result.put(entry.getKey().intValue(), convertToInt(entry.getValue()));
            }
        }
        return result;
    }

    public Map<Integer, Integer> createReverseMapping() throws IOException {
        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<Integer, char[]> entry : this.byteMappings.entrySet()) {
            if (entry.getValue().length == 1) {
                result.put(Integer.valueOf(convertToInt(entry.getValue())), entry.getKey());
            }
        }
        return result;
    }

    private int convertToInt(char[] s) {
        int value = 0;
        for (int i = 0; i < s.length - 1; i++) {
            value = (value + s[i]) << 8;
        }
        return value + s[s.length - 1];
    }

    void addChar(int cid, char[] uni) {
        this.byteMappings.put(Integer.valueOf(cid), uni);
    }

    @Override // com.itextpdf.io.font.cmap.AbstractCMap
    void addChar(String mark, CMapObject code) {
        if (mark.length() == 1) {
            char[] dest = createCharsFromDoubleBytes((byte[]) code.getValue());
            this.byteMappings.put(Integer.valueOf(mark.charAt(0)), dest);
        } else if (mark.length() == 2) {
            char[] dest2 = createCharsFromDoubleBytes((byte[]) code.getValue());
            this.byteMappings.put(Integer.valueOf((mark.charAt(0) << '\b') + mark.charAt(1)), dest2);
        } else {
            Logger logger = LoggerFactory.getLogger((Class<?>) CMapToUnicode.class);
            logger.warn(LogMessageConstant.TOUNICODE_CMAP_MORE_THAN_2_BYTES_NOT_SUPPORTED);
        }
    }

    private char[] createCharsFromSingleBytes(byte[] bytes) {
        if (bytes.length == 1) {
            return new char[]{(char) (bytes[0] & 255)};
        }
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char) (bytes[i] & 255);
        }
        return chars;
    }

    private char[] createCharsFromDoubleBytes(byte[] bytes) {
        char[] chars = new char[bytes.length / 2];
        for (int i = 0; i < bytes.length; i += 2) {
            chars[i / 2] = (char) (((bytes[i] & 255) << 8) + (bytes[i + 1] & 255));
        }
        return chars;
    }
}
