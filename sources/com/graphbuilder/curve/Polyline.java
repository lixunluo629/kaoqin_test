package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/Polyline.class */
public class Polyline extends Curve {
    public Polyline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
    }

    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        this.gi.set(0, 0);
        if (this.connect) {
            mp.lineTo(this.cp.getPoint(this.gi.next()).getLocation());
        } else {
            mp.moveTo(this.cp.getPoint(this.gi.next()).getLocation());
        }
        while (this.gi.hasNext()) {
            mp.lineTo(this.cp.getPoint(this.gi.next()).getLocation());
        }
    }
}
