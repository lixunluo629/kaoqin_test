package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPositivePercentage.class */
public interface CTPositivePercentage extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPositivePercentage.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpositivepercentage2f8etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPositivePercentage$Factory.class */
    public static final class Factory {
        public static CTPositivePercentage newInstance() {
            return (CTPositivePercentage) POIXMLTypeLoader.newInstance(CTPositivePercentage.type, null);
        }

        public static CTPositivePercentage newInstance(XmlOptions xmlOptions) {
            return (CTPositivePercentage) POIXMLTypeLoader.newInstance(CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(String str) throws XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(str, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(str, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(File file) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(file, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(file, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(URL url) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(url, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(url, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(inputStream, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(inputStream, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(Reader reader) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(reader, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(reader, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(xMLStreamReader, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(xMLStreamReader, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(Node node) throws XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(node, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(node, CTPositivePercentage.type, xmlOptions);
        }

        public static CTPositivePercentage parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(xMLInputStream, CTPositivePercentage.type, (XmlOptions) null);
        }

        public static CTPositivePercentage parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPositivePercentage) POIXMLTypeLoader.parse(xMLInputStream, CTPositivePercentage.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPositivePercentage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPositivePercentage.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getVal();

    STPositivePercentage xgetVal();

    void setVal(int i);

    void xsetVal(STPositivePercentage sTPositivePercentage);
}
