package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPercentage.class */
public interface STPercentage extends XmlInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STPercentage.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stpercentage0a85type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPercentage$Factory.class */
    public static final class Factory {
        public static STPercentage newValue(Object obj) {
            return (STPercentage) STPercentage.type.newValue(obj);
        }

        public static STPercentage newInstance() {
            return (STPercentage) POIXMLTypeLoader.newInstance(STPercentage.type, null);
        }

        public static STPercentage newInstance(XmlOptions xmlOptions) {
            return (STPercentage) POIXMLTypeLoader.newInstance(STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(String str) throws XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(str, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(str, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(File file) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(file, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(file, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(URL url) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(url, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(url, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(InputStream inputStream) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(inputStream, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(inputStream, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(Reader reader) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(reader, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPercentage) POIXMLTypeLoader.parse(reader, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(xMLStreamReader, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(xMLStreamReader, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(Node node) throws XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(node, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(node, STPercentage.type, xmlOptions);
        }

        public static STPercentage parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(xMLInputStream, STPercentage.type, (XmlOptions) null);
        }

        public static STPercentage parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STPercentage) POIXMLTypeLoader.parse(xMLInputStream, STPercentage.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPercentage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPercentage.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
