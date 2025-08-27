package org.apache.poi.xdgf.usermodel.section.geometry;

import com.microsoft.schemas.office.visio.x2012.main.RowType;
import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.util.ObjectFactory;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/geometry/GeometryRowFactory.class */
public class GeometryRowFactory {
    static final ObjectFactory<GeometryRow, RowType> _rowTypes = new ObjectFactory<>();

    static {
        try {
            _rowTypes.put("ArcTo", ArcTo.class, RowType.class);
            _rowTypes.put("Ellipse", Ellipse.class, RowType.class);
            _rowTypes.put("EllipticalArcTo", EllipticalArcTo.class, RowType.class);
            _rowTypes.put("InfiniteLine", InfiniteLine.class, RowType.class);
            _rowTypes.put("LineTo", LineTo.class, RowType.class);
            _rowTypes.put("MoveTo", MoveTo.class, RowType.class);
            _rowTypes.put("NURBSTo", NURBSTo.class, RowType.class);
            _rowTypes.put("PolylineTo", PolyLineTo.class, RowType.class);
            _rowTypes.put("PolyLineTo", PolyLineTo.class, RowType.class);
            _rowTypes.put("RelCubBezTo", RelCubBezTo.class, RowType.class);
            _rowTypes.put("RelEllipticalArcTo", RelEllipticalArcTo.class, RowType.class);
            _rowTypes.put("RelLineTo", RelLineTo.class, RowType.class);
            _rowTypes.put("RelMoveTo", RelMoveTo.class, RowType.class);
            _rowTypes.put("RelQuadBezTo", RelQuadBezTo.class, RowType.class);
            _rowTypes.put("SplineKnot", SplineKnot.class, RowType.class);
            _rowTypes.put("SplineStart", SplineStart.class, RowType.class);
        } catch (NoSuchMethodException e) {
            throw new POIXMLException("Internal error", e);
        } catch (SecurityException e2) {
            throw new POIXMLException("Internal error", e2);
        }
    }

    public static GeometryRow load(RowType row) {
        return _rowTypes.load(row.getT(), row);
    }
}
