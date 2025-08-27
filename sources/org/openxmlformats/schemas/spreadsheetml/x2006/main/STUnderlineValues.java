package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STUnderlineValues.class */
public interface STUnderlineValues extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STUnderlineValues.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stunderlinevaluesb6ddtype");
    public static final Enum SINGLE = Enum.forString("single");
    public static final Enum DOUBLE = Enum.forString(XmlErrorCodes.DOUBLE);
    public static final Enum SINGLE_ACCOUNTING = Enum.forString("singleAccounting");
    public static final Enum DOUBLE_ACCOUNTING = Enum.forString("doubleAccounting");
    public static final Enum NONE = Enum.forString("none");
    public static final int INT_SINGLE = 1;
    public static final int INT_DOUBLE = 2;
    public static final int INT_SINGLE_ACCOUNTING = 3;
    public static final int INT_DOUBLE_ACCOUNTING = 4;
    public static final int INT_NONE = 5;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STUnderlineValues$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_SINGLE = 1;
        static final int INT_DOUBLE = 2;
        static final int INT_SINGLE_ACCOUNTING = 3;
        static final int INT_DOUBLE_ACCOUNTING = 4;
        static final int INT_NONE = 5;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("single", 1), new Enum(XmlErrorCodes.DOUBLE, 2), new Enum("singleAccounting", 3), new Enum("doubleAccounting", 4), new Enum("none", 5)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STUnderlineValues$Factory.class */
    public static final class Factory {
        public static STUnderlineValues newValue(Object obj) {
            return (STUnderlineValues) STUnderlineValues.type.newValue(obj);
        }

        public static STUnderlineValues newInstance() {
            return (STUnderlineValues) POIXMLTypeLoader.newInstance(STUnderlineValues.type, null);
        }

        public static STUnderlineValues newInstance(XmlOptions xmlOptions) {
            return (STUnderlineValues) POIXMLTypeLoader.newInstance(STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(String str) throws XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(str, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(str, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(File file) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(file, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(file, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(URL url) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(url, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(url, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(InputStream inputStream) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(inputStream, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(inputStream, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(Reader reader) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(reader, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(reader, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(xMLStreamReader, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(xMLStreamReader, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(Node node) throws XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(node, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(node, STUnderlineValues.type, xmlOptions);
        }

        public static STUnderlineValues parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(xMLInputStream, STUnderlineValues.type, (XmlOptions) null);
        }

        public static STUnderlineValues parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STUnderlineValues) POIXMLTypeLoader.parse(xMLInputStream, STUnderlineValues.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STUnderlineValues.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STUnderlineValues.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
