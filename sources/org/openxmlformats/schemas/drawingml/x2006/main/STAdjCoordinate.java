package org.openxmlformats.schemas.drawingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STAdjCoordinate.class */
public interface STAdjCoordinate extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STAdjCoordinate.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stadjcoordinated920type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STAdjCoordinate$Factory.class */
    public static final class Factory {
        public static STAdjCoordinate newValue(Object obj) {
            return (STAdjCoordinate) STAdjCoordinate.type.newValue(obj);
        }

        public static STAdjCoordinate newInstance() {
            return (STAdjCoordinate) POIXMLTypeLoader.newInstance(STAdjCoordinate.type, null);
        }

        public static STAdjCoordinate newInstance(XmlOptions xmlOptions) {
            return (STAdjCoordinate) POIXMLTypeLoader.newInstance(STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(String str) throws XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(str, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(str, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(File file) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(file, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(file, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(URL url) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(url, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(url, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(InputStream inputStream) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(inputStream, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(inputStream, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(Reader reader) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(reader, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(reader, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(xMLStreamReader, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(xMLStreamReader, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(Node node) throws XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(node, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(node, STAdjCoordinate.type, xmlOptions);
        }

        public static STAdjCoordinate parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(xMLInputStream, STAdjCoordinate.type, (XmlOptions) null);
        }

        public static STAdjCoordinate parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STAdjCoordinate) POIXMLTypeLoader.parse(xMLInputStream, STAdjCoordinate.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAdjCoordinate.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAdjCoordinate.type, xmlOptions);
        }

        private Factory() {
        }
    }

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();
}
