package com.microsoft.schemas.office.x2006.encryption;

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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/STBlockSize.class */
public interface STBlockSize extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STBlockSize.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("stblocksize2e10type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/STBlockSize$Factory.class */
    public static final class Factory {
        public static STBlockSize newValue(Object obj) {
            return (STBlockSize) STBlockSize.type.newValue(obj);
        }

        public static STBlockSize newInstance() {
            return (STBlockSize) POIXMLTypeLoader.newInstance(STBlockSize.type, null);
        }

        public static STBlockSize newInstance(XmlOptions xmlOptions) {
            return (STBlockSize) POIXMLTypeLoader.newInstance(STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(String str) throws XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(str, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(str, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(File file) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(file, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(file, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(URL url) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(url, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(url, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(InputStream inputStream) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(inputStream, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(inputStream, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(Reader reader) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(reader, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBlockSize) POIXMLTypeLoader.parse(reader, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(xMLStreamReader, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(xMLStreamReader, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(Node node) throws XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(node, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(node, STBlockSize.type, xmlOptions);
        }

        public static STBlockSize parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(xMLInputStream, STBlockSize.type, (XmlOptions) null);
        }

        public static STBlockSize parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STBlockSize) POIXMLTypeLoader.parse(xMLInputStream, STBlockSize.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STBlockSize.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STBlockSize.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getIntValue();

    void setIntValue(int i);

    int intValue();

    void set(int i);
}
