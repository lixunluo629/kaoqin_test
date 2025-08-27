package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPaneImpl.class */
public class CTPaneImpl extends XmlComplexContentImpl implements CTPane {
    private static final QName XSPLIT$0 = new QName("", "xSplit");
    private static final QName YSPLIT$2 = new QName("", "ySplit");
    private static final QName TOPLEFTCELL$4 = new QName("", "topLeftCell");
    private static final QName ACTIVEPANE$6 = new QName("", "activePane");
    private static final QName STATE$8 = new QName("", "state");

    public CTPaneImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public double getXSplit() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(XSPLIT$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(XSPLIT$0);
            }
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public XmlDouble xgetXSplit() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(XSPLIT$0);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_default_attribute_value(XSPLIT$0);
            }
            xmlDouble = xmlDouble2;
        }
        return xmlDouble;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public boolean isSetXSplit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(XSPLIT$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void setXSplit(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(XSPLIT$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(XSPLIT$0);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void xsetXSplit(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(XSPLIT$0);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(XSPLIT$0);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void unsetXSplit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(XSPLIT$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public double getYSplit() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(YSPLIT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(YSPLIT$2);
            }
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public XmlDouble xgetYSplit() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(YSPLIT$2);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_default_attribute_value(YSPLIT$2);
            }
            xmlDouble = xmlDouble2;
        }
        return xmlDouble;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public boolean isSetYSplit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(YSPLIT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void setYSplit(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(YSPLIT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(YSPLIT$2);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void xsetYSplit(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(YSPLIT$2);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(YSPLIT$2);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void unsetYSplit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(YSPLIT$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public String getTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPLEFTCELL$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public STCellRef xgetTopLeftCell() {
        STCellRef sTCellRef;
        synchronized (monitor()) {
            check_orphaned();
            sTCellRef = (STCellRef) get_store().find_attribute_user(TOPLEFTCELL$4);
        }
        return sTCellRef;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public boolean isSetTopLeftCell() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOPLEFTCELL$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void setTopLeftCell(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPLEFTCELL$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOPLEFTCELL$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void xsetTopLeftCell(STCellRef sTCellRef) {
        synchronized (monitor()) {
            check_orphaned();
            STCellRef sTCellRef2 = (STCellRef) get_store().find_attribute_user(TOPLEFTCELL$4);
            if (sTCellRef2 == null) {
                sTCellRef2 = (STCellRef) get_store().add_attribute_user(TOPLEFTCELL$4);
            }
            sTCellRef2.set(sTCellRef);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void unsetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOPLEFTCELL$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public STPane.Enum getActivePane() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTIVEPANE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ACTIVEPANE$6);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPane.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public STPane xgetActivePane() {
        STPane sTPane;
        synchronized (monitor()) {
            check_orphaned();
            STPane sTPane2 = (STPane) get_store().find_attribute_user(ACTIVEPANE$6);
            if (sTPane2 == null) {
                sTPane2 = (STPane) get_default_attribute_value(ACTIVEPANE$6);
            }
            sTPane = sTPane2;
        }
        return sTPane;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public boolean isSetActivePane() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ACTIVEPANE$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void setActivePane(STPane.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTIVEPANE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACTIVEPANE$6);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void xsetActivePane(STPane sTPane) {
        synchronized (monitor()) {
            check_orphaned();
            STPane sTPane2 = (STPane) get_store().find_attribute_user(ACTIVEPANE$6);
            if (sTPane2 == null) {
                sTPane2 = (STPane) get_store().add_attribute_user(ACTIVEPANE$6);
            }
            sTPane2.set(sTPane);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void unsetActivePane() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ACTIVEPANE$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public STPaneState.Enum getState() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STATE$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(STATE$8);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPaneState.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public STPaneState xgetState() {
        STPaneState sTPaneState;
        synchronized (monitor()) {
            check_orphaned();
            STPaneState sTPaneState2 = (STPaneState) get_store().find_attribute_user(STATE$8);
            if (sTPaneState2 == null) {
                sTPaneState2 = (STPaneState) get_default_attribute_value(STATE$8);
            }
            sTPaneState = sTPaneState2;
        }
        return sTPaneState;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public boolean isSetState() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STATE$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void setState(STPaneState.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STATE$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STATE$8);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void xsetState(STPaneState sTPaneState) {
        synchronized (monitor()) {
            check_orphaned();
            STPaneState sTPaneState2 = (STPaneState) get_store().find_attribute_user(STATE$8);
            if (sTPaneState2 == null) {
                sTPaneState2 = (STPaneState) get_store().add_attribute_user(STATE$8);
            }
            sTPaneState2.set(sTPaneState);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
    public void unsetState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STATE$8);
        }
    }
}
