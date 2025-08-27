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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPatternFill.class */
public interface CTPatternFill extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPatternFill.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpatternfill7452type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPatternFill$Factory.class */
    public static final class Factory {
        public static CTPatternFill newInstance() {
            return (CTPatternFill) POIXMLTypeLoader.newInstance(CTPatternFill.type, null);
        }

        public static CTPatternFill newInstance(XmlOptions xmlOptions) {
            return (CTPatternFill) POIXMLTypeLoader.newInstance(CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(String str) throws XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(str, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(str, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(File file) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(file, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(file, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(URL url) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(url, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(url, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(inputStream, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(inputStream, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(Reader reader) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(reader, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFill) POIXMLTypeLoader.parse(reader, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(xMLStreamReader, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(xMLStreamReader, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(Node node) throws XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(node, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(node, CTPatternFill.type, xmlOptions);
        }

        public static CTPatternFill parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(xMLInputStream, CTPatternFill.type, (XmlOptions) null);
        }

        public static CTPatternFill parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPatternFill) POIXMLTypeLoader.parse(xMLInputStream, CTPatternFill.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPatternFill.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPatternFill.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTColor getFgColor();

    boolean isSetFgColor();

    void setFgColor(CTColor cTColor);

    CTColor addNewFgColor();

    void unsetFgColor();

    CTColor getBgColor();

    boolean isSetBgColor();

    void setBgColor(CTColor cTColor);

    CTColor addNewBgColor();

    void unsetBgColor();

    STPatternType.Enum getPatternType();

    STPatternType xgetPatternType();

    boolean isSetPatternType();

    void setPatternType(STPatternType.Enum r1);

    void xsetPatternType(STPatternType sTPatternType);

    void unsetPatternType();
}
