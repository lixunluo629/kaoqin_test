package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleElement;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTTableStyleImpl.class */
public class CTTableStyleImpl extends XmlComplexContentImpl implements CTTableStyle {
    private static final QName TABLESTYLEELEMENT$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "tableStyleElement");
    private static final QName NAME$2 = new QName("", "name");
    private static final QName PIVOT$4 = new QName("", "pivot");
    private static final QName TABLE$6 = new QName("", "table");
    private static final QName COUNT$8 = new QName("", "count");

    public CTTableStyleImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public List<CTTableStyleElement> getTableStyleElementList() {
        AbstractList<CTTableStyleElement> abstractList;
        synchronized (monitor()) {
            check_orphaned();
            abstractList = new AbstractList<CTTableStyleElement>() { // from class: org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTTableStyleImpl.1TableStyleElementList
                @Override // java.util.AbstractList, java.util.List
                public CTTableStyleElement get(int i) {
                    return CTTableStyleImpl.this.getTableStyleElementArray(i);
                }

                @Override // java.util.AbstractList, java.util.List
                public CTTableStyleElement set(int i, CTTableStyleElement cTTableStyleElement) {
                    CTTableStyleElement tableStyleElementArray = CTTableStyleImpl.this.getTableStyleElementArray(i);
                    CTTableStyleImpl.this.setTableStyleElementArray(i, cTTableStyleElement);
                    return tableStyleElementArray;
                }

                @Override // java.util.AbstractList, java.util.List
                public void add(int i, CTTableStyleElement cTTableStyleElement) {
                    CTTableStyleImpl.this.insertNewTableStyleElement(i).set(cTTableStyleElement);
                }

                @Override // java.util.AbstractList, java.util.List
                public CTTableStyleElement remove(int i) {
                    CTTableStyleElement tableStyleElementArray = CTTableStyleImpl.this.getTableStyleElementArray(i);
                    CTTableStyleImpl.this.removeTableStyleElement(i);
                    return tableStyleElementArray;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return CTTableStyleImpl.this.sizeOfTableStyleElementArray();
                }
            };
        }
        return abstractList;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public CTTableStyleElement[] getTableStyleElementArray() {
        CTTableStyleElement[] cTTableStyleElementArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(TABLESTYLEELEMENT$0, arrayList);
            cTTableStyleElementArr = new CTTableStyleElement[arrayList.size()];
            arrayList.toArray(cTTableStyleElementArr);
        }
        return cTTableStyleElementArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public CTTableStyleElement getTableStyleElementArray(int i) {
        CTTableStyleElement cTTableStyleElement;
        synchronized (monitor()) {
            check_orphaned();
            cTTableStyleElement = (CTTableStyleElement) get_store().find_element_user(TABLESTYLEELEMENT$0, i);
            if (cTTableStyleElement == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTTableStyleElement;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public int sizeOfTableStyleElementArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(TABLESTYLEELEMENT$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void setTableStyleElementArray(CTTableStyleElement[] cTTableStyleElementArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTTableStyleElementArr, TABLESTYLEELEMENT$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void setTableStyleElementArray(int i, CTTableStyleElement cTTableStyleElement) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleElement cTTableStyleElement2 = (CTTableStyleElement) get_store().find_element_user(TABLESTYLEELEMENT$0, i);
            if (cTTableStyleElement2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTTableStyleElement2.set(cTTableStyleElement);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public CTTableStyleElement insertNewTableStyleElement(int i) {
        CTTableStyleElement cTTableStyleElement;
        synchronized (monitor()) {
            check_orphaned();
            cTTableStyleElement = (CTTableStyleElement) get_store().insert_element_user(TABLESTYLEELEMENT$0, i);
        }
        return cTTableStyleElement;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public CTTableStyleElement addNewTableStyleElement() {
        CTTableStyleElement cTTableStyleElement;
        synchronized (monitor()) {
            check_orphaned();
            cTTableStyleElement = (CTTableStyleElement) get_store().add_element_user(TABLESTYLEELEMENT$0);
        }
        return cTTableStyleElement;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void removeTableStyleElement(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TABLESTYLEELEMENT$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public XmlString xgetName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAME$2);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void xsetName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAME$2);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAME$2);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public boolean getPivot() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PIVOT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PIVOT$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public XmlBoolean xgetPivot() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PIVOT$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PIVOT$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public boolean isSetPivot() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PIVOT$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void setPivot(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PIVOT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PIVOT$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void xsetPivot(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PIVOT$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PIVOT$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void unsetPivot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PIVOT$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public boolean getTable() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABLE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TABLE$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public XmlBoolean xgetTable() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TABLE$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TABLE$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public boolean isSetTable() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TABLE$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void setTable(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABLE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TABLE$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void xsetTable(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TABLE$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TABLE$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void unsetTable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TABLE$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public long getCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNT$8);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public XmlUnsignedInt xgetCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$8);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public boolean isSetCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNT$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void setCount(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COUNT$8);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void xsetCount(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(COUNT$8);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNT$8);
        }
    }
}
