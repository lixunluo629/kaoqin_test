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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageSetUpPr.class */
public interface CTPageSetUpPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPageSetUpPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpagesetuppr24cftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageSetUpPr$Factory.class */
    public static final class Factory {
        public static CTPageSetUpPr newInstance() {
            return (CTPageSetUpPr) POIXMLTypeLoader.newInstance(CTPageSetUpPr.type, null);
        }

        public static CTPageSetUpPr newInstance(XmlOptions xmlOptions) {
            return (CTPageSetUpPr) POIXMLTypeLoader.newInstance(CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(String str) throws XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(str, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(str, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(File file) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(file, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(file, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(URL url) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(url, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(url, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(inputStream, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(inputStream, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(Reader reader) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(reader, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(reader, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(xMLStreamReader, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(xMLStreamReader, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(Node node) throws XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(node, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(node, CTPageSetUpPr.type, xmlOptions);
        }

        public static CTPageSetUpPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(xMLInputStream, CTPageSetUpPr.type, (XmlOptions) null);
        }

        public static CTPageSetUpPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPageSetUpPr) POIXMLTypeLoader.parse(xMLInputStream, CTPageSetUpPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageSetUpPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageSetUpPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    boolean getAutoPageBreaks();

    XmlBoolean xgetAutoPageBreaks();

    boolean isSetAutoPageBreaks();

    void setAutoPageBreaks(boolean z);

    void xsetAutoPageBreaks(XmlBoolean xmlBoolean);

    void unsetAutoPageBreaks();

    boolean getFitToPage();

    XmlBoolean xgetFitToPage();

    boolean isSetFitToPage();

    void setFitToPage(boolean z);

    void xsetFitToPage(XmlBoolean xmlBoolean);

    void unsetFitToPage();
}
