package org.w3.x2000.x09.xmldsig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/TransformType.class */
public interface TransformType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TransformType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("transformtype550btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/TransformType$Factory.class */
    public static final class Factory {
        public static TransformType newInstance() {
            return (TransformType) POIXMLTypeLoader.newInstance(TransformType.type, null);
        }

        public static TransformType newInstance(XmlOptions xmlOptions) {
            return (TransformType) POIXMLTypeLoader.newInstance(TransformType.type, xmlOptions);
        }

        public static TransformType parse(String str) throws XmlException {
            return (TransformType) POIXMLTypeLoader.parse(str, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (TransformType) POIXMLTypeLoader.parse(str, TransformType.type, xmlOptions);
        }

        public static TransformType parse(File file) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(file, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(file, TransformType.type, xmlOptions);
        }

        public static TransformType parse(URL url) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(url, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(url, TransformType.type, xmlOptions);
        }

        public static TransformType parse(InputStream inputStream) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(inputStream, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(inputStream, TransformType.type, xmlOptions);
        }

        public static TransformType parse(Reader reader) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(reader, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TransformType) POIXMLTypeLoader.parse(reader, TransformType.type, xmlOptions);
        }

        public static TransformType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (TransformType) POIXMLTypeLoader.parse(xMLStreamReader, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (TransformType) POIXMLTypeLoader.parse(xMLStreamReader, TransformType.type, xmlOptions);
        }

        public static TransformType parse(Node node) throws XmlException {
            return (TransformType) POIXMLTypeLoader.parse(node, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (TransformType) POIXMLTypeLoader.parse(node, TransformType.type, xmlOptions);
        }

        public static TransformType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (TransformType) POIXMLTypeLoader.parse(xMLInputStream, TransformType.type, (XmlOptions) null);
        }

        public static TransformType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (TransformType) POIXMLTypeLoader.parse(xMLInputStream, TransformType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, TransformType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, TransformType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<String> getXPathList();

    String[] getXPathArray();

    String getXPathArray(int i);

    List<XmlString> xgetXPathList();

    XmlString[] xgetXPathArray();

    XmlString xgetXPathArray(int i);

    int sizeOfXPathArray();

    void setXPathArray(String[] strArr);

    void setXPathArray(int i, String str);

    void xsetXPathArray(XmlString[] xmlStringArr);

    void xsetXPathArray(int i, XmlString xmlString);

    void insertXPath(int i, String str);

    void addXPath(String str);

    XmlString insertNewXPath(int i);

    XmlString addNewXPath();

    void removeXPath(int i);

    String getAlgorithm();

    XmlAnyURI xgetAlgorithm();

    void setAlgorithm(String str);

    void xsetAlgorithm(XmlAnyURI xmlAnyURI);
}
