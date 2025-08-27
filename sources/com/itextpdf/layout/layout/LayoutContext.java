package com.itextpdf.layout.layout;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.margincollapse.MarginsCollapseInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/LayoutContext.class */
public class LayoutContext {
    protected LayoutArea area;
    protected MarginsCollapseInfo marginsCollapseInfo;
    protected List<Rectangle> floatRendererAreas;
    protected boolean clippedHeight;

    public LayoutContext(LayoutArea area) {
        this.floatRendererAreas = new ArrayList();
        this.clippedHeight = false;
        this.area = area;
    }

    public LayoutContext(LayoutArea area, MarginsCollapseInfo marginsCollapseInfo) {
        this.floatRendererAreas = new ArrayList();
        this.clippedHeight = false;
        this.area = area;
        this.marginsCollapseInfo = marginsCollapseInfo;
    }

    public LayoutContext(LayoutArea area, MarginsCollapseInfo marginsCollapseInfo, List<Rectangle> floatedRendererAreas) {
        this(area, marginsCollapseInfo);
        if (floatedRendererAreas != null) {
            this.floatRendererAreas = floatedRendererAreas;
        }
    }

    public LayoutContext(LayoutArea area, boolean clippedHeight) {
        this(area);
        this.clippedHeight = clippedHeight;
    }

    public LayoutContext(LayoutArea area, MarginsCollapseInfo marginsCollapseInfo, List<Rectangle> floatedRendererAreas, boolean clippedHeight) {
        this(area, marginsCollapseInfo);
        if (floatedRendererAreas != null) {
            this.floatRendererAreas = floatedRendererAreas;
        }
        this.clippedHeight = clippedHeight;
    }

    public LayoutArea getArea() {
        return this.area;
    }

    public MarginsCollapseInfo getMarginsCollapseInfo() {
        return this.marginsCollapseInfo;
    }

    public List<Rectangle> getFloatRendererAreas() {
        return this.floatRendererAreas;
    }

    public boolean isClippedHeight() {
        return this.clippedHeight;
    }

    public void setClippedHeight(boolean clippedHeight) {
        this.clippedHeight = clippedHeight;
    }

    public String toString() {
        return this.area.toString();
    }
}
