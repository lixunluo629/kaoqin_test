package com.itextpdf.layout.renderer;

import com.itextpdf.layout.element.Div;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/DivRenderer.class */
public class DivRenderer extends BlockRenderer {
    public DivRenderer(Div modelElement) {
        super(modelElement);
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new DivRenderer((Div) this.modelElement);
    }
}
