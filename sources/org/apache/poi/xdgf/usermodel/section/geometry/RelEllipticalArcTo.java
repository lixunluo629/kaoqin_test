package org.apache.poi.xdgf.usermodel.section.geometry;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;
import java.awt.geom.Path2D;
import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/geometry/RelEllipticalArcTo.class */
public class RelEllipticalArcTo implements GeometryRow {
    RelEllipticalArcTo _master = null;
    Double x;
    Double y;
    Double a;
    Double b;
    Double c;
    Double d;
    Boolean deleted;

    public RelEllipticalArcTo(RowType row) {
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
                throw new POIXMLException("Invalid cell '" + cellName + "' in RelEllipticalArcTo row");
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
        this._master = (RelEllipticalArcTo) row;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    public void addToPath(Path2D.Double path, XDGFShape parent) {
        if (getDel()) {
            return;
        }
        double w = parent.getWidth().doubleValue();
        double h = parent.getHeight().doubleValue();
        double x = getX().doubleValue() * w;
        double y = getY().doubleValue() * h;
        double a = getA().doubleValue() * w;
        double b = getB().doubleValue() * h;
        double c = getC().doubleValue();
        double d = getD().doubleValue();
        EllipticalArcTo.createEllipticalArc(x, y, a, b, c, d, path);
    }
}
