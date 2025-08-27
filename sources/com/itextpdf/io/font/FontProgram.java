package com.itextpdf.io.font;

import com.itextpdf.io.font.otf.Glyph;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontProgram.class */
public abstract class FontProgram implements Serializable {
    private static final long serialVersionUID = -3488910249070253659L;
    public static final int DEFAULT_WIDTH = 1000;
    public static final int UNITS_NORMALIZATION = 1000;
    protected boolean isFontSpecific;
    protected FontNames fontNames;
    protected int avgWidth;
    protected String registry;
    protected Map<Integer, Glyph> codeToGlyph = new HashMap();
    protected Map<Integer, Glyph> unicodeToGlyph = new HashMap();
    protected FontMetrics fontMetrics = new FontMetrics();
    protected FontIdentification fontIdentification = new FontIdentification();
    protected String encodingScheme = FontEncoding.FONT_SPECIFIC;

    public abstract int getPdfFontFlags();

    public abstract int getKerning(Glyph glyph, Glyph glyph2);

    public int countOfGlyphs() {
        return Math.max(this.codeToGlyph.size(), this.unicodeToGlyph.size());
    }

    public FontNames getFontNames() {
        return this.fontNames;
    }

    public FontMetrics getFontMetrics() {
        return this.fontMetrics;
    }

    public FontIdentification getFontIdentification() {
        return this.fontIdentification;
    }

    public String getRegistry() {
        return this.registry;
    }

    public boolean isFontSpecific() {
        return this.isFontSpecific;
    }

    public int getWidth(int unicode) {
        Glyph glyph = getGlyph(unicode);
        if (glyph != null) {
            return glyph.getWidth();
        }
        return 0;
    }

    public int getAvgWidth() {
        return this.avgWidth;
    }

    public int[] getCharBBox(int unicode) {
        Glyph glyph = getGlyph(unicode);
        if (glyph != null) {
            return glyph.getBbox();
        }
        return null;
    }

    public Glyph getGlyph(int unicode) {
        return this.unicodeToGlyph.get(Integer.valueOf(unicode));
    }

    public Glyph getGlyphByCode(int charCode) {
        return this.codeToGlyph.get(Integer.valueOf(charCode));
    }

    public boolean hasKernPairs() {
        return false;
    }

    public int getKerning(int first, int second) {
        return getKerning(this.unicodeToGlyph.get(Integer.valueOf(first)), this.unicodeToGlyph.get(Integer.valueOf(second)));
    }

    public boolean isBuiltWith(String fontName) {
        return false;
    }

    protected void setRegistry(String registry) {
        this.registry = registry;
    }

    static String trimFontStyle(String name) {
        if (name == null) {
            return null;
        }
        if (name.endsWith(",Bold")) {
            return name.substring(0, name.length() - 5);
        }
        if (name.endsWith(",Italic")) {
            return name.substring(0, name.length() - 7);
        }
        if (name.endsWith(",BoldItalic")) {
            return name.substring(0, name.length() - 11);
        }
        return name;
    }

    protected void setTypoAscender(int ascender) {
        this.fontMetrics.setTypoAscender(ascender);
    }

    protected void setTypoDescender(int descender) {
        this.fontMetrics.setTypoDescender(descender);
    }

    protected void setCapHeight(int capHeight) {
        this.fontMetrics.setCapHeight(capHeight);
    }

    protected void setXHeight(int xHeight) {
        this.fontMetrics.setXHeight(xHeight);
    }

    protected void setItalicAngle(int italicAngle) {
        this.fontMetrics.setItalicAngle(italicAngle);
    }

    protected void setStemV(int stemV) {
        this.fontMetrics.setStemV(stemV);
    }

    protected void setStemH(int stemH) {
        this.fontMetrics.setStemH(stemH);
    }

    protected void setFontWeight(int fontWeight) {
        this.fontNames.setFontWeight(fontWeight);
    }

    protected void setFontStretch(String fontWidth) {
        this.fontNames.setFontStretch(fontWidth);
    }

    protected void setFixedPitch(boolean isFixedPitch) {
        this.fontMetrics.setIsFixedPitch(isFixedPitch);
    }

    protected void setBold(boolean isBold) {
        if (isBold) {
            this.fontNames.setMacStyle(this.fontNames.getMacStyle() | 1);
        } else {
            this.fontNames.setMacStyle(this.fontNames.getMacStyle() & (-2));
        }
    }

    protected void setBbox(int[] bbox) {
        this.fontMetrics.setBbox(bbox[0], bbox[1], bbox[2], bbox[3]);
    }

    protected void setFontFamily(String fontFamily) {
        this.fontNames.setFamilyName(fontFamily);
    }

    protected void setFontName(String fontName) {
        this.fontNames.setFontName(fontName);
        if (this.fontNames.getFullName() == null) {
            this.fontNames.setFullName(fontName);
        }
    }

    protected void fixSpaceIssue() {
        Glyph space = this.unicodeToGlyph.get(32);
        if (space != null) {
            this.codeToGlyph.put(Integer.valueOf(space.getCode()), space);
        }
    }

    public String toString() {
        String name = getFontNames().getFontName();
        return name.length() > 0 ? name : super.toString();
    }
}
