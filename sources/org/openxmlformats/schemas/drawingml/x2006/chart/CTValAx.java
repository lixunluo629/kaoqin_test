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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTValAx.class */
public interface CTValAx extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTValAx.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctvalaxd06etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTValAx$Factory.class */
    public static final class Factory {
        public static CTValAx newInstance() {
            return (CTValAx) POIXMLTypeLoader.newInstance(CTValAx.type, null);
        }

        public static CTValAx newInstance(XmlOptions xmlOptions) {
            return (CTValAx) POIXMLTypeLoader.newInstance(CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(String str) throws XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(str, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(str, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(File file) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(file, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(file, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(URL url) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(url, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(url, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(InputStream inputStream) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(inputStream, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(inputStream, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(Reader reader) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(reader, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTValAx) POIXMLTypeLoader.parse(reader, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(xMLStreamReader, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(xMLStreamReader, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(Node node) throws XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(node, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(node, CTValAx.type, xmlOptions);
        }

        public static CTValAx parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(xMLInputStream, CTValAx.type, (XmlOptions) null);
        }

        public static CTValAx parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTValAx) POIXMLTypeLoader.parse(xMLInputStream, CTValAx.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTValAx.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTValAx.type, xmlOptions);
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

    CTCrossBetween getCrossBetween();

    boolean isSetCrossBetween();

    void setCrossBetween(CTCrossBetween cTCrossBetween);

    CTCrossBetween addNewCrossBetween();

    void unsetCrossBetween();

    CTAxisUnit getMajorUnit();

    boolean isSetMajorUnit();

    void setMajorUnit(CTAxisUnit cTAxisUnit);

    CTAxisUnit addNewMajorUnit();

    void unsetMajorUnit();

    CTAxisUnit getMinorUnit();

    boolean isSetMinorUnit();

    void setMinorUnit(CTAxisUnit cTAxisUnit);

    CTAxisUnit addNewMinorUnit();

    void unsetMinorUnit();

    CTDispUnits getDispUnits();

    boolean isSetDispUnits();

    void setDispUnits(CTDispUnits cTDispUnits);

    CTDispUnits addNewDispUnits();

    void unsetDispUnits();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
