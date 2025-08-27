package com.microsoft.schemas.office.visio.x2012.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/PageContentsDocument.class */
public interface PageContentsDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(PageContentsDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("pagecontentsfc8bdoctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/PageContentsDocument$Factory.class */
    public static final class Factory {
        public static PageContentsDocument newInstance() {
            return (PageContentsDocument) POIXMLTypeLoader.newInstance(PageContentsDocument.type, null);
        }

        public static PageContentsDocument newInstance(XmlOptions xmlOptions) {
            return (PageContentsDocument) POIXMLTypeLoader.newInstance(PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(String str) throws XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(str, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(str, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(File file) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(file, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(file, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(URL url) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(url, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(url, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(inputStream, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(inputStream, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(Reader reader) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(reader, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(reader, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(xMLStreamReader, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(xMLStreamReader, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(Node node) throws XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(node, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(node, PageContentsDocument.type, xmlOptions);
        }

        public static PageContentsDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(xMLInputStream, PageContentsDocument.type, (XmlOptions) null);
        }

        public static PageContentsDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (PageContentsDocument) POIXMLTypeLoader.parse(xMLInputStream, PageContentsDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PageContentsDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PageContentsDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    PageContentsType getPageContents();

    void setPageContents(PageContentsType pageContentsType);

    PageContentsType addNewPageContents();
}
