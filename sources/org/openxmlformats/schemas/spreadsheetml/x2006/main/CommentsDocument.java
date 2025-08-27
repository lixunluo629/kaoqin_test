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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CommentsDocument.class */
public interface CommentsDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CommentsDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("comments4c11doctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CommentsDocument$Factory.class */
    public static final class Factory {
        public static CommentsDocument newInstance() {
            return (CommentsDocument) POIXMLTypeLoader.newInstance(CommentsDocument.type, null);
        }

        public static CommentsDocument newInstance(XmlOptions xmlOptions) {
            return (CommentsDocument) POIXMLTypeLoader.newInstance(CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(String str) throws XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(str, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(str, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(File file) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(file, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(file, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(URL url) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(url, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(url, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(inputStream, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(inputStream, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(Reader reader) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(reader, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CommentsDocument) POIXMLTypeLoader.parse(reader, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(xMLStreamReader, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(xMLStreamReader, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(Node node) throws XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(node, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(node, CommentsDocument.type, xmlOptions);
        }

        public static CommentsDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(xMLInputStream, CommentsDocument.type, (XmlOptions) null);
        }

        public static CommentsDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CommentsDocument) POIXMLTypeLoader.parse(xMLInputStream, CommentsDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CommentsDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CommentsDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTComments getComments();

    void setComments(CTComments cTComments);

    CTComments addNewComments();
}
