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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTRelativeRect.class */
public interface CTRelativeRect extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRelativeRect.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrelativerecta4ebtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTRelativeRect$Factory.class */
    public static final class Factory {
        public static CTRelativeRect newInstance() {
            return (CTRelativeRect) POIXMLTypeLoader.newInstance(CTRelativeRect.type, null);
        }

        public static CTRelativeRect newInstance(XmlOptions xmlOptions) {
            return (CTRelativeRect) POIXMLTypeLoader.newInstance(CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(String str) throws XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(str, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(str, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(File file) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(file, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(file, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(URL url) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(url, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(url, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(inputStream, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(inputStream, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(Reader reader) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(reader, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(reader, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(xMLStreamReader, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(xMLStreamReader, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(Node node) throws XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(node, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(node, CTRelativeRect.type, xmlOptions);
        }

        public static CTRelativeRect parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(xMLInputStream, CTRelativeRect.type, (XmlOptions) null);
        }

        public static CTRelativeRect parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRelativeRect) POIXMLTypeLoader.parse(xMLInputStream, CTRelativeRect.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRelativeRect.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRelativeRect.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getL();

    STPercentage xgetL();

    boolean isSetL();

    void setL(int i);

    void xsetL(STPercentage sTPercentage);

    void unsetL();

    int getT();

    STPercentage xgetT();

    boolean isSetT();

    void setT(int i);

    void xsetT(STPercentage sTPercentage);

    void unsetT();

    int getR();

    STPercentage xgetR();

    boolean isSetR();

    void setR(int i);

    void xsetR(STPercentage sTPercentage);

    void unsetR();

    int getB();

    STPercentage xgetB();

    boolean isSetB();

    void setB(int i);

    void xsetB(STPercentage sTPercentage);

    void unsetB();
}
