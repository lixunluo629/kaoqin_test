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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPositiveFixedPercentage.class */
public interface CTPositiveFixedPercentage extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPositiveFixedPercentage.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpositivefixedpercentage8966type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPositiveFixedPercentage$Factory.class */
    public static final class Factory {
        public static CTPositiveFixedPercentage newInstance() {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.newInstance(CTPositiveFixedPercentage.type, null);
        }

        public static CTPositiveFixedPercentage newInstance(XmlOptions xmlOptions) {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.newInstance(CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(String str) throws XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(str, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(str, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(File file) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(file, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(file, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(URL url) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(url, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(url, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(inputStream, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(inputStream, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(Reader reader) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(reader, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(reader, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(xMLStreamReader, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(xMLStreamReader, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(Node node) throws XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(node, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(node, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static CTPositiveFixedPercentage parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(xMLInputStream, CTPositiveFixedPercentage.type, (XmlOptions) null);
        }

        public static CTPositiveFixedPercentage parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPositiveFixedPercentage) POIXMLTypeLoader.parse(xMLInputStream, CTPositiveFixedPercentage.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPositiveFixedPercentage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPositiveFixedPercentage.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getVal();

    STPositiveFixedPercentage xgetVal();

    void setVal(int i);

    void xsetVal(STPositiveFixedPercentage sTPositiveFixedPercentage);
}
