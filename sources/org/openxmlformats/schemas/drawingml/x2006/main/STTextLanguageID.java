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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextLanguageID.class */
public interface STTextLanguageID extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextLanguageID.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextlanguageid806btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextLanguageID$Factory.class */
    public static final class Factory {
        public static STTextLanguageID newValue(Object obj) {
            return (STTextLanguageID) STTextLanguageID.type.newValue(obj);
        }

        public static STTextLanguageID newInstance() {
            return (STTextLanguageID) POIXMLTypeLoader.newInstance(STTextLanguageID.type, null);
        }

        public static STTextLanguageID newInstance(XmlOptions xmlOptions) {
            return (STTextLanguageID) POIXMLTypeLoader.newInstance(STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(String str) throws XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(str, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(str, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(File file) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(file, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(file, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(URL url) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(url, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(url, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(inputStream, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(inputStream, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(Reader reader) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(reader, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(reader, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(xMLStreamReader, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(xMLStreamReader, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(Node node) throws XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(node, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(node, STTextLanguageID.type, xmlOptions);
        }

        public static STTextLanguageID parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(xMLInputStream, STTextLanguageID.type, (XmlOptions) null);
        }

        public static STTextLanguageID parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextLanguageID) POIXMLTypeLoader.parse(xMLInputStream, STTextLanguageID.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextLanguageID.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextLanguageID.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
