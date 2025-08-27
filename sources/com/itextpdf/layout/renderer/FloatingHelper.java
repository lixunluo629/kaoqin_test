package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.margincollapse.MarginsCollapseHandler;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.property.ClearPropertyValue;
import com.itextpdf.layout.property.FloatPropertyValue;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.UnitValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/FloatingHelper.class */
class FloatingHelper {
    private FloatingHelper() {
    }

    static void adjustLineAreaAccordingToFloats(List<Rectangle> floatRendererAreas, Rectangle layoutBox) {
        adjustLayoutBoxAccordingToFloats(floatRendererAreas, layoutBox, null, 0.0f, null);
    }

    static float adjustLayoutBoxAccordingToFloats(List<Rectangle> floatRendererAreas, Rectangle layoutBox, Float boxWidth, float clearHeightCorrection, MarginsCollapseHandler marginsCollapseHandler) {
        float left;
        float right;
        float topShift = clearHeightCorrection;
        Rectangle[] lastLeftAndRightBoxes = null;
        do {
            if (lastLeftAndRightBoxes != null) {
                float bottomLeft = lastLeftAndRightBoxes[0] != null ? lastLeftAndRightBoxes[0].getBottom() : Float.MAX_VALUE;
                float bottomRight = lastLeftAndRightBoxes[1] != null ? lastLeftAndRightBoxes[1].getBottom() : Float.MAX_VALUE;
                float updatedHeight = Math.min(bottomLeft, bottomRight) - layoutBox.getY();
                topShift = layoutBox.getHeight() - updatedHeight;
            }
            List<Rectangle> boxesAtYLevel = getBoxesAtYLevel(floatRendererAreas, layoutBox.getTop() - topShift);
            if (boxesAtYLevel.isEmpty()) {
                applyClearance(layoutBox, marginsCollapseHandler, topShift, false);
                return topShift;
            }
            lastLeftAndRightBoxes = findLastLeftAndRightBoxes(layoutBox, boxesAtYLevel);
            left = lastLeftAndRightBoxes[0] != null ? lastLeftAndRightBoxes[0].getRight() : Float.MIN_VALUE;
            right = lastLeftAndRightBoxes[1] != null ? lastLeftAndRightBoxes[1].getLeft() : Float.MAX_VALUE;
            if (left > right || left > layoutBox.getRight() || right < layoutBox.getLeft()) {
                left = layoutBox.getLeft();
                right = left;
            } else {
                if (right > layoutBox.getRight()) {
                    right = layoutBox.getRight();
                }
                if (left < layoutBox.getLeft()) {
                    left = layoutBox.getLeft();
                }
            }
            if (boxWidth == null) {
                break;
            }
        } while (boxWidth.floatValue() > right - left);
        if (layoutBox.getWidth() > right - left) {
            layoutBox.setX(left).setWidth(right - left);
        }
        applyClearance(layoutBox, marginsCollapseHandler, topShift, false);
        return topShift;
    }

    static Float calculateLineShiftUnderFloats(List<Rectangle> floatRendererAreas, Rectangle layoutBox) {
        float maxLastFloatBottom;
        List<Rectangle> boxesAtYLevel = getBoxesAtYLevel(floatRendererAreas, layoutBox.getTop());
        if (boxesAtYLevel.isEmpty()) {
            return null;
        }
        Rectangle[] lastLeftAndRightBoxes = findLastLeftAndRightBoxes(layoutBox, boxesAtYLevel);
        float left = lastLeftAndRightBoxes[0] != null ? lastLeftAndRightBoxes[0].getRight() : layoutBox.getLeft();
        float right = lastLeftAndRightBoxes[1] != null ? lastLeftAndRightBoxes[1].getLeft() : layoutBox.getRight();
        if (layoutBox.getLeft() < left || layoutBox.getRight() > right) {
            if (lastLeftAndRightBoxes[0] != null && lastLeftAndRightBoxes[1] != null) {
                maxLastFloatBottom = Math.max(lastLeftAndRightBoxes[0].getBottom(), lastLeftAndRightBoxes[1].getBottom());
            } else if (lastLeftAndRightBoxes[0] != null) {
                maxLastFloatBottom = lastLeftAndRightBoxes[0].getBottom();
            } else {
                maxLastFloatBottom = lastLeftAndRightBoxes[1].getBottom();
            }
            return Float.valueOf((layoutBox.getTop() - maxLastFloatBottom) + 1.0E-4f);
        }
        return null;
    }

    static void adjustFloatedTableLayoutBox(TableRenderer tableRenderer, Rectangle layoutBox, float tableWidth, List<Rectangle> floatRendererAreas, FloatPropertyValue floatPropertyValue) {
        tableRenderer.setProperty(28, null);
        UnitValue[] margins = tableRenderer.getMargins();
        if (!margins[1].isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) FloatingHelper.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 45));
        }
        if (!margins[3].isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) FloatingHelper.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
        }
        adjustBlockAreaAccordingToFloatRenderers(floatRendererAreas, layoutBox, tableWidth + margins[1].getValue() + margins[3].getValue(), FloatPropertyValue.LEFT.equals(floatPropertyValue));
    }

    static Float adjustFloatedBlockLayoutBox(AbstractRenderer renderer, Rectangle parentBBox, Float blockWidth, List<Rectangle> floatRendererAreas, FloatPropertyValue floatPropertyValue, OverflowPropertyValue overflowX) {
        float floatElemWidth;
        renderer.setProperty(28, null);
        boolean overflowFit = AbstractRenderer.isOverflowFit(overflowX);
        if (blockWidth != null) {
            floatElemWidth = blockWidth.floatValue() + AbstractRenderer.calculateAdditionalWidth(renderer);
            if (overflowFit && floatElemWidth > parentBBox.getWidth()) {
                floatElemWidth = parentBBox.getWidth();
            }
        } else {
            MinMaxWidth minMaxWidth = calculateMinMaxWidthForFloat(renderer, floatPropertyValue);
            float maxWidth = minMaxWidth.getMaxWidth();
            if (maxWidth > parentBBox.getWidth()) {
                maxWidth = parentBBox.getWidth();
            }
            if (!overflowFit && minMaxWidth.getMinWidth() > parentBBox.getWidth()) {
                maxWidth = minMaxWidth.getMinWidth();
            }
            floatElemWidth = maxWidth + 1.0E-4f;
            blockWidth = Float.valueOf((maxWidth - minMaxWidth.getAdditionalWidth()) + 1.0E-4f);
        }
        adjustBlockAreaAccordingToFloatRenderers(floatRendererAreas, parentBBox, floatElemWidth, FloatPropertyValue.LEFT.equals(floatPropertyValue));
        return blockWidth;
    }

    private static void adjustBlockAreaAccordingToFloatRenderers(List<Rectangle> floatRendererAreas, Rectangle layoutBox, float blockWidth, boolean isFloatLeft) {
        float currY;
        if (floatRendererAreas.isEmpty()) {
            if (!isFloatLeft) {
                adjustBoxForFloatRight(layoutBox, blockWidth);
                return;
            }
            return;
        }
        if (floatRendererAreas.get(floatRendererAreas.size() - 1).getTop() < layoutBox.getTop()) {
            currY = floatRendererAreas.get(floatRendererAreas.size() - 1).getTop();
        } else {
            currY = layoutBox.getTop();
        }
        Rectangle[] lastLeftAndRightBoxes = null;
        float left = 0.0f;
        float left2 = 0.0f;
        while (true) {
            float right = left2;
            if (lastLeftAndRightBoxes == null || right - left < blockWidth) {
                if (lastLeftAndRightBoxes != null) {
                    if (isFloatLeft) {
                        currY = lastLeftAndRightBoxes[0] != null ? lastLeftAndRightBoxes[0].getBottom() : lastLeftAndRightBoxes[1].getBottom();
                    } else {
                        currY = lastLeftAndRightBoxes[1] != null ? lastLeftAndRightBoxes[1].getBottom() : lastLeftAndRightBoxes[0].getBottom();
                    }
                }
                layoutBox.setHeight(currY - layoutBox.getY());
                List<Rectangle> yLevelBoxes = getBoxesAtYLevel(floatRendererAreas, currY);
                if (yLevelBoxes.isEmpty()) {
                    if (!isFloatLeft) {
                        adjustBoxForFloatRight(layoutBox, blockWidth);
                        return;
                    }
                    return;
                } else {
                    lastLeftAndRightBoxes = findLastLeftAndRightBoxes(layoutBox, yLevelBoxes);
                    left = lastLeftAndRightBoxes[0] != null ? lastLeftAndRightBoxes[0].getRight() : layoutBox.getLeft();
                    left2 = lastLeftAndRightBoxes[1] != null ? lastLeftAndRightBoxes[1].getLeft() : layoutBox.getRight();
                }
            } else {
                layoutBox.setX(left);
                layoutBox.setWidth(right - left);
                if (!isFloatLeft) {
                    adjustBoxForFloatRight(layoutBox, blockWidth);
                    return;
                }
                return;
            }
        }
    }

    static void removeFloatsAboveRendererBottom(List<Rectangle> floatRendererAreas, IRenderer renderer) {
        if (!isRendererFloating(renderer)) {
            float bottom = renderer.getOccupiedArea().getBBox().getBottom();
            for (int i = floatRendererAreas.size() - 1; i >= 0; i--) {
                if (floatRendererAreas.get(i).getBottom() >= bottom) {
                    floatRendererAreas.remove(i);
                }
            }
        }
    }

    static LayoutArea adjustResultOccupiedAreaForFloatAndClear(IRenderer renderer, List<Rectangle> floatRendererAreas, Rectangle parentBBox, float clearHeightCorrection, boolean marginsCollapsingEnabled) {
        LayoutArea occupiedArea = renderer.getOccupiedArea();
        LayoutArea editedArea = occupiedArea;
        if (isRendererFloating(renderer)) {
            editedArea = occupiedArea.mo950clone();
            if (occupiedArea.getBBox().getWidth() > 0.0f) {
                floatRendererAreas.add(occupiedArea.getBBox());
            }
            editedArea.getBBox().setY(parentBBox.getTop());
            editedArea.getBBox().setHeight(0.0f);
        } else if (clearHeightCorrection > 0.0f && !marginsCollapsingEnabled) {
            editedArea = occupiedArea.mo950clone();
            editedArea.getBBox().increaseHeight(clearHeightCorrection);
        }
        return editedArea;
    }

    static void includeChildFloatsInOccupiedArea(List<Rectangle> floatRendererAreas, IRenderer renderer, Set<Rectangle> nonChildFloatingRendererAreas) {
        Rectangle commonRectangle = includeChildFloatsInOccupiedArea(floatRendererAreas, renderer.getOccupiedArea().getBBox(), nonChildFloatingRendererAreas);
        renderer.getOccupiedArea().setBBox(commonRectangle);
    }

    static Rectangle includeChildFloatsInOccupiedArea(List<Rectangle> floatRendererAreas, Rectangle occupiedAreaBbox, Set<Rectangle> nonChildFloatingRendererAreas) {
        for (Rectangle floatBox : floatRendererAreas) {
            if (!nonChildFloatingRendererAreas.contains(floatBox)) {
                occupiedAreaBbox = Rectangle.getCommonRectangle(occupiedAreaBbox, floatBox);
            }
        }
        return occupiedAreaBbox;
    }

    static MinMaxWidth calculateMinMaxWidthForFloat(AbstractRenderer renderer, FloatPropertyValue floatPropertyVal) {
        boolean floatPropIsRendererOwn = renderer.hasOwnProperty(99);
        renderer.setProperty(99, FloatPropertyValue.NONE);
        MinMaxWidth kidMinMaxWidth = renderer.getMinMaxWidth();
        if (floatPropIsRendererOwn) {
            renderer.setProperty(99, floatPropertyVal);
        } else {
            renderer.deleteOwnProperty(99);
        }
        return kidMinMaxWidth;
    }

    static float calculateClearHeightCorrection(IRenderer renderer, List<Rectangle> floatRendererAreas, Rectangle parentBBox) {
        float currY;
        ClearPropertyValue clearPropertyValue = (ClearPropertyValue) renderer.getProperty(100);
        float clearHeightCorrection = 0.0f;
        if (clearPropertyValue == null || floatRendererAreas.isEmpty()) {
            return 0.0f;
        }
        if (floatRendererAreas.get(floatRendererAreas.size() - 1).getTop() < parentBBox.getTop()) {
            currY = floatRendererAreas.get(floatRendererAreas.size() - 1).getTop();
        } else {
            currY = parentBBox.getTop();
        }
        List<Rectangle> boxesAtYLevel = getBoxesAtYLevel(floatRendererAreas, currY);
        Rectangle[] lastLeftAndRightBoxes = findLastLeftAndRightBoxes(parentBBox, boxesAtYLevel);
        float lowestFloatBottom = Float.MAX_VALUE;
        boolean isBoth = clearPropertyValue.equals(ClearPropertyValue.BOTH);
        if ((clearPropertyValue.equals(ClearPropertyValue.LEFT) || isBoth) && lastLeftAndRightBoxes[0] != null) {
            for (Rectangle floatBox : floatRendererAreas) {
                if (floatBox.getBottom() < lowestFloatBottom && floatBox.getLeft() <= lastLeftAndRightBoxes[0].getLeft()) {
                    lowestFloatBottom = floatBox.getBottom();
                }
            }
        }
        if ((clearPropertyValue.equals(ClearPropertyValue.RIGHT) || isBoth) && lastLeftAndRightBoxes[1] != null) {
            for (Rectangle floatBox2 : floatRendererAreas) {
                if (floatBox2.getBottom() < lowestFloatBottom && floatBox2.getRight() >= lastLeftAndRightBoxes[1].getRight()) {
                    lowestFloatBottom = floatBox2.getBottom();
                }
            }
        }
        if (lowestFloatBottom < Float.MAX_VALUE) {
            clearHeightCorrection = (parentBBox.getTop() - lowestFloatBottom) + 1.0E-4f;
        }
        return clearHeightCorrection;
    }

    static void applyClearance(Rectangle layoutBox, MarginsCollapseHandler marginsCollapseHandler, float clearHeightAdjustment, boolean isFloat) {
        if (clearHeightAdjustment <= 0.0f) {
            return;
        }
        if (marginsCollapseHandler == null || isFloat) {
            layoutBox.decreaseHeight(clearHeightAdjustment);
        } else {
            marginsCollapseHandler.applyClearance(clearHeightAdjustment);
        }
    }

    static boolean isRendererFloating(IRenderer renderer) {
        return isRendererFloating(renderer, (FloatPropertyValue) renderer.getProperty(99));
    }

    static boolean isRendererFloating(IRenderer renderer, FloatPropertyValue kidFloatPropertyVal) {
        Integer position = (Integer) renderer.getProperty(52);
        boolean notAbsolutePos = position == null || position.intValue() != 3;
        return (!notAbsolutePos || kidFloatPropertyVal == null || kidFloatPropertyVal.equals(FloatPropertyValue.NONE)) ? false : true;
    }

    static boolean isClearanceApplied(List<IRenderer> floatingRenderers, ClearPropertyValue clearPropertyValue) {
        if (clearPropertyValue == null || clearPropertyValue.equals(ClearPropertyValue.NONE)) {
            return false;
        }
        for (IRenderer floatingRenderer : floatingRenderers) {
            FloatPropertyValue floatPropertyValue = (FloatPropertyValue) floatingRenderer.getProperty(99);
            if (!clearPropertyValue.equals(ClearPropertyValue.BOTH)) {
                if (!floatPropertyValue.equals(FloatPropertyValue.LEFT) || !clearPropertyValue.equals(ClearPropertyValue.LEFT)) {
                    if (floatPropertyValue.equals(FloatPropertyValue.RIGHT) && clearPropertyValue.equals(ClearPropertyValue.RIGHT)) {
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    static void removeParentArtifactsOnPageSplitIfOnlyFloatsOverflow(IRenderer overflowRenderer) {
        overflowRenderer.setProperty(6, null);
        overflowRenderer.setProperty(90, null);
        overflowRenderer.setProperty(106, null);
        Border[] borders = AbstractRenderer.getBorders(overflowRenderer);
        overflowRenderer.setProperty(13, null);
        overflowRenderer.setProperty(10, null);
        if (borders[1] != null) {
            overflowRenderer.setProperty(12, new SolidBorder(ColorConstants.BLACK, borders[1].getWidth(), 0.0f));
        }
        if (borders[3] != null) {
            overflowRenderer.setProperty(11, new SolidBorder(ColorConstants.BLACK, borders[3].getWidth(), 0.0f));
        }
        overflowRenderer.setProperty(46, UnitValue.createPointValue(0.0f));
        overflowRenderer.setProperty(43, UnitValue.createPointValue(0.0f));
        overflowRenderer.setProperty(50, UnitValue.createPointValue(0.0f));
        overflowRenderer.setProperty(47, UnitValue.createPointValue(0.0f));
    }

    private static void adjustBoxForFloatRight(Rectangle layoutBox, float blockWidth) {
        layoutBox.setX(layoutBox.getRight() - blockWidth);
        layoutBox.setWidth(blockWidth);
    }

    private static Rectangle[] findLastLeftAndRightBoxes(Rectangle layoutBox, List<Rectangle> yLevelBoxes) {
        Rectangle lastLeftFloatAtY = null;
        Rectangle lastRightFloatAtY = null;
        float left = layoutBox.getLeft();
        for (Rectangle box : yLevelBoxes) {
            if (box.getLeft() < left) {
                left = box.getLeft();
            }
        }
        for (Rectangle box2 : yLevelBoxes) {
            if (left >= box2.getLeft() && left < box2.getRight()) {
                lastLeftFloatAtY = box2;
                left = box2.getRight();
            } else {
                lastRightFloatAtY = box2;
            }
        }
        return new Rectangle[]{lastLeftFloatAtY, lastRightFloatAtY};
    }

    private static List<Rectangle> getBoxesAtYLevel(List<Rectangle> floatRendererAreas, float currY) {
        List<Rectangle> yLevelBoxes = new ArrayList<>();
        for (Rectangle box : floatRendererAreas) {
            if (box.getBottom() + 1.0E-4f < currY && box.getTop() + 1.0E-4f >= currY) {
                yLevelBoxes.add(box);
            }
        }
        return yLevelBoxes;
    }
}
