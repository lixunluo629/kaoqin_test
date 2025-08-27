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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STOrientation.class */
public interface STOrientation extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STOrientation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("storientation3c9ftype");
    public static final Enum DEFAULT = Enum.forString("default");
    public static final Enum PORTRAIT = Enum.forString("portrait");
    public static final Enum LANDSCAPE = Enum.forString("landscape");
    public static final int INT_DEFAULT = 1;
    public static final int INT_PORTRAIT = 2;
    public static final int INT_LANDSCAPE = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STOrientation$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_DEFAULT = 1;
        static final int INT_PORTRAIT = 2;
        static final int INT_LANDSCAPE = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("default", 1), new Enum("portrait", 2), new Enum("landscape", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STOrientation$Factory.class */
    public static final class Factory {
        public static STOrientation newValue(Object obj) {
            return (STOrientation) STOrientation.type.newValue(obj);
        }

        public static STOrientation newInstance() {
            return (STOrientation) POIXMLTypeLoader.newInstance(STOrientation.type, null);
        }

        public static STOrientation newInstance(XmlOptions xmlOptions) {
            return (STOrientation) POIXMLTypeLoader.newInstance(STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(String str) throws XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(str, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(str, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(File file) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(file, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(file, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(URL url) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(url, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(url, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(InputStream inputStream) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(inputStream, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(inputStream, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(Reader reader) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(reader, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOrientation) POIXMLTypeLoader.parse(reader, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(xMLStreamReader, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(xMLStreamReader, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(Node node) throws XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(node, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(node, STOrientation.type, xmlOptions);
        }

        public static STOrientation parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(xMLInputStream, STOrientation.type, (XmlOptions) null);
        }

        public static STOrientation parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STOrientation) POIXMLTypeLoader.parse(xMLInputStream, STOrientation.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STOrientation.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STOrientation.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
