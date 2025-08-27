package com.itextpdf.layout.renderer;

import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/IRenderer.class */
public interface IRenderer extends IPropertyContainer {
    void addChild(IRenderer iRenderer);

    LayoutResult layout(LayoutContext layoutContext);

    void draw(DrawContext drawContext);

    LayoutArea getOccupiedArea();

    <T1> T1 getProperty(int i, T1 t1);

    IRenderer setParent(IRenderer iRenderer);

    IRenderer getParent();

    IPropertyContainer getModelElement();

    List<IRenderer> getChildRenderers();

    boolean isFlushed();

    void move(float f, float f2);

    IRenderer getNextRenderer();
}
