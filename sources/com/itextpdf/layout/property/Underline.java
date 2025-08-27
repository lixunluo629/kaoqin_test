package com.itextpdf.layout.property;

import com.itextpdf.kernel.colors.Color;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/Underline.class */
public class Underline {
    protected TransparentColor transparentColor;
    protected float thickness;
    protected float thicknessMul;
    protected float yPosition;
    protected float yPositionMul;
    protected int lineCapStyle;

    public Underline(Color color, float thickness, float thicknessMul, float yPosition, float yPositionMul, int lineCapStyle) {
        this(color, 1.0f, thickness, thicknessMul, yPosition, yPositionMul, lineCapStyle);
    }

    public Underline(Color color, float opacity, float thickness, float thicknessMul, float yPosition, float yPositionMul, int lineCapStyle) {
        this.lineCapStyle = 0;
        this.transparentColor = new TransparentColor(color, opacity);
        this.thickness = thickness;
        this.thicknessMul = thicknessMul;
        this.yPosition = yPosition;
        this.yPositionMul = yPositionMul;
        this.lineCapStyle = lineCapStyle;
    }

    public Color getColor() {
        return this.transparentColor.getColor();
    }

    public float getOpacity() {
        return this.transparentColor.getOpacity();
    }

    public float getThickness(float fontSize) {
        return this.thickness + (this.thicknessMul * fontSize);
    }

    public float getYPosition(float fontSize) {
        return this.yPosition + (this.yPositionMul * fontSize);
    }

    public float getYPositionMul() {
        return this.yPositionMul;
    }

    public int getLineCapStyle() {
        return this.lineCapStyle;
    }
}
