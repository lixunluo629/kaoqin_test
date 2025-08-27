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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/CanonicalizationMethodType.class */
public interface CanonicalizationMethodType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CanonicalizationMethodType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("canonicalizationmethodtypeec74type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/CanonicalizationMethodType$Factory.class */
    public static final class Factory {
        public static CanonicalizationMethodType newInstance() {
            return (CanonicalizationMethodType) POIXMLTypeLoader.newInstance(CanonicalizationMethodType.type, null);
        }

        public static CanonicalizationMethodType newInstance(XmlOptions xmlOptions) {
            return (CanonicalizationMethodType) POIXMLTypeLoader.newInstance(CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(String str) throws XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(str, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(str, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(File file) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(file, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(file, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(URL url) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(url, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(url, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(InputStream inputStream) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(inputStream, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(inputStream, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(Reader reader) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(reader, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(reader, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(xMLStreamReader, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(xMLStreamReader, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(Node node) throws XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(node, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(node, CanonicalizationMethodType.type, xmlOptions);
        }

        public static CanonicalizationMethodType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(xMLInputStream, CanonicalizationMethodType.type, (XmlOptions) null);
        }

        public static CanonicalizationMethodType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CanonicalizationMethodType) POIXMLTypeLoader.parse(xMLInputStream, CanonicalizationMethodType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CanonicalizationMethodType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CanonicalizationMethodType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getAlgorithm();

    XmlAnyURI xgetAlgorithm();

    void setAlgorithm(String str);

    void xsetAlgorithm(XmlAnyURI xmlAnyURI);
}
