package com.itextpdf.layout.layout;

import com.itextpdf.layout.renderer.IRenderer;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/TextLayoutResult.class */
public class TextLayoutResult extends MinMaxWidthLayoutResult {
    protected boolean wordHasBeenSplit;
    protected boolean splitForcedByNewline;

    public TextLayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer) {
        super(status, occupiedArea, splitRenderer, overflowRenderer);
    }

    public TextLayoutResult(int status, LayoutArea occupiedArea, IRenderer splitRenderer, IRenderer overflowRenderer, IRenderer cause) {
        super(status, occupiedArea, splitRenderer, overflowRenderer, cause);
    }

    public boolean isWordHasBeenSplit() {
        return this.wordHasBeenSplit;
    }

    public TextLayoutResult setWordHasBeenSplit(boolean wordHasBeenSplit) {
        this.wordHasBeenSplit = wordHasBeenSplit;
        return this;
    }

    public boolean isSplitForcedByNewline() {
        return this.splitForcedByNewline;
    }

    public TextLayoutResult setSplitForcedByNewline(boolean isSplitForcedByNewline) {
        this.splitForcedByNewline = isSplitForcedByNewline;
        return this;
    }
}
