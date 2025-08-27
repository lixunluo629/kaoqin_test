package com.itextpdf.io.font;

import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontMetrics.class */
public class FontMetrics implements Serializable {
    private static final long serialVersionUID = -7113134666493365588L;
    private int numOfGlyphs;
    private int[] glyphWidths;
    private int ascender;
    private int descender;
    private int lineGap;
    private int winAscender;
    private int winDescender;
    private int advanceWidthMax;
    private int strikeoutPosition;
    private int strikeoutSize;
    private int subscriptSize;
    private int subscriptOffset;
    private int superscriptSize;
    private int superscriptOffset;
    private boolean isFixedPitch;
    protected float normalizationCoef = 1.0f;
    private int unitsPerEm = 1000;
    private int typoAscender = 800;
    private int typoDescender = -200;
    private int capHeight = 700;
    private int xHeight = 0;
    private float italicAngle = 0.0f;
    private int[] bbox = {-50, -200, 1000, 900};
    private int underlinePosition = -100;
    private int underlineThickness = 50;
    private int stemV = 80;
    private int stemH = 0;

    public int getUnitsPerEm() {
        return this.unitsPerEm;
    }

    public int getNumberOfGlyphs() {
        return this.numOfGlyphs;
    }

    public int[] getGlyphWidths() {
        return this.glyphWidths;
    }

    public int getTypoAscender() {
        return this.typoAscender;
    }

    public int getTypoDescender() {
        return this.typoDescender;
    }

    public int getCapHeight() {
        return this.capHeight;
    }

    public int getXHeight() {
        return this.xHeight;
    }

    public float getItalicAngle() {
        return this.italicAngle;
    }

    public int[] getBbox() {
        return this.bbox;
    }

    public void setBbox(int llx, int lly, int urx, int ury) {
        this.bbox[0] = llx;
        this.bbox[1] = lly;
        this.bbox[2] = urx;
        this.bbox[3] = ury;
    }

    public int getAscender() {
        return this.ascender;
    }

    public int getDescender() {
        return this.descender;
    }

    public int getLineGap() {
        return this.lineGap;
    }

    public int getWinAscender() {
        return this.winAscender;
    }

    public int getWinDescender() {
        return this.winDescender;
    }

    public int getAdvanceWidthMax() {
        return this.advanceWidthMax;
    }

    public int getUnderlinePosition() {
        return this.underlinePosition - (this.underlineThickness / 2);
    }

    public int getUnderlineThickness() {
        return this.underlineThickness;
    }

    public int getStrikeoutPosition() {
        return this.strikeoutPosition;
    }

    public int getStrikeoutSize() {
        return this.strikeoutSize;
    }

    public int getSubscriptSize() {
        return this.subscriptSize;
    }

    public int getSubscriptOffset() {
        return this.subscriptOffset;
    }

    public int getSuperscriptSize() {
        return this.superscriptSize;
    }

    public int getSuperscriptOffset() {
        return this.superscriptOffset;
    }

    public int getStemV() {
        return this.stemV;
    }

    public int getStemH() {
        return this.stemH;
    }

    public boolean isFixedPitch() {
        return this.isFixedPitch;
    }

    protected void setUnitsPerEm(int unitsPerEm) {
        this.unitsPerEm = unitsPerEm;
        this.normalizationCoef = 1000.0f / unitsPerEm;
    }

    protected void updateBbox(float llx, float lly, float urx, float ury) {
        this.bbox[0] = (int) (llx * this.normalizationCoef);
        this.bbox[1] = (int) (lly * this.normalizationCoef);
        this.bbox[2] = (int) (urx * this.normalizationCoef);
        this.bbox[3] = (int) (ury * this.normalizationCoef);
    }

    protected void setNumberOfGlyphs(int numOfGlyphs) {
        this.numOfGlyphs = numOfGlyphs;
    }

    protected void setGlyphWidths(int[] glyphWidths) {
        this.glyphWidths = glyphWidths;
    }

    protected void setTypoAscender(int typoAscender) {
        this.typoAscender = (int) (typoAscender * this.normalizationCoef);
    }

    protected void setTypoDescender(int typoDesctender) {
        this.typoDescender = (int) (typoDesctender * this.normalizationCoef);
    }

    protected void setCapHeight(int capHeight) {
        this.capHeight = (int) (capHeight * this.normalizationCoef);
    }

    protected void setXHeight(int xHeight) {
        this.xHeight = (int) (xHeight * this.normalizationCoef);
    }

    protected void setItalicAngle(float italicAngle) {
        this.italicAngle = italicAngle;
    }

    protected void setAscender(int ascender) {
        this.ascender = (int) (ascender * this.normalizationCoef);
    }

    protected void setDescender(int descender) {
        this.descender = (int) (descender * this.normalizationCoef);
    }

    protected void setLineGap(int lineGap) {
        this.lineGap = (int) (lineGap * this.normalizationCoef);
    }

    protected void setWinAscender(int winAscender) {
        this.winAscender = (int) (winAscender * this.normalizationCoef);
    }

    protected void setWinDescender(int winDescender) {
        this.winDescender = (int) (winDescender * this.normalizationCoef);
    }

    protected void setAdvanceWidthMax(int advanceWidthMax) {
        this.advanceWidthMax = (int) (advanceWidthMax * this.normalizationCoef);
    }

    protected void setUnderlinePosition(int underlinePosition) {
        this.underlinePosition = (int) (underlinePosition * this.normalizationCoef);
    }

    protected void setUnderlineThickness(int underineThickness) {
        this.underlineThickness = underineThickness;
    }

    protected void setStrikeoutPosition(int strikeoutPosition) {
        this.strikeoutPosition = (int) (strikeoutPosition * this.normalizationCoef);
    }

    protected void setStrikeoutSize(int strikeoutSize) {
        this.strikeoutSize = (int) (strikeoutSize * this.normalizationCoef);
    }

    protected void setSubscriptSize(int subscriptSize) {
        this.subscriptSize = (int) (subscriptSize * this.normalizationCoef);
    }

    protected void setSubscriptOffset(int subscriptOffset) {
        this.subscriptOffset = (int) (subscriptOffset * this.normalizationCoef);
    }

    protected void setSuperscriptSize(int superscriptSize) {
        this.superscriptSize = superscriptSize;
    }

    protected void setSuperscriptOffset(int superscriptOffset) {
        this.superscriptOffset = (int) (superscriptOffset * this.normalizationCoef);
    }

    public void setStemV(int stemV) {
        this.stemV = stemV;
    }

    protected void setStemH(int stemH) {
        this.stemH = stemH;
    }

    protected void setIsFixedPitch(boolean isFixedPitch) {
        this.isFixedPitch = isFixedPitch;
    }
}
