package com.itextpdf.layout.margincollapse;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.property.FloatPropertyValue;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.BlockFormattingContextUtil;
import com.itextpdf.layout.renderer.BlockRenderer;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.LineRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/margincollapse/MarginsCollapseHandler.class */
public class MarginsCollapseHandler {
    private IRenderer renderer;
    private MarginsCollapseInfo collapseInfo;
    private MarginsCollapseInfo childMarginInfo;
    private MarginsCollapseInfo prevChildMarginInfo;
    private int firstNotEmptyKidIndex = 0;
    private int processedChildrenNum = 0;
    private List<IRenderer> rendererChildren = new ArrayList();
    private Rectangle backupLayoutBox;
    private MarginsCollapseInfo backupCollapseInfo;
    private boolean lastKidCollapsedAfterHasClearanceApplied;

    public MarginsCollapseHandler(IRenderer renderer, MarginsCollapseInfo marginsCollapseInfo) {
        this.renderer = renderer;
        this.collapseInfo = marginsCollapseInfo != null ? marginsCollapseInfo : new MarginsCollapseInfo();
    }

    public void processFixedHeightAdjustment(float heightDelta) {
        this.collapseInfo.setBufferSpaceOnTop(this.collapseInfo.getBufferSpaceOnTop() + heightDelta);
        this.collapseInfo.setBufferSpaceOnBottom(this.collapseInfo.getBufferSpaceOnBottom() + heightDelta);
    }

    public MarginsCollapseInfo startChildMarginsHandling(IRenderer child, Rectangle layoutBox) {
        if (this.backupLayoutBox != null) {
            restoreLayoutBoxAfterFailedLayoutAttempt(layoutBox);
            int i = this.processedChildrenNum - 1;
            this.processedChildrenNum = i;
            removeRendererChild(i);
            this.childMarginInfo = null;
        }
        this.rendererChildren.add(child);
        int childIndex = this.processedChildrenNum;
        this.processedChildrenNum = childIndex + 1;
        boolean childIsBlockElement = !rendererIsFloated(child) && isBlockElement(child);
        this.backupLayoutBox = layoutBox.mo825clone();
        this.backupCollapseInfo = new MarginsCollapseInfo();
        this.collapseInfo.copyTo(this.backupCollapseInfo);
        prepareBoxForLayoutAttempt(layoutBox, childIndex, childIsBlockElement);
        if (childIsBlockElement) {
            this.childMarginInfo = createMarginsInfoForBlockChild(childIndex);
        }
        return this.childMarginInfo;
    }

    public void applyClearance(float clearHeightCorrection) {
        this.collapseInfo.setClearanceApplied(true);
        this.collapseInfo.getCollapseBefore().joinMargin(clearHeightCorrection);
    }

    private MarginsCollapseInfo createMarginsInfoForBlockChild(int childIndex) {
        MarginsCollapse childCollapseBefore;
        boolean ignoreChildTopMargin = false;
        boolean ignoreChildBottomMargin = lastChildMarginAdjoinedToParent(this.renderer);
        if (childIndex == this.firstNotEmptyKidIndex) {
            ignoreChildTopMargin = firstChildMarginAdjoinedToParent(this.renderer);
        }
        if (childIndex == 0) {
            MarginsCollapse parentCollapseBefore = this.collapseInfo.getCollapseBefore();
            childCollapseBefore = ignoreChildTopMargin ? parentCollapseBefore : new MarginsCollapse();
        } else {
            MarginsCollapse prevChildCollapseAfter = this.prevChildMarginInfo != null ? this.prevChildMarginInfo.getOwnCollapseAfter() : null;
            childCollapseBefore = prevChildCollapseAfter != null ? prevChildCollapseAfter : new MarginsCollapse();
        }
        MarginsCollapse parentCollapseAfter = this.collapseInfo.getCollapseAfter().m951clone();
        MarginsCollapse childCollapseAfter = ignoreChildBottomMargin ? parentCollapseAfter : new MarginsCollapse();
        MarginsCollapseInfo childMarginsInfo = new MarginsCollapseInfo(ignoreChildTopMargin, ignoreChildBottomMargin, childCollapseBefore, childCollapseAfter);
        if (ignoreChildTopMargin && childIndex == this.firstNotEmptyKidIndex) {
            childMarginsInfo.setBufferSpaceOnTop(this.collapseInfo.getBufferSpaceOnTop());
        }
        if (ignoreChildBottomMargin) {
            childMarginsInfo.setBufferSpaceOnBottom(this.collapseInfo.getBufferSpaceOnBottom());
        }
        return childMarginsInfo;
    }

    public void endChildMarginsHandling(Rectangle layoutBox) {
        int childIndex = this.processedChildrenNum - 1;
        if (rendererIsFloated(getRendererChild(childIndex))) {
            return;
        }
        if (this.childMarginInfo != null) {
            if (this.firstNotEmptyKidIndex == childIndex && this.childMarginInfo.isSelfCollapsing()) {
                this.firstNotEmptyKidIndex = childIndex + 1;
            }
            this.collapseInfo.setSelfCollapsing(this.collapseInfo.isSelfCollapsing() && this.childMarginInfo.isSelfCollapsing());
            this.lastKidCollapsedAfterHasClearanceApplied = this.childMarginInfo.isSelfCollapsing() && this.childMarginInfo.isClearanceApplied();
        } else {
            this.lastKidCollapsedAfterHasClearanceApplied = false;
            this.collapseInfo.setSelfCollapsing(false);
        }
        if (this.prevChildMarginInfo != null) {
            fixPrevChildOccupiedArea(childIndex);
            updateCollapseBeforeIfPrevKidIsFirstAndSelfCollapsed(this.prevChildMarginInfo.getOwnCollapseAfter());
        }
        if (this.firstNotEmptyKidIndex == childIndex && firstChildMarginAdjoinedToParent(this.renderer) && !this.collapseInfo.isSelfCollapsing()) {
            getRidOfCollapseArtifactsAtopOccupiedArea();
            if (this.childMarginInfo != null) {
                processUsedChildBufferSpaceOnTop(layoutBox);
            }
        }
        this.prevChildMarginInfo = this.childMarginInfo;
        this.childMarginInfo = null;
        this.backupLayoutBox = null;
        this.backupCollapseInfo = null;
    }

    public void startMarginsCollapse(Rectangle parentBBox) {
        this.collapseInfo.getCollapseBefore().joinMargin(getModelTopMargin(this.renderer));
        this.collapseInfo.getCollapseAfter().joinMargin(getModelBottomMargin(this.renderer));
        if (!firstChildMarginAdjoinedToParent(this.renderer)) {
            float topIndent = this.collapseInfo.getCollapseBefore().getCollapsedMarginsSize();
            applyTopMargin(parentBBox, topIndent);
        }
        if (!lastChildMarginAdjoinedToParent(this.renderer)) {
            float bottomIndent = this.collapseInfo.getCollapseAfter().getCollapsedMarginsSize();
            applyBottomMargin(parentBBox, bottomIndent);
        }
        ignoreModelTopMargin(this.renderer);
        ignoreModelBottomMargin(this.renderer);
    }

    public void endMarginsCollapse(Rectangle layoutBox) {
        MarginsCollapse ownCollapseAfter;
        if (this.backupLayoutBox != null) {
            restoreLayoutBoxAfterFailedLayoutAttempt(layoutBox);
        }
        if (this.prevChildMarginInfo != null) {
            updateCollapseBeforeIfPrevKidIsFirstAndSelfCollapsed(this.prevChildMarginInfo.getCollapseAfter());
        }
        boolean couldBeSelfCollapsing = marginsCouldBeSelfCollapsing(this.renderer) && !this.lastKidCollapsedAfterHasClearanceApplied;
        boolean blockHasNoKidsWithContent = this.collapseInfo.isSelfCollapsing();
        if (firstChildMarginAdjoinedToParent(this.renderer) && blockHasNoKidsWithContent && !couldBeSelfCollapsing) {
            addNotYetAppliedTopMargin(layoutBox);
        }
        this.collapseInfo.setSelfCollapsing(this.collapseInfo.isSelfCollapsing() && couldBeSelfCollapsing);
        if (!blockHasNoKidsWithContent && this.lastKidCollapsedAfterHasClearanceApplied) {
            applySelfCollapsedKidMarginWithClearance(layoutBox);
        }
        boolean lastChildMarginJoinedToParent = (this.prevChildMarginInfo == null || !this.prevChildMarginInfo.isIgnoreOwnMarginBottom() || this.lastKidCollapsedAfterHasClearanceApplied) ? false : true;
        if (lastChildMarginJoinedToParent) {
            ownCollapseAfter = this.prevChildMarginInfo.getOwnCollapseAfter();
        } else {
            ownCollapseAfter = new MarginsCollapse();
        }
        ownCollapseAfter.joinMargin(getModelBottomMargin(this.renderer));
        this.collapseInfo.setOwnCollapseAfter(ownCollapseAfter);
        if (this.collapseInfo.isSelfCollapsing()) {
            if (this.prevChildMarginInfo != null) {
                this.collapseInfo.setCollapseAfter(this.prevChildMarginInfo.getCollapseAfter());
            } else {
                this.collapseInfo.getCollapseAfter().joinMargin(this.collapseInfo.getCollapseBefore());
                this.collapseInfo.getOwnCollapseAfter().joinMargin(this.collapseInfo.getCollapseBefore());
            }
            if (!this.collapseInfo.isIgnoreOwnMarginBottom() && !this.collapseInfo.isIgnoreOwnMarginTop()) {
                float collapsedMargins = this.collapseInfo.getCollapseAfter().getCollapsedMarginsSize();
                overrideModelBottomMargin(this.renderer, collapsedMargins);
            }
        } else {
            MarginsCollapse marginsCollapseBefore = this.collapseInfo.getCollapseBefore();
            if (!this.collapseInfo.isIgnoreOwnMarginTop()) {
                float collapsedMargins2 = marginsCollapseBefore.getCollapsedMarginsSize();
                overrideModelTopMargin(this.renderer, collapsedMargins2);
            }
            if (lastChildMarginJoinedToParent) {
                this.collapseInfo.setCollapseAfter(this.prevChildMarginInfo.getCollapseAfter());
            }
            if (!this.collapseInfo.isIgnoreOwnMarginBottom()) {
                float collapsedMargins3 = this.collapseInfo.getCollapseAfter().getCollapsedMarginsSize();
                overrideModelBottomMargin(this.renderer, collapsedMargins3);
            }
        }
        if (lastChildMarginAdjoinedToParent(this.renderer)) {
            if (this.prevChildMarginInfo != null || blockHasNoKidsWithContent) {
                float collapsedMargins4 = this.collapseInfo.getCollapseAfter().getCollapsedMarginsSize();
                applyBottomMargin(layoutBox, collapsedMargins4);
            }
        }
    }

    private void updateCollapseBeforeIfPrevKidIsFirstAndSelfCollapsed(MarginsCollapse collapseAfter) {
        if (this.prevChildMarginInfo.isSelfCollapsing() && this.prevChildMarginInfo.isIgnoreOwnMarginTop()) {
            this.collapseInfo.getCollapseBefore().joinMargin(collapseAfter);
        }
    }

    private void prepareBoxForLayoutAttempt(Rectangle layoutBox, int childIndex, boolean childIsBlockElement) {
        if (this.prevChildMarginInfo != null) {
            boolean prevChildHasAppliedCollapseAfter = (this.prevChildMarginInfo.isIgnoreOwnMarginBottom() || (this.prevChildMarginInfo.isSelfCollapsing() && this.prevChildMarginInfo.isIgnoreOwnMarginTop())) ? false : true;
            if (prevChildHasAppliedCollapseAfter) {
                layoutBox.setHeight(layoutBox.getHeight() + this.prevChildMarginInfo.getCollapseAfter().getCollapsedMarginsSize());
            }
            boolean prevChildCanApplyCollapseAfter = (this.prevChildMarginInfo.isSelfCollapsing() && this.prevChildMarginInfo.isIgnoreOwnMarginTop()) ? false : true;
            if (!childIsBlockElement && prevChildCanApplyCollapseAfter) {
                MarginsCollapse ownCollapseAfter = this.prevChildMarginInfo.getOwnCollapseAfter();
                float ownCollapsedMargins = ownCollapseAfter == null ? 0.0f : ownCollapseAfter.getCollapsedMarginsSize();
                layoutBox.setHeight(layoutBox.getHeight() - ownCollapsedMargins);
            }
        } else if (childIndex > this.firstNotEmptyKidIndex && lastChildMarginAdjoinedToParent(this.renderer)) {
            float bottomIndent = this.collapseInfo.getCollapseAfter().getCollapsedMarginsSize() - this.collapseInfo.getUsedBufferSpaceOnBottom();
            this.collapseInfo.setBufferSpaceOnBottom(this.collapseInfo.getBufferSpaceOnBottom() + this.collapseInfo.getUsedBufferSpaceOnBottom());
            this.collapseInfo.setUsedBufferSpaceOnBottom(0.0f);
            layoutBox.setY(layoutBox.getY() - bottomIndent);
            layoutBox.setHeight(layoutBox.getHeight() + bottomIndent);
        }
        if (!childIsBlockElement) {
            if (childIndex == this.firstNotEmptyKidIndex && firstChildMarginAdjoinedToParent(this.renderer)) {
                float topIndent = this.collapseInfo.getCollapseBefore().getCollapsedMarginsSize();
                applyTopMargin(layoutBox, topIndent);
            }
            if (lastChildMarginAdjoinedToParent(this.renderer)) {
                applyBottomMargin(layoutBox, this.collapseInfo.getCollapseAfter().getCollapsedMarginsSize());
            }
        }
    }

    private void restoreLayoutBoxAfterFailedLayoutAttempt(Rectangle layoutBox) {
        layoutBox.setX(this.backupLayoutBox.getX()).setY(this.backupLayoutBox.getY()).setWidth(this.backupLayoutBox.getWidth()).setHeight(this.backupLayoutBox.getHeight());
        this.backupCollapseInfo.copyTo(this.collapseInfo);
        this.backupLayoutBox = null;
        this.backupCollapseInfo = null;
    }

    private void applyTopMargin(Rectangle box, float topIndent) {
        float bufferLeftoversOnTop = this.collapseInfo.getBufferSpaceOnTop() - topIndent;
        float usedTopBuffer = bufferLeftoversOnTop > 0.0f ? topIndent : this.collapseInfo.getBufferSpaceOnTop();
        this.collapseInfo.setUsedBufferSpaceOnTop(usedTopBuffer);
        subtractUsedTopBufferFromBottomBuffer(usedTopBuffer);
        if (bufferLeftoversOnTop >= 0.0f) {
            this.collapseInfo.setBufferSpaceOnTop(bufferLeftoversOnTop);
            box.moveDown(topIndent);
        } else {
            box.moveDown(this.collapseInfo.getBufferSpaceOnTop());
            this.collapseInfo.setBufferSpaceOnTop(0.0f);
            box.setHeight(box.getHeight() + bufferLeftoversOnTop);
        }
    }

    private void applyBottomMargin(Rectangle box, float bottomIndent) {
        float bottomIndentLeftovers = bottomIndent - this.collapseInfo.getBufferSpaceOnBottom();
        if (bottomIndentLeftovers < 0.0f) {
            this.collapseInfo.setUsedBufferSpaceOnBottom(bottomIndent);
            this.collapseInfo.setBufferSpaceOnBottom(-bottomIndentLeftovers);
        } else {
            this.collapseInfo.setUsedBufferSpaceOnBottom(this.collapseInfo.getBufferSpaceOnBottom());
            this.collapseInfo.setBufferSpaceOnBottom(0.0f);
            box.setY(box.getY() + bottomIndentLeftovers);
            box.setHeight(box.getHeight() - bottomIndentLeftovers);
        }
    }

    private void processUsedChildBufferSpaceOnTop(Rectangle layoutBox) {
        float childUsedBufferSpaceOnTop = this.childMarginInfo.getUsedBufferSpaceOnTop();
        if (childUsedBufferSpaceOnTop > 0.0f) {
            if (childUsedBufferSpaceOnTop > this.collapseInfo.getBufferSpaceOnTop()) {
                childUsedBufferSpaceOnTop = this.collapseInfo.getBufferSpaceOnTop();
            }
            this.collapseInfo.setBufferSpaceOnTop(this.collapseInfo.getBufferSpaceOnTop() - childUsedBufferSpaceOnTop);
            this.collapseInfo.setUsedBufferSpaceOnTop(childUsedBufferSpaceOnTop);
            layoutBox.moveDown(childUsedBufferSpaceOnTop);
            subtractUsedTopBufferFromBottomBuffer(childUsedBufferSpaceOnTop);
        }
    }

    private void subtractUsedTopBufferFromBottomBuffer(float usedTopBuffer) {
        if (this.collapseInfo.getBufferSpaceOnTop() > this.collapseInfo.getBufferSpaceOnBottom()) {
            float bufferLeftoversOnTop = this.collapseInfo.getBufferSpaceOnTop() - usedTopBuffer;
            if (bufferLeftoversOnTop < this.collapseInfo.getBufferSpaceOnBottom()) {
                this.collapseInfo.setBufferSpaceOnBottom(bufferLeftoversOnTop);
                return;
            }
            return;
        }
        this.collapseInfo.setBufferSpaceOnBottom(this.collapseInfo.getBufferSpaceOnBottom() - usedTopBuffer);
    }

    private void fixPrevChildOccupiedArea(int childIndex) {
        IRenderer prevRenderer = getRendererChild(childIndex - 1);
        Rectangle bBox = prevRenderer.getOccupiedArea().getBBox();
        boolean prevChildHasAppliedCollapseAfter = (this.prevChildMarginInfo.isIgnoreOwnMarginBottom() || (this.prevChildMarginInfo.isSelfCollapsing() && this.prevChildMarginInfo.isIgnoreOwnMarginTop())) ? false : true;
        if (prevChildHasAppliedCollapseAfter) {
            float bottomMargin = this.prevChildMarginInfo.getCollapseAfter().getCollapsedMarginsSize();
            bBox.setHeight(bBox.getHeight() - bottomMargin);
            bBox.moveUp(bottomMargin);
            ignoreModelBottomMargin(prevRenderer);
        }
        boolean isNotBlockChild = !isBlockElement(getRendererChild(childIndex));
        boolean prevChildCanApplyCollapseAfter = (this.prevChildMarginInfo.isSelfCollapsing() && this.prevChildMarginInfo.isIgnoreOwnMarginTop()) ? false : true;
        if (isNotBlockChild && prevChildCanApplyCollapseAfter) {
            float ownCollapsedMargins = this.prevChildMarginInfo.getOwnCollapseAfter().getCollapsedMarginsSize();
            bBox.setHeight(bBox.getHeight() + ownCollapsedMargins);
            bBox.moveDown(ownCollapsedMargins);
            overrideModelBottomMargin(prevRenderer, ownCollapsedMargins);
        }
    }

    private void addNotYetAppliedTopMargin(Rectangle layoutBox) {
        float indentTop = this.collapseInfo.getCollapseBefore().getCollapsedMarginsSize();
        this.renderer.getOccupiedArea().getBBox().moveDown(indentTop);
        applyTopMargin(layoutBox, indentTop);
    }

    private void applySelfCollapsedKidMarginWithClearance(Rectangle layoutBox) {
        float clearedKidMarginWithClearance = this.prevChildMarginInfo.getOwnCollapseAfter().getCollapsedMarginsSize();
        this.renderer.getOccupiedArea().getBBox().increaseHeight(clearedKidMarginWithClearance).moveDown(clearedKidMarginWithClearance);
        layoutBox.decreaseHeight(clearedKidMarginWithClearance);
    }

    private IRenderer getRendererChild(int index) {
        return this.rendererChildren.get(index);
    }

    private IRenderer removeRendererChild(int index) {
        return this.rendererChildren.remove(index);
    }

    private void getRidOfCollapseArtifactsAtopOccupiedArea() {
        Rectangle bBox = this.renderer.getOccupiedArea().getBBox();
        bBox.decreaseHeight(this.collapseInfo.getCollapseBefore().getCollapsedMarginsSize());
    }

    private static boolean marginsCouldBeSelfCollapsing(IRenderer renderer) {
        return ((renderer instanceof TableRenderer) || rendererIsFloated(renderer) || hasBottomBorders(renderer) || hasTopBorders(renderer) || hasBottomPadding(renderer) || hasTopPadding(renderer) || hasPositiveHeight(renderer) || (isBlockElement(renderer) && (renderer instanceof AbstractRenderer) && (((AbstractRenderer) renderer).getParent() instanceof LineRenderer))) ? false : true;
    }

    private static boolean firstChildMarginAdjoinedToParent(IRenderer parent) {
        return (BlockFormattingContextUtil.isRendererCreateBfc(parent) || (parent instanceof TableRenderer) || hasTopBorders(parent) || hasTopPadding(parent)) ? false : true;
    }

    private static boolean lastChildMarginAdjoinedToParent(IRenderer parent) {
        return (BlockFormattingContextUtil.isRendererCreateBfc(parent) || (parent instanceof TableRenderer) || hasBottomBorders(parent) || hasBottomPadding(parent) || hasHeightProp(parent)) ? false : true;
    }

    private static boolean isBlockElement(IRenderer renderer) {
        return (renderer instanceof BlockRenderer) || (renderer instanceof TableRenderer);
    }

    private static boolean hasHeightProp(IRenderer renderer) {
        return renderer.getModelElement().hasProperty(27);
    }

    private static boolean hasPositiveHeight(IRenderer renderer) {
        float value;
        float height = renderer.getOccupiedArea().getBBox().getHeight();
        if (height == 0.0f) {
            UnitValue heightPropVal = (UnitValue) renderer.getProperty(27);
            UnitValue minHeightPropVal = (UnitValue) renderer.getProperty(85);
            if (minHeightPropVal != null) {
                value = minHeightPropVal.getValue();
            } else {
                value = heightPropVal != null ? heightPropVal.getValue() : 0.0f;
            }
            height = value;
        }
        return height > 0.0f;
    }

    private static boolean hasTopPadding(IRenderer renderer) {
        UnitValue padding = (UnitValue) renderer.getModelElement().getProperty(50);
        if (null != padding && !padding.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) MarginsCollapseHandler.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 50));
        }
        return padding != null && padding.getValue() > 0.0f;
    }

    private static boolean hasBottomPadding(IRenderer renderer) {
        UnitValue padding = (UnitValue) renderer.getModelElement().getProperty(47);
        if (null != padding && !padding.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) MarginsCollapseHandler.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 47));
        }
        return padding != null && padding.getValue() > 0.0f;
    }

    private static boolean hasTopBorders(IRenderer renderer) {
        IPropertyContainer modelElement = renderer.getModelElement();
        return modelElement.hasProperty(13) || modelElement.hasProperty(9);
    }

    private static boolean hasBottomBorders(IRenderer renderer) {
        IPropertyContainer modelElement = renderer.getModelElement();
        return modelElement.hasProperty(10) || modelElement.hasProperty(9);
    }

    private static boolean rendererIsFloated(IRenderer renderer) {
        FloatPropertyValue floatPropertyValue;
        return (renderer == null || (floatPropertyValue = (FloatPropertyValue) renderer.getProperty(99)) == null || floatPropertyValue.equals(FloatPropertyValue.NONE)) ? false : true;
    }

    private static float getModelTopMargin(IRenderer renderer) {
        UnitValue marginUV = (UnitValue) renderer.getModelElement().getProperty(46);
        if (null != marginUV && !marginUV.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) MarginsCollapseHandler.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 46));
        }
        if (marginUV == null || (renderer instanceof CellRenderer)) {
            return 0.0f;
        }
        return marginUV.getValue();
    }

    private static void ignoreModelTopMargin(IRenderer renderer) {
        renderer.setProperty(46, UnitValue.createPointValue(0.0f));
    }

    private static void overrideModelTopMargin(IRenderer renderer, float collapsedMargins) {
        renderer.setProperty(46, UnitValue.createPointValue(collapsedMargins));
    }

    private static float getModelBottomMargin(IRenderer renderer) {
        UnitValue marginUV = (UnitValue) renderer.getModelElement().getProperty(43);
        if (null != marginUV && !marginUV.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) MarginsCollapseHandler.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 46));
        }
        if (marginUV == null || (renderer instanceof CellRenderer)) {
            return 0.0f;
        }
        return marginUV.getValue();
    }

    private static void ignoreModelBottomMargin(IRenderer renderer) {
        renderer.setProperty(43, UnitValue.createPointValue(0.0f));
    }

    private static void overrideModelBottomMargin(IRenderer renderer, float collapsedMargins) {
        renderer.setProperty(43, UnitValue.createPointValue(collapsedMargins));
    }
}
