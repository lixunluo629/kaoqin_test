package com.microsoft.schemas.office.x2006.encryption;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/STCipherChaining.class */
public interface STCipherChaining extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCipherChaining.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("stcipherchaining1e98type");
    public static final Enum CHAINING_MODE_CBC = Enum.forString("ChainingModeCBC");
    public static final Enum CHAINING_MODE_CFB = Enum.forString("ChainingModeCFB");
    public static final int INT_CHAINING_MODE_CBC = 1;
    public static final int INT_CHAINING_MODE_CFB = 2;

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/STCipherChaining$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_CHAINING_MODE_CBC = 1;
        static final int INT_CHAINING_MODE_CFB = 2;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("ChainingModeCBC", 1), new Enum("ChainingModeCFB", 2)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/encryption/STCipherChaining$Factory.class */
    public static final class Factory {
        public static STCipherChaining newValue(Object obj) {
            return (STCipherChaining) STCipherChaining.type.newValue(obj);
        }

        public static STCipherChaining newInstance() {
            return (STCipherChaining) POIXMLTypeLoader.newInstance(STCipherChaining.type, null);
        }

        public static STCipherChaining newInstance(XmlOptions xmlOptions) {
            return (STCipherChaining) POIXMLTypeLoader.newInstance(STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(String str) throws XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(str, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(str, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(File file) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(file, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(file, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(URL url) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(url, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(url, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(InputStream inputStream) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(inputStream, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(inputStream, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(Reader reader) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(reader, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCipherChaining) POIXMLTypeLoader.parse(reader, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(xMLStreamReader, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(xMLStreamReader, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(Node node) throws XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(node, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(node, STCipherChaining.type, xmlOptions);
        }

        public static STCipherChaining parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(xMLInputStream, STCipherChaining.type, (XmlOptions) null);
        }

        public static STCipherChaining parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCipherChaining) POIXMLTypeLoader.parse(xMLInputStream, STCipherChaining.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCipherChaining.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCipherChaining.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
