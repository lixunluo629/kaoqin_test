package com.itextpdf.layout.element;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.ListSymbolAlignment;
import com.itextpdf.layout.property.ListSymbolPosition;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ListRenderer;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/List.class */
public class List extends BlockElement<List> {
    public static final String DEFAULT_LIST_SYMBOL = "- ";
    protected DefaultAccessibilityProperties tagProperties;

    public List() {
    }

    public List(ListNumberingType listNumberingType) {
        setListSymbol(listNumberingType);
    }

    @Override // com.itextpdf.layout.element.BlockElement, com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        switch (i) {
            case 37:
                return (T1) new Text(DEFAULT_LIST_SYMBOL);
            case 41:
                return "";
            case 42:
                return ". ";
            case 83:
                return (T1) ListSymbolPosition.DEFAULT;
            default:
                return (T1) super.getDefaultProperty(i);
        }
    }

    public List add(ListItem listItem) {
        this.childElements.add(listItem);
        return this;
    }

    public List add(String text) {
        return add(new ListItem(text));
    }

    public List setItemStartIndex(int start) {
        setProperty(36, Integer.valueOf(start));
        return this;
    }

    public List setListSymbol(String symbol) {
        return setListSymbol(new Text(symbol));
    }

    public List setListSymbol(Text text) {
        setProperty(37, text);
        return this;
    }

    public List setListSymbol(Image image) {
        setProperty(37, image);
        return this;
    }

    public List setListSymbol(ListNumberingType listNumberingType) {
        if (listNumberingType == ListNumberingType.ZAPF_DINGBATS_1 || listNumberingType == ListNumberingType.ZAPF_DINGBATS_2 || listNumberingType == ListNumberingType.ZAPF_DINGBATS_3 || listNumberingType == ListNumberingType.ZAPF_DINGBATS_4) {
            setPostSymbolText(SymbolConstants.SPACE_SYMBOL);
        }
        setProperty(37, listNumberingType);
        return this;
    }

    public List setListSymbolAlignment(ListSymbolAlignment alignment) {
        setProperty(38, alignment);
        return this;
    }

    public Float getSymbolIndent() {
        return (Float) getProperty(39);
    }

    public List setSymbolIndent(float symbolIndent) {
        setProperty(39, Float.valueOf(symbolIndent));
        return this;
    }

    public String getPostSymbolText() {
        return (String) getProperty(42);
    }

    public void setPostSymbolText(String postSymbolText) {
        setProperty(42, postSymbolText);
    }

    public String getPreSymbolText() {
        return (String) getProperty(41);
    }

    public void setPreSymbolText(String preSymbolText) {
        setProperty(41, preSymbolText);
    }

    @Override // com.itextpdf.layout.tagging.IAccessibleElement
    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties(StandardRoles.L);
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new ListRenderer(this);
    }
}
