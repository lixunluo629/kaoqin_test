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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLineSpacingRule.class */
public interface STLineSpacingRule extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLineSpacingRule.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlinespacingrule6237type");
    public static final Enum AUTO = Enum.forString("auto");
    public static final Enum EXACT = Enum.forString("exact");
    public static final Enum AT_LEAST = Enum.forString("atLeast");
    public static final int INT_AUTO = 1;
    public static final int INT_EXACT = 2;
    public static final int INT_AT_LEAST = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLineSpacingRule$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_AUTO = 1;
        static final int INT_EXACT = 2;
        static final int INT_AT_LEAST = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("auto", 1), new Enum("exact", 2), new Enum("atLeast", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLineSpacingRule$Factory.class */
    public static final class Factory {
        public static STLineSpacingRule newValue(Object obj) {
            return (STLineSpacingRule) STLineSpacingRule.type.newValue(obj);
        }

        public static STLineSpacingRule newInstance() {
            return (STLineSpacingRule) POIXMLTypeLoader.newInstance(STLineSpacingRule.type, null);
        }

        public static STLineSpacingRule newInstance(XmlOptions xmlOptions) {
            return (STLineSpacingRule) POIXMLTypeLoader.newInstance(STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(String str) throws XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(str, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(str, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(File file) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(file, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(file, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(URL url) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(url, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(url, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(InputStream inputStream) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(inputStream, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(inputStream, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(Reader reader) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(reader, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(reader, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(xMLStreamReader, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(xMLStreamReader, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(Node node) throws XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(node, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(node, STLineSpacingRule.type, xmlOptions);
        }

        public static STLineSpacingRule parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(xMLInputStream, STLineSpacingRule.type, (XmlOptions) null);
        }

        public static STLineSpacingRule parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLineSpacingRule) POIXMLTypeLoader.parse(xMLInputStream, STLineSpacingRule.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineSpacingRule.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineSpacingRule.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
