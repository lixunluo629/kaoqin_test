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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextTabStop.class */
public interface CTTextTabStop extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextTabStop.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttexttabstopb57btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextTabStop$Factory.class */
    public static final class Factory {
        public static CTTextTabStop newInstance() {
            return (CTTextTabStop) POIXMLTypeLoader.newInstance(CTTextTabStop.type, null);
        }

        public static CTTextTabStop newInstance(XmlOptions xmlOptions) {
            return (CTTextTabStop) POIXMLTypeLoader.newInstance(CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(String str) throws XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(str, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(str, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(File file) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(file, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(file, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(URL url) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(url, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(url, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(inputStream, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(inputStream, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(Reader reader) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(reader, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(reader, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(xMLStreamReader, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(xMLStreamReader, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(Node node) throws XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(node, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(node, CTTextTabStop.type, xmlOptions);
        }

        public static CTTextTabStop parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(xMLInputStream, CTTextTabStop.type, (XmlOptions) null);
        }

        public static CTTextTabStop parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextTabStop) POIXMLTypeLoader.parse(xMLInputStream, CTTextTabStop.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextTabStop.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextTabStop.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getPos();

    STCoordinate32 xgetPos();

    boolean isSetPos();

    void setPos(int i);

    void xsetPos(STCoordinate32 sTCoordinate32);

    void unsetPos();

    STTextTabAlignType$Enum getAlgn();

    STTextTabAlignType xgetAlgn();

    boolean isSetAlgn();

    void setAlgn(STTextTabAlignType$Enum sTTextTabAlignType$Enum);

    void xsetAlgn(STTextTabAlignType sTTextTabAlignType);

    void unsetAlgn();
}
