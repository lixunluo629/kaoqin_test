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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnderlineValues;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTUnderlineProperty.class */
public interface CTUnderlineProperty extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTUnderlineProperty.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctunderlineproperty8e20type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTUnderlineProperty$Factory.class */
    public static final class Factory {
        public static CTUnderlineProperty newInstance() {
            return (CTUnderlineProperty) POIXMLTypeLoader.newInstance(CTUnderlineProperty.type, null);
        }

        public static CTUnderlineProperty newInstance(XmlOptions xmlOptions) {
            return (CTUnderlineProperty) POIXMLTypeLoader.newInstance(CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(String str) throws XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(str, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(str, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(File file) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(file, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(file, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(URL url) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(url, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(url, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(InputStream inputStream) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(inputStream, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(inputStream, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(Reader reader) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(reader, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(reader, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(xMLStreamReader, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(xMLStreamReader, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(Node node) throws XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(node, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(node, CTUnderlineProperty.type, xmlOptions);
        }

        public static CTUnderlineProperty parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(xMLInputStream, CTUnderlineProperty.type, (XmlOptions) null);
        }

        public static CTUnderlineProperty parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTUnderlineProperty) POIXMLTypeLoader.parse(xMLInputStream, CTUnderlineProperty.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTUnderlineProperty.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTUnderlineProperty.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STUnderlineValues.Enum getVal();

    STUnderlineValues xgetVal();

    boolean isSetVal();

    void setVal(STUnderlineValues.Enum r1);

    void xsetVal(STUnderlineValues sTUnderlineValues);

    void unsetVal();
}
