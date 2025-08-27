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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTAuthors.class */
public interface CTAuthors extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTAuthors.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctauthorsb8a7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTAuthors$Factory.class */
    public static final class Factory {
        public static CTAuthors newInstance() {
            return (CTAuthors) POIXMLTypeLoader.newInstance(CTAuthors.type, null);
        }

        public static CTAuthors newInstance(XmlOptions xmlOptions) {
            return (CTAuthors) POIXMLTypeLoader.newInstance(CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(String str) throws XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(str, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(str, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(File file) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(file, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(file, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(URL url) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(url, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(url, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(InputStream inputStream) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(inputStream, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(inputStream, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(Reader reader) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(reader, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAuthors) POIXMLTypeLoader.parse(reader, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(xMLStreamReader, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(xMLStreamReader, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(Node node) throws XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(node, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(node, CTAuthors.type, xmlOptions);
        }

        public static CTAuthors parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(xMLInputStream, CTAuthors.type, (XmlOptions) null);
        }

        public static CTAuthors parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTAuthors) POIXMLTypeLoader.parse(xMLInputStream, CTAuthors.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAuthors.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAuthors.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<String> getAuthorList();

    String[] getAuthorArray();

    String getAuthorArray(int i);

    List<STXstring> xgetAuthorList();

    STXstring[] xgetAuthorArray();

    STXstring xgetAuthorArray(int i);

    int sizeOfAuthorArray();

    void setAuthorArray(String[] strArr);

    void setAuthorArray(int i, String str);

    void xsetAuthorArray(STXstring[] sTXstringArr);

    void xsetAuthorArray(int i, STXstring sTXstring);

    void insertAuthor(int i, String str);

    void addAuthor(String str);

    STXstring insertNewAuthor(int i);

    STXstring addNewAuthor();

    void removeAuthor(int i);
}
