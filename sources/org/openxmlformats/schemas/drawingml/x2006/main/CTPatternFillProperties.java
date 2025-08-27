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
import org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPatternFillProperties.class */
public interface CTPatternFillProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPatternFillProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpatternfillproperties3637type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPatternFillProperties$Factory.class */
    public static final class Factory {
        public static CTPatternFillProperties newInstance() {
            return (CTPatternFillProperties) POIXMLTypeLoader.newInstance(CTPatternFillProperties.type, null);
        }

        public static CTPatternFillProperties newInstance(XmlOptions xmlOptions) {
            return (CTPatternFillProperties) POIXMLTypeLoader.newInstance(CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(String str) throws XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(str, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(str, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(File file) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(file, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(file, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(URL url) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(url, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(url, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(inputStream, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(inputStream, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(Reader reader) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(reader, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(reader, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(Node node) throws XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(node, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(node, CTPatternFillProperties.type, xmlOptions);
        }

        public static CTPatternFillProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(xMLInputStream, CTPatternFillProperties.type, (XmlOptions) null);
        }

        public static CTPatternFillProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPatternFillProperties) POIXMLTypeLoader.parse(xMLInputStream, CTPatternFillProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPatternFillProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPatternFillProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTColor getFgClr();

    boolean isSetFgClr();

    void setFgClr(CTColor cTColor);

    CTColor addNewFgClr();

    void unsetFgClr();

    CTColor getBgClr();

    boolean isSetBgClr();

    void setBgClr(CTColor cTColor);

    CTColor addNewBgClr();

    void unsetBgClr();

    STPresetPatternVal.Enum getPrst();

    STPresetPatternVal xgetPrst();

    boolean isSetPrst();

    void setPrst(STPresetPatternVal.Enum r1);

    void xsetPrst(STPresetPatternVal sTPresetPatternVal);

    void unsetPrst();
}
