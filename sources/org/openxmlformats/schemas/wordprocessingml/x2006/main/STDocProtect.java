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
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STDocProtect.class */
public interface STDocProtect extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STDocProtect.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stdocprotect5801type");
    public static final Enum NONE = Enum.forString("none");
    public static final Enum READ_ONLY = Enum.forString(DefaultTransactionDefinition.READ_ONLY_MARKER);
    public static final Enum COMMENTS = Enum.forString("comments");
    public static final Enum TRACKED_CHANGES = Enum.forString("trackedChanges");
    public static final Enum FORMS = Enum.forString("forms");
    public static final int INT_NONE = 1;
    public static final int INT_READ_ONLY = 2;
    public static final int INT_COMMENTS = 3;
    public static final int INT_TRACKED_CHANGES = 4;
    public static final int INT_FORMS = 5;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STDocProtect$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_NONE = 1;
        static final int INT_READ_ONLY = 2;
        static final int INT_COMMENTS = 3;
        static final int INT_TRACKED_CHANGES = 4;
        static final int INT_FORMS = 5;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("none", 1), new Enum(DefaultTransactionDefinition.READ_ONLY_MARKER, 2), new Enum("comments", 3), new Enum("trackedChanges", 4), new Enum("forms", 5)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STDocProtect$Factory.class */
    public static final class Factory {
        public static STDocProtect newValue(Object obj) {
            return (STDocProtect) STDocProtect.type.newValue(obj);
        }

        public static STDocProtect newInstance() {
            return (STDocProtect) POIXMLTypeLoader.newInstance(STDocProtect.type, null);
        }

        public static STDocProtect newInstance(XmlOptions xmlOptions) {
            return (STDocProtect) POIXMLTypeLoader.newInstance(STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(String str) throws XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(str, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(str, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(File file) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(file, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(file, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(URL url) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(url, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(url, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(InputStream inputStream) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(inputStream, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(inputStream, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(Reader reader) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(reader, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDocProtect) POIXMLTypeLoader.parse(reader, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(xMLStreamReader, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(xMLStreamReader, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(Node node) throws XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(node, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(node, STDocProtect.type, xmlOptions);
        }

        public static STDocProtect parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(xMLInputStream, STDocProtect.type, (XmlOptions) null);
        }

        public static STDocProtect parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STDocProtect) POIXMLTypeLoader.parse(xMLInputStream, STDocProtect.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDocProtect.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDocProtect.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
