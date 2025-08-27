package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPageFieldsImpl.class */
public class CTPageFieldsImpl extends XmlComplexContentImpl implements CTPageFields {
    private static final QName PAGEFIELD$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "pageField");
    private static final QName COUNT$2 = new QName("", "count");

    public CTPageFieldsImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public List<CTPageField> getPageFieldList() {
        1PageFieldList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1PageFieldList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public CTPageField[] getPageFieldArray() {
        CTPageField[] cTPageFieldArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(PAGEFIELD$0, arrayList);
            cTPageFieldArr = new CTPageField[arrayList.size()];
            arrayList.toArray(cTPageFieldArr);
        }
        return cTPageFieldArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public CTPageField getPageFieldArray(int i) {
        CTPageField cTPageField;
        synchronized (monitor()) {
            check_orphaned();
            cTPageField = (CTPageField) get_store().find_element_user(PAGEFIELD$0, i);
            if (cTPageField == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTPageField;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public int sizeOfPageFieldArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(PAGEFIELD$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public void setPageFieldArray(CTPageField[] cTPageFieldArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTPageFieldArr, PAGEFIELD$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public void setPageFieldArray(int i, CTPageField cTPageField) {
        synchronized (monitor()) {
            check_orphaned();
            CTPageField cTPageField2 = (CTPageField) get_store().find_element_user(PAGEFIELD$0, i);
            if (cTPageField2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTPageField2.set(cTPageField);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public CTPageField insertNewPageField(int i) {
        CTPageField cTPageField;
        synchronized (monitor()) {
            check_orphaned();
            cTPageField = (CTPageField) get_store().insert_element_user(PAGEFIELD$0, i);
        }
        return cTPageField;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public CTPageField addNewPageField() {
        CTPageField cTPageField;
        synchronized (monitor()) {
            check_orphaned();
            cTPageField = (CTPageField) get_store().add_element_user(PAGEFIELD$0);
        }
        return cTPageField;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public void removePageField(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PAGEFIELD$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public XmlUnsignedInt xgetCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$2);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public boolean isSetCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNT$2);
        }
    }
}
