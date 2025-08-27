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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCols.class */
public interface CTCols extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCols.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcols627ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCols$Factory.class */
    public static final class Factory {
        public static CTCols newInstance() {
            return (CTCols) POIXMLTypeLoader.newInstance(CTCols.type, null);
        }

        public static CTCols newInstance(XmlOptions xmlOptions) {
            return (CTCols) POIXMLTypeLoader.newInstance(CTCols.type, xmlOptions);
        }

        public static CTCols parse(String str) throws XmlException {
            return (CTCols) POIXMLTypeLoader.parse(str, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCols) POIXMLTypeLoader.parse(str, CTCols.type, xmlOptions);
        }

        public static CTCols parse(File file) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(file, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(file, CTCols.type, xmlOptions);
        }

        public static CTCols parse(URL url) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(url, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(url, CTCols.type, xmlOptions);
        }

        public static CTCols parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(inputStream, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(inputStream, CTCols.type, xmlOptions);
        }

        public static CTCols parse(Reader reader) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(reader, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCols) POIXMLTypeLoader.parse(reader, CTCols.type, xmlOptions);
        }

        public static CTCols parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCols) POIXMLTypeLoader.parse(xMLStreamReader, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCols) POIXMLTypeLoader.parse(xMLStreamReader, CTCols.type, xmlOptions);
        }

        public static CTCols parse(Node node) throws XmlException {
            return (CTCols) POIXMLTypeLoader.parse(node, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCols) POIXMLTypeLoader.parse(node, CTCols.type, xmlOptions);
        }

        public static CTCols parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCols) POIXMLTypeLoader.parse(xMLInputStream, CTCols.type, (XmlOptions) null);
        }

        public static CTCols parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCols) POIXMLTypeLoader.parse(xMLInputStream, CTCols.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCols.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCols.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCol> getColList();

    CTCol[] getColArray();

    CTCol getColArray(int i);

    int sizeOfColArray();

    void setColArray(CTCol[] cTColArr);

    void setColArray(int i, CTCol cTCol);

    CTCol insertNewCol(int i);

    CTCol addNewCol();

    void removeCol(int i);
}
