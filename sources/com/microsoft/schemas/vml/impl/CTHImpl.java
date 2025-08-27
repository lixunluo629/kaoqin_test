package com.microsoft.schemas.vml.impl;

import com.microsoft.schemas.vml.CTH;
import com.microsoft.schemas.vml.STTrueFalse;
import com.microsoft.schemas.vml.STTrueFalseBlank;
import com.microsoft.schemas.vml.STTrueFalseBlank$Enum;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/impl/CTHImpl.class */
public class CTHImpl extends XmlComplexContentImpl implements CTH {
    private static final QName POSITION$0 = new QName("", "position");
    private static final QName POLAR$2 = new QName("", "polar");
    private static final QName MAP$4 = new QName("", BeanDefinitionParserDelegate.MAP_ELEMENT);
    private static final QName INVX$6 = new QName("", "invx");
    private static final QName INVY$8 = new QName("", "invy");
    private static final QName SWITCH$10 = new QName("", "switch");
    private static final QName XRANGE$12 = new QName("", "xrange");
    private static final QName YRANGE$14 = new QName("", "yrange");
    private static final QName RADIUSRANGE$16 = new QName("", "radiusrange");

    public CTHImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.vml.CTH
    public String getPosition() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POSITION$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public XmlString xgetPosition() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(POSITION$0);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetPosition() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(POSITION$0) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setPosition(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POSITION$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(POSITION$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetPosition(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(POSITION$0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(POSITION$0);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetPosition() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(POSITION$0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public String getPolar() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POLAR$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public XmlString xgetPolar() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(POLAR$2);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetPolar() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(POLAR$2) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setPolar(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POLAR$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(POLAR$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetPolar(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(POLAR$2);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(POLAR$2);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetPolar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(POLAR$2);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public String getMap() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MAP$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public XmlString xgetMap() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(MAP$4);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetMap() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MAP$4) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setMap(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MAP$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MAP$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetMap(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(MAP$4);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(MAP$4);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetMap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MAP$4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public STTrueFalse.Enum getInvx() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INVX$6);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public STTrueFalse xgetInvx() {
        STTrueFalse sTTrueFalse;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalse = (STTrueFalse) get_store().find_attribute_user(INVX$6);
        }
        return sTTrueFalse;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetInvx() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INVX$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setInvx(STTrueFalse.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INVX$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INVX$6);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetInvx(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalse2 = (STTrueFalse) get_store().find_attribute_user(INVX$6);
            if (sTTrueFalse2 == null) {
                sTTrueFalse2 = (STTrueFalse) get_store().add_attribute_user(INVX$6);
            }
            sTTrueFalse2.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetInvx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INVX$6);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public STTrueFalse.Enum getInvy() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INVY$8);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public STTrueFalse xgetInvy() {
        STTrueFalse sTTrueFalse;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalse = (STTrueFalse) get_store().find_attribute_user(INVY$8);
        }
        return sTTrueFalse;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetInvy() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INVY$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setInvy(STTrueFalse.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INVY$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INVY$8);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetInvy(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalse2 = (STTrueFalse) get_store().find_attribute_user(INVY$8);
            if (sTTrueFalse2 == null) {
                sTTrueFalse2 = (STTrueFalse) get_store().add_attribute_user(INVY$8);
            }
            sTTrueFalse2.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetInvy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INVY$8);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public STTrueFalseBlank$Enum getSwitch() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SWITCH$10);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalseBlank$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public STTrueFalseBlank xgetSwitch() {
        STTrueFalseBlank sTTrueFalseBlankFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseBlankFind_attribute_user = get_store().find_attribute_user(SWITCH$10);
        }
        return sTTrueFalseBlankFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetSwitch() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SWITCH$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setSwitch(STTrueFalseBlank$Enum sTTrueFalseBlank$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SWITCH$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SWITCH$10);
            }
            simpleValue.setEnumValue(sTTrueFalseBlank$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetSwitch(STTrueFalseBlank sTTrueFalseBlank) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalseBlank sTTrueFalseBlankFind_attribute_user = get_store().find_attribute_user(SWITCH$10);
            if (sTTrueFalseBlankFind_attribute_user == null) {
                sTTrueFalseBlankFind_attribute_user = (STTrueFalseBlank) get_store().add_attribute_user(SWITCH$10);
            }
            sTTrueFalseBlankFind_attribute_user.set(sTTrueFalseBlank);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetSwitch() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SWITCH$10);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public String getXrange() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(XRANGE$12);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public XmlString xgetXrange() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(XRANGE$12);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetXrange() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(XRANGE$12) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setXrange(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(XRANGE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(XRANGE$12);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetXrange(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(XRANGE$12);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(XRANGE$12);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetXrange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(XRANGE$12);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public String getYrange() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(YRANGE$14);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public XmlString xgetYrange() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(YRANGE$14);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetYrange() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(YRANGE$14) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setYrange(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(YRANGE$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(YRANGE$14);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetYrange(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(YRANGE$14);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(YRANGE$14);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetYrange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(YRANGE$14);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public String getRadiusrange() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RADIUSRANGE$16);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public XmlString xgetRadiusrange() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(RADIUSRANGE$16);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public boolean isSetRadiusrange() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RADIUSRANGE$16) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void setRadiusrange(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RADIUSRANGE$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RADIUSRANGE$16);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void xsetRadiusrange(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(RADIUSRANGE$16);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(RADIUSRANGE$16);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTH
    public void unsetRadiusrange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RADIUSRANGE$16);
        }
    }
}
