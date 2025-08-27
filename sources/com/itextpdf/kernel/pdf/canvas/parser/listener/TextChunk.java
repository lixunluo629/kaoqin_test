package com.itextpdf.kernel.pdf.canvas.parser.listener;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/TextChunk.class */
public class TextChunk {
    protected final String text;
    protected final ITextChunkLocation location;

    public TextChunk(String string, ITextChunkLocation loc) {
        this.text = string;
        this.location = loc;
    }

    public String getText() {
        return this.text;
    }

    public ITextChunkLocation getLocation() {
        return this.location;
    }

    void printDiagnostics() {
        System.out.println("Text (@" + this.location.getStartLocation() + " -> " + this.location.getEndLocation() + "): " + this.text);
        System.out.println("orientationMagnitude: " + this.location.orientationMagnitude());
        System.out.println("distPerpendicular: " + this.location.distPerpendicular());
        System.out.println("distParallel: " + this.location.distParallelStart());
    }

    boolean sameLine(TextChunk lastChunk) {
        return getLocation().sameLine(lastChunk.getLocation());
    }
}
