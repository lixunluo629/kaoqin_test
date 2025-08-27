package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/GroupDocumentImpl.class */
public class GroupDocumentImpl extends XmlComplexContentImpl implements GroupDocument {
    private static final long serialVersionUID = 1;
    private static final QName GROUP$0 = new QName("http://www.w3.org/2001/XMLSchema", "group");

    public GroupDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.GroupDocument
    public NamedGroup getGroup() {
        synchronized (monitor()) {
            check_orphaned();
            NamedGroup target = (NamedGroup) get_store().find_element_user(GROUP$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.GroupDocument
    public void setGroup(NamedGroup group) {
        generatedSetterHelperImpl(group, GROUP$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.GroupDocument
    public NamedGroup addNewGroup() {
        NamedGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NamedGroup) get_store().add_element_user(GROUP$0);
        }
        return target;
    }
}
