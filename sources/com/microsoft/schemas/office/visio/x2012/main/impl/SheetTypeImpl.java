package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.SectionType;
import com.microsoft.schemas.office.visio.x2012.main.SheetType;
import com.microsoft.schemas.office.visio.x2012.main.TriggerType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/SheetTypeImpl.class */
public class SheetTypeImpl extends XmlComplexContentImpl implements SheetType {
    private static final QName CELL$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Cell");
    private static final QName TRIGGER$2 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Trigger");
    private static final QName SECTION$4 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Section");
    private static final QName LINESTYLE$6 = new QName("", "LineStyle");
    private static final QName FILLSTYLE$8 = new QName("", "FillStyle");
    private static final QName TEXTSTYLE$10 = new QName("", "TextStyle");

    public SheetTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public List<CellType> getCellList() {
        1CellList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CellList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public int sizeOfCellArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CELL$0);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setCellArray(CellType[] cellTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cellTypeArr, CELL$0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public CellType insertNewCell(int i) {
        CellType cellType;
        synchronized (monitor()) {
            check_orphaned();
            cellType = (CellType) get_store().insert_element_user(CELL$0, i);
        }
        return cellType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public CellType addNewCell() {
        CellType cellType;
        synchronized (monitor()) {
            check_orphaned();
            cellType = (CellType) get_store().add_element_user(CELL$0);
        }
        return cellType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void removeCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CELL$0, i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public List<TriggerType> getTriggerList() {
        1TriggerList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1TriggerList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public int sizeOfTriggerArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(TRIGGER$2);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setTriggerArray(TriggerType[] triggerTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper((XmlObject[]) triggerTypeArr, TRIGGER$2);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public TriggerType insertNewTrigger(int i) {
        TriggerType triggerTypeInsert_element_user;
        synchronized (monitor()) {
            check_orphaned();
            triggerTypeInsert_element_user = get_store().insert_element_user(TRIGGER$2, i);
        }
        return triggerTypeInsert_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public TriggerType addNewTrigger() {
        TriggerType triggerTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            triggerTypeAdd_element_user = get_store().add_element_user(TRIGGER$2);
        }
        return triggerTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void removeTrigger(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TRIGGER$2, i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public List<SectionType> getSectionList() {
        1SectionList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1SectionList(this);
        }
        return r0;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public SectionType[] getSectionArray() {
        SectionType[] sectionTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(SECTION$4, arrayList);
            sectionTypeArr = new SectionType[arrayList.size()];
            arrayList.toArray(sectionTypeArr);
        }
        return sectionTypeArr;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public SectionType getSectionArray(int i) {
        SectionType sectionType;
        synchronized (monitor()) {
            check_orphaned();
            sectionType = (SectionType) get_store().find_element_user(SECTION$4, i);
            if (sectionType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return sectionType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public int sizeOfSectionArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(SECTION$4);
        }
        return iCount_elements;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setSectionArray(SectionType[] sectionTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(sectionTypeArr, SECTION$4);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setSectionArray(int i, SectionType sectionType) {
        synchronized (monitor()) {
            check_orphaned();
            SectionType sectionType2 = (SectionType) get_store().find_element_user(SECTION$4, i);
            if (sectionType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            sectionType2.set(sectionType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public SectionType insertNewSection(int i) {
        SectionType sectionType;
        synchronized (monitor()) {
            check_orphaned();
            sectionType = (SectionType) get_store().insert_element_user(SECTION$4, i);
        }
        return sectionType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public SectionType addNewSection() {
        SectionType sectionType;
        synchronized (monitor()) {
            check_orphaned();
            sectionType = (SectionType) get_store().add_element_user(SECTION$4);
        }
        return sectionType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void removeSection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SECTION$4, i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public long getLineStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINESTYLE$6);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public XmlUnsignedInt xgetLineStyle() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(LINESTYLE$6);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public boolean isSetLineStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LINESTYLE$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setLineStyle(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINESTYLE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LINESTYLE$6);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void xsetLineStyle(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(LINESTYLE$6);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(LINESTYLE$6);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void unsetLineStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LINESTYLE$6);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public long getFillStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILLSTYLE$8);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public XmlUnsignedInt xgetFillStyle() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(FILLSTYLE$8);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public boolean isSetFillStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FILLSTYLE$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setFillStyle(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILLSTYLE$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FILLSTYLE$8);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void xsetFillStyle(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FILLSTYLE$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FILLSTYLE$8);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void unsetFillStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FILLSTYLE$8);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public long getTextStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TEXTSTYLE$10);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public XmlUnsignedInt xgetTextStyle() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(TEXTSTYLE$10);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public boolean isSetTextStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TEXTSTYLE$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void setTextStyle(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TEXTSTYLE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TEXTSTYLE$10);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void xsetTextStyle(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(TEXTSTYLE$10);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(TEXTSTYLE$10);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.SheetType
    public void unsetTextStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TEXTSTYLE$10);
        }
    }
}
