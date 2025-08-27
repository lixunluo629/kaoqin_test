package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect;
import org.openxmlformats.schemas.drawingml.x2006.main.STPercentage;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTRelativeRectImpl.class */
public class CTRelativeRectImpl extends XmlComplexContentImpl implements CTRelativeRect {
    private static final QName L$0 = new QName("", "l");
    private static final QName T$2 = new QName("", "t");
    private static final QName R$4 = new QName("", ExcelXmlConstants.POSITION);
    private static final QName B$6 = new QName("", "b");

    public CTRelativeRectImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public int getL() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(L$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(L$0);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public STPercentage xgetL() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(L$0);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_default_attribute_value(L$0);
            }
            sTPercentage = sTPercentage2;
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public boolean isSetL() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(L$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void setL(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(L$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(L$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void xsetL(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(L$0);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(L$0);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void unsetL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(L$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public int getT() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(T$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(T$2);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public STPercentage xgetT() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(T$2);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_default_attribute_value(T$2);
            }
            sTPercentage = sTPercentage2;
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public boolean isSetT() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(T$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void setT(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(T$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(T$2);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void xsetT(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(T$2);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(T$2);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void unsetT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(T$2);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public int getR() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(R$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(R$4);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public STPercentage xgetR() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(R$4);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_default_attribute_value(R$4);
            }
            sTPercentage = sTPercentage2;
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public boolean isSetR() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(R$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void setR(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(R$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(R$4);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void xsetR(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(R$4);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(R$4);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void unsetR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(R$4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public int getB() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(B$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(B$6);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public STPercentage xgetB() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(B$6);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_default_attribute_value(B$6);
            }
            sTPercentage = sTPercentage2;
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public boolean isSetB() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(B$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void setB(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(B$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(B$6);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void xsetB(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(B$6);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(B$6);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
    public void unsetB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(B$6);
        }
    }
}
