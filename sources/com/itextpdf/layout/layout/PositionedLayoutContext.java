package com.itextpdf.layout.layout;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/PositionedLayoutContext.class */
public class PositionedLayoutContext extends LayoutContext {
    private LayoutArea parentOccupiedArea;

    public PositionedLayoutContext(LayoutArea area, LayoutArea parentOccupiedArea) {
        super(area);
        this.parentOccupiedArea = parentOccupiedArea;
    }

    public LayoutArea getParentOccupiedArea() {
        return this.parentOccupiedArea;
    }
}
