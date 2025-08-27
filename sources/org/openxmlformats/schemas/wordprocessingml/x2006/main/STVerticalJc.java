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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STVerticalJc.class */
public interface STVerticalJc extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STVerticalJc.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stverticaljc3629type");
    public static final Enum TOP = Enum.forString("top");
    public static final Enum CENTER = Enum.forString("center");
    public static final Enum BOTH = Enum.forString("both");
    public static final Enum BOTTOM = Enum.forString("bottom");
    public static final int INT_TOP = 1;
    public static final int INT_CENTER = 2;
    public static final int INT_BOTH = 3;
    public static final int INT_BOTTOM = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STVerticalJc$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_TOP = 1;
        static final int INT_CENTER = 2;
        static final int INT_BOTH = 3;
        static final int INT_BOTTOM = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("top", 1), new Enum("center", 2), new Enum("both", 3), new Enum("bottom", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STVerticalJc$Factory.class */
    public static final class Factory {
        public static STVerticalJc newValue(Object obj) {
            return (STVerticalJc) STVerticalJc.type.newValue(obj);
        }

        public static STVerticalJc newInstance() {
            return (STVerticalJc) POIXMLTypeLoader.newInstance(STVerticalJc.type, null);
        }

        public static STVerticalJc newInstance(XmlOptions xmlOptions) {
            return (STVerticalJc) POIXMLTypeLoader.newInstance(STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(String str) throws XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(str, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(str, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(File file) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(file, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(file, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(URL url) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(url, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(url, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(InputStream inputStream) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(inputStream, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(inputStream, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(Reader reader) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(reader, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalJc) POIXMLTypeLoader.parse(reader, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(xMLStreamReader, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(xMLStreamReader, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(Node node) throws XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(node, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(node, STVerticalJc.type, xmlOptions);
        }

        public static STVerticalJc parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(xMLInputStream, STVerticalJc.type, (XmlOptions) null);
        }

        public static STVerticalJc parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STVerticalJc) POIXMLTypeLoader.parse(xMLInputStream, STVerticalJc.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STVerticalJc.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STVerticalJc.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
