package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Vector3D", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTVector3D.class */
public class CTVector3D {

    @XmlAttribute(required = true)
    protected long dx;

    @XmlAttribute(required = true)
    protected long dy;

    @XmlAttribute(required = true)
    protected long dz;

    public long getDx() {
        return this.dx;
    }

    public void setDx(long value) {
        this.dx = value;
    }

    public boolean isSetDx() {
        return true;
    }

    public long getDy() {
        return this.dy;
    }

    public void setDy(long value) {
        this.dy = value;
    }

    public boolean isSetDy() {
        return true;
    }

    public long getDz() {
        return this.dz;
    }

    public void setDz(long value) {
        this.dz = value;
    }

    public boolean isSetDz() {
        return true;
    }
}
