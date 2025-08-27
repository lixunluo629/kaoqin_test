package org.openxmlformats.schemas.drawingml.x2006.chart;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STCrosses.class */
public interface STCrosses extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCrosses.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcrosses3cc8type");
    public static final Enum AUTO_ZERO = Enum.forString("autoZero");
    public static final Enum MAX = Enum.forString("max");
    public static final Enum MIN = Enum.forString("min");
    public static final int INT_AUTO_ZERO = 1;
    public static final int INT_MAX = 2;
    public static final int INT_MIN = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STCrosses$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_AUTO_ZERO = 1;
        static final int INT_MAX = 2;
        static final int INT_MIN = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("autoZero", 1), new Enum("max", 2), new Enum("min", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STCrosses$Factory.class */
    public static final class Factory {
        public static STCrosses newValue(Object obj) {
            return (STCrosses) STCrosses.type.newValue(obj);
        }

        public static STCrosses newInstance() {
            return (STCrosses) POIXMLTypeLoader.newInstance(STCrosses.type, null);
        }

        public static STCrosses newInstance(XmlOptions xmlOptions) {
            return (STCrosses) POIXMLTypeLoader.newInstance(STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(String str) throws XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(str, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(str, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(File file) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(file, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(file, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(URL url) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(url, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(url, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(InputStream inputStream) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(inputStream, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(inputStream, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(Reader reader) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(reader, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCrosses) POIXMLTypeLoader.parse(reader, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(xMLStreamReader, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(xMLStreamReader, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(Node node) throws XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(node, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(node, STCrosses.type, xmlOptions);
        }

        public static STCrosses parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(xMLInputStream, STCrosses.type, (XmlOptions) null);
        }

        public static STCrosses parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCrosses) POIXMLTypeLoader.parse(xMLInputStream, STCrosses.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCrosses.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCrosses.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
