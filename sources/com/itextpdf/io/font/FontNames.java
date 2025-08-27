package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.FontStretches;
import com.itextpdf.io.font.constants.FontWeights;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontNames.class */
public class FontNames implements Serializable {
    private static final long serialVersionUID = 1005168842463622025L;
    protected Map<Integer, List<String[]>> allNames;
    private String[][] fullName;
    private String[][] familyName;
    private String[][] subfamily;
    private String fontName;
    private String cidFontName;
    private int macStyle;
    private boolean allowEmbedding;
    private String style = "";
    private int weight = 400;
    private String fontStretch = FontStretches.NORMAL;

    public String[][] getNames(int id) {
        List<String[]> names = this.allNames.get(Integer.valueOf(id));
        return (names == null || names.size() <= 0) ? (String[][]) null : listToArray(names);
    }

    public String[][] getFullName() {
        return this.fullName;
    }

    public String getFontName() {
        return this.fontName;
    }

    public String getCidFontName() {
        return this.cidFontName;
    }

    public String[][] getFamilyName() {
        return this.familyName;
    }

    public String getStyle() {
        return this.style;
    }

    public String getSubfamily() {
        return this.subfamily != null ? this.subfamily[0][3] : "";
    }

    public int getFontWeight() {
        return this.weight;
    }

    protected void setFontWeight(int weight) {
        this.weight = FontWeights.normalizeFontWeight(weight);
    }

    public String getFontStretch() {
        return this.fontStretch;
    }

    protected void setFontStretch(String fontStretch) {
        this.fontStretch = fontStretch;
    }

    public boolean allowEmbedding() {
        return this.allowEmbedding;
    }

    public boolean isBold() {
        return (this.macStyle & 1) != 0;
    }

    public boolean isItalic() {
        return (this.macStyle & 2) != 0;
    }

    public boolean isUnderline() {
        return (this.macStyle & 4) != 0;
    }

    public boolean isOutline() {
        return (this.macStyle & 8) != 0;
    }

    public boolean isShadow() {
        return (this.macStyle & 16) != 0;
    }

    public boolean isCondensed() {
        return (this.macStyle & 32) != 0;
    }

    public boolean isExtended() {
        return (this.macStyle & 64) != 0;
    }

    protected void setAllNames(Map<Integer, List<String[]>> allNames) {
        this.allNames = allNames;
    }

    protected void setFullName(String[][] fullName) {
        this.fullName = fullName;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String[], java.lang.String[][]] */
    protected void setFullName(String fullName) {
        this.fullName = new String[]{new String[]{"", "", "", fullName}};
    }

    protected void setFontName(String psFontName) {
        this.fontName = psFontName;
    }

    protected void setCidFontName(String cidFontName) {
        this.cidFontName = cidFontName;
    }

    protected void setFamilyName(String[][] familyName) {
        this.familyName = familyName;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String[], java.lang.String[][]] */
    protected void setFamilyName(String familyName) {
        this.familyName = new String[]{new String[]{"", "", "", familyName}};
    }

    protected void setStyle(String style) {
        this.style = style;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String[], java.lang.String[][]] */
    protected void setSubfamily(String subfamily) {
        this.subfamily = new String[]{new String[]{"", "", "", subfamily}};
    }

    protected void setSubfamily(String[][] subfamily) {
        this.subfamily = subfamily;
    }

    protected void setMacStyle(int macStyle) {
        this.macStyle = macStyle;
    }

    protected int getMacStyle() {
        return this.macStyle;
    }

    protected void setAllowEmbedding(boolean allowEmbedding) {
        this.allowEmbedding = allowEmbedding;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.String[], java.lang.String[][]] */
    private String[][] listToArray(List<String[]> list) {
        ?? r0 = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            r0[i] = list.get(i);
        }
        return r0;
    }

    public String toString() {
        String name = getFontName();
        return name.length() > 0 ? name : super.toString();
    }
}
