package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Shape.class */
public interface Shape {
    String getShapeName();

    Shape getParent();

    ChildAnchor getAnchor();

    boolean isNoFill();

    void setNoFill(boolean z);

    void setFillColor(int i, int i2, int i3);

    void setLineStyleColor(int i, int i2, int i3);
}
