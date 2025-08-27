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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGradientStop.class */
public interface CTGradientStop extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGradientStop.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgradientstopc7edtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGradientStop$Factory.class */
    public static final class Factory {
        public static CTGradientStop newInstance() {
            return (CTGradientStop) POIXMLTypeLoader.newInstance(CTGradientStop.type, null);
        }

        public static CTGradientStop newInstance(XmlOptions xmlOptions) {
            return (CTGradientStop) POIXMLTypeLoader.newInstance(CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(String str) throws XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(str, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(str, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(File file) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(file, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(file, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(URL url) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(url, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(url, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(inputStream, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(inputStream, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(Reader reader) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(reader, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientStop) POIXMLTypeLoader.parse(reader, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(xMLStreamReader, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(xMLStreamReader, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(Node node) throws XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(node, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(node, CTGradientStop.type, xmlOptions);
        }

        public static CTGradientStop parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(xMLInputStream, CTGradientStop.type, (XmlOptions) null);
        }

        public static CTGradientStop parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGradientStop) POIXMLTypeLoader.parse(xMLInputStream, CTGradientStop.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGradientStop.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGradientStop.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTScRgbColor getScrgbClr();

    boolean isSetScrgbClr();

    void setScrgbClr(CTScRgbColor cTScRgbColor);

    CTScRgbColor addNewScrgbClr();

    void unsetScrgbClr();

    CTSRgbColor getSrgbClr();

    boolean isSetSrgbClr();

    void setSrgbClr(CTSRgbColor cTSRgbColor);

    CTSRgbColor addNewSrgbClr();

    void unsetSrgbClr();

    CTHslColor getHslClr();

    boolean isSetHslClr();

    void setHslClr(CTHslColor cTHslColor);

    CTHslColor addNewHslClr();

    void unsetHslClr();

    CTSystemColor getSysClr();

    boolean isSetSysClr();

    void setSysClr(CTSystemColor cTSystemColor);

    CTSystemColor addNewSysClr();

    void unsetSysClr();

    CTSchemeColor getSchemeClr();

    boolean isSetSchemeClr();

    void setSchemeClr(CTSchemeColor cTSchemeColor);

    CTSchemeColor addNewSchemeClr();

    void unsetSchemeClr();

    CTPresetColor getPrstClr();

    boolean isSetPrstClr();

    void setPrstClr(CTPresetColor cTPresetColor);

    CTPresetColor addNewPrstClr();

    void unsetPrstClr();

    int getPos();

    STPositiveFixedPercentage xgetPos();

    void setPos(int i);

    void xsetPos(STPositiveFixedPercentage sTPositiveFixedPercentage);
}
