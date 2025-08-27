package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CompleteCertificateRefsType.class */
public interface CompleteCertificateRefsType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CompleteCertificateRefsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("completecertificaterefstype07datype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CompleteCertificateRefsType$Factory.class */
    public static final class Factory {
        public static CompleteCertificateRefsType newInstance() {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.newInstance(CompleteCertificateRefsType.type, null);
        }

        public static CompleteCertificateRefsType newInstance(XmlOptions xmlOptions) {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.newInstance(CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(String str) throws XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(str, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(str, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(File file) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(file, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(file, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(URL url) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(url, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(url, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(InputStream inputStream) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(inputStream, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(inputStream, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(Reader reader) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(reader, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(reader, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(xMLStreamReader, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(xMLStreamReader, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(Node node) throws XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(node, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(node, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static CompleteCertificateRefsType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(xMLInputStream, CompleteCertificateRefsType.type, (XmlOptions) null);
        }

        public static CompleteCertificateRefsType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CompleteCertificateRefsType) POIXMLTypeLoader.parse(xMLInputStream, CompleteCertificateRefsType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CompleteCertificateRefsType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CompleteCertificateRefsType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CertIDListType getCertRefs();

    void setCertRefs(CertIDListType certIDListType);

    CertIDListType addNewCertRefs();

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
