package com.itextpdf.layout.element;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.ListSymbolPosition;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ListItemRenderer;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/ListItem.class */
public class ListItem extends Div {
    public ListItem() {
    }

    public ListItem(String text) {
        this();
        add(new Paragraph(text).setMarginTop(0.0f).setMarginBottom(0.0f));
    }

    public ListItem setListSymbolOrdinalValue(int ordinalValue) {
        setProperty(120, Integer.valueOf(ordinalValue));
        return this;
    }

    public ListItem(Image image) {
        this();
        add(image);
    }

    @Override // com.itextpdf.layout.element.BlockElement, com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        switch (i) {
            case 83:
                return (T1) ListSymbolPosition.DEFAULT;
            default:
                return (T1) super.getDefaultProperty(i);
        }
    }

    public ListItem setListSymbol(String symbol) {
        return setListSymbol(new Text(symbol));
    }

    public ListItem setListSymbol(Text text) {
        setProperty(37, text);
        return this;
    }

    public ListItem setListSymbol(Image image) {
        setProperty(37, image);
        return this;
    }

    public ListItem setListSymbol(ListNumberingType listNumberingType) {
        if (listNumberingType == ListNumberingType.ZAPF_DINGBATS_1 || listNumberingType == ListNumberingType.ZAPF_DINGBATS_2 || listNumberingType == ListNumberingType.ZAPF_DINGBATS_3 || listNumberingType == ListNumberingType.ZAPF_DINGBATS_4) {
            setProperty(42, SymbolConstants.SPACE_SYMBOL);
        }
        setProperty(37, listNumberingType);
        return this;
    }

    @Override // com.itextpdf.layout.element.Div, com.itextpdf.layout.tagging.IAccessibleElement
    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties(StandardRoles.LBODY);
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.Div, com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new ListItemRenderer(this);
    }
}
