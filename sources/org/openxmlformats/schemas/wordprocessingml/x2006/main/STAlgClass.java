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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STAlgClass.class */
public interface STAlgClass extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STAlgClass.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stalgclass061ctype");
    public static final Enum HASH = Enum.forString("hash");
    public static final int INT_HASH = 1;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STAlgClass$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_HASH = 1;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("hash", 1)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STAlgClass$Factory.class */
    public static final class Factory {
        public static STAlgClass newValue(Object obj) {
            return (STAlgClass) STAlgClass.type.newValue(obj);
        }

        public static STAlgClass newInstance() {
            return (STAlgClass) POIXMLTypeLoader.newInstance(STAlgClass.type, null);
        }

        public static STAlgClass newInstance(XmlOptions xmlOptions) {
            return (STAlgClass) POIXMLTypeLoader.newInstance(STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(String str) throws XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(str, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(str, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(File file) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(file, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(file, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(URL url) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(url, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(url, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(InputStream inputStream) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(inputStream, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(inputStream, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(Reader reader) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(reader, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAlgClass) POIXMLTypeLoader.parse(reader, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(xMLStreamReader, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(xMLStreamReader, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(Node node) throws XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(node, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(node, STAlgClass.type, xmlOptions);
        }

        public static STAlgClass parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(xMLInputStream, STAlgClass.type, (XmlOptions) null);
        }

        public static STAlgClass parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STAlgClass) POIXMLTypeLoader.parse(xMLInputStream, STAlgClass.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAlgClass.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAlgClass.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
