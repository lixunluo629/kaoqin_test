package com.itextpdf.layout.property;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/BorderRadius.class */
public class BorderRadius {
    private UnitValue horizontalRadius;
    private UnitValue verticalRadius;

    public BorderRadius(UnitValue radius) {
        this.horizontalRadius = radius;
        this.verticalRadius = radius;
    }

    public BorderRadius(float radius) {
        this.horizontalRadius = UnitValue.createPointValue(radius);
        this.verticalRadius = this.horizontalRadius;
    }

    public BorderRadius(UnitValue horizontalRadius, UnitValue verticalRadius) {
        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
    }

    public BorderRadius(float horizontalRadius, float verticalRadius) {
        this.horizontalRadius = UnitValue.createPointValue(horizontalRadius);
        this.verticalRadius = UnitValue.createPointValue(verticalRadius);
    }

    public UnitValue getHorizontalRadius() {
        return this.horizontalRadius;
    }

    public UnitValue getVerticalRadius() {
        return this.verticalRadius;
    }
}
