package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/ValueVectorListener.class */
public interface ValueVectorListener {
    void valueChanged(ValueVector valueVector, int i, double d);

    void valueInserted(ValueVector valueVector, int i, double d);

    void valueRemoved(ValueVector valueVector, int i, double d);
}
