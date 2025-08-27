package com.itextpdf.layout.layout;

import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.renderer.IRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/MinMaxWidthLayoutResult.class */
public class MinMaxWidthLayoutResult extends LayoutResult {
    protected MinMaxWidth minMaxWidth;

    public MinMaxWidthLayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer) {
        super(status, occupiedArea, splitRenderer, overflowRenderer);
        this.minMaxWidth = new MinMaxWidth();
    }

    public MinMaxWidthLayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer, IRenderer cause) {
        super(status, occupiedArea, splitRenderer, overflowRenderer, cause);
        this.minMaxWidth = new MinMaxWidth();
    }

    public MinMaxWidth getMinMaxWidth() {
        return this.minMaxWidth;
    }

    public MinMaxWidthLayoutResult setMinMaxWidth(MinMaxWidth minMaxWidth) {
        this.minMaxWidth = minMaxWidth;
        return this;
    }
}
