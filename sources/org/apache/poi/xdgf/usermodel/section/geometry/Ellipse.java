package org.apache.poi.xdgf.usermodel.section.geometry;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/geometry/Ellipse.class */
public class Ellipse implements GeometryRow {
    Ellipse _master = null;
    Double x;
    Double y;
    Double a;
    Double b;
    Double c;
    Double d;
    Boolean deleted;

    public Ellipse(RowType row) {
        this.x = null;
        this.y = null;
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
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
            } else if (cellName.equals("B")) {
                this.b = XDGFCell.parseDoubleValue(cell);
            } else if (cellName.equals("C")) {
                this.c = XDGFCell.parseDoubleValue(cell);
            } else if (cellName.equals("D")) {
                this.d = XDGFCell.parseDoubleValue(cell);
            } else {
                throw new POIXMLException("Invalid cell '" + cellName + "' in Ellipse row");
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

    public Double getB() {
        return this.b == null ? this._master.b : this.b;
    }

    public Double getC() {
        return this.c == null ? this._master.c : this.c;
    }

    public Double getD() {
        return this.d == null ? this._master.d : this.d;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    public void setupMaster(GeometryRow row) {
        this._master = (Ellipse) row;
    }

    public Path2D.Double getPath() {
        if (getDel()) {
            return null;
        }
        double cx = getX().doubleValue();
        double cy = getY().doubleValue();
        double a = getA().doubleValue();
        double b = getB().doubleValue();
        double c = getC().doubleValue();
        double d = getD().doubleValue();
        double rx = Math.hypot(a - cx, b - cy);
        double ry = Math.hypot(c - cx, d - cy);
        double angle = (6.283185307179586d + ((cy > b ? 1.0d : -1.0d) * Math.acos((cx - a) / rx))) % 6.283185307179586d;
        Ellipse2D.Double ellipse = new Ellipse2D.Double(cx - rx, cy - ry, rx * 2.0d, ry * 2.0d);
        Path2D.Double path = new Path2D.Double(ellipse);
        AffineTransform tr = new AffineTransform();
        tr.rotate(angle, cx, cy);
        path.transform(tr);
        return path;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    public void addToPath(Path2D.Double path, XDGFShape parent) {
        throw new POIXMLException("Ellipse elements cannot be part of a path");
    }
}
