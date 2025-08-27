package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.util.ArrayUtil;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.LineLayoutContext;
import com.itextpdf.layout.layout.LineLayoutResult;
import com.itextpdf.layout.layout.MinMaxWidthLayoutResult;
import com.itextpdf.layout.layout.TextLayoutResult;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.FloatPropertyValue;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.UnitValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/LineRenderer.class */
public class LineRenderer extends AbstractRenderer {
    private static final float MIN_MAX_WIDTH_CORRECTION_EPS = 0.001f;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) LineRenderer.class);
    protected float maxAscent;
    protected float maxDescent;
    protected byte[] levels;
    private float maxTextAscent;
    private float maxTextDescent;
    private float maxBlockAscent;
    private float maxBlockDescent;

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        AbstractWidthHandler widthHandler;
        Float yLine;
        Rectangle layoutBox = layoutContext.getArea().getBBox().mo825clone();
        boolean wasParentsHeightClipped = layoutContext.isClippedHeight();
        List<Rectangle> floatRendererAreas = layoutContext.getFloatRendererAreas();
        OverflowPropertyValue oldXOverflow = null;
        boolean wasXOverflowChanged = false;
        if (floatRendererAreas != null) {
            float layoutWidth = layoutBox.getWidth();
            FloatingHelper.adjustLineAreaAccordingToFloats(floatRendererAreas, layoutBox);
            if (layoutWidth > layoutBox.getWidth()) {
                oldXOverflow = (OverflowPropertyValue) getProperty(103);
                wasXOverflowChanged = true;
                setProperty(103, OverflowPropertyValue.FIT);
            }
        }
        boolean noSoftWrap = Boolean.TRUE.equals(getOwnProperty(118));
        LineLayoutContext lineLayoutContext = layoutContext instanceof LineLayoutContext ? (LineLayoutContext) layoutContext : new LineLayoutContext(layoutContext);
        if (lineLayoutContext.getTextIndent() != 0.0f) {
            layoutBox.moveRight(lineLayoutContext.getTextIndent()).setWidth(layoutBox.getWidth() - lineLayoutContext.getTextIndent());
        }
        this.occupiedArea = new LayoutArea(layoutContext.getArea().getPageNumber(), layoutBox.mo825clone().moveUp(layoutBox.getHeight()).setHeight(0.0f).setWidth(0.0f));
        float curWidth = 0.0f;
        this.maxAscent = 0.0f;
        this.maxDescent = 0.0f;
        this.maxTextAscent = 0.0f;
        this.maxTextDescent = 0.0f;
        this.maxBlockAscent = -1.0E20f;
        this.maxBlockDescent = 1.0E20f;
        int childPos = 0;
        MinMaxWidth minMaxWidth = new MinMaxWidth();
        if (noSoftWrap) {
            widthHandler = new SumSumWidthHandler(minMaxWidth);
        } else {
            widthHandler = new MaxSumWidthHandler(minMaxWidth);
        }
        updateChildrenParent();
        resolveChildrenFonts();
        int totalNumberOfTrimmedGlyphs = trimFirst();
        BaseDirection baseDirection = applyOtf();
        updateBidiLevels(totalNumberOfTrimmedGlyphs, baseDirection);
        boolean anythingPlaced = false;
        TabStop hangingTabStop = null;
        LineLayoutResult result = null;
        boolean floatsPlaced = false;
        Map<Integer, IRenderer> floatsToNextPageSplitRenderers = new LinkedHashMap<>();
        List<IRenderer> floatsToNextPageOverflowRenderers = new ArrayList<>();
        List<IRenderer> floatsOverflowedToNextLine = new ArrayList<>();
        int lastTabIndex = 0;
        while (true) {
            if (childPos >= this.childRenderers.size()) {
                break;
            }
            IRenderer childRenderer = this.childRenderers.get(childPos);
            LayoutResult childResult = null;
            Rectangle bbox = new Rectangle(layoutBox.getX() + curWidth, layoutBox.getY(), layoutBox.getWidth() - curWidth, layoutBox.getHeight());
            if (childRenderer instanceof TextRenderer) {
                childRenderer.deleteOwnProperty(15);
                childRenderer.deleteOwnProperty(78);
            } else if (childRenderer instanceof TabRenderer) {
                if (hangingTabStop != null) {
                    IRenderer tabRenderer = this.childRenderers.get(childPos - 1);
                    tabRenderer.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), bbox), wasParentsHeightClipped));
                    curWidth += tabRenderer.getOccupiedArea().getBBox().getWidth();
                    widthHandler.updateMaxChildWidth(tabRenderer.getOccupiedArea().getBBox().getWidth());
                }
                hangingTabStop = calculateTab(childRenderer, curWidth, layoutBox.getWidth());
                if (childPos == this.childRenderers.size() - 1) {
                    hangingTabStop = null;
                }
                if (hangingTabStop != null) {
                    lastTabIndex = childPos;
                    childPos++;
                }
            }
            if (hangingTabStop != null && hangingTabStop.getTabAlignment() == TabAlignment.ANCHOR && (childRenderer instanceof TextRenderer)) {
                childRenderer.setProperty(66, hangingTabStop.getTabAnchor());
            }
            Object childWidth = childRenderer.getProperty(77);
            boolean childWidthWasReplaced = false;
            boolean childRendererHasOwnWidthProperty = childRenderer.hasOwnProperty(77);
            if ((childWidth instanceof UnitValue) && ((UnitValue) childWidth).isPercentValue()) {
                float normalizedChildWidth = decreaseRelativeWidthByChildAdditionalWidth(childRenderer, (((UnitValue) childWidth).getValue() / 100.0f) * layoutContext.getArea().getBBox().getWidth());
                if (normalizedChildWidth > 0.0f) {
                    childRenderer.setProperty(77, UnitValue.createPointValue(normalizedChildWidth));
                    childWidthWasReplaced = true;
                }
            }
            FloatPropertyValue kidFloatPropertyVal = (FloatPropertyValue) childRenderer.getProperty(99);
            boolean isChildFloating = (childRenderer instanceof AbstractRenderer) && FloatingHelper.isRendererFloating(childRenderer, kidFloatPropertyVal);
            if (isChildFloating) {
                LayoutResult childResult2 = null;
                MinMaxWidth kidMinMaxWidth = FloatingHelper.calculateMinMaxWidthForFloat((AbstractRenderer) childRenderer, kidFloatPropertyVal);
                float floatingBoxFullWidth = kidMinMaxWidth.getMaxWidth();
                if (!wasXOverflowChanged && childPos > 0) {
                    oldXOverflow = (OverflowPropertyValue) getProperty(103);
                    wasXOverflowChanged = true;
                    setProperty(103, OverflowPropertyValue.FIT);
                }
                if (!lineLayoutContext.isFloatOverflowedToNextPageWithNothing() && floatsOverflowedToNextLine.isEmpty() && (!anythingPlaced || floatingBoxFullWidth <= bbox.getWidth())) {
                    childResult2 = childRenderer.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), layoutContext.getArea().getBBox().mo825clone()), null, floatRendererAreas, wasParentsHeightClipped));
                }
                if (childWidthWasReplaced) {
                    if (childRendererHasOwnWidthProperty) {
                        childRenderer.setProperty(77, childWidth);
                    } else {
                        childRenderer.deleteOwnProperty(77);
                    }
                }
                float minChildWidth = 0.0f;
                if (childResult2 instanceof MinMaxWidthLayoutResult) {
                    if (!childWidthWasReplaced) {
                        minChildWidth = ((MinMaxWidthLayoutResult) childResult2).getMinMaxWidth().getMinWidth();
                    }
                    float maxChildWidth = ((MinMaxWidthLayoutResult) childResult2).getMinMaxWidth().getMaxWidth();
                    widthHandler.updateMinChildWidth(minChildWidth + 1.0E-4f);
                    widthHandler.updateMaxChildWidth(maxChildWidth + 1.0E-4f);
                } else {
                    widthHandler.updateMinChildWidth(kidMinMaxWidth.getMinWidth() + 1.0E-4f);
                    widthHandler.updateMaxChildWidth(kidMinMaxWidth.getMaxWidth() + 1.0E-4f);
                }
                if (childResult2 == null && !lineLayoutContext.isFloatOverflowedToNextPageWithNothing()) {
                    floatsOverflowedToNextLine.add(childRenderer);
                } else if (lineLayoutContext.isFloatOverflowedToNextPageWithNothing() || childResult2.getStatus() == 3) {
                    floatsToNextPageSplitRenderers.put(Integer.valueOf(childPos), null);
                    floatsToNextPageOverflowRenderers.add(childRenderer);
                    lineLayoutContext.setFloatOverflowedToNextPageWithNothing(true);
                } else if (childResult2.getStatus() == 2) {
                    floatsPlaced = true;
                    if (childRenderer instanceof TextRenderer) {
                        LineRenderer[] split = splitNotFittingFloat(childPos, childResult2);
                        IRenderer splitRenderer = childResult2.getSplitRenderer();
                        if (splitRenderer instanceof TextRenderer) {
                            ((TextRenderer) splitRenderer).trimFirst();
                            ((TextRenderer) splitRenderer).trimLast();
                        }
                        splitRenderer.getOccupiedArea().getBBox().setWidth(layoutContext.getArea().getBBox().getWidth());
                        result = new LineLayoutResult(2, this.occupiedArea, split[0], split[1], null);
                    } else {
                        floatsToNextPageSplitRenderers.put(Integer.valueOf(childPos), childResult2.getSplitRenderer());
                        floatsToNextPageOverflowRenderers.add(childResult2.getOverflowRenderer());
                        adjustLineOnFloatPlaced(layoutBox, childPos, kidFloatPropertyVal, childResult2.getSplitRenderer().getOccupiedArea().getBBox());
                    }
                } else {
                    floatsPlaced = true;
                    if (childRenderer instanceof TextRenderer) {
                        ((TextRenderer) childRenderer).trimFirst();
                        ((TextRenderer) childRenderer).trimLast();
                    }
                    adjustLineOnFloatPlaced(layoutBox, childPos, kidFloatPropertyVal, childRenderer.getOccupiedArea().getBBox());
                }
                childPos++;
                if (!anythingPlaced && childResult2 != null && childResult2.getStatus() == 3 && floatRendererAreas.isEmpty() && isFirstOnRootArea()) {
                    break;
                }
            } else {
                MinMaxWidth childBlockMinMaxWidth = null;
                boolean isInlineBlockChild = isInlineBlockChild(childRenderer);
                if (!childWidthWasReplaced && isInlineBlockChild && (childRenderer instanceof AbstractRenderer)) {
                    childBlockMinMaxWidth = ((AbstractRenderer) childRenderer).getMinMaxWidth();
                    float childMaxWidth = childBlockMinMaxWidth.getMaxWidth();
                    float lineFullAvailableWidth = layoutContext.getArea().getBBox().getWidth() - lineLayoutContext.getTextIndent();
                    if (!noSoftWrap && childMaxWidth > bbox.getWidth() + MIN_MAX_WIDTH_CORRECTION_EPS && bbox.getWidth() != lineFullAvailableWidth) {
                        childResult = new LineLayoutResult(3, null, null, childRenderer, childRenderer);
                    } else {
                        float inlineBlockWidth = Math.min(childMaxWidth + MIN_MAX_WIDTH_CORRECTION_EPS, lineFullAvailableWidth);
                        if (!isOverflowFit((OverflowPropertyValue) getProperty(103))) {
                            float childMinWidth = childBlockMinMaxWidth.getMinWidth() + MIN_MAX_WIDTH_CORRECTION_EPS;
                            inlineBlockWidth = Math.max(childMinWidth, inlineBlockWidth);
                        }
                        bbox.setWidth(inlineBlockWidth);
                        if (childBlockMinMaxWidth.getMinWidth() > bbox.getWidth()) {
                            if (logger.isWarnEnabled()) {
                                logger.warn(LogMessageConstant.INLINE_BLOCK_ELEMENT_WILL_BE_CLIPPED);
                            }
                            childRenderer.setProperty(26, true);
                        }
                    }
                    childBlockMinMaxWidth.setChildrenMaxWidth(childBlockMinMaxWidth.getChildrenMaxWidth() + MIN_MAX_WIDTH_CORRECTION_EPS);
                    childBlockMinMaxWidth.setChildrenMinWidth(childBlockMinMaxWidth.getChildrenMinWidth() + MIN_MAX_WIDTH_CORRECTION_EPS);
                }
                if (childResult == null) {
                    if (!wasXOverflowChanged && childPos > 0) {
                        oldXOverflow = (OverflowPropertyValue) getProperty(103);
                        wasXOverflowChanged = true;
                        setProperty(103, OverflowPropertyValue.FIT);
                    }
                    childResult = childRenderer.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), bbox), wasParentsHeightClipped));
                    if ((childResult instanceof MinMaxWidthLayoutResult) && null != childBlockMinMaxWidth) {
                        MinMaxWidth childResultMinMaxWidth = ((MinMaxWidthLayoutResult) childResult).getMinMaxWidth();
                        childResultMinMaxWidth.setChildrenMaxWidth(childResultMinMaxWidth.getChildrenMaxWidth() + MIN_MAX_WIDTH_CORRECTION_EPS);
                        childResultMinMaxWidth.setChildrenMinWidth(childResultMinMaxWidth.getChildrenMinWidth() + MIN_MAX_WIDTH_CORRECTION_EPS);
                    }
                }
                if (childWidthWasReplaced) {
                    if (childRendererHasOwnWidthProperty) {
                        childRenderer.setProperty(77, childWidth);
                    } else {
                        childRenderer.deleteOwnProperty(77);
                    }
                }
                float minChildWidth2 = 0.0f;
                float maxChildWidth2 = 0.0f;
                if (childResult instanceof MinMaxWidthLayoutResult) {
                    if (!childWidthWasReplaced) {
                        minChildWidth2 = ((MinMaxWidthLayoutResult) childResult).getMinMaxWidth().getMinWidth();
                    }
                    maxChildWidth2 = ((MinMaxWidthLayoutResult) childResult).getMinMaxWidth().getMaxWidth();
                } else if (childBlockMinMaxWidth != null) {
                    minChildWidth2 = childBlockMinMaxWidth.getMinWidth();
                    maxChildWidth2 = childBlockMinMaxWidth.getMaxWidth();
                }
                float childAscent = 0.0f;
                float childDescent = 0.0f;
                if ((childRenderer instanceof ILeafElementRenderer) && childResult.getStatus() != 3) {
                    childAscent = ((ILeafElementRenderer) childRenderer).getAscent();
                    childDescent = ((ILeafElementRenderer) childRenderer).getDescent();
                } else if (isInlineBlockChild && childResult.getStatus() != 3) {
                    if (!(childRenderer instanceof AbstractRenderer) || (yLine = ((AbstractRenderer) childRenderer).getLastYLineRecursively()) == null) {
                        childAscent = childRenderer.getOccupiedArea().getBBox().getHeight();
                    } else {
                        childAscent = childRenderer.getOccupiedArea().getBBox().getTop() - yLine.floatValue();
                        childDescent = -(yLine.floatValue() - childRenderer.getOccupiedArea().getBBox().getBottom());
                    }
                }
                boolean newLineOccurred = (childResult instanceof TextLayoutResult) && ((TextLayoutResult) childResult).isSplitForcedByNewline();
                boolean shouldBreakLayouting = childResult.getStatus() != 1 || newLineOccurred;
                boolean wordWasSplitAndItWillFitOntoNextLine = false;
                if (shouldBreakLayouting && (childResult instanceof TextLayoutResult) && ((TextLayoutResult) childResult).isWordHasBeenSplit()) {
                    if (wasXOverflowChanged) {
                        setProperty(103, oldXOverflow);
                    }
                    LayoutResult newLayoutResult = childRenderer.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), layoutBox), wasParentsHeightClipped));
                    if (wasXOverflowChanged) {
                        setProperty(103, OverflowPropertyValue.FIT);
                    }
                    if ((newLayoutResult instanceof TextLayoutResult) && !((TextLayoutResult) newLayoutResult).isWordHasBeenSplit()) {
                        wordWasSplitAndItWillFitOntoNextLine = true;
                    }
                }
                if (!wordWasSplitAndItWillFitOntoNextLine) {
                    this.maxAscent = Math.max(this.maxAscent, childAscent);
                    if (childRenderer instanceof TextRenderer) {
                        this.maxTextAscent = Math.max(this.maxTextAscent, childAscent);
                    } else if (!isChildFloating) {
                        this.maxBlockAscent = Math.max(this.maxBlockAscent, childAscent);
                    }
                    this.maxDescent = Math.min(this.maxDescent, childDescent);
                    if (childRenderer instanceof TextRenderer) {
                        this.maxTextDescent = Math.min(this.maxTextDescent, childDescent);
                    } else if (!isChildFloating) {
                        this.maxBlockDescent = Math.min(this.maxBlockDescent, childDescent);
                    }
                }
                float maxHeight = this.maxAscent - this.maxDescent;
                float currChildTextIndent = anythingPlaced ? 0.0f : lineLayoutContext.getTextIndent();
                if (hangingTabStop != null && (TabAlignment.LEFT == hangingTabStop.getTabAlignment() || shouldBreakLayouting || this.childRenderers.size() - 1 == childPos || (this.childRenderers.get(childPos + 1) instanceof TabRenderer))) {
                    IRenderer tabRenderer2 = this.childRenderers.get(lastTabIndex);
                    List<IRenderer> affectedRenderers = new ArrayList<>();
                    affectedRenderers.addAll(this.childRenderers.subList(lastTabIndex + 1, childPos + 1));
                    float tabWidth = calculateTab(layoutBox, curWidth, hangingTabStop, affectedRenderers, tabRenderer2);
                    tabRenderer2.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), bbox), wasParentsHeightClipped));
                    float sumOfAffectedRendererWidths = 0.0f;
                    for (IRenderer renderer : affectedRenderers) {
                        renderer.move(tabWidth + sumOfAffectedRendererWidths, 0.0f);
                        sumOfAffectedRendererWidths += renderer.getOccupiedArea().getBBox().getWidth();
                    }
                    if (childResult.getSplitRenderer() != null) {
                        childResult.getSplitRenderer().move((tabWidth + sumOfAffectedRendererWidths) - childResult.getSplitRenderer().getOccupiedArea().getBBox().getWidth(), 0.0f);
                    }
                    float tabAndNextElemWidth = tabWidth + childResult.getOccupiedArea().getBBox().getWidth();
                    if (hangingTabStop.getTabAlignment() == TabAlignment.RIGHT && curWidth + tabAndNextElemWidth < hangingTabStop.getTabPosition()) {
                        curWidth = hangingTabStop.getTabPosition();
                    } else {
                        curWidth += tabAndNextElemWidth;
                    }
                    widthHandler.updateMinChildWidth(minChildWidth2 + currChildTextIndent);
                    widthHandler.updateMaxChildWidth(tabWidth + maxChildWidth2 + currChildTextIndent);
                    hangingTabStop = null;
                } else if (null == hangingTabStop) {
                    if (childResult.getOccupiedArea() != null && childResult.getOccupiedArea().getBBox() != null) {
                        curWidth += childResult.getOccupiedArea().getBBox().getWidth();
                    }
                    widthHandler.updateMinChildWidth(minChildWidth2 + currChildTextIndent);
                    widthHandler.updateMaxChildWidth(maxChildWidth2 + currChildTextIndent);
                }
                if (!wordWasSplitAndItWillFitOntoNextLine) {
                    this.occupiedArea.setBBox(new Rectangle(layoutBox.getX(), (layoutBox.getY() + layoutBox.getHeight()) - maxHeight, curWidth, maxHeight));
                }
                if (shouldBreakLayouting) {
                    LineRenderer[] split2 = split();
                    split2[0].childRenderers = new ArrayList(this.childRenderers.subList(0, childPos));
                    if (wordWasSplitAndItWillFitOntoNextLine) {
                        split2[1].childRenderers.add(childRenderer);
                        split2[1].childRenderers.addAll(this.childRenderers.subList(childPos + 1, this.childRenderers.size()));
                    } else {
                        boolean forcePlacement = Boolean.TRUE.equals(getPropertyAsBoolean(26));
                        boolean isInlineBlockAndFirstOnRootArea = isInlineBlockChild && isFirstOnRootArea();
                        if ((childResult.getStatus() == 2 && (!isInlineBlockChild || forcePlacement || isInlineBlockAndFirstOnRootArea)) || childResult.getStatus() == 1) {
                            split2[0].addChild(childResult.getSplitRenderer());
                            anythingPlaced = true;
                        }
                        if (null != childResult.getOverflowRenderer()) {
                            if (isInlineBlockChild && !forcePlacement && !isInlineBlockAndFirstOnRootArea) {
                                split2[1].childRenderers.add(childRenderer);
                            } else if (isInlineBlockChild && childResult.getOverflowRenderer().getChildRenderers().size() == 0 && childResult.getStatus() == 2) {
                                if (logger.isWarnEnabled()) {
                                    logger.warn(LogMessageConstant.INLINE_BLOCK_ELEMENT_WILL_BE_CLIPPED);
                                }
                            } else {
                                split2[1].childRenderers.add(childResult.getOverflowRenderer());
                            }
                        }
                        split2[1].childRenderers.addAll(this.childRenderers.subList(childPos + 1, this.childRenderers.size()));
                    }
                    replaceSplitRendererKidFloats(floatsToNextPageSplitRenderers, split2[0]);
                    split2[0].childRenderers.removeAll(floatsOverflowedToNextLine);
                    split2[1].childRenderers.addAll(0, floatsOverflowedToNextLine);
                    if (split2[1].childRenderers.size() == 0 && floatsToNextPageOverflowRenderers.isEmpty()) {
                        split2[1] = null;
                    }
                    IRenderer causeOfNothing = childResult.getStatus() == 3 ? childResult.getCauseOfNothing() : childRenderer;
                    if (split2[1] == null) {
                        result = new LineLayoutResult(1, this.occupiedArea, split2[0], split2[1], causeOfNothing);
                    } else {
                        if (anythingPlaced || floatsPlaced) {
                            result = new LineLayoutResult(2, this.occupiedArea, split2[0], split2[1], causeOfNothing);
                        } else {
                            result = new LineLayoutResult(3, null, split2[0], split2[1], null);
                        }
                        result.setFloatsOverflowedToNextPage(floatsToNextPageOverflowRenderers);
                    }
                    if (newLineOccurred) {
                        result.setSplitForcedByNewline(true);
                    }
                } else {
                    anythingPlaced = true;
                    childPos++;
                }
            }
        }
        if (result == null) {
            boolean noOverflowedFloats = floatsOverflowedToNextLine.isEmpty() && floatsToNextPageOverflowRenderers.isEmpty();
            if (((anythingPlaced || floatsPlaced) && noOverflowedFloats) || 0 == this.childRenderers.size() || noOverflowedFloats) {
                result = new LineLayoutResult(1, this.occupiedArea, null, null);
            } else if (anythingPlaced || floatsPlaced) {
                LineRenderer[] split3 = split();
                split3[0].childRenderers.addAll(this.childRenderers.subList(0, childPos));
                replaceSplitRendererKidFloats(floatsToNextPageSplitRenderers, split3[0]);
                split3[0].childRenderers.removeAll(floatsOverflowedToNextLine);
                split3[1].childRenderers.addAll(floatsOverflowedToNextLine);
                result = new LineLayoutResult(2, this.occupiedArea, split3[0], split3[1], null);
                result.setFloatsOverflowedToNextPage(floatsToNextPageOverflowRenderers);
            } else {
                result = new LineLayoutResult(3, null, null, this, floatsOverflowedToNextLine.isEmpty() ? floatsToNextPageOverflowRenderers.get(0) : floatsOverflowedToNextLine.get(0));
            }
        }
        if (baseDirection != null && baseDirection != BaseDirection.NO_BIDI) {
            List<IRenderer> children = null;
            if (result.getStatus() == 2) {
                children = result.getSplitRenderer().getChildRenderers();
            } else if (result.getStatus() == 1) {
                children = getChildRenderers();
            }
            if (children != null) {
                boolean newLineFound = false;
                List<RendererGlyph> lineGlyphs = new ArrayList<>();
                Map<TextRenderer, List<IRenderer>> insertAfter = new HashMap<>();
                List<IRenderer> starterNonTextRenderers = new ArrayList<>();
                TextRenderer lastTextRenderer = null;
                for (IRenderer child : children) {
                    if (newLineFound) {
                        break;
                    }
                    if (child instanceof TextRenderer) {
                        GlyphLine childLine = ((TextRenderer) child).line;
                        int i = childLine.start;
                        while (true) {
                            if (i >= childLine.end) {
                                break;
                            }
                            if (TextUtil.isNewLine(childLine.get(i))) {
                                newLineFound = true;
                                break;
                            }
                            lineGlyphs.add(new RendererGlyph(childLine.get(i), (TextRenderer) child));
                            i++;
                        }
                        lastTextRenderer = (TextRenderer) child;
                    } else if (lastTextRenderer != null) {
                        if (!insertAfter.containsKey(lastTextRenderer)) {
                            insertAfter.put(lastTextRenderer, new ArrayList<>());
                        }
                        insertAfter.get(lastTextRenderer).add(child);
                    } else {
                        starterNonTextRenderers.add(child);
                    }
                }
                byte[] lineLevels = new byte[lineGlyphs.size()];
                if (this.levels != null) {
                    System.arraycopy(this.levels, 0, lineLevels, 0, lineGlyphs.size());
                }
                int[] reorder = TypographyUtils.reorderLine(lineGlyphs, lineLevels, this.levels);
                if (reorder != null) {
                    children.clear();
                    int pos = 0;
                    int initialPos = 0;
                    boolean reversed = false;
                    int offset = 0;
                    Iterator<IRenderer> it = starterNonTextRenderers.iterator();
                    while (it.hasNext()) {
                        children.add(it.next());
                    }
                    while (pos < lineGlyphs.size()) {
                        IRenderer renderer2 = lineGlyphs.get(pos).renderer;
                        TextRenderer newRenderer = new TextRenderer((TextRenderer) renderer2).removeReversedRanges();
                        children.add(newRenderer);
                        if (insertAfter.containsKey((TextRenderer) renderer2)) {
                            children.addAll(insertAfter.get((TextRenderer) renderer2));
                            insertAfter.remove((TextRenderer) renderer2);
                        }
                        newRenderer.line = new GlyphLine(newRenderer.line);
                        List<Glyph> replacementGlyphs = new ArrayList<>();
                        while (pos < lineGlyphs.size() && lineGlyphs.get(pos).renderer == renderer2) {
                            if (pos + 1 < lineGlyphs.size()) {
                                if (reorder[pos] == reorder[pos + 1] + 1 && !TextUtil.isSpaceOrWhitespace(lineGlyphs.get(pos + 1).glyph) && !TextUtil.isSpaceOrWhitespace(lineGlyphs.get(pos).glyph)) {
                                    reversed = true;
                                } else {
                                    if (reversed) {
                                        List<int[]> reversedRange = newRenderer.initReversedRanges();
                                        reversedRange.add(new int[]{initialPos - offset, pos - offset});
                                        reversed = false;
                                    }
                                    initialPos = pos + 1;
                                }
                            }
                            replacementGlyphs.add(lineGlyphs.get(pos).glyph);
                            pos++;
                        }
                        if (reversed) {
                            List<int[]> reversedRange2 = newRenderer.initReversedRanges();
                            reversedRange2.add(new int[]{initialPos - offset, (pos - 1) - offset});
                            reversed = false;
                            initialPos = pos;
                        }
                        offset = initialPos;
                        newRenderer.line.setGlyphs(replacementGlyphs);
                    }
                    adjustChildPositionsAfterReordering(children, this.occupiedArea.getBBox().getLeft());
                }
                if (result.getStatus() == 2) {
                    LineRenderer overflow = (LineRenderer) result.getOverflowRenderer();
                    if (this.levels != null) {
                        overflow.levels = new byte[this.levels.length - lineLevels.length];
                        System.arraycopy(this.levels, lineLevels.length, overflow.levels, 0, overflow.levels.length);
                        if (overflow.levels.length == 0) {
                            overflow.levels = null;
                        }
                    }
                }
            }
        }
        LineRenderer processed = result.getStatus() == 1 ? this : (LineRenderer) result.getSplitRenderer();
        if (anythingPlaced || floatsPlaced) {
            processed.adjustChildrenYLine().trimLast();
            result.setMinMaxWidth(minMaxWidth);
        }
        if (wasXOverflowChanged) {
            setProperty(103, oldXOverflow);
            if (null != result.getSplitRenderer()) {
                result.getSplitRenderer().setProperty(103, oldXOverflow);
            }
            if (null != result.getOverflowRenderer()) {
                result.getOverflowRenderer().setProperty(103, oldXOverflow);
            }
        }
        return result;
    }

    public float getMaxAscent() {
        return this.maxAscent;
    }

    public float getMaxDescent() {
        return this.maxDescent;
    }

    public float getYLine() {
        return this.occupiedArea.getBBox().getY() - this.maxDescent;
    }

    public float getLeadingValue(Leading leading) {
        switch (leading.getType()) {
            case 1:
                return Math.max(leading.getValue(), this.maxBlockAscent - this.maxBlockDescent);
            case 2:
                return getTopLeadingIndent(leading) + getBottomLeadingIndent(leading);
            default:
                throw new IllegalStateException();
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new LineRenderer();
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float getFirstYLineRecursively() {
        return Float.valueOf(getYLine());
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float getLastYLineRecursively() {
        return Float.valueOf(getYLine());
    }

    public void justify(float width) {
        float ratio = getPropertyAsFloat(61).floatValue();
        IRenderer lastChildRenderer = getLastNonFloatChildRenderer();
        if (lastChildRenderer == null) {
            return;
        }
        float freeWidth = ((this.occupiedArea.getBBox().getX() + width) - lastChildRenderer.getOccupiedArea().getBBox().getX()) - lastChildRenderer.getOccupiedArea().getBBox().getWidth();
        int numberOfSpaces = getNumberOfSpaces();
        int baseCharsCount = baseCharactersCount();
        float baseFactor = freeWidth / ((ratio * numberOfSpaces) + ((1.0f - ratio) * (baseCharsCount - 1)));
        if (Float.isInfinite(baseFactor)) {
            baseFactor = 0.0f;
        }
        float wordSpacing = ratio * baseFactor;
        float characterSpacing = (1.0f - ratio) * baseFactor;
        float lastRightPos = this.occupiedArea.getBBox().getX();
        Iterator<IRenderer> it = this.childRenderers.iterator();
        while (it.hasNext()) {
            IRenderer child = it.next();
            if (!FloatingHelper.isRendererFloating(child)) {
                float childX = child.getOccupiedArea().getBBox().getX();
                child.move(lastRightPos - childX, 0.0f);
                float childX2 = lastRightPos;
                if (child instanceof TextRenderer) {
                    float childHSCale = ((TextRenderer) child).getPropertyAsFloat(29, Float.valueOf(1.0f)).floatValue();
                    Float oldCharacterSpacing = ((TextRenderer) child).getPropertyAsFloat(15);
                    Float oldWordSpacing = ((TextRenderer) child).getPropertyAsFloat(78);
                    child.setProperty(15, Float.valueOf((null == oldCharacterSpacing ? 0.0f : oldCharacterSpacing.floatValue()) + (characterSpacing / childHSCale)));
                    child.setProperty(78, Float.valueOf((null == oldWordSpacing ? 0.0f : oldWordSpacing.floatValue()) + (wordSpacing / childHSCale)));
                    boolean isLastTextRenderer = child == lastChildRenderer;
                    float widthAddition = ((isLastTextRenderer ? ((TextRenderer) child).lineLength() - 1 : ((TextRenderer) child).lineLength()) * characterSpacing) + (wordSpacing * ((TextRenderer) child).getNumberOfSpaces());
                    child.getOccupiedArea().getBBox().setWidth(child.getOccupiedArea().getBBox().getWidth() + widthAddition);
                }
                lastRightPos = childX2 + child.getOccupiedArea().getBBox().getWidth();
            }
        }
        getOccupiedArea().getBBox().setWidth(width);
    }

    protected int getNumberOfSpaces() {
        int spaces = 0;
        for (IRenderer child : this.childRenderers) {
            if ((child instanceof TextRenderer) && !FloatingHelper.isRendererFloating(child)) {
                spaces += ((TextRenderer) child).getNumberOfSpaces();
            }
        }
        return spaces;
    }

    protected int length() {
        int length = 0;
        for (IRenderer child : this.childRenderers) {
            if ((child instanceof TextRenderer) && !FloatingHelper.isRendererFloating(child)) {
                length += ((TextRenderer) child).lineLength();
            }
        }
        return length;
    }

    protected int baseCharactersCount() {
        int count = 0;
        for (IRenderer child : this.childRenderers) {
            if ((child instanceof TextRenderer) && !FloatingHelper.isRendererFloating(child)) {
                count += ((TextRenderer) child).baseCharactersCount();
            }
        }
        return count;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IRenderer renderer : this.childRenderers) {
            sb.append(renderer.toString());
        }
        return sb.toString();
    }

    protected LineRenderer createSplitRenderer() {
        return (LineRenderer) getNextRenderer();
    }

    protected LineRenderer createOverflowRenderer() {
        return (LineRenderer) getNextRenderer();
    }

    protected LineRenderer[] split() {
        LineRenderer splitRenderer = createSplitRenderer();
        splitRenderer.occupiedArea = this.occupiedArea.mo950clone();
        splitRenderer.parent = this.parent;
        splitRenderer.maxAscent = this.maxAscent;
        splitRenderer.maxDescent = this.maxDescent;
        splitRenderer.maxTextAscent = this.maxTextAscent;
        splitRenderer.maxTextDescent = this.maxTextDescent;
        splitRenderer.maxBlockAscent = this.maxBlockAscent;
        splitRenderer.maxBlockDescent = this.maxBlockDescent;
        splitRenderer.levels = this.levels;
        splitRenderer.addAllProperties(getOwnProperties());
        LineRenderer overflowRenderer = createOverflowRenderer();
        overflowRenderer.parent = this.parent;
        overflowRenderer.addAllProperties(getOwnProperties());
        return new LineRenderer[]{splitRenderer, overflowRenderer};
    }

    protected LineRenderer adjustChildrenYLine() {
        float actualYLine = (this.occupiedArea.getBBox().getY() + this.occupiedArea.getBBox().getHeight()) - this.maxAscent;
        for (IRenderer renderer : this.childRenderers) {
            if (!FloatingHelper.isRendererFloating(renderer)) {
                if (renderer instanceof ILeafElementRenderer) {
                    float descent = ((ILeafElementRenderer) renderer).getDescent();
                    renderer.move(0.0f, (actualYLine - renderer.getOccupiedArea().getBBox().getBottom()) + descent);
                } else {
                    Float yLine = (isInlineBlockChild(renderer) && (renderer instanceof AbstractRenderer)) ? ((AbstractRenderer) renderer).getLastYLineRecursively() : null;
                    renderer.move(0.0f, actualYLine - (yLine == null ? renderer.getOccupiedArea().getBBox().getBottom() : yLine.floatValue()));
                }
            }
        }
        return this;
    }

    protected void applyLeading(float deltaY) {
        this.occupiedArea.getBBox().moveUp(deltaY);
        this.occupiedArea.getBBox().decreaseHeight(deltaY);
        for (IRenderer child : this.childRenderers) {
            if (!FloatingHelper.isRendererFloating(child)) {
                child.move(0.0f, deltaY);
            }
        }
    }

    protected LineRenderer trimLast() {
        int lastIndex = this.childRenderers.size();
        IRenderer lastRenderer = null;
        do {
            lastIndex--;
            if (lastIndex < 0) {
                break;
            }
            lastRenderer = this.childRenderers.get(lastIndex);
        } while (FloatingHelper.isRendererFloating(lastRenderer));
        if ((lastRenderer instanceof TextRenderer) && lastIndex >= 0) {
            float trimmedSpace = ((TextRenderer) lastRenderer).trimLast();
            this.occupiedArea.getBBox().setWidth(this.occupiedArea.getBBox().getWidth() - trimmedSpace);
        }
        return this;
    }

    public boolean containsImage() {
        for (IRenderer renderer : this.childRenderers) {
            if (renderer instanceof ImageRenderer) {
                return true;
            }
        }
        return false;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        LineLayoutResult result = (LineLayoutResult) layout(new LayoutContext(new LayoutArea(1, new Rectangle(MinMaxWidthUtils.getInfWidth(), 1000000.0f))));
        return result.getMinMaxWidth();
    }

    float getTopLeadingIndent(Leading leading) {
        switch (leading.getType()) {
            case 1:
                return (Math.max(leading.getValue(), this.maxBlockAscent - this.maxBlockDescent) - this.occupiedArea.getBBox().getHeight()) / 2.0f;
            case 2:
                UnitValue fontSize = (UnitValue) getProperty(24, UnitValue.createPointValue(0.0f));
                if (!fontSize.isPointValue()) {
                    logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 24));
                }
                float textAscent = (this.maxTextAscent != 0.0f || this.maxTextDescent != 0.0f || Math.abs(this.maxAscent) + Math.abs(this.maxDescent) == 0.0f || containsImage()) ? this.maxTextAscent : fontSize.getValue() * 0.8f;
                float textDescent = (this.maxTextAscent != 0.0f || this.maxTextDescent != 0.0f || Math.abs(this.maxAscent) + Math.abs(this.maxDescent) == 0.0f || containsImage()) ? this.maxTextDescent : (-fontSize.getValue()) * 0.2f;
                return Math.max(textAscent + (((textAscent - textDescent) * (leading.getValue() - 1.0f)) / 2.0f), this.maxBlockAscent) - this.maxAscent;
            default:
                throw new IllegalStateException();
        }
    }

    float getBottomLeadingIndent(Leading leading) {
        switch (leading.getType()) {
            case 1:
                return (Math.max(leading.getValue(), this.maxBlockAscent - this.maxBlockDescent) - this.occupiedArea.getBBox().getHeight()) / 2.0f;
            case 2:
                UnitValue fontSize = (UnitValue) getProperty(24, UnitValue.createPointValue(0.0f));
                if (!fontSize.isPointValue()) {
                    logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 24));
                }
                float textAscent = (this.maxTextAscent == 0.0f && this.maxTextDescent == 0.0f && !containsImage()) ? fontSize.getValue() * 0.8f : this.maxTextAscent;
                float textDescent = (this.maxTextAscent == 0.0f && this.maxTextDescent == 0.0f && !containsImage()) ? (-fontSize.getValue()) * 0.2f : this.maxTextDescent;
                return Math.max((-textDescent) + (((textAscent - textDescent) * (leading.getValue() - 1.0f)) / 2.0f), -this.maxBlockDescent) + this.maxDescent;
            default:
                throw new IllegalStateException();
        }
    }

    static void adjustChildPositionsAfterReordering(List<IRenderer> children, float initialXPos) {
        float currentWidth;
        float currentXPos = initialXPos;
        for (IRenderer child : children) {
            if (!FloatingHelper.isRendererFloating(child)) {
                if (child instanceof TextRenderer) {
                    float currentWidth2 = ((TextRenderer) child).calculateLineWidth();
                    UnitValue[] margins = ((TextRenderer) child).getMargins();
                    if (!margins[1].isPointValue() && logger.isErrorEnabled()) {
                        logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, "right margin"));
                    }
                    if (!margins[3].isPointValue() && logger.isErrorEnabled()) {
                        logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, "left margin"));
                    }
                    UnitValue[] paddings = ((TextRenderer) child).getPaddings();
                    if (!paddings[1].isPointValue() && logger.isErrorEnabled()) {
                        logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, "right padding"));
                    }
                    if (!paddings[3].isPointValue() && logger.isErrorEnabled()) {
                        logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, "left padding"));
                    }
                    currentWidth = currentWidth2 + margins[1].getValue() + margins[3].getValue() + paddings[1].getValue() + paddings[3].getValue();
                    ((TextRenderer) child).occupiedArea.getBBox().setX(currentXPos).setWidth(currentWidth);
                } else {
                    currentWidth = child.getOccupiedArea().getBBox().getWidth();
                    child.move(currentXPos - child.getOccupiedArea().getBBox().getX(), 0.0f);
                }
                currentXPos += currentWidth;
            }
        }
    }

    private LineRenderer[] splitNotFittingFloat(int childPos, LayoutResult childResult) {
        LineRenderer[] split = split();
        split[0].childRenderers.addAll(this.childRenderers.subList(0, childPos));
        split[0].childRenderers.add(childResult.getSplitRenderer());
        split[1].childRenderers.add(childResult.getOverflowRenderer());
        split[1].childRenderers.addAll(this.childRenderers.subList(childPos + 1, this.childRenderers.size()));
        return split;
    }

    private void adjustLineOnFloatPlaced(Rectangle layoutBox, int childPos, FloatPropertyValue kidFloatPropertyVal, Rectangle justPlacedFloatBox) {
        if (justPlacedFloatBox.getBottom() >= layoutBox.getTop() || justPlacedFloatBox.getTop() < layoutBox.getTop()) {
            return;
        }
        float floatWidth = justPlacedFloatBox.getWidth();
        if (kidFloatPropertyVal.equals(FloatPropertyValue.LEFT)) {
            layoutBox.setWidth(layoutBox.getWidth() - floatWidth).moveRight(floatWidth);
            this.occupiedArea.getBBox().moveRight(floatWidth);
            if (1 != 0) {
                for (int i = 0; i < childPos; i++) {
                    IRenderer prevChild = this.childRenderers.get(i);
                    if (!FloatingHelper.isRendererFloating(prevChild)) {
                        prevChild.move(floatWidth, 0.0f);
                    }
                }
                return;
            }
            return;
        }
        layoutBox.setWidth(layoutBox.getWidth() - floatWidth);
        if (1 == 0) {
        }
    }

    private void replaceSplitRendererKidFloats(Map<Integer, IRenderer> floatsToNextPageSplitRenderers, LineRenderer splitRenderer) {
        for (Map.Entry<Integer, IRenderer> splitFloat : floatsToNextPageSplitRenderers.entrySet()) {
            if (splitFloat.getValue() != null) {
                splitRenderer.childRenderers.set(splitFloat.getKey().intValue(), splitFloat.getValue());
            } else {
                splitRenderer.childRenderers.set(splitFloat.getKey().intValue(), null);
            }
        }
        for (int i = splitRenderer.getChildRenderers().size() - 1; i >= 0; i--) {
            if (splitRenderer.getChildRenderers().get(i) == null) {
                splitRenderer.getChildRenderers().remove(i);
            }
        }
    }

    private IRenderer getLastNonFloatChildRenderer() {
        for (int i = this.childRenderers.size() - 1; i >= 0; i--) {
            if (!FloatingHelper.isRendererFloating(this.childRenderers.get(i))) {
                return this.childRenderers.get(i);
            }
        }
        return null;
    }

    private TabStop getNextTabStop(float curWidth) {
        NavigableMap<Float, TabStop> tabStops = (NavigableMap) getProperty(69);
        Map.Entry<Float, TabStop> nextTabStopEntry = null;
        TabStop nextTabStop = null;
        if (tabStops != null) {
            nextTabStopEntry = tabStops.higherEntry(Float.valueOf(curWidth));
        }
        if (nextTabStopEntry != null) {
            nextTabStop = nextTabStopEntry.getValue();
        }
        return nextTabStop;
    }

    private TabStop calculateTab(IRenderer childRenderer, float curWidth, float lineWidth) {
        TabStop nextTabStop = getNextTabStop(curWidth);
        if (nextTabStop == null) {
            processDefaultTab(childRenderer, curWidth, lineWidth);
            return null;
        }
        childRenderer.setProperty(68, nextTabStop.getTabLeader());
        childRenderer.setProperty(77, UnitValue.createPointValue(nextTabStop.getTabPosition() - curWidth));
        childRenderer.setProperty(85, UnitValue.createPointValue(this.maxAscent - this.maxDescent));
        if (nextTabStop.getTabAlignment() == TabAlignment.LEFT) {
            return null;
        }
        return nextTabStop;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private float calculateTab(com.itextpdf.kernel.geom.Rectangle r6, float r7, com.itextpdf.layout.element.TabStop r8, java.util.List<com.itextpdf.layout.renderer.IRenderer> r9, com.itextpdf.layout.renderer.IRenderer r10) {
        /*
            Method dump skipped, instructions count: 314
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.layout.renderer.LineRenderer.calculateTab(com.itextpdf.kernel.geom.Rectangle, float, com.itextpdf.layout.element.TabStop, java.util.List, com.itextpdf.layout.renderer.IRenderer):float");
    }

    private void processDefaultTab(IRenderer tabRenderer, float curWidth, float lineWidth) {
        Float tabDefault = getPropertyAsFloat(67);
        Float tabWidth = Float.valueOf(tabDefault.floatValue() - (curWidth % tabDefault.floatValue()));
        if (curWidth + tabWidth.floatValue() > lineWidth) {
            tabWidth = Float.valueOf(lineWidth - curWidth);
        }
        tabRenderer.setProperty(77, UnitValue.createPointValue(tabWidth.floatValue()));
        tabRenderer.setProperty(85, UnitValue.createPointValue(this.maxAscent - this.maxDescent));
    }

    private void updateChildrenParent() {
        for (IRenderer renderer : this.childRenderers) {
            renderer.setParent(this);
        }
    }

    private int trimFirst() {
        int totalNumberOfTrimmedGlyphs = 0;
        for (IRenderer renderer : this.childRenderers) {
            if (!FloatingHelper.isRendererFloating(renderer)) {
                if (!(renderer instanceof TextRenderer)) {
                    break;
                }
                TextRenderer textRenderer = (TextRenderer) renderer;
                GlyphLine currentText = textRenderer.getText();
                if (currentText != null) {
                    int prevTextStart = currentText.start;
                    textRenderer.trimFirst();
                    int numOfTrimmedGlyphs = textRenderer.getText().start - prevTextStart;
                    totalNumberOfTrimmedGlyphs += numOfTrimmedGlyphs;
                }
                if (textRenderer.length() > 0) {
                    break;
                }
            }
        }
        return totalNumberOfTrimmedGlyphs;
    }

    private BaseDirection applyOtf() {
        BaseDirection baseDirection = (BaseDirection) getProperty(7);
        for (IRenderer renderer : this.childRenderers) {
            if (renderer instanceof TextRenderer) {
                ((TextRenderer) renderer).applyOtf();
                if (baseDirection == null || baseDirection == BaseDirection.NO_BIDI) {
                    baseDirection = (BaseDirection) renderer.getOwnProperty(7);
                }
            }
        }
        return baseDirection;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v35, types: [char] */
    /* JADX WARN: Type inference failed for: r0v36 */
    /* JADX WARN: Type inference failed for: r0v40, types: [int] */
    private void updateBidiLevels(int totalNumberOfTrimmedGlyphs, BaseDirection baseDirection) {
        if (totalNumberOfTrimmedGlyphs != 0 && this.levels != null) {
            this.levels = Arrays.copyOfRange(this.levels, totalNumberOfTrimmedGlyphs, this.levels.length);
        }
        if (this.levels == null && baseDirection != null && baseDirection != BaseDirection.NO_BIDI) {
            ArrayList unicodeIdsReorderingList = new ArrayList();
            boolean newLineFound = false;
            for (IRenderer child : this.childRenderers) {
                if (newLineFound) {
                    break;
                }
                if (child instanceof TextRenderer) {
                    GlyphLine text = ((TextRenderer) child).getText();
                    int i = text.start;
                    while (true) {
                        if (i < text.end) {
                            Glyph glyph = text.get(i);
                            if (TextUtil.isNewLine(glyph)) {
                                newLineFound = true;
                                break;
                            } else {
                                int unicode = glyph.hasValidUnicode() ? glyph.getUnicode() : glyph.getUnicodeChars()[0];
                                unicodeIdsReorderingList.add(Integer.valueOf(unicode));
                                i++;
                            }
                        }
                    }
                }
            }
            this.levels = unicodeIdsReorderingList.size() > 0 ? TypographyUtils.getBidiLevels(baseDirection, ArrayUtil.toIntArray(unicodeIdsReorderingList)) : null;
        }
    }

    private void resolveChildrenFonts() {
        List<IRenderer> newChildRenderers = new ArrayList<>(this.childRenderers.size());
        boolean updateChildRendrers = false;
        for (IRenderer child : this.childRenderers) {
            if (child instanceof TextRenderer) {
                if (((TextRenderer) child).resolveFonts(newChildRenderers)) {
                    updateChildRendrers = true;
                }
            } else {
                newChildRenderers.add(child);
            }
        }
        if (updateChildRendrers) {
            this.childRenderers = newChildRenderers;
        }
    }

    private float decreaseRelativeWidthByChildAdditionalWidth(IRenderer childRenderer, float normalizedChildWidth) {
        if (childRenderer instanceof AbstractRenderer) {
            Rectangle dummyRect = new Rectangle(normalizedChildWidth, 0.0f);
            ((AbstractRenderer) childRenderer).applyMargins(dummyRect, false);
            if (!isBorderBoxSizing(childRenderer)) {
                ((AbstractRenderer) childRenderer).applyBorderBox(dummyRect, false);
                ((AbstractRenderer) childRenderer).applyPaddings(dummyRect, false);
            }
            normalizedChildWidth = dummyRect.getWidth();
        }
        return normalizedChildWidth;
    }

    private boolean isInlineBlockChild(IRenderer child) {
        return (child instanceof BlockRenderer) || (child instanceof TableRenderer);
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/LineRenderer$RendererGlyph.class */
    static class RendererGlyph {
        public Glyph glyph;
        public TextRenderer renderer;

        public RendererGlyph(Glyph glyph, TextRenderer textRenderer) {
            this.glyph = glyph;
            this.renderer = textRenderer;
        }
    }
}
