package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.ConnectType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/ConnectTypeImpl.class */
public class ConnectTypeImpl extends XmlComplexContentImpl implements ConnectType {
    private static final QName FROMSHEET$0 = new QName("", "FromSheet");
    private static final QName FROMCELL$2 = new QName("", "FromCell");
    private static final QName FROMPART$4 = new QName("", "FromPart");
    private static final QName TOSHEET$6 = new QName("", "ToSheet");
    private static final QName TOCELL$8 = new QName("", "ToCell");
    private static final QName TOPART$10 = new QName("", "ToPart");

    public ConnectTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public long getFromSheet() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMSHEET$0);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public XmlUnsignedInt xgetFromSheet() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(FROMSHEET$0);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void setFromSheet(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMSHEET$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FROMSHEET$0);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void xsetFromSheet(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FROMSHEET$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FROMSHEET$0);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public String getFromCell() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMCELL$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public XmlString xgetFromCell() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(FROMCELL$2);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public boolean isSetFromCell() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FROMCELL$2) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void setFromCell(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMCELL$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FROMCELL$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void xsetFromCell(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(FROMCELL$2);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(FROMCELL$2);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void unsetFromCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FROMCELL$2);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public int getFromPart() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMPART$4);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public XmlInt xgetFromPart() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlInt = (XmlInt) get_store().find_attribute_user(FROMPART$4);
        }
        return xmlInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public boolean isSetFromPart() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FROMPART$4) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void setFromPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMPART$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FROMPART$4);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void xsetFromPart(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(FROMPART$4);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(FROMPART$4);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void unsetFromPart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FROMPART$4);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public long getToSheet() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOSHEET$6);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public XmlUnsignedInt xgetToSheet() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(TOSHEET$6);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void setToSheet(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOSHEET$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOSHEET$6);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void xsetToSheet(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(TOSHEET$6);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(TOSHEET$6);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public String getToCell() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOCELL$8);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public XmlString xgetToCell() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(TOCELL$8);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public boolean isSetToCell() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOCELL$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void setToCell(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOCELL$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOCELL$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void xsetToCell(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(TOCELL$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(TOCELL$8);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void unsetToCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOCELL$8);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public int getToPart() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPART$10);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public XmlInt xgetToPart() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlInt = (XmlInt) get_store().find_attribute_user(TOPART$10);
        }
        return xmlInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public boolean isSetToPart() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOPART$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void setToPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPART$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOPART$10);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void xsetToPart(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(TOPART$10);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(TOPART$10);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ConnectType
    public void unsetToPart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOPART$10);
        }
    }
}
