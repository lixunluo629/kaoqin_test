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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STAxis.class */
public interface STAxis extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STAxis.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("staxis45batype");
    public static final Enum AXIS_ROW = Enum.forString("axisRow");
    public static final Enum AXIS_COL = Enum.forString("axisCol");
    public static final Enum AXIS_PAGE = Enum.forString("axisPage");
    public static final Enum AXIS_VALUES = Enum.forString("axisValues");
    public static final int INT_AXIS_ROW = 1;
    public static final int INT_AXIS_COL = 2;
    public static final int INT_AXIS_PAGE = 3;
    public static final int INT_AXIS_VALUES = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STAxis$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_AXIS_ROW = 1;
        static final int INT_AXIS_COL = 2;
        static final int INT_AXIS_PAGE = 3;
        static final int INT_AXIS_VALUES = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("axisRow", 1), new Enum("axisCol", 2), new Enum("axisPage", 3), new Enum("axisValues", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STAxis$Factory.class */
    public static final class Factory {
        public static STAxis newValue(Object obj) {
            return (STAxis) STAxis.type.newValue(obj);
        }

        public static STAxis newInstance() {
            return (STAxis) POIXMLTypeLoader.newInstance(STAxis.type, null);
        }

        public static STAxis newInstance(XmlOptions xmlOptions) {
            return (STAxis) POIXMLTypeLoader.newInstance(STAxis.type, xmlOptions);
        }

        public static STAxis parse(String str) throws XmlException {
            return (STAxis) POIXMLTypeLoader.parse(str, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STAxis) POIXMLTypeLoader.parse(str, STAxis.type, xmlOptions);
        }

        public static STAxis parse(File file) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(file, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(file, STAxis.type, xmlOptions);
        }

        public static STAxis parse(URL url) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(url, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(url, STAxis.type, xmlOptions);
        }

        public static STAxis parse(InputStream inputStream) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(inputStream, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(inputStream, STAxis.type, xmlOptions);
        }

        public static STAxis parse(Reader reader) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(reader, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxis) POIXMLTypeLoader.parse(reader, STAxis.type, xmlOptions);
        }

        public static STAxis parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STAxis) POIXMLTypeLoader.parse(xMLStreamReader, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STAxis) POIXMLTypeLoader.parse(xMLStreamReader, STAxis.type, xmlOptions);
        }

        public static STAxis parse(Node node) throws XmlException {
            return (STAxis) POIXMLTypeLoader.parse(node, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STAxis) POIXMLTypeLoader.parse(node, STAxis.type, xmlOptions);
        }

        public static STAxis parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STAxis) POIXMLTypeLoader.parse(xMLInputStream, STAxis.type, (XmlOptions) null);
        }

        public static STAxis parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STAxis) POIXMLTypeLoader.parse(xMLInputStream, STAxis.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAxis.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAxis.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
