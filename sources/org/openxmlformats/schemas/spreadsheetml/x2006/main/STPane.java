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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPane.class */
public interface STPane extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STPane.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stpane2ac1type");
    public static final Enum BOTTOM_RIGHT = Enum.forString("bottomRight");
    public static final Enum TOP_RIGHT = Enum.forString("topRight");
    public static final Enum BOTTOM_LEFT = Enum.forString("bottomLeft");
    public static final Enum TOP_LEFT = Enum.forString("topLeft");
    public static final int INT_BOTTOM_RIGHT = 1;
    public static final int INT_TOP_RIGHT = 2;
    public static final int INT_BOTTOM_LEFT = 3;
    public static final int INT_TOP_LEFT = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPane$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_BOTTOM_RIGHT = 1;
        static final int INT_TOP_RIGHT = 2;
        static final int INT_BOTTOM_LEFT = 3;
        static final int INT_TOP_LEFT = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("bottomRight", 1), new Enum("topRight", 2), new Enum("bottomLeft", 3), new Enum("topLeft", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPane$Factory.class */
    public static final class Factory {
        public static STPane newValue(Object obj) {
            return (STPane) STPane.type.newValue(obj);
        }

        public static STPane newInstance() {
            return (STPane) POIXMLTypeLoader.newInstance(STPane.type, null);
        }

        public static STPane newInstance(XmlOptions xmlOptions) {
            return (STPane) POIXMLTypeLoader.newInstance(STPane.type, xmlOptions);
        }

        public static STPane parse(String str) throws XmlException {
            return (STPane) POIXMLTypeLoader.parse(str, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STPane) POIXMLTypeLoader.parse(str, STPane.type, xmlOptions);
        }

        public static STPane parse(File file) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(file, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(file, STPane.type, xmlOptions);
        }

        public static STPane parse(URL url) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(url, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(url, STPane.type, xmlOptions);
        }

        public static STPane parse(InputStream inputStream) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(inputStream, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(inputStream, STPane.type, xmlOptions);
        }

        public static STPane parse(Reader reader) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(reader, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPane) POIXMLTypeLoader.parse(reader, STPane.type, xmlOptions);
        }

        public static STPane parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STPane) POIXMLTypeLoader.parse(xMLStreamReader, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STPane) POIXMLTypeLoader.parse(xMLStreamReader, STPane.type, xmlOptions);
        }

        public static STPane parse(Node node) throws XmlException {
            return (STPane) POIXMLTypeLoader.parse(node, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STPane) POIXMLTypeLoader.parse(node, STPane.type, xmlOptions);
        }

        public static STPane parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STPane) POIXMLTypeLoader.parse(xMLInputStream, STPane.type, (XmlOptions) null);
        }

        public static STPane parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STPane) POIXMLTypeLoader.parse(xMLInputStream, STPane.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPane.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPane.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
