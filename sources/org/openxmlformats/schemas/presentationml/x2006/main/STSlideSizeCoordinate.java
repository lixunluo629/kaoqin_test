package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STSlideSizeCoordinate.class */
public interface STSlideSizeCoordinate extends STPositiveCoordinate32 {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STSlideSizeCoordinate.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stslidesizecoordinate24b5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STSlideSizeCoordinate$Factory.class */
    public static final class Factory {
        public static STSlideSizeCoordinate newValue(Object obj) {
            return (STSlideSizeCoordinate) STSlideSizeCoordinate.type.newValue(obj);
        }

        public static STSlideSizeCoordinate newInstance() {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.newInstance(STSlideSizeCoordinate.type, null);
        }

        public static STSlideSizeCoordinate newInstance(XmlOptions xmlOptions) {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.newInstance(STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(String str) throws XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(str, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(str, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(File file) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(file, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(file, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(URL url) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(url, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(url, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(InputStream inputStream) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(inputStream, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(inputStream, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(Reader reader) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(reader, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(reader, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(xMLStreamReader, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(xMLStreamReader, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(Node node) throws XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(node, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(node, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static STSlideSizeCoordinate parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(xMLInputStream, STSlideSizeCoordinate.type, (XmlOptions) null);
        }

        public static STSlideSizeCoordinate parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STSlideSizeCoordinate) POIXMLTypeLoader.parse(xMLInputStream, STSlideSizeCoordinate.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSlideSizeCoordinate.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSlideSizeCoordinate.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
