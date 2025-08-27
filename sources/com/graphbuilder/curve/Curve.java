package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/Curve.class */
public abstract class Curve {
    protected ControlPath cp;
    protected GroupIterator gi;
    protected boolean connect = false;

    public abstract void appendTo(MultiPath multiPath);

    public Curve(ControlPath cp, GroupIterator gi) {
        setControlPath(cp);
        setGroupIterator(gi);
    }

    public ControlPath getControlPath() {
        return this.cp;
    }

    public void setControlPath(ControlPath cp) {
        if (cp == null) {
            throw new IllegalArgumentException("ControlPath cannot be null.");
        }
        this.cp = cp;
    }

    public GroupIterator getGroupIterator() {
        return this.gi;
    }

    public void setGroupIterator(GroupIterator gi) {
        if (gi == null) {
            throw new IllegalArgumentException("GroupIterator cannot be null.");
        }
        this.gi = gi;
    }

    public boolean getConnect() {
        return this.connect;
    }

    public void setConnect(boolean b) {
        this.connect = b;
    }

    public void resetMemory() {
    }
}
