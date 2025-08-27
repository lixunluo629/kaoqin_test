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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPositivePercentage.class */
public interface STPositivePercentage extends STPercentage {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STPositivePercentage.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stpositivepercentagedb9etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPositivePercentage$Factory.class */
    public static final class Factory {
        public static STPositivePercentage newValue(Object obj) {
            return (STPositivePercentage) STPositivePercentage.type.newValue(obj);
        }

        public static STPositivePercentage newInstance() {
            return (STPositivePercentage) POIXMLTypeLoader.newInstance(STPositivePercentage.type, null);
        }

        public static STPositivePercentage newInstance(XmlOptions xmlOptions) {
            return (STPositivePercentage) POIXMLTypeLoader.newInstance(STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(String str) throws XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(str, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(str, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(File file) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(file, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(file, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(URL url) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(url, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(url, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(InputStream inputStream) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(inputStream, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(inputStream, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(Reader reader) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(reader, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(reader, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(xMLStreamReader, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(xMLStreamReader, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(Node node) throws XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(node, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(node, STPositivePercentage.type, xmlOptions);
        }

        public static STPositivePercentage parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(xMLInputStream, STPositivePercentage.type, (XmlOptions) null);
        }

        public static STPositivePercentage parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STPositivePercentage) POIXMLTypeLoader.parse(xMLInputStream, STPositivePercentage.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPositivePercentage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPositivePercentage.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
