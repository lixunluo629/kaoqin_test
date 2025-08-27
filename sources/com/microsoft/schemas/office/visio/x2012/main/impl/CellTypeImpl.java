package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RefByType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/CellTypeImpl.class */
public class CellTypeImpl extends XmlComplexContentImpl implements CellType {
    private static final QName REFBY$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "RefBy");
    private static final QName N$2 = new QName("", "N");
    private static final QName U$4 = new QName("", "U");
    private static final QName E$6 = new QName("", "E");
    private static final QName F$8 = new QName("", "F");
    private static final QName V$10 = new QName("", "V");

    public CellTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public List<RefByType> getRefByList() {
        1RefByList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1RefByList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public RefByType[] getRefByArray() {
        RefByType[] refByTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(REFBY$0, arrayList);
            refByTypeArr = new RefByType[arrayList.size()];
            arrayList.toArray(refByTypeArr);
        }
        return refByTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public RefByType getRefByArray(int i) {
        RefByType refByTypeFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            refByTypeFind_element_user = get_store().find_element_user(REFBY$0, i);
            if (refByTypeFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return refByTypeFind_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public int sizeOfRefByArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(REFBY$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setRefByArray(RefByType[] refByTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper((XmlObject[]) refByTypeArr, REFBY$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setRefByArray(int i, RefByType refByType) {
        synchronized (monitor()) {
            check_orphaned();
            RefByType refByTypeFind_element_user = get_store().find_element_user(REFBY$0, i);
            if (refByTypeFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
            refByTypeFind_element_user.set(refByType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public RefByType insertNewRefBy(int i) {
        RefByType refByTypeInsert_element_user;
        synchronized (monitor()) {
            check_orphaned();
            refByTypeInsert_element_user = get_store().insert_element_user(REFBY$0, i);
        }
        return refByTypeInsert_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public RefByType addNewRefBy() {
        RefByType refByTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            refByTypeAdd_element_user = get_store().add_element_user(REFBY$0);
        }
        return refByTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void removeRefBy(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(REFBY$0, i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public String getN() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(N$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public XmlString xgetN() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(N$2);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setN(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(N$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(N$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void xsetN(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(N$2);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(N$2);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public String getU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(U$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public XmlString xgetU() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(U$4);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public boolean isSetU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(U$4) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setU(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(U$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(U$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void xsetU(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(U$4);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(U$4);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void unsetU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(U$4);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public String getE() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(E$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public XmlString xgetE() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(E$6);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public boolean isSetE() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(E$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setE(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(E$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(E$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void xsetE(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(E$6);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(E$6);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void unsetE() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(E$6);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public String getF() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(F$8);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public XmlString xgetF() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(F$8);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public boolean isSetF() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(F$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setF(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(F$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(F$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void xsetF(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(F$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(F$8);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void unsetF() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(F$8);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public String getV() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(V$10);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public XmlString xgetV() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(V$10);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public boolean isSetV() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(V$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void setV(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(V$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(V$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void xsetV(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(V$10);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(V$10);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.CellType
    public void unsetV() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(V$10);
        }
    }
}
