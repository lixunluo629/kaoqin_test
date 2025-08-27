package com.alibaba.excel.metadata;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/Font.class */
public class Font {
    private String fontName;
    private short fontHeightInPoints;
    private boolean bold;

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public short getFontHeightInPoints() {
        return this.fontHeightInPoints;
    }

    public void setFontHeightInPoints(short fontHeightInPoints) {
        this.fontHeightInPoints = fontHeightInPoints;
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
