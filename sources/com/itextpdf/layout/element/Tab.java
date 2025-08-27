package com.itextpdf.layout.element;

import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TabRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/Tab.class */
public class Tab extends AbstractElement<Tab> implements ILeafElement {
    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new TabRenderer(this);
    }
}
