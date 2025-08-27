package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.catalina.Lifecycle;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STDataValidationErrorStyle.class */
public interface STDataValidationErrorStyle extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STDataValidationErrorStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stdatavalidationerrorstyleca85type");
    public static final Enum STOP = Enum.forString(Lifecycle.STOP_EVENT);
    public static final Enum WARNING = Enum.forString(AsmRelationshipUtils.DECLARE_WARNING);
    public static final Enum INFORMATION = Enum.forString("information");
    public static final int INT_STOP = 1;
    public static final int INT_WARNING = 2;
    public static final int INT_INFORMATION = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STDataValidationErrorStyle$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_STOP = 1;
        static final int INT_WARNING = 2;
        static final int INT_INFORMATION = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum(Lifecycle.STOP_EVENT, 1), new Enum(AsmRelationshipUtils.DECLARE_WARNING, 2), new Enum("information", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STDataValidationErrorStyle$Factory.class */
    public static final class Factory {
        public static STDataValidationErrorStyle newValue(Object obj) {
            return (STDataValidationErrorStyle) STDataValidationErrorStyle.type.newValue(obj);
        }

        public static STDataValidationErrorStyle newInstance() {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.newInstance(STDataValidationErrorStyle.type, null);
        }

        public static STDataValidationErrorStyle newInstance(XmlOptions xmlOptions) {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.newInstance(STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(String str) throws XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(str, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(str, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(File file) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(file, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(file, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(URL url) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(url, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(url, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(inputStream, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(inputStream, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(Reader reader) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(reader, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(reader, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(xMLStreamReader, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(xMLStreamReader, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(Node node) throws XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(node, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(node, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static STDataValidationErrorStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(xMLInputStream, STDataValidationErrorStyle.type, (XmlOptions) null);
        }

        public static STDataValidationErrorStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STDataValidationErrorStyle) POIXMLTypeLoader.parse(xMLInputStream, STDataValidationErrorStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDataValidationErrorStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDataValidationErrorStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
