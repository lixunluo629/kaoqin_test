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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTR.class */
public interface CTR extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTR.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctr8120type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTR$Factory.class */
    public static final class Factory {
        public static CTR newInstance() {
            return (CTR) POIXMLTypeLoader.newInstance(CTR.type, null);
        }

        public static CTR newInstance(XmlOptions xmlOptions) {
            return (CTR) POIXMLTypeLoader.newInstance(CTR.type, xmlOptions);
        }

        public static CTR parse(String str) throws XmlException {
            return (CTR) POIXMLTypeLoader.parse(str, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTR) POIXMLTypeLoader.parse(str, CTR.type, xmlOptions);
        }

        public static CTR parse(File file) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(file, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(file, CTR.type, xmlOptions);
        }

        public static CTR parse(URL url) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(url, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(url, CTR.type, xmlOptions);
        }

        public static CTR parse(InputStream inputStream) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(inputStream, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(inputStream, CTR.type, xmlOptions);
        }

        public static CTR parse(Reader reader) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(reader, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTR) POIXMLTypeLoader.parse(reader, CTR.type, xmlOptions);
        }

        public static CTR parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTR) POIXMLTypeLoader.parse(xMLStreamReader, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTR) POIXMLTypeLoader.parse(xMLStreamReader, CTR.type, xmlOptions);
        }

        public static CTR parse(Node node) throws XmlException {
            return (CTR) POIXMLTypeLoader.parse(node, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTR) POIXMLTypeLoader.parse(node, CTR.type, xmlOptions);
        }

        public static CTR parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTR) POIXMLTypeLoader.parse(xMLInputStream, CTR.type, (XmlOptions) null);
        }

        public static CTR parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTR) POIXMLTypeLoader.parse(xMLInputStream, CTR.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTR.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTR.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTRPr getRPr();

    boolean isSetRPr();

    void setRPr(CTRPr cTRPr);

    CTRPr addNewRPr();

    void unsetRPr();

    List<CTBr> getBrList();

    CTBr[] getBrArray();

    CTBr getBrArray(int i);

    int sizeOfBrArray();

    void setBrArray(CTBr[] cTBrArr);

    void setBrArray(int i, CTBr cTBr);

    CTBr insertNewBr(int i);

    CTBr addNewBr();

    void removeBr(int i);

    List<CTText> getTList();

    CTText[] getTArray();

    CTText getTArray(int i);

    int sizeOfTArray();

    void setTArray(CTText[] cTTextArr);

    void setTArray(int i, CTText cTText);

    CTText insertNewT(int i);

    CTText addNewT();

    void removeT(int i);

    List<CTText> getDelTextList();

    CTText[] getDelTextArray();

    CTText getDelTextArray(int i);

    int sizeOfDelTextArray();

    void setDelTextArray(CTText[] cTTextArr);

    void setDelTextArray(int i, CTText cTText);

    CTText insertNewDelText(int i);

    CTText addNewDelText();

    void removeDelText(int i);

    List<CTText> getInstrTextList();

    CTText[] getInstrTextArray();

    CTText getInstrTextArray(int i);

    int sizeOfInstrTextArray();

    void setInstrTextArray(CTText[] cTTextArr);

    void setInstrTextArray(int i, CTText cTText);

    CTText insertNewInstrText(int i);

    CTText addNewInstrText();

    void removeInstrText(int i);

    List<CTText> getDelInstrTextList();

    CTText[] getDelInstrTextArray();

    CTText getDelInstrTextArray(int i);

    int sizeOfDelInstrTextArray();

    void setDelInstrTextArray(CTText[] cTTextArr);

    void setDelInstrTextArray(int i, CTText cTText);

    CTText insertNewDelInstrText(int i);

    CTText addNewDelInstrText();

    void removeDelInstrText(int i);

    List<CTEmpty> getNoBreakHyphenList();

    CTEmpty[] getNoBreakHyphenArray();

    CTEmpty getNoBreakHyphenArray(int i);

    int sizeOfNoBreakHyphenArray();

    void setNoBreakHyphenArray(CTEmpty[] cTEmptyArr);

    void setNoBreakHyphenArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewNoBreakHyphen(int i);

    CTEmpty addNewNoBreakHyphen();

    void removeNoBreakHyphen(int i);

    List<CTEmpty> getSoftHyphenList();

    CTEmpty[] getSoftHyphenArray();

    CTEmpty getSoftHyphenArray(int i);

    int sizeOfSoftHyphenArray();

    void setSoftHyphenArray(CTEmpty[] cTEmptyArr);

    void setSoftHyphenArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewSoftHyphen(int i);

    CTEmpty addNewSoftHyphen();

    void removeSoftHyphen(int i);

    List<CTEmpty> getDayShortList();

    CTEmpty[] getDayShortArray();

    CTEmpty getDayShortArray(int i);

    int sizeOfDayShortArray();

    void setDayShortArray(CTEmpty[] cTEmptyArr);

    void setDayShortArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewDayShort(int i);

    CTEmpty addNewDayShort();

    void removeDayShort(int i);

    List<CTEmpty> getMonthShortList();

    CTEmpty[] getMonthShortArray();

    CTEmpty getMonthShortArray(int i);

    int sizeOfMonthShortArray();

    void setMonthShortArray(CTEmpty[] cTEmptyArr);

    void setMonthShortArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewMonthShort(int i);

    CTEmpty addNewMonthShort();

    void removeMonthShort(int i);

    List<CTEmpty> getYearShortList();

    CTEmpty[] getYearShortArray();

    CTEmpty getYearShortArray(int i);

    int sizeOfYearShortArray();

    void setYearShortArray(CTEmpty[] cTEmptyArr);

    void setYearShortArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewYearShort(int i);

    CTEmpty addNewYearShort();

    void removeYearShort(int i);

    List<CTEmpty> getDayLongList();

    CTEmpty[] getDayLongArray();

    CTEmpty getDayLongArray(int i);

    int sizeOfDayLongArray();

    void setDayLongArray(CTEmpty[] cTEmptyArr);

    void setDayLongArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewDayLong(int i);

    CTEmpty addNewDayLong();

    void removeDayLong(int i);

    List<CTEmpty> getMonthLongList();

    CTEmpty[] getMonthLongArray();

    CTEmpty getMonthLongArray(int i);

    int sizeOfMonthLongArray();

    void setMonthLongArray(CTEmpty[] cTEmptyArr);

    void setMonthLongArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewMonthLong(int i);

    CTEmpty addNewMonthLong();

    void removeMonthLong(int i);

    List<CTEmpty> getYearLongList();

    CTEmpty[] getYearLongArray();

    CTEmpty getYearLongArray(int i);

    int sizeOfYearLongArray();

    void setYearLongArray(CTEmpty[] cTEmptyArr);

    void setYearLongArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewYearLong(int i);

    CTEmpty addNewYearLong();

    void removeYearLong(int i);

    List<CTEmpty> getAnnotationRefList();

    CTEmpty[] getAnnotationRefArray();

    CTEmpty getAnnotationRefArray(int i);

    int sizeOfAnnotationRefArray();

    void setAnnotationRefArray(CTEmpty[] cTEmptyArr);

    void setAnnotationRefArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewAnnotationRef(int i);

    CTEmpty addNewAnnotationRef();

    void removeAnnotationRef(int i);

    List<CTEmpty> getFootnoteRefList();

    CTEmpty[] getFootnoteRefArray();

    CTEmpty getFootnoteRefArray(int i);

    int sizeOfFootnoteRefArray();

    void setFootnoteRefArray(CTEmpty[] cTEmptyArr);

    void setFootnoteRefArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewFootnoteRef(int i);

    CTEmpty addNewFootnoteRef();

    void removeFootnoteRef(int i);

    List<CTEmpty> getEndnoteRefList();

    CTEmpty[] getEndnoteRefArray();

    CTEmpty getEndnoteRefArray(int i);

    int sizeOfEndnoteRefArray();

    void setEndnoteRefArray(CTEmpty[] cTEmptyArr);

    void setEndnoteRefArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewEndnoteRef(int i);

    CTEmpty addNewEndnoteRef();

    void removeEndnoteRef(int i);

    List<CTEmpty> getSeparatorList();

    CTEmpty[] getSeparatorArray();

    CTEmpty getSeparatorArray(int i);

    int sizeOfSeparatorArray();

    void setSeparatorArray(CTEmpty[] cTEmptyArr);

    void setSeparatorArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewSeparator(int i);

    CTEmpty addNewSeparator();

    void removeSeparator(int i);

    List<CTEmpty> getContinuationSeparatorList();

    CTEmpty[] getContinuationSeparatorArray();

    CTEmpty getContinuationSeparatorArray(int i);

    int sizeOfContinuationSeparatorArray();

    void setContinuationSeparatorArray(CTEmpty[] cTEmptyArr);

    void setContinuationSeparatorArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewContinuationSeparator(int i);

    CTEmpty addNewContinuationSeparator();

    void removeContinuationSeparator(int i);

    List<CTSym> getSymList();

    CTSym[] getSymArray();

    CTSym getSymArray(int i);

    int sizeOfSymArray();

    void setSymArray(CTSym[] cTSymArr);

    void setSymArray(int i, CTSym cTSym);

    CTSym insertNewSym(int i);

    CTSym addNewSym();

    void removeSym(int i);

    List<CTEmpty> getPgNumList();

    CTEmpty[] getPgNumArray();

    CTEmpty getPgNumArray(int i);

    int sizeOfPgNumArray();

    void setPgNumArray(CTEmpty[] cTEmptyArr);

    void setPgNumArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewPgNum(int i);

    CTEmpty addNewPgNum();

    void removePgNum(int i);

    List<CTEmpty> getCrList();

    CTEmpty[] getCrArray();

    CTEmpty getCrArray(int i);

    int sizeOfCrArray();

    void setCrArray(CTEmpty[] cTEmptyArr);

    void setCrArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewCr(int i);

    CTEmpty addNewCr();

    void removeCr(int i);

    List<CTEmpty> getTabList();

    CTEmpty[] getTabArray();

    CTEmpty getTabArray(int i);

    int sizeOfTabArray();

    void setTabArray(CTEmpty[] cTEmptyArr);

    void setTabArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewTab(int i);

    CTEmpty addNewTab();

    void removeTab(int i);

    List<CTObject> getObjectList();

    CTObject[] getObjectArray();

    CTObject getObjectArray(int i);

    int sizeOfObjectArray();

    void setObjectArray(CTObject[] cTObjectArr);

    void setObjectArray(int i, CTObject cTObject);

    CTObject insertNewObject(int i);

    CTObject addNewObject();

    void removeObject(int i);

    List<CTPicture> getPictList();

    CTPicture[] getPictArray();

    CTPicture getPictArray(int i);

    int sizeOfPictArray();

    void setPictArray(CTPicture[] cTPictureArr);

    void setPictArray(int i, CTPicture cTPicture);

    CTPicture insertNewPict(int i);

    CTPicture addNewPict();

    void removePict(int i);

    List<CTFldChar> getFldCharList();

    CTFldChar[] getFldCharArray();

    CTFldChar getFldCharArray(int i);

    int sizeOfFldCharArray();

    void setFldCharArray(CTFldChar[] cTFldCharArr);

    void setFldCharArray(int i, CTFldChar cTFldChar);

    CTFldChar insertNewFldChar(int i);

    CTFldChar addNewFldChar();

    void removeFldChar(int i);

    List<CTRuby> getRubyList();

    CTRuby[] getRubyArray();

    CTRuby getRubyArray(int i);

    int sizeOfRubyArray();

    void setRubyArray(CTRuby[] cTRubyArr);

    void setRubyArray(int i, CTRuby cTRuby);

    CTRuby insertNewRuby(int i);

    CTRuby addNewRuby();

    void removeRuby(int i);

    List<CTFtnEdnRef> getFootnoteReferenceList();

    CTFtnEdnRef[] getFootnoteReferenceArray();

    CTFtnEdnRef getFootnoteReferenceArray(int i);

    int sizeOfFootnoteReferenceArray();

    void setFootnoteReferenceArray(CTFtnEdnRef[] cTFtnEdnRefArr);

    void setFootnoteReferenceArray(int i, CTFtnEdnRef cTFtnEdnRef);

    CTFtnEdnRef insertNewFootnoteReference(int i);

    CTFtnEdnRef addNewFootnoteReference();

    void removeFootnoteReference(int i);

    List<CTFtnEdnRef> getEndnoteReferenceList();

    CTFtnEdnRef[] getEndnoteReferenceArray();

    CTFtnEdnRef getEndnoteReferenceArray(int i);

    int sizeOfEndnoteReferenceArray();

    void setEndnoteReferenceArray(CTFtnEdnRef[] cTFtnEdnRefArr);

    void setEndnoteReferenceArray(int i, CTFtnEdnRef cTFtnEdnRef);

    CTFtnEdnRef insertNewEndnoteReference(int i);

    CTFtnEdnRef addNewEndnoteReference();

    void removeEndnoteReference(int i);

    List<CTMarkup> getCommentReferenceList();

    CTMarkup[] getCommentReferenceArray();

    CTMarkup getCommentReferenceArray(int i);

    int sizeOfCommentReferenceArray();

    void setCommentReferenceArray(CTMarkup[] cTMarkupArr);

    void setCommentReferenceArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCommentReference(int i);

    CTMarkup addNewCommentReference();

    void removeCommentReference(int i);

    List<CTDrawing> getDrawingList();

    CTDrawing[] getDrawingArray();

    CTDrawing getDrawingArray(int i);

    int sizeOfDrawingArray();

    void setDrawingArray(CTDrawing[] cTDrawingArr);

    void setDrawingArray(int i, CTDrawing cTDrawing);

    CTDrawing insertNewDrawing(int i);

    CTDrawing addNewDrawing();

    void removeDrawing(int i);

    List<CTPTab> getPtabList();

    CTPTab[] getPtabArray();

    CTPTab getPtabArray(int i);

    int sizeOfPtabArray();

    void setPtabArray(CTPTab[] cTPTabArr);

    void setPtabArray(int i, CTPTab cTPTab);

    CTPTab insertNewPtab(int i);

    CTPTab addNewPtab();

    void removePtab(int i);

    List<CTEmpty> getLastRenderedPageBreakList();

    CTEmpty[] getLastRenderedPageBreakArray();

    CTEmpty getLastRenderedPageBreakArray(int i);

    int sizeOfLastRenderedPageBreakArray();

    void setLastRenderedPageBreakArray(CTEmpty[] cTEmptyArr);

    void setLastRenderedPageBreakArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewLastRenderedPageBreak(int i);

    CTEmpty addNewLastRenderedPageBreak();

    void removeLastRenderedPageBreak(int i);

    byte[] getRsidRPr();

    STLongHexNumber xgetRsidRPr();

    boolean isSetRsidRPr();

    void setRsidRPr(byte[] bArr);

    void xsetRsidRPr(STLongHexNumber sTLongHexNumber);

    void unsetRsidRPr();

    byte[] getRsidDel();

    STLongHexNumber xgetRsidDel();

    boolean isSetRsidDel();

    void setRsidDel(byte[] bArr);

    void xsetRsidDel(STLongHexNumber sTLongHexNumber);

    void unsetRsidDel();

    byte[] getRsidR();

    STLongHexNumber xgetRsidR();

    boolean isSetRsidR();

    void setRsidR(byte[] bArr);

    void xsetRsidR(STLongHexNumber sTLongHexNumber);

    void unsetRsidR();
}
