package com.itextpdf.kernel.font;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfOutputStream;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/PdfFont.class */
public abstract class PdfFont extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -7661159455613720321L;
    protected FontProgram fontProgram;
    protected static final byte[] EMPTY_BYTES = new byte[0];
    protected static final double[] DEFAULT_FONT_MATRIX = {0.001d, 0.0d, 0.0d, 0.001d, 0.0d, 0.0d};
    protected Map<Integer, Glyph> notdefGlyphs;
    protected boolean newFont;
    protected boolean embedded;
    protected boolean subset;
    protected List<int[]> subsetRanges;

    public abstract Glyph getGlyph(int i);

    public abstract GlyphLine createGlyphLine(String str);

    public abstract int appendGlyphs(String str, int i, int i2, List<Glyph> list);

    public abstract int appendAnyGlyph(String str, int i, List<Glyph> list);

    public abstract byte[] convertToBytes(String str);

    public abstract byte[] convertToBytes(GlyphLine glyphLine);

    public abstract String decode(PdfString pdfString);

    public abstract GlyphLine decodeIntoGlyphLine(PdfString pdfString);

    public abstract float getContentWidth(PdfString pdfString);

    public abstract byte[] convertToBytes(Glyph glyph);

    public abstract void writeText(GlyphLine glyphLine, int i, int i2, PdfOutputStream pdfOutputStream);

    public abstract void writeText(String str, PdfOutputStream pdfOutputStream);

    protected abstract PdfDictionary getFontDescriptor(String str);

    protected PdfFont(PdfDictionary fontDictionary) {
        super(fontDictionary);
        this.notdefGlyphs = new HashMap();
        this.newFont = true;
        this.embedded = false;
        this.subset = true;
        getPdfObject().put(PdfName.Type, PdfName.Font);
    }

    protected PdfFont() {
        super(new PdfDictionary());
        this.notdefGlyphs = new HashMap();
        this.newFont = true;
        this.embedded = false;
        this.subset = true;
        getPdfObject().put(PdfName.Type, PdfName.Font);
    }

    public boolean containsGlyph(int unicode) {
        Glyph glyph = getGlyph(unicode);
        if (glyph != null) {
            return (getFontProgram() == null || !getFontProgram().isFontSpecific()) ? glyph.getCode() > 0 : glyph.getCode() > -1;
        }
        return false;
    }

    public double[] getFontMatrix() {
        return DEFAULT_FONT_MATRIX;
    }

    public int getWidth(int unicode) {
        Glyph glyph = getGlyph(unicode);
        if (glyph != null) {
            return glyph.getWidth();
        }
        return 0;
    }

    public float getWidth(int unicode, float fontSize) {
        return (getWidth(unicode) * fontSize) / 1000.0f;
    }

    public int getWidth(String text) {
        int ch2;
        int total = 0;
        int i = 0;
        while (i < text.length()) {
            if (TextUtil.isSurrogatePair(text, i)) {
                ch2 = TextUtil.convertToUtf32(text, i);
                i++;
            } else {
                ch2 = text.charAt(i);
            }
            Glyph glyph = getGlyph(ch2);
            if (glyph != null) {
                total += glyph.getWidth();
            }
            i++;
        }
        return total;
    }

    public float getWidth(String text, float fontSize) {
        return (getWidth(text) * fontSize) / 1000.0f;
    }

    public int getDescent(String text, float fontSize) {
        int ch2;
        int min = 0;
        int k = 0;
        while (k < text.length()) {
            if (TextUtil.isSurrogatePair(text, k)) {
                ch2 = TextUtil.convertToUtf32(text, k);
                k++;
            } else {
                ch2 = text.charAt(k);
            }
            Glyph glyph = getGlyph(ch2);
            if (glyph != null) {
                int[] bbox = glyph.getBbox();
                if (bbox != null && bbox[1] < min) {
                    min = bbox[1];
                } else if (bbox == null && getFontProgram().getFontMetrics().getTypoDescender() < min) {
                    min = getFontProgram().getFontMetrics().getTypoDescender();
                }
            }
            k++;
        }
        return (int) ((min * fontSize) / 1000.0f);
    }

    public int getDescent(int unicode, float fontSize) {
        int min = 0;
        Glyph glyph = getGlyph(unicode);
        if (glyph == null) {
            return 0;
        }
        int[] bbox = glyph.getBbox();
        if (bbox != null && bbox[1] < 0) {
            min = bbox[1];
        } else if (bbox == null && getFontProgram().getFontMetrics().getTypoDescender() < 0) {
            min = getFontProgram().getFontMetrics().getTypoDescender();
        }
        return (int) ((min * fontSize) / 1000.0f);
    }

    public int getAscent(String text, float fontSize) {
        int ch2;
        int max = 0;
        int k = 0;
        while (k < text.length()) {
            if (TextUtil.isSurrogatePair(text, k)) {
                ch2 = TextUtil.convertToUtf32(text, k);
                k++;
            } else {
                ch2 = text.charAt(k);
            }
            Glyph glyph = getGlyph(ch2);
            if (glyph != null) {
                int[] bbox = glyph.getBbox();
                if (bbox != null && bbox[3] > max) {
                    max = bbox[3];
                } else if (bbox == null && getFontProgram().getFontMetrics().getTypoAscender() > max) {
                    max = getFontProgram().getFontMetrics().getTypoAscender();
                }
            }
            k++;
        }
        return (int) ((max * fontSize) / 1000.0f);
    }

    public int getAscent(int unicode, float fontSize) {
        int max = 0;
        Glyph glyph = getGlyph(unicode);
        if (glyph == null) {
            return 0;
        }
        int[] bbox = glyph.getBbox();
        if (bbox != null && bbox[3] > 0) {
            max = bbox[3];
        } else if (bbox == null && getFontProgram().getFontMetrics().getTypoAscender() > 0) {
            max = getFontProgram().getFontMetrics().getTypoAscender();
        }
        return (int) ((max * fontSize) / 1000.0f);
    }

    public FontProgram getFontProgram() {
        return this.fontProgram;
    }

    public boolean isEmbedded() {
        return this.embedded;
    }

    public boolean isSubset() {
        return this.subset;
    }

    public void setSubset(boolean subset) {
        this.subset = subset;
    }

    public void addSubsetRange(int[] range) {
        if (this.subsetRanges == null) {
            this.subsetRanges = new ArrayList();
        }
        this.subsetRanges.add(range);
        setSubset(true);
    }

    public List<String> splitString(String text, float fontSize, float maxWidth) {
        List<String> resultString = new ArrayList<>();
        int lastWhiteSpace = 0;
        int startPos = 0;
        float tokenLength = 0.0f;
        int i = 0;
        while (i < text.length()) {
            char ch2 = text.charAt(i);
            if (Character.isWhitespace(ch2)) {
                lastWhiteSpace = i;
            }
            float currentCharWidth = getWidth(ch2, fontSize);
            if (tokenLength + currentCharWidth >= maxWidth || ch2 == '\n') {
                if (startPos < lastWhiteSpace) {
                    resultString.add(text.substring(startPos, lastWhiteSpace));
                    startPos = lastWhiteSpace + 1;
                    tokenLength = 0.0f;
                    i = lastWhiteSpace;
                } else if (startPos != i) {
                    resultString.add(text.substring(startPos, i));
                    startPos = i;
                    tokenLength = currentCharWidth;
                } else {
                    resultString.add(text.substring(startPos, startPos + 1));
                    startPos = i + 1;
                    tokenLength = 0.0f;
                }
            } else {
                tokenLength += currentCharWidth;
            }
            i++;
        }
        resultString.add(text.substring(startPos));
        return resultString;
    }

    public boolean isBuiltWith(String fontProgram, String encoding) {
        return false;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    protected static String updateSubsetPrefix(String fontName, boolean isSubset, boolean isEmbedded) {
        if (isSubset && isEmbedded) {
            StringBuilder s = new StringBuilder(fontName.length() + 7);
            for (int k = 0; k < 6; k++) {
                s.append((char) ((Math.random() * 26.0d) + 65.0d));
            }
            return s.append('+').append(fontName).toString();
        }
        return fontName;
    }

    protected PdfStream getPdfFontStream(byte[] fontStreamBytes, int[] fontStreamLengths) {
        if (fontStreamBytes == null) {
            throw new PdfException(PdfException.FontEmbeddingIssue);
        }
        PdfStream fontStream = new PdfStream(fontStreamBytes);
        makeObjectIndirect(fontStream);
        for (int k = 0; k < fontStreamLengths.length; k++) {
            fontStream.put(new PdfName("Length" + (k + 1)), new PdfNumber(fontStreamLengths[k]));
        }
        return fontStream;
    }

    @Deprecated
    protected static int[] compactRanges(List<int[]> ranges) {
        List<int[]> simp = new ArrayList<>();
        for (int[] range : ranges) {
            for (int j = 0; j < range.length; j += 2) {
                simp.add(new int[]{Math.max(0, Math.min(range[j], range[j + 1])), Math.min(65535, Math.max(range[j], range[j + 1]))});
            }
        }
        for (int k1 = 0; k1 < simp.size() - 1; k1++) {
            int k2 = k1 + 1;
            while (k2 < simp.size()) {
                int[] r1 = simp.get(k1);
                int[] r2 = simp.get(k2);
                if ((r1[0] >= r2[0] && r1[0] <= r2[1]) || (r1[1] >= r2[0] && r1[0] <= r2[1])) {
                    r1[0] = Math.min(r1[0], r2[0]);
                    r1[1] = Math.max(r1[1], r2[1]);
                    simp.remove(k2);
                    k2--;
                }
                k2++;
            }
        }
        int[] s = new int[simp.size() * 2];
        for (int k = 0; k < simp.size(); k++) {
            int[] r = simp.get(k);
            s[k * 2] = r[0];
            s[(k * 2) + 1] = r[1];
        }
        return s;
    }

    boolean makeObjectIndirect(PdfObject obj) {
        if (getPdfObject().getIndirectReference() != null) {
            obj.makeIndirect(getPdfObject().getIndirectReference().getDocument());
            return true;
        }
        markObjectAsIndirect(obj);
        return false;
    }

    public String toString() {
        return "PdfFont{fontProgram=" + this.fontProgram + '}';
    }
}
