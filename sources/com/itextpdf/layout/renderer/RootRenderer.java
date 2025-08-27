package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.RootLayoutArea;
import com.itextpdf.layout.margincollapse.MarginsCollapseHandler;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/RootRenderer.class */
public abstract class RootRenderer extends AbstractRenderer {
    protected RootLayoutArea currentArea;
    protected int currentPageNumber;
    private IRenderer keepWithNextHangingRenderer;
    private LayoutResult keepWithNextHangingRendererLayoutResult;
    private MarginsCollapseHandler marginsCollapseHandler;
    private LayoutArea initialCurrentArea;
    private List<Rectangle> floatRendererAreas;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected boolean immediateFlush = true;
    protected List<IRenderer> waitingDrawingElements = new ArrayList();
    private List<IRenderer> waitingNextPageRenderers = new ArrayList();
    private boolean floatOverflowedCompletely = false;

    protected abstract void flushSingleRenderer(IRenderer iRenderer);

    protected abstract LayoutArea updateCurrentArea(LayoutResult layoutResult);

    static {
        $assertionsDisabled = !RootRenderer.class.desiredAssertionStatus();
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x045a  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x046d  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0478  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x04b4  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01a0 A[PHI: r20
  0x01a0: PHI (r20v2 'result' com.itextpdf.layout.layout.LayoutResult) = (r20v1 'result' com.itextpdf.layout.layout.LayoutResult), (r20v6 'result' com.itextpdf.layout.layout.LayoutResult) binds: [B:37:0x0168, B:43:0x019d] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void addChild(com.itextpdf.layout.renderer.IRenderer r9) {
        /*
            Method dump skipped, instructions count: 1612
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.layout.renderer.RootRenderer.addChild(com.itextpdf.layout.renderer.IRenderer):void");
    }

    public void flush() {
        for (IRenderer resultRenderer : this.childRenderers) {
            flushSingleRenderer(resultRenderer);
        }
        for (IRenderer resultRenderer2 : this.positionedRenderers) {
            flushSingleRenderer(resultRenderer2);
        }
        this.childRenderers.clear();
        this.positionedRenderers.clear();
    }

    public void close() {
        addAllWaitingNextPageRenderers();
        if (this.keepWithNextHangingRenderer != null) {
            this.keepWithNextHangingRenderer.setProperty(81, false);
            IRenderer rendererToBeAdded = this.keepWithNextHangingRenderer;
            this.keepWithNextHangingRenderer = null;
            addChild(rendererToBeAdded);
        }
        if (!this.immediateFlush) {
            flush();
        }
        flushWaitingDrawingElements();
        LayoutTaggingHelper taggingHelper = (LayoutTaggingHelper) getProperty(108);
        if (taggingHelper != null) {
            taggingHelper.releaseAllHints();
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        throw new IllegalStateException("Layout is not supported for root renderers.");
    }

    public LayoutArea getCurrentArea() {
        if (this.currentArea == null) {
            updateCurrentAndInitialArea(null);
        }
        return this.currentArea;
    }

    protected void flushWaitingDrawingElements() {
        for (int i = 0; i < this.waitingDrawingElements.size(); i++) {
            IRenderer waitingDrawingElement = this.waitingDrawingElements.get(i);
            flushSingleRenderer(waitingDrawingElement);
        }
        this.waitingDrawingElements.clear();
    }

    protected void shrinkCurrentAreaAndProcessRenderer(IRenderer renderer, List<IRenderer> resultRenderers, LayoutResult result) {
        if (this.currentArea != null) {
            float resultRendererHeight = result.getOccupiedArea().getBBox().getHeight();
            this.currentArea.getBBox().setHeight(this.currentArea.getBBox().getHeight() - resultRendererHeight);
            if (this.currentArea.isEmptyArea() && (resultRendererHeight > 0.0f || FloatingHelper.isRendererFloating(renderer))) {
                this.currentArea.setEmptyArea(false);
            }
            processRenderer(renderer, resultRenderers);
        }
        if (!this.immediateFlush) {
            this.childRenderers.addAll(resultRenderers);
        }
    }

    private void processRenderer(IRenderer renderer, List<IRenderer> resultRenderers) {
        alignChildHorizontally(renderer, this.currentArea.getBBox());
        if (this.immediateFlush) {
            flushSingleRenderer(renderer);
        } else {
            resultRenderers.add(renderer);
        }
    }

    private void processWaitingKeepWithNextElement(IRenderer renderer) {
        if (this.keepWithNextHangingRenderer != null) {
            LayoutArea rest = this.currentArea.mo950clone();
            rest.getBBox().setHeight(rest.getBBox().getHeight() - this.keepWithNextHangingRendererLayoutResult.getOccupiedArea().getBBox().getHeight());
            boolean ableToProcessKeepWithNext = false;
            if (renderer.setParent(this).layout(new LayoutContext(rest)).getStatus() != 3) {
                shrinkCurrentAreaAndProcessRenderer(this.keepWithNextHangingRenderer, new ArrayList(), this.keepWithNextHangingRendererLayoutResult);
                ableToProcessKeepWithNext = true;
            } else {
                float originalElementHeight = this.keepWithNextHangingRendererLayoutResult.getOccupiedArea().getBBox().getHeight();
                List<Float> trySplitHeightPoints = new ArrayList<>();
                for (int i = 1; i <= 5 && originalElementHeight - (35.0f * i) > originalElementHeight / 2.0f; i++) {
                    trySplitHeightPoints.add(Float.valueOf(originalElementHeight - (35.0f * i)));
                }
                for (int i2 = 0; i2 < trySplitHeightPoints.size() && !ableToProcessKeepWithNext; i2++) {
                    float curElementSplitHeight = trySplitHeightPoints.get(i2).floatValue();
                    RootLayoutArea firstElementSplitLayoutArea = (RootLayoutArea) this.currentArea.mo950clone();
                    firstElementSplitLayoutArea.getBBox().setHeight(curElementSplitHeight).moveUp(this.currentArea.getBBox().getHeight() - curElementSplitHeight);
                    LayoutResult firstElementSplitLayoutResult = this.keepWithNextHangingRenderer.setParent(this).layout(new LayoutContext(firstElementSplitLayoutArea.mo950clone()));
                    if (firstElementSplitLayoutResult.getStatus() == 2) {
                        RootLayoutArea storedArea = this.currentArea;
                        updateCurrentAndInitialArea(firstElementSplitLayoutResult);
                        LayoutResult firstElementOverflowLayoutResult = firstElementSplitLayoutResult.getOverflowRenderer().layout(new LayoutContext(this.currentArea.mo950clone()));
                        if (firstElementOverflowLayoutResult.getStatus() == 1) {
                            LayoutArea secondElementLayoutArea = this.currentArea.mo950clone();
                            secondElementLayoutArea.getBBox().setHeight(secondElementLayoutArea.getBBox().getHeight() - firstElementOverflowLayoutResult.getOccupiedArea().getBBox().getHeight());
                            LayoutResult secondElementLayoutResult = renderer.setParent(this).layout(new LayoutContext(secondElementLayoutArea));
                            if (secondElementLayoutResult.getStatus() != 3) {
                                ableToProcessKeepWithNext = true;
                                this.currentArea = firstElementSplitLayoutArea;
                                this.currentPageNumber = firstElementSplitLayoutArea.getPageNumber();
                                shrinkCurrentAreaAndProcessRenderer(firstElementSplitLayoutResult.getSplitRenderer(), new ArrayList(), firstElementSplitLayoutResult);
                                updateCurrentAndInitialArea(firstElementSplitLayoutResult);
                                shrinkCurrentAreaAndProcessRenderer(firstElementSplitLayoutResult.getOverflowRenderer(), new ArrayList(), firstElementOverflowLayoutResult);
                            }
                        }
                        if (!ableToProcessKeepWithNext) {
                            this.currentArea = storedArea;
                            this.currentPageNumber = storedArea.getPageNumber();
                        }
                    }
                }
            }
            if (!ableToProcessKeepWithNext && !this.currentArea.isEmptyArea()) {
                RootLayoutArea storedArea2 = this.currentArea;
                updateCurrentAndInitialArea(null);
                LayoutResult firstElementLayoutResult = this.keepWithNextHangingRenderer.setParent(this).layout(new LayoutContext(this.currentArea.mo950clone()));
                if (firstElementLayoutResult.getStatus() == 1) {
                    LayoutArea secondElementLayoutArea2 = this.currentArea.mo950clone();
                    secondElementLayoutArea2.getBBox().setHeight(secondElementLayoutArea2.getBBox().getHeight() - firstElementLayoutResult.getOccupiedArea().getBBox().getHeight());
                    LayoutResult secondElementLayoutResult2 = renderer.setParent(this).layout(new LayoutContext(secondElementLayoutArea2));
                    if (secondElementLayoutResult2.getStatus() != 3) {
                        ableToProcessKeepWithNext = true;
                        shrinkCurrentAreaAndProcessRenderer(this.keepWithNextHangingRenderer, new ArrayList(), this.keepWithNextHangingRendererLayoutResult);
                    }
                }
                if (!ableToProcessKeepWithNext) {
                    this.currentArea = storedArea2;
                    this.currentPageNumber = storedArea2.getPageNumber();
                }
            }
            if (!ableToProcessKeepWithNext) {
                Logger logger = LoggerFactory.getLogger((Class<?>) RootRenderer.class);
                logger.warn(LogMessageConstant.RENDERER_WAS_NOT_ABLE_TO_PROCESS_KEEP_WITH_NEXT);
                shrinkCurrentAreaAndProcessRenderer(this.keepWithNextHangingRenderer, new ArrayList(), this.keepWithNextHangingRendererLayoutResult);
            }
            this.keepWithNextHangingRenderer = null;
            this.keepWithNextHangingRendererLayoutResult = null;
        }
    }

    private void updateCurrentAndInitialArea(LayoutResult overflowResult) {
        this.floatRendererAreas = new ArrayList();
        updateCurrentArea(overflowResult);
        this.initialCurrentArea = this.currentArea == null ? null : this.currentArea.mo950clone();
        addWaitingNextPageRenderers();
    }

    private void addAllWaitingNextPageRenderers() {
        boolean marginsCollapsingEnabled = Boolean.TRUE.equals(getPropertyAsBoolean(89));
        while (!this.waitingNextPageRenderers.isEmpty()) {
            if (marginsCollapsingEnabled) {
                this.marginsCollapseHandler = new MarginsCollapseHandler(this, null);
            }
            updateCurrentAndInitialArea(null);
        }
    }

    private void addWaitingNextPageRenderers() {
        this.floatOverflowedCompletely = false;
        List<IRenderer> waitingFloatRenderers = new ArrayList<>(this.waitingNextPageRenderers);
        this.waitingNextPageRenderers.clear();
        for (IRenderer renderer : waitingFloatRenderers) {
            addChild(renderer);
        }
    }
}
