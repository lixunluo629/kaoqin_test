package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect;
import org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTGeomRectImpl.class */
public class CTGeomRectImpl extends XmlComplexContentImpl implements CTGeomRect {
    private static final QName L$0 = new QName("", "l");
    private static final QName T$2 = new QName("", "t");
    private static final QName R$4 = new QName("", ExcelXmlConstants.POSITION);
    private static final QName B$6 = new QName("", "b");

    public CTGeomRectImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public Object getL() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(L$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getObjectValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public STAdjCoordinate xgetL() {
        STAdjCoordinate sTAdjCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            sTAdjCoordinate = (STAdjCoordinate) get_store().find_attribute_user(L$0);
        }
        return sTAdjCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void setL(Object obj) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(L$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(L$0);
            }
            simpleValue.setObjectValue(obj);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void xsetL(STAdjCoordinate sTAdjCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STAdjCoordinate sTAdjCoordinate2 = (STAdjCoordinate) get_store().find_attribute_user(L$0);
            if (sTAdjCoordinate2 == null) {
                sTAdjCoordinate2 = (STAdjCoordinate) get_store().add_attribute_user(L$0);
            }
            sTAdjCoordinate2.set(sTAdjCoordinate);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public Object getT() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(T$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getObjectValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public STAdjCoordinate xgetT() {
        STAdjCoordinate sTAdjCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            sTAdjCoordinate = (STAdjCoordinate) get_store().find_attribute_user(T$2);
        }
        return sTAdjCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void setT(Object obj) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(T$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(T$2);
            }
            simpleValue.setObjectValue(obj);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void xsetT(STAdjCoordinate sTAdjCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STAdjCoordinate sTAdjCoordinate2 = (STAdjCoordinate) get_store().find_attribute_user(T$2);
            if (sTAdjCoordinate2 == null) {
                sTAdjCoordinate2 = (STAdjCoordinate) get_store().add_attribute_user(T$2);
            }
            sTAdjCoordinate2.set(sTAdjCoordinate);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public Object getR() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(R$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getObjectValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public STAdjCoordinate xgetR() {
        STAdjCoordinate sTAdjCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            sTAdjCoordinate = (STAdjCoordinate) get_store().find_attribute_user(R$4);
        }
        return sTAdjCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void setR(Object obj) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(R$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(R$4);
            }
            simpleValue.setObjectValue(obj);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void xsetR(STAdjCoordinate sTAdjCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STAdjCoordinate sTAdjCoordinate2 = (STAdjCoordinate) get_store().find_attribute_user(R$4);
            if (sTAdjCoordinate2 == null) {
                sTAdjCoordinate2 = (STAdjCoordinate) get_store().add_attribute_user(R$4);
            }
            sTAdjCoordinate2.set(sTAdjCoordinate);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public Object getB() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(B$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getObjectValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public STAdjCoordinate xgetB() {
        STAdjCoordinate sTAdjCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            sTAdjCoordinate = (STAdjCoordinate) get_store().find_attribute_user(B$6);
        }
        return sTAdjCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void setB(Object obj) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(B$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(B$6);
            }
            simpleValue.setObjectValue(obj);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
    public void xsetB(STAdjCoordinate sTAdjCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STAdjCoordinate sTAdjCoordinate2 = (STAdjCoordinate) get_store().find_attribute_user(B$6);
            if (sTAdjCoordinate2 == null) {
                sTAdjCoordinate2 = (STAdjCoordinate) get_store().add_attribute_user(B$6);
            }
            sTAdjCoordinate2.set(sTAdjCoordinate);
        }
    }
}
