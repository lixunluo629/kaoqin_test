package com.itextpdf.kernel.pdf.canvas.parser.data;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/data/AbstractRenderInfo.class */
public class AbstractRenderInfo implements IEventData {
    protected CanvasGraphicsState gs;
    private boolean graphicsStateIsPreserved;

    public AbstractRenderInfo(CanvasGraphicsState gs) {
        this.gs = gs;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.data.IEventData
    public CanvasGraphicsState getGraphicsState() {
        checkGraphicsState();
        return this.graphicsStateIsPreserved ? this.gs : new CanvasGraphicsState(this.gs);
    }

    public boolean isGraphicsStatePreserved() {
        return this.graphicsStateIsPreserved;
    }

    public void preserveGraphicsState() {
        checkGraphicsState();
        this.graphicsStateIsPreserved = true;
        this.gs = new CanvasGraphicsState(this.gs);
    }

    public void releaseGraphicsState() {
        if (!this.graphicsStateIsPreserved) {
            this.gs = null;
        }
    }

    protected void checkGraphicsState() {
        if (null == this.gs) {
            throw new IllegalStateException(LogMessageConstant.GRAPHICS_STATE_WAS_DELETED);
        }
    }
}
