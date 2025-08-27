package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_ConnectionSiteList", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"cxn"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTConnectionSiteList.class */
public class CTConnectionSiteList {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected List<CTConnectionSite> cxn;

    public List<CTConnectionSite> getCxn() {
        if (this.cxn == null) {
            this.cxn = new ArrayList();
        }
        return this.cxn;
    }

    public boolean isSetCxn() {
        return (this.cxn == null || this.cxn.isEmpty()) ? false : true;
    }

    public void unsetCxn() {
        this.cxn = null;
    }
}
