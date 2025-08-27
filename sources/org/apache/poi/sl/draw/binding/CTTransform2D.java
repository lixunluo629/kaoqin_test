package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Transform2D", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {CustomBooleanEditor.VALUE_OFF, "ext"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTTransform2D.class */
public class CTTransform2D {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected CTPoint2D off;

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected CTPositiveSize2D ext;

    @XmlAttribute
    protected Integer rot;

    @XmlAttribute
    protected Boolean flipH;

    @XmlAttribute
    protected Boolean flipV;

    public CTPoint2D getOff() {
        return this.off;
    }

    public void setOff(CTPoint2D value) {
        this.off = value;
    }

    public boolean isSetOff() {
        return this.off != null;
    }

    public CTPositiveSize2D getExt() {
        return this.ext;
    }

    public void setExt(CTPositiveSize2D value) {
        this.ext = value;
    }

    public boolean isSetExt() {
        return this.ext != null;
    }

    public int getRot() {
        if (this.rot == null) {
            return 0;
        }
        return this.rot.intValue();
    }

    public void setRot(int value) {
        this.rot = Integer.valueOf(value);
    }

    public boolean isSetRot() {
        return this.rot != null;
    }

    public void unsetRot() {
        this.rot = null;
    }

    public boolean isFlipH() {
        if (this.flipH == null) {
            return false;
        }
        return this.flipH.booleanValue();
    }

    public void setFlipH(boolean value) {
        this.flipH = Boolean.valueOf(value);
    }

    public boolean isSetFlipH() {
        return this.flipH != null;
    }

    public void unsetFlipH() {
        this.flipH = null;
    }

    public boolean isFlipV() {
        if (this.flipV == null) {
            return false;
        }
        return this.flipV.booleanValue();
    }

    public void setFlipV(boolean value) {
        this.flipV = Boolean.valueOf(value);
    }

    public boolean isSetFlipV() {
        return this.flipV != null;
    }

    public void unsetFlipV() {
        this.flipV = null;
    }
}
