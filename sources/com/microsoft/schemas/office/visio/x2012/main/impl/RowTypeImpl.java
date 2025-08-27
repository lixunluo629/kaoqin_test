package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;
import com.microsoft.schemas.office.visio.x2012.main.TriggerType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/RowTypeImpl.class */
public class RowTypeImpl extends XmlComplexContentImpl implements RowType {
    private static final QName CELL$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Cell");
    private static final QName TRIGGER$2 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Trigger");
    private static final QName N$4 = new QName("", "N");
    private static final QName LOCALNAME$6 = new QName("", "LocalName");
    private static final QName IX$8 = new QName("", "IX");
    private static final QName T$10 = new QName("", "T");
    private static final QName DEL$12 = new QName("", "Del");

    public RowTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public List<CellType> getCellList() {
        1CellList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CellList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public CellType[] getCellArray() {
        CellType[] cellTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(CELL$0, arrayList);
            cellTypeArr = new CellType[arrayList.size()];
            arrayList.toArray(cellTypeArr);
        }
        return cellTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public CellType getCellArray(int i) {
        CellType cellType;
        synchronized (monitor()) {
            check_orphaned();
            cellType = (CellType) get_store().find_element_user(CELL$0, i);
            if (cellType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cellType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public int sizeOfCellArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CELL$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setCellArray(CellType[] cellTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cellTypeArr, CELL$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setCellArray(int i, CellType cellType) {
        synchronized (monitor()) {
            check_orphaned();
            CellType cellType2 = (CellType) get_store().find_element_user(CELL$0, i);
            if (cellType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cellType2.set(cellType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public CellType insertNewCell(int i) {
        CellType cellType;
        synchronized (monitor()) {
            check_orphaned();
            cellType = (CellType) get_store().insert_element_user(CELL$0, i);
        }
        return cellType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public CellType addNewCell() {
        CellType cellType;
        synchronized (monitor()) {
            check_orphaned();
            cellType = (CellType) get_store().add_element_user(CELL$0);
        }
        return cellType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void removeCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CELL$0, i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public List<TriggerType> getTriggerList() {
        1TriggerList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1TriggerList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public TriggerType[] getTriggerArray() {
        TriggerType[] triggerTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(TRIGGER$2, arrayList);
            triggerTypeArr = new TriggerType[arrayList.size()];
            arrayList.toArray(triggerTypeArr);
        }
        return triggerTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public TriggerType getTriggerArray(int i) {
        TriggerType triggerTypeFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            triggerTypeFind_element_user = get_store().find_element_user(TRIGGER$2, i);
            if (triggerTypeFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return triggerTypeFind_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public int sizeOfTriggerArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(TRIGGER$2);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setTriggerArray(TriggerType[] triggerTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper((XmlObject[]) triggerTypeArr, TRIGGER$2);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setTriggerArray(int i, TriggerType triggerType) {
        synchronized (monitor()) {
            check_orphaned();
            TriggerType triggerTypeFind_element_user = get_store().find_element_user(TRIGGER$2, i);
            if (triggerTypeFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
            triggerTypeFind_element_user.set(triggerType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public TriggerType insertNewTrigger(int i) {
        TriggerType triggerTypeInsert_element_user;
        synchronized (monitor()) {
            check_orphaned();
            triggerTypeInsert_element_user = get_store().insert_element_user(TRIGGER$2, i);
        }
        return triggerTypeInsert_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public TriggerType addNewTrigger() {
        TriggerType triggerTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            triggerTypeAdd_element_user = get_store().add_element_user(TRIGGER$2);
        }
        return triggerTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void removeTrigger(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TRIGGER$2, i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public String getN() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(N$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public XmlString xgetN() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(N$4);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public boolean isSetN() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(N$4) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setN(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(N$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(N$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void xsetN(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(N$4);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(N$4);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void unsetN() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(N$4);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public String getLocalName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCALNAME$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public XmlString xgetLocalName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(LOCALNAME$6);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public boolean isSetLocalName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LOCALNAME$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setLocalName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCALNAME$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LOCALNAME$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void xsetLocalName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(LOCALNAME$6);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(LOCALNAME$6);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void unsetLocalName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LOCALNAME$6);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public long getIX() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IX$8);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public XmlUnsignedInt xgetIX() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(IX$8);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public boolean isSetIX() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(IX$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setIX(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IX$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(IX$8);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void xsetIX(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(IX$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(IX$8);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void unsetIX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(IX$8);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public String getT() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(T$10);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public XmlString xgetT() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(T$10);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public boolean isSetT() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(T$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setT(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(T$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(T$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void xsetT(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(T$10);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(T$10);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void unsetT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(T$10);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public boolean getDel() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEL$12);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public XmlBoolean xgetDel() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(DEL$12);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public boolean isSetDel() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEL$12) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void setDel(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEL$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEL$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void xsetDel(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEL$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DEL$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.RowType
    public void unsetDel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEL$12);
        }
    }
}
