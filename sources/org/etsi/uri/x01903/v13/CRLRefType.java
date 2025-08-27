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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CRLRefType.class */
public interface CRLRefType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CRLRefType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("crlreftype4444type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CRLRefType$Factory.class */
    public static final class Factory {
        public static CRLRefType newInstance() {
            return (CRLRefType) POIXMLTypeLoader.newInstance(CRLRefType.type, null);
        }

        public static CRLRefType newInstance(XmlOptions xmlOptions) {
            return (CRLRefType) POIXMLTypeLoader.newInstance(CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(String str) throws XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(str, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(str, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(File file) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(file, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(file, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(URL url) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(url, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(url, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(InputStream inputStream) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(inputStream, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(inputStream, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(Reader reader) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(reader, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefType) POIXMLTypeLoader.parse(reader, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(xMLStreamReader, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(xMLStreamReader, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(Node node) throws XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(node, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(node, CRLRefType.type, xmlOptions);
        }

        public static CRLRefType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(xMLInputStream, CRLRefType.type, (XmlOptions) null);
        }

        public static CRLRefType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CRLRefType) POIXMLTypeLoader.parse(xMLInputStream, CRLRefType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CRLRefType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CRLRefType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    DigestAlgAndValueType getDigestAlgAndValue();

    void setDigestAlgAndValue(DigestAlgAndValueType digestAlgAndValueType);

    DigestAlgAndValueType addNewDigestAlgAndValue();

    CRLIdentifierType getCRLIdentifier();

    boolean isSetCRLIdentifier();

    void setCRLIdentifier(CRLIdentifierType cRLIdentifierType);

    CRLIdentifierType addNewCRLIdentifier();

    void unsetCRLIdentifier();
}
