package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_GeomGuideList", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"gd"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTGeomGuideList.class */
public class CTGeomGuideList {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected List<CTGeomGuide> gd;

    public List<CTGeomGuide> getGd() {
        if (this.gd == null) {
            this.gd = new ArrayList();
        }
        return this.gd;
    }

    public boolean isSetGd() {
        return (this.gd == null || this.gd.isEmpty()) ? false : true;
    }

    public void unsetGd() {
        this.gd = null;
    }
}
