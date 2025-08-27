package com.microsoft.schemas.office.x2006.encryption.impl;

import com.microsoft.schemas.office.x2006.encryption.CTEncryption;
import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/impl/EncryptionDocumentImpl.class */
public class EncryptionDocumentImpl extends XmlComplexContentImpl implements EncryptionDocument {
    private static final QName ENCRYPTION$0 = new QName("http://schemas.microsoft.com/office/2006/encryption", "encryption");

    public EncryptionDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.x2006.encryption.EncryptionDocument
    public CTEncryption getEncryption() {
        synchronized (monitor()) {
            check_orphaned();
            CTEncryption cTEncryption = (CTEncryption) get_store().find_element_user(ENCRYPTION$0, 0);
            if (cTEncryption == null) {
                return null;
            }
            return cTEncryption;
        }
    }

    @Override // com.microsoft.schemas.office.x2006.encryption.EncryptionDocument
    public void setEncryption(CTEncryption cTEncryption) {
        synchronized (monitor()) {
            check_orphaned();
            CTEncryption cTEncryption2 = (CTEncryption) get_store().find_element_user(ENCRYPTION$0, 0);
            if (cTEncryption2 == null) {
                cTEncryption2 = (CTEncryption) get_store().add_element_user(ENCRYPTION$0);
            }
            cTEncryption2.set(cTEncryption);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.encryption.EncryptionDocument
    public CTEncryption addNewEncryption() {
        CTEncryption cTEncryption;
        synchronized (monitor()) {
            check_orphaned();
            cTEncryption = (CTEncryption) get_store().add_element_user(ENCRYPTION$0);
        }
        return cTEncryption;
    }
}
