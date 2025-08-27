package com.itextpdf.layout;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.FontKerning;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.RootRenderer;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;
import com.itextpdf.layout.splitting.ISplitCharacters;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/RootElement.class */
public abstract class RootElement<T extends IPropertyContainer> extends ElementPropertyContainer<T> implements Closeable {
    protected PdfDocument pdfDocument;
    protected PdfFont defaultFont;
    protected FontProvider defaultFontProvider;
    protected ISplitCharacters defaultSplitCharacters;
    protected RootRenderer rootRenderer;
    private LayoutTaggingHelper defaultLayoutTaggingHelper;
    protected boolean immediateFlush = true;
    protected List<IElement> childElements = new ArrayList();

    protected abstract RootRenderer ensureRootRendererNotNull();

    public T add(IBlockElement element) {
        this.childElements.add(element);
        createAndAddRendererSubTree(element);
        if (this.immediateFlush) {
            this.childElements.remove(this.childElements.size() - 1);
        }
        return this;
    }

    public T add(Image image) {
        this.childElements.add(image);
        createAndAddRendererSubTree(image);
        if (this.immediateFlush) {
            this.childElements.remove(this.childElements.size() - 1);
        }
        return this;
    }

    public FontProvider getFontProvider() {
        Object fontProvider = getProperty(91);
        if (fontProvider instanceof FontProvider) {
            return (FontProvider) fontProvider;
        }
        return null;
    }

    public void setFontProvider(FontProvider fontProvider) {
        setProperty(91, fontProvider);
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public boolean hasProperty(int property) {
        return hasOwnProperty(property);
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public boolean hasOwnProperty(int property) {
        return this.properties.containsKey(Integer.valueOf(property));
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getProperty(int i) {
        return (T1) getOwnProperty(i);
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getOwnProperty(int i) {
        return (T1) this.properties.get(Integer.valueOf(i));
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        try {
            switch (i) {
                case 20:
                    if (this.defaultFont == null) {
                        this.defaultFont = PdfFontFactory.createFont();
                    }
                    return (T1) this.defaultFont;
                case 24:
                    return (T1) UnitValue.createPointValue(12.0f);
                case 61:
                    return (T1) Float.valueOf(0.75f);
                case 62:
                    if (this.defaultSplitCharacters == null) {
                        this.defaultSplitCharacters = new DefaultSplitCharacters();
                    }
                    return (T1) this.defaultSplitCharacters;
                case 71:
                    return (T1) 0;
                case 72:
                    return (T1) Float.valueOf(0.0f);
                case 91:
                    if (this.defaultFontProvider == null) {
                        this.defaultFontProvider = new FontProvider();
                    }
                    return (T1) this.defaultFontProvider;
                case 108:
                    return (T1) initTaggingHelperIfNeeded();
                default:
                    return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public void deleteOwnProperty(int property) {
        this.properties.remove(Integer.valueOf(property));
    }

    @Override // com.itextpdf.layout.ElementPropertyContainer, com.itextpdf.layout.IPropertyContainer
    public void setProperty(int property, Object value) {
        this.properties.put(Integer.valueOf(property), value);
    }

    public RootRenderer getRenderer() {
        return ensureRootRendererNotNull();
    }

    public T showTextAligned(String str, float f, float f2, TextAlignment textAlignment) {
        return (T) showTextAligned(str, f, f2, textAlignment, 0.0f);
    }

    public T showTextAligned(String str, float f, float f2, TextAlignment textAlignment, float f3) {
        return (T) showTextAligned(str, f, f2, textAlignment, VerticalAlignment.BOTTOM, f3);
    }

    public T showTextAligned(String str, float f, float f2, TextAlignment textAlignment, VerticalAlignment verticalAlignment, float f3) {
        return (T) showTextAligned(new Paragraph(str).setMultipliedLeading(1.0f).setMargin(0.0f), f, f2, this.pdfDocument.getNumberOfPages(), textAlignment, verticalAlignment, f3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T showTextAlignedKerned(String str, float f, float f2, TextAlignment textAlignment, VerticalAlignment verticalAlignment, float f3) {
        return (T) showTextAligned((Paragraph) new Paragraph(str).setMultipliedLeading(1.0f).setMargin(0.0f).setFontKerning(FontKerning.YES), f, f2, this.pdfDocument.getNumberOfPages(), textAlignment, verticalAlignment, f3);
    }

    public T showTextAligned(Paragraph paragraph, float f, float f2, TextAlignment textAlignment) {
        return (T) showTextAligned(paragraph, f, f2, this.pdfDocument.getNumberOfPages(), textAlignment, VerticalAlignment.BOTTOM, 0.0f);
    }

    public T showTextAligned(Paragraph paragraph, float f, float f2, TextAlignment textAlignment, VerticalAlignment verticalAlignment) {
        return (T) showTextAligned(paragraph, f, f2, this.pdfDocument.getNumberOfPages(), textAlignment, verticalAlignment, 0.0f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T showTextAligned(Paragraph p, float x, float y, int pageNumber, TextAlignment textAlign, VerticalAlignment vertAlign, float radAngle) {
        Div div = new Div();
        ((Div) div.setTextAlignment(textAlign)).setVerticalAlignment(vertAlign);
        if (radAngle != 0.0f) {
            div.setRotationAngle(radAngle);
        }
        div.setProperty(58, Float.valueOf(x));
        div.setProperty(59, Float.valueOf(y));
        float divX = x;
        float divY = y;
        if (textAlign == TextAlignment.CENTER) {
            divX = x - (5000.0f / 2.0f);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
        } else if (textAlign == TextAlignment.RIGHT) {
            divX = x - 5000.0f;
            p.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        }
        if (vertAlign == VerticalAlignment.MIDDLE) {
            divY = y - (5000.0f / 2.0f);
        } else if (vertAlign == VerticalAlignment.TOP) {
            divY = y - 5000.0f;
        }
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        ((Div) div.setFixedPosition(pageNumber, divX, divY, 5000.0f)).setMinHeight(5000.0f);
        if (p.getProperty(33) == null) {
            p.setMultipliedLeading(1.0f);
        }
        div.add(p.setMargins(0.0f, 0.0f, 0.0f, 0.0f));
        div.getAccessibilityProperties().setRole(StandardRoles.ARTIFACT);
        add(div);
        return this;
    }

    protected void createAndAddRendererSubTree(IElement element) {
        IRenderer rendererSubTreeRoot = element.createRendererSubTree();
        LayoutTaggingHelper taggingHelper = initTaggingHelperIfNeeded();
        if (taggingHelper != null) {
            taggingHelper.addKidsHint(this.pdfDocument.getTagStructureContext().getAutoTaggingPointer(), Collections.singletonList(rendererSubTreeRoot));
        }
        ensureRootRendererNotNull().addChild(rendererSubTreeRoot);
    }

    private LayoutTaggingHelper initTaggingHelperIfNeeded() {
        if (this.defaultLayoutTaggingHelper != null || !this.pdfDocument.isTagged()) {
            return this.defaultLayoutTaggingHelper;
        }
        LayoutTaggingHelper layoutTaggingHelper = new LayoutTaggingHelper(this.pdfDocument, this.immediateFlush);
        this.defaultLayoutTaggingHelper = layoutTaggingHelper;
        return layoutTaggingHelper;
    }
}
