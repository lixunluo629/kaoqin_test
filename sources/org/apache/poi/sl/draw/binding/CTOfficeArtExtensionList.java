package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_OfficeArtExtensionList", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {"ext"})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTOfficeArtExtensionList.class */
public class CTOfficeArtExtensionList {

    @XmlElement(namespace = XSSFRelation.NS_DRAWINGML)
    protected List<CTOfficeArtExtension> ext;

    public List<CTOfficeArtExtension> getExt() {
        if (this.ext == null) {
            this.ext = new ArrayList();
        }
        return this.ext;
    }

    public boolean isSetExt() {
        return (this.ext == null || this.ext.isEmpty()) ? false : true;
    }

    public void unsetExt() {
        this.ext = null;
    }
}
