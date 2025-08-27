package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_AdjustHandleList", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"ahXYOrAhPolar"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTAdjustHandleList.class */
public class CTAdjustHandleList {

    @XmlElements({@XmlElement(name = "ahXY", namespace = XSSFRelation.NS_DRAWINGML, type = CTXYAdjustHandle.class), @XmlElement(name = "ahPolar", namespace = XSSFRelation.NS_DRAWINGML, type = CTPolarAdjustHandle.class)})
    protected List<Object> ahXYOrAhPolar;

    public List<Object> getAhXYOrAhPolar() {
        if (this.ahXYOrAhPolar == null) {
            this.ahXYOrAhPolar = new ArrayList();
        }
        return this.ahXYOrAhPolar;
    }

    public boolean isSetAhXYOrAhPolar() {
        return (this.ahXYOrAhPolar == null || this.ahXYOrAhPolar.isEmpty()) ? false : true;
    }

    public void unsetAhXYOrAhPolar() {
        this.ahXYOrAhPolar = null;
    }
}
