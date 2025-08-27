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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColors.class */
public interface CTColors extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTColors.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcolors6579type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColors$Factory.class */
    public static final class Factory {
        public static CTColors newInstance() {
            return (CTColors) POIXMLTypeLoader.newInstance(CTColors.type, null);
        }

        public static CTColors newInstance(XmlOptions xmlOptions) {
            return (CTColors) POIXMLTypeLoader.newInstance(CTColors.type, xmlOptions);
        }

        public static CTColors parse(String str) throws XmlException {
            return (CTColors) POIXMLTypeLoader.parse(str, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTColors) POIXMLTypeLoader.parse(str, CTColors.type, xmlOptions);
        }

        public static CTColors parse(File file) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(file, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(file, CTColors.type, xmlOptions);
        }

        public static CTColors parse(URL url) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(url, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(url, CTColors.type, xmlOptions);
        }

        public static CTColors parse(InputStream inputStream) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(inputStream, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(inputStream, CTColors.type, xmlOptions);
        }

        public static CTColors parse(Reader reader) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(reader, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColors) POIXMLTypeLoader.parse(reader, CTColors.type, xmlOptions);
        }

        public static CTColors parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTColors) POIXMLTypeLoader.parse(xMLStreamReader, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTColors) POIXMLTypeLoader.parse(xMLStreamReader, CTColors.type, xmlOptions);
        }

        public static CTColors parse(Node node) throws XmlException {
            return (CTColors) POIXMLTypeLoader.parse(node, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTColors) POIXMLTypeLoader.parse(node, CTColors.type, xmlOptions);
        }

        public static CTColors parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTColors) POIXMLTypeLoader.parse(xMLInputStream, CTColors.type, (XmlOptions) null);
        }

        public static CTColors parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTColors) POIXMLTypeLoader.parse(xMLInputStream, CTColors.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColors.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColors.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTIndexedColors getIndexedColors();

    boolean isSetIndexedColors();

    void setIndexedColors(CTIndexedColors cTIndexedColors);

    CTIndexedColors addNewIndexedColors();

    void unsetIndexedColors();

    CTMRUColors getMruColors();

    boolean isSetMruColors();

    void setMruColors(CTMRUColors cTMRUColors);

    CTMRUColors addNewMruColors();

    void unsetMruColors();
}
