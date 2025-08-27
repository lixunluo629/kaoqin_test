package com.itextpdf.layout.renderer;

import com.itextpdf.layout.minmaxwidth.MinMaxWidth;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/AbstractWidthHandler.class */
public abstract class AbstractWidthHandler {
    MinMaxWidth minMaxWidth;

    public abstract void updateMinChildWidth(float f);

    public abstract void updateMaxChildWidth(float f);

    public AbstractWidthHandler(MinMaxWidth minMaxWidth) {
        this.minMaxWidth = minMaxWidth;
    }

    public void updateMinMaxWidth(MinMaxWidth minMaxWidth) {
        if (minMaxWidth != null) {
            updateMaxChildWidth(minMaxWidth.getMaxWidth());
            updateMinChildWidth(minMaxWidth.getMinWidth());
        }
    }
}
