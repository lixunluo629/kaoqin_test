package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTTblGridBaseImpl.class */
public class CTTblGridBaseImpl extends XmlComplexContentImpl implements CTTblGridBase {
    private static final QName GRIDCOL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gridCol");

    public CTTblGridBaseImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public List<CTTblGridCol> getGridColList() {
        1GridColList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1GridColList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public CTTblGridCol[] getGridColArray() {
        CTTblGridCol[] cTTblGridColArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(GRIDCOL$0, arrayList);
            cTTblGridColArr = new CTTblGridCol[arrayList.size()];
            arrayList.toArray(cTTblGridColArr);
        }
        return cTTblGridColArr;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public CTTblGridCol getGridColArray(int i) {
        CTTblGridCol cTTblGridCol;
        synchronized (monitor()) {
            check_orphaned();
            cTTblGridCol = (CTTblGridCol) get_store().find_element_user(GRIDCOL$0, i);
            if (cTTblGridCol == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTTblGridCol;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public int sizeOfGridColArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(GRIDCOL$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public void setGridColArray(CTTblGridCol[] cTTblGridColArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTTblGridColArr, GRIDCOL$0);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public void setGridColArray(int i, CTTblGridCol cTTblGridCol) {
        synchronized (monitor()) {
            check_orphaned();
            CTTblGridCol cTTblGridCol2 = (CTTblGridCol) get_store().find_element_user(GRIDCOL$0, i);
            if (cTTblGridCol2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTTblGridCol2.set(cTTblGridCol);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public CTTblGridCol insertNewGridCol(int i) {
        CTTblGridCol cTTblGridCol;
        synchronized (monitor()) {
            check_orphaned();
            cTTblGridCol = (CTTblGridCol) get_store().insert_element_user(GRIDCOL$0, i);
        }
        return cTTblGridCol;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public CTTblGridCol addNewGridCol() {
        CTTblGridCol cTTblGridCol;
        synchronized (monitor()) {
            check_orphaned();
            cTTblGridCol = (CTTblGridCol) get_store().add_element_user(GRIDCOL$0);
        }
        return cTTblGridCol;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
    public void removeGridCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GRIDCOL$0, i);
        }
    }
}
