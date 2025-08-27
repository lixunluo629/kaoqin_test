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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTScatterSer.class */
public interface CTScatterSer extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTScatterSer.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctscatterser2f7atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTScatterSer$Factory.class */
    public static final class Factory {
        public static CTScatterSer newInstance() {
            return (CTScatterSer) POIXMLTypeLoader.newInstance(CTScatterSer.type, null);
        }

        public static CTScatterSer newInstance(XmlOptions xmlOptions) {
            return (CTScatterSer) POIXMLTypeLoader.newInstance(CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(String str) throws XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(str, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(str, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(File file) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(file, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(file, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(URL url) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(url, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(url, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(InputStream inputStream) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(inputStream, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(inputStream, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(Reader reader) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(reader, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterSer) POIXMLTypeLoader.parse(reader, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(xMLStreamReader, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(xMLStreamReader, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(Node node) throws XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(node, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(node, CTScatterSer.type, xmlOptions);
        }

        public static CTScatterSer parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(xMLInputStream, CTScatterSer.type, (XmlOptions) null);
        }

        public static CTScatterSer parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTScatterSer) POIXMLTypeLoader.parse(xMLInputStream, CTScatterSer.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScatterSer.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScatterSer.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTUnsignedInt getIdx();

    void setIdx(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewIdx();

    CTUnsignedInt getOrder();

    void setOrder(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewOrder();

    CTSerTx getTx();

    boolean isSetTx();

    void setTx(CTSerTx cTSerTx);

    CTSerTx addNewTx();

    void unsetTx();

    CTShapeProperties getSpPr();

    boolean isSetSpPr();

    void setSpPr(CTShapeProperties cTShapeProperties);

    CTShapeProperties addNewSpPr();

    void unsetSpPr();

    CTMarker getMarker();

    boolean isSetMarker();

    void setMarker(CTMarker cTMarker);

    CTMarker addNewMarker();

    void unsetMarker();

    List<CTDPt> getDPtList();

    CTDPt[] getDPtArray();

    CTDPt getDPtArray(int i);

    int sizeOfDPtArray();

    void setDPtArray(CTDPt[] cTDPtArr);

    void setDPtArray(int i, CTDPt cTDPt);

    CTDPt insertNewDPt(int i);

    CTDPt addNewDPt();

    void removeDPt(int i);

    CTDLbls getDLbls();

    boolean isSetDLbls();

    void setDLbls(CTDLbls cTDLbls);

    CTDLbls addNewDLbls();

    void unsetDLbls();

    List<CTTrendline> getTrendlineList();

    CTTrendline[] getTrendlineArray();

    CTTrendline getTrendlineArray(int i);

    int sizeOfTrendlineArray();

    void setTrendlineArray(CTTrendline[] cTTrendlineArr);

    void setTrendlineArray(int i, CTTrendline cTTrendline);

    CTTrendline insertNewTrendline(int i);

    CTTrendline addNewTrendline();

    void removeTrendline(int i);

    List<CTErrBars> getErrBarsList();

    CTErrBars[] getErrBarsArray();

    CTErrBars getErrBarsArray(int i);

    int sizeOfErrBarsArray();

    void setErrBarsArray(CTErrBars[] cTErrBarsArr);

    void setErrBarsArray(int i, CTErrBars cTErrBars);

    CTErrBars insertNewErrBars(int i);

    CTErrBars addNewErrBars();

    void removeErrBars(int i);

    CTAxDataSource getXVal();

    boolean isSetXVal();

    void setXVal(CTAxDataSource cTAxDataSource);

    CTAxDataSource addNewXVal();

    void unsetXVal();

    CTNumDataSource getYVal();

    boolean isSetYVal();

    void setYVal(CTNumDataSource cTNumDataSource);

    CTNumDataSource addNewYVal();

    void unsetYVal();

    CTBoolean getSmooth();

    boolean isSetSmooth();

    void setSmooth(CTBoolean cTBoolean);

    CTBoolean addNewSmooth();

    void unsetSmooth();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
