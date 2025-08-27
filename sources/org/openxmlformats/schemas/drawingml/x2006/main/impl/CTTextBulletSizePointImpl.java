package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextBulletSizePointImpl.class */
public class CTTextBulletSizePointImpl extends XmlComplexContentImpl implements CTTextBulletSizePoint {
    private static final QName VAL$0 = new QName("", "val");

    public CTTextBulletSizePointImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint
    public int getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint
    public STTextFontSize xgetVal() {
        STTextFontSize sTTextFontSize;
        synchronized (monitor()) {
            check_orphaned();
            sTTextFontSize = (STTextFontSize) get_store().find_attribute_user(VAL$0);
        }
        return sTTextFontSize;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint
    public void setVal(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint
    public void xsetVal(STTextFontSize sTTextFontSize) {
        synchronized (monitor()) {
            check_orphaned();
            STTextFontSize sTTextFontSize2 = (STTextFontSize) get_store().find_attribute_user(VAL$0);
            if (sTTextFontSize2 == null) {
                sTTextFontSize2 = (STTextFontSize) get_store().add_attribute_user(VAL$0);
            }
            sTTextFontSize2.set(sTTextFontSize);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
