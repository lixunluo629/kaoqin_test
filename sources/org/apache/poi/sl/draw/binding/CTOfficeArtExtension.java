package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_OfficeArtExtension", namespace = XSSFRelation.NS_DRAWINGML, propOrder = {Languages.ANY})
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTOfficeArtExtension.class */
public class CTOfficeArtExtension {

    @XmlAnyElement(lax = true)
    protected Object any;

    @XmlSchemaType(name = "token")
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String uri;

    public Object getAny() {
        return this.any;
    }

    public void setAny(Object value) {
        this.any = value;
    }

    public boolean isSetAny() {
        return this.any != null;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String value) {
        this.uri = value;
    }

    public boolean isSetUri() {
        return this.uri != null;
    }
}
