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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblBorders.class */
public interface CTTblBorders extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTblBorders.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttblborders459ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblBorders$Factory.class */
    public static final class Factory {
        public static CTTblBorders newInstance() {
            return (CTTblBorders) POIXMLTypeLoader.newInstance(CTTblBorders.type, null);
        }

        public static CTTblBorders newInstance(XmlOptions xmlOptions) {
            return (CTTblBorders) POIXMLTypeLoader.newInstance(CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(String str) throws XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(str, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(str, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(File file) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(file, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(file, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(URL url) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(url, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(url, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(inputStream, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(inputStream, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(Reader reader) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(reader, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblBorders) POIXMLTypeLoader.parse(reader, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(xMLStreamReader, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(xMLStreamReader, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(Node node) throws XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(node, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(node, CTTblBorders.type, xmlOptions);
        }

        public static CTTblBorders parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(xMLInputStream, CTTblBorders.type, (XmlOptions) null);
        }

        public static CTTblBorders parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTblBorders) POIXMLTypeLoader.parse(xMLInputStream, CTTblBorders.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblBorders.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblBorders.type, xmlOptions);
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
}
