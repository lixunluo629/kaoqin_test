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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPercentage.class */
public interface CTPercentage extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPercentage.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpercentage4e75type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPercentage$Factory.class */
    public static final class Factory {
        public static CTPercentage newInstance() {
            return (CTPercentage) POIXMLTypeLoader.newInstance(CTPercentage.type, null);
        }

        public static CTPercentage newInstance(XmlOptions xmlOptions) {
            return (CTPercentage) POIXMLTypeLoader.newInstance(CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(String str) throws XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(str, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(str, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(File file) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(file, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(file, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(URL url) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(url, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(url, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(inputStream, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(inputStream, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(Reader reader) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(reader, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPercentage) POIXMLTypeLoader.parse(reader, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(xMLStreamReader, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(xMLStreamReader, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(Node node) throws XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(node, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(node, CTPercentage.type, xmlOptions);
        }

        public static CTPercentage parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(xMLInputStream, CTPercentage.type, (XmlOptions) null);
        }

        public static CTPercentage parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPercentage) POIXMLTypeLoader.parse(xMLInputStream, CTPercentage.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPercentage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPercentage.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getVal();

    STPercentage xgetVal();

    void setVal(int i);

    void xsetVal(STPercentage sTPercentage);
}
