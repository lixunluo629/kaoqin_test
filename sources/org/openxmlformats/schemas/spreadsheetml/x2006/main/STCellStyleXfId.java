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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellStyleXfId.class */
public interface STCellStyleXfId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCellStyleXfId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcellstylexfid70c7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STCellStyleXfId$Factory.class */
    public static final class Factory {
        public static STCellStyleXfId newValue(Object obj) {
            return (STCellStyleXfId) STCellStyleXfId.type.newValue(obj);
        }

        public static STCellStyleXfId newInstance() {
            return (STCellStyleXfId) POIXMLTypeLoader.newInstance(STCellStyleXfId.type, null);
        }

        public static STCellStyleXfId newInstance(XmlOptions xmlOptions) {
            return (STCellStyleXfId) POIXMLTypeLoader.newInstance(STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(String str) throws XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(str, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(str, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(File file) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(file, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(file, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(URL url) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(url, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(url, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(InputStream inputStream) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(inputStream, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(inputStream, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(Reader reader) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(reader, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(reader, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(xMLStreamReader, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(xMLStreamReader, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(Node node) throws XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(node, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(node, STCellStyleXfId.type, xmlOptions);
        }

        public static STCellStyleXfId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(xMLInputStream, STCellStyleXfId.type, (XmlOptions) null);
        }

        public static STCellStyleXfId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCellStyleXfId) POIXMLTypeLoader.parse(xMLInputStream, STCellStyleXfId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCellStyleXfId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCellStyleXfId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
