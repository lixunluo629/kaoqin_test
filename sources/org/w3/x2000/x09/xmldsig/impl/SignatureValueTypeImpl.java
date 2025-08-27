package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.values.JavaBase64HolderEx;
import org.w3.x2000.x09.xmldsig.SignatureValueType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/SignatureValueTypeImpl.class */
public class SignatureValueTypeImpl extends JavaBase64HolderEx implements SignatureValueType {
    private static final QName ID$0 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);

    public SignatureValueTypeImpl(SchemaType schemaType) {
        super(schemaType, true);
    }

    protected SignatureValueTypeImpl(SchemaType schemaType, boolean z) {
        super(schemaType, z);
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureValueType
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureValueType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$0);
        }
        return xmlID;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureValueType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$0) != null;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureValueType
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureValueType
    public void xsetId(XmlID xmlID) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID xmlID2 = (XmlID) get_store().find_attribute_user(ID$0);
            if (xmlID2 == null) {
                xmlID2 = (XmlID) get_store().add_attribute_user(ID$0);
            }
            xmlID2.set(xmlID);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureValueType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$0);
        }
    }
}
