package com.itextpdf.layout.element;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.layout.tagging.IAccessibleElement;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/Text.class */
public class Text extends AbstractElement<Text> implements ILeafElement, IAccessibleElement {
    protected String text;
    protected DefaultAccessibilityProperties tagProperties;

    public Text(String text) {
        if (null == text) {
            throw new IllegalArgumentException();
        }
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextRise() {
        return ((Float) getProperty(72)).floatValue();
    }

    public Text setTextRise(float textRise) {
        setProperty(72, Float.valueOf(textRise));
        return this;
    }

    public Float getHorizontalScaling() {
        return (Float) getProperty(29);
    }

    public Text setSkew(float alpha, float beta) {
        setProperty(65, new float[]{(float) Math.tan((alpha * 3.141592653589793d) / 180.0d), (float) Math.tan((beta * 3.141592653589793d) / 180.0d)});
        return this;
    }

    public Text setHorizontalScaling(float horizontalScaling) {
        setProperty(29, Float.valueOf(horizontalScaling));
        return this;
    }

    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties(StandardRoles.SPAN);
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new TextRenderer(this, this.text);
    }
}
