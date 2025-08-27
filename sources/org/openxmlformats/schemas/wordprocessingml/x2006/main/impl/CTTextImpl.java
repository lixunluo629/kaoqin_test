package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTTextImpl.class */
public class CTTextImpl extends JavaStringHolderEx implements CTText {
    private static final QName SPACE$0 = new QName("http://www.w3.org/XML/1998/namespace", "space");

    public CTTextImpl(SchemaType schemaType) {
        super(schemaType, true);
    }

    protected CTTextImpl(SchemaType schemaType, boolean z) {
        super(schemaType, z);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText
    public SpaceAttribute.Space.Enum getSpace() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPACE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SPACE$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (SpaceAttribute.Space.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText
    public SpaceAttribute.Space xgetSpace() {
        SpaceAttribute.Space space;
        synchronized (monitor()) {
            check_orphaned();
            SpaceAttribute.Space space2 = (SpaceAttribute.Space) get_store().find_attribute_user(SPACE$0);
            if (space2 == null) {
                space2 = (SpaceAttribute.Space) get_default_attribute_value(SPACE$0);
            }
            space = space2;
        }
        return space;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText
    public boolean isSetSpace() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SPACE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText
    public void setSpace(SpaceAttribute.Space.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPACE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SPACE$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText
    public void xsetSpace(SpaceAttribute.Space space) {
        synchronized (monitor()) {
            check_orphaned();
            SpaceAttribute.Space space2 = (SpaceAttribute.Space) get_store().find_attribute_user(SPACE$0);
            if (space2 == null) {
                space2 = (SpaceAttribute.Space) get_store().add_attribute_user(SPACE$0);
            }
            space2.set(space);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText
    public void unsetSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SPACE$0);
        }
    }
}
