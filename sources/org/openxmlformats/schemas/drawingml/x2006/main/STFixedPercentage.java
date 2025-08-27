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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STFixedPercentage.class */
public interface STFixedPercentage extends STPercentage {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STFixedPercentage.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stfixedpercentagef0cftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STFixedPercentage$Factory.class */
    public static final class Factory {
        public static STFixedPercentage newValue(Object obj) {
            return (STFixedPercentage) STFixedPercentage.type.newValue(obj);
        }

        public static STFixedPercentage newInstance() {
            return (STFixedPercentage) POIXMLTypeLoader.newInstance(STFixedPercentage.type, null);
        }

        public static STFixedPercentage newInstance(XmlOptions xmlOptions) {
            return (STFixedPercentage) POIXMLTypeLoader.newInstance(STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(String str) throws XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(str, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(str, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(File file) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(file, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(file, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(URL url) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(url, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(url, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(InputStream inputStream) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(inputStream, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(inputStream, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(Reader reader) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(reader, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(reader, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(xMLStreamReader, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(xMLStreamReader, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(Node node) throws XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(node, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(node, STFixedPercentage.type, xmlOptions);
        }

        public static STFixedPercentage parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(xMLInputStream, STFixedPercentage.type, (XmlOptions) null);
        }

        public static STFixedPercentage parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STFixedPercentage) POIXMLTypeLoader.parse(xMLInputStream, STFixedPercentage.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFixedPercentage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFixedPercentage.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
