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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STTabTlc.class */
public interface STTabTlc extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTabTlc.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttabtlc6f42type");
    public static final Enum NONE = Enum.forString("none");
    public static final Enum DOT = Enum.forString("dot");
    public static final Enum HYPHEN = Enum.forString("hyphen");
    public static final Enum UNDERSCORE = Enum.forString("underscore");
    public static final Enum HEAVY = Enum.forString("heavy");
    public static final Enum MIDDLE_DOT = Enum.forString("middleDot");
    public static final int INT_NONE = 1;
    public static final int INT_DOT = 2;
    public static final int INT_HYPHEN = 3;
    public static final int INT_UNDERSCORE = 4;
    public static final int INT_HEAVY = 5;
    public static final int INT_MIDDLE_DOT = 6;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STTabTlc$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NONE = 1;
        static final int INT_DOT = 2;
        static final int INT_HYPHEN = 3;
        static final int INT_UNDERSCORE = 4;
        static final int INT_HEAVY = 5;
        static final int INT_MIDDLE_DOT = 6;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("none", 1), new Enum("dot", 2), new Enum("hyphen", 3), new Enum("underscore", 4), new Enum("heavy", 5), new Enum("middleDot", 6)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STTabTlc$Factory.class */
    public static final class Factory {
        public static STTabTlc newValue(Object obj) {
            return (STTabTlc) STTabTlc.type.newValue(obj);
        }

        public static STTabTlc newInstance() {
            return (STTabTlc) POIXMLTypeLoader.newInstance(STTabTlc.type, null);
        }

        public static STTabTlc newInstance(XmlOptions xmlOptions) {
            return (STTabTlc) POIXMLTypeLoader.newInstance(STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(String str) throws XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(str, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(str, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(File file) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(file, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(file, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(URL url) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(url, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(url, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(InputStream inputStream) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(inputStream, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(inputStream, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(Reader reader) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(reader, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTabTlc) POIXMLTypeLoader.parse(reader, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(xMLStreamReader, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(xMLStreamReader, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(Node node) throws XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(node, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(node, STTabTlc.type, xmlOptions);
        }

        public static STTabTlc parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(xMLInputStream, STTabTlc.type, (XmlOptions) null);
        }

        public static STTabTlc parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTabTlc) POIXMLTypeLoader.parse(xMLInputStream, STTabTlc.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTabTlc.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTabTlc.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
