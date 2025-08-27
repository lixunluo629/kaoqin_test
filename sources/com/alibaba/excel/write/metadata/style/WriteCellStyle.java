package com.alibaba.excel.write.metadata.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/style/WriteCellStyle.class */
public class WriteCellStyle {
    private Short dataFormat;
    private WriteFont writeFont;
    private Boolean hidden;
    private Boolean locked;
    private Boolean quotePrefix;
    private HorizontalAlignment horizontalAlignment;
    private Boolean wrapped;
    private VerticalAlignment verticalAlignment;
    private Short rotation;
    private Short indent;
    private BorderStyle borderLeft;
    private BorderStyle borderRight;
    private BorderStyle borderTop;
    private BorderStyle borderBottom;
    private Short leftBorderColor;
    private Short rightBorderColor;
    private Short topBorderColor;
    private Short bottomBorderColor;
    private FillPatternType fillPatternType;
    private Short fillBackgroundColor;
    private Short fillForegroundColor;
    private Boolean shrinkToFit;

    public Short getDataFormat() {
        return this.dataFormat;
    }

    public void setDataFormat(Short dataFormat) {
        this.dataFormat = dataFormat;
    }

    public WriteFont getWriteFont() {
        return this.writeFont;
    }

    public void setWriteFont(WriteFont writeFont) {
        this.writeFont = writeFont;
    }

    public Boolean getHidden() {
        return this.hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getLocked() {
        return this.locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getQuotePrefix() {
        return this.quotePrefix;
    }

    public void setQuotePrefix(Boolean quotePrefix) {
        this.quotePrefix = quotePrefix;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public Boolean getWrapped() {
        return this.wrapped;
    }

    public void setWrapped(Boolean wrapped) {
        this.wrapped = wrapped;
    }

    public VerticalAlignment getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public Short getRotation() {
        return this.rotation;
    }

    public void setRotation(Short rotation) {
        this.rotation = rotation;
    }

    public Short getIndent() {
        return this.indent;
    }

    public void setIndent(Short indent) {
        this.indent = indent;
    }

    public BorderStyle getBorderLeft() {
        return this.borderLeft;
    }

    public void setBorderLeft(BorderStyle borderLeft) {
        this.borderLeft = borderLeft;
    }

    public BorderStyle getBorderRight() {
        return this.borderRight;
    }

    public void setBorderRight(BorderStyle borderRight) {
        this.borderRight = borderRight;
    }

    public BorderStyle getBorderTop() {
        return this.borderTop;
    }

    public void setBorderTop(BorderStyle borderTop) {
        this.borderTop = borderTop;
    }

    public BorderStyle getBorderBottom() {
        return this.borderBottom;
    }

    public void setBorderBottom(BorderStyle borderBottom) {
        this.borderBottom = borderBottom;
    }

    public Short getLeftBorderColor() {
        return this.leftBorderColor;
    }

    public void setLeftBorderColor(Short leftBorderColor) {
        this.leftBorderColor = leftBorderColor;
    }

    public Short getRightBorderColor() {
        return this.rightBorderColor;
    }

    public void setRightBorderColor(Short rightBorderColor) {
        this.rightBorderColor = rightBorderColor;
    }

    public Short getTopBorderColor() {
        return this.topBorderColor;
    }

    public void setTopBorderColor(Short topBorderColor) {
        this.topBorderColor = topBorderColor;
    }

    public Short getBottomBorderColor() {
        return this.bottomBorderColor;
    }

    public void setBottomBorderColor(Short bottomBorderColor) {
        this.bottomBorderColor = bottomBorderColor;
    }

    public FillPatternType getFillPatternType() {
        return this.fillPatternType;
    }

    public void setFillPatternType(FillPatternType fillPatternType) {
        this.fillPatternType = fillPatternType;
    }

    public Short getFillBackgroundColor() {
        return this.fillBackgroundColor;
    }

    public void setFillBackgroundColor(Short fillBackgroundColor) {
        this.fillBackgroundColor = fillBackgroundColor;
    }

    public Short getFillForegroundColor() {
        return this.fillForegroundColor;
    }

    public void setFillForegroundColor(Short fillForegroundColor) {
        this.fillForegroundColor = fillForegroundColor;
    }

    public Boolean getShrinkToFit() {
        return this.shrinkToFit;
    }

    public void setShrinkToFit(Boolean shrinkToFit) {
        this.shrinkToFit = shrinkToFit;
    }
}
