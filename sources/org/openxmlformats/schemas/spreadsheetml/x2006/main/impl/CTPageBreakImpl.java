package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPageBreakImpl.class */
public class CTPageBreakImpl extends XmlComplexContentImpl implements CTPageBreak {
    private static final QName BRK$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "brk");
    private static final QName COUNT$2 = new QName("", "count");
    private static final QName MANUALBREAKCOUNT$4 = new QName("", "manualBreakCount");

    public CTPageBreakImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public List<CTBreak> getBrkList() {
        1BrkList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1BrkList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public CTBreak[] getBrkArray() {
        CTBreak[] cTBreakArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(BRK$0, arrayList);
            cTBreakArr = new CTBreak[arrayList.size()];
            arrayList.toArray(cTBreakArr);
        }
        return cTBreakArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public CTBreak getBrkArray(int i) {
        CTBreak cTBreak;
        synchronized (monitor()) {
            check_orphaned();
            cTBreak = (CTBreak) get_store().find_element_user(BRK$0, i);
            if (cTBreak == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTBreak;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public int sizeOfBrkArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(BRK$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void setBrkArray(CTBreak[] cTBreakArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTBreakArr, BRK$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void setBrkArray(int i, CTBreak cTBreak) {
        synchronized (monitor()) {
            check_orphaned();
            CTBreak cTBreak2 = (CTBreak) get_store().find_element_user(BRK$0, i);
            if (cTBreak2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTBreak2.set(cTBreak);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public CTBreak insertNewBrk(int i) {
        CTBreak cTBreak;
        synchronized (monitor()) {
            check_orphaned();
            cTBreak = (CTBreak) get_store().insert_element_user(BRK$0, i);
        }
        return cTBreak;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public CTBreak addNewBrk() {
        CTBreak cTBreak;
        synchronized (monitor()) {
            check_orphaned();
            cTBreak = (CTBreak) get_store().add_element_user(BRK$0);
        }
        return cTBreak;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void removeBrk(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(BRK$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public long getCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COUNT$2);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public XmlUnsignedInt xgetCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COUNT$2);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(COUNT$2);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public boolean isSetCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
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

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNT$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public long getManualBreakCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MANUALBREAKCOUNT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MANUALBREAKCOUNT$4);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public XmlUnsignedInt xgetManualBreakCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(MANUALBREAKCOUNT$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(MANUALBREAKCOUNT$4);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public boolean isSetManualBreakCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MANUALBREAKCOUNT$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void setManualBreakCount(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MANUALBREAKCOUNT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MANUALBREAKCOUNT$4);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void xsetManualBreakCount(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(MANUALBREAKCOUNT$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(MANUALBREAKCOUNT$4);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
    public void unsetManualBreakCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MANUALBREAKCOUNT$4);
        }
    }
}
