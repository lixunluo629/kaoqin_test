package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheets.class */
public interface CTSheets extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSheets.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsheets49fdtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheets$Factory.class */
    public static final class Factory {
        public static CTSheets newInstance() {
            return (CTSheets) POIXMLTypeLoader.newInstance(CTSheets.type, null);
        }

        public static CTSheets newInstance(XmlOptions xmlOptions) {
            return (CTSheets) POIXMLTypeLoader.newInstance(CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(String str) throws XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(str, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(str, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(File file) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(file, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(file, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(URL url) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(url, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(url, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(inputStream, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(inputStream, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(Reader reader) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(reader, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheets) POIXMLTypeLoader.parse(reader, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(xMLStreamReader, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(xMLStreamReader, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(Node node) throws XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(node, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(node, CTSheets.type, xmlOptions);
        }

        public static CTSheets parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(xMLInputStream, CTSheets.type, (XmlOptions) null);
        }

        public static CTSheets parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSheets) POIXMLTypeLoader.parse(xMLInputStream, CTSheets.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheets.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheets.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTSheet> getSheetList();

    CTSheet[] getSheetArray();

    CTSheet getSheetArray(int i);

    int sizeOfSheetArray();

    void setSheetArray(CTSheet[] cTSheetArr);

    void setSheetArray(int i, CTSheet cTSheet);

    CTSheet insertNewSheet(int i);

    CTSheet addNewSheet();

    void removeSheet(int i);
}
