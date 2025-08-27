package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Vector;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/TextChunkLocationDefaultImp.class */
class TextChunkLocationDefaultImp implements ITextChunkLocation {
    private static final float DIACRITICAL_MARKS_ALLOWED_VERTICAL_DEVIATION = 2.0f;
    private final Vector startLocation;
    private final Vector endLocation;
    private final Vector orientationVector;
    private final int orientationMagnitude;
    private final int distPerpendicular;
    private final float distParallelStart;
    private final float distParallelEnd;
    private final float charSpaceWidth;

    public TextChunkLocationDefaultImp(Vector startLocation, Vector endLocation, float charSpaceWidth) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.charSpaceWidth = charSpaceWidth;
        Vector oVector = endLocation.subtract(startLocation);
        this.orientationVector = (oVector.length() == 0.0f ? new Vector(1.0f, 0.0f, 0.0f) : oVector).normalize();
        this.orientationMagnitude = (int) (Math.atan2(this.orientationVector.get(1), this.orientationVector.get(0)) * 1000.0d);
        Vector origin = new Vector(0.0f, 0.0f, 1.0f);
        this.distPerpendicular = (int) startLocation.subtract(origin).cross(this.orientationVector).get(2);
        this.distParallelStart = this.orientationVector.dot(startLocation);
        this.distParallelEnd = this.orientationVector.dot(endLocation);
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public int orientationMagnitude() {
        return this.orientationMagnitude;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public int distPerpendicular() {
        return this.distPerpendicular;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public float distParallelStart() {
        return this.distParallelStart;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public float distParallelEnd() {
        return this.distParallelEnd;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public Vector getStartLocation() {
        return this.startLocation;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public Vector getEndLocation() {
        return this.endLocation;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public float getCharSpaceWidth() {
        return this.charSpaceWidth;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public boolean sameLine(ITextChunkLocation as) {
        if (orientationMagnitude() != as.orientationMagnitude()) {
            return false;
        }
        float distPerpendicularDiff = distPerpendicular() - as.distPerpendicular();
        if (distPerpendicularDiff == 0.0f) {
            return true;
        }
        LineSegment mySegment = new LineSegment(this.startLocation, this.endLocation);
        LineSegment otherSegment = new LineSegment(as.getStartLocation(), as.getEndLocation());
        return Math.abs(distPerpendicularDiff) <= DIACRITICAL_MARKS_ALLOWED_VERTICAL_DEVIATION && (mySegment.getLength() == 0.0f || otherSegment.getLength() == 0.0f);
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public float distanceFromEndOf(ITextChunkLocation other) {
        return distParallelStart() - other.distParallelEnd();
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation
    public boolean isAtWordBoundary(ITextChunkLocation previous) {
        if (this.startLocation.equals(this.endLocation) || previous.getEndLocation().equals(previous.getStartLocation())) {
            return false;
        }
        float dist = distanceFromEndOf(previous);
        if (dist < 0.0f) {
            dist = previous.distanceFromEndOf(this);
            if (dist < 0.0f) {
                return false;
            }
        }
        return dist > getCharSpaceWidth() / DIACRITICAL_MARKS_ALLOWED_VERTICAL_DEVIATION;
    }

    static boolean containsMark(ITextChunkLocation baseLocation, ITextChunkLocation markLocation) {
        return baseLocation.getStartLocation().get(0) <= markLocation.getStartLocation().get(0) && baseLocation.getEndLocation().get(0) >= markLocation.getEndLocation().get(0) && ((float) Math.abs(baseLocation.distPerpendicular() - markLocation.distPerpendicular())) <= DIACRITICAL_MARKS_ALLOWED_VERTICAL_DEVIATION;
    }
}
