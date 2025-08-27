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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextStrikeType.class */
public interface STTextStrikeType extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextStrikeType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextstriketype4744type");
    public static final Enum NO_STRIKE = Enum.forString("noStrike");
    public static final Enum SNG_STRIKE = Enum.forString("sngStrike");
    public static final Enum DBL_STRIKE = Enum.forString("dblStrike");
    public static final int INT_NO_STRIKE = 1;
    public static final int INT_SNG_STRIKE = 2;
    public static final int INT_DBL_STRIKE = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextStrikeType$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NO_STRIKE = 1;
        static final int INT_SNG_STRIKE = 2;
        static final int INT_DBL_STRIKE = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("noStrike", 1), new Enum("sngStrike", 2), new Enum("dblStrike", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextStrikeType$Factory.class */
    public static final class Factory {
        public static STTextStrikeType newValue(Object obj) {
            return (STTextStrikeType) STTextStrikeType.type.newValue(obj);
        }

        public static STTextStrikeType newInstance() {
            return (STTextStrikeType) POIXMLTypeLoader.newInstance(STTextStrikeType.type, null);
        }

        public static STTextStrikeType newInstance(XmlOptions xmlOptions) {
            return (STTextStrikeType) POIXMLTypeLoader.newInstance(STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(String str) throws XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(str, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(str, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(File file) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(file, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(file, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(URL url) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(url, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(url, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(inputStream, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(inputStream, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(Reader reader) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(reader, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(reader, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(xMLStreamReader, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(xMLStreamReader, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(Node node) throws XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(node, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(node, STTextStrikeType.type, xmlOptions);
        }

        public static STTextStrikeType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(xMLInputStream, STTextStrikeType.type, (XmlOptions) null);
        }

        public static STTextStrikeType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextStrikeType) POIXMLTypeLoader.parse(xMLInputStream, STTextStrikeType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextStrikeType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextStrikeType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
