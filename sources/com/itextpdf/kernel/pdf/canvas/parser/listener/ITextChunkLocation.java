package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.Vector;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/ITextChunkLocation.class */
public interface ITextChunkLocation {
    float distParallelEnd();

    float distParallelStart();

    int distPerpendicular();

    float getCharSpaceWidth();

    Vector getEndLocation();

    Vector getStartLocation();

    int orientationMagnitude();

    boolean sameLine(ITextChunkLocation iTextChunkLocation);

    float distanceFromEndOf(ITextChunkLocation iTextChunkLocation);

    boolean isAtWordBoundary(ITextChunkLocation iTextChunkLocation);
}
