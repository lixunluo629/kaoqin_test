package com.microsoft.schemas.office.office;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STConnectType.class */
public interface STConnectType extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STConnectType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stconnecttype97adtype");
    public static final Enum NONE = Enum.forString("none");
    public static final Enum RECT = Enum.forString("rect");
    public static final Enum SEGMENTS = Enum.forString("segments");
    public static final Enum CUSTOM = Enum.forString("custom");
    public static final int INT_NONE = 1;
    public static final int INT_RECT = 2;
    public static final int INT_SEGMENTS = 3;
    public static final int INT_CUSTOM = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STConnectType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NONE = 1;
        static final int INT_RECT = 2;
        static final int INT_SEGMENTS = 3;
        static final int INT_CUSTOM = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("none", 1), new Enum("rect", 2), new Enum("segments", 3), new Enum("custom", 4)});
        private static final long serialVersionUID = 1;

        public static Enum forString(String str) {
            return (Enum) table.forString(str);
        }

        public static Enum forInt(int i) {
            return (Enum) table.forInt(i);
        }

        private Enum(String str, int i) {
            super(str, i);
        }

        private Object readResolve() {
            return forInt(intValue());
        }
    }

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STConnectType$Factory.class */
    public static final class Factory {
        public static STConnectType newValue(Object obj) {
            return (STConnectType) STConnectType.type.newValue(obj);
        }

        public static STConnectType newInstance() {
            return (STConnectType) POIXMLTypeLoader.newInstance(STConnectType.type, null);
        }

        public static STConnectType newInstance(XmlOptions xmlOptions) {
            return (STConnectType) POIXMLTypeLoader.newInstance(STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(String str) throws XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(str, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(str, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(File file) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(file, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(file, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(URL url) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(url, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(url, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(InputStream inputStream) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(inputStream, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(inputStream, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(Reader reader) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(reader, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STConnectType) POIXMLTypeLoader.parse(reader, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(xMLStreamReader, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(xMLStreamReader, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(Node node) throws XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(node, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(node, STConnectType.type, xmlOptions);
        }

        public static STConnectType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(xMLInputStream, STConnectType.type, (XmlOptions) null);
        }

        public static STConnectType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STConnectType) POIXMLTypeLoader.parse(xMLInputStream, STConnectType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STConnectType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STConnectType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
