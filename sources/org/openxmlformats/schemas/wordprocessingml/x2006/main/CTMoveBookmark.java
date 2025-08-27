package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTMoveBookmark.class */
public interface CTMoveBookmark extends CTBookmark {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTMoveBookmark.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctmovebookmarkf7a1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTMoveBookmark$Factory.class */
    public static final class Factory {
        public static CTMoveBookmark newInstance() {
            return (CTMoveBookmark) POIXMLTypeLoader.newInstance(CTMoveBookmark.type, null);
        }

        public static CTMoveBookmark newInstance(XmlOptions xmlOptions) {
            return (CTMoveBookmark) POIXMLTypeLoader.newInstance(CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(String str) throws XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(str, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(str, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(File file) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(file, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(file, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(URL url) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(url, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(url, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(InputStream inputStream) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(inputStream, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(inputStream, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(Reader reader) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(reader, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(reader, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(xMLStreamReader, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(xMLStreamReader, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(Node node) throws XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(node, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(node, CTMoveBookmark.type, xmlOptions);
        }

        public static CTMoveBookmark parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(xMLInputStream, CTMoveBookmark.type, (XmlOptions) null);
        }

        public static CTMoveBookmark parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTMoveBookmark) POIXMLTypeLoader.parse(xMLInputStream, CTMoveBookmark.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMoveBookmark.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMoveBookmark.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getAuthor();

    STString xgetAuthor();

    void setAuthor(String str);

    void xsetAuthor(STString sTString);

    Calendar getDate();

    STDateTime xgetDate();

    void setDate(Calendar calendar);

    void xsetDate(STDateTime sTDateTime);
}
