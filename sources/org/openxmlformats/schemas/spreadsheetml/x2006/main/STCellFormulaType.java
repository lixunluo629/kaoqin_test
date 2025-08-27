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
import org.aspectj.weaver.tools.cache.SimpleCache;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellFormulaType.class */
public interface STCellFormulaType extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCellFormulaType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcellformulatypee2cdtype");
    public static final Enum NORMAL = Enum.forString("normal");
    public static final Enum ARRAY = Enum.forString("array");
    public static final Enum DATA_TABLE = Enum.forString("dataTable");
    public static final Enum SHARED = Enum.forString(SimpleCache.IMPL_NAME);
    public static final int INT_NORMAL = 1;
    public static final int INT_ARRAY = 2;
    public static final int INT_DATA_TABLE = 3;
    public static final int INT_SHARED = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellFormulaType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NORMAL = 1;
        static final int INT_ARRAY = 2;
        static final int INT_DATA_TABLE = 3;
        static final int INT_SHARED = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("normal", 1), new Enum("array", 2), new Enum("dataTable", 3), new Enum(SimpleCache.IMPL_NAME, 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellFormulaType$Factory.class */
    public static final class Factory {
        public static STCellFormulaType newValue(Object obj) {
            return (STCellFormulaType) STCellFormulaType.type.newValue(obj);
        }

        public static STCellFormulaType newInstance() {
            return (STCellFormulaType) POIXMLTypeLoader.newInstance(STCellFormulaType.type, null);
        }

        public static STCellFormulaType newInstance(XmlOptions xmlOptions) {
            return (STCellFormulaType) POIXMLTypeLoader.newInstance(STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(String str) throws XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(str, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(str, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(File file) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(file, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(file, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(URL url) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(url, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(url, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(InputStream inputStream) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(inputStream, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(inputStream, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(Reader reader) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(reader, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(reader, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(xMLStreamReader, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(xMLStreamReader, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(Node node) throws XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(node, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(node, STCellFormulaType.type, xmlOptions);
        }

        public static STCellFormulaType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(xMLInputStream, STCellFormulaType.type, (XmlOptions) null);
        }

        public static STCellFormulaType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCellFormulaType) POIXMLTypeLoader.parse(xMLInputStream, STCellFormulaType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCellFormulaType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCellFormulaType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
