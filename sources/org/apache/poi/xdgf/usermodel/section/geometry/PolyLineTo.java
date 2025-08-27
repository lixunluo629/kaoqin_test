package org.apache.poi.xdgf.usermodel.section.geometry;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;
import java.awt.geom.Path2D;
import org.apache.poi.POIXMLException;
import org.apache.poi.util.NotImplemented;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/section/geometry/PolyLineTo.class */
public class PolyLineTo implements GeometryRow {
    PolyLineTo _master = null;
    Double x;
    Double y;
    String a;
    Boolean deleted;

    public PolyLineTo(RowType row) {
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
                this.a = cell.getV();
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

    public String getA() {
        return this.a == null ? this._master.a : this.a;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    public void setupMaster(GeometryRow row) {
        this._master = (PolyLineTo) row;
    }

    @Override // org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
    @NotImplemented
    public void addToPath(Path2D.Double path, XDGFShape parent) {
        if (getDel()) {
        } else {
            throw new POIXMLException("Polyline support not implemented");
        }
    }
}
