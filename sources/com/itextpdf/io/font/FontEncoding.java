package com.itextpdf.io.font;

import com.itextpdf.io.util.ArrayUtil;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.io.util.TextUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringTokenizer;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontEncoding.class */
public class FontEncoding implements Serializable {
    private static final long serialVersionUID = -684967385759439083L;
    private static final byte[] emptyBytes = new byte[0];
    public static final String FONT_SPECIFIC = "FontSpecific";
    public static final String NOTDEF = ".notdef";
    protected String baseEncoding;
    protected String[] differences;
    protected IntHashtable unicodeToCode = new IntHashtable(256);
    protected int[] codeToUnicode = ArrayUtil.fillWithValue(new int[256], -1);
    protected IntHashtable unicodeDifferences = new IntHashtable(256);
    protected boolean fontSpecific = false;

    protected FontEncoding() {
    }

    public static FontEncoding createFontEncoding(String baseEncoding) throws NumberFormatException {
        FontEncoding encoding = new FontEncoding();
        encoding.baseEncoding = normalizeEncoding(baseEncoding);
        if (encoding.baseEncoding.startsWith("#")) {
            encoding.fillCustomEncoding();
        } else {
            encoding.fillNamedEncoding();
        }
        return encoding;
    }

    public static FontEncoding createEmptyFontEncoding() {
        FontEncoding encoding = new FontEncoding();
        encoding.baseEncoding = null;
        encoding.fontSpecific = false;
        encoding.differences = new String[256];
        for (int ch2 = 0; ch2 < 256; ch2++) {
            encoding.unicodeDifferences.put(ch2, ch2);
        }
        return encoding;
    }

    public static FontEncoding createFontSpecificEncoding() {
        FontEncoding encoding = new FontEncoding();
        encoding.fontSpecific = true;
        for (int ch2 = 0; ch2 < 256; ch2++) {
            encoding.unicodeToCode.put(ch2, ch2);
            encoding.codeToUnicode[ch2] = ch2;
            encoding.unicodeDifferences.put(ch2, ch2);
        }
        return encoding;
    }

    public String getBaseEncoding() {
        return this.baseEncoding;
    }

    public boolean isFontSpecific() {
        return this.fontSpecific;
    }

    public boolean addSymbol(int code, int unicode) {
        String glyphName;
        if (code >= 0 && code <= 255 && (glyphName = AdobeGlyphList.unicodeToName(unicode)) != null) {
            this.unicodeToCode.put(unicode, code);
            this.codeToUnicode[code] = unicode;
            this.differences[code] = glyphName;
            this.unicodeDifferences.put(unicode, unicode);
            return true;
        }
        return false;
    }

    public int getUnicode(int index) {
        return this.codeToUnicode[index];
    }

    public int getUnicodeDifference(int index) {
        return this.unicodeDifferences.get(index);
    }

    public boolean hasDifferences() {
        return this.differences != null;
    }

    public String getDifference(int index) {
        if (this.differences != null) {
            return this.differences[index];
        }
        return null;
    }

    public byte[] convertToBytes(String text) {
        if (text == null || text.length() == 0) {
            return emptyBytes;
        }
        int ptr = 0;
        byte[] bytes = new byte[text.length()];
        for (int i = 0; i < text.length(); i++) {
            if (this.unicodeToCode.containsKey(text.charAt(i))) {
                int i2 = ptr;
                ptr++;
                bytes[i2] = (byte) convertToByte(text.charAt(i));
            }
        }
        return ArrayUtil.shortenArray(bytes, ptr);
    }

    public int convertToByte(int unicode) {
        return this.unicodeToCode.get(unicode);
    }

    public boolean canEncode(int unicode) {
        return this.unicodeToCode.containsKey(unicode) || TextUtil.isNonPrintable(unicode) || TextUtil.isNewLine(unicode);
    }

    public boolean canDecode(int code) {
        return this.codeToUnicode[code] > -1;
    }

    public boolean isBuiltWith(String encoding) {
        return Objects.equals(normalizeEncoding(encoding), this.baseEncoding);
    }

    protected void fillCustomEncoding() throws NumberFormatException {
        int orderK;
        this.differences = new String[256];
        StringTokenizer tok = new StringTokenizer(this.baseEncoding.substring(1), " ,\t\n\r\f");
        if (tok.nextToken().equals("full")) {
            while (tok.hasMoreTokens()) {
                String order = tok.nextToken();
                String name = tok.nextToken();
                char uni = (char) Integer.parseInt(tok.nextToken(), 16);
                int uniName = AdobeGlyphList.nameToUnicode(name);
                if (order.startsWith("'")) {
                    orderK = order.charAt(1);
                } else {
                    orderK = Integer.parseInt(order);
                }
                int orderK2 = orderK % 256;
                this.unicodeToCode.put(uni, orderK2);
                this.codeToUnicode[orderK2] = uni;
                this.differences[orderK2] = name;
                this.unicodeDifferences.put(uni, uniName);
            }
        } else {
            int k = 0;
            if (tok.hasMoreTokens()) {
                k = Integer.parseInt(tok.nextToken());
            }
            while (tok.hasMoreTokens() && k < 256) {
                String hex = tok.nextToken();
                int uni2 = Integer.parseInt(hex, 16) % 65536;
                String name2 = AdobeGlyphList.unicodeToName(uni2);
                if (name2 == null) {
                    name2 = "uni" + hex;
                }
                this.unicodeToCode.put(uni2, k);
                this.codeToUnicode[k] = uni2;
                this.differences[k] = name2;
                this.unicodeDifferences.put(uni2, uni2);
                k++;
            }
        }
        for (int k2 = 0; k2 < 256; k2++) {
            if (this.differences[k2] == null) {
                this.differences[k2] = NOTDEF;
            }
        }
    }

    protected void fillNamedEncoding() {
        PdfEncodings.convertToBytes(SymbolConstants.SPACE_SYMBOL, this.baseEncoding);
        boolean stdEncoding = "Cp1252".equals(this.baseEncoding) || PdfEncodings.MACROMAN.equals(this.baseEncoding);
        if (!stdEncoding && this.differences == null) {
            this.differences = new String[256];
        }
        byte[] b = new byte[256];
        for (int k = 0; k < 256; k++) {
            b[k] = (byte) k;
        }
        String str = PdfEncodings.convertToString(b, this.baseEncoding);
        char[] encoded = str.toCharArray();
        for (int ch2 = 0; ch2 < 256; ch2++) {
            char uni = encoded[ch2];
            String name = AdobeGlyphList.unicodeToName(uni);
            if (name == null) {
                name = NOTDEF;
            } else {
                this.unicodeToCode.put(uni, ch2);
                this.codeToUnicode[ch2] = uni;
                this.unicodeDifferences.put(uni, uni);
            }
            if (this.differences != null) {
                this.differences[ch2] = name;
            }
        }
    }

    protected void fillStandardEncoding() {
        int[] encoded = PdfEncodings.standardEncoding;
        for (int ch2 = 0; ch2 < 256; ch2++) {
            int uni = encoded[ch2];
            String name = AdobeGlyphList.unicodeToName(uni);
            if (name == null) {
                name = NOTDEF;
            } else {
                this.unicodeToCode.put(uni, ch2);
                this.codeToUnicode[ch2] = uni;
                this.unicodeDifferences.put(uni, uni);
            }
            if (this.differences != null) {
                this.differences[ch2] = name;
            }
        }
    }

    protected static String normalizeEncoding(String enc) {
        String tmp = enc == null ? "" : enc.toLowerCase();
        switch (tmp) {
            case "":
            case "winansi":
            case "winansiencoding":
                return "Cp1252";
            case "macroman":
            case "macromanencoding":
                return PdfEncodings.MACROMAN;
            case "zapfdingbatsencoding":
                return "ZapfDingbats";
            default:
                return enc;
        }
    }
}
