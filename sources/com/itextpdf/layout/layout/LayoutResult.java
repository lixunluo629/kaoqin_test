package com.itextpdf.layout.layout;

import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.renderer.IRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/LayoutResult.class */
public class LayoutResult {
    public static final int FULL = 1;
    public static final int PARTIAL = 2;
    public static final int NOTHING = 3;
    protected int status;
    protected LayoutArea occupiedArea;
    protected IRenderer splitRenderer;
    protected IRenderer overflowRenderer;
    protected AreaBreak areaBreak;
    protected IRenderer causeOfNothing;

    public LayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer) {
        this(status, occupiedArea, splitRenderer, overflowRenderer, null);
    }

    public LayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer, IRenderer cause) {
        this.status = status;
        this.occupiedArea = occupiedArea;
        this.splitRenderer = splitRenderer;
        this.overflowRenderer = overflowRenderer;
        this.causeOfNothing = cause;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LayoutArea getOccupiedArea() {
        return this.occupiedArea;
    }

    public IRenderer getSplitRenderer() {
        return this.splitRenderer;
    }

    public void setSplitRenderer(IRenderer splitRenderer) {
        this.splitRenderer = splitRenderer;
    }

    public IRenderer getOverflowRenderer() {
        return this.overflowRenderer;
    }

    public void setOverflowRenderer(IRenderer overflowRenderer) {
        this.overflowRenderer = overflowRenderer;
    }

    public AreaBreak getAreaBreak() {
        return this.areaBreak;
    }

    public LayoutResult setAreaBreak(AreaBreak areaBreak) {
        this.areaBreak = areaBreak;
        return this;
    }

    public IRenderer getCauseOfNothing() {
        return this.causeOfNothing;
    }

    public String toString() {
        String status;
        switch (getStatus()) {
            case 1:
                status = "Full";
                break;
            case 2:
                status = "Partial";
                break;
            case 3:
                status = "Nothing";
                break;
            default:
                status = "None";
                break;
        }
        return "LayoutResult{" + status + ", areaBreak=" + this.areaBreak + ", occupiedArea=" + this.occupiedArea + '}';
    }
}
