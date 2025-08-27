package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Percentage", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTPercentage.class */
public class CTPercentage {

    @XmlAttribute(required = true)
    protected int val;

    public int getVal() {
        return this.val;
    }

    public void setVal(int value) {
        this.val = value;
    }

    public boolean isSetVal() {
        return true;
    }
}
