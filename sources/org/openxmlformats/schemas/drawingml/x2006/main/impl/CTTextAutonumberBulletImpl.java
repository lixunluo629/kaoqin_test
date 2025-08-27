package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextAutonumberBulletImpl.class */
public class CTTextAutonumberBulletImpl extends XmlComplexContentImpl implements CTTextAutonumberBullet {
    private static final QName TYPE$0 = new QName("", "type");
    private static final QName STARTAT$2 = new QName("", "startAt");

    public CTTextAutonumberBulletImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public STTextAutonumberScheme.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$0);
            if (simpleValue == null) {
                return null;
            }
            return (STTextAutonumberScheme.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public STTextAutonumberScheme xgetType() {
        STTextAutonumberScheme sTTextAutonumberScheme;
        synchronized (monitor()) {
            check_orphaned();
            sTTextAutonumberScheme = (STTextAutonumberScheme) get_store().find_attribute_user(TYPE$0);
        }
        return sTTextAutonumberScheme;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public void setType(STTextAutonumberScheme.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPE$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public void xsetType(STTextAutonumberScheme sTTextAutonumberScheme) {
        synchronized (monitor()) {
            check_orphaned();
            STTextAutonumberScheme sTTextAutonumberScheme2 = (STTextAutonumberScheme) get_store().find_attribute_user(TYPE$0);
            if (sTTextAutonumberScheme2 == null) {
                sTTextAutonumberScheme2 = (STTextAutonumberScheme) get_store().add_attribute_user(TYPE$0);
            }
            sTTextAutonumberScheme2.set(sTTextAutonumberScheme);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public int getStartAt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTAT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(STARTAT$2);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public STTextBulletStartAtNum xgetStartAt() {
        STTextBulletStartAtNum sTTextBulletStartAtNum;
        synchronized (monitor()) {
            check_orphaned();
            STTextBulletStartAtNum sTTextBulletStartAtNum2 = (STTextBulletStartAtNum) get_store().find_attribute_user(STARTAT$2);
            if (sTTextBulletStartAtNum2 == null) {
                sTTextBulletStartAtNum2 = (STTextBulletStartAtNum) get_default_attribute_value(STARTAT$2);
            }
            sTTextBulletStartAtNum = sTTextBulletStartAtNum2;
        }
        return sTTextBulletStartAtNum;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public boolean isSetStartAt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STARTAT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public void setStartAt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTAT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STARTAT$2);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public void xsetStartAt(STTextBulletStartAtNum sTTextBulletStartAtNum) {
        synchronized (monitor()) {
            check_orphaned();
            STTextBulletStartAtNum sTTextBulletStartAtNum2 = (STTextBulletStartAtNum) get_store().find_attribute_user(STARTAT$2);
            if (sTTextBulletStartAtNum2 == null) {
                sTTextBulletStartAtNum2 = (STTextBulletStartAtNum) get_store().add_attribute_user(STARTAT$2);
            }
            sTTextBulletStartAtNum2.set(sTTextBulletStartAtNum);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
    public void unsetStartAt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STARTAT$2);
        }
    }
}
