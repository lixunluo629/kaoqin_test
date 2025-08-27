package com.microsoft.schemas.office.x2006.encryption;

import com.microsoft.schemas.office.x2006.encryption.STCipherAlgorithm;
import com.microsoft.schemas.office.x2006.encryption.STCipherChaining;
import com.microsoft.schemas.office.x2006.encryption.STHashAlgorithm;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyData.class */
public interface CTKeyData extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTKeyData.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ctkeydata6bdbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/CTKeyData$Factory.class */
    public static final class Factory {
        public static CTKeyData newInstance() {
            return (CTKeyData) POIXMLTypeLoader.newInstance(CTKeyData.type, null);
        }

        public static CTKeyData newInstance(XmlOptions xmlOptions) {
            return (CTKeyData) POIXMLTypeLoader.newInstance(CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(String str) throws XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(str, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(str, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(File file) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(file, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(file, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(URL url) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(url, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(url, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(InputStream inputStream) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(inputStream, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(inputStream, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(Reader reader) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(reader, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTKeyData) POIXMLTypeLoader.parse(reader, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(xMLStreamReader, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(xMLStreamReader, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(Node node) throws XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(node, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(node, CTKeyData.type, xmlOptions);
        }

        public static CTKeyData parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(xMLInputStream, CTKeyData.type, (XmlOptions) null);
        }

        public static CTKeyData parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTKeyData) POIXMLTypeLoader.parse(xMLInputStream, CTKeyData.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTKeyData.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTKeyData.type, xmlOptions);
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
}
