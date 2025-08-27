package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/impl/CTRelationshipReferenceImpl.class */
public class CTRelationshipReferenceImpl extends JavaStringHolderEx implements CTRelationshipReference {
    private static final QName SOURCEID$0 = new QName("", "SourceId");

    public CTRelationshipReferenceImpl(SchemaType schemaType) {
        super(schemaType, true);
    }

    protected CTRelationshipReferenceImpl(SchemaType schemaType, boolean z) {
        super(schemaType, z);
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference
    public String getSourceId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SOURCEID$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference
    public XmlString xgetSourceId() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(SOURCEID$0);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference
    public void setSourceId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SOURCEID$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SOURCEID$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference
    public void xsetSourceId(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(SOURCEID$0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(SOURCEID$0);
            }
            xmlString2.set(xmlString);
        }
    }
}
