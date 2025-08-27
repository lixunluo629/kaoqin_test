package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import com.alibaba.excel.constant.ExcelXmlConstants;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellType.class */
public interface STCellType extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCellType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcelltypebf95type");
    public static final Enum B = Enum.forString("b");
    public static final Enum N = Enum.forString("n");
    public static final Enum E = Enum.forString("e");
    public static final Enum S = Enum.forString(ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
    public static final Enum STR = Enum.forString("str");
    public static final Enum INLINE_STR = Enum.forString("inlineStr");
    public static final int INT_B = 1;
    public static final int INT_N = 2;
    public static final int INT_E = 3;
    public static final int INT_S = 4;
    public static final int INT_STR = 5;
    public static final int INT_INLINE_STR = 6;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_B = 1;
        static final int INT_N = 2;
        static final int INT_E = 3;
        static final int INT_S = 4;
        static final int INT_STR = 5;
        static final int INT_INLINE_STR = 6;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("b", 1), new Enum("n", 2), new Enum("e", 3), new Enum(ExcelXmlConstants.CELL_DATA_FORMAT_TAG, 4), new Enum("str", 5), new Enum("inlineStr", 6)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellType$Factory.class */
    public static final class Factory {
        public static STCellType newValue(Object obj) {
            return (STCellType) STCellType.type.newValue(obj);
        }

        public static STCellType newInstance() {
            return (STCellType) POIXMLTypeLoader.newInstance(STCellType.type, null);
        }

        public static STCellType newInstance(XmlOptions xmlOptions) {
            return (STCellType) POIXMLTypeLoader.newInstance(STCellType.type, xmlOptions);
        }

        public static STCellType parse(String str) throws XmlException {
            return (STCellType) POIXMLTypeLoader.parse(str, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCellType) POIXMLTypeLoader.parse(str, STCellType.type, xmlOptions);
        }

        public static STCellType parse(File file) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(file, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(file, STCellType.type, xmlOptions);
        }

        public static STCellType parse(URL url) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(url, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(url, STCellType.type, xmlOptions);
        }

        public static STCellType parse(InputStream inputStream) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(inputStream, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(inputStream, STCellType.type, xmlOptions);
        }

        public static STCellType parse(Reader reader) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(reader, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellType) POIXMLTypeLoader.parse(reader, STCellType.type, xmlOptions);
        }

        public static STCellType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCellType) POIXMLTypeLoader.parse(xMLStreamReader, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCellType) POIXMLTypeLoader.parse(xMLStreamReader, STCellType.type, xmlOptions);
        }

        public static STCellType parse(Node node) throws XmlException {
            return (STCellType) POIXMLTypeLoader.parse(node, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCellType) POIXMLTypeLoader.parse(node, STCellType.type, xmlOptions);
        }

        public static STCellType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCellType) POIXMLTypeLoader.parse(xMLInputStream, STCellType.type, (XmlOptions) null);
        }

        public static STCellType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCellType) POIXMLTypeLoader.parse(xMLInputStream, STCellType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCellType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCellType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
