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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTStretchInfoProperties.class */
public interface CTStretchInfoProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStretchInfoProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstretchinfopropertiesde57type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTStretchInfoProperties$Factory.class */
    public static final class Factory {
        public static CTStretchInfoProperties newInstance() {
            return (CTStretchInfoProperties) POIXMLTypeLoader.newInstance(CTStretchInfoProperties.type, null);
        }

        public static CTStretchInfoProperties newInstance(XmlOptions xmlOptions) {
            return (CTStretchInfoProperties) POIXMLTypeLoader.newInstance(CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(String str) throws XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(str, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(str, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(File file) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(file, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(file, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(URL url) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(url, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(url, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(inputStream, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(inputStream, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(Reader reader) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(reader, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(reader, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(Node node) throws XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(node, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(node, CTStretchInfoProperties.type, xmlOptions);
        }

        public static CTStretchInfoProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(xMLInputStream, CTStretchInfoProperties.type, (XmlOptions) null);
        }

        public static CTStretchInfoProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStretchInfoProperties) POIXMLTypeLoader.parse(xMLInputStream, CTStretchInfoProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStretchInfoProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStretchInfoProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTRelativeRect getFillRect();

    boolean isSetFillRect();

    void setFillRect(CTRelativeRect cTRelativeRect);

    CTRelativeRect addNewFillRect();

    void unsetFillRect();
}
