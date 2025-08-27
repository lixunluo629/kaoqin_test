package org.openxmlformats.schemas.drawingml.x2006.chart;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTChart.class */
public interface CTChart extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTChart.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctchartc108type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTChart$Factory.class */
    public static final class Factory {
        public static CTChart newInstance() {
            return (CTChart) POIXMLTypeLoader.newInstance(CTChart.type, null);
        }

        public static CTChart newInstance(XmlOptions xmlOptions) {
            return (CTChart) POIXMLTypeLoader.newInstance(CTChart.type, xmlOptions);
        }

        public static CTChart parse(String str) throws XmlException {
            return (CTChart) POIXMLTypeLoader.parse(str, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTChart) POIXMLTypeLoader.parse(str, CTChart.type, xmlOptions);
        }

        public static CTChart parse(File file) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(file, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(file, CTChart.type, xmlOptions);
        }

        public static CTChart parse(URL url) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(url, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(url, CTChart.type, xmlOptions);
        }

        public static CTChart parse(InputStream inputStream) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(inputStream, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(inputStream, CTChart.type, xmlOptions);
        }

        public static CTChart parse(Reader reader) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(reader, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChart) POIXMLTypeLoader.parse(reader, CTChart.type, xmlOptions);
        }

        public static CTChart parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTChart) POIXMLTypeLoader.parse(xMLStreamReader, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTChart) POIXMLTypeLoader.parse(xMLStreamReader, CTChart.type, xmlOptions);
        }

        public static CTChart parse(Node node) throws XmlException {
            return (CTChart) POIXMLTypeLoader.parse(node, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTChart) POIXMLTypeLoader.parse(node, CTChart.type, xmlOptions);
        }

        public static CTChart parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTChart) POIXMLTypeLoader.parse(xMLInputStream, CTChart.type, (XmlOptions) null);
        }

        public static CTChart parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTChart) POIXMLTypeLoader.parse(xMLInputStream, CTChart.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTChart.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTChart.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTitle getTitle();

    boolean isSetTitle();

    void setTitle(CTTitle cTTitle);

    CTTitle addNewTitle();

    void unsetTitle();

    CTBoolean getAutoTitleDeleted();

    boolean isSetAutoTitleDeleted();

    void setAutoTitleDeleted(CTBoolean cTBoolean);

    CTBoolean addNewAutoTitleDeleted();

    void unsetAutoTitleDeleted();

    CTPivotFmts getPivotFmts();

    boolean isSetPivotFmts();

    void setPivotFmts(CTPivotFmts cTPivotFmts);

    CTPivotFmts addNewPivotFmts();

    void unsetPivotFmts();

    CTView3D getView3D();

    boolean isSetView3D();

    void setView3D(CTView3D cTView3D);

    CTView3D addNewView3D();

    void unsetView3D();

    CTSurface getFloor();

    boolean isSetFloor();

    void setFloor(CTSurface cTSurface);

    CTSurface addNewFloor();

    void unsetFloor();

    CTSurface getSideWall();

    boolean isSetSideWall();

    void setSideWall(CTSurface cTSurface);

    CTSurface addNewSideWall();

    void unsetSideWall();

    CTSurface getBackWall();

    boolean isSetBackWall();

    void setBackWall(CTSurface cTSurface);

    CTSurface addNewBackWall();

    void unsetBackWall();

    CTPlotArea getPlotArea();

    void setPlotArea(CTPlotArea cTPlotArea);

    CTPlotArea addNewPlotArea();

    CTLegend getLegend();

    boolean isSetLegend();

    void setLegend(CTLegend cTLegend);

    CTLegend addNewLegend();

    void unsetLegend();

    CTBoolean getPlotVisOnly();

    boolean isSetPlotVisOnly();

    void setPlotVisOnly(CTBoolean cTBoolean);

    CTBoolean addNewPlotVisOnly();

    void unsetPlotVisOnly();

    CTDispBlanksAs getDispBlanksAs();

    boolean isSetDispBlanksAs();

    void setDispBlanksAs(CTDispBlanksAs cTDispBlanksAs);

    CTDispBlanksAs addNewDispBlanksAs();

    void unsetDispBlanksAs();

    CTBoolean getShowDLblsOverMax();

    boolean isSetShowDLblsOverMax();

    void setShowDLblsOverMax(CTBoolean cTBoolean);

    CTBoolean addNewShowDLblsOverMax();

    void unsetShowDLblsOverMax();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
