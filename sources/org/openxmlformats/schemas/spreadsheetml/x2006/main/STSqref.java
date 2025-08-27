package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STSqref.class */
public interface STSqref extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STSqref.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stsqrefb044type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STSqref$Factory.class */
    public static final class Factory {
        public static STSqref newValue(Object obj) {
            return (STSqref) STSqref.type.newValue(obj);
        }

        public static STSqref newInstance() {
            return (STSqref) POIXMLTypeLoader.newInstance(STSqref.type, null);
        }

        public static STSqref newInstance(XmlOptions xmlOptions) {
            return (STSqref) POIXMLTypeLoader.newInstance(STSqref.type, xmlOptions);
        }

        public static STSqref parse(String str) throws XmlException {
            return (STSqref) POIXMLTypeLoader.parse(str, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STSqref) POIXMLTypeLoader.parse(str, STSqref.type, xmlOptions);
        }

        public static STSqref parse(File file) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(file, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(file, STSqref.type, xmlOptions);
        }

        public static STSqref parse(URL url) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(url, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(url, STSqref.type, xmlOptions);
        }

        public static STSqref parse(InputStream inputStream) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(inputStream, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(inputStream, STSqref.type, xmlOptions);
        }

        public static STSqref parse(Reader reader) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(reader, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSqref) POIXMLTypeLoader.parse(reader, STSqref.type, xmlOptions);
        }

        public static STSqref parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STSqref) POIXMLTypeLoader.parse(xMLStreamReader, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STSqref) POIXMLTypeLoader.parse(xMLStreamReader, STSqref.type, xmlOptions);
        }

        public static STSqref parse(Node node) throws XmlException {
            return (STSqref) POIXMLTypeLoader.parse(node, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STSqref) POIXMLTypeLoader.parse(node, STSqref.type, xmlOptions);
        }

        public static STSqref parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STSqref) POIXMLTypeLoader.parse(xMLInputStream, STSqref.type, (XmlOptions) null);
        }

        public static STSqref parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STSqref) POIXMLTypeLoader.parse(xMLInputStream, STSqref.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSqref.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSqref.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List getListValue();

    List xgetListValue();

    void setListValue(List list);

    List listValue();

    List xlistValue();

    void set(List list);
}
