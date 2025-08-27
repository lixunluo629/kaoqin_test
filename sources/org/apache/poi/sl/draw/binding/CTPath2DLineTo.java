package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DLineTo", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"pt"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTPath2DLineTo.class */
public class CTPath2DLineTo {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML, required = true)
    protected CTAdjPoint2D pt;

    public CTAdjPoint2D getPt() {
        return this.pt;
    }

    public void setPt(CTAdjPoint2D value) {
        this.pt = value;
    }

    public boolean isSetPt() {
        return this.pt != null;
    }
}
