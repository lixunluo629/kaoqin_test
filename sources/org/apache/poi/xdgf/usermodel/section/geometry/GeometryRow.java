package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.Path2D;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/geometry/GeometryRow.class */
public interface GeometryRow {
    void setupMaster(GeometryRow geometryRow);

    void addToPath(Path2D.Double r1, XDGFShape xDGFShape);
}
