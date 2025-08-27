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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPaneState.class */
public interface STPaneState extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STPaneState.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stpanestateae58type");
    public static final Enum SPLIT = Enum.forString("split");
    public static final Enum FROZEN = Enum.forString("frozen");
    public static final Enum FROZEN_SPLIT = Enum.forString("frozenSplit");
    public static final int INT_SPLIT = 1;
    public static final int INT_FROZEN = 2;
    public static final int INT_FROZEN_SPLIT = 3;

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPaneState$Enum.class */
    public static final class Enum extends StringEnumAbstractBase {
        static final int INT_SPLIT = 1;
        static final int INT_FROZEN = 2;
        static final int INT_FROZEN_SPLIT = 3;
        public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("split", 1), new Enum("frozen", 2), new Enum("frozenSplit", 3)});
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

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPaneState$Factory.class */
    public static final class Factory {
        public static STPaneState newValue(Object obj) {
            return (STPaneState) STPaneState.type.newValue(obj);
        }

        public static STPaneState newInstance() {
            return (STPaneState) POIXMLTypeLoader.newInstance(STPaneState.type, null);
        }

        public static STPaneState newInstance(XmlOptions xmlOptions) {
            return (STPaneState) POIXMLTypeLoader.newInstance(STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(String str) throws XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(str, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(str, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(File file) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(file, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(file, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(URL url) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(url, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(url, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(InputStream inputStream) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(inputStream, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(inputStream, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(Reader reader) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(reader, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPaneState) POIXMLTypeLoader.parse(reader, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(xMLStreamReader, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(xMLStreamReader, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(Node node) throws XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(node, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(node, STPaneState.type, xmlOptions);
        }

        public static STPaneState parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(xMLInputStream, STPaneState.type, (XmlOptions) null);
        }

        public static STPaneState parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STPaneState) POIXMLTypeLoader.parse(xMLInputStream, STPaneState.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPaneState.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPaneState.type, xmlOptions);
        }

        private Factory() {
        }
    }

    StringEnumAbstractBase enumValue();

    void set(StringEnumAbstractBase stringEnumAbstractBase);
}
