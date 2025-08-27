package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTcBorders.class */
public interface CTTcBorders extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTcBorders.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttcbordersa5fatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTcBorders$Factory.class */
    public static final class Factory {
        public static CTTcBorders newInstance() {
            return (CTTcBorders) POIXMLTypeLoader.newInstance(CTTcBorders.type, null);
        }

        public static CTTcBorders newInstance(XmlOptions xmlOptions) {
            return (CTTcBorders) POIXMLTypeLoader.newInstance(CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(String str) throws XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(str, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(str, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(File file) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(file, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(file, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(URL url) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(url, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(url, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(inputStream, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(inputStream, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(Reader reader) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(reader, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcBorders) POIXMLTypeLoader.parse(reader, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(xMLStreamReader, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(xMLStreamReader, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(Node node) throws XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(node, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(node, CTTcBorders.type, xmlOptions);
        }

        public static CTTcBorders parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(xMLInputStream, CTTcBorders.type, (XmlOptions) null);
        }

        public static CTTcBorders parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTcBorders) POIXMLTypeLoader.parse(xMLInputStream, CTTcBorders.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTcBorders.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTcBorders.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTBorder getTop();

    boolean isSetTop();

    void setTop(CTBorder cTBorder);

    CTBorder addNewTop();

    void unsetTop();

    CTBorder getLeft();

    boolean isSetLeft();

    void setLeft(CTBorder cTBorder);

    CTBorder addNewLeft();

    void unsetLeft();

    CTBorder getBottom();

    boolean isSetBottom();

    void setBottom(CTBorder cTBorder);

    CTBorder addNewBottom();

    void unsetBottom();

    CTBorder getRight();

    boolean isSetRight();

    void setRight(CTBorder cTBorder);

    CTBorder addNewRight();

    void unsetRight();

    CTBorder getInsideH();

    boolean isSetInsideH();

    void setInsideH(CTBorder cTBorder);

    CTBorder addNewInsideH();

    void unsetInsideH();

    CTBorder getInsideV();

    boolean isSetInsideV();

    void setInsideV(CTBorder cTBorder);

    CTBorder addNewInsideV();

    void unsetInsideV();

    CTBorder getTl2Br();

    boolean isSetTl2Br();

    void setTl2Br(CTBorder cTBorder);

    CTBorder addNewTl2Br();

    void unsetTl2Br();

    CTBorder getTr2Bl();

    boolean isSetTr2Bl();

    void setTr2Bl(CTBorder cTBorder);

    CTBorder addNewTr2Bl();

    void unsetTr2Bl();
}
