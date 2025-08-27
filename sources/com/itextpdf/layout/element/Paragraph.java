package com.itextpdf.layout.element;

import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/Paragraph.class */
public class Paragraph extends BlockElement<Paragraph> {
    protected DefaultAccessibilityProperties tagProperties;

    public Paragraph() {
    }

    public Paragraph(String text) {
        this(new Text(text));
    }

    public Paragraph(Text text) {
        add(text);
    }

    public Paragraph add(String text) {
        return add(new Text(text));
    }

    public Paragraph add(ILeafElement element) {
        this.childElements.add(element);
        return this;
    }

    public Paragraph add(IBlockElement element) {
        this.childElements.add(element);
        return this;
    }

    public <T2 extends ILeafElement> Paragraph addAll(java.util.List<T2> elements) {
        for (ILeafElement element : elements) {
            add(element);
        }
        return this;
    }

    public Paragraph addTabStops(TabStop... tabStops) {
        addTabStopsAsProperty(Arrays.asList(tabStops));
        return this;
    }

    public Paragraph addTabStops(java.util.List<TabStop> tabStops) {
        addTabStopsAsProperty(tabStops);
        return this;
    }

    public Paragraph removeTabStop(float tabStopPosition) {
        Map<Float, TabStop> tabStops = (Map) getProperty(69);
        if (tabStops != null) {
            tabStops.remove(Float.valueOf(tabStopPosition));
        }
        return this;
    }

    @Override // com.itextpdf.layout.element.BlockElement, com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        switch (i) {
            case 18:
                return (T1) Float.valueOf(0.0f);
            case 33:
                return (T1) new Leading(2, (this.childElements.size() == 1 && (this.childElements.get(0) instanceof Image)) ? 1.0f : 1.35f);
            case 43:
            case 46:
                return (T1) UnitValue.createPointValue(4.0f);
            case 67:
                return (T1) Float.valueOf(50.0f);
            default:
                return (T1) super.getDefaultProperty(i);
        }
    }

    public Paragraph setFirstLineIndent(float indent) {
        setProperty(18, Float.valueOf(indent));
        return this;
    }

    public Paragraph setFixedLeading(float leading) {
        setProperty(33, new Leading(1, leading));
        return this;
    }

    public Paragraph setMultipliedLeading(float leading) {
        setProperty(33, new Leading(2, leading));
        return this;
    }

    @Override // com.itextpdf.layout.tagging.IAccessibleElement
    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties("P");
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new ParagraphRenderer(this);
    }

    private void addTabStopsAsProperty(java.util.List<TabStop> newTabStops) {
        Map<Float, TabStop> tabStops = (Map) getProperty(69);
        if (tabStops == null) {
            tabStops = new TreeMap<>();
            setProperty(69, tabStops);
        }
        for (TabStop tabStop : newTabStops) {
            tabStops.put(Float.valueOf(tabStop.getTabPosition()), tabStop);
        }
    }
}
