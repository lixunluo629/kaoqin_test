package com.microsoft.schemas.office.x2006.keyEncryptor.password;

import com.microsoft.schemas.office.x2006.encryption.STBlockSize;
import com.microsoft.schemas.office.x2006.encryption.STCipherAlgorithm;
import com.microsoft.schemas.office.x2006.encryption.STCipherChaining;
import com.microsoft.schemas.office.x2006.encryption.STHashAlgorithm;
import com.microsoft.schemas.office.x2006.encryption.STHashSize;
import com.microsoft.schemas.office.x2006.encryption.STKeyBits;
import com.microsoft.schemas.office.x2006.encryption.STSaltSize;
import com.microsoft.schemas.office.x2006.encryption.STSpinCount;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/keyEncryptor/password/CTPasswordKeyEncryptor.class */
public interface CTPasswordKeyEncryptor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPasswordKeyEncryptor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ctpasswordkeyencryptorde24type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/keyEncryptor/password/CTPasswordKeyEncryptor$Factory.class */
    public static final class Factory {
        public static CTPasswordKeyEncryptor newInstance() {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.newInstance(CTPasswordKeyEncryptor.type, null);
        }

        public static CTPasswordKeyEncryptor newInstance(XmlOptions xmlOptions) {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.newInstance(CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(String str) throws XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(str, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(str, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(File file) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(file, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(file, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(URL url) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(url, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(url, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(inputStream, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(inputStream, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(Reader reader) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(reader, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(reader, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(xMLStreamReader, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(xMLStreamReader, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(Node node) throws XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(node, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(node, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static CTPasswordKeyEncryptor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(xMLInputStream, CTPasswordKeyEncryptor.type, (XmlOptions) null);
        }

        public static CTPasswordKeyEncryptor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPasswordKeyEncryptor) POIXMLTypeLoader.parse(xMLInputStream, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPasswordKeyEncryptor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPasswordKeyEncryptor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getSaltSize();

    STSaltSize xgetSaltSize();

    void setSaltSize(int i);

    void xsetSaltSize(STSaltSize sTSaltSize);

    int getBlockSize();

    STBlockSize xgetBlockSize();

    void setBlockSize(int i);

    void xsetBlockSize(STBlockSize sTBlockSize);

    long getKeyBits();

    STKeyBits xgetKeyBits();

    void setKeyBits(long j);

    void xsetKeyBits(STKeyBits sTKeyBits);

    int getHashSize();

    STHashSize xgetHashSize();

    void setHashSize(int i);

    void xsetHashSize(STHashSize sTHashSize);

    STCipherAlgorithm.Enum getCipherAlgorithm();

    STCipherAlgorithm xgetCipherAlgorithm();

    void setCipherAlgorithm(STCipherAlgorithm.Enum r1);

    void xsetCipherAlgorithm(STCipherAlgorithm sTCipherAlgorithm);

    STCipherChaining.Enum getCipherChaining();

    STCipherChaining xgetCipherChaining();

    void setCipherChaining(STCipherChaining.Enum r1);

    void xsetCipherChaining(STCipherChaining sTCipherChaining);

    STHashAlgorithm.Enum getHashAlgorithm();

    STHashAlgorithm xgetHashAlgorithm();

    void setHashAlgorithm(STHashAlgorithm.Enum r1);

    void xsetHashAlgorithm(STHashAlgorithm sTHashAlgorithm);

    byte[] getSaltValue();

    XmlBase64Binary xgetSaltValue();

    void setSaltValue(byte[] bArr);

    void xsetSaltValue(XmlBase64Binary xmlBase64Binary);

    int getSpinCount();

    STSpinCount xgetSpinCount();

    void setSpinCount(int i);

    void xsetSpinCount(STSpinCount sTSpinCount);

    byte[] getEncryptedVerifierHashInput();

    XmlBase64Binary xgetEncryptedVerifierHashInput();

    void setEncryptedVerifierHashInput(byte[] bArr);

    void xsetEncryptedVerifierHashInput(XmlBase64Binary xmlBase64Binary);

    byte[] getEncryptedVerifierHashValue();

    XmlBase64Binary xgetEncryptedVerifierHashValue();

    void setEncryptedVerifierHashValue(byte[] bArr);

    void xsetEncryptedVerifierHashValue(XmlBase64Binary xmlBase64Binary);

    byte[] getEncryptedKeyValue();

    XmlBase64Binary xgetEncryptedKeyValue();

    void setEncryptedKeyValue(byte[] bArr);

    void xsetEncryptedKeyValue(XmlBase64Binary xmlBase64Binary);
}
