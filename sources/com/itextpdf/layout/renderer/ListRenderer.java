package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.numbering.EnglishAlphabetNumbering;
import com.itextpdf.kernel.numbering.GreekAlphabetNumbering;
import com.itextpdf.kernel.numbering.RomanNumbering;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.IListSymbolFactory;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.ListSymbolPosition;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/ListRenderer.class */
public class ListRenderer extends BlockRenderer {
    public ListRenderer(List modelElement) {
        super(modelElement);
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        LayoutResult errorResult = initializeListSymbols(layoutContext);
        if (errorResult != null) {
            return errorResult;
        }
        LayoutResult result = super.layout(layoutContext);
        if (Boolean.TRUE.equals(getPropertyAsBoolean(26)) && null != result.getCauseOfNothing()) {
            if (1 == result.getStatus()) {
                result = correctListSplitting(this, null, result.getCauseOfNothing(), result.getOccupiedArea());
            } else if (2 == result.getStatus()) {
                result = correctListSplitting(result.getSplitRenderer(), result.getOverflowRenderer(), result.getCauseOfNothing(), result.getOccupiedArea());
            }
        }
        return result;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new ListRenderer((List) this.modelElement);
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createSplitRenderer(int layoutResult) {
        AbstractRenderer splitRenderer = super.createSplitRenderer(layoutResult);
        splitRenderer.addAllProperties(getOwnProperties());
        splitRenderer.setProperty(40, Boolean.TRUE);
        return splitRenderer;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createOverflowRenderer(int layoutResult) {
        AbstractRenderer overflowRenderer = super.createOverflowRenderer(layoutResult);
        overflowRenderer.addAllProperties(getOwnProperties());
        overflowRenderer.setProperty(40, Boolean.TRUE);
        return overflowRenderer;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        LayoutResult errorResult = initializeListSymbols(new LayoutContext(new LayoutArea(1, new Rectangle(MinMaxWidthUtils.getInfWidth(), 1000000.0f))));
        if (errorResult != null) {
            return MinMaxWidthUtils.countDefaultMinMaxWidth(this);
        }
        return super.getMinMaxWidth();
    }

    protected IRenderer makeListSymbolRenderer(int index, IRenderer renderer) {
        IRenderer symbolRenderer = createListSymbolRenderer(index, renderer);
        if (symbolRenderer != null) {
            symbolRenderer.setProperty(74, false);
        }
        return symbolRenderer;
    }

    static Object getListItemOrListProperty(IRenderer listItem, IRenderer list, int propertyId) {
        return listItem.hasProperty(propertyId) ? listItem.getProperty(propertyId) : list.getProperty(propertyId);
    }

    private IRenderer createListSymbolRenderer(int index, IRenderer renderer) {
        String numberText;
        IRenderer textRenderer;
        Object defaultListSymbol = getListItemOrListProperty(renderer, this, 37);
        if (defaultListSymbol instanceof Text) {
            return surroundTextBullet(new TextRenderer((Text) defaultListSymbol));
        }
        if (defaultListSymbol instanceof Image) {
            return new ImageRenderer((Image) defaultListSymbol);
        }
        if (defaultListSymbol instanceof ListNumberingType) {
            ListNumberingType numberingType = (ListNumberingType) defaultListSymbol;
            switch (numberingType) {
                case DECIMAL:
                    numberText = String.valueOf(index);
                    break;
                case DECIMAL_LEADING_ZERO:
                    numberText = (index < 10 ? "0" : "") + String.valueOf(index);
                    break;
                case ROMAN_LOWER:
                    numberText = RomanNumbering.toRomanLowerCase(index);
                    break;
                case ROMAN_UPPER:
                    numberText = RomanNumbering.toRomanUpperCase(index);
                    break;
                case ENGLISH_LOWER:
                    numberText = EnglishAlphabetNumbering.toLatinAlphabetNumberLowerCase(index);
                    break;
                case ENGLISH_UPPER:
                    numberText = EnglishAlphabetNumbering.toLatinAlphabetNumberUpperCase(index);
                    break;
                case GREEK_LOWER:
                    numberText = GreekAlphabetNumbering.toGreekAlphabetNumber(index, false, true);
                    break;
                case GREEK_UPPER:
                    numberText = GreekAlphabetNumbering.toGreekAlphabetNumber(index, true, true);
                    break;
                case ZAPF_DINGBATS_1:
                    numberText = TextUtil.charToString((char) (index + 171));
                    break;
                case ZAPF_DINGBATS_2:
                    numberText = TextUtil.charToString((char) (index + 181));
                    break;
                case ZAPF_DINGBATS_3:
                    numberText = TextUtil.charToString((char) (index + 191));
                    break;
                case ZAPF_DINGBATS_4:
                    numberText = TextUtil.charToString((char) (index + 201));
                    break;
                default:
                    throw new IllegalStateException();
            }
            Text textElement = new Text(getListItemOrListProperty(renderer, this, 41) + numberText + getListItemOrListProperty(renderer, this, 42));
            if (numberingType == ListNumberingType.GREEK_LOWER || numberingType == ListNumberingType.GREEK_UPPER || numberingType == ListNumberingType.ZAPF_DINGBATS_1 || numberingType == ListNumberingType.ZAPF_DINGBATS_2 || numberingType == ListNumberingType.ZAPF_DINGBATS_3 || numberingType == ListNumberingType.ZAPF_DINGBATS_4) {
                final String constantFont = (numberingType == ListNumberingType.GREEK_LOWER || numberingType == ListNumberingType.GREEK_UPPER) ? "Symbol" : "ZapfDingbats";
                textRenderer = new TextRenderer(textElement) { // from class: com.itextpdf.layout.renderer.ListRenderer.1
                    @Override // com.itextpdf.layout.renderer.TextRenderer, com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
                    public void draw(DrawContext drawContext) {
                        try {
                            setProperty(20, PdfFontFactory.createFont(constantFont));
                        } catch (IOException e) {
                        }
                        super.draw(drawContext);
                    }
                };
                try {
                    textRenderer.setProperty(20, PdfFontFactory.createFont(constantFont));
                } catch (IOException e) {
                }
            } else {
                textRenderer = new TextRenderer(textElement);
            }
            return surroundTextBullet(textRenderer);
        }
        if (defaultListSymbol instanceof IListSymbolFactory) {
            return surroundTextBullet(((IListSymbolFactory) defaultListSymbol).createSymbol(index, this, renderer).createRendererSubTree());
        }
        if (defaultListSymbol == null) {
            return null;
        }
        throw new IllegalStateException();
    }

    private LineRenderer surroundTextBullet(IRenderer bulletRenderer) {
        LineRenderer lineRenderer = new LineRenderer();
        Text zeroWidthJoiner = new Text("\u200d");
        zeroWidthJoiner.getAccessibilityProperties().setRole(StandardRoles.ARTIFACT);
        TextRenderer zeroWidthJoinerRenderer = new TextRenderer(zeroWidthJoiner);
        lineRenderer.addChild(zeroWidthJoinerRenderer);
        lineRenderer.addChild(bulletRenderer);
        lineRenderer.addChild(zeroWidthJoinerRenderer);
        return lineRenderer;
    }

    private LayoutResult correctListSplitting(IRenderer splitRenderer, IRenderer overflowRenderer, IRenderer causeOfNothing, LayoutArea occupiedArea) {
        int firstNotRendered = splitRenderer.getChildRenderers().get(0).getChildRenderers().indexOf(causeOfNothing);
        if (-1 == firstNotRendered) {
            return new LayoutResult(null == overflowRenderer ? 1 : 2, occupiedArea, splitRenderer, overflowRenderer, this);
        }
        IRenderer firstListItemRenderer = splitRenderer.getChildRenderers().get(0);
        ListRenderer newOverflowRenderer = (ListRenderer) createOverflowRenderer(2);
        newOverflowRenderer.deleteOwnProperty(26);
        newOverflowRenderer.childRenderers.add(((ListItemRenderer) firstListItemRenderer).createOverflowRenderer(2));
        newOverflowRenderer.childRenderers.addAll(splitRenderer.getChildRenderers().subList(1, splitRenderer.getChildRenderers().size()));
        java.util.List<IRenderer> childrenStillRemainingToRender = new ArrayList<>(firstListItemRenderer.getChildRenderers().subList(firstNotRendered + 1, firstListItemRenderer.getChildRenderers().size()));
        splitRenderer.getChildRenderers().removeAll(splitRenderer.getChildRenderers().subList(1, splitRenderer.getChildRenderers().size()));
        if (0 != childrenStillRemainingToRender.size()) {
            newOverflowRenderer.getChildRenderers().get(0).getChildRenderers().addAll(childrenStillRemainingToRender);
            splitRenderer.getChildRenderers().get(0).getChildRenderers().removeAll(childrenStillRemainingToRender);
            newOverflowRenderer.getChildRenderers().get(0).setProperty(44, splitRenderer.getChildRenderers().get(0).getProperty(44));
        } else {
            newOverflowRenderer.childRenderers.remove(0);
        }
        if (null != overflowRenderer) {
            newOverflowRenderer.childRenderers.addAll(overflowRenderer.getChildRenderers());
        }
        if (0 != newOverflowRenderer.childRenderers.size()) {
            return new LayoutResult(2, occupiedArea, splitRenderer, newOverflowRenderer, this);
        }
        return new LayoutResult(1, occupiedArea, null, null, this);
    }

    private LayoutResult initializeListSymbols(LayoutContext layoutContext) {
        LayoutTaggingHelper taggingHelper;
        if (!hasOwnProperty(40)) {
            java.util.List<IRenderer> symbolRenderers = new ArrayList<>();
            int listItemNum = ((Integer) getProperty(36, 1)).intValue();
            for (int i = 0; i < this.childRenderers.size(); i++) {
                this.childRenderers.get(i).setParent(this);
                listItemNum = this.childRenderers.get(i).getProperty(120) != null ? ((Integer) this.childRenderers.get(i).getProperty(120)).intValue() : listItemNum;
                IRenderer currentSymbolRenderer = makeListSymbolRenderer(listItemNum, this.childRenderers.get(i));
                if (BaseDirection.RIGHT_TO_LEFT.equals(getProperty(7))) {
                    currentSymbolRenderer.setProperty(7, BaseDirection.RIGHT_TO_LEFT);
                }
                LayoutResult listSymbolLayoutResult = null;
                if (currentSymbolRenderer != null) {
                    listItemNum++;
                    currentSymbolRenderer.setParent(this.childRenderers.get(i));
                    listSymbolLayoutResult = currentSymbolRenderer.layout(layoutContext);
                    currentSymbolRenderer.setParent(null);
                }
                this.childRenderers.get(i).setParent(null);
                boolean isForcedPlacement = Boolean.TRUE.equals(getPropertyAsBoolean(26));
                boolean listSymbolNotFit = (listSymbolLayoutResult == null || listSymbolLayoutResult.getStatus() == 1) ? false : true;
                if (listSymbolNotFit && isForcedPlacement) {
                    currentSymbolRenderer = null;
                }
                symbolRenderers.add(currentSymbolRenderer);
                if (listSymbolNotFit && !isForcedPlacement) {
                    return new LayoutResult(3, null, null, this, listSymbolLayoutResult.getCauseOfNothing());
                }
            }
            float maxSymbolWidth = 0.0f;
            for (int i2 = 0; i2 < this.childRenderers.size(); i2++) {
                IRenderer symbolRenderer = symbolRenderers.get(i2);
                if (symbolRenderer != null) {
                    IRenderer listItemRenderer = this.childRenderers.get(i2);
                    if (((ListSymbolPosition) getListItemOrListProperty(listItemRenderer, this, 83)) != ListSymbolPosition.INSIDE) {
                        maxSymbolWidth = Math.max(maxSymbolWidth, symbolRenderer.getOccupiedArea().getBBox().getWidth());
                    }
                }
            }
            Float symbolIndent = getPropertyAsFloat(39);
            int listItemNum2 = 0;
            for (IRenderer childRenderer : this.childRenderers) {
                childRenderer.setParent(this);
                childRenderer.deleteOwnProperty(44);
                UnitValue marginLeftUV = (UnitValue) childRenderer.getProperty(44, UnitValue.createPointValue(0.0f));
                if (!marginLeftUV.isPointValue()) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) ListRenderer.class);
                    logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
                }
                float calculatedMargin = marginLeftUV.getValue();
                if (((ListSymbolPosition) getListItemOrListProperty(childRenderer, this, 83)) == ListSymbolPosition.DEFAULT) {
                    calculatedMargin += maxSymbolWidth + (symbolIndent != null ? symbolIndent.floatValue() : 0.0f);
                }
                childRenderer.setProperty(44, UnitValue.createPointValue(calculatedMargin));
                int i3 = listItemNum2;
                listItemNum2++;
                IRenderer symbolRenderer2 = symbolRenderers.get(i3);
                ((ListItemRenderer) childRenderer).addSymbolRenderer(symbolRenderer2, maxSymbolWidth);
                if (symbolRenderer2 != null && (taggingHelper = (LayoutTaggingHelper) getProperty(108)) != null) {
                    if (symbolRenderer2 instanceof LineRenderer) {
                        taggingHelper.setRoleHint(symbolRenderer2.getChildRenderers().get(1), StandardRoles.LBL);
                    } else {
                        taggingHelper.setRoleHint(symbolRenderer2, StandardRoles.LBL);
                    }
                }
            }
            return null;
        }
        return null;
    }
}
