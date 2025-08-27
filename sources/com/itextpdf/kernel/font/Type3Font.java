package com.itextpdf.kernel.font;

import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.otf.Glyph;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/Type3Font.class */
public class Type3Font extends FontProgram {
    private static final long serialVersionUID = 1027076515537536993L;
    private boolean colorized;
    private final Map<Integer, Type3Glyph> type3Glyphs = new HashMap();
    private int flags = 0;

    Type3Font(boolean colorized) {
        this.colorized = false;
        this.colorized = colorized;
        this.fontNames = new FontNames();
        getFontMetrics().setBbox(0, 0, 0, 0);
    }

    public Type3Glyph getType3Glyph(int unicode) {
        return this.type3Glyphs.get(Integer.valueOf(unicode));
    }

    @Override // com.itextpdf.io.font.FontProgram
    public int getPdfFontFlags() {
        return this.flags;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public boolean isFontSpecific() {
        return false;
    }

    public boolean isColorized() {
        return this.colorized;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public int getKerning(Glyph glyph1, Glyph glyph2) {
        return 0;
    }

    public int getNumberOfGlyphs() {
        return this.type3Glyphs.size();
    }

    @Override // com.itextpdf.io.font.FontProgram
    protected void setFontName(String fontName) {
        super.setFontName(fontName);
    }

    @Override // com.itextpdf.io.font.FontProgram
    protected void setFontFamily(String fontFamily) {
        super.setFontFamily(fontFamily);
    }

    @Override // com.itextpdf.io.font.FontProgram
    protected void setFontWeight(int fontWeight) {
        super.setFontWeight(fontWeight);
    }

    @Override // com.itextpdf.io.font.FontProgram
    protected void setFontStretch(String fontWidth) {
        super.setFontStretch(fontWidth);
    }

    @Override // com.itextpdf.io.font.FontProgram
    protected void setItalicAngle(int italicAngle) {
        super.setItalicAngle(italicAngle);
    }

    void setPdfFontFlags(int flags) {
        this.flags = flags;
    }

    void addGlyph(int code, int unicode, int width, int[] bbox, Type3Glyph type3Glyph) {
        Glyph glyph = new Glyph(code, width, unicode, bbox);
        this.codeToGlyph.put(Integer.valueOf(code), glyph);
        this.unicodeToGlyph.put(Integer.valueOf(unicode), glyph);
        this.type3Glyphs.put(Integer.valueOf(unicode), type3Glyph);
    }
}
