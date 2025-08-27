package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime;
import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/impl/SignatureTimeDocumentImpl.class */
public class SignatureTimeDocumentImpl extends XmlComplexContentImpl implements SignatureTimeDocument {
    private static final QName SIGNATURETIME$0 = new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "SignatureTime");

    public SignatureTimeDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument
    public CTSignatureTime getSignatureTime() {
        synchronized (monitor()) {
            check_orphaned();
            CTSignatureTime cTSignatureTime = (CTSignatureTime) get_store().find_element_user(SIGNATURETIME$0, 0);
            if (cTSignatureTime == null) {
                return null;
            }
            return cTSignatureTime;
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument
    public void setSignatureTime(CTSignatureTime cTSignatureTime) {
        synchronized (monitor()) {
            check_orphaned();
            CTSignatureTime cTSignatureTime2 = (CTSignatureTime) get_store().find_element_user(SIGNATURETIME$0, 0);
            if (cTSignatureTime2 == null) {
                cTSignatureTime2 = (CTSignatureTime) get_store().add_element_user(SIGNATURETIME$0);
            }
            cTSignatureTime2.set(cTSignatureTime);
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument
    public CTSignatureTime addNewSignatureTime() {
        CTSignatureTime cTSignatureTime;
        synchronized (monitor()) {
            check_orphaned();
            cTSignatureTime = (CTSignatureTime) get_store().add_element_user(SIGNATURETIME$0);
        }
        return cTSignatureTime;
    }
}
