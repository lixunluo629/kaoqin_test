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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STFtnEdn.class */
public interface STFtnEdn extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STFtnEdn.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stftnednd4c9type");
    public static final Enum NORMAL = Enum.forString("normal");
    public static final Enum SEPARATOR = Enum.forString("separator");
    public static final Enum CONTINUATION_SEPARATOR = Enum.forString("continuationSeparator");
    public static final Enum CONTINUATION_NOTICE = Enum.forString("continuationNotice");
    public static final int INT_NORMAL = 1;
    public static final int INT_SEPARATOR = 2;
    public static final int INT_CONTINUATION_SEPARATOR = 3;
    public static final int INT_CONTINUATION_NOTICE = 4;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STFtnEdn$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NORMAL = 1;
        static final int INT_SEPARATOR = 2;
        static final int INT_CONTINUATION_SEPARATOR = 3;
        static final int INT_CONTINUATION_NOTICE = 4;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("normal", 1), new Enum("separator", 2), new Enum("continuationSeparator", 3), new Enum("continuationNotice", 4)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STFtnEdn$Factory.class */
    public static final class Factory {
        public static STFtnEdn newValue(Object obj) {
            return (STFtnEdn) STFtnEdn.type.newValue(obj);
        }

        public static STFtnEdn newInstance() {
            return (STFtnEdn) POIXMLTypeLoader.newInstance(STFtnEdn.type, null);
        }

        public static STFtnEdn newInstance(XmlOptions xmlOptions) {
            return (STFtnEdn) POIXMLTypeLoader.newInstance(STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(String str) throws XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(str, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(str, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(File file) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(file, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(file, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(URL url) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(url, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(url, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(InputStream inputStream) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(inputStream, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(inputStream, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(Reader reader) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(reader, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFtnEdn) POIXMLTypeLoader.parse(reader, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(xMLStreamReader, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(xMLStreamReader, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(Node node) throws XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(node, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(node, STFtnEdn.type, xmlOptions);
        }

        public static STFtnEdn parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(xMLInputStream, STFtnEdn.type, (XmlOptions) null);
        }

        public static STFtnEdn parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STFtnEdn) POIXMLTypeLoader.parse(xMLInputStream, STFtnEdn.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFtnEdn.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFtnEdn.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
