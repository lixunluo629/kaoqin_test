package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBorderPr.class */
public interface CTBorderPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBorderPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctborderpre497type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBorderPr$Factory.class */
    public static final class Factory {
        public static CTBorderPr newInstance() {
            return (CTBorderPr) POIXMLTypeLoader.newInstance(CTBorderPr.type, null);
        }

        public static CTBorderPr newInstance(XmlOptions xmlOptions) {
            return (CTBorderPr) POIXMLTypeLoader.newInstance(CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(String str) throws XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(str, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(str, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(File file) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(file, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(file, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(URL url) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(url, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(url, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(inputStream, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(inputStream, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(Reader reader) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(reader, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorderPr) POIXMLTypeLoader.parse(reader, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(xMLStreamReader, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(xMLStreamReader, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(Node node) throws XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(node, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(node, CTBorderPr.type, xmlOptions);
        }

        public static CTBorderPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(xMLInputStream, CTBorderPr.type, (XmlOptions) null);
        }

        public static CTBorderPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBorderPr) POIXMLTypeLoader.parse(xMLInputStream, CTBorderPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBorderPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBorderPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTColor getColor();

    boolean isSetColor();

    void setColor(CTColor cTColor);

    CTColor addNewColor();

    void unsetColor();

    STBorderStyle.Enum getStyle();

    STBorderStyle xgetStyle();

    boolean isSetStyle();

    void setStyle(STBorderStyle.Enum r1);

    void xsetStyle(STBorderStyle sTBorderStyle);

    void unsetStyle();
}
