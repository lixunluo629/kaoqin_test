package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop;
import org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextTabAlignType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextTabAlignType$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextTabStopImpl.class */
public class CTTextTabStopImpl extends XmlComplexContentImpl implements CTTextTabStop {
    private static final QName POS$0 = new QName("", "pos");
    private static final QName ALGN$2 = new QName("", "algn");

    public CTTextTabStopImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public int getPos() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POS$0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public STCoordinate32 xgetPos() {
        STCoordinate32 sTCoordinate32;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate32 = (STCoordinate32) get_store().find_attribute_user(POS$0);
        }
        return sTCoordinate32;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public boolean isSetPos() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(POS$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public void setPos(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POS$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(POS$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public void xsetPos(STCoordinate32 sTCoordinate32) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate32 sTCoordinate322 = (STCoordinate32) get_store().find_attribute_user(POS$0);
            if (sTCoordinate322 == null) {
                sTCoordinate322 = (STCoordinate32) get_store().add_attribute_user(POS$0);
            }
            sTCoordinate322.set(sTCoordinate32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public void unsetPos() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(POS$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public STTextTabAlignType$Enum getAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALGN$2);
            if (simpleValue == null) {
                return null;
            }
            return (STTextTabAlignType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public STTextTabAlignType xgetAlgn() {
        STTextTabAlignType sTTextTabAlignTypeFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTextTabAlignTypeFind_attribute_user = get_store().find_attribute_user(ALGN$2);
        }
        return sTTextTabAlignTypeFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public boolean isSetAlgn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALGN$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public void setAlgn(STTextTabAlignType$Enum sTTextTabAlignType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALGN$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALGN$2);
            }
            simpleValue.setEnumValue(sTTextTabAlignType$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public void xsetAlgn(STTextTabAlignType sTTextTabAlignType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextTabAlignType sTTextTabAlignTypeFind_attribute_user = get_store().find_attribute_user(ALGN$2);
            if (sTTextTabAlignTypeFind_attribute_user == null) {
                sTTextTabAlignTypeFind_attribute_user = (STTextTabAlignType) get_store().add_attribute_user(ALGN$2);
            }
            sTTextTabAlignTypeFind_attribute_user.set(sTTextTabAlignType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
    public void unsetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALGN$2);
        }
    }
}
