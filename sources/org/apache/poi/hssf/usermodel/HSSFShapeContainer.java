package org.apache.poi.hssf.usermodel;

import java.util.List;
import org.apache.poi.ss.usermodel.ShapeContainer;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFShapeContainer.class */
public interface HSSFShapeContainer extends ShapeContainer<HSSFShape> {
    List<HSSFShape> getChildren();

    void addShape(HSSFShape hSSFShape);

    void setCoordinates(int i, int i2, int i3, int i4);

    void clear();

    int getX1();

    int getY1();

    int getX2();

    int getY2();

    boolean removeShape(HSSFShape hSSFShape);
}
