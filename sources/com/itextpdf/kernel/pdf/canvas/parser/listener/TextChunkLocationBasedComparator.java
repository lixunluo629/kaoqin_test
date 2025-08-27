package com.itextpdf.kernel.pdf.canvas.parser.listener;

import java.util.Comparator;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/TextChunkLocationBasedComparator.class */
class TextChunkLocationBasedComparator implements Comparator<TextChunk> {
    private Comparator<ITextChunkLocation> locationComparator;

    public TextChunkLocationBasedComparator(Comparator<ITextChunkLocation> locationComparator) {
        this.locationComparator = locationComparator;
    }

    @Override // java.util.Comparator
    public int compare(TextChunk o1, TextChunk o2) {
        return this.locationComparator.compare(o1.location, o2.location);
    }
}
