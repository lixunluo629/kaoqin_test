package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Connection", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTConnection.class */
public class CTConnection {

    @XmlAttribute(required = true)
    protected long id;

    @XmlSchemaType(name = "unsignedInt")
    @XmlAttribute(required = true)
    protected long idx;

    public long getId() {
        return this.id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public boolean isSetId() {
        return true;
    }

    public long getIdx() {
        return this.idx;
    }

    public void setIdx(long value) {
        this.idx = value;
    }

    public boolean isSetIdx() {
        return true;
    }
}
