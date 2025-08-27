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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCalcMode.class */
public interface STCalcMode extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCalcMode.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcalcmode5e71type");
    public static final Enum MANUAL = Enum.forString("manual");
    public static final Enum AUTO = Enum.forString("auto");
    public static final Enum AUTO_NO_TABLE = Enum.forString("autoNoTable");
    public static final int INT_MANUAL = 1;
    public static final int INT_AUTO = 2;
    public static final int INT_AUTO_NO_TABLE = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCalcMode$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_MANUAL = 1;
        static final int INT_AUTO = 2;
        static final int INT_AUTO_NO_TABLE = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("manual", 1), new Enum("auto", 2), new Enum("autoNoTable", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCalcMode$Factory.class */
    public static final class Factory {
        public static STCalcMode newValue(Object obj) {
            return (STCalcMode) STCalcMode.type.newValue(obj);
        }

        public static STCalcMode newInstance() {
            return (STCalcMode) POIXMLTypeLoader.newInstance(STCalcMode.type, null);
        }

        public static STCalcMode newInstance(XmlOptions xmlOptions) {
            return (STCalcMode) POIXMLTypeLoader.newInstance(STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(String str) throws XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(str, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(str, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(File file) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(file, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(file, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(URL url) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(url, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(url, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(InputStream inputStream) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(inputStream, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(inputStream, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(Reader reader) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(reader, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCalcMode) POIXMLTypeLoader.parse(reader, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(xMLStreamReader, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(xMLStreamReader, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(Node node) throws XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(node, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(node, STCalcMode.type, xmlOptions);
        }

        public static STCalcMode parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(xMLInputStream, STCalcMode.type, (XmlOptions) null);
        }

        public static STCalcMode parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCalcMode) POIXMLTypeLoader.parse(xMLInputStream, STCalcMode.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCalcMode.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCalcMode.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
