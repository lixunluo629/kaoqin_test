package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_RelativeRect", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTRelativeRect.class */
public class CTRelativeRect {

    @XmlAttribute
    protected Integer l;

    @XmlAttribute
    protected Integer t;

    @XmlAttribute
    protected Integer r;

    @XmlAttribute
    protected Integer b;

    public int getL() {
        if (this.l == null) {
            return 0;
        }
        return this.l.intValue();
    }

    public void setL(int value) {
        this.l = Integer.valueOf(value);
    }

    public boolean isSetL() {
        return this.l != null;
    }

    public void unsetL() {
        this.l = null;
    }

    public int getT() {
        if (this.t == null) {
            return 0;
        }
        return this.t.intValue();
    }

    public void setT(int value) {
        this.t = Integer.valueOf(value);
    }

    public boolean isSetT() {
        return this.t != null;
    }

    public void unsetT() {
        this.t = null;
    }

    public int getR() {
        if (this.r == null) {
            return 0;
        }
        return this.r.intValue();
    }

    public void setR(int value) {
        this.r = Integer.valueOf(value);
    }

    public boolean isSetR() {
        return this.r != null;
    }

    public void unsetR() {
        this.r = null;
    }

    public int getB() {
        if (this.b == null) {
            return 0;
        }
        return this.b.intValue();
    }

    public void setB(int value) {
        this.b = Integer.valueOf(value);
    }

    public boolean isSetB() {
        return this.b != null;
    }

    public void unsetB() {
        this.b = null;
    }
}
