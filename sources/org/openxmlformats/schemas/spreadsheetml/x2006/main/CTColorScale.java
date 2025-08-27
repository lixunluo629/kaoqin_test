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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColorScale.class */
public interface CTColorScale extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTColorScale.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcolorscale1a70type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColorScale$Factory.class */
    public static final class Factory {
        public static CTColorScale newInstance() {
            return (CTColorScale) POIXMLTypeLoader.newInstance(CTColorScale.type, null);
        }

        public static CTColorScale newInstance(XmlOptions xmlOptions) {
            return (CTColorScale) POIXMLTypeLoader.newInstance(CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(String str) throws XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(str, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(str, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(File file) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(file, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(file, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(URL url) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(url, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(url, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(InputStream inputStream) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(inputStream, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(inputStream, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(Reader reader) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(reader, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScale) POIXMLTypeLoader.parse(reader, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(xMLStreamReader, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(xMLStreamReader, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(Node node) throws XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(node, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(node, CTColorScale.type, xmlOptions);
        }

        public static CTColorScale parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(xMLInputStream, CTColorScale.type, (XmlOptions) null);
        }

        public static CTColorScale parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTColorScale) POIXMLTypeLoader.parse(xMLInputStream, CTColorScale.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColorScale.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColorScale.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCfvo> getCfvoList();

    CTCfvo[] getCfvoArray();

    CTCfvo getCfvoArray(int i);

    int sizeOfCfvoArray();

    void setCfvoArray(CTCfvo[] cTCfvoArr);

    void setCfvoArray(int i, CTCfvo cTCfvo);

    CTCfvo insertNewCfvo(int i);

    CTCfvo addNewCfvo();

    void removeCfvo(int i);

    List<CTColor> getColorList();

    CTColor[] getColorArray();

    CTColor getColorArray(int i);

    int sizeOfColorArray();

    void setColorArray(CTColor[] cTColorArr);

    void setColorArray(int i, CTColor cTColor);

    CTColor insertNewColor(int i);

    CTColor addNewColor();

    void removeColor(int i);
}
