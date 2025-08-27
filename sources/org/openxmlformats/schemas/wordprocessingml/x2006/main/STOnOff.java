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
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STOnOff.class */
public interface STOnOff extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STOnOff.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stonofffcd2type");
    public static final Enum TRUE = Enum.forString("true");
    public static final Enum FALSE = Enum.forString("false");
    public static final Enum ON = Enum.forString(CustomBooleanEditor.VALUE_ON);
    public static final Enum OFF = Enum.forString(CustomBooleanEditor.VALUE_OFF);
    public static final Enum X_0 = Enum.forString("0");
    public static final Enum X_1 = Enum.forString("1");
    public static final int INT_TRUE = 1;
    public static final int INT_FALSE = 2;
    public static final int INT_ON = 3;
    public static final int INT_OFF = 4;
    public static final int INT_X_0 = 5;
    public static final int INT_X_1 = 6;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STOnOff$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_TRUE = 1;
        static final int INT_FALSE = 2;
        static final int INT_ON = 3;
        static final int INT_OFF = 4;
        static final int INT_X_0 = 5;
        static final int INT_X_1 = 6;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("true", 1), new Enum("false", 2), new Enum(CustomBooleanEditor.VALUE_ON, 3), new Enum(CustomBooleanEditor.VALUE_OFF, 4), new Enum("0", 5), new Enum("1", 6)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STOnOff$Factory.class */
    public static final class Factory {
        public static STOnOff newValue(Object obj) {
            return (STOnOff) STOnOff.type.newValue(obj);
        }

        public static STOnOff newInstance() {
            return (STOnOff) POIXMLTypeLoader.newInstance(STOnOff.type, null);
        }

        public static STOnOff newInstance(XmlOptions xmlOptions) {
            return (STOnOff) POIXMLTypeLoader.newInstance(STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(String str) throws XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(str, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(str, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(File file) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(file, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(file, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(URL url) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(url, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(url, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(InputStream inputStream) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(inputStream, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(inputStream, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(Reader reader) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(reader, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STOnOff) POIXMLTypeLoader.parse(reader, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(xMLStreamReader, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(xMLStreamReader, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(Node node) throws XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(node, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(node, STOnOff.type, xmlOptions);
        }

        public static STOnOff parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(xMLInputStream, STOnOff.type, (XmlOptions) null);
        }

        public static STOnOff parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STOnOff) POIXMLTypeLoader.parse(xMLInputStream, STOnOff.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STOnOff.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STOnOff.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
