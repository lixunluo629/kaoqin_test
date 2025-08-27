package org.apache.xmlbeans.impl.xb.xmlschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/impl/SpaceAttributeImpl.class */
public class SpaceAttributeImpl extends XmlComplexContentImpl implements SpaceAttribute {
    private static final long serialVersionUID = 1;
    private static final QName SPACE$0 = new QName("http://www.w3.org/XML/1998/namespace", "space");

    public SpaceAttributeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute
    public SpaceAttribute.Space.Enum getSpace() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(SPACE$0);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(SPACE$0);
            }
            if (target == null) {
                return null;
            }
            return (SpaceAttribute.Space.Enum) target.getEnumValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute
    public SpaceAttribute.Space xgetSpace() {
        SpaceAttribute.Space space;
        synchronized (monitor()) {
            check_orphaned();
            SpaceAttribute.Space target = (SpaceAttribute.Space) get_store().find_attribute_user(SPACE$0);
            if (target == null) {
                target = (SpaceAttribute.Space) get_default_attribute_value(SPACE$0);
            }
            space = target;
        }
        return space;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute
    public boolean isSetSpace() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SPACE$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute
    public void setSpace(SpaceAttribute.Space.Enum space) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(SPACE$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(SPACE$0);
            }
            target.setEnumValue(space);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute
    public void xsetSpace(SpaceAttribute.Space space) {
        synchronized (monitor()) {
            check_orphaned();
            SpaceAttribute.Space target = (SpaceAttribute.Space) get_store().find_attribute_user(SPACE$0);
            if (target == null) {
                target = (SpaceAttribute.Space) get_store().add_attribute_user(SPACE$0);
            }
            target.set(space);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute
    public void unsetSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SPACE$0);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/impl/SpaceAttributeImpl$SpaceImpl.class */
    public static class SpaceImpl extends JavaStringEnumerationHolderEx implements SpaceAttribute.Space {
        private static final long serialVersionUID = 1;

        public SpaceImpl(SchemaType sType) {
            super(sType, false);
        }

        protected SpaceImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
