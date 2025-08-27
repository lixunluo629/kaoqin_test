package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType;
import org.etsi.uri.x01903.v13.SignedPropertiesType;
import org.etsi.uri.x01903.v13.SignedSignaturePropertiesType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/impl/SignedPropertiesTypeImpl.class */
public class SignedPropertiesTypeImpl extends XmlComplexContentImpl implements SignedPropertiesType {
    private static final QName SIGNEDSIGNATUREPROPERTIES$0 = new QName(SignatureFacet.XADES_132_NS, "SignedSignatureProperties");
    private static final QName SIGNEDDATAOBJECTPROPERTIES$2 = new QName(SignatureFacet.XADES_132_NS, "SignedDataObjectProperties");
    private static final QName ID$4 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);

    public SignedPropertiesTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public SignedSignaturePropertiesType getSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            SignedSignaturePropertiesType signedSignaturePropertiesType = (SignedSignaturePropertiesType) get_store().find_element_user(SIGNEDSIGNATUREPROPERTIES$0, 0);
            if (signedSignaturePropertiesType == null) {
                return null;
            }
            return signedSignaturePropertiesType;
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public boolean isSetSignedSignatureProperties() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SIGNEDSIGNATUREPROPERTIES$0) != 0;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void setSignedSignatureProperties(SignedSignaturePropertiesType signedSignaturePropertiesType) {
        synchronized (monitor()) {
            check_orphaned();
            SignedSignaturePropertiesType signedSignaturePropertiesType2 = (SignedSignaturePropertiesType) get_store().find_element_user(SIGNEDSIGNATUREPROPERTIES$0, 0);
            if (signedSignaturePropertiesType2 == null) {
                signedSignaturePropertiesType2 = (SignedSignaturePropertiesType) get_store().add_element_user(SIGNEDSIGNATUREPROPERTIES$0);
            }
            signedSignaturePropertiesType2.set(signedSignaturePropertiesType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public SignedSignaturePropertiesType addNewSignedSignatureProperties() {
        SignedSignaturePropertiesType signedSignaturePropertiesType;
        synchronized (monitor()) {
            check_orphaned();
            signedSignaturePropertiesType = (SignedSignaturePropertiesType) get_store().add_element_user(SIGNEDSIGNATUREPROPERTIES$0);
        }
        return signedSignaturePropertiesType;
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void unsetSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SIGNEDSIGNATUREPROPERTIES$0, 0);
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public SignedDataObjectPropertiesType getSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            SignedDataObjectPropertiesType signedDataObjectPropertiesTypeFind_element_user = get_store().find_element_user(SIGNEDDATAOBJECTPROPERTIES$2, 0);
            if (signedDataObjectPropertiesTypeFind_element_user == null) {
                return null;
            }
            return signedDataObjectPropertiesTypeFind_element_user;
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public boolean isSetSignedDataObjectProperties() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SIGNEDDATAOBJECTPROPERTIES$2) != 0;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void setSignedDataObjectProperties(SignedDataObjectPropertiesType signedDataObjectPropertiesType) {
        synchronized (monitor()) {
            check_orphaned();
            SignedDataObjectPropertiesType signedDataObjectPropertiesTypeFind_element_user = get_store().find_element_user(SIGNEDDATAOBJECTPROPERTIES$2, 0);
            if (signedDataObjectPropertiesTypeFind_element_user == null) {
                signedDataObjectPropertiesTypeFind_element_user = (SignedDataObjectPropertiesType) get_store().add_element_user(SIGNEDDATAOBJECTPROPERTIES$2);
            }
            signedDataObjectPropertiesTypeFind_element_user.set(signedDataObjectPropertiesType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public SignedDataObjectPropertiesType addNewSignedDataObjectProperties() {
        SignedDataObjectPropertiesType signedDataObjectPropertiesTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            signedDataObjectPropertiesTypeAdd_element_user = get_store().add_element_user(SIGNEDDATAOBJECTPROPERTIES$2);
        }
        return signedDataObjectPropertiesTypeAdd_element_user;
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void unsetSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SIGNEDDATAOBJECTPROPERTIES$2, 0);
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$4);
        }
        return xmlID;
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$4) != null;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void xsetId(XmlID xmlID) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID xmlID2 = (XmlID) get_store().find_attribute_user(ID$4);
            if (xmlID2 == null) {
                xmlID2 = (XmlID) get_store().add_attribute_user(ID$4);
            }
            xmlID2.set(xmlID);
        }
    }

    @Override // org.etsi.uri.x01903.v13.SignedPropertiesType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$4);
        }
    }
}
