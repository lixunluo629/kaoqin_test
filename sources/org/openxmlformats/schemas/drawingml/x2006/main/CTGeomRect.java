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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGeomRect.class */
public interface CTGeomRect extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGeomRect.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgeomrect53dbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGeomRect$Factory.class */
    public static final class Factory {
        public static CTGeomRect newInstance() {
            return (CTGeomRect) POIXMLTypeLoader.newInstance(CTGeomRect.type, null);
        }

        public static CTGeomRect newInstance(XmlOptions xmlOptions) {
            return (CTGeomRect) POIXMLTypeLoader.newInstance(CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(String str) throws XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(str, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(str, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(File file) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(file, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(file, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(URL url) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(url, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(url, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(inputStream, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(inputStream, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(Reader reader) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(reader, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGeomRect) POIXMLTypeLoader.parse(reader, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(xMLStreamReader, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(xMLStreamReader, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(Node node) throws XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(node, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(node, CTGeomRect.type, xmlOptions);
        }

        public static CTGeomRect parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(xMLInputStream, CTGeomRect.type, (XmlOptions) null);
        }

        public static CTGeomRect parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGeomRect) POIXMLTypeLoader.parse(xMLInputStream, CTGeomRect.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGeomRect.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGeomRect.type, xmlOptions);
        }

        private Factory() {
        }
    }

    Object getL();

    STAdjCoordinate xgetL();

    void setL(Object obj);

    void xsetL(STAdjCoordinate sTAdjCoordinate);

    Object getT();

    STAdjCoordinate xgetT();

    void setT(Object obj);

    void xsetT(STAdjCoordinate sTAdjCoordinate);

    Object getR();

    STAdjCoordinate xgetR();

    void setR(Object obj);

    void xsetR(STAdjCoordinate sTAdjCoordinate);

    Object getB();

    STAdjCoordinate xgetB();

    void setB(Object obj);

    void xsetB(STAdjCoordinate sTAdjCoordinate);
}
