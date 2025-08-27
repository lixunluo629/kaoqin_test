package org.w3.x2000.x09.xmldsig;

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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/DigestMethodType.class */
public interface DigestMethodType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DigestMethodType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("digestmethodtype5ce0type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/DigestMethodType$Factory.class */
    public static final class Factory {
        public static DigestMethodType newInstance() {
            return (DigestMethodType) POIXMLTypeLoader.newInstance(DigestMethodType.type, null);
        }

        public static DigestMethodType newInstance(XmlOptions xmlOptions) {
            return (DigestMethodType) POIXMLTypeLoader.newInstance(DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(String str) throws XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(str, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(str, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(File file) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(file, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(file, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(URL url) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(url, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(url, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(InputStream inputStream) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(inputStream, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(inputStream, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(Reader reader) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(reader, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestMethodType) POIXMLTypeLoader.parse(reader, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(xMLStreamReader, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(xMLStreamReader, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(Node node) throws XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(node, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(node, DigestMethodType.type, xmlOptions);
        }

        public static DigestMethodType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(xMLInputStream, DigestMethodType.type, (XmlOptions) null);
        }

        public static DigestMethodType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (DigestMethodType) POIXMLTypeLoader.parse(xMLInputStream, DigestMethodType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, DigestMethodType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, DigestMethodType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getAlgorithm();

    XmlAnyURI xgetAlgorithm();

    void setAlgorithm(String str);

    void xsetAlgorithm(XmlAnyURI xmlAnyURI);
}
