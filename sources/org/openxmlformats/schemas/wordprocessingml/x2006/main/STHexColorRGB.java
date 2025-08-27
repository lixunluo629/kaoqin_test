package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.apache.xmlbeans.XmlHexBinary;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHexColorRGB.class */
public interface STHexColorRGB extends XmlHexBinary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STHexColorRGB.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sthexcolorrgbd59dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHexColorRGB$Factory.class */
    public static final class Factory {
        public static STHexColorRGB newValue(Object obj) {
            return (STHexColorRGB) STHexColorRGB.type.newValue(obj);
        }

        public static STHexColorRGB newInstance() {
            return (STHexColorRGB) POIXMLTypeLoader.newInstance(STHexColorRGB.type, null);
        }

        public static STHexColorRGB newInstance(XmlOptions xmlOptions) {
            return (STHexColorRGB) POIXMLTypeLoader.newInstance(STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(String str) throws XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(str, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(str, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(File file) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(file, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(file, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(URL url) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(url, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(url, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(InputStream inputStream) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(inputStream, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(inputStream, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(Reader reader) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(reader, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(reader, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(xMLStreamReader, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(xMLStreamReader, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(Node node) throws XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(node, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(node, STHexColorRGB.type, xmlOptions);
        }

        public static STHexColorRGB parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(xMLInputStream, STHexColorRGB.type, (XmlOptions) null);
        }

        public static STHexColorRGB parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STHexColorRGB) POIXMLTypeLoader.parse(xMLInputStream, STHexColorRGB.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STHexColorRGB.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STHexColorRGB.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
