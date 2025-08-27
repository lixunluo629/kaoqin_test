package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTSelectionImpl.class */
public class CTSelectionImpl extends XmlComplexContentImpl implements CTSelection {
    private static final QName PANE$0 = new QName("", "pane");
    private static final QName ACTIVECELL$2 = new QName("", "activeCell");
    private static final QName ACTIVECELLID$4 = new QName("", "activeCellId");
    private static final QName SQREF$6 = new QName("", "sqref");

    public CTSelectionImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public STPane.Enum getPane() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PANE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PANE$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPane.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public STPane xgetPane() {
        STPane sTPane;
        synchronized (monitor()) {
            check_orphaned();
            STPane sTPane2 = (STPane) get_store().find_attribute_user(PANE$0);
            if (sTPane2 == null) {
                sTPane2 = (STPane) get_default_attribute_value(PANE$0);
            }
            sTPane = sTPane2;
        }
        return sTPane;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public boolean isSetPane() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PANE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void setPane(STPane.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PANE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PANE$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void xsetPane(STPane sTPane) {
        synchronized (monitor()) {
            check_orphaned();
            STPane sTPane2 = (STPane) get_store().find_attribute_user(PANE$0);
            if (sTPane2 == null) {
                sTPane2 = (STPane) get_store().add_attribute_user(PANE$0);
            }
            sTPane2.set(sTPane);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void unsetPane() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PANE$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public String getActiveCell() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTIVECELL$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public STCellRef xgetActiveCell() {
        STCellRef sTCellRef;
        synchronized (monitor()) {
            check_orphaned();
            sTCellRef = (STCellRef) get_store().find_attribute_user(ACTIVECELL$2);
        }
        return sTCellRef;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public boolean isSetActiveCell() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ACTIVECELL$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void setActiveCell(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTIVECELL$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACTIVECELL$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void xsetActiveCell(STCellRef sTCellRef) {
        synchronized (monitor()) {
            check_orphaned();
            STCellRef sTCellRef2 = (STCellRef) get_store().find_attribute_user(ACTIVECELL$2);
            if (sTCellRef2 == null) {
                sTCellRef2 = (STCellRef) get_store().add_attribute_user(ACTIVECELL$2);
            }
            sTCellRef2.set(sTCellRef);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void unsetActiveCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ACTIVECELL$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public long getActiveCellId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTIVECELLID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ACTIVECELLID$4);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public XmlUnsignedInt xgetActiveCellId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ACTIVECELLID$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(ACTIVECELLID$4);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public boolean isSetActiveCellId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ACTIVECELLID$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void setActiveCellId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTIVECELLID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACTIVECELLID$4);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void xsetActiveCellId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ACTIVECELLID$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ACTIVECELLID$4);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void unsetActiveCellId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ACTIVECELLID$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public List getSqref() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SQREF$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SQREF$6);
            }
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getListValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public STSqref xgetSqref() {
        STSqref sTSqref;
        synchronized (monitor()) {
            check_orphaned();
            STSqref sTSqref2 = (STSqref) get_store().find_attribute_user(SQREF$6);
            if (sTSqref2 == null) {
                sTSqref2 = (STSqref) get_default_attribute_value(SQREF$6);
            }
            sTSqref = sTSqref2;
        }
        return sTSqref;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public boolean isSetSqref() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SQREF$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void setSqref(List list) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SQREF$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SQREF$6);
            }
            simpleValue.setListValue(list);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void xsetSqref(STSqref sTSqref) {
        synchronized (monitor()) {
            check_orphaned();
            STSqref sTSqref2 = (STSqref) get_store().find_attribute_user(SQREF$6);
            if (sTSqref2 == null) {
                sTSqref2 = (STSqref) get_store().add_attribute_user(SQREF$6);
            }
            sTSqref2.set(sTSqref);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
    public void unsetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SQREF$6);
        }
    }
}
