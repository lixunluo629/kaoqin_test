package com.itextpdf.kernel.pdf.canvas.parser.listener;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/GlyphTextEventListener.class */
public class GlyphTextEventListener extends GlyphEventListener implements ITextExtractionStrategy {
    public GlyphTextEventListener(ITextExtractionStrategy delegate) {
        super(delegate);
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy
    public String getResultantText() {
        if (this.delegate instanceof ITextExtractionStrategy) {
            return ((ITextExtractionStrategy) this.delegate).getResultantText();
        }
        return null;
    }
}
