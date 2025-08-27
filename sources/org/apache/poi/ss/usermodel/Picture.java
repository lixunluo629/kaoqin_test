package org.apache.poi.ss.usermodel;

import java.awt.Dimension;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Picture.class */
public interface Picture extends Shape {
    void resize();

    void resize(double d);

    void resize(double d, double d2);

    ClientAnchor getPreferredSize();

    ClientAnchor getPreferredSize(double d, double d2);

    Dimension getImageDimension();

    PictureData getPictureData();

    ClientAnchor getClientAnchor();

    Sheet getSheet();
}
