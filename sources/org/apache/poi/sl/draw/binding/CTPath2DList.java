package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DList", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {Cookie2.PATH})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTPath2DList.class */
public class CTPath2DList {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected List<CTPath2D> path;

    public List<CTPath2D> getPath() {
        if (this.path == null) {
            this.path = new ArrayList();
        }
        return this.path;
    }

    public boolean isSetPath() {
        return (this.path == null || this.path.isEmpty()) ? false : true;
    }

    public void unsetPath() {
        this.path = null;
    }
}
