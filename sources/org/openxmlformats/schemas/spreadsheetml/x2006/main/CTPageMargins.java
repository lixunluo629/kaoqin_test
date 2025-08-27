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
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageMargins.class */
public interface CTPageMargins extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPageMargins.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpagemargins5455type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageMargins$Factory.class */
    public static final class Factory {
        public static CTPageMargins newInstance() {
            return (CTPageMargins) POIXMLTypeLoader.newInstance(CTPageMargins.type, null);
        }

        public static CTPageMargins newInstance(XmlOptions xmlOptions) {
            return (CTPageMargins) POIXMLTypeLoader.newInstance(CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(String str) throws XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(str, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(str, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(File file) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(file, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(file, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(URL url) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(url, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(url, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(inputStream, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(inputStream, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(Reader reader) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(reader, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageMargins) POIXMLTypeLoader.parse(reader, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(xMLStreamReader, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(xMLStreamReader, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(Node node) throws XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(node, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(node, CTPageMargins.type, xmlOptions);
        }

        public static CTPageMargins parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(xMLInputStream, CTPageMargins.type, (XmlOptions) null);
        }

        public static CTPageMargins parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPageMargins) POIXMLTypeLoader.parse(xMLInputStream, CTPageMargins.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageMargins.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageMargins.type, xmlOptions);
        }

        private Factory() {
        }
    }

    double getLeft();

    XmlDouble xgetLeft();

    void setLeft(double d);

    void xsetLeft(XmlDouble xmlDouble);

    double getRight();

    XmlDouble xgetRight();

    void setRight(double d);

    void xsetRight(XmlDouble xmlDouble);

    double getTop();

    XmlDouble xgetTop();

    void setTop(double d);

    void xsetTop(XmlDouble xmlDouble);

    double getBottom();

    XmlDouble xgetBottom();

    void setBottom(double d);

    void xsetBottom(XmlDouble xmlDouble);

    double getHeader();

    XmlDouble xgetHeader();

    void setHeader(double d);

    void xsetHeader(XmlDouble xmlDouble);

    double getFooter();

    XmlDouble xgetFooter();

    void setFooter(double d);

    void xsetFooter(XmlDouble xmlDouble);
}
