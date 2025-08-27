package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/STColID.class */
public interface STColID extends XmlInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STColID.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcolidb7f5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/STColID$Factory.class */
    public static final class Factory {
        public static STColID newValue(Object obj) {
            return (STColID) STColID.type.newValue(obj);
        }

        public static STColID newInstance() {
            return (STColID) POIXMLTypeLoader.newInstance(STColID.type, null);
        }

        public static STColID newInstance(XmlOptions xmlOptions) {
            return (STColID) POIXMLTypeLoader.newInstance(STColID.type, xmlOptions);
        }

        public static STColID parse(String str) throws XmlException {
            return (STColID) POIXMLTypeLoader.parse(str, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STColID) POIXMLTypeLoader.parse(str, STColID.type, xmlOptions);
        }

        public static STColID parse(File file) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(file, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(file, STColID.type, xmlOptions);
        }

        public static STColID parse(URL url) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(url, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(url, STColID.type, xmlOptions);
        }

        public static STColID parse(InputStream inputStream) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(inputStream, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(inputStream, STColID.type, xmlOptions);
        }

        public static STColID parse(Reader reader) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(reader, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColID) POIXMLTypeLoader.parse(reader, STColID.type, xmlOptions);
        }

        public static STColID parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STColID) POIXMLTypeLoader.parse(xMLStreamReader, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STColID) POIXMLTypeLoader.parse(xMLStreamReader, STColID.type, xmlOptions);
        }

        public static STColID parse(Node node) throws XmlException {
            return (STColID) POIXMLTypeLoader.parse(node, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STColID) POIXMLTypeLoader.parse(node, STColID.type, xmlOptions);
        }

        public static STColID parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STColID) POIXMLTypeLoader.parse(xMLInputStream, STColID.type, (XmlOptions) null);
        }

        public static STColID parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STColID) POIXMLTypeLoader.parse(xMLInputStream, STColID.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STColID.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STColID.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
