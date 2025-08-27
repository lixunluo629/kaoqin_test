package com.itextpdf.io.font.cmap;

import com.itextpdf.io.font.PdfEncodings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/AbstractCMap.class */
public abstract class AbstractCMap implements Serializable {
    private static final long serialVersionUID = -9057458889624600915L;
    private String cmapName;
    private String registry;
    private String ordering;
    private int supplement;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract void addChar(String str, CMapObject cMapObject);

    static {
        $assertionsDisabled = !AbstractCMap.class.desiredAssertionStatus();
    }

    public String getName() {
        return this.cmapName;
    }

    void setName(String cmapName) {
        this.cmapName = cmapName;
    }

    public String getOrdering() {
        return this.ordering;
    }

    void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public String getRegistry() {
        return this.registry;
    }

    void setRegistry(String registry) {
        this.registry = registry;
    }

    public int getSupplement() {
        return this.supplement;
    }

    void setSupplement(int supplement) {
        this.supplement = supplement;
    }

    void addCodeSpaceRange(byte[] low, byte[] high) {
    }

    void addRange(String from, String to, CMapObject code) {
        byte[] a1 = decodeStringToByte(from);
        byte[] a2 = decodeStringToByte(to);
        if (a1.length != a2.length || a1.length == 0) {
            throw new IllegalArgumentException("Invalid map.");
        }
        byte[] sout = null;
        if (code.isString()) {
            sout = decodeStringToByte(code.toString());
        }
        int start = byteArrayToInt(a1);
        int end = byteArrayToInt(a2);
        for (int k = start; k <= end; k++) {
            intToByteArray(k, a1);
            String mark = PdfEncodings.convertToString(a1, null);
            if (code.isArray()) {
                List<CMapObject> codes = (ArrayList) code.getValue();
                addChar(mark, codes.get(k - start));
            } else if (code.isNumber()) {
                int nn = (((Integer) code.getValue()).intValue() + k) - start;
                addChar(mark, new CMapObject(4, Integer.valueOf(nn)));
            } else if (code.isString()) {
                CMapObject s1 = new CMapObject(2, sout);
                addChar(mark, s1);
                if (!$assertionsDisabled && sout == null) {
                    throw new AssertionError();
                }
                intToByteArray(byteArrayToInt(sout) + 1, sout);
            } else {
                continue;
            }
        }
    }

    public static byte[] decodeStringToByte(String range) {
        byte[] bytes = new byte[range.length()];
        for (int i = 0; i < range.length(); i++) {
            bytes[i] = (byte) range.charAt(i);
        }
        return bytes;
    }

    protected String toUnicodeString(String value, boolean isHexWriting) {
        byte[] bytes = decodeStringToByte(value);
        if (isHexWriting) {
            return PdfEncodings.convertToString(bytes, PdfEncodings.UNICODE_BIG_UNMARKED);
        }
        if (bytes.length >= 2 && bytes[0] == -2 && bytes[1] == -1) {
            return PdfEncodings.convertToString(bytes, PdfEncodings.UNICODE_BIG);
        }
        return PdfEncodings.convertToString(bytes, PdfEncodings.PDF_DOC_ENCODING);
    }

    private static void intToByteArray(int n, byte[] b) {
        for (int k = b.length - 1; k >= 0; k--) {
            b[k] = (byte) n;
            n >>>= 8;
        }
    }

    private static int byteArrayToInt(byte[] b) {
        int n = 0;
        for (byte b2 : b) {
            n = (n << 8) | (b2 & 255);
        }
        return n;
    }
}
