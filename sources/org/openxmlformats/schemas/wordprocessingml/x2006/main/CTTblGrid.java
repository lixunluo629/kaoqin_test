package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblGrid.class */
public interface CTTblGrid extends CTTblGridBase {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTblGrid.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttblgrid2eeetype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblGrid$Factory.class */
    public static final class Factory {
        public static CTTblGrid newInstance() {
            return (CTTblGrid) POIXMLTypeLoader.newInstance(CTTblGrid.type, null);
        }

        public static CTTblGrid newInstance(XmlOptions xmlOptions) {
            return (CTTblGrid) POIXMLTypeLoader.newInstance(CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(String str) throws XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(str, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(str, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(File file) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(file, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(file, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(URL url) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(url, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(url, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(inputStream, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(inputStream, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(Reader reader) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(reader, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblGrid) POIXMLTypeLoader.parse(reader, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(xMLStreamReader, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(xMLStreamReader, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(Node node) throws XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(node, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(node, CTTblGrid.type, xmlOptions);
        }

        public static CTTblGrid parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(xMLInputStream, CTTblGrid.type, (XmlOptions) null);
        }

        public static CTTblGrid parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTblGrid) POIXMLTypeLoader.parse(xMLInputStream, CTTblGrid.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblGrid.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblGrid.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTblGridChange getTblGridChange();

    boolean isSetTblGridChange();

    void setTblGridChange(CTTblGridChange cTTblGridChange);

    CTTblGridChange addNewTblGridChange();

    void unsetTblGridChange();
}
