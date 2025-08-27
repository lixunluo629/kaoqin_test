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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontScheme;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTFontScheme.class */
public interface CTFontScheme extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFontScheme.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfontschemebf5dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTFontScheme$Factory.class */
    public static final class Factory {
        public static CTFontScheme newInstance() {
            return (CTFontScheme) POIXMLTypeLoader.newInstance(CTFontScheme.type, null);
        }

        public static CTFontScheme newInstance(XmlOptions xmlOptions) {
            return (CTFontScheme) POIXMLTypeLoader.newInstance(CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(String str) throws XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(str, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(str, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(File file) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(file, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(file, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(URL url) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(url, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(url, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(inputStream, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(inputStream, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(Reader reader) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(reader, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontScheme) POIXMLTypeLoader.parse(reader, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(xMLStreamReader, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(xMLStreamReader, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(Node node) throws XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(node, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(node, CTFontScheme.type, xmlOptions);
        }

        public static CTFontScheme parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(xMLInputStream, CTFontScheme.type, (XmlOptions) null);
        }

        public static CTFontScheme parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFontScheme) POIXMLTypeLoader.parse(xMLInputStream, CTFontScheme.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFontScheme.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFontScheme.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STFontScheme.Enum getVal();

    STFontScheme xgetVal();

    void setVal(STFontScheme.Enum r1);

    void xsetVal(STFontScheme sTFontScheme);
}
