package com.itextpdf.layout.layout;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.margincollapse.MarginsCollapseInfo;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/LineLayoutContext.class */
public class LineLayoutContext extends LayoutContext {
    private boolean floatOverflowedToNextPageWithNothing;
    private float textIndent;

    public LineLayoutContext(LayoutArea area, MarginsCollapseInfo marginsCollapseInfo, List<Rectangle> floatedRendererAreas, boolean clippedHeight) {
        super(area, marginsCollapseInfo, floatedRendererAreas, clippedHeight);
        this.floatOverflowedToNextPageWithNothing = false;
    }

    public LineLayoutContext(LayoutContext layoutContext) {
        super(layoutContext.area, layoutContext.marginsCollapseInfo, layoutContext.floatRendererAreas, layoutContext.clippedHeight);
        this.floatOverflowedToNextPageWithNothing = false;
    }

    public boolean isFloatOverflowedToNextPageWithNothing() {
        return this.floatOverflowedToNextPageWithNothing;
    }

    public LineLayoutContext setFloatOverflowedToNextPageWithNothing(boolean floatOverflowedToNextPageWithNothing) {
        this.floatOverflowedToNextPageWithNothing = floatOverflowedToNextPageWithNothing;
        return this;
    }

    public float getTextIndent() {
        return this.textIndent;
    }

    public LineLayoutContext setTextIndent(float textIndent) {
        this.textIndent = textIndent;
        return this;
    }
}
