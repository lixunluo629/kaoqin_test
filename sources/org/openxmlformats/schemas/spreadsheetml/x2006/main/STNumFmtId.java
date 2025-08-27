package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STNumFmtId.class */
public interface STNumFmtId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STNumFmtId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stnumfmtid76fbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STNumFmtId$Factory.class */
    public static final class Factory {
        public static STNumFmtId newValue(Object obj) {
            return (STNumFmtId) STNumFmtId.type.newValue(obj);
        }

        public static STNumFmtId newInstance() {
            return (STNumFmtId) POIXMLTypeLoader.newInstance(STNumFmtId.type, null);
        }

        public static STNumFmtId newInstance(XmlOptions xmlOptions) {
            return (STNumFmtId) POIXMLTypeLoader.newInstance(STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(String str) throws XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(str, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(str, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(File file) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(file, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(file, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(URL url) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(url, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(url, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(InputStream inputStream) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(inputStream, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(inputStream, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(Reader reader) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(reader, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STNumFmtId) POIXMLTypeLoader.parse(reader, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(xMLStreamReader, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(xMLStreamReader, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(Node node) throws XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(node, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(node, STNumFmtId.type, xmlOptions);
        }

        public static STNumFmtId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(xMLInputStream, STNumFmtId.type, (XmlOptions) null);
        }

        public static STNumFmtId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STNumFmtId) POIXMLTypeLoader.parse(xMLInputStream, STNumFmtId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STNumFmtId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STNumFmtId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
