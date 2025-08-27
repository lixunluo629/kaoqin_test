package com.itextpdf.layout.element;

import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.renderer.IRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/IElement.class */
public interface IElement extends IPropertyContainer {
    void setNextRenderer(IRenderer iRenderer);

    IRenderer getRenderer();

    IRenderer createRendererSubTree();
}
