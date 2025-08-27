package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTrPrBase.class */
public interface CTTrPrBase extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTrPrBase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttrprbase5d77type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTrPrBase$Factory.class */
    public static final class Factory {
        public static CTTrPrBase newInstance() {
            return (CTTrPrBase) POIXMLTypeLoader.newInstance(CTTrPrBase.type, null);
        }

        public static CTTrPrBase newInstance(XmlOptions xmlOptions) {
            return (CTTrPrBase) POIXMLTypeLoader.newInstance(CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(String str) throws XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(str, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(str, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(File file) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(file, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(file, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(URL url) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(url, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(url, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(inputStream, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(inputStream, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(Reader reader) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(reader, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(reader, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(Node node) throws XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(node, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(node, CTTrPrBase.type, xmlOptions);
        }

        public static CTTrPrBase parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTTrPrBase.type, (XmlOptions) null);
        }

        public static CTTrPrBase parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTrPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTTrPrBase.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTrPrBase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTrPrBase.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCnf> getCnfStyleList();

    CTCnf[] getCnfStyleArray();

    CTCnf getCnfStyleArray(int i);

    int sizeOfCnfStyleArray();

    void setCnfStyleArray(CTCnf[] cTCnfArr);

    void setCnfStyleArray(int i, CTCnf cTCnf);

    CTCnf insertNewCnfStyle(int i);

    CTCnf addNewCnfStyle();

    void removeCnfStyle(int i);

    List<CTDecimalNumber> getDivIdList();

    CTDecimalNumber[] getDivIdArray();

    CTDecimalNumber getDivIdArray(int i);

    int sizeOfDivIdArray();

    void setDivIdArray(CTDecimalNumber[] cTDecimalNumberArr);

    void setDivIdArray(int i, CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber insertNewDivId(int i);

    CTDecimalNumber addNewDivId();

    void removeDivId(int i);

    List<CTDecimalNumber> getGridBeforeList();

    CTDecimalNumber[] getGridBeforeArray();

    CTDecimalNumber getGridBeforeArray(int i);

    int sizeOfGridBeforeArray();

    void setGridBeforeArray(CTDecimalNumber[] cTDecimalNumberArr);

    void setGridBeforeArray(int i, CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber insertNewGridBefore(int i);

    CTDecimalNumber addNewGridBefore();

    void removeGridBefore(int i);

    List<CTDecimalNumber> getGridAfterList();

    CTDecimalNumber[] getGridAfterArray();

    CTDecimalNumber getGridAfterArray(int i);

    int sizeOfGridAfterArray();

    void setGridAfterArray(CTDecimalNumber[] cTDecimalNumberArr);

    void setGridAfterArray(int i, CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber insertNewGridAfter(int i);

    CTDecimalNumber addNewGridAfter();

    void removeGridAfter(int i);

    List<CTTblWidth> getWBeforeList();

    CTTblWidth[] getWBeforeArray();

    CTTblWidth getWBeforeArray(int i);

    int sizeOfWBeforeArray();

    void setWBeforeArray(CTTblWidth[] cTTblWidthArr);

    void setWBeforeArray(int i, CTTblWidth cTTblWidth);

    CTTblWidth insertNewWBefore(int i);

    CTTblWidth addNewWBefore();

    void removeWBefore(int i);

    List<CTTblWidth> getWAfterList();

    CTTblWidth[] getWAfterArray();

    CTTblWidth getWAfterArray(int i);

    int sizeOfWAfterArray();

    void setWAfterArray(CTTblWidth[] cTTblWidthArr);

    void setWAfterArray(int i, CTTblWidth cTTblWidth);

    CTTblWidth insertNewWAfter(int i);

    CTTblWidth addNewWAfter();

    void removeWAfter(int i);

    List<CTOnOff> getCantSplitList();

    CTOnOff[] getCantSplitArray();

    CTOnOff getCantSplitArray(int i);

    int sizeOfCantSplitArray();

    void setCantSplitArray(CTOnOff[] cTOnOffArr);

    void setCantSplitArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewCantSplit(int i);

    CTOnOff addNewCantSplit();

    void removeCantSplit(int i);

    List<CTHeight> getTrHeightList();

    CTHeight[] getTrHeightArray();

    CTHeight getTrHeightArray(int i);

    int sizeOfTrHeightArray();

    void setTrHeightArray(CTHeight[] cTHeightArr);

    void setTrHeightArray(int i, CTHeight cTHeight);

    CTHeight insertNewTrHeight(int i);

    CTHeight addNewTrHeight();

    void removeTrHeight(int i);

    List<CTOnOff> getTblHeaderList();

    CTOnOff[] getTblHeaderArray();

    CTOnOff getTblHeaderArray(int i);

    int sizeOfTblHeaderArray();

    void setTblHeaderArray(CTOnOff[] cTOnOffArr);

    void setTblHeaderArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewTblHeader(int i);

    CTOnOff addNewTblHeader();

    void removeTblHeader(int i);

    List<CTTblWidth> getTblCellSpacingList();

    CTTblWidth[] getTblCellSpacingArray();

    CTTblWidth getTblCellSpacingArray(int i);

    int sizeOfTblCellSpacingArray();

    void setTblCellSpacingArray(CTTblWidth[] cTTblWidthArr);

    void setTblCellSpacingArray(int i, CTTblWidth cTTblWidth);

    CTTblWidth insertNewTblCellSpacing(int i);

    CTTblWidth addNewTblCellSpacing();

    void removeTblCellSpacing(int i);

    List<CTJc> getJcList();

    CTJc[] getJcArray();

    CTJc getJcArray(int i);

    int sizeOfJcArray();

    void setJcArray(CTJc[] cTJcArr);

    void setJcArray(int i, CTJc cTJc);

    CTJc insertNewJc(int i);

    CTJc addNewJc();

    void removeJc(int i);

    List<CTOnOff> getHiddenList();

    CTOnOff[] getHiddenArray();

    CTOnOff getHiddenArray(int i);

    int sizeOfHiddenArray();

    void setHiddenArray(CTOnOff[] cTOnOffArr);

    void setHiddenArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewHidden(int i);

    CTOnOff addNewHidden();

    void removeHidden(int i);
}
