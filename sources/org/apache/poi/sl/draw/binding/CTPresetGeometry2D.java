package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_PresetGeometry2D", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"avLst"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTPresetGeometry2D.class */
public class CTPresetGeometry2D {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected CTGeomGuideList avLst;

    @XmlAttribute(required = true)
    protected STShapeType prst;

    public CTGeomGuideList getAvLst() {
        return this.avLst;
    }

    public void setAvLst(CTGeomGuideList value) {
        this.avLst = value;
    }

    public boolean isSetAvLst() {
        return this.avLst != null;
    }

    public STShapeType getPrst() {
        return this.prst;
    }

    public void setPrst(STShapeType value) {
        this.prst = value;
    }

    public boolean isSetPrst() {
        return this.prst != null;
    }
}
