package com.itextpdf.layout.layout;

import com.itextpdf.layout.renderer.IRenderer;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/LineLayoutResult.class */
public class LineLayoutResult extends MinMaxWidthLayoutResult {
    protected boolean splitForcedByNewline;
    private List<IRenderer> floatsOverflowedToNextPage;

    public LineLayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer) {
        super(status, occupiedArea, splitRenderer, overflowRenderer);
    }

    public LineLayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer, IRenderer cause) {
        super(status, occupiedArea, splitRenderer, overflowRenderer, cause);
    }

    public boolean isSplitForcedByNewline() {
        return this.splitForcedByNewline;
    }

    public LineLayoutResult setSplitForcedByNewline(boolean isSplitForcedByNewline) {
        this.splitForcedByNewline = isSplitForcedByNewline;
        return this;
    }

    public List<IRenderer> getFloatsOverflowedToNextPage() {
        return this.floatsOverflowedToNextPage;
    }

    public void setFloatsOverflowedToNextPage(List<IRenderer> floatsOverflowedToNextPage) {
        this.floatsOverflowedToNextPage = floatsOverflowedToNextPage;
    }
}
