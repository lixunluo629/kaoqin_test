package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTCacheFieldsImpl.class */
public class CTCacheFieldsImpl extends XmlComplexContentImpl implements CTCacheFields {
    private static final QName CACHEFIELD$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "cacheField");
    private static final QName COUNT$2 = new QName("", "count");

    public CTCacheFieldsImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public List<CTCacheField> getCacheFieldList() {
        1CacheFieldList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CacheFieldList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public CTCacheField[] getCacheFieldArray() {
        CTCacheField[] cTCacheFieldArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(CACHEFIELD$0, arrayList);
            cTCacheFieldArr = new CTCacheField[arrayList.size()];
            arrayList.toArray(cTCacheFieldArr);
        }
        return cTCacheFieldArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public CTCacheField getCacheFieldArray(int i) {
        CTCacheField cTCacheField;
        synchronized (monitor()) {
            check_orphaned();
            cTCacheField = (CTCacheField) get_store().find_element_user(CACHEFIELD$0, i);
            if (cTCacheField == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTCacheField;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public int sizeOfCacheFieldArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CACHEFIELD$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public void setCacheFieldArray(CTCacheField[] cTCacheFieldArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTCacheFieldArr, CACHEFIELD$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public void setCacheFieldArray(int i, CTCacheField cTCacheField) {
        synchronized (monitor()) {
            check_orphaned();
            CTCacheField cTCacheField2 = (CTCacheField) get_store().find_element_user(CACHEFIELD$0, i);
            if (cTCacheField2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTCacheField2.set(cTCacheField);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public CTCacheField insertNewCacheField(int i) {
        CTCacheField cTCacheField;
        synchronized (monitor()) {
            check_orphaned();
            cTCacheField = (CTCacheField) get_store().insert_element_user(CACHEFIELD$0, i);
        }
        return cTCacheField;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public CTCacheField addNewCacheField() {
        CTCacheField cTCacheField;
        synchronized (monitor()) {
            check_orphaned();
            cTCacheField = (CTCacheField) get_store().add_element_user(CACHEFIELD$0);
        }
        return cTCacheField;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public void removeCacheField(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CACHEFIELD$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public XmlUnsignedInt xgetCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$2);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public boolean isSetCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNT$2);
        }
    }
}
