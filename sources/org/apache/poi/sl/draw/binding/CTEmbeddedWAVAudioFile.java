package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_EmbeddedWAVAudioFile", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/CTEmbeddedWAVAudioFile.class */
public class CTEmbeddedWAVAudioFile {

    @XmlAttribute(namespace = PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, required = true)
    protected String embed;

    @XmlAttribute
    protected String name;

    @XmlAttribute
    protected Boolean builtIn;

    public String getEmbed() {
        return this.embed;
    }

    public void setEmbed(String value) {
        this.embed = value;
    }

    public boolean isSetEmbed() {
        return this.embed != null;
    }

    public String getName() {
        if (this.name == null) {
            return "";
        }
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return this.name != null;
    }

    public boolean isBuiltIn() {
        if (this.builtIn == null) {
            return false;
        }
        return this.builtIn.booleanValue();
    }

    public void setBuiltIn(boolean value) {
        this.builtIn = Boolean.valueOf(value);
    }

    public boolean isSetBuiltIn() {
        return this.builtIn != null;
    }

    public void unsetBuiltIn() {
        this.builtIn = null;
    }
}
