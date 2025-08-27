package com.microsoft.schemas.vml;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STExt.class */
public interface STExt extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STExt.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stext2fe5type");
    public static final Enum VIEW = Enum.forString("view");
    public static final Enum EDIT = Enum.forString("edit");
    public static final Enum BACKWARD_COMPATIBLE = Enum.forString("backwardCompatible");
    public static final int INT_VIEW = 1;
    public static final int INT_EDIT = 2;
    public static final int INT_BACKWARD_COMPATIBLE = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STExt$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_VIEW = 1;
        static final int INT_EDIT = 2;
        static final int INT_BACKWARD_COMPATIBLE = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("view", 1), new Enum("edit", 2), new Enum("backwardCompatible", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STExt$Factory.class */
    public static final class Factory {
        public static STExt newValue(Object obj) {
            return (STExt) STExt.type.newValue(obj);
        }

        public static STExt newInstance() {
            return (STExt) POIXMLTypeLoader.newInstance(STExt.type, null);
        }

        public static STExt newInstance(XmlOptions xmlOptions) {
            return (STExt) POIXMLTypeLoader.newInstance(STExt.type, xmlOptions);
        }

        public static STExt parse(String str) throws XmlException {
            return (STExt) POIXMLTypeLoader.parse(str, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STExt) POIXMLTypeLoader.parse(str, STExt.type, xmlOptions);
        }

        public static STExt parse(File file) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(file, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(file, STExt.type, xmlOptions);
        }

        public static STExt parse(URL url) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(url, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(url, STExt.type, xmlOptions);
        }

        public static STExt parse(InputStream inputStream) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(inputStream, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(inputStream, STExt.type, xmlOptions);
        }

        public static STExt parse(Reader reader) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(reader, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STExt) POIXMLTypeLoader.parse(reader, STExt.type, xmlOptions);
        }

        public static STExt parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STExt) POIXMLTypeLoader.parse(xMLStreamReader, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STExt) POIXMLTypeLoader.parse(xMLStreamReader, STExt.type, xmlOptions);
        }

        public static STExt parse(Node node) throws XmlException {
            return (STExt) POIXMLTypeLoader.parse(node, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STExt) POIXMLTypeLoader.parse(node, STExt.type, xmlOptions);
        }

        public static STExt parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STExt) POIXMLTypeLoader.parse(xMLInputStream, STExt.type, (XmlOptions) null);
        }

        public static STExt parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STExt) POIXMLTypeLoader.parse(xMLInputStream, STExt.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STExt.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STExt.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
