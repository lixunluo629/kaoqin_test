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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextHorzOverflowType.class */
public interface STTextHorzOverflowType extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextHorzOverflowType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttexthorzoverflowtype6003type");
    public static final Enum OVERFLOW = Enum.forString("overflow");
    public static final Enum CLIP = Enum.forString("clip");
    public static final int INT_OVERFLOW = 1;
    public static final int INT_CLIP = 2;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextHorzOverflowType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_OVERFLOW = 1;
        static final int INT_CLIP = 2;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("overflow", 1), new Enum("clip", 2)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextHorzOverflowType$Factory.class */
    public static final class Factory {
        public static STTextHorzOverflowType newValue(Object obj) {
            return (STTextHorzOverflowType) STTextHorzOverflowType.type.newValue(obj);
        }

        public static STTextHorzOverflowType newInstance() {
            return (STTextHorzOverflowType) POIXMLTypeLoader.newInstance(STTextHorzOverflowType.type, null);
        }

        public static STTextHorzOverflowType newInstance(XmlOptions xmlOptions) {
            return (STTextHorzOverflowType) POIXMLTypeLoader.newInstance(STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(String str) throws XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(str, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(str, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(File file) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(file, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(file, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(URL url) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(url, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(url, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(inputStream, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(inputStream, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(Reader reader) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(reader, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(reader, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(xMLStreamReader, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(xMLStreamReader, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(Node node) throws XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(node, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(node, STTextHorzOverflowType.type, xmlOptions);
        }

        public static STTextHorzOverflowType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(xMLInputStream, STTextHorzOverflowType.type, (XmlOptions) null);
        }

        public static STTextHorzOverflowType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextHorzOverflowType) POIXMLTypeLoader.parse(xMLInputStream, STTextHorzOverflowType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextHorzOverflowType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextHorzOverflowType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
