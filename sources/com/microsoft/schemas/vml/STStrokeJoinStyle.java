package com.microsoft.schemas.vml;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STStrokeJoinStyle.class */
public interface STStrokeJoinStyle extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STStrokeJoinStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ststrokejoinstyle3c13type");
    public static final Enum ROUND = Enum.forString("round");
    public static final Enum BEVEL = Enum.forString("bevel");
    public static final Enum MITER = Enum.forString("miter");
    public static final int INT_ROUND = 1;
    public static final int INT_BEVEL = 2;
    public static final int INT_MITER = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STStrokeJoinStyle$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_ROUND = 1;
        static final int INT_BEVEL = 2;
        static final int INT_MITER = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("round", 1), new Enum("bevel", 2), new Enum("miter", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STStrokeJoinStyle$Factory.class */
    public static final class Factory {
        public static STStrokeJoinStyle newValue(Object obj) {
            return (STStrokeJoinStyle) STStrokeJoinStyle.type.newValue(obj);
        }

        public static STStrokeJoinStyle newInstance() {
            return (STStrokeJoinStyle) POIXMLTypeLoader.newInstance(STStrokeJoinStyle.type, null);
        }

        public static STStrokeJoinStyle newInstance(XmlOptions xmlOptions) {
            return (STStrokeJoinStyle) POIXMLTypeLoader.newInstance(STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(String str) throws XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(str, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(str, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(File file) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(file, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(file, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(URL url) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(url, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(url, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(inputStream, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(inputStream, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(Reader reader) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(reader, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(reader, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(xMLStreamReader, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(xMLStreamReader, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(Node node) throws XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(node, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(node, STStrokeJoinStyle.type, xmlOptions);
        }

        public static STStrokeJoinStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(xMLInputStream, STStrokeJoinStyle.type, (XmlOptions) null);
        }

        public static STStrokeJoinStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STStrokeJoinStyle) POIXMLTypeLoader.parse(xMLInputStream, STStrokeJoinStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STStrokeJoinStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STStrokeJoinStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
