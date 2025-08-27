package com.microsoft.schemas.office.office.impl;

import com.microsoft.schemas.office.office.CTIdMap;
import com.microsoft.schemas.vml.STExt;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/impl/CTIdMapImpl.class */
public class CTIdMapImpl extends XmlComplexContentImpl implements CTIdMap {
    private static final QName EXT$0 = new QName("urn:schemas-microsoft-com:vml", "ext");
    private static final QName DATA$2 = new QName("", "data");

    public CTIdMapImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public STExt.Enum getExt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EXT$0);
            if (simpleValue == null) {
                return null;
            }
            return (STExt.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public STExt xgetExt() {
        STExt sTExt;
        synchronized (monitor()) {
            check_orphaned();
            sTExt = (STExt) get_store().find_attribute_user(EXT$0);
        }
        return sTExt;
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public boolean isSetExt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EXT$0) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public void setExt(STExt.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EXT$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EXT$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public void xsetExt(STExt sTExt) {
        synchronized (monitor()) {
            check_orphaned();
            STExt sTExt2 = (STExt) get_store().find_attribute_user(EXT$0);
            if (sTExt2 == null) {
                sTExt2 = (STExt) get_store().add_attribute_user(EXT$0);
            }
            sTExt2.set(sTExt);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public void unsetExt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EXT$0);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public String getData() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATA$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public XmlString xgetData() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(DATA$2);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public boolean isSetData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATA$2) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public void setData(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATA$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATA$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public void xsetData(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(DATA$2);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(DATA$2);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTIdMap
    public void unsetData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATA$2);
        }
    }
}
