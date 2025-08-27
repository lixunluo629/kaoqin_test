package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STTblWidth.class */
public interface STTblWidth extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTblWidth.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttblwidth3a30type");
    public static final Enum NIL = Enum.forString("nil");
    public static final Enum PCT = Enum.forString("pct");
    public static final Enum DXA = Enum.forString("dxa");
    public static final Enum AUTO = Enum.forString("auto");
    public static final int INT_NIL = 1;
    public static final int INT_PCT = 2;
    public static final int INT_DXA = 3;
    public static final int INT_AUTO = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STTblWidth$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NIL = 1;
        static final int INT_PCT = 2;
        static final int INT_DXA = 3;
        static final int INT_AUTO = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("nil", 1), new Enum("pct", 2), new Enum("dxa", 3), new Enum("auto", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STTblWidth$Factory.class */
    public static final class Factory {
        public static STTblWidth newValue(Object obj) {
            return (STTblWidth) STTblWidth.type.newValue(obj);
        }

        public static STTblWidth newInstance() {
            return (STTblWidth) POIXMLTypeLoader.newInstance(STTblWidth.type, null);
        }

        public static STTblWidth newInstance(XmlOptions xmlOptions) {
            return (STTblWidth) POIXMLTypeLoader.newInstance(STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(String str) throws XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(str, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(str, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(File file) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(file, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(file, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(URL url) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(url, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(url, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(InputStream inputStream) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(inputStream, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(inputStream, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(Reader reader) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(reader, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTblWidth) POIXMLTypeLoader.parse(reader, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(xMLStreamReader, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(xMLStreamReader, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(Node node) throws XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(node, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(node, STTblWidth.type, xmlOptions);
        }

        public static STTblWidth parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(xMLInputStream, STTblWidth.type, (XmlOptions) null);
        }

        public static STTblWidth parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTblWidth) POIXMLTypeLoader.parse(xMLInputStream, STTblWidth.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTblWidth.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTblWidth.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
