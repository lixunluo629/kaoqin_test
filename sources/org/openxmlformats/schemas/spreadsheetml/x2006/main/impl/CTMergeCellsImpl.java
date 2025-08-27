package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTMergeCellsImpl.class */
public class CTMergeCellsImpl extends XmlComplexContentImpl implements CTMergeCells {
    private static final QName MERGECELL$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "mergeCell");
    private static final QName COUNT$2 = new QName("", "count");

    public CTMergeCellsImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public List<CTMergeCell> getMergeCellList() {
        1MergeCellList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1MergeCellList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public CTMergeCell[] getMergeCellArray() {
        CTMergeCell[] cTMergeCellArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(MERGECELL$0, arrayList);
            cTMergeCellArr = new CTMergeCell[arrayList.size()];
            arrayList.toArray(cTMergeCellArr);
        }
        return cTMergeCellArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public CTMergeCell getMergeCellArray(int i) {
        CTMergeCell cTMergeCell;
        synchronized (monitor()) {
            check_orphaned();
            cTMergeCell = (CTMergeCell) get_store().find_element_user(MERGECELL$0, i);
            if (cTMergeCell == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTMergeCell;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public int sizeOfMergeCellArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(MERGECELL$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public void setMergeCellArray(CTMergeCell[] cTMergeCellArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTMergeCellArr, MERGECELL$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public void setMergeCellArray(int i, CTMergeCell cTMergeCell) {
        synchronized (monitor()) {
            check_orphaned();
            CTMergeCell cTMergeCell2 = (CTMergeCell) get_store().find_element_user(MERGECELL$0, i);
            if (cTMergeCell2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTMergeCell2.set(cTMergeCell);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public CTMergeCell insertNewMergeCell(int i) {
        CTMergeCell cTMergeCell;
        synchronized (monitor()) {
            check_orphaned();
            cTMergeCell = (CTMergeCell) get_store().insert_element_user(MERGECELL$0, i);
        }
        return cTMergeCell;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public CTMergeCell addNewMergeCell() {
        CTMergeCell cTMergeCell;
        synchronized (monitor()) {
            check_orphaned();
            cTMergeCell = (CTMergeCell) get_store().add_element_user(MERGECELL$0);
        }
        return cTMergeCell;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public void removeMergeCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MERGECELL$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public long getCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNT$2);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public XmlUnsignedInt xgetCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$2);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public boolean isSetCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public void setCount(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COUNT$2);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public void xsetCount(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$2);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(COUNT$2);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNT$2);
        }
    }
}
