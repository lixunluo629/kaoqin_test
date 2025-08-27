package org.openxmlformats.schemas.presentationml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTCommentAuthorList.class */
public interface CTCommentAuthorList extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCommentAuthorList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcommentauthorlisteb07type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTCommentAuthorList$Factory.class */
    public static final class Factory {
        public static CTCommentAuthorList newInstance() {
            return (CTCommentAuthorList) POIXMLTypeLoader.newInstance(CTCommentAuthorList.type, null);
        }

        public static CTCommentAuthorList newInstance(XmlOptions xmlOptions) {
            return (CTCommentAuthorList) POIXMLTypeLoader.newInstance(CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(String str) throws XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(str, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(str, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(File file) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(file, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(file, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(URL url) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(url, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(url, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(inputStream, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(inputStream, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(Reader reader) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(reader, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(reader, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(xMLStreamReader, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(xMLStreamReader, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(Node node) throws XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(node, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(node, CTCommentAuthorList.type, xmlOptions);
        }

        public static CTCommentAuthorList parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(xMLInputStream, CTCommentAuthorList.type, (XmlOptions) null);
        }

        public static CTCommentAuthorList parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCommentAuthorList) POIXMLTypeLoader.parse(xMLInputStream, CTCommentAuthorList.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCommentAuthorList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCommentAuthorList.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCommentAuthor> getCmAuthorList();

    CTCommentAuthor[] getCmAuthorArray();

    CTCommentAuthor getCmAuthorArray(int i);

    int sizeOfCmAuthorArray();

    void setCmAuthorArray(CTCommentAuthor[] cTCommentAuthorArr);

    void setCmAuthorArray(int i, CTCommentAuthor cTCommentAuthor);

    CTCommentAuthor insertNewCmAuthor(int i);

    CTCommentAuthor addNewCmAuthor();

    void removeCmAuthor(int i);
}
