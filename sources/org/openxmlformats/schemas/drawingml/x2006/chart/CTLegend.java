package org.openxmlformats.schemas.drawingml.x2006.chart;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTLegend.class */
public interface CTLegend extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLegend.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlegenda54ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTLegend$Factory.class */
    public static final class Factory {
        public static CTLegend newInstance() {
            return (CTLegend) POIXMLTypeLoader.newInstance(CTLegend.type, null);
        }

        public static CTLegend newInstance(XmlOptions xmlOptions) {
            return (CTLegend) POIXMLTypeLoader.newInstance(CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(String str) throws XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(str, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(str, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(File file) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(file, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(file, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(URL url) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(url, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(url, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(inputStream, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(inputStream, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(Reader reader) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(reader, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLegend) POIXMLTypeLoader.parse(reader, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(xMLStreamReader, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(xMLStreamReader, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(Node node) throws XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(node, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(node, CTLegend.type, xmlOptions);
        }

        public static CTLegend parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(xMLInputStream, CTLegend.type, (XmlOptions) null);
        }

        public static CTLegend parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLegend) POIXMLTypeLoader.parse(xMLInputStream, CTLegend.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLegend.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLegend.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLegendPos getLegendPos();

    boolean isSetLegendPos();

    void setLegendPos(CTLegendPos cTLegendPos);

    CTLegendPos addNewLegendPos();

    void unsetLegendPos();

    List<CTLegendEntry> getLegendEntryList();

    CTLegendEntry[] getLegendEntryArray();

    CTLegendEntry getLegendEntryArray(int i);

    int sizeOfLegendEntryArray();

    void setLegendEntryArray(CTLegendEntry[] cTLegendEntryArr);

    void setLegendEntryArray(int i, CTLegendEntry cTLegendEntry);

    CTLegendEntry insertNewLegendEntry(int i);

    CTLegendEntry addNewLegendEntry();

    void removeLegendEntry(int i);

    CTLayout getLayout();

    boolean isSetLayout();

    void setLayout(CTLayout cTLayout);

    CTLayout addNewLayout();

    void unsetLayout();

    CTBoolean getOverlay();

    boolean isSetOverlay();

    void setOverlay(CTBoolean cTBoolean);

    CTBoolean addNewOverlay();

    void unsetOverlay();

    CTShapeProperties getSpPr();

    boolean isSetSpPr();

    void setSpPr(CTShapeProperties cTShapeProperties);

    CTShapeProperties addNewSpPr();

    void unsetSpPr();

    CTTextBody getTxPr();

    boolean isSetTxPr();

    void setTxPr(CTTextBody cTTextBody);

    CTTextBody addNewTxPr();

    void unsetTxPr();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
