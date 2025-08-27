package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPPrBase.class */
public interface CTPPrBase extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPPrBase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpprbasebaeftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPPrBase$Factory.class */
    public static final class Factory {
        public static CTPPrBase newInstance() {
            return (CTPPrBase) POIXMLTypeLoader.newInstance(CTPPrBase.type, null);
        }

        public static CTPPrBase newInstance(XmlOptions xmlOptions) {
            return (CTPPrBase) POIXMLTypeLoader.newInstance(CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(String str) throws XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(str, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(str, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(File file) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(file, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(file, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(URL url) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(url, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(url, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(inputStream, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(inputStream, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(Reader reader) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(reader, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrBase) POIXMLTypeLoader.parse(reader, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(Node node) throws XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(node, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(node, CTPPrBase.type, xmlOptions);
        }

        public static CTPPrBase parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTPPrBase.type, (XmlOptions) null);
        }

        public static CTPPrBase parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTPPrBase.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPPrBase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPPrBase.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTString getPStyle();

    boolean isSetPStyle();

    void setPStyle(CTString cTString);

    CTString addNewPStyle();

    void unsetPStyle();

    CTOnOff getKeepNext();

    boolean isSetKeepNext();

    void setKeepNext(CTOnOff cTOnOff);

    CTOnOff addNewKeepNext();

    void unsetKeepNext();

    CTOnOff getKeepLines();

    boolean isSetKeepLines();

    void setKeepLines(CTOnOff cTOnOff);

    CTOnOff addNewKeepLines();

    void unsetKeepLines();

    CTOnOff getPageBreakBefore();

    boolean isSetPageBreakBefore();

    void setPageBreakBefore(CTOnOff cTOnOff);

    CTOnOff addNewPageBreakBefore();

    void unsetPageBreakBefore();

    CTFramePr getFramePr();

    boolean isSetFramePr();

    void setFramePr(CTFramePr cTFramePr);

    CTFramePr addNewFramePr();

    void unsetFramePr();

    CTOnOff getWidowControl();

    boolean isSetWidowControl();

    void setWidowControl(CTOnOff cTOnOff);

    CTOnOff addNewWidowControl();

    void unsetWidowControl();

    CTNumPr getNumPr();

    boolean isSetNumPr();

    void setNumPr(CTNumPr cTNumPr);

    CTNumPr addNewNumPr();

    void unsetNumPr();

    CTOnOff getSuppressLineNumbers();

    boolean isSetSuppressLineNumbers();

    void setSuppressLineNumbers(CTOnOff cTOnOff);

    CTOnOff addNewSuppressLineNumbers();

    void unsetSuppressLineNumbers();

    CTPBdr getPBdr();

    boolean isSetPBdr();

    void setPBdr(CTPBdr cTPBdr);

    CTPBdr addNewPBdr();

    void unsetPBdr();

    CTShd getShd();

    boolean isSetShd();

    void setShd(CTShd cTShd);

    CTShd addNewShd();

    void unsetShd();

    CTTabs getTabs();

    boolean isSetTabs();

    void setTabs(CTTabs cTTabs);

    CTTabs addNewTabs();

    void unsetTabs();

    CTOnOff getSuppressAutoHyphens();

    boolean isSetSuppressAutoHyphens();

    void setSuppressAutoHyphens(CTOnOff cTOnOff);

    CTOnOff addNewSuppressAutoHyphens();

    void unsetSuppressAutoHyphens();

    CTOnOff getKinsoku();

    boolean isSetKinsoku();

    void setKinsoku(CTOnOff cTOnOff);

    CTOnOff addNewKinsoku();

    void unsetKinsoku();

    CTOnOff getWordWrap();

    boolean isSetWordWrap();

    void setWordWrap(CTOnOff cTOnOff);

    CTOnOff addNewWordWrap();

    void unsetWordWrap();

    CTOnOff getOverflowPunct();

    boolean isSetOverflowPunct();

    void setOverflowPunct(CTOnOff cTOnOff);

    CTOnOff addNewOverflowPunct();

    void unsetOverflowPunct();

    CTOnOff getTopLinePunct();

    boolean isSetTopLinePunct();

    void setTopLinePunct(CTOnOff cTOnOff);

    CTOnOff addNewTopLinePunct();

    void unsetTopLinePunct();

    CTOnOff getAutoSpaceDE();

    boolean isSetAutoSpaceDE();

    void setAutoSpaceDE(CTOnOff cTOnOff);

    CTOnOff addNewAutoSpaceDE();

    void unsetAutoSpaceDE();

    CTOnOff getAutoSpaceDN();

    boolean isSetAutoSpaceDN();

    void setAutoSpaceDN(CTOnOff cTOnOff);

    CTOnOff addNewAutoSpaceDN();

    void unsetAutoSpaceDN();

    CTOnOff getBidi();

    boolean isSetBidi();

    void setBidi(CTOnOff cTOnOff);

    CTOnOff addNewBidi();

    void unsetBidi();

    CTOnOff getAdjustRightInd();

    boolean isSetAdjustRightInd();

    void setAdjustRightInd(CTOnOff cTOnOff);

    CTOnOff addNewAdjustRightInd();

    void unsetAdjustRightInd();

    CTOnOff getSnapToGrid();

    boolean isSetSnapToGrid();

    void setSnapToGrid(CTOnOff cTOnOff);

    CTOnOff addNewSnapToGrid();

    void unsetSnapToGrid();

    CTSpacing getSpacing();

    boolean isSetSpacing();

    void setSpacing(CTSpacing cTSpacing);

    CTSpacing addNewSpacing();

    void unsetSpacing();

    CTInd getInd();

    boolean isSetInd();

    void setInd(CTInd cTInd);

    CTInd addNewInd();

    void unsetInd();

    CTOnOff getContextualSpacing();

    boolean isSetContextualSpacing();

    void setContextualSpacing(CTOnOff cTOnOff);

    CTOnOff addNewContextualSpacing();

    void unsetContextualSpacing();

    CTOnOff getMirrorIndents();

    boolean isSetMirrorIndents();

    void setMirrorIndents(CTOnOff cTOnOff);

    CTOnOff addNewMirrorIndents();

    void unsetMirrorIndents();

    CTOnOff getSuppressOverlap();

    boolean isSetSuppressOverlap();

    void setSuppressOverlap(CTOnOff cTOnOff);

    CTOnOff addNewSuppressOverlap();

    void unsetSuppressOverlap();

    CTJc getJc();

    boolean isSetJc();

    void setJc(CTJc cTJc);

    CTJc addNewJc();

    void unsetJc();

    CTTextDirection getTextDirection();

    boolean isSetTextDirection();

    void setTextDirection(CTTextDirection cTTextDirection);

    CTTextDirection addNewTextDirection();

    void unsetTextDirection();

    CTTextAlignment getTextAlignment();

    boolean isSetTextAlignment();

    void setTextAlignment(CTTextAlignment cTTextAlignment);

    CTTextAlignment addNewTextAlignment();

    void unsetTextAlignment();

    CTTextboxTightWrap getTextboxTightWrap();

    boolean isSetTextboxTightWrap();

    void setTextboxTightWrap(CTTextboxTightWrap cTTextboxTightWrap);

    CTTextboxTightWrap addNewTextboxTightWrap();

    void unsetTextboxTightWrap();

    CTDecimalNumber getOutlineLvl();

    boolean isSetOutlineLvl();

    void setOutlineLvl(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewOutlineLvl();

    void unsetOutlineLvl();

    CTDecimalNumber getDivId();

    boolean isSetDivId();

    void setDivId(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewDivId();

    void unsetDivId();

    CTCnf getCnfStyle();

    boolean isSetCnfStyle();

    void setCnfStyle(CTCnf cTCnf);

    CTCnf addNewCnfStyle();

    void unsetCnfStyle();
}
