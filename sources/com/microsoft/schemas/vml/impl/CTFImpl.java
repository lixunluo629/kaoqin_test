package com.microsoft.schemas.vml.impl;

import com.microsoft.schemas.vml.CTF;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/impl/CTFImpl.class */
public class CTFImpl extends XmlComplexContentImpl implements CTF {
    private static final QName EQN$0 = new QName("", "eqn");

    public CTFImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.vml.CTF
    public String getEqn() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EQN$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTF
    public XmlString xgetEqn() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(EQN$0);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTF
    public boolean isSetEqn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EQN$0) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTF
    public void setEqn(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EQN$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EQN$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTF
    public void xsetEqn(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(EQN$0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(EQN$0);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTF
    public void unsetEqn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EQN$0);
        }
    }
}
