package com.itextpdf.layout;

import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/Style.class */
public class Style extends ElementPropertyContainer<Style> {
    public UnitValue getMarginLeft() {
        return (UnitValue) getProperty(44);
    }

    public Style setMarginLeft(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(44, marginUV);
        return this;
    }

    public UnitValue getMarginRight() {
        return (UnitValue) getProperty(45);
    }

    public Style setMarginRight(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(45, marginUV);
        return this;
    }

    public UnitValue getMarginTop() {
        return (UnitValue) getProperty(46);
    }

    public Style setMarginTop(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(46, marginUV);
        return this;
    }

    public UnitValue getMarginBottom() {
        return (UnitValue) getProperty(43);
    }

    public Style setMarginBottom(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(43, marginUV);
        return this;
    }

    public Style setMargin(float commonMargin) {
        return setMargins(commonMargin, commonMargin, commonMargin, commonMargin);
    }

    public Style setMargins(float marginTop, float marginRight, float marginBottom, float marginLeft) {
        setMarginTop(marginTop);
        setMarginRight(marginRight);
        setMarginBottom(marginBottom);
        setMarginLeft(marginLeft);
        return this;
    }

    public UnitValue getPaddingLeft() {
        return (UnitValue) getProperty(48);
    }

    public Style setPaddingLeft(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(48, paddingUV);
        return this;
    }

    public UnitValue getPaddingRight() {
        return (UnitValue) getProperty(49);
    }

    public Style setPaddingRight(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(49, paddingUV);
        return this;
    }

    public UnitValue getPaddingTop() {
        return (UnitValue) getProperty(50);
    }

    public Style setPaddingTop(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(50, paddingUV);
        return this;
    }

    public UnitValue getPaddingBottom() {
        return (UnitValue) getProperty(47);
    }

    public Style setPaddingBottom(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(47, paddingUV);
        return this;
    }

    public Style setPadding(float commonPadding) {
        return setPaddings(commonPadding, commonPadding, commonPadding, commonPadding);
    }

    public Style setPaddings(float paddingTop, float paddingRight, float paddingBottom, float paddingLeft) {
        setPaddingTop(paddingTop);
        setPaddingRight(paddingRight);
        setPaddingBottom(paddingBottom);
        setPaddingLeft(paddingLeft);
        return this;
    }

    public Style setVerticalAlignment(VerticalAlignment verticalAlignment) {
        setProperty(75, verticalAlignment);
        return this;
    }

    public Style setSpacingRatio(float ratio) {
        setProperty(61, Float.valueOf(ratio));
        return this;
    }

    public Boolean isKeepTogether() {
        return (Boolean) getProperty(32);
    }

    public Style setKeepTogether(boolean keepTogether) {
        setProperty(32, Boolean.valueOf(keepTogether));
        return this;
    }

    public Style setRotationAngle(float radAngle) {
        setProperty(55, Float.valueOf(radAngle));
        return this;
    }

    public Style setRotationAngle(double angle) {
        setProperty(55, Float.valueOf((float) angle));
        return this;
    }

    public Style setWidth(float width) {
        setProperty(77, UnitValue.createPointValue(width));
        return this;
    }

    public Style setWidth(UnitValue width) {
        setProperty(77, width);
        return this;
    }

    public UnitValue getWidth() {
        return (UnitValue) getProperty(77);
    }

    public Style setHeight(UnitValue height) {
        setProperty(27, height);
        return this;
    }

    public Style setHeight(float height) {
        UnitValue heightAsUV = UnitValue.createPointValue(height);
        setProperty(27, heightAsUV);
        return this;
    }

    public UnitValue getHeight() {
        return (UnitValue) getProperty(27);
    }

    public Style setMaxHeight(float maxHeight) {
        UnitValue maxHeightAsUV = UnitValue.createPointValue(maxHeight);
        setProperty(84, maxHeightAsUV);
        return this;
    }

    public Style setMaxHeight(UnitValue maxHeight) {
        setProperty(84, maxHeight);
        return this;
    }

    public Style setMinHeight(UnitValue minHeight) {
        setProperty(85, minHeight);
        return this;
    }

    public Style setMinHeight(float minHeight) {
        UnitValue minHeightAsUV = UnitValue.createPointValue(minHeight);
        setProperty(85, minHeightAsUV);
        return this;
    }

    public Style setMaxWidth(UnitValue maxWidth) {
        setProperty(79, maxWidth);
        return this;
    }

    public Style setMaxWidth(float maxWidth) {
        setProperty(79, UnitValue.createPointValue(maxWidth));
        return this;
    }

    public Style setMinWidth(UnitValue minWidth) {
        setProperty(80, minWidth);
        return this;
    }

    public Style setMinWidth(float minWidth) {
        setProperty(80, UnitValue.createPointValue(minWidth));
        return this;
    }
}
