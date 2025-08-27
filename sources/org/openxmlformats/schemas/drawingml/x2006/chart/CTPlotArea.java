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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTPlotArea.class */
public interface CTPlotArea extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPlotArea.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctplotarea106etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTPlotArea$Factory.class */
    public static final class Factory {
        public static CTPlotArea newInstance() {
            return (CTPlotArea) POIXMLTypeLoader.newInstance(CTPlotArea.type, null);
        }

        public static CTPlotArea newInstance(XmlOptions xmlOptions) {
            return (CTPlotArea) POIXMLTypeLoader.newInstance(CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(String str) throws XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(str, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(str, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(File file) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(file, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(file, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(URL url) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(url, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(url, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(inputStream, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(inputStream, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(Reader reader) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(reader, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlotArea) POIXMLTypeLoader.parse(reader, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(xMLStreamReader, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(xMLStreamReader, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(Node node) throws XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(node, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(node, CTPlotArea.type, xmlOptions);
        }

        public static CTPlotArea parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(xMLInputStream, CTPlotArea.type, (XmlOptions) null);
        }

        public static CTPlotArea parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPlotArea) POIXMLTypeLoader.parse(xMLInputStream, CTPlotArea.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPlotArea.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPlotArea.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLayout getLayout();

    boolean isSetLayout();

    void setLayout(CTLayout cTLayout);

    CTLayout addNewLayout();

    void unsetLayout();

    List<CTAreaChart> getAreaChartList();

    CTAreaChart[] getAreaChartArray();

    CTAreaChart getAreaChartArray(int i);

    int sizeOfAreaChartArray();

    void setAreaChartArray(CTAreaChart[] cTAreaChartArr);

    void setAreaChartArray(int i, CTAreaChart cTAreaChart);

    CTAreaChart insertNewAreaChart(int i);

    CTAreaChart addNewAreaChart();

    void removeAreaChart(int i);

    List<CTArea3DChart> getArea3DChartList();

    CTArea3DChart[] getArea3DChartArray();

    CTArea3DChart getArea3DChartArray(int i);

    int sizeOfArea3DChartArray();

    void setArea3DChartArray(CTArea3DChart[] cTArea3DChartArr);

    void setArea3DChartArray(int i, CTArea3DChart cTArea3DChart);

    CTArea3DChart insertNewArea3DChart(int i);

    CTArea3DChart addNewArea3DChart();

    void removeArea3DChart(int i);

    List<CTLineChart> getLineChartList();

    CTLineChart[] getLineChartArray();

    CTLineChart getLineChartArray(int i);

    int sizeOfLineChartArray();

    void setLineChartArray(CTLineChart[] cTLineChartArr);

    void setLineChartArray(int i, CTLineChart cTLineChart);

    CTLineChart insertNewLineChart(int i);

    CTLineChart addNewLineChart();

    void removeLineChart(int i);

    List<CTLine3DChart> getLine3DChartList();

    CTLine3DChart[] getLine3DChartArray();

    CTLine3DChart getLine3DChartArray(int i);

    int sizeOfLine3DChartArray();

    void setLine3DChartArray(CTLine3DChart[] cTLine3DChartArr);

    void setLine3DChartArray(int i, CTLine3DChart cTLine3DChart);

    CTLine3DChart insertNewLine3DChart(int i);

    CTLine3DChart addNewLine3DChart();

    void removeLine3DChart(int i);

    List<CTStockChart> getStockChartList();

    CTStockChart[] getStockChartArray();

    CTStockChart getStockChartArray(int i);

    int sizeOfStockChartArray();

    void setStockChartArray(CTStockChart[] cTStockChartArr);

    void setStockChartArray(int i, CTStockChart cTStockChart);

    CTStockChart insertNewStockChart(int i);

    CTStockChart addNewStockChart();

    void removeStockChart(int i);

    List<CTRadarChart> getRadarChartList();

    CTRadarChart[] getRadarChartArray();

    CTRadarChart getRadarChartArray(int i);

    int sizeOfRadarChartArray();

    void setRadarChartArray(CTRadarChart[] cTRadarChartArr);

    void setRadarChartArray(int i, CTRadarChart cTRadarChart);

    CTRadarChart insertNewRadarChart(int i);

    CTRadarChart addNewRadarChart();

    void removeRadarChart(int i);

    List<CTScatterChart> getScatterChartList();

    CTScatterChart[] getScatterChartArray();

    CTScatterChart getScatterChartArray(int i);

    int sizeOfScatterChartArray();

    void setScatterChartArray(CTScatterChart[] cTScatterChartArr);

    void setScatterChartArray(int i, CTScatterChart cTScatterChart);

    CTScatterChart insertNewScatterChart(int i);

    CTScatterChart addNewScatterChart();

    void removeScatterChart(int i);

    List<CTPieChart> getPieChartList();

    CTPieChart[] getPieChartArray();

    CTPieChart getPieChartArray(int i);

    int sizeOfPieChartArray();

    void setPieChartArray(CTPieChart[] cTPieChartArr);

    void setPieChartArray(int i, CTPieChart cTPieChart);

    CTPieChart insertNewPieChart(int i);

    CTPieChart addNewPieChart();

    void removePieChart(int i);

    List<CTPie3DChart> getPie3DChartList();

    CTPie3DChart[] getPie3DChartArray();

    CTPie3DChart getPie3DChartArray(int i);

    int sizeOfPie3DChartArray();

    void setPie3DChartArray(CTPie3DChart[] cTPie3DChartArr);

    void setPie3DChartArray(int i, CTPie3DChart cTPie3DChart);

    CTPie3DChart insertNewPie3DChart(int i);

    CTPie3DChart addNewPie3DChart();

    void removePie3DChart(int i);

    List<CTDoughnutChart> getDoughnutChartList();

    CTDoughnutChart[] getDoughnutChartArray();

    CTDoughnutChart getDoughnutChartArray(int i);

    int sizeOfDoughnutChartArray();

    void setDoughnutChartArray(CTDoughnutChart[] cTDoughnutChartArr);

    void setDoughnutChartArray(int i, CTDoughnutChart cTDoughnutChart);

    CTDoughnutChart insertNewDoughnutChart(int i);

    CTDoughnutChart addNewDoughnutChart();

    void removeDoughnutChart(int i);

    List<CTBarChart> getBarChartList();

    CTBarChart[] getBarChartArray();

    CTBarChart getBarChartArray(int i);

    int sizeOfBarChartArray();

    void setBarChartArray(CTBarChart[] cTBarChartArr);

    void setBarChartArray(int i, CTBarChart cTBarChart);

    CTBarChart insertNewBarChart(int i);

    CTBarChart addNewBarChart();

    void removeBarChart(int i);

    List<CTBar3DChart> getBar3DChartList();

    CTBar3DChart[] getBar3DChartArray();

    CTBar3DChart getBar3DChartArray(int i);

    int sizeOfBar3DChartArray();

    void setBar3DChartArray(CTBar3DChart[] cTBar3DChartArr);

    void setBar3DChartArray(int i, CTBar3DChart cTBar3DChart);

    CTBar3DChart insertNewBar3DChart(int i);

    CTBar3DChart addNewBar3DChart();

    void removeBar3DChart(int i);

    List<CTOfPieChart> getOfPieChartList();

    CTOfPieChart[] getOfPieChartArray();

    CTOfPieChart getOfPieChartArray(int i);

    int sizeOfOfPieChartArray();

    void setOfPieChartArray(CTOfPieChart[] cTOfPieChartArr);

    void setOfPieChartArray(int i, CTOfPieChart cTOfPieChart);

    CTOfPieChart insertNewOfPieChart(int i);

    CTOfPieChart addNewOfPieChart();

    void removeOfPieChart(int i);

    List<CTSurfaceChart> getSurfaceChartList();

    CTSurfaceChart[] getSurfaceChartArray();

    CTSurfaceChart getSurfaceChartArray(int i);

    int sizeOfSurfaceChartArray();

    void setSurfaceChartArray(CTSurfaceChart[] cTSurfaceChartArr);

    void setSurfaceChartArray(int i, CTSurfaceChart cTSurfaceChart);

    CTSurfaceChart insertNewSurfaceChart(int i);

    CTSurfaceChart addNewSurfaceChart();

    void removeSurfaceChart(int i);

    List<CTSurface3DChart> getSurface3DChartList();

    CTSurface3DChart[] getSurface3DChartArray();

    CTSurface3DChart getSurface3DChartArray(int i);

    int sizeOfSurface3DChartArray();

    void setSurface3DChartArray(CTSurface3DChart[] cTSurface3DChartArr);

    void setSurface3DChartArray(int i, CTSurface3DChart cTSurface3DChart);

    CTSurface3DChart insertNewSurface3DChart(int i);

    CTSurface3DChart addNewSurface3DChart();

    void removeSurface3DChart(int i);

    List<CTBubbleChart> getBubbleChartList();

    CTBubbleChart[] getBubbleChartArray();

    CTBubbleChart getBubbleChartArray(int i);

    int sizeOfBubbleChartArray();

    void setBubbleChartArray(CTBubbleChart[] cTBubbleChartArr);

    void setBubbleChartArray(int i, CTBubbleChart cTBubbleChart);

    CTBubbleChart insertNewBubbleChart(int i);

    CTBubbleChart addNewBubbleChart();

    void removeBubbleChart(int i);

    List<CTValAx> getValAxList();

    CTValAx[] getValAxArray();

    CTValAx getValAxArray(int i);

    int sizeOfValAxArray();

    void setValAxArray(CTValAx[] cTValAxArr);

    void setValAxArray(int i, CTValAx cTValAx);

    CTValAx insertNewValAx(int i);

    CTValAx addNewValAx();

    void removeValAx(int i);

    List<CTCatAx> getCatAxList();

    CTCatAx[] getCatAxArray();

    CTCatAx getCatAxArray(int i);

    int sizeOfCatAxArray();

    void setCatAxArray(CTCatAx[] cTCatAxArr);

    void setCatAxArray(int i, CTCatAx cTCatAx);

    CTCatAx insertNewCatAx(int i);

    CTCatAx addNewCatAx();

    void removeCatAx(int i);

    List<CTDateAx> getDateAxList();

    CTDateAx[] getDateAxArray();

    CTDateAx getDateAxArray(int i);

    int sizeOfDateAxArray();

    void setDateAxArray(CTDateAx[] cTDateAxArr);

    void setDateAxArray(int i, CTDateAx cTDateAx);

    CTDateAx insertNewDateAx(int i);

    CTDateAx addNewDateAx();

    void removeDateAx(int i);

    List<CTSerAx> getSerAxList();

    CTSerAx[] getSerAxArray();

    CTSerAx getSerAxArray(int i);

    int sizeOfSerAxArray();

    void setSerAxArray(CTSerAx[] cTSerAxArr);

    void setSerAxArray(int i, CTSerAx cTSerAx);

    CTSerAx insertNewSerAx(int i);

    CTSerAx addNewSerAx();

    void removeSerAx(int i);

    CTDTable getDTable();

    boolean isSetDTable();

    void setDTable(CTDTable cTDTable);

    CTDTable addNewDTable();

    void unsetDTable();

    CTShapeProperties getSpPr();

    boolean isSetSpPr();

    void setSpPr(CTShapeProperties cTShapeProperties);

    CTShapeProperties addNewSpPr();

    void unsetSpPr();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
