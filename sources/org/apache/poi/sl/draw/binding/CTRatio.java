package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Ratio", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTRatio.class */
public class CTRatio {

    @XmlAttribute(required = true)
    protected long n;

    @XmlAttribute(required = true)
    protected long d;

    public long getN() {
        return this.n;
    }

    public void setN(long value) {
        this.n = value;
    }

    public boolean isSetN() {
        return true;
    }

    public long getD() {
        return this.d;
    }

    public void setD(long value) {
        this.d = value;
    }

    public boolean isSetD() {
        return true;
    }
}
