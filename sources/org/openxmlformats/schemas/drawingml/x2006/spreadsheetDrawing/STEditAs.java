package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/STEditAs.class */
public interface STEditAs extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STEditAs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("steditasad40type");
    public static final Enum TWO_CELL = Enum.forString("twoCell");
    public static final Enum ONE_CELL = Enum.forString("oneCell");
    public static final Enum ABSOLUTE = Enum.forString("absolute");
    public static final int INT_TWO_CELL = 1;
    public static final int INT_ONE_CELL = 2;
    public static final int INT_ABSOLUTE = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/STEditAs$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_TWO_CELL = 1;
        static final int INT_ONE_CELL = 2;
        static final int INT_ABSOLUTE = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("twoCell", 1), new Enum("oneCell", 2), new Enum("absolute", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/STEditAs$Factory.class */
    public static final class Factory {
        public static STEditAs newValue(Object obj) {
            return (STEditAs) STEditAs.type.newValue(obj);
        }

        public static STEditAs newInstance() {
            return (STEditAs) POIXMLTypeLoader.newInstance(STEditAs.type, null);
        }

        public static STEditAs newInstance(XmlOptions xmlOptions) {
            return (STEditAs) POIXMLTypeLoader.newInstance(STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(String str) throws XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(str, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(str, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(File file) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(file, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(file, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(URL url) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(url, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(url, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(InputStream inputStream) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(inputStream, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(inputStream, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(Reader reader) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(reader, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEditAs) POIXMLTypeLoader.parse(reader, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(xMLStreamReader, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(xMLStreamReader, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(Node node) throws XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(node, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(node, STEditAs.type, xmlOptions);
        }

        public static STEditAs parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(xMLInputStream, STEditAs.type, (XmlOptions) null);
        }

        public static STEditAs parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STEditAs) POIXMLTypeLoader.parse(xMLInputStream, STEditAs.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STEditAs.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STEditAs.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
