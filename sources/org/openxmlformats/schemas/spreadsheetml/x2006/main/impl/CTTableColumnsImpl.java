package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTTableColumnsImpl.class */
public class CTTableColumnsImpl extends XmlComplexContentImpl implements CTTableColumns {
    private static final QName TABLECOLUMN$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "tableColumn");
    private static final QName COUNT$2 = new QName("", "count");

    public CTTableColumnsImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public List<CTTableColumn> getTableColumnList() {
        AbstractList<CTTableColumn> abstractList;
        synchronized (monitor()) {
            check_orphaned();
            abstractList = new AbstractList<CTTableColumn>() { // from class: org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTTableColumnsImpl.1TableColumnList
                @Override // java.util.AbstractList, java.util.List
                public CTTableColumn get(int i) {
                    return CTTableColumnsImpl.this.getTableColumnArray(i);
                }

                @Override // java.util.AbstractList, java.util.List
                public CTTableColumn set(int i, CTTableColumn cTTableColumn) {
                    CTTableColumn tableColumnArray = CTTableColumnsImpl.this.getTableColumnArray(i);
                    CTTableColumnsImpl.this.setTableColumnArray(i, cTTableColumn);
                    return tableColumnArray;
                }

                @Override // java.util.AbstractList, java.util.List
                public void add(int i, CTTableColumn cTTableColumn) {
                    CTTableColumnsImpl.this.insertNewTableColumn(i).set(cTTableColumn);
                }

                @Override // java.util.AbstractList, java.util.List
                public CTTableColumn remove(int i) {
                    CTTableColumn tableColumnArray = CTTableColumnsImpl.this.getTableColumnArray(i);
                    CTTableColumnsImpl.this.removeTableColumn(i);
                    return tableColumnArray;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return CTTableColumnsImpl.this.sizeOfTableColumnArray();
                }
            };
        }
        return abstractList;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public CTTableColumn[] getTableColumnArray() {
        CTTableColumn[] cTTableColumnArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(TABLECOLUMN$0, arrayList);
            cTTableColumnArr = new CTTableColumn[arrayList.size()];
            arrayList.toArray(cTTableColumnArr);
        }
        return cTTableColumnArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public CTTableColumn getTableColumnArray(int i) {
        CTTableColumn cTTableColumn;
        synchronized (monitor()) {
            check_orphaned();
            cTTableColumn = (CTTableColumn) get_store().find_element_user(TABLECOLUMN$0, i);
            if (cTTableColumn == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTTableColumn;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public int sizeOfTableColumnArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(TABLECOLUMN$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public void setTableColumnArray(CTTableColumn[] cTTableColumnArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTTableColumnArr, TABLECOLUMN$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public void setTableColumnArray(int i, CTTableColumn cTTableColumn) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableColumn cTTableColumn2 = (CTTableColumn) get_store().find_element_user(TABLECOLUMN$0, i);
            if (cTTableColumn2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTTableColumn2.set(cTTableColumn);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public CTTableColumn insertNewTableColumn(int i) {
        CTTableColumn cTTableColumn;
        synchronized (monitor()) {
            check_orphaned();
            cTTableColumn = (CTTableColumn) get_store().insert_element_user(TABLECOLUMN$0, i);
        }
        return cTTableColumn;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public CTTableColumn addNewTableColumn() {
        CTTableColumn cTTableColumn;
        synchronized (monitor()) {
            check_orphaned();
            cTTableColumn = (CTTableColumn) get_store().add_element_user(TABLECOLUMN$0);
        }
        return cTTableColumn;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public void removeTableColumn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TABLECOLUMN$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public XmlUnsignedInt xgetCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$2);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public boolean isSetCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNT$2);
        }
    }
}
