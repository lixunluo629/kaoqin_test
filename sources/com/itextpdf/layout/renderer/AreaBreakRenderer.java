package com.itextpdf.layout.renderer;

import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/AreaBreakRenderer.class */
public class AreaBreakRenderer implements IRenderer {
    protected AreaBreak areaBreak;

    public AreaBreakRenderer(AreaBreak areaBreak) {
        this.areaBreak = areaBreak;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public void addChild(IRenderer renderer) {
        throw new RuntimeException();
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        return new LayoutResult(3, null, null, null, this).setAreaBreak(this.areaBreak);
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        throw new UnsupportedOperationException();
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutArea getOccupiedArea() {
        throw new UnsupportedOperationException();
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public boolean hasProperty(int property) {
        return false;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public boolean hasOwnProperty(int property) {
        return false;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getProperty(int key) {
        return null;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getOwnProperty(int property) {
        return null;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int property) {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public <T1> T1 getProperty(int property, T1 defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public void setProperty(int property, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public void deleteOwnProperty(int property) {
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer setParent(IRenderer parent) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IPropertyContainer getModelElement() {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getParent() {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public List<IRenderer> getChildRenderers() {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public boolean isFlushed() {
        return false;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public void move(float dx, float dy) {
        throw new UnsupportedOperationException();
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return null;
    }
}
