package com.microsoft.schemas.office.x2006.keyEncryptor.certificate.impl;

import com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/keyEncryptor/certificate/impl/CTCertificateKeyEncryptorImpl.class */
public class CTCertificateKeyEncryptorImpl extends XmlComplexContentImpl implements CTCertificateKeyEncryptor {
    private static final QName ENCRYPTEDKEYVALUE$0 = new QName("", "encryptedKeyValue");
    private static final QName X509CERTIFICATE$2 = new QName("", "X509Certificate");
    private static final QName CERTVERIFIER$4 = new QName("", "certVerifier");

    public CTCertificateKeyEncryptorImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public byte[] getEncryptedKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENCRYPTEDKEYVALUE$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public XmlBase64Binary xgetEncryptedKeyValue() {
        XmlBase64Binary xmlBase64Binary;
        synchronized (monitor()) {
            check_orphaned();
            xmlBase64Binary = (XmlBase64Binary) get_store().find_attribute_user(ENCRYPTEDKEYVALUE$0);
        }
        return xmlBase64Binary;
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public void setEncryptedKeyValue(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENCRYPTEDKEYVALUE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENCRYPTEDKEYVALUE$0);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public void xsetEncryptedKeyValue(XmlBase64Binary xmlBase64Binary) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBase64Binary xmlBase64Binary2 = (XmlBase64Binary) get_store().find_attribute_user(ENCRYPTEDKEYVALUE$0);
            if (xmlBase64Binary2 == null) {
                xmlBase64Binary2 = (XmlBase64Binary) get_store().add_attribute_user(ENCRYPTEDKEYVALUE$0);
            }
            xmlBase64Binary2.set(xmlBase64Binary);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public byte[] getX509Certificate() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(X509CERTIFICATE$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public XmlBase64Binary xgetX509Certificate() {
        XmlBase64Binary xmlBase64Binary;
        synchronized (monitor()) {
            check_orphaned();
            xmlBase64Binary = (XmlBase64Binary) get_store().find_attribute_user(X509CERTIFICATE$2);
        }
        return xmlBase64Binary;
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public void setX509Certificate(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(X509CERTIFICATE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(X509CERTIFICATE$2);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public void xsetX509Certificate(XmlBase64Binary xmlBase64Binary) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBase64Binary xmlBase64Binary2 = (XmlBase64Binary) get_store().find_attribute_user(X509CERTIFICATE$2);
            if (xmlBase64Binary2 == null) {
                xmlBase64Binary2 = (XmlBase64Binary) get_store().add_attribute_user(X509CERTIFICATE$2);
            }
            xmlBase64Binary2.set(xmlBase64Binary);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public byte[] getCertVerifier() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CERTVERIFIER$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public XmlBase64Binary xgetCertVerifier() {
        XmlBase64Binary xmlBase64Binary;
        synchronized (monitor()) {
            check_orphaned();
            xmlBase64Binary = (XmlBase64Binary) get_store().find_attribute_user(CERTVERIFIER$4);
        }
        return xmlBase64Binary;
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public void setCertVerifier(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CERTVERIFIER$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CERTVERIFIER$4);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor
    public void xsetCertVerifier(XmlBase64Binary xmlBase64Binary) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBase64Binary xmlBase64Binary2 = (XmlBase64Binary) get_store().find_attribute_user(CERTVERIFIER$4);
            if (xmlBase64Binary2 == null) {
                xmlBase64Binary2 = (XmlBase64Binary) get_store().add_attribute_user(CERTVERIFIER$4);
            }
            xmlBase64Binary2.set(xmlBase64Binary);
        }
    }
}
