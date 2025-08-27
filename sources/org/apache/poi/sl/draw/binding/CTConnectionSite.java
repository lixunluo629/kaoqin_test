package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_ConnectionSite", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"pos"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTConnectionSite.class */
public class CTConnectionSite {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML, required = true)
    protected CTAdjPoint2D pos;

    @XmlAttribute(required = true)
    protected String ang;

    public CTAdjPoint2D getPos() {
        return this.pos;
    }

    public void setPos(CTAdjPoint2D value) {
        this.pos = value;
    }

    public boolean isSetPos() {
        return this.pos != null;
    }

    public String getAng() {
        return this.ang;
    }

    public void setAng(String value) {
        this.ang = value;
    }

    public boolean isSetAng() {
        return this.ang != null;
    }
}
