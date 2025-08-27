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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTAdjPoint2D.class */
public interface CTAdjPoint2D extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTAdjPoint2D.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctadjpoint2d1656type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTAdjPoint2D$Factory.class */
    public static final class Factory {
        public static CTAdjPoint2D newInstance() {
            return (CTAdjPoint2D) POIXMLTypeLoader.newInstance(CTAdjPoint2D.type, null);
        }

        public static CTAdjPoint2D newInstance(XmlOptions xmlOptions) {
            return (CTAdjPoint2D) POIXMLTypeLoader.newInstance(CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(String str) throws XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(str, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(str, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(File file) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(file, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(file, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(URL url) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(url, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(url, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(InputStream inputStream) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(inputStream, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(inputStream, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(Reader reader) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(reader, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(reader, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(xMLStreamReader, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(xMLStreamReader, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(Node node) throws XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(node, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(node, CTAdjPoint2D.type, xmlOptions);
        }

        public static CTAdjPoint2D parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(xMLInputStream, CTAdjPoint2D.type, (XmlOptions) null);
        }

        public static CTAdjPoint2D parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTAdjPoint2D) POIXMLTypeLoader.parse(xMLInputStream, CTAdjPoint2D.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAdjPoint2D.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAdjPoint2D.type, xmlOptions);
        }

        private Factory() {
        }
    }

    Object getX();

    STAdjCoordinate xgetX();

    void setX(Object obj);

    void xsetX(STAdjCoordinate sTAdjCoordinate);

    Object getY();

    STAdjCoordinate xgetY();

    void setY(Object obj);

    void xsetY(STAdjCoordinate sTAdjCoordinate);
}
