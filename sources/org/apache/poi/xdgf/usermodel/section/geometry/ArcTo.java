package org.apache.poi.xdgf.usermodel.section.geometry;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/geometry/ArcTo.class */
public class ArcTo implements GeometryRow {
    ArcTo _master = null;
    Double x;
    Double y;
    Double a;
    Boolean deleted;

    public ArcTo(RowType row) {
        this.x = null;
        this.y = null;
        this.a = null;
        this.deleted = null;
        if (row.isSetDel()) {
            this.deleted = Boolean.valueOf(row.getDel());
        }
        CellType[] arr$ = row.getCellArray();
        for (CellType cell : arr$) {
            String cellName = cell.getN();
            if (cellName.equals("X")) {
                this.x = XDGFCell.parseDoubleValue(cell);
            } else if (cellName.equals("Y")) {
                this.y = XDGFCell.parseDoubleValue(cell);
            } else if (cellName.equals("A")) {
                this.a = XDGFCell.parseDoubleValue(cell);
            } else {
                throw new POIXMLException("Invalid cell '" + cellName + "' in ArcTo row");
            }
        }
    }

    public boolean getDel() {
        if (this.deleted != null) {
            return this.deleted.booleanValue();
        }
        if (this._master != null) {
            return this._master.getDel();
        }
        return false;
    }

    public Double getX() {
        return this.x == null ? this._master.x : this.x;
    }

    public Double getY() {
        return this.y == null ? this._master.y : this.y;
    }

    public Double getA() {
        return this.a == null ? this._master.a : this.a;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    public void setupMaster(GeometryRow row) {
        this._master = (ArcTo) row;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    public void addToPath(Path2D.Double path, XDGFShape parent) {
        if (getDel()) {
            return;
        }
        Point2D last = path.getCurrentPoint();
        double x = getX().doubleValue();
        double y = getY().doubleValue();
        double a = getA().doubleValue();
        if (a == 0.0d) {
            path.lineTo(x, y);
            return;
        }
        double x0 = last.getX();
        double y0 = last.getY();
        double chordLength = Math.hypot(y - y0, x - x0);
        double radius = (((4.0d * a) * a) + (chordLength * chordLength)) / (8.0d * Math.abs(a));
        double cx = x0 + ((x - x0) / 2.0d);
        double cy = y0 + ((y - y0) / 2.0d);
        double rotate = Math.atan2(y - cy, x - cx);
        path.append(AffineTransform.getRotateInstance(rotate, x0, y0).createTransformedShape(new Arc2D.Double(x0, y0 - radius, chordLength, 2.0d * radius, 180.0d, x0 < x ? 180.0d : -180.0d, 0)), true);
    }
}
