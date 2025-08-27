package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.margincollapse.MarginsCollapseHandler;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/BlockRenderer.class */
public abstract class BlockRenderer extends AbstractRenderer {
    protected BlockRenderer(IElement modelElement) {
        super(modelElement);
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x04dc  */
    @Override // com.itextpdf.layout.renderer.IRenderer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.itextpdf.layout.layout.LayoutResult layout(com.itextpdf.layout.layout.LayoutContext r12) {
        /*
            Method dump skipped, instructions count: 3024
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.layout.renderer.BlockRenderer.layout(com.itextpdf.layout.layout.LayoutContext):com.itextpdf.layout.layout.LayoutResult");
    }

    protected AbstractRenderer createSplitRenderer(int layoutResult) {
        AbstractRenderer splitRenderer = (AbstractRenderer) getNextRenderer();
        splitRenderer.parent = this.parent;
        splitRenderer.modelElement = this.modelElement;
        splitRenderer.occupiedArea = this.occupiedArea;
        splitRenderer.isLastRendererForModelElement = false;
        splitRenderer.addAllProperties(getOwnProperties());
        return splitRenderer;
    }

    protected AbstractRenderer createOverflowRenderer(int layoutResult) {
        AbstractRenderer overflowRenderer = (AbstractRenderer) getNextRenderer();
        overflowRenderer.parent = this.parent;
        overflowRenderer.modelElement = this.modelElement;
        overflowRenderer.addAllProperties(getOwnProperties());
        return overflowRenderer;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        Rectangle clippedArea;
        if (this.occupiedArea == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) BlockRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Drawing won't be performed."));
            return;
        }
        boolean isTagged = drawContext.isTaggingEnabled();
        LayoutTaggingHelper taggingHelper = null;
        if (isTagged) {
            taggingHelper = (LayoutTaggingHelper) getProperty(108);
            if (taggingHelper == null) {
                isTagged = false;
            } else {
                TagTreePointer tagPointer = taggingHelper.useAutoTaggingPointerAndRememberItsPosition(this);
                if (taggingHelper.createTag(this, tagPointer)) {
                    tagPointer.getProperties().addAttributes(0, AccessibleAttributesApplier.getListAttributes(this, tagPointer)).addAttributes(0, AccessibleAttributesApplier.getTableAttributes(this, tagPointer)).addAttributes(0, AccessibleAttributesApplier.getLayoutAttributes(this, tagPointer));
                }
            }
        }
        beginTransformationIfApplied(drawContext.getCanvas());
        applyDestinationsAndAnnotation(drawContext);
        boolean isRelativePosition = isRelativePosition();
        if (isRelativePosition) {
            applyRelativePositioningTranslation(false);
        }
        beginElementOpacityApplying(drawContext);
        beginRotationIfApplied(drawContext.getCanvas());
        boolean overflowXHidden = isOverflowProperty(OverflowPropertyValue.HIDDEN, 103);
        boolean overflowYHidden = isOverflowProperty(OverflowPropertyValue.HIDDEN, 104);
        boolean processOverflow = overflowXHidden || overflowYHidden;
        drawBackground(drawContext);
        drawBorder(drawContext);
        if (processOverflow) {
            drawContext.getCanvas().saveState();
            int pageNumber = this.occupiedArea.getPageNumber();
            if (pageNumber < 1 || pageNumber > drawContext.getDocument().getNumberOfPages()) {
                clippedArea = new Rectangle(-500000.0f, -500000.0f, 1000000.0f, 1000000.0f);
            } else {
                clippedArea = drawContext.getDocument().getPage(pageNumber).getPageSize();
            }
            Rectangle area = getBorderAreaBBox();
            if (overflowXHidden) {
                clippedArea.setX(area.getX()).setWidth(area.getWidth());
            }
            if (overflowYHidden) {
                clippedArea.setY(area.getY()).setHeight(area.getHeight());
            }
            drawContext.getCanvas().rectangle(clippedArea).clip().endPath();
        }
        drawChildren(drawContext);
        drawPositionedChildren(drawContext);
        if (processOverflow) {
            drawContext.getCanvas().restoreState();
        }
        endRotationIfApplied(drawContext.getCanvas());
        endElementOpacityApplying(drawContext);
        if (isRelativePosition) {
            applyRelativePositioningTranslation(true);
        }
        if (isTagged) {
            if (this.isLastRendererForModelElement) {
                taggingHelper.finishTaggingHint(this);
            }
            taggingHelper.restoreAutoTaggingPointerPosition(this);
        }
        this.flushed = true;
        endTransformationIfApplied(drawContext.getCanvas());
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public Rectangle getOccupiedAreaBBox() {
        Rectangle bBox = this.occupiedArea.getBBox().mo825clone();
        Float rotationAngle = (Float) getProperty(55);
        if (rotationAngle != null) {
            if (!hasOwnProperty(57) || !hasOwnProperty(56)) {
                Logger logger = LoggerFactory.getLogger((Class<?>) BlockRenderer.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.ROTATION_WAS_NOT_CORRECTLY_PROCESSED_FOR_RENDERER, getClass().getSimpleName()));
            } else {
                bBox.setWidth(getPropertyAsFloat(57).floatValue());
                bBox.setHeight(getPropertyAsFloat(56).floatValue());
            }
        }
        return bBox;
    }

    protected void applyVerticalAlignment() {
        VerticalAlignment verticalAlignment = (VerticalAlignment) getProperty(75);
        if (verticalAlignment == null || verticalAlignment == VerticalAlignment.TOP || this.childRenderers.isEmpty()) {
            return;
        }
        float lowestChildBottom = Float.MAX_VALUE;
        if (FloatingHelper.isRendererFloating(this) || (this instanceof CellRenderer)) {
            for (IRenderer child : this.childRenderers) {
                if (child.getOccupiedArea().getBBox().getBottom() < lowestChildBottom) {
                    lowestChildBottom = child.getOccupiedArea().getBBox().getBottom();
                }
            }
        } else {
            int lastChildIndex = this.childRenderers.size() - 1;
            while (true) {
                if (lastChildIndex < 0) {
                    break;
                }
                int i = lastChildIndex;
                lastChildIndex--;
                IRenderer child2 = this.childRenderers.get(i);
                if (!FloatingHelper.isRendererFloating(child2)) {
                    lowestChildBottom = child2.getOccupiedArea().getBBox().getBottom();
                    break;
                }
            }
        }
        if (lowestChildBottom == Float.MAX_VALUE) {
        }
        float deltaY = lowestChildBottom - getInnerAreaBBox().getY();
        if (deltaY < 0.0f) {
            return;
        }
        switch (verticalAlignment) {
            case BOTTOM:
                Iterator<IRenderer> it = this.childRenderers.iterator();
                while (it.hasNext()) {
                    it.next().move(0.0f, -deltaY);
                }
                break;
            case MIDDLE:
                Iterator<IRenderer> it2 = this.childRenderers.iterator();
                while (it2.hasNext()) {
                    it2.next().move(0.0f, (-deltaY) / 2.0f);
                }
                break;
        }
    }

    protected void applyRotationLayout(Rectangle layoutBox) {
        float angle = getPropertyAsFloat(55).floatValue();
        float x = this.occupiedArea.getBBox().getX();
        float y = this.occupiedArea.getBBox().getY();
        float height = this.occupiedArea.getBBox().getHeight();
        float width = this.occupiedArea.getBBox().getWidth();
        setProperty(57, Float.valueOf(width));
        setProperty(56, Float.valueOf(height));
        AffineTransform rotationTransform = new AffineTransform();
        if (isPositioned()) {
            Float rotationPointX = getPropertyAsFloat(58);
            Float rotationPointY = getPropertyAsFloat(59);
            if (rotationPointX == null || rotationPointY == null) {
                rotationPointX = Float.valueOf(x);
                rotationPointY = Float.valueOf(y);
            }
            rotationTransform.translate(rotationPointX.floatValue(), rotationPointY.floatValue());
            rotationTransform.rotate(angle);
            rotationTransform.translate(-rotationPointX.floatValue(), -rotationPointY.floatValue());
            Rectangle newBBox = calculateBBox(transformPoints(rectangleToPointsList(this.occupiedArea.getBBox()), rotationTransform));
            this.occupiedArea.getBBox().setWidth(newBBox.getWidth());
            this.occupiedArea.getBBox().setHeight(newBBox.getHeight());
            float occupiedAreaShiftX = newBBox.getX() - x;
            float occupiedAreaShiftY = newBBox.getY() - y;
            move(occupiedAreaShiftX, occupiedAreaShiftY);
            return;
        }
        List<Point> rotatedPoints = transformPoints(rectangleToPointsList(this.occupiedArea.getBBox()), AffineTransform.getRotateInstance(angle));
        float[] shift = calculateShiftToPositionBBoxOfPointsAt(x, y + height, rotatedPoints);
        for (Point point : rotatedPoints) {
            point.setLocation(point.getX() + shift[0], point.getY() + shift[1]);
        }
        Rectangle newBBox2 = calculateBBox(rotatedPoints);
        this.occupiedArea.getBBox().setWidth(newBBox2.getWidth());
        this.occupiedArea.getBBox().setHeight(newBBox2.getHeight());
        float heightDiff = height - newBBox2.getHeight();
        move(0.0f, heightDiff);
    }

    protected AffineTransform createRotationTransformInsideOccupiedArea() {
        Float angle = (Float) getProperty(55);
        AffineTransform rotationTransform = AffineTransform.getRotateInstance(angle.floatValue());
        Rectangle contentBox = getOccupiedAreaBBox();
        List<Point> rotatedContentBoxPoints = transformPoints(rectangleToPointsList(contentBox), rotationTransform);
        float[] shift = calculateShiftToPositionBBoxOfPointsAt(this.occupiedArea.getBBox().getLeft(), this.occupiedArea.getBBox().getTop(), rotatedContentBoxPoints);
        rotationTransform.preConcatenate(AffineTransform.getTranslateInstance(shift[0], shift[1]));
        return rotationTransform;
    }

    protected void beginRotationIfApplied(PdfCanvas canvas) {
        Float angle = getPropertyAsFloat(55);
        if (angle != null) {
            if (!hasOwnProperty(56)) {
                Logger logger = LoggerFactory.getLogger((Class<?>) BlockRenderer.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.ROTATION_WAS_NOT_CORRECTLY_PROCESSED_FOR_RENDERER, getClass().getSimpleName()));
            } else {
                AffineTransform transform = createRotationTransformInsideOccupiedArea();
                canvas.saveState().concatMatrix(transform);
            }
        }
    }

    protected void endRotationIfApplied(PdfCanvas canvas) {
        Float angle = getPropertyAsFloat(55);
        if (angle != null && hasOwnProperty(56)) {
            canvas.restoreState();
        }
    }

    void correctFixedLayout(Rectangle layoutBox) {
        if (isFixedLayout()) {
            float y = getPropertyAsFloat(14).floatValue();
            move(0.0f, y - this.occupiedArea.getBBox().getY());
        }
    }

    void applyWidth(Rectangle parentBBox, Float blockWidth, OverflowPropertyValue overflowX) {
        Float rotation = getPropertyAsFloat(55);
        if (blockWidth != null && (blockWidth.floatValue() < parentBBox.getWidth() || isPositioned() || rotation != null || !isOverflowFit(overflowX))) {
            parentBBox.setWidth(blockWidth.floatValue());
            return;
        }
        Float minWidth = retrieveMinWidth(parentBBox.getWidth());
        if (minWidth != null && minWidth.floatValue() > parentBBox.getWidth()) {
            parentBBox.setWidth(minWidth.floatValue());
        }
    }

    boolean applyMaxHeight(Rectangle parentBBox, Float blockMaxHeight, MarginsCollapseHandler marginsCollapseHandler, boolean isCellRenderer, boolean wasParentsHeightClipped, OverflowPropertyValue overflowY) {
        if (null == blockMaxHeight) {
            return false;
        }
        if (blockMaxHeight.floatValue() >= parentBBox.getHeight() && isOverflowFit(overflowY)) {
            return false;
        }
        boolean wasHeightClipped = false;
        if (blockMaxHeight.floatValue() <= parentBBox.getHeight()) {
            wasHeightClipped = true;
        }
        float heightDelta = parentBBox.getHeight() - blockMaxHeight.floatValue();
        if (marginsCollapseHandler != null && !isCellRenderer) {
            marginsCollapseHandler.processFixedHeightAdjustment(heightDelta);
        }
        parentBBox.moveUp(heightDelta).setHeight(blockMaxHeight.floatValue());
        return wasHeightClipped;
    }

    AbstractRenderer applyMinHeight(OverflowPropertyValue overflowY, Rectangle layoutBox) {
        AbstractRenderer overflowRenderer = null;
        Float blockMinHeight = retrieveMinHeight();
        if (!Boolean.TRUE.equals(getPropertyAsBoolean(26)) && null != blockMinHeight && blockMinHeight.floatValue() > this.occupiedArea.getBBox().getHeight()) {
            float blockBottom = this.occupiedArea.getBBox().getBottom() - (blockMinHeight.floatValue() - this.occupiedArea.getBBox().getHeight());
            if (!isFixedLayout() && isOverflowFit(overflowY) && blockBottom < layoutBox.getBottom()) {
                float hDelta = this.occupiedArea.getBBox().getBottom() - layoutBox.getBottom();
                this.occupiedArea.getBBox().increaseHeight(hDelta).setY(layoutBox.getBottom());
                if (this.occupiedArea.getBBox().getHeight() < 0.0f) {
                    this.occupiedArea.getBBox().setHeight(0.0f);
                }
                this.isLastRendererForModelElement = false;
                overflowRenderer = createOverflowRenderer(2);
                overflowRenderer.updateMinHeight(UnitValue.createPointValue(blockMinHeight.floatValue() - this.occupiedArea.getBBox().getHeight()));
                if (hasProperty(27)) {
                    overflowRenderer.updateHeight(UnitValue.createPointValue(retrieveHeight().floatValue() - this.occupiedArea.getBBox().getHeight()));
                }
            } else {
                this.occupiedArea.getBBox().setY(blockBottom).setHeight(blockMinHeight.floatValue());
            }
        }
        return overflowRenderer;
    }

    void fixOccupiedAreaIfOverflowedX(OverflowPropertyValue overflowX, Rectangle layoutBox) {
        if (isOverflowFit(overflowX)) {
            return;
        }
        if (this.occupiedArea.getBBox().getWidth() > layoutBox.getWidth() || this.occupiedArea.getBBox().getLeft() < layoutBox.getLeft()) {
            this.occupiedArea.getBBox().setX(layoutBox.getX()).setWidth(layoutBox.getWidth());
        }
    }

    void fixOccupiedAreaIfOverflowedY(OverflowPropertyValue overflowY, Rectangle layoutBox) {
        if (!isOverflowFit(overflowY) && this.occupiedArea.getBBox().getBottom() < layoutBox.getBottom()) {
            float difference = layoutBox.getBottom() - this.occupiedArea.getBBox().getBottom();
            this.occupiedArea.getBBox().moveUp(difference).decreaseHeight(difference);
        }
    }

    protected float applyBordersPaddingsMargins(Rectangle parentBBox, Border[] borders, UnitValue[] paddings) {
        float parentWidth = parentBBox.getWidth();
        applyMargins(parentBBox, false);
        applyBorderBox(parentBBox, borders, false);
        if (isFixedLayout()) {
            parentBBox.setX(getPropertyAsFloat(34).floatValue());
        }
        applyPaddings(parentBBox, paddings, false);
        return parentWidth - parentBBox.getWidth();
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        MinMaxWidth childMinMaxWidth;
        MinMaxWidth minMaxWidth = new MinMaxWidth(calculateAdditionalWidth(this));
        if (!setMinMaxWidthBasedOnFixedWidth(minMaxWidth)) {
            Float minWidth = hasAbsoluteUnitValue(80) ? retrieveMinWidth(0.0f) : null;
            Float maxWidth = hasAbsoluteUnitValue(79) ? retrieveMaxWidth(0.0f) : null;
            if (minWidth == null || maxWidth == null) {
                AbstractWidthHandler handler = new MaxMaxWidthHandler(minMaxWidth);
                int epsilonNum = 0;
                int curEpsNum = 0;
                float previousFloatingChildWidth = 0.0f;
                for (IRenderer childRenderer : this.childRenderers) {
                    childRenderer.setParent(this);
                    if (childRenderer instanceof AbstractRenderer) {
                        childMinMaxWidth = ((AbstractRenderer) childRenderer).getMinMaxWidth();
                    } else {
                        childMinMaxWidth = MinMaxWidthUtils.countDefaultMinMaxWidth(childRenderer);
                    }
                    handler.updateMaxChildWidth(childMinMaxWidth.getMaxWidth() + (FloatingHelper.isRendererFloating(childRenderer) ? previousFloatingChildWidth : 0.0f));
                    handler.updateMinChildWidth(childMinMaxWidth.getMinWidth());
                    previousFloatingChildWidth = FloatingHelper.isRendererFloating(childRenderer) ? previousFloatingChildWidth + childMinMaxWidth.getMaxWidth() : 0.0f;
                    if (FloatingHelper.isRendererFloating(childRenderer)) {
                        curEpsNum++;
                    } else {
                        epsilonNum = Math.max(epsilonNum, curEpsNum);
                        curEpsNum = 0;
                    }
                }
                int epsilonNum2 = Math.max(epsilonNum, curEpsNum);
                handler.minMaxWidth.setChildrenMaxWidth(handler.minMaxWidth.getChildrenMaxWidth() + (epsilonNum2 * 1.0E-4f));
                handler.minMaxWidth.setChildrenMinWidth(handler.minMaxWidth.getChildrenMinWidth() + (epsilonNum2 * 1.0E-4f));
            }
            if (minWidth != null) {
                minMaxWidth.setChildrenMinWidth(minWidth.floatValue());
            }
            if (maxWidth != null) {
                minMaxWidth.setChildrenMaxWidth(maxWidth.floatValue());
            } else if (minMaxWidth.getChildrenMinWidth() > minMaxWidth.getChildrenMaxWidth()) {
                minMaxWidth.setChildrenMaxWidth(minMaxWidth.getChildrenMinWidth());
            }
        }
        if (getPropertyAsFloat(55) != null) {
            return RotationUtils.countRotationMinMaxWidth(minMaxWidth, this);
        }
        return minMaxWidth;
    }

    private AbstractRenderer[] createSplitAndOverflowRenderers(int childPos, int layoutStatus, LayoutResult childResult, Map<Integer, IRenderer> waitingFloatsSplitRenderers, List<IRenderer> waitingOverflowFloatRenderers) {
        AbstractRenderer splitRenderer = createSplitRenderer(layoutStatus);
        splitRenderer.childRenderers = new ArrayList(this.childRenderers.subList(0, childPos));
        if (childResult.getStatus() == 2 && childResult.getSplitRenderer() != null) {
            splitRenderer.childRenderers.add(childResult.getSplitRenderer());
        }
        replaceSplitRendererKidFloats(waitingFloatsSplitRenderers, splitRenderer);
        for (IRenderer renderer : splitRenderer.childRenderers) {
            renderer.setParent(splitRenderer);
        }
        AbstractRenderer overflowRenderer = createOverflowRenderer(layoutStatus);
        overflowRenderer.childRenderers.addAll(waitingOverflowFloatRenderers);
        if (childResult.getOverflowRenderer() != null) {
            overflowRenderer.childRenderers.add(childResult.getOverflowRenderer());
        }
        overflowRenderer.childRenderers.addAll(this.childRenderers.subList(childPos + 1, this.childRenderers.size()));
        if (childResult.getStatus() == 2) {
            overflowRenderer.deleteOwnProperty(26);
        }
        return new AbstractRenderer[]{splitRenderer, overflowRenderer};
    }

    private void replaceSplitRendererKidFloats(Map<Integer, IRenderer> waitingFloatsSplitRenderers, IRenderer splitRenderer) {
        for (Map.Entry<Integer, IRenderer> waitingSplitRenderer : waitingFloatsSplitRenderers.entrySet()) {
            if (waitingSplitRenderer.getValue() != null) {
                splitRenderer.getChildRenderers().set(waitingSplitRenderer.getKey().intValue(), waitingSplitRenderer.getValue());
            } else {
                splitRenderer.getChildRenderers().set(waitingSplitRenderer.getKey().intValue(), null);
            }
        }
        for (int i = splitRenderer.getChildRenderers().size() - 1; i >= 0; i--) {
            if (splitRenderer.getChildRenderers().get(i) == null) {
                splitRenderer.getChildRenderers().remove(i);
            }
        }
    }

    private List<Point> clipPolygon(List<Point> points, Point clipLineBeg, Point clipLineEnd) {
        List<Point> filteredPoints = new ArrayList<>();
        boolean prevOnRightSide = false;
        Point filteringPoint = points.get(0);
        if (checkPointSide(filteringPoint, clipLineBeg, clipLineEnd) >= 0) {
            filteredPoints.add(filteringPoint);
            prevOnRightSide = true;
        }
        Point prevPoint = filteringPoint;
        for (int i = 1; i < points.size() + 1; i++) {
            Point filteringPoint2 = points.get(i % points.size());
            if (checkPointSide(filteringPoint2, clipLineBeg, clipLineEnd) >= 0) {
                if (!prevOnRightSide) {
                    filteredPoints.add(getIntersectionPoint(prevPoint, filteringPoint2, clipLineBeg, clipLineEnd));
                }
                filteredPoints.add(filteringPoint2);
                prevOnRightSide = true;
            } else if (prevOnRightSide) {
                filteredPoints.add(getIntersectionPoint(prevPoint, filteringPoint2, clipLineBeg, clipLineEnd));
            }
            prevPoint = filteringPoint2;
        }
        return filteredPoints;
    }

    private int checkPointSide(Point filteredPoint, Point clipLineBeg, Point clipLineEnd) {
        double x1 = filteredPoint.getX() - clipLineBeg.getX();
        double y2 = clipLineEnd.getY() - clipLineBeg.getY();
        double x2 = clipLineEnd.getX() - clipLineBeg.getX();
        double y1 = filteredPoint.getY() - clipLineBeg.getY();
        double sgn = (x1 * y2) - (x2 * y1);
        if (Math.abs(sgn) < 0.001d) {
            return 0;
        }
        if (sgn > 0.0d) {
            return 1;
        }
        return sgn < 0.0d ? -1 : 0;
    }

    private Point getIntersectionPoint(Point lineBeg, Point lineEnd, Point clipLineBeg, Point clipLineEnd) {
        double A1 = lineBeg.getY() - lineEnd.getY();
        double A2 = clipLineBeg.getY() - clipLineEnd.getY();
        double B1 = lineEnd.getX() - lineBeg.getX();
        double B2 = clipLineEnd.getX() - clipLineBeg.getX();
        double C1 = (lineBeg.getX() * lineEnd.getY()) - (lineBeg.getY() * lineEnd.getX());
        double C2 = (clipLineBeg.getX() * clipLineEnd.getY()) - (clipLineBeg.getY() * clipLineEnd.getX());
        double M = (B1 * A2) - (B2 * A1);
        return new Point(((B2 * C1) - (B1 * C2)) / M, ((C2 * A1) - (C1 * A2)) / M);
    }
}
