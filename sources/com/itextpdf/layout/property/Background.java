package com.itextpdf.layout.property;

import com.itextpdf.kernel.colors.Color;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/Background.class */
public class Background {
    protected TransparentColor transparentColor;
    protected float extraLeft;
    protected float extraRight;
    protected float extraTop;
    protected float extraBottom;

    public Background(Color color) {
        this(color, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Background(Color color, float opacity) {
        this(color, opacity, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Background(Color color, float extraLeft, float extraTop, float extraRight, float extraBottom) {
        this(color, 1.0f, extraLeft, extraTop, extraRight, extraBottom);
    }

    public Background(Color color, float opacity, float extraLeft, float extraTop, float extraRight, float extraBottom) {
        this.transparentColor = new TransparentColor(color, opacity);
        this.extraLeft = extraLeft;
        this.extraRight = extraRight;
        this.extraTop = extraTop;
        this.extraBottom = extraBottom;
    }

    public Color getColor() {
        return this.transparentColor.getColor();
    }

    public float getOpacity() {
        return this.transparentColor.getOpacity();
    }

    public float getExtraLeft() {
        return this.extraLeft;
    }

    public float getExtraRight() {
        return this.extraRight;
    }

    public float getExtraTop() {
        return this.extraTop;
    }

    public float getExtraBottom() {
        return this.extraBottom;
    }
}
