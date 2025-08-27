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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STDxfId.class */
public interface STDxfId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STDxfId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stdxfid9fdctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STDxfId$Factory.class */
    public static final class Factory {
        public static STDxfId newValue(Object obj) {
            return (STDxfId) STDxfId.type.newValue(obj);
        }

        public static STDxfId newInstance() {
            return (STDxfId) POIXMLTypeLoader.newInstance(STDxfId.type, null);
        }

        public static STDxfId newInstance(XmlOptions xmlOptions) {
            return (STDxfId) POIXMLTypeLoader.newInstance(STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(String str) throws XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(str, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(str, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(File file) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(file, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(file, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(URL url) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(url, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(url, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(InputStream inputStream) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(inputStream, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(inputStream, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(Reader reader) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(reader, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDxfId) POIXMLTypeLoader.parse(reader, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(xMLStreamReader, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(xMLStreamReader, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(Node node) throws XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(node, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(node, STDxfId.type, xmlOptions);
        }

        public static STDxfId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(xMLInputStream, STDxfId.type, (XmlOptions) null);
        }

        public static STDxfId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STDxfId) POIXMLTypeLoader.parse(xMLInputStream, STDxfId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDxfId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDxfId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
