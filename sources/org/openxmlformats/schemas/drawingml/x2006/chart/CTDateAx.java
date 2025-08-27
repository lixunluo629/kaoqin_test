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
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTDateAx.class */
public interface CTDateAx extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDateAx.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdateaxbdd7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTDateAx$Factory.class */
    public static final class Factory {
        public static CTDateAx newInstance() {
            return (CTDateAx) POIXMLTypeLoader.newInstance(CTDateAx.type, null);
        }

        public static CTDateAx newInstance(XmlOptions xmlOptions) {
            return (CTDateAx) POIXMLTypeLoader.newInstance(CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(String str) throws XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(str, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(str, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(File file) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(file, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(file, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(URL url) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(url, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(url, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(inputStream, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(inputStream, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(Reader reader) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(reader, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDateAx) POIXMLTypeLoader.parse(reader, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(xMLStreamReader, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(xMLStreamReader, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(Node node) throws XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(node, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(node, CTDateAx.type, xmlOptions);
        }

        public static CTDateAx parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(xMLInputStream, CTDateAx.type, (XmlOptions) null);
        }

        public static CTDateAx parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDateAx) POIXMLTypeLoader.parse(xMLInputStream, CTDateAx.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDateAx.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDateAx.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTUnsignedInt getAxId();

    void setAxId(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewAxId();

    CTScaling getScaling();

    void setScaling(CTScaling cTScaling);

    CTScaling addNewScaling();

    CTBoolean getDelete();

    boolean isSetDelete();

    void setDelete(CTBoolean cTBoolean);

    CTBoolean addNewDelete();

    void unsetDelete();

    CTAxPos getAxPos();

    void setAxPos(CTAxPos cTAxPos);

    CTAxPos addNewAxPos();

    CTChartLines getMajorGridlines();

    boolean isSetMajorGridlines();

    void setMajorGridlines(CTChartLines cTChartLines);

    CTChartLines addNewMajorGridlines();

    void unsetMajorGridlines();

    CTChartLines getMinorGridlines();

    boolean isSetMinorGridlines();

    void setMinorGridlines(CTChartLines cTChartLines);

    CTChartLines addNewMinorGridlines();

    void unsetMinorGridlines();

    CTTitle getTitle();

    boolean isSetTitle();

    void setTitle(CTTitle cTTitle);

    CTTitle addNewTitle();

    void unsetTitle();

    CTNumFmt getNumFmt();

    boolean isSetNumFmt();

    void setNumFmt(CTNumFmt cTNumFmt);

    CTNumFmt addNewNumFmt();

    void unsetNumFmt();

    CTTickMark getMajorTickMark();

    boolean isSetMajorTickMark();

    void setMajorTickMark(CTTickMark cTTickMark);

    CTTickMark addNewMajorTickMark();

    void unsetMajorTickMark();

    CTTickMark getMinorTickMark();

    boolean isSetMinorTickMark();

    void setMinorTickMark(CTTickMark cTTickMark);

    CTTickMark addNewMinorTickMark();

    void unsetMinorTickMark();

    CTTickLblPos getTickLblPos();

    boolean isSetTickLblPos();

    void setTickLblPos(CTTickLblPos cTTickLblPos);

    CTTickLblPos addNewTickLblPos();

    void unsetTickLblPos();

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

    CTUnsignedInt getCrossAx();

    void setCrossAx(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewCrossAx();

    CTCrosses getCrosses();

    boolean isSetCrosses();

    void setCrosses(CTCrosses cTCrosses);

    CTCrosses addNewCrosses();

    void unsetCrosses();

    CTDouble getCrossesAt();

    boolean isSetCrossesAt();

    void setCrossesAt(CTDouble cTDouble);

    CTDouble addNewCrossesAt();

    void unsetCrossesAt();

    CTBoolean getAuto();

    boolean isSetAuto();

    void setAuto(CTBoolean cTBoolean);

    CTBoolean addNewAuto();

    void unsetAuto();

    CTLblOffset getLblOffset();

    boolean isSetLblOffset();

    void setLblOffset(CTLblOffset cTLblOffset);

    CTLblOffset addNewLblOffset();

    void unsetLblOffset();

    CTTimeUnit getBaseTimeUnit();

    boolean isSetBaseTimeUnit();

    void setBaseTimeUnit(CTTimeUnit cTTimeUnit);

    CTTimeUnit addNewBaseTimeUnit();

    void unsetBaseTimeUnit();

    CTAxisUnit getMajorUnit();

    boolean isSetMajorUnit();

    void setMajorUnit(CTAxisUnit cTAxisUnit);

    CTAxisUnit addNewMajorUnit();

    void unsetMajorUnit();

    CTTimeUnit getMajorTimeUnit();

    boolean isSetMajorTimeUnit();

    void setMajorTimeUnit(CTTimeUnit cTTimeUnit);

    CTTimeUnit addNewMajorTimeUnit();

    void unsetMajorTimeUnit();

    CTAxisUnit getMinorUnit();

    boolean isSetMinorUnit();

    void setMinorUnit(CTAxisUnit cTAxisUnit);

    CTAxisUnit addNewMinorUnit();

    void unsetMinorUnit();

    CTTimeUnit getMinorTimeUnit();

    boolean isSetMinorTimeUnit();

    void setMinorTimeUnit(CTTimeUnit cTTimeUnit);

    CTTimeUnit addNewMinorTimeUnit();

    void unsetMinorTimeUnit();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
