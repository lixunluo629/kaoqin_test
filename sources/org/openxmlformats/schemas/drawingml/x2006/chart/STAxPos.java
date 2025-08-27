package org.openxmlformats.schemas.drawingml.x2006.chart;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STAxPos.class */
public interface STAxPos extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STAxPos.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("staxpos4379type");
    public static final Enum B = Enum.forString("b");
    public static final Enum L = Enum.forString("l");
    public static final Enum R = Enum.forString(ExcelXmlConstants.POSITION);
    public static final Enum T = Enum.forString("t");
    public static final int INT_B = 1;
    public static final int INT_L = 2;
    public static final int INT_R = 3;
    public static final int INT_T = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STAxPos$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_B = 1;
        static final int INT_L = 2;
        static final int INT_R = 3;
        static final int INT_T = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("b", 1), new Enum("l", 2), new Enum(ExcelXmlConstants.POSITION, 3), new Enum("t", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STAxPos$Factory.class */
    public static final class Factory {
        public static STAxPos newValue(Object obj) {
            return (STAxPos) STAxPos.type.newValue(obj);
        }

        public static STAxPos newInstance() {
            return (STAxPos) POIXMLTypeLoader.newInstance(STAxPos.type, null);
        }

        public static STAxPos newInstance(XmlOptions xmlOptions) {
            return (STAxPos) POIXMLTypeLoader.newInstance(STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(String str) throws XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(str, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(str, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(File file) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(file, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(file, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(URL url) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(url, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(url, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(InputStream inputStream) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(inputStream, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(inputStream, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(Reader reader) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(reader, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAxPos) POIXMLTypeLoader.parse(reader, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(xMLStreamReader, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(xMLStreamReader, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(Node node) throws XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(node, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(node, STAxPos.type, xmlOptions);
        }

        public static STAxPos parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(xMLInputStream, STAxPos.type, (XmlOptions) null);
        }

        public static STAxPos parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STAxPos) POIXMLTypeLoader.parse(xMLInputStream, STAxPos.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAxPos.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAxPos.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
