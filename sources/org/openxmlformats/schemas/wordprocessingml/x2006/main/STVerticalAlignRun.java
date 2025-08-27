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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STVerticalAlignRun.class */
public interface STVerticalAlignRun extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STVerticalAlignRun.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stverticalalignrun297ctype");
    public static final Enum BASELINE = Enum.forString("baseline");
    public static final Enum SUPERSCRIPT = Enum.forString("superscript");
    public static final Enum SUBSCRIPT = Enum.forString("subscript");
    public static final int INT_BASELINE = 1;
    public static final int INT_SUPERSCRIPT = 2;
    public static final int INT_SUBSCRIPT = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STVerticalAlignRun$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_BASELINE = 1;
        static final int INT_SUPERSCRIPT = 2;
        static final int INT_SUBSCRIPT = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("baseline", 1), new Enum("superscript", 2), new Enum("subscript", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STVerticalAlignRun$Factory.class */
    public static final class Factory {
        public static STVerticalAlignRun newValue(Object obj) {
            return (STVerticalAlignRun) STVerticalAlignRun.type.newValue(obj);
        }

        public static STVerticalAlignRun newInstance() {
            return (STVerticalAlignRun) POIXMLTypeLoader.newInstance(STVerticalAlignRun.type, null);
        }

        public static STVerticalAlignRun newInstance(XmlOptions xmlOptions) {
            return (STVerticalAlignRun) POIXMLTypeLoader.newInstance(STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(String str) throws XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(str, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(str, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(File file) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(file, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(file, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(URL url) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(url, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(url, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(InputStream inputStream) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(inputStream, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(inputStream, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(Reader reader) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(reader, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(reader, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(xMLStreamReader, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(xMLStreamReader, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(Node node) throws XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(node, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(node, STVerticalAlignRun.type, xmlOptions);
        }

        public static STVerticalAlignRun parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(xMLInputStream, STVerticalAlignRun.type, (XmlOptions) null);
        }

        public static STVerticalAlignRun parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STVerticalAlignRun) POIXMLTypeLoader.parse(xMLInputStream, STVerticalAlignRun.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STVerticalAlignRun.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STVerticalAlignRun.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
