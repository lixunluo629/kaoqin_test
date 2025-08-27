package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBorders.class */
public interface CTBorders extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBorders.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctborders0d66type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBorders$Factory.class */
    public static final class Factory {
        public static CTBorders newInstance() {
            return (CTBorders) POIXMLTypeLoader.newInstance(CTBorders.type, null);
        }

        public static CTBorders newInstance(XmlOptions xmlOptions) {
            return (CTBorders) POIXMLTypeLoader.newInstance(CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(String str) throws XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(str, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(str, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(File file) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(file, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(file, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(URL url) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(url, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(url, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(inputStream, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(inputStream, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(Reader reader) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(reader, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorders) POIXMLTypeLoader.parse(reader, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(xMLStreamReader, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(xMLStreamReader, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(Node node) throws XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(node, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(node, CTBorders.type, xmlOptions);
        }

        public static CTBorders parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(xMLInputStream, CTBorders.type, (XmlOptions) null);
        }

        public static CTBorders parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBorders) POIXMLTypeLoader.parse(xMLInputStream, CTBorders.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBorders.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBorders.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTBorder> getBorderList();

    CTBorder[] getBorderArray();

    CTBorder getBorderArray(int i);

    int sizeOfBorderArray();

    void setBorderArray(CTBorder[] cTBorderArr);

    void setBorderArray(int i, CTBorder cTBorder);

    CTBorder insertNewBorder(int i);

    CTBorder addNewBorder();

    void removeBorder(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
