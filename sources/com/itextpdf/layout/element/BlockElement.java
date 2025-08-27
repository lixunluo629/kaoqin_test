package com.itextpdf.layout.element;

import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.tagging.IAccessibleElement;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/BlockElement.class */
public abstract class BlockElement<T extends IElement> extends AbstractElement<T> implements IAccessibleElement, IBlockElement {
    protected BlockElement() {
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        switch (i) {
            case 102:
            case 103:
            case 104:
                return (T1) OverflowPropertyValue.FIT;
            default:
                return (T1) super.getDefaultProperty(i);
        }
    }

    public UnitValue getMarginLeft() {
        return (UnitValue) getProperty(44);
    }

    public T setMarginLeft(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(44, marginUV);
        return this;
    }

    public UnitValue getMarginRight() {
        return (UnitValue) getProperty(45);
    }

    public T setMarginRight(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(45, marginUV);
        return this;
    }

    public UnitValue getMarginTop() {
        return (UnitValue) getProperty(46);
    }

    public T setMarginTop(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(46, marginUV);
        return this;
    }

    public UnitValue getMarginBottom() {
        return (UnitValue) getProperty(43);
    }

    public T setMarginBottom(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(43, marginUV);
        return this;
    }

    public T setMargin(float f) {
        return (T) setMargins(f, f, f, f);
    }

    public T setMargins(float marginTop, float marginRight, float marginBottom, float marginLeft) {
        setMarginTop(marginTop);
        setMarginRight(marginRight);
        setMarginBottom(marginBottom);
        setMarginLeft(marginLeft);
        return this;
    }

    public UnitValue getPaddingLeft() {
        return (UnitValue) getProperty(48);
    }

    public T setPaddingLeft(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(48, paddingUV);
        return this;
    }

    public UnitValue getPaddingRight() {
        return (UnitValue) getProperty(49);
    }

    public T setPaddingRight(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(49, paddingUV);
        return this;
    }

    public UnitValue getPaddingTop() {
        return (UnitValue) getProperty(50);
    }

    public T setPaddingTop(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(50, paddingUV);
        return this;
    }

    public UnitValue getPaddingBottom() {
        return (UnitValue) getProperty(47);
    }

    public T setPaddingBottom(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(47, paddingUV);
        return this;
    }

    public T setPadding(float f) {
        return (T) setPaddings(f, f, f, f);
    }

    public T setPaddings(float paddingTop, float paddingRight, float paddingBottom, float paddingLeft) {
        setPaddingTop(paddingTop);
        setPaddingRight(paddingRight);
        setPaddingBottom(paddingBottom);
        setPaddingLeft(paddingLeft);
        return this;
    }

    public T setVerticalAlignment(VerticalAlignment verticalAlignment) {
        setProperty(75, verticalAlignment);
        return this;
    }

    public T setSpacingRatio(float ratio) {
        setProperty(61, Float.valueOf(ratio));
        return this;
    }

    public Boolean isKeepTogether() {
        return (Boolean) getProperty(32);
    }

    public T setKeepTogether(boolean keepTogether) {
        setProperty(32, Boolean.valueOf(keepTogether));
        return this;
    }

    public Boolean isKeepWithNext() {
        return (Boolean) getProperty(81);
    }

    public T setKeepWithNext(boolean keepWithNext) {
        setProperty(81, Boolean.valueOf(keepWithNext));
        return this;
    }

    public T setRotationAngle(float angleInRadians) {
        setProperty(55, Float.valueOf(angleInRadians));
        return this;
    }

    public T setRotationAngle(double angleInRadians) {
        setProperty(55, Float.valueOf((float) angleInRadians));
        return this;
    }

    public T setWidth(float width) {
        setProperty(77, UnitValue.createPointValue(width));
        return this;
    }

    public T setWidth(UnitValue width) {
        setProperty(77, width);
        return this;
    }

    public UnitValue getWidth() {
        return (UnitValue) getProperty(77);
    }

    public T setHeight(UnitValue height) {
        setProperty(27, height);
        return this;
    }

    public T setHeight(float height) {
        UnitValue heightAsUV = UnitValue.createPointValue(height);
        setProperty(27, heightAsUV);
        return this;
    }

    public UnitValue getHeight() {
        return (UnitValue) getProperty(27);
    }

    public T setMaxHeight(float maxHeight) {
        UnitValue maxHeightAsUV = UnitValue.createPointValue(maxHeight);
        setProperty(84, maxHeightAsUV);
        return this;
    }

    public T setMaxHeight(UnitValue maxHeight) {
        setProperty(84, maxHeight);
        return this;
    }

    public T setMinHeight(UnitValue minHeight) {
        setProperty(85, minHeight);
        return this;
    }

    public T setMinHeight(float minHeight) {
        UnitValue minHeightAsUV = UnitValue.createPointValue(minHeight);
        setProperty(85, minHeightAsUV);
        return this;
    }

    public T setMaxWidth(UnitValue maxWidth) {
        setProperty(79, maxWidth);
        return this;
    }

    public T setMaxWidth(float maxWidth) {
        setProperty(79, UnitValue.createPointValue(maxWidth));
        return this;
    }

    public T setMinWidth(UnitValue minWidth) {
        setProperty(80, minWidth);
        return this;
    }

    public T setMinWidth(float minWidth) {
        setProperty(80, UnitValue.createPointValue(minWidth));
        return this;
    }
}
