package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2D", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"closeOrMoveToOrLnTo"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTPath2D.class */
public class CTPath2D {

    @XmlElements({@XmlElement(name = "lnTo", namespace = XSSFRelation.NS_DRAWINGML, type = CTPath2DLineTo.class), @XmlElement(name = "close", namespace = XSSFRelation.NS_DRAWINGML, type = CTPath2DClose.class), @XmlElement(name = "cubicBezTo", namespace = XSSFRelation.NS_DRAWINGML, type = CTPath2DCubicBezierTo.class), @XmlElement(name = "quadBezTo", namespace = XSSFRelation.NS_DRAWINGML, type = CTPath2DQuadBezierTo.class), @XmlElement(name = "arcTo", namespace = XSSFRelation.NS_DRAWINGML, type = CTPath2DArcTo.class), @XmlElement(name = "moveTo", namespace = XSSFRelation.NS_DRAWINGML, type = CTPath2DMoveTo.class)})
    protected List<Object> closeOrMoveToOrLnTo;

    @XmlAttribute
    protected Long w;

    @XmlAttribute
    protected Long h;

    @XmlAttribute
    protected STPathFillMode fill;

    @XmlAttribute
    protected Boolean stroke;

    @XmlAttribute
    protected Boolean extrusionOk;

    public List<Object> getCloseOrMoveToOrLnTo() {
        if (this.closeOrMoveToOrLnTo == null) {
            this.closeOrMoveToOrLnTo = new ArrayList();
        }
        return this.closeOrMoveToOrLnTo;
    }

    public boolean isSetCloseOrMoveToOrLnTo() {
        return (this.closeOrMoveToOrLnTo == null || this.closeOrMoveToOrLnTo.isEmpty()) ? false : true;
    }

    public void unsetCloseOrMoveToOrLnTo() {
        this.closeOrMoveToOrLnTo = null;
    }

    public long getW() {
        if (this.w == null) {
            return 0L;
        }
        return this.w.longValue();
    }

    public void setW(long value) {
        this.w = Long.valueOf(value);
    }

    public boolean isSetW() {
        return this.w != null;
    }

    public void unsetW() {
        this.w = null;
    }

    public long getH() {
        if (this.h == null) {
            return 0L;
        }
        return this.h.longValue();
    }

    public void setH(long value) {
        this.h = Long.valueOf(value);
    }

    public boolean isSetH() {
        return this.h != null;
    }

    public void unsetH() {
        this.h = null;
    }

    public STPathFillMode getFill() {
        if (this.fill == null) {
            return STPathFillMode.NORM;
        }
        return this.fill;
    }

    public void setFill(STPathFillMode value) {
        this.fill = value;
    }

    public boolean isSetFill() {
        return this.fill != null;
    }

    public boolean isStroke() {
        if (this.stroke == null) {
            return true;
        }
        return this.stroke.booleanValue();
    }

    public void setStroke(boolean value) {
        this.stroke = Boolean.valueOf(value);
    }

    public boolean isSetStroke() {
        return this.stroke != null;
    }

    public void unsetStroke() {
        this.stroke = null;
    }

    public boolean isExtrusionOk() {
        if (this.extrusionOk == null) {
            return true;
        }
        return this.extrusionOk.booleanValue();
    }

    public void setExtrusionOk(boolean value) {
        this.extrusionOk = Boolean.valueOf(value);
    }

    public boolean isSetExtrusionOk() {
        return this.extrusionOk != null;
    }

    public void unsetExtrusionOk() {
        this.extrusionOk = null;
    }
}
