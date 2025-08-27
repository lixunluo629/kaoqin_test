package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.w3.x2000.x09.xmldsig.SignatureDocument;
import org.w3.x2000.x09.xmldsig.SignatureType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/SignatureDocumentImpl.class */
public class SignatureDocumentImpl extends XmlComplexContentImpl implements SignatureDocument {
    private static final QName SIGNATURE$0 = new QName(SignatureFacet.XML_DIGSIG_NS, SignatureAttribute.tag);

    public SignatureDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureDocument
    public SignatureType getSignature() {
        synchronized (monitor()) {
            check_orphaned();
            SignatureType signatureType = (SignatureType) get_store().find_element_user(SIGNATURE$0, 0);
            if (signatureType == null) {
                return null;
            }
            return signatureType;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureDocument
    public void setSignature(SignatureType signatureType) {
        synchronized (monitor()) {
            check_orphaned();
            SignatureType signatureType2 = (SignatureType) get_store().find_element_user(SIGNATURE$0, 0);
            if (signatureType2 == null) {
                signatureType2 = (SignatureType) get_store().add_element_user(SIGNATURE$0);
            }
            signatureType2.set(signatureType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureDocument
    public SignatureType addNewSignature() {
        SignatureType signatureType;
        synchronized (monitor()) {
            check_orphaned();
            signatureType = (SignatureType) get_store().add_element_user(SIGNATURE$0);
        }
        return signatureType;
    }
}
