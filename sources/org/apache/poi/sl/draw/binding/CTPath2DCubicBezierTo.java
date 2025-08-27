package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DCubicBezierTo", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"pt"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTPath2DCubicBezierTo.class */
public class CTPath2DCubicBezierTo {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML, required = true)
    protected List<CTAdjPoint2D> pt;

    public List<CTAdjPoint2D> getPt() {
        if (this.pt == null) {
            this.pt = new ArrayList();
        }
        return this.pt;
    }

    public boolean isSetPt() {
        return (this.pt == null || this.pt.isEmpty()) ? false : true;
    }

    public void unsetPt() {
        this.pt = null;
    }
}
