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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STMarkerStyle.class */
public interface STMarkerStyle extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STMarkerStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stmarkerstyle177ftype");
    public static final Enum CIRCLE = Enum.forString("circle");
    public static final Enum DASH = Enum.forString("dash");
    public static final Enum DIAMOND = Enum.forString("diamond");
    public static final Enum DOT = Enum.forString("dot");
    public static final Enum NONE = Enum.forString("none");
    public static final Enum PICTURE = Enum.forString("picture");
    public static final Enum PLUS = Enum.forString("plus");
    public static final Enum SQUARE = Enum.forString("square");
    public static final Enum STAR = Enum.forString("star");
    public static final Enum TRIANGLE = Enum.forString("triangle");
    public static final Enum X = Enum.forString("x");
    public static final int INT_CIRCLE = 1;
    public static final int INT_DASH = 2;
    public static final int INT_DIAMOND = 3;
    public static final int INT_DOT = 4;
    public static final int INT_NONE = 5;
    public static final int INT_PICTURE = 6;
    public static final int INT_PLUS = 7;
    public static final int INT_SQUARE = 8;
    public static final int INT_STAR = 9;
    public static final int INT_TRIANGLE = 10;
    public static final int INT_X = 11;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STMarkerStyle$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_CIRCLE = 1;
        static final int INT_DASH = 2;
        static final int INT_DIAMOND = 3;
        static final int INT_DOT = 4;
        static final int INT_NONE = 5;
        static final int INT_PICTURE = 6;
        static final int INT_PLUS = 7;
        static final int INT_SQUARE = 8;
        static final int INT_STAR = 9;
        static final int INT_TRIANGLE = 10;
        static final int INT_X = 11;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("circle", 1), new Enum("dash", 2), new Enum("diamond", 3), new Enum("dot", 4), new Enum("none", 5), new Enum("picture", 6), new Enum("plus", 7), new Enum("square", 8), new Enum("star", 9), new Enum("triangle", 10), new Enum("x", 11)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STMarkerStyle$Factory.class */
    public static final class Factory {
        public static STMarkerStyle newValue(Object obj) {
            return (STMarkerStyle) STMarkerStyle.type.newValue(obj);
        }

        public static STMarkerStyle newInstance() {
            return (STMarkerStyle) POIXMLTypeLoader.newInstance(STMarkerStyle.type, null);
        }

        public static STMarkerStyle newInstance(XmlOptions xmlOptions) {
            return (STMarkerStyle) POIXMLTypeLoader.newInstance(STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(String str) throws XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(str, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(str, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(File file) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(file, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(file, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(URL url) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(url, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(url, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(inputStream, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(inputStream, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(Reader reader) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(reader, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(reader, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(xMLStreamReader, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(xMLStreamReader, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(Node node) throws XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(node, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(node, STMarkerStyle.type, xmlOptions);
        }

        public static STMarkerStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(xMLInputStream, STMarkerStyle.type, (XmlOptions) null);
        }

        public static STMarkerStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STMarkerStyle) POIXMLTypeLoader.parse(xMLInputStream, STMarkerStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STMarkerStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STMarkerStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
