package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Scale2D", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"sx", "sy"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTScale2D.class */
public class CTScale2D {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML, required = true)
    protected CTRatio sx;

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML, required = true)
    protected CTRatio sy;

    public CTRatio getSx() {
        return this.sx;
    }

    public void setSx(CTRatio value) {
        this.sx = value;
    }

    public boolean isSetSx() {
        return this.sx != null;
    }

    public CTRatio getSy() {
        return this.sy;
    }

    public void setSy(CTRatio value) {
        this.sy = value;
    }

    public boolean isSetSy() {
        return this.sy != null;
    }
}
