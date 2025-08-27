package com.alibaba.excel.write.metadata.style;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/style/WriteFont.class */
public class WriteFont {
    private String fontName;
    private Short fontHeightInPoints;
    private Boolean italic;
    private Boolean strikeout;
    private Short color;
    private Short typeOffset;
    private Byte underline;
    private Integer charset;
    private Boolean bold;

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Short getFontHeightInPoints() {
        return this.fontHeightInPoints;
    }

    public void setFontHeightInPoints(Short fontHeightInPoints) {
        this.fontHeightInPoints = fontHeightInPoints;
    }

    public Boolean getItalic() {
        return this.italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getStrikeout() {
        return this.strikeout;
    }

    public void setStrikeout(Boolean strikeout) {
        this.strikeout = strikeout;
    }

    public Short getColor() {
        return this.color;
    }

    public void setColor(Short color) {
        this.color = color;
    }

    public Short getTypeOffset() {
        return this.typeOffset;
    }

    public void setTypeOffset(Short typeOffset) {
        this.typeOffset = typeOffset;
    }

    public Byte getUnderline() {
        return this.underline;
    }

    public void setUnderline(Byte underline) {
        this.underline = underline;
    }

    public Integer getCharset() {
        return this.charset;
    }

    public void setCharset(Integer charset) {
        this.charset = charset;
    }

    public Boolean getBold() {
        return this.bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }
}
