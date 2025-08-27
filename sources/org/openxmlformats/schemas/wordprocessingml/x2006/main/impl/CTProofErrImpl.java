package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STProofErr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STProofErr$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTProofErrImpl.class */
public class CTProofErrImpl extends XmlComplexContentImpl implements CTProofErr {
    private static final QName TYPE$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "type");

    public CTProofErrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr
    public STProofErr$Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$0);
            if (simpleValue == null) {
                return null;
            }
            return (STProofErr$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr
    public STProofErr xgetType() {
        STProofErr sTProofErrFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTProofErrFind_attribute_user = get_store().find_attribute_user(TYPE$0);
        }
        return sTProofErrFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr
    public void setType(STProofErr$Enum sTProofErr$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPE$0);
            }
            simpleValue.setEnumValue(sTProofErr$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr
    public void xsetType(STProofErr sTProofErr) {
        synchronized (monitor()) {
            check_orphaned();
            STProofErr sTProofErrFind_attribute_user = get_store().find_attribute_user(TYPE$0);
            if (sTProofErrFind_attribute_user == null) {
                sTProofErrFind_attribute_user = (STProofErr) get_store().add_attribute_user(TYPE$0);
            }
            sTProofErrFind_attribute_user.set(sTProofErr);
        }
    }
}
