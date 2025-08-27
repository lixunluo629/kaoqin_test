package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBooleanProperty.class */
public interface CTBooleanProperty extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBooleanProperty.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbooleanproperty1f3ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBooleanProperty$Factory.class */
    public static final class Factory {
        public static CTBooleanProperty newInstance() {
            return (CTBooleanProperty) POIXMLTypeLoader.newInstance(CTBooleanProperty.type, null);
        }

        public static CTBooleanProperty newInstance(XmlOptions xmlOptions) {
            return (CTBooleanProperty) POIXMLTypeLoader.newInstance(CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(String str) throws XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(str, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(str, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(File file) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(file, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(file, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(URL url) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(url, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(url, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(inputStream, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(inputStream, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(Reader reader) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(reader, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(reader, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(xMLStreamReader, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(xMLStreamReader, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(Node node) throws XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(node, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(node, CTBooleanProperty.type, xmlOptions);
        }

        public static CTBooleanProperty parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(xMLInputStream, CTBooleanProperty.type, (XmlOptions) null);
        }

        public static CTBooleanProperty parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBooleanProperty) POIXMLTypeLoader.parse(xMLInputStream, CTBooleanProperty.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBooleanProperty.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBooleanProperty.type, xmlOptions);
        }

        private Factory() {
        }
    }

    boolean getVal();

    XmlBoolean xgetVal();

    boolean isSetVal();

    void setVal(boolean z);

    void xsetVal(XmlBoolean xmlBoolean);

    void unsetVal();
}
