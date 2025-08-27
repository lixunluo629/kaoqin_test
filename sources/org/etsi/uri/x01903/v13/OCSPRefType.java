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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/OCSPRefType.class */
public interface OCSPRefType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(OCSPRefType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ocspreftype089etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/OCSPRefType$Factory.class */
    public static final class Factory {
        public static OCSPRefType newInstance() {
            return (OCSPRefType) POIXMLTypeLoader.newInstance(OCSPRefType.type, null);
        }

        public static OCSPRefType newInstance(XmlOptions xmlOptions) {
            return (OCSPRefType) POIXMLTypeLoader.newInstance(OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(String str) throws XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(str, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(str, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(File file) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(file, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(file, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(URL url) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(url, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(url, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(InputStream inputStream) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(inputStream, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(inputStream, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(Reader reader) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(reader, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPRefType) POIXMLTypeLoader.parse(reader, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(xMLStreamReader, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(xMLStreamReader, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(Node node) throws XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(node, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(node, OCSPRefType.type, xmlOptions);
        }

        public static OCSPRefType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(xMLInputStream, OCSPRefType.type, (XmlOptions) null);
        }

        public static OCSPRefType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (OCSPRefType) POIXMLTypeLoader.parse(xMLInputStream, OCSPRefType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, OCSPRefType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, OCSPRefType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    OCSPIdentifierType getOCSPIdentifier();

    void setOCSPIdentifier(OCSPIdentifierType oCSPIdentifierType);

    OCSPIdentifierType addNewOCSPIdentifier();

    DigestAlgAndValueType getDigestAlgAndValue();

    boolean isSetDigestAlgAndValue();

    void setDigestAlgAndValue(DigestAlgAndValueType digestAlgAndValueType);

    DigestAlgAndValueType addNewDigestAlgAndValue();

    void unsetDigestAlgAndValue();
}
