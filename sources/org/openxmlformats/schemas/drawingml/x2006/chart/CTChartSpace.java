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
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTChartSpace.class */
public interface CTChartSpace extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTChartSpace.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctchartspacef9b4type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTChartSpace$Factory.class */
    public static final class Factory {
        public static CTChartSpace newInstance() {
            return (CTChartSpace) POIXMLTypeLoader.newInstance(CTChartSpace.type, null);
        }

        public static CTChartSpace newInstance(XmlOptions xmlOptions) {
            return (CTChartSpace) POIXMLTypeLoader.newInstance(CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(String str) throws XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(str, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(str, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(File file) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(file, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(file, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(URL url) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(url, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(url, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(InputStream inputStream) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(inputStream, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(inputStream, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(Reader reader) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(reader, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartSpace) POIXMLTypeLoader.parse(reader, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(xMLStreamReader, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(xMLStreamReader, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(Node node) throws XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(node, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(node, CTChartSpace.type, xmlOptions);
        }

        public static CTChartSpace parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(xMLInputStream, CTChartSpace.type, (XmlOptions) null);
        }

        public static CTChartSpace parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTChartSpace) POIXMLTypeLoader.parse(xMLInputStream, CTChartSpace.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTChartSpace.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTChartSpace.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTBoolean getDate1904();

    boolean isSetDate1904();

    void setDate1904(CTBoolean cTBoolean);

    CTBoolean addNewDate1904();

    void unsetDate1904();

    CTTextLanguageID getLang();

    boolean isSetLang();

    void setLang(CTTextLanguageID cTTextLanguageID);

    CTTextLanguageID addNewLang();

    void unsetLang();

    CTBoolean getRoundedCorners();

    boolean isSetRoundedCorners();

    void setRoundedCorners(CTBoolean cTBoolean);

    CTBoolean addNewRoundedCorners();

    void unsetRoundedCorners();

    CTStyle getStyle();

    boolean isSetStyle();

    void setStyle(CTStyle cTStyle);

    CTStyle addNewStyle();

    void unsetStyle();

    CTColorMapping getClrMapOvr();

    boolean isSetClrMapOvr();

    void setClrMapOvr(CTColorMapping cTColorMapping);

    CTColorMapping addNewClrMapOvr();

    void unsetClrMapOvr();

    CTPivotSource getPivotSource();

    boolean isSetPivotSource();

    void setPivotSource(CTPivotSource cTPivotSource);

    CTPivotSource addNewPivotSource();

    void unsetPivotSource();

    CTProtection getProtection();

    boolean isSetProtection();

    void setProtection(CTProtection cTProtection);

    CTProtection addNewProtection();

    void unsetProtection();

    CTChart getChart();

    void setChart(CTChart cTChart);

    CTChart addNewChart();

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

    CTExternalData getExternalData();

    boolean isSetExternalData();

    void setExternalData(CTExternalData cTExternalData);

    CTExternalData addNewExternalData();

    void unsetExternalData();

    CTPrintSettings getPrintSettings();

    boolean isSetPrintSettings();

    void setPrintSettings(CTPrintSettings cTPrintSettings);

    CTPrintSettings addNewPrintSettings();

    void unsetPrintSettings();

    CTRelId getUserShapes();

    boolean isSetUserShapes();

    void setUserShapes(CTRelId cTRelId);

    CTRelId addNewUserShapes();

    void unsetUserShapes();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
