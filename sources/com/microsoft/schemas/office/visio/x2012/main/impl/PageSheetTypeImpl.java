package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.PageSheetType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/PageSheetTypeImpl.class */
public class PageSheetTypeImpl extends SheetTypeImpl implements PageSheetType {
    private static final QName UNIQUEID$0 = new QName("", "UniqueID");

    public PageSheetTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageSheetType
    public String getUniqueID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEID$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageSheetType
    public XmlString xgetUniqueID() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(UNIQUEID$0);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageSheetType
    public boolean isSetUniqueID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UNIQUEID$0) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageSheetType
    public void setUniqueID(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEID$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UNIQUEID$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageSheetType
    public void xsetUniqueID(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(UNIQUEID$0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(UNIQUEID$0);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageSheetType
    public void unsetUniqueID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UNIQUEID$0);
        }
    }
}
