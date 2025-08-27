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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTCatAx.class */
public interface CTCatAx extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCatAx.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcatax7159type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTCatAx$Factory.class */
    public static final class Factory {
        public static CTCatAx newInstance() {
            return (CTCatAx) POIXMLTypeLoader.newInstance(CTCatAx.type, null);
        }

        public static CTCatAx newInstance(XmlOptions xmlOptions) {
            return (CTCatAx) POIXMLTypeLoader.newInstance(CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(String str) throws XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(str, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(str, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(File file) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(file, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(file, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(URL url) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(url, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(url, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(inputStream, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(inputStream, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(Reader reader) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(reader, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCatAx) POIXMLTypeLoader.parse(reader, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(xMLStreamReader, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(xMLStreamReader, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(Node node) throws XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(node, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(node, CTCatAx.type, xmlOptions);
        }

        public static CTCatAx parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(xMLInputStream, CTCatAx.type, (XmlOptions) null);
        }

        public static CTCatAx parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCatAx) POIXMLTypeLoader.parse(xMLInputStream, CTCatAx.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCatAx.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCatAx.type, xmlOptions);
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

    CTLblAlgn getLblAlgn();

    boolean isSetLblAlgn();

    void setLblAlgn(CTLblAlgn cTLblAlgn);

    CTLblAlgn addNewLblAlgn();

    void unsetLblAlgn();

    CTLblOffset getLblOffset();

    boolean isSetLblOffset();

    void setLblOffset(CTLblOffset cTLblOffset);

    CTLblOffset addNewLblOffset();

    void unsetLblOffset();

    CTSkip getTickLblSkip();

    boolean isSetTickLblSkip();

    void setTickLblSkip(CTSkip cTSkip);

    CTSkip addNewTickLblSkip();

    void unsetTickLblSkip();

    CTSkip getTickMarkSkip();

    boolean isSetTickMarkSkip();

    void setTickMarkSkip(CTSkip cTSkip);

    CTSkip addNewTickMarkSkip();

    void unsetTickMarkSkip();

    CTBoolean getNoMultiLvlLbl();

    boolean isSetNoMultiLvlLbl();

    void setNoMultiLvlLbl(CTBoolean cTBoolean);

    CTBoolean addNewNoMultiLvlLbl();

    void unsetNoMultiLvlLbl();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
