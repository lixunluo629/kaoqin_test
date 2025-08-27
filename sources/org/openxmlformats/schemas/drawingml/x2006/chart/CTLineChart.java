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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTLineChart.class */
public interface CTLineChart extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLineChart.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlinechart249ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTLineChart$Factory.class */
    public static final class Factory {
        public static CTLineChart newInstance() {
            return (CTLineChart) POIXMLTypeLoader.newInstance(CTLineChart.type, null);
        }

        public static CTLineChart newInstance(XmlOptions xmlOptions) {
            return (CTLineChart) POIXMLTypeLoader.newInstance(CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(String str) throws XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(str, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(str, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(File file) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(file, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(file, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(URL url) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(url, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(url, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(inputStream, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(inputStream, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(Reader reader) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(reader, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineChart) POIXMLTypeLoader.parse(reader, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(xMLStreamReader, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(xMLStreamReader, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(Node node) throws XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(node, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(node, CTLineChart.type, xmlOptions);
        }

        public static CTLineChart parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(xMLInputStream, CTLineChart.type, (XmlOptions) null);
        }

        public static CTLineChart parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLineChart) POIXMLTypeLoader.parse(xMLInputStream, CTLineChart.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineChart.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineChart.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTGrouping getGrouping();

    void setGrouping(CTGrouping cTGrouping);

    CTGrouping addNewGrouping();

    CTBoolean getVaryColors();

    boolean isSetVaryColors();

    void setVaryColors(CTBoolean cTBoolean);

    CTBoolean addNewVaryColors();

    void unsetVaryColors();

    List<CTLineSer> getSerList();

    CTLineSer[] getSerArray();

    CTLineSer getSerArray(int i);

    int sizeOfSerArray();

    void setSerArray(CTLineSer[] cTLineSerArr);

    void setSerArray(int i, CTLineSer cTLineSer);

    CTLineSer insertNewSer(int i);

    CTLineSer addNewSer();

    void removeSer(int i);

    CTDLbls getDLbls();

    boolean isSetDLbls();

    void setDLbls(CTDLbls cTDLbls);

    CTDLbls addNewDLbls();

    void unsetDLbls();

    CTChartLines getDropLines();

    boolean isSetDropLines();

    void setDropLines(CTChartLines cTChartLines);

    CTChartLines addNewDropLines();

    void unsetDropLines();

    CTChartLines getHiLowLines();

    boolean isSetHiLowLines();

    void setHiLowLines(CTChartLines cTChartLines);

    CTChartLines addNewHiLowLines();

    void unsetHiLowLines();

    CTUpDownBars getUpDownBars();

    boolean isSetUpDownBars();

    void setUpDownBars(CTUpDownBars cTUpDownBars);

    CTUpDownBars addNewUpDownBars();

    void unsetUpDownBars();

    CTBoolean getMarker();

    boolean isSetMarker();

    void setMarker(CTBoolean cTBoolean);

    CTBoolean addNewMarker();

    void unsetMarker();

    CTBoolean getSmooth();

    boolean isSetSmooth();

    void setSmooth(CTBoolean cTBoolean);

    CTBoolean addNewSmooth();

    void unsetSmooth();

    List<CTUnsignedInt> getAxIdList();

    CTUnsignedInt[] getAxIdArray();

    CTUnsignedInt getAxIdArray(int i);

    int sizeOfAxIdArray();

    void setAxIdArray(CTUnsignedInt[] cTUnsignedIntArr);

    void setAxIdArray(int i, CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt insertNewAxId(int i);

    CTUnsignedInt addNewAxId();

    void removeAxId(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
