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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineEndType.class */
public interface STLineEndType extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLineEndType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlineendtype8902type");
    public static final Enum NONE = Enum.forString("none");
    public static final Enum TRIANGLE = Enum.forString("triangle");
    public static final Enum STEALTH = Enum.forString("stealth");
    public static final Enum DIAMOND = Enum.forString("diamond");
    public static final Enum OVAL = Enum.forString("oval");
    public static final Enum ARROW = Enum.forString("arrow");
    public static final int INT_NONE = 1;
    public static final int INT_TRIANGLE = 2;
    public static final int INT_STEALTH = 3;
    public static final int INT_DIAMOND = 4;
    public static final int INT_OVAL = 5;
    public static final int INT_ARROW = 6;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineEndType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NONE = 1;
        static final int INT_TRIANGLE = 2;
        static final int INT_STEALTH = 3;
        static final int INT_DIAMOND = 4;
        static final int INT_OVAL = 5;
        static final int INT_ARROW = 6;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("none", 1), new Enum("triangle", 2), new Enum("stealth", 3), new Enum("diamond", 4), new Enum("oval", 5), new Enum("arrow", 6)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineEndType$Factory.class */
    public static final class Factory {
        public static STLineEndType newValue(Object obj) {
            return (STLineEndType) STLineEndType.type.newValue(obj);
        }

        public static STLineEndType newInstance() {
            return (STLineEndType) POIXMLTypeLoader.newInstance(STLineEndType.type, null);
        }

        public static STLineEndType newInstance(XmlOptions xmlOptions) {
            return (STLineEndType) POIXMLTypeLoader.newInstance(STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(String str) throws XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(str, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(str, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(File file) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(file, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(file, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(URL url) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(url, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(url, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(InputStream inputStream) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(inputStream, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(inputStream, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(Reader reader) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(reader, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndType) POIXMLTypeLoader.parse(reader, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(xMLStreamReader, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(xMLStreamReader, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(Node node) throws XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(node, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(node, STLineEndType.type, xmlOptions);
        }

        public static STLineEndType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(xMLInputStream, STLineEndType.type, (XmlOptions) null);
        }

        public static STLineEndType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLineEndType) POIXMLTypeLoader.parse(xMLInputStream, STLineEndType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineEndType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineEndType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
