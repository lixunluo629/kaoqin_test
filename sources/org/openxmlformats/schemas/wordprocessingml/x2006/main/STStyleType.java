package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STStyleType.class */
public interface STStyleType extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STStyleType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ststyletypec2b7type");
    public static final Enum PARAGRAPH = Enum.forString("paragraph");
    public static final Enum CHARACTER = Enum.forString("character");
    public static final Enum TABLE = Enum.forString("table");
    public static final Enum NUMBERING = Enum.forString("numbering");
    public static final int INT_PARAGRAPH = 1;
    public static final int INT_CHARACTER = 2;
    public static final int INT_TABLE = 3;
    public static final int INT_NUMBERING = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STStyleType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_PARAGRAPH = 1;
        static final int INT_CHARACTER = 2;
        static final int INT_TABLE = 3;
        static final int INT_NUMBERING = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("paragraph", 1), new Enum("character", 2), new Enum("table", 3), new Enum("numbering", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STStyleType$Factory.class */
    public static final class Factory {
        public static STStyleType newValue(Object obj) {
            return (STStyleType) STStyleType.type.newValue(obj);
        }

        public static STStyleType newInstance() {
            return (STStyleType) POIXMLTypeLoader.newInstance(STStyleType.type, null);
        }

        public static STStyleType newInstance(XmlOptions xmlOptions) {
            return (STStyleType) POIXMLTypeLoader.newInstance(STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(String str) throws XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(str, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(str, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(File file) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(file, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(file, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(URL url) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(url, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(url, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(InputStream inputStream) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(inputStream, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(inputStream, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(Reader reader) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(reader, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStyleType) POIXMLTypeLoader.parse(reader, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(xMLStreamReader, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(xMLStreamReader, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(Node node) throws XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(node, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(node, STStyleType.type, xmlOptions);
        }

        public static STStyleType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(xMLInputStream, STStyleType.type, (XmlOptions) null);
        }

        public static STStyleType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STStyleType) POIXMLTypeLoader.parse(xMLInputStream, STStyleType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STStyleType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STStyleType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
