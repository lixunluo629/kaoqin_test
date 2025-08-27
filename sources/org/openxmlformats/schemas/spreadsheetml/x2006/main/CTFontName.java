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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTFontName.class */
public interface CTFontName extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFontName.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfontname2dc3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTFontName$Factory.class */
    public static final class Factory {
        public static CTFontName newInstance() {
            return (CTFontName) POIXMLTypeLoader.newInstance(CTFontName.type, null);
        }

        public static CTFontName newInstance(XmlOptions xmlOptions) {
            return (CTFontName) POIXMLTypeLoader.newInstance(CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(String str) throws XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(str, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(str, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(File file) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(file, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(file, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(URL url) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(url, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(url, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(inputStream, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(inputStream, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(Reader reader) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(reader, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontName) POIXMLTypeLoader.parse(reader, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(xMLStreamReader, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(xMLStreamReader, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(Node node) throws XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(node, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(node, CTFontName.type, xmlOptions);
        }

        public static CTFontName parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(xMLInputStream, CTFontName.type, (XmlOptions) null);
        }

        public static CTFontName parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFontName) POIXMLTypeLoader.parse(xMLInputStream, CTFontName.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFontName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFontName.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getVal();

    STXstring xgetVal();

    void setVal(String str);

    void xsetVal(STXstring sTXstring);
}
