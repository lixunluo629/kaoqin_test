package org.w3.x2000.x09.xmldsig;

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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/DigestValueType.class */
public interface DigestValueType extends XmlBase64Binary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DigestValueType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("digestvaluetype010atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/DigestValueType$Factory.class */
    public static final class Factory {
        public static DigestValueType newValue(Object obj) {
            return (DigestValueType) DigestValueType.type.newValue(obj);
        }

        public static DigestValueType newInstance() {
            return (DigestValueType) POIXMLTypeLoader.newInstance(DigestValueType.type, null);
        }

        public static DigestValueType newInstance(XmlOptions xmlOptions) {
            return (DigestValueType) POIXMLTypeLoader.newInstance(DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(String str) throws XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(str, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(str, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(File file) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(file, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(file, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(URL url) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(url, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(url, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(InputStream inputStream) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(inputStream, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(inputStream, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(Reader reader) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(reader, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DigestValueType) POIXMLTypeLoader.parse(reader, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(xMLStreamReader, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(xMLStreamReader, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(Node node) throws XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(node, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(node, DigestValueType.type, xmlOptions);
        }

        public static DigestValueType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(xMLInputStream, DigestValueType.type, (XmlOptions) null);
        }

        public static DigestValueType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (DigestValueType) POIXMLTypeLoader.parse(xMLInputStream, DigestValueType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, DigestValueType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, DigestValueType.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
