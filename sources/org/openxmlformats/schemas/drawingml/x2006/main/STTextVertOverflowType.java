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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextVertOverflowType.class */
public interface STTextVertOverflowType extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextVertOverflowType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextvertoverflowtype2725type");
    public static final Enum OVERFLOW = Enum.forString("overflow");
    public static final Enum ELLIPSIS = Enum.forString("ellipsis");
    public static final Enum CLIP = Enum.forString("clip");
    public static final int INT_OVERFLOW = 1;
    public static final int INT_ELLIPSIS = 2;
    public static final int INT_CLIP = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextVertOverflowType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_OVERFLOW = 1;
        static final int INT_ELLIPSIS = 2;
        static final int INT_CLIP = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("overflow", 1), new Enum("ellipsis", 2), new Enum("clip", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextVertOverflowType$Factory.class */
    public static final class Factory {
        public static STTextVertOverflowType newValue(Object obj) {
            return (STTextVertOverflowType) STTextVertOverflowType.type.newValue(obj);
        }

        public static STTextVertOverflowType newInstance() {
            return (STTextVertOverflowType) POIXMLTypeLoader.newInstance(STTextVertOverflowType.type, null);
        }

        public static STTextVertOverflowType newInstance(XmlOptions xmlOptions) {
            return (STTextVertOverflowType) POIXMLTypeLoader.newInstance(STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(String str) throws XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(str, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(str, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(File file) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(file, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(file, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(URL url) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(url, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(url, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(inputStream, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(inputStream, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(Reader reader) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(reader, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(reader, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(xMLStreamReader, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(xMLStreamReader, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(Node node) throws XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(node, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(node, STTextVertOverflowType.type, xmlOptions);
        }

        public static STTextVertOverflowType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(xMLInputStream, STTextVertOverflowType.type, (XmlOptions) null);
        }

        public static STTextVertOverflowType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextVertOverflowType) POIXMLTypeLoader.parse(xMLInputStream, STTextVertOverflowType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextVertOverflowType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextVertOverflowType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
