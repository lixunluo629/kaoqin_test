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
import org.springframework.hateoas.Link;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHdrFtr.class */
public interface STHdrFtr extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STHdrFtr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sthdrftr30catype");
    public static final Enum EVEN = Enum.forString("even");
    public static final Enum DEFAULT = Enum.forString("default");
    public static final Enum FIRST = Enum.forString(Link.REL_FIRST);
    public static final int INT_EVEN = 1;
    public static final int INT_DEFAULT = 2;
    public static final int INT_FIRST = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHdrFtr$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_EVEN = 1;
        static final int INT_DEFAULT = 2;
        static final int INT_FIRST = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("even", 1), new Enum("default", 2), new Enum(Link.REL_FIRST, 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHdrFtr$Factory.class */
    public static final class Factory {
        public static STHdrFtr newValue(Object obj) {
            return (STHdrFtr) STHdrFtr.type.newValue(obj);
        }

        public static STHdrFtr newInstance() {
            return (STHdrFtr) POIXMLTypeLoader.newInstance(STHdrFtr.type, null);
        }

        public static STHdrFtr newInstance(XmlOptions xmlOptions) {
            return (STHdrFtr) POIXMLTypeLoader.newInstance(STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(String str) throws XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(str, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(str, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(File file) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(file, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(file, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(URL url) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(url, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(url, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(InputStream inputStream) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(inputStream, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(inputStream, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(Reader reader) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(reader, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHdrFtr) POIXMLTypeLoader.parse(reader, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(xMLStreamReader, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(xMLStreamReader, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(Node node) throws XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(node, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(node, STHdrFtr.type, xmlOptions);
        }

        public static STHdrFtr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(xMLInputStream, STHdrFtr.type, (XmlOptions) null);
        }

        public static STHdrFtr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STHdrFtr) POIXMLTypeLoader.parse(xMLInputStream, STHdrFtr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STHdrFtr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STHdrFtr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
