package com.itextpdf.layout.element;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.LinkRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/Link.class */
public class Link extends Text {
    public Link(String text, PdfLinkAnnotation linkAnnotation) {
        super(text);
        setProperty(88, linkAnnotation);
    }

    public Link(String text, PdfAction action) {
        this(text, (PdfLinkAnnotation) new PdfLinkAnnotation(new Rectangle(0.0f, 0.0f, 0.0f, 0.0f)).setAction(action).setFlags(4));
    }

    public Link(String text, PdfDestination destination) {
        this(text, (PdfLinkAnnotation) new PdfLinkAnnotation(new Rectangle(0.0f, 0.0f, 0.0f, 0.0f)).setDestination(destination).setFlags(4));
    }

    public PdfLinkAnnotation getLinkAnnotation() {
        return (PdfLinkAnnotation) getProperty(88);
    }

    @Override // com.itextpdf.layout.element.Text, com.itextpdf.layout.tagging.IAccessibleElement
    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties("Link");
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.Text, com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new LinkRenderer(this, this.text);
    }
}
