package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3.x2000.x09.xmldsig.X509IssuerSerialType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CertIDType.class */
public interface CertIDType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CertIDType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("certidtypee64dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CertIDType$Factory.class */
    public static final class Factory {
        public static CertIDType newInstance() {
            return (CertIDType) POIXMLTypeLoader.newInstance(CertIDType.type, null);
        }

        public static CertIDType newInstance(XmlOptions xmlOptions) {
            return (CertIDType) POIXMLTypeLoader.newInstance(CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(String str) throws XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(str, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(str, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(File file) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(file, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(file, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(URL url) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(url, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(url, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(InputStream inputStream) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(inputStream, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(inputStream, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(Reader reader) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(reader, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertIDType) POIXMLTypeLoader.parse(reader, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(xMLStreamReader, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(xMLStreamReader, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(Node node) throws XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(node, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(node, CertIDType.type, xmlOptions);
        }

        public static CertIDType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(xMLInputStream, CertIDType.type, (XmlOptions) null);
        }

        public static CertIDType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CertIDType) POIXMLTypeLoader.parse(xMLInputStream, CertIDType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CertIDType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CertIDType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    DigestAlgAndValueType getCertDigest();

    void setCertDigest(DigestAlgAndValueType digestAlgAndValueType);

    DigestAlgAndValueType addNewCertDigest();

    X509IssuerSerialType getIssuerSerial();

    void setIssuerSerial(X509IssuerSerialType x509IssuerSerialType);

    X509IssuerSerialType addNewIssuerSerial();

    String getURI();

    XmlAnyURI xgetURI();

    boolean isSetURI();

    void setURI(String str);

    void xsetURI(XmlAnyURI xmlAnyURI);

    void unsetURI();
}
