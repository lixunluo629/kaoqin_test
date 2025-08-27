package com.itextpdf.layout.element;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.renderer.IRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/AbstractElement.class */
public abstract class AbstractElement<T extends IElement> extends ElementPropertyContainer<T> implements IElement {
    protected IRenderer nextRenderer;
    protected java.util.List<IElement> childElements = new ArrayList();
    protected Set<Style> styles;

    protected abstract IRenderer makeNewRenderer();

    @Override // com.itextpdf.layout.element.IElement
    public IRenderer getRenderer() {
        if (this.nextRenderer != null) {
            IRenderer renderer = this.nextRenderer;
            this.nextRenderer = this.nextRenderer.getNextRenderer();
            return renderer;
        }
        return makeNewRenderer();
    }

    @Override // com.itextpdf.layout.element.IElement
    public void setNextRenderer(IRenderer renderer) {
        this.nextRenderer = renderer;
    }

    @Override // com.itextpdf.layout.element.IElement
    public IRenderer createRendererSubTree() {
        IRenderer rendererRoot = getRenderer();
        for (IElement child : this.childElements) {
            rendererRoot.addChild(child.createRendererSubTree());
        }
        return rendererRoot;
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public boolean hasProperty(int property) {
        boolean hasProperty = super.hasProperty(property);
        if (this.styles != null && this.styles.size() > 0 && !hasProperty) {
            Iterator<Style> it = this.styles.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Style style = it.next();
                if (style.hasProperty(property)) {
                    hasProperty = true;
                    break;
                }
            }
        }
        return hasProperty;
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getProperty(int i) {
        Object property = super.getProperty(i);
        if (this.styles != null && this.styles.size() > 0 && property == null && !super.hasProperty(i)) {
            for (Style style : this.styles) {
                Object property2 = style.getProperty(i);
                if (property2 != null || style.hasProperty(i)) {
                    property = property2;
                }
            }
        }
        return (T1) property;
    }

    public T addStyle(Style style) {
        if (this.styles == null) {
            this.styles = new LinkedHashSet();
        }
        this.styles.add(style);
        return this;
    }

    public java.util.List<IElement> getChildren() {
        return this.childElements;
    }

    public boolean isEmpty() {
        return 0 == this.childElements.size();
    }

    public T setAction(PdfAction action) {
        setProperty(1, action);
        return this;
    }

    public T setPageNumber(int pageNumber) {
        setProperty(51, Integer.valueOf(pageNumber));
        return this;
    }
}
