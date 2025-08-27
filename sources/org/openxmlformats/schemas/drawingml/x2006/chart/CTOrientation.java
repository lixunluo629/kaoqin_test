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
import org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTOrientation.class */
public interface CTOrientation extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTOrientation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctorientationcb16type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTOrientation$Factory.class */
    public static final class Factory {
        public static CTOrientation newInstance() {
            return (CTOrientation) POIXMLTypeLoader.newInstance(CTOrientation.type, null);
        }

        public static CTOrientation newInstance(XmlOptions xmlOptions) {
            return (CTOrientation) POIXMLTypeLoader.newInstance(CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(String str) throws XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(str, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(str, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(File file) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(file, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(file, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(URL url) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(url, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(url, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(InputStream inputStream) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(inputStream, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(inputStream, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(Reader reader) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(reader, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOrientation) POIXMLTypeLoader.parse(reader, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(xMLStreamReader, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(xMLStreamReader, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(Node node) throws XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(node, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(node, CTOrientation.type, xmlOptions);
        }

        public static CTOrientation parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(xMLInputStream, CTOrientation.type, (XmlOptions) null);
        }

        public static CTOrientation parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTOrientation) POIXMLTypeLoader.parse(xMLInputStream, CTOrientation.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOrientation.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOrientation.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STOrientation.Enum getVal();

    STOrientation xgetVal();

    boolean isSetVal();

    void setVal(STOrientation.Enum r1);

    void xsetVal(STOrientation sTOrientation);

    void unsetVal();
}
