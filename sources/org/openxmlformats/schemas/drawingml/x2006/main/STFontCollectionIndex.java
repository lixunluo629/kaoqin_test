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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STFontCollectionIndex.class */
public interface STFontCollectionIndex extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STFontCollectionIndex.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stfontcollectionindex6766type");
    public static final Enum MAJOR = Enum.forString("major");
    public static final Enum MINOR = Enum.forString("minor");
    public static final Enum NONE = Enum.forString("none");
    public static final int INT_MAJOR = 1;
    public static final int INT_MINOR = 2;
    public static final int INT_NONE = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STFontCollectionIndex$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_MAJOR = 1;
        static final int INT_MINOR = 2;
        static final int INT_NONE = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("major", 1), new Enum("minor", 2), new Enum("none", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STFontCollectionIndex$Factory.class */
    public static final class Factory {
        public static STFontCollectionIndex newValue(Object obj) {
            return (STFontCollectionIndex) STFontCollectionIndex.type.newValue(obj);
        }

        public static STFontCollectionIndex newInstance() {
            return (STFontCollectionIndex) POIXMLTypeLoader.newInstance(STFontCollectionIndex.type, null);
        }

        public static STFontCollectionIndex newInstance(XmlOptions xmlOptions) {
            return (STFontCollectionIndex) POIXMLTypeLoader.newInstance(STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(String str) throws XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(str, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(str, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(File file) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(file, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(file, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(URL url) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(url, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(url, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(InputStream inputStream) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(inputStream, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(inputStream, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(Reader reader) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(reader, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(reader, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(xMLStreamReader, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(xMLStreamReader, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(Node node) throws XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(node, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(node, STFontCollectionIndex.type, xmlOptions);
        }

        public static STFontCollectionIndex parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(xMLInputStream, STFontCollectionIndex.type, (XmlOptions) null);
        }

        public static STFontCollectionIndex parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STFontCollectionIndex) POIXMLTypeLoader.parse(xMLInputStream, STFontCollectionIndex.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFontCollectionIndex.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFontCollectionIndex.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
