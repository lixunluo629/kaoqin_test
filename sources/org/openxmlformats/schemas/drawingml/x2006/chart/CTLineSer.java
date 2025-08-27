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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTLineSer.class */
public interface CTLineSer extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLineSer.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlineserd01atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTLineSer$Factory.class */
    public static final class Factory {
        public static CTLineSer newInstance() {
            return (CTLineSer) POIXMLTypeLoader.newInstance(CTLineSer.type, null);
        }

        public static CTLineSer newInstance(XmlOptions xmlOptions) {
            return (CTLineSer) POIXMLTypeLoader.newInstance(CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(String str) throws XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(str, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(str, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(File file) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(file, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(file, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(URL url) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(url, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(url, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(inputStream, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(inputStream, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(Reader reader) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(reader, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineSer) POIXMLTypeLoader.parse(reader, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(xMLStreamReader, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(xMLStreamReader, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(Node node) throws XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(node, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(node, CTLineSer.type, xmlOptions);
        }

        public static CTLineSer parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(xMLInputStream, CTLineSer.type, (XmlOptions) null);
        }

        public static CTLineSer parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLineSer) POIXMLTypeLoader.parse(xMLInputStream, CTLineSer.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineSer.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineSer.type, xmlOptions);
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

    CTErrBars getErrBars();

    boolean isSetErrBars();

    void setErrBars(CTErrBars cTErrBars);

    CTErrBars addNewErrBars();

    void unsetErrBars();

    CTAxDataSource getCat();

    boolean isSetCat();

    void setCat(CTAxDataSource cTAxDataSource);

    CTAxDataSource addNewCat();

    void unsetCat();

    CTNumDataSource getVal();

    boolean isSetVal();

    void setVal(CTNumDataSource cTNumDataSource);

    CTNumDataSource addNewVal();

    void unsetVal();

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
