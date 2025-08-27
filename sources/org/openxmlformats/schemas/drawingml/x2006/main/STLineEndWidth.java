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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineEndWidth.class */
public interface STLineEndWidth extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLineEndWidth.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlineendwidth16aatype");
    public static final Enum SM = Enum.forString("sm");
    public static final Enum MED = Enum.forString("med");
    public static final Enum LG = Enum.forString("lg");
    public static final int INT_SM = 1;
    public static final int INT_MED = 2;
    public static final int INT_LG = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineEndWidth$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_SM = 1;
        static final int INT_MED = 2;
        static final int INT_LG = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("sm", 1), new Enum("med", 2), new Enum("lg", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STLineEndWidth$Factory.class */
    public static final class Factory {
        public static STLineEndWidth newValue(Object obj) {
            return (STLineEndWidth) STLineEndWidth.type.newValue(obj);
        }

        public static STLineEndWidth newInstance() {
            return (STLineEndWidth) POIXMLTypeLoader.newInstance(STLineEndWidth.type, null);
        }

        public static STLineEndWidth newInstance(XmlOptions xmlOptions) {
            return (STLineEndWidth) POIXMLTypeLoader.newInstance(STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(String str) throws XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(str, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(str, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(File file) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(file, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(file, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(URL url) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(url, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(url, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(InputStream inputStream) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(inputStream, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(inputStream, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(Reader reader) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(reader, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(reader, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(xMLStreamReader, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(xMLStreamReader, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(Node node) throws XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(node, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(node, STLineEndWidth.type, xmlOptions);
        }

        public static STLineEndWidth parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(xMLInputStream, STLineEndWidth.type, (XmlOptions) null);
        }

        public static STLineEndWidth parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLineEndWidth) POIXMLTypeLoader.parse(xMLInputStream, STLineEndWidth.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineEndWidth.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLineEndWidth.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
