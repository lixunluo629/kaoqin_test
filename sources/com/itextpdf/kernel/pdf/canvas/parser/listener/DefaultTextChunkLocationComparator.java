package com.itextpdf.kernel.pdf.canvas.parser.listener;

import java.util.Comparator;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/DefaultTextChunkLocationComparator.class */
class DefaultTextChunkLocationComparator implements Comparator<ITextChunkLocation> {
    private boolean leftToRight;

    public DefaultTextChunkLocationComparator() {
        this(true);
    }

    public DefaultTextChunkLocationComparator(boolean leftToRight) {
        this.leftToRight = true;
        this.leftToRight = leftToRight;
    }

    @Override // java.util.Comparator
    public int compare(ITextChunkLocation first, ITextChunkLocation second) {
        if (first == second) {
            return 0;
        }
        int result = Integer.compare(first.orientationMagnitude(), second.orientationMagnitude());
        if (result != 0) {
            return result;
        }
        int distPerpendicularDiff = first.distPerpendicular() - second.distPerpendicular();
        if (distPerpendicularDiff != 0) {
            return distPerpendicularDiff;
        }
        return this.leftToRight ? Float.compare(first.distParallelStart(), second.distParallelStart()) : -Float.compare(first.distParallelEnd(), second.distParallelEnd());
    }
}
