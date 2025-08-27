package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.usermodel.Shape;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Drawing.class */
public interface Drawing<T extends Shape> extends ShapeContainer<T> {
    Picture createPicture(ClientAnchor clientAnchor, int i);

    Comment createCellComment(ClientAnchor clientAnchor);

    Chart createChart(ClientAnchor clientAnchor);

    ClientAnchor createAnchor(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    ObjectData createObjectData(ClientAnchor clientAnchor, int i, int i2);
}
