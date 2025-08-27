package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTLineEndPropertiesImpl.class */
public class CTLineEndPropertiesImpl extends XmlComplexContentImpl implements CTLineEndProperties {
    private static final QName TYPE$0 = new QName("", "type");
    private static final QName W$2 = new QName("", "w");
    private static final QName LEN$4 = new QName("", "len");

    public CTLineEndPropertiesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public STLineEndType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$0);
            if (simpleValue == null) {
                return null;
            }
            return (STLineEndType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public STLineEndType xgetType() {
        STLineEndType sTLineEndType;
        synchronized (monitor()) {
            check_orphaned();
            sTLineEndType = (STLineEndType) get_store().find_attribute_user(TYPE$0);
        }
        return sTLineEndType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public boolean isSetType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TYPE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void setType(STLineEndType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPE$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void xsetType(STLineEndType sTLineEndType) {
        synchronized (monitor()) {
            check_orphaned();
            STLineEndType sTLineEndType2 = (STLineEndType) get_store().find_attribute_user(TYPE$0);
            if (sTLineEndType2 == null) {
                sTLineEndType2 = (STLineEndType) get_store().add_attribute_user(TYPE$0);
            }
            sTLineEndType2.set(sTLineEndType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TYPE$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public STLineEndWidth.Enum getW() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(W$2);
            if (simpleValue == null) {
                return null;
            }
            return (STLineEndWidth.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public STLineEndWidth xgetW() {
        STLineEndWidth sTLineEndWidth;
        synchronized (monitor()) {
            check_orphaned();
            sTLineEndWidth = (STLineEndWidth) get_store().find_attribute_user(W$2);
        }
        return sTLineEndWidth;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public boolean isSetW() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(W$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void setW(STLineEndWidth.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(W$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(W$2);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void xsetW(STLineEndWidth sTLineEndWidth) {
        synchronized (monitor()) {
            check_orphaned();
            STLineEndWidth sTLineEndWidth2 = (STLineEndWidth) get_store().find_attribute_user(W$2);
            if (sTLineEndWidth2 == null) {
                sTLineEndWidth2 = (STLineEndWidth) get_store().add_attribute_user(W$2);
            }
            sTLineEndWidth2.set(sTLineEndWidth);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(W$2);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public STLineEndLength.Enum getLen() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LEN$4);
            if (simpleValue == null) {
                return null;
            }
            return (STLineEndLength.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public STLineEndLength xgetLen() {
        STLineEndLength sTLineEndLength;
        synchronized (monitor()) {
            check_orphaned();
            sTLineEndLength = (STLineEndLength) get_store().find_attribute_user(LEN$4);
        }
        return sTLineEndLength;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public boolean isSetLen() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LEN$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void setLen(STLineEndLength.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LEN$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LEN$4);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void xsetLen(STLineEndLength sTLineEndLength) {
        synchronized (monitor()) {
            check_orphaned();
            STLineEndLength sTLineEndLength2 = (STLineEndLength) get_store().find_attribute_user(LEN$4);
            if (sTLineEndLength2 == null) {
                sTLineEndLength2 = (STLineEndLength) get_store().add_attribute_user(LEN$4);
            }
            sTLineEndLength2.set(sTLineEndLength);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
    public void unsetLen() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LEN$4);
        }
    }
}
