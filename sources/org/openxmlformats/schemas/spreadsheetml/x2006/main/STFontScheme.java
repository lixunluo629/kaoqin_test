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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STFontScheme.class */
public interface STFontScheme extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STFontScheme.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stfontschemef36dtype");
    public static final Enum NONE = Enum.forString("none");
    public static final Enum MAJOR = Enum.forString("major");
    public static final Enum MINOR = Enum.forString("minor");
    public static final int INT_NONE = 1;
    public static final int INT_MAJOR = 2;
    public static final int INT_MINOR = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STFontScheme$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NONE = 1;
        static final int INT_MAJOR = 2;
        static final int INT_MINOR = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("none", 1), new Enum("major", 2), new Enum("minor", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STFontScheme$Factory.class */
    public static final class Factory {
        public static STFontScheme newValue(Object obj) {
            return (STFontScheme) STFontScheme.type.newValue(obj);
        }

        public static STFontScheme newInstance() {
            return (STFontScheme) POIXMLTypeLoader.newInstance(STFontScheme.type, null);
        }

        public static STFontScheme newInstance(XmlOptions xmlOptions) {
            return (STFontScheme) POIXMLTypeLoader.newInstance(STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(String str) throws XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(str, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(str, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(File file) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(file, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(file, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(URL url) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(url, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(url, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(InputStream inputStream) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(inputStream, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(inputStream, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(Reader reader) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(reader, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontScheme) POIXMLTypeLoader.parse(reader, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(xMLStreamReader, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(xMLStreamReader, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(Node node) throws XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(node, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(node, STFontScheme.type, xmlOptions);
        }

        public static STFontScheme parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(xMLInputStream, STFontScheme.type, (XmlOptions) null);
        }

        public static STFontScheme parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STFontScheme) POIXMLTypeLoader.parse(xMLInputStream, STFontScheme.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFontScheme.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFontScheme.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
