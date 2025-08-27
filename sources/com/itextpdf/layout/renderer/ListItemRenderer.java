package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.ListSymbolAlignment;
import com.itextpdf.layout.property.ListSymbolPosition;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import com.itextpdf.layout.tagging.TaggingDummyElement;
import com.itextpdf.layout.tagging.TaggingHintKey;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/ListItemRenderer.class */
public class ListItemRenderer extends DivRenderer {
    protected IRenderer symbolRenderer;
    protected float symbolAreaWidth;
    private boolean symbolAddedInside;

    public ListItemRenderer(ListItem modelElement) {
        super(modelElement);
    }

    public void addSymbolRenderer(IRenderer symbolRenderer, float symbolAreaWidth) {
        this.symbolRenderer = symbolRenderer;
        this.symbolAreaWidth = symbolAreaWidth;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        if (this.symbolRenderer != null && getProperty(27) == null && !isListSymbolEmpty(this.symbolRenderer)) {
            float[] ascenderDescender = calculateAscenderDescender();
            float minHeight = Math.max(this.symbolRenderer.getOccupiedArea().getBBox().getHeight(), ascenderDescender[0] - ascenderDescender[1]);
            updateMinHeight(UnitValue.createPointValue(minHeight));
        }
        applyListSymbolPosition();
        LayoutResult result = super.layout(layoutContext);
        if (2 == result.getStatus()) {
            result.getOverflowRenderer().deleteOwnProperty(85);
        }
        return result;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        LayoutTaggingHelper taggingHelper;
        if (this.occupiedArea == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) ListItemRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Drawing won't be performed."));
            return;
        }
        if (drawContext.isTaggingEnabled() && (taggingHelper = (LayoutTaggingHelper) getProperty(108)) != null) {
            if (this.symbolRenderer != null) {
                LayoutTaggingHelper.addTreeHints(taggingHelper, this.symbolRenderer);
            }
            if (taggingHelper.isArtifact(this)) {
                taggingHelper.markArtifactHint(this.symbolRenderer);
            } else {
                TaggingHintKey hintKey = LayoutTaggingHelper.getHintKey(this);
                TaggingHintKey parentHint = taggingHelper.getAccessibleParentHint(hintKey);
                if (parentHint != null && !StandardRoles.LI.equals(parentHint.getAccessibleElement().getAccessibilityProperties().getRole())) {
                    TaggingDummyElement listItemIntermediate = new TaggingDummyElement(StandardRoles.LI);
                    List<TaggingHintKey> intermediateKid = Collections.singletonList(LayoutTaggingHelper.getOrCreateHintKey(listItemIntermediate));
                    taggingHelper.replaceKidHint(hintKey, intermediateKid);
                    if (this.symbolRenderer != null) {
                        taggingHelper.addKidsHint(listItemIntermediate, Collections.singletonList(this.symbolRenderer));
                    }
                    taggingHelper.addKidsHint(listItemIntermediate, Collections.singletonList(this));
                }
            }
        }
        super.draw(drawContext);
        if (this.symbolRenderer != null && !this.symbolAddedInside) {
            boolean isRtl = BaseDirection.RIGHT_TO_LEFT.equals(getProperty(7));
            this.symbolRenderer.setParent(this);
            float x = isRtl ? this.occupiedArea.getBBox().getRight() : this.occupiedArea.getBBox().getLeft();
            ListSymbolPosition symbolPosition = (ListSymbolPosition) ListRenderer.getListItemOrListProperty(this, this.parent, 83);
            if (symbolPosition != ListSymbolPosition.DEFAULT) {
                Float symbolIndent = getPropertyAsFloat(39);
                if (isRtl) {
                    x += this.symbolAreaWidth + (symbolIndent == null ? 0.0f : symbolIndent.floatValue());
                } else {
                    x -= this.symbolAreaWidth + (symbolIndent == null ? 0.0f : symbolIndent.floatValue());
                }
                if (symbolPosition == ListSymbolPosition.OUTSIDE) {
                    if (isRtl) {
                        UnitValue marginRightUV = getPropertyAsUnitValue(45);
                        if (!marginRightUV.isPointValue()) {
                            Logger logger2 = LoggerFactory.getLogger((Class<?>) ListItemRenderer.class);
                            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 45));
                        }
                        x -= marginRightUV.getValue();
                    } else {
                        UnitValue marginLeftUV = getPropertyAsUnitValue(44);
                        if (!marginLeftUV.isPointValue()) {
                            Logger logger3 = LoggerFactory.getLogger((Class<?>) ListItemRenderer.class);
                            logger3.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
                        }
                        x += marginLeftUV.getValue();
                    }
                }
            }
            applyMargins(this.occupiedArea.getBBox(), false);
            applyBorderBox(this.occupiedArea.getBBox(), false);
            if (this.childRenderers.size() > 0) {
                Float yLine = null;
                for (int i = 0; i < this.childRenderers.size(); i++) {
                    if (this.childRenderers.get(i).getOccupiedArea().getBBox().getHeight() > 0.0f) {
                        yLine = ((AbstractRenderer) this.childRenderers.get(i)).getFirstYLineRecursively();
                        if (yLine != null) {
                            break;
                        }
                    }
                }
                if (yLine != null) {
                    if (this.symbolRenderer instanceof LineRenderer) {
                        this.symbolRenderer.move(0.0f, yLine.floatValue() - ((LineRenderer) this.symbolRenderer).getYLine());
                    } else {
                        this.symbolRenderer.move(0.0f, yLine.floatValue() - this.symbolRenderer.getOccupiedArea().getBBox().getY());
                    }
                } else {
                    this.symbolRenderer.move(0.0f, (this.occupiedArea.getBBox().getY() + this.occupiedArea.getBBox().getHeight()) - (this.symbolRenderer.getOccupiedArea().getBBox().getY() + this.symbolRenderer.getOccupiedArea().getBBox().getHeight()));
                }
            } else if (this.symbolRenderer instanceof TextRenderer) {
                ((TextRenderer) this.symbolRenderer).moveYLineTo((this.occupiedArea.getBBox().getY() + this.occupiedArea.getBBox().getHeight()) - calculateAscenderDescender()[0]);
            } else {
                this.symbolRenderer.move(0.0f, ((this.occupiedArea.getBBox().getY() + this.occupiedArea.getBBox().getHeight()) - this.symbolRenderer.getOccupiedArea().getBBox().getHeight()) - this.symbolRenderer.getOccupiedArea().getBBox().getY());
            }
            applyBorderBox(this.occupiedArea.getBBox(), true);
            applyMargins(this.occupiedArea.getBBox(), true);
            ListSymbolAlignment listSymbolAlignment = (ListSymbolAlignment) this.parent.getProperty(38, isRtl ? ListSymbolAlignment.LEFT : ListSymbolAlignment.RIGHT);
            float dxPosition = x - this.symbolRenderer.getOccupiedArea().getBBox().getX();
            if (listSymbolAlignment == ListSymbolAlignment.RIGHT) {
                if (!isRtl) {
                    dxPosition += this.symbolAreaWidth - this.symbolRenderer.getOccupiedArea().getBBox().getWidth();
                }
            } else if (listSymbolAlignment == ListSymbolAlignment.LEFT && isRtl) {
                dxPosition -= this.symbolAreaWidth - this.symbolRenderer.getOccupiedArea().getBBox().getWidth();
            }
            if ((this.symbolRenderer instanceof LineRenderer) && isRtl) {
                this.symbolRenderer.move(dxPosition - this.symbolRenderer.getOccupiedArea().getBBox().getWidth(), 0.0f);
            } else {
                this.symbolRenderer.move(dxPosition, 0.0f);
            }
            if (this.symbolRenderer.getOccupiedArea().getBBox().getRight() > this.parent.getOccupiedArea().getBBox().getLeft()) {
                beginElementOpacityApplying(drawContext);
                this.symbolRenderer.draw(drawContext);
                endElementOpacityApplying(drawContext);
            }
        }
    }

    @Override // com.itextpdf.layout.renderer.DivRenderer, com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new ListItemRenderer((ListItem) this.modelElement);
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createSplitRenderer(int layoutResult) {
        ListItemRenderer splitRenderer = (ListItemRenderer) getNextRenderer();
        splitRenderer.parent = this.parent;
        splitRenderer.modelElement = this.modelElement;
        splitRenderer.occupiedArea = this.occupiedArea;
        splitRenderer.isLastRendererForModelElement = false;
        if (layoutResult == 2) {
            splitRenderer.symbolRenderer = this.symbolRenderer;
            splitRenderer.symbolAreaWidth = this.symbolAreaWidth;
        }
        splitRenderer.addAllProperties(getOwnProperties());
        return splitRenderer;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createOverflowRenderer(int layoutResult) {
        ListItemRenderer overflowRenderer = (ListItemRenderer) getNextRenderer();
        overflowRenderer.parent = this.parent;
        overflowRenderer.modelElement = this.modelElement;
        if (layoutResult == 3) {
            overflowRenderer.symbolRenderer = this.symbolRenderer;
            overflowRenderer.symbolAreaWidth = this.symbolAreaWidth;
        }
        overflowRenderer.addAllProperties(getOwnProperties());
        return overflowRenderer;
    }

    private void applyListSymbolPosition() {
        if (this.symbolRenderer != null) {
            ListSymbolPosition symbolPosition = (ListSymbolPosition) ListRenderer.getListItemOrListProperty(this, this.parent, 83);
            if (symbolPosition == ListSymbolPosition.INSIDE) {
                boolean isRtl = BaseDirection.RIGHT_TO_LEFT.equals(getProperty(7));
                if (this.childRenderers.size() > 0 && (this.childRenderers.get(0) instanceof ParagraphRenderer)) {
                    ParagraphRenderer paragraphRenderer = (ParagraphRenderer) this.childRenderers.get(0);
                    Float symbolIndent = getPropertyAsFloat(39);
                    if (this.symbolRenderer instanceof LineRenderer) {
                        if (symbolIndent != null) {
                            this.symbolRenderer.getChildRenderers().get(1).setProperty(isRtl ? 44 : 45, UnitValue.createPointValue(symbolIndent.floatValue()));
                        }
                        for (IRenderer childRenderer : this.symbolRenderer.getChildRenderers()) {
                            paragraphRenderer.childRenderers.add(0, childRenderer);
                        }
                    } else {
                        if (symbolIndent != null) {
                            this.symbolRenderer.setProperty(isRtl ? 44 : 45, UnitValue.createPointValue(symbolIndent.floatValue()));
                        }
                        paragraphRenderer.childRenderers.add(0, this.symbolRenderer);
                    }
                    this.symbolAddedInside = true;
                } else if (this.childRenderers.size() > 0 && (this.childRenderers.get(0) instanceof ImageRenderer)) {
                    Paragraph p = new Paragraph();
                    p.getAccessibilityProperties().setRole(null);
                    IRenderer paragraphRenderer2 = p.setMargin(0.0f).createRendererSubTree();
                    Float symbolIndent2 = getPropertyAsFloat(39);
                    if (symbolIndent2 != null) {
                        this.symbolRenderer.setProperty(45, UnitValue.createPointValue(symbolIndent2.floatValue()));
                    }
                    paragraphRenderer2.addChild(this.symbolRenderer);
                    paragraphRenderer2.addChild(this.childRenderers.get(0));
                    this.childRenderers.set(0, paragraphRenderer2);
                    this.symbolAddedInside = true;
                }
                if (!this.symbolAddedInside) {
                    Paragraph p2 = new Paragraph();
                    p2.getAccessibilityProperties().setRole(null);
                    IRenderer paragraphRenderer3 = p2.setMargin(0.0f).createRendererSubTree();
                    Float symbolIndent3 = getPropertyAsFloat(39);
                    if (symbolIndent3 != null) {
                        this.symbolRenderer.setProperty(45, UnitValue.createPointValue(symbolIndent3.floatValue()));
                    }
                    paragraphRenderer3.addChild(this.symbolRenderer);
                    this.childRenderers.add(0, paragraphRenderer3);
                    this.symbolAddedInside = true;
                }
            }
        }
    }

    private boolean isListSymbolEmpty(IRenderer listSymbolRenderer) {
        return listSymbolRenderer instanceof TextRenderer ? ((TextRenderer) listSymbolRenderer).getText().toString().length() == 0 : (listSymbolRenderer instanceof LineRenderer) && ((TextRenderer) listSymbolRenderer.getChildRenderers().get(1)).getText().toString().length() == 0;
    }

    private float[] calculateAscenderDescender() {
        PdfFont listItemFont = resolveFirstPdfFont();
        UnitValue fontSize = getPropertyAsUnitValue(24);
        if (listItemFont != null && fontSize != null) {
            if (!fontSize.isPointValue()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) ListItemRenderer.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 24));
            }
            float[] ascenderDescender = TextRenderer.calculateAscenderDescender(listItemFont);
            return new float[]{(fontSize.getValue() * ascenderDescender[0]) / 1000.0f, (fontSize.getValue() * ascenderDescender[1]) / 1000.0f};
        }
        return new float[]{0.0f, 0.0f};
    }
}
