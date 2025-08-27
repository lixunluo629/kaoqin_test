package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.LineLayoutContext;
import com.itextpdf.layout.layout.LineLayoutResult;
import com.itextpdf.layout.layout.MinMaxWidthLayoutResult;
import com.itextpdf.layout.margincollapse.MarginsCollapseHandler;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.FloatPropertyValue;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/ParagraphRenderer.class */
public class ParagraphRenderer extends BlockRenderer {
    protected float previousDescent;
    protected List<LineRenderer> lines;

    public ParagraphRenderer(Paragraph modelElement) {
        super(modelElement);
        this.previousDescent = 0.0f;
        this.lines = null;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        List<Rectangle> areas;
        boolean wasParentsHeightClipped = layoutContext.isClippedHeight();
        int pageNumber = layoutContext.getArea().getPageNumber();
        boolean anythingPlaced = false;
        boolean firstLineInBox = true;
        LineRenderer currentRenderer = (LineRenderer) new LineRenderer().setParent(this);
        Rectangle parentBBox = layoutContext.getArea().getBBox().mo825clone();
        MarginsCollapseHandler marginsCollapseHandler = null;
        boolean marginsCollapsingEnabled = Boolean.TRUE.equals(getPropertyAsBoolean(89));
        if (marginsCollapsingEnabled) {
            marginsCollapseHandler = new MarginsCollapseHandler(this, layoutContext.getMarginsCollapseInfo());
        }
        OverflowPropertyValue overflowX = (OverflowPropertyValue) getProperty(103);
        Boolean nowrapProp = getPropertyAsBoolean(118);
        currentRenderer.setProperty(118, nowrapProp);
        boolean notAllKidsAreFloats = false;
        List<Rectangle> floatRendererAreas = layoutContext.getFloatRendererAreas();
        FloatPropertyValue floatPropertyValue = (FloatPropertyValue) getProperty(99);
        float clearHeightCorrection = FloatingHelper.calculateClearHeightCorrection(this, floatRendererAreas, parentBBox);
        FloatingHelper.applyClearance(parentBBox, marginsCollapseHandler, clearHeightCorrection, FloatingHelper.isRendererFloating(this));
        Float blockWidth = retrieveWidth(parentBBox.getWidth());
        if (FloatingHelper.isRendererFloating(this, floatPropertyValue)) {
            blockWidth = FloatingHelper.adjustFloatedBlockLayoutBox(this, parentBBox, blockWidth, floatRendererAreas, floatPropertyValue, overflowX);
            floatRendererAreas = new ArrayList();
        }
        if (0 == this.childRenderers.size()) {
            anythingPlaced = true;
            currentRenderer = null;
        }
        boolean isPositioned = isPositioned();
        Float rotation = getPropertyAsFloat(55);
        Float blockMaxHeight = retrieveMaxHeight();
        OverflowPropertyValue overflowY = ((null == blockMaxHeight || blockMaxHeight.floatValue() > parentBBox.getHeight()) && !wasParentsHeightClipped) ? OverflowPropertyValue.FIT : (OverflowPropertyValue) getProperty(104);
        if (rotation != null || isFixedLayout()) {
            parentBBox.moveDown(1000000.0f - parentBBox.getHeight()).setHeight(1000000.0f);
        }
        if (rotation != null && !FloatingHelper.isRendererFloating(this)) {
            blockWidth = RotationUtils.retrieveRotatedLayoutWidth(parentBBox.getWidth(), this);
        }
        if (marginsCollapsingEnabled) {
            marginsCollapseHandler.startMarginsCollapse(parentBBox);
        }
        Border[] borders = getBorders();
        UnitValue[] paddings = getPaddings();
        float additionalWidth = applyBordersPaddingsMargins(parentBBox, borders, paddings);
        applyWidth(parentBBox, blockWidth, overflowX);
        boolean wasHeightClipped = applyMaxHeight(parentBBox, blockMaxHeight, marginsCollapseHandler, false, wasParentsHeightClipped, overflowY);
        MinMaxWidth minMaxWidth = new MinMaxWidth(additionalWidth);
        AbstractWidthHandler widthHandler = new MaxMaxWidthHandler(minMaxWidth);
        if (isPositioned) {
            areas = Collections.singletonList(parentBBox);
        } else {
            areas = initElementAreas(new LayoutArea(pageNumber, parentBBox));
        }
        this.occupiedArea = new LayoutArea(pageNumber, new Rectangle(parentBBox.getX(), parentBBox.getY() + parentBBox.getHeight(), parentBBox.getWidth(), 0.0f));
        shrinkOccupiedAreaForAbsolutePosition();
        int currentAreaPos = 0;
        Rectangle layoutBox = areas.get(0).mo825clone();
        this.lines = new ArrayList();
        for (IRenderer child : this.childRenderers) {
            notAllKidsAreFloats = notAllKidsAreFloats || !FloatingHelper.isRendererFloating(child);
            currentRenderer.addChild(child);
        }
        float lastYLine = layoutBox.getY() + layoutBox.getHeight();
        Leading leading = (Leading) getProperty(33);
        float lastLineBottomLeadingIndent = 0.0f;
        boolean onlyOverflowedFloatsLeft = false;
        List<IRenderer> inlineFloatsOverflowedToNextPage = new ArrayList<>();
        boolean floatOverflowedToNextPageWithNothing = false;
        Set<Rectangle> nonChildFloatingRendererAreas = new HashSet<>(floatRendererAreas);
        if (marginsCollapsingEnabled && this.childRenderers.size() > 0) {
            marginsCollapseHandler.startChildMarginsHandling(null, layoutBox);
        }
        boolean includeFloatsInOccupiedArea = BlockFormattingContextUtil.isRendererCreateBfc(this);
        while (currentRenderer != null) {
            currentRenderer.setProperty(67, getPropertyAsFloat(67));
            currentRenderer.setProperty(69, getProperty(69));
            float lineIndent = anythingPlaced ? 0.0f : getPropertyAsFloat(18).floatValue();
            Rectangle childLayoutBox = new Rectangle(layoutBox.getX(), layoutBox.getY(), layoutBox.getWidth(), layoutBox.getHeight());
            currentRenderer.setProperty(103, overflowX);
            currentRenderer.setProperty(104, overflowY);
            LineLayoutContext lineLayoutContext = new LineLayoutContext(new LayoutArea(pageNumber, childLayoutBox), null, floatRendererAreas, wasHeightClipped || wasParentsHeightClipped).setTextIndent(lineIndent).setFloatOverflowedToNextPageWithNothing(floatOverflowedToNextPageWithNothing);
            LineLayoutResult result = (LineLayoutResult) ((LineRenderer) currentRenderer.setParent(this)).layout(lineLayoutContext);
            if (result.getStatus() == 3) {
                Float lineShiftUnderFloats = FloatingHelper.calculateLineShiftUnderFloats(floatRendererAreas, layoutBox);
                if (lineShiftUnderFloats != null) {
                    layoutBox.decreaseHeight(lineShiftUnderFloats.floatValue());
                    firstLineInBox = true;
                } else {
                    boolean allRemainingKidsAreFloats = !currentRenderer.childRenderers.isEmpty();
                    for (IRenderer renderer : currentRenderer.childRenderers) {
                        allRemainingKidsAreFloats = allRemainingKidsAreFloats && FloatingHelper.isRendererFloating(renderer);
                    }
                    if (allRemainingKidsAreFloats) {
                        onlyOverflowedFloatsLeft = true;
                    }
                }
            }
            floatOverflowedToNextPageWithNothing = lineLayoutContext.isFloatOverflowedToNextPageWithNothing();
            if (result.getFloatsOverflowedToNextPage() != null) {
                inlineFloatsOverflowedToNextPage.addAll(result.getFloatsOverflowedToNextPage());
            }
            float minChildWidth = 0.0f;
            float maxChildWidth = 0.0f;
            if (result instanceof MinMaxWidthLayoutResult) {
                minChildWidth = result.getMinMaxWidth().getMinWidth();
                maxChildWidth = result.getMinMaxWidth().getMaxWidth();
            }
            widthHandler.updateMinChildWidth(minChildWidth);
            widthHandler.updateMaxChildWidth(maxChildWidth);
            LineRenderer processedRenderer = null;
            if (result.getStatus() == 1) {
                processedRenderer = currentRenderer;
            } else if (result.getStatus() == 2) {
                processedRenderer = (LineRenderer) result.getSplitRenderer();
            }
            if (onlyOverflowedFloatsLeft) {
                processedRenderer = null;
            }
            TextAlignment textAlignment = (TextAlignment) getProperty(70, TextAlignment.LEFT);
            applyTextAlignment(textAlignment, result, processedRenderer, layoutBox, floatRendererAreas, onlyOverflowedFloatsLeft, lineIndent);
            boolean lineHasContent = processedRenderer != null && processedRenderer.getOccupiedArea().getBBox().getHeight() > 0.0f;
            boolean doesNotFit = processedRenderer == null;
            float deltaY = 0.0f;
            if (!doesNotFit) {
                if (lineHasContent) {
                    float indentFromLastLine = ((this.previousDescent - lastLineBottomLeadingIndent) - (leading != null ? processedRenderer.getTopLeadingIndent(leading) : 0.0f)) - processedRenderer.getMaxAscent();
                    if (processedRenderer != null && processedRenderer.containsImage()) {
                        indentFromLastLine += this.previousDescent;
                    }
                    deltaY = (lastYLine + indentFromLastLine) - processedRenderer.getYLine();
                    lastLineBottomLeadingIndent = leading != null ? processedRenderer.getBottomLeadingIndent(leading) : 0.0f;
                    if (lastLineBottomLeadingIndent < 0.0f && processedRenderer.containsImage()) {
                        lastLineBottomLeadingIndent = 0.0f;
                    }
                }
                if (firstLineInBox) {
                    deltaY = (processedRenderer == null || leading == null) ? 0.0f : -processedRenderer.getTopLeadingIndent(leading);
                }
                doesNotFit = leading != null && processedRenderer.getOccupiedArea().getBBox().getY() + deltaY < layoutBox.getY();
            }
            if (doesNotFit && (null == processedRenderer || isOverflowFit(overflowY))) {
                if (currentAreaPos + 1 < areas.size()) {
                    currentAreaPos++;
                    layoutBox = areas.get(currentAreaPos).mo825clone();
                    lastYLine = layoutBox.getY() + layoutBox.getHeight();
                    firstLineInBox = true;
                } else {
                    boolean keepTogether = isKeepTogether();
                    if (keepTogether) {
                        return new MinMaxWidthLayoutResult(3, null, null, this, null == result.getCauseOfNothing() ? this : result.getCauseOfNothing());
                    }
                    if (marginsCollapsingEnabled && anythingPlaced && notAllKidsAreFloats) {
                        marginsCollapseHandler.endChildMarginsHandling(layoutBox);
                    }
                    boolean includeFloatsInOccupiedAreaOnSplit = !onlyOverflowedFloatsLeft || includeFloatsInOccupiedArea;
                    if (includeFloatsInOccupiedAreaOnSplit) {
                        FloatingHelper.includeChildFloatsInOccupiedArea(floatRendererAreas, this, nonChildFloatingRendererAreas);
                        fixOccupiedAreaIfOverflowedX(overflowX, layoutBox);
                    }
                    if (marginsCollapsingEnabled) {
                        marginsCollapseHandler.endMarginsCollapse(layoutBox);
                    }
                    boolean minHeightOverflowed = false;
                    if (!includeFloatsInOccupiedAreaOnSplit) {
                        AbstractRenderer minHeightOverflow = applyMinHeight(overflowY, layoutBox);
                        minHeightOverflowed = minHeightOverflow != null;
                        applyVerticalAlignment();
                    }
                    ParagraphRenderer[] split = split();
                    split[0].lines = this.lines;
                    for (LineRenderer line : this.lines) {
                        split[0].childRenderers.addAll(line.getChildRenderers());
                    }
                    split[1].childRenderers.addAll(inlineFloatsOverflowedToNextPage);
                    if (processedRenderer != null) {
                        split[1].childRenderers.addAll(processedRenderer.getChildRenderers());
                    }
                    if (result.getOverflowRenderer() != null) {
                        split[1].childRenderers.addAll(result.getOverflowRenderer().getChildRenderers());
                    }
                    if (onlyOverflowedFloatsLeft && !includeFloatsInOccupiedArea && !minHeightOverflowed) {
                        FloatingHelper.removeParentArtifactsOnPageSplitIfOnlyFloatsOverflow(split[1]);
                    }
                    float usedHeight = this.occupiedArea.getBBox().getHeight();
                    if (!includeFloatsInOccupiedAreaOnSplit) {
                        Rectangle commonRectangle = Rectangle.getCommonRectangle(layoutBox, this.occupiedArea.getBBox());
                        usedHeight = commonRectangle.getHeight();
                    }
                    updateHeightsOnSplit(usedHeight, wasHeightClipped, this, split[1], includeFloatsInOccupiedAreaOnSplit);
                    correctFixedLayout(layoutBox);
                    applyPaddings(this.occupiedArea.getBBox(), paddings, true);
                    applyBorderBox(this.occupiedArea.getBBox(), borders, true);
                    applyMargins(this.occupiedArea.getBBox(), true);
                    applyAbsolutePositionIfNeeded(layoutContext);
                    LayoutArea editedArea = FloatingHelper.adjustResultOccupiedAreaForFloatAndClear(this, layoutContext.getFloatRendererAreas(), layoutContext.getArea().getBBox(), clearHeightCorrection, marginsCollapsingEnabled);
                    if (wasHeightClipped) {
                        return new MinMaxWidthLayoutResult(1, editedArea, split[0], null).setMinMaxWidth(minMaxWidth);
                    }
                    if (anythingPlaced) {
                        return new MinMaxWidthLayoutResult(2, editedArea, split[0], split[1]).setMinMaxWidth(minMaxWidth);
                    }
                    if (Boolean.TRUE.equals(getPropertyAsBoolean(26))) {
                        this.occupiedArea.setBBox(Rectangle.getCommonRectangle(this.occupiedArea.getBBox(), currentRenderer.getOccupiedArea().getBBox()));
                        fixOccupiedAreaIfOverflowedX(overflowX, layoutBox);
                        this.parent.setProperty(25, true);
                        this.lines.add(currentRenderer);
                        if (2 == result.getStatus()) {
                            IRenderer childNotRendered = result.getCauseOfNothing();
                            int firstNotRendered = currentRenderer.childRenderers.indexOf(childNotRendered);
                            currentRenderer.childRenderers.retainAll(currentRenderer.childRenderers.subList(0, firstNotRendered));
                            split[1].childRenderers.removeAll(split[1].childRenderers.subList(0, firstNotRendered));
                            return new MinMaxWidthLayoutResult(2, editedArea, this, split[1], null).setMinMaxWidth(minMaxWidth);
                        }
                        return new MinMaxWidthLayoutResult(1, editedArea, null, null, this).setMinMaxWidth(minMaxWidth);
                    }
                    return new MinMaxWidthLayoutResult(3, null, null, this, null == result.getCauseOfNothing() ? this : result.getCauseOfNothing());
                }
            } else {
                if (leading != null) {
                    processedRenderer.applyLeading(deltaY);
                    if (lineHasContent) {
                        lastYLine = processedRenderer.getYLine();
                    }
                }
                if (lineHasContent) {
                    this.occupiedArea.setBBox(Rectangle.getCommonRectangle(this.occupiedArea.getBBox(), processedRenderer.getOccupiedArea().getBBox()));
                    fixOccupiedAreaIfOverflowedX(overflowX, layoutBox);
                }
                firstLineInBox = false;
                layoutBox.setHeight(processedRenderer.getOccupiedArea().getBBox().getY() - layoutBox.getY());
                this.lines.add(processedRenderer);
                anythingPlaced = true;
                currentRenderer = (LineRenderer) result.getOverflowRenderer();
                this.previousDescent = processedRenderer.getMaxDescent();
                if (!inlineFloatsOverflowedToNextPage.isEmpty() && result.getOverflowRenderer() == null) {
                    onlyOverflowedFloatsLeft = true;
                    currentRenderer = new LineRenderer();
                }
            }
        }
        float moveDown = lastLineBottomLeadingIndent;
        if (isOverflowFit(overflowY) && moveDown > this.occupiedArea.getBBox().getY() - layoutBox.getY()) {
            moveDown = this.occupiedArea.getBBox().getY() - layoutBox.getY();
        }
        this.occupiedArea.getBBox().moveDown(moveDown);
        this.occupiedArea.getBBox().setHeight(this.occupiedArea.getBBox().getHeight() + moveDown);
        if (marginsCollapsingEnabled && this.childRenderers.size() > 0 && notAllKidsAreFloats) {
            marginsCollapseHandler.endChildMarginsHandling(layoutBox);
        }
        if (includeFloatsInOccupiedArea) {
            FloatingHelper.includeChildFloatsInOccupiedArea(floatRendererAreas, this, nonChildFloatingRendererAreas);
            fixOccupiedAreaIfOverflowedX(overflowX, layoutBox);
        }
        if (wasHeightClipped) {
            fixOccupiedAreaIfOverflowedY(overflowY, layoutBox);
        }
        if (marginsCollapsingEnabled) {
            marginsCollapseHandler.endMarginsCollapse(layoutBox);
        }
        AbstractRenderer overflowRenderer = applyMinHeight(overflowY, layoutBox);
        if (overflowRenderer != null && isKeepTogether()) {
            return new LayoutResult(3, null, null, this, this);
        }
        correctFixedLayout(layoutBox);
        applyPaddings(this.occupiedArea.getBBox(), paddings, true);
        applyBorderBox(this.occupiedArea.getBBox(), borders, true);
        applyMargins(this.occupiedArea.getBBox(), true);
        applyAbsolutePositionIfNeeded(layoutContext);
        if (rotation != null) {
            applyRotationLayout(layoutContext.getArea().getBBox().mo825clone());
            if (isNotFittingLayoutArea(layoutContext.getArea())) {
                if (isNotFittingWidth(layoutContext.getArea()) && !isNotFittingHeight(layoutContext.getArea())) {
                    LoggerFactory.getLogger(getClass()).warn(MessageFormatUtil.format(LogMessageConstant.ELEMENT_DOES_NOT_FIT_AREA, "It fits by height so it will be forced placed"));
                } else if (!Boolean.TRUE.equals(getPropertyAsBoolean(26))) {
                    return new MinMaxWidthLayoutResult(3, null, null, this, this);
                }
            }
        }
        applyVerticalAlignment();
        FloatingHelper.removeFloatsAboveRendererBottom(floatRendererAreas, this);
        LayoutArea editedArea2 = FloatingHelper.adjustResultOccupiedAreaForFloatAndClear(this, layoutContext.getFloatRendererAreas(), layoutContext.getArea().getBBox(), clearHeightCorrection, marginsCollapsingEnabled);
        if (null == overflowRenderer) {
            return new MinMaxWidthLayoutResult(1, editedArea2, null, null, null).setMinMaxWidth(minMaxWidth);
        }
        return new MinMaxWidthLayoutResult(2, editedArea2, this, overflowRenderer, null).setMinMaxWidth(minMaxWidth);
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new ParagraphRenderer((Paragraph) this.modelElement);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int i) {
        if ((i == 46 || i == 43) && (this.parent instanceof CellRenderer)) {
            return (T1) UnitValue.createPointValue(0.0f);
        }
        return (T1) super.getDefaultProperty(i);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.lines != null && this.lines.size() > 0) {
            for (int i = 0; i < this.lines.size(); i++) {
                if (i > 0) {
                    sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                sb.append(this.lines.get(i).toString());
            }
        } else {
            for (IRenderer renderer : this.childRenderers) {
                sb.append(renderer.toString());
            }
        }
        return sb.toString();
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawChildren(DrawContext drawContext) {
        if (this.lines != null) {
            for (LineRenderer line : this.lines) {
                line.draw(drawContext);
            }
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void move(float dxRight, float dyUp) {
        this.occupiedArea.getBBox().moveRight(dxRight);
        this.occupiedArea.getBBox().moveUp(dyUp);
        if (null != this.lines) {
            for (LineRenderer line : this.lines) {
                line.move(dxRight, dyUp);
            }
        }
    }

    public List<LineRenderer> getLines() {
        return this.lines;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float getFirstYLineRecursively() {
        if (this.lines == null || this.lines.size() == 0) {
            return null;
        }
        return this.lines.get(0).getFirstYLineRecursively();
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float getLastYLineRecursively() {
        if (!allowLastYLineRecursiveExtraction() || this.lines == null || this.lines.size() == 0) {
            return null;
        }
        for (int i = this.lines.size() - 1; i >= 0; i--) {
            Float yLine = this.lines.get(i).getLastYLineRecursively();
            if (yLine != null) {
                return yLine;
            }
        }
        return null;
    }

    private ParagraphRenderer createOverflowRenderer() {
        return (ParagraphRenderer) getNextRenderer();
    }

    private ParagraphRenderer createSplitRenderer() {
        return (ParagraphRenderer) getNextRenderer();
    }

    protected ParagraphRenderer createOverflowRenderer(IRenderer parent) {
        ParagraphRenderer overflowRenderer = createOverflowRenderer();
        overflowRenderer.parent = parent;
        fixOverflowRenderer(overflowRenderer);
        overflowRenderer.addAllProperties(getOwnProperties());
        return overflowRenderer;
    }

    protected ParagraphRenderer createSplitRenderer(IRenderer parent) {
        ParagraphRenderer splitRenderer = createSplitRenderer();
        splitRenderer.parent = parent;
        splitRenderer.addAllProperties(getOwnProperties());
        return splitRenderer;
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer
    protected AbstractRenderer createOverflowRenderer(int layoutResult) {
        return createOverflowRenderer(this.parent);
    }

    @Override // com.itextpdf.layout.renderer.BlockRenderer, com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        MinMaxWidth minMaxWidth = new MinMaxWidth();
        Float rotation = getPropertyAsFloat(55);
        if (!setMinMaxWidthBasedOnFixedWidth(minMaxWidth)) {
            Float minWidth = hasAbsoluteUnitValue(80) ? retrieveMinWidth(0.0f) : null;
            Float maxWidth = hasAbsoluteUnitValue(79) ? retrieveMaxWidth(0.0f) : null;
            if (minWidth == null || maxWidth == null) {
                boolean restoreRotation = hasOwnProperty(55);
                setProperty(55, null);
                MinMaxWidthLayoutResult result = (MinMaxWidthLayoutResult) layout(new LayoutContext(new LayoutArea(1, new Rectangle(MinMaxWidthUtils.getInfWidth(), 1000000.0f))));
                if (restoreRotation) {
                    setProperty(55, rotation);
                } else {
                    deleteOwnProperty(55);
                }
                minMaxWidth = result.getMinMaxWidth();
            }
            if (minWidth != null) {
                minMaxWidth.setChildrenMinWidth(minWidth.floatValue());
            }
            if (maxWidth != null) {
                minMaxWidth.setChildrenMaxWidth(maxWidth.floatValue());
            }
            if (minMaxWidth.getChildrenMinWidth() > minMaxWidth.getChildrenMaxWidth()) {
                minMaxWidth.setChildrenMaxWidth(minMaxWidth.getChildrenMaxWidth());
            }
        } else {
            minMaxWidth.setAdditionalWidth(calculateAdditionalWidth(this));
        }
        return rotation != null ? RotationUtils.countRotationMinMaxWidth(minMaxWidth, this) : minMaxWidth;
    }

    protected ParagraphRenderer[] split() {
        ParagraphRenderer splitRenderer = createSplitRenderer(this.parent);
        splitRenderer.occupiedArea = this.occupiedArea;
        splitRenderer.isLastRendererForModelElement = false;
        ParagraphRenderer overflowRenderer = createOverflowRenderer(this.parent);
        return new ParagraphRenderer[]{splitRenderer, overflowRenderer};
    }

    private void fixOverflowRenderer(ParagraphRenderer overflowRenderer) {
        float firstLineIndent = overflowRenderer.getPropertyAsFloat(18).floatValue();
        if (firstLineIndent != 0.0f) {
            overflowRenderer.setProperty(18, Float.valueOf(0.0f));
        }
    }

    private void alignStaticKids(LineRenderer renderer, float dxRight) {
        renderer.getOccupiedArea().getBBox().moveRight(dxRight);
        for (IRenderer childRenderer : renderer.getChildRenderers()) {
            if (!FloatingHelper.isRendererFloating(childRenderer)) {
                childRenderer.move(dxRight, 0.0f);
            }
        }
    }

    private void applyTextAlignment(TextAlignment textAlignment, LineLayoutResult result, LineRenderer processedRenderer, Rectangle layoutBox, List<Rectangle> floatRendererAreas, boolean onlyOverflowedFloatsLeft, float lineIndent) {
        if ((textAlignment == TextAlignment.JUSTIFIED && result.getStatus() == 2 && !result.isSplitForcedByNewline() && !onlyOverflowedFloatsLeft) || textAlignment == TextAlignment.JUSTIFIED_ALL) {
            if (processedRenderer != null) {
                Rectangle actualLineLayoutBox = layoutBox.mo825clone();
                FloatingHelper.adjustLineAreaAccordingToFloats(floatRendererAreas, actualLineLayoutBox);
                processedRenderer.justify(actualLineLayoutBox.getWidth() - lineIndent);
                return;
            }
            return;
        }
        if (textAlignment != TextAlignment.LEFT && processedRenderer != null) {
            Rectangle actualLineLayoutBox2 = layoutBox.mo825clone();
            FloatingHelper.adjustLineAreaAccordingToFloats(floatRendererAreas, actualLineLayoutBox2);
            float deltaX = Math.max(0.0f, (actualLineLayoutBox2.getWidth() - lineIndent) - processedRenderer.getOccupiedArea().getBBox().getWidth());
            switch (textAlignment) {
                case RIGHT:
                    alignStaticKids(processedRenderer, deltaX);
                    break;
                case CENTER:
                    alignStaticKids(processedRenderer, deltaX / 2.0f);
                    break;
                case JUSTIFIED:
                    if (BaseDirection.RIGHT_TO_LEFT.equals(getProperty(7))) {
                        alignStaticKids(processedRenderer, deltaX);
                        break;
                    }
                    break;
            }
        }
    }
}
