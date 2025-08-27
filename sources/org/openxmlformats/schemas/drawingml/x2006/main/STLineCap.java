package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineCap.class */
public interface STLineCap extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLineCap.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlinecapcddftype");
    public static final Enum RND = Enum.forString("rnd");
    public static final Enum SQ = Enum.forString("sq");
    public static final Enum FLAT = Enum.forString("flat");
    public static final int INT_RND = 1;
    public static final int INT_SQ = 2;
    public static final int INT_FLAT = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineCap$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_RND = 1;
        static final int INT_SQ = 2;
        static final int INT_FLAT = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("rnd", 1), new Enum("sq", 2), new Enum("flat", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineCap$Factory.class */
    public static final class Factory {
        public static STLineCap newValue(Object obj) {
            return (STLineCap) STLineCap.type.newValue(obj);
        }

        public static STLineCap newInstance() {
            return (STLineCap) POIXMLTypeLoader.newInstance(STLineCap.type, null);
        }

        public static STLineCap newInstance(XmlOptions xmlOptions) {
            return (STLineCap) POIXMLTypeLoader.newInstance(STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(String str) throws XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(str, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(str, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(File file) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(file, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(file, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(URL url) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(url, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(url, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(InputStream inputStream) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(inputStream, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(inputStream, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(Reader reader) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(reader, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineCap) POIXMLTypeLoader.parse(reader, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(xMLStreamReader, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(xMLStreamReader, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(Node node) throws XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(node, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(node, STLineCap.type, xmlOptions);
        }

        public static STLineCap parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(xMLInputStream, STLineCap.type, (XmlOptions) null);
        }

        public static STLineCap parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLineCap) POIXMLTypeLoader.parse(xMLInputStream, STLineCap.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineCap.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineCap.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
