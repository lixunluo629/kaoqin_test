package org.openxmlformats.schemas.drawingml.x2006.chart;

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
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTTickMark.class */
public interface CTTickMark extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTickMark.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttickmarke7f2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTTickMark$Factory.class */
    public static final class Factory {
        public static CTTickMark newInstance() {
            return (CTTickMark) POIXMLTypeLoader.newInstance(CTTickMark.type, null);
        }

        public static CTTickMark newInstance(XmlOptions xmlOptions) {
            return (CTTickMark) POIXMLTypeLoader.newInstance(CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(String str) throws XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(str, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(str, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(File file) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(file, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(file, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(URL url) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(url, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(url, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(inputStream, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(inputStream, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(Reader reader) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(reader, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTickMark) POIXMLTypeLoader.parse(reader, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(xMLStreamReader, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(xMLStreamReader, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(Node node) throws XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(node, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(node, CTTickMark.type, xmlOptions);
        }

        public static CTTickMark parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(xMLInputStream, CTTickMark.type, (XmlOptions) null);
        }

        public static CTTickMark parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTickMark) POIXMLTypeLoader.parse(xMLInputStream, CTTickMark.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTickMark.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTickMark.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STTickMark.Enum getVal();

    STTickMark xgetVal();

    boolean isSetVal();

    void setVal(STTickMark.Enum r1);

    void xsetVal(STTickMark sTTickMark);

    void unsetVal();
}
