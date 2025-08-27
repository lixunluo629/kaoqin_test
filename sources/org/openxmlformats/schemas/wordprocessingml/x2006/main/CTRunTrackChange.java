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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTBar;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTBox;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTD;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTF;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTM;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTNary;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTRad;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRunTrackChange.class */
public interface CTRunTrackChange extends CTTrackChange {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRunTrackChange.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctruntrackchangea458type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRunTrackChange$Factory.class */
    public static final class Factory {
        public static CTRunTrackChange newInstance() {
            return (CTRunTrackChange) POIXMLTypeLoader.newInstance(CTRunTrackChange.type, null);
        }

        public static CTRunTrackChange newInstance(XmlOptions xmlOptions) {
            return (CTRunTrackChange) POIXMLTypeLoader.newInstance(CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(String str) throws XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(str, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(str, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(File file) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(file, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(file, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(URL url) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(url, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(url, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(inputStream, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(inputStream, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(Reader reader) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(reader, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(reader, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(xMLStreamReader, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(xMLStreamReader, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(Node node) throws XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(node, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(node, CTRunTrackChange.type, xmlOptions);
        }

        public static CTRunTrackChange parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(xMLInputStream, CTRunTrackChange.type, (XmlOptions) null);
        }

        public static CTRunTrackChange parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRunTrackChange) POIXMLTypeLoader.parse(xMLInputStream, CTRunTrackChange.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRunTrackChange.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRunTrackChange.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCustomXmlRun> getCustomXmlList();

    CTCustomXmlRun[] getCustomXmlArray();

    CTCustomXmlRun getCustomXmlArray(int i);

    int sizeOfCustomXmlArray();

    void setCustomXmlArray(CTCustomXmlRun[] cTCustomXmlRunArr);

    void setCustomXmlArray(int i, CTCustomXmlRun cTCustomXmlRun);

    CTCustomXmlRun insertNewCustomXml(int i);

    CTCustomXmlRun addNewCustomXml();

    void removeCustomXml(int i);

    List<CTSmartTagRun> getSmartTagList();

    CTSmartTagRun[] getSmartTagArray();

    CTSmartTagRun getSmartTagArray(int i);

    int sizeOfSmartTagArray();

    void setSmartTagArray(CTSmartTagRun[] cTSmartTagRunArr);

    void setSmartTagArray(int i, CTSmartTagRun cTSmartTagRun);

    CTSmartTagRun insertNewSmartTag(int i);

    CTSmartTagRun addNewSmartTag();

    void removeSmartTag(int i);

    List<CTSdtRun> getSdtList();

    CTSdtRun[] getSdtArray();

    CTSdtRun getSdtArray(int i);

    int sizeOfSdtArray();

    void setSdtArray(CTSdtRun[] cTSdtRunArr);

    void setSdtArray(int i, CTSdtRun cTSdtRun);

    CTSdtRun insertNewSdt(int i);

    CTSdtRun addNewSdt();

    void removeSdt(int i);

    List<CTR> getRList();

    CTR[] getRArray();

    CTR getRArray(int i);

    int sizeOfRArray();

    void setRArray(CTR[] ctrArr);

    void setRArray(int i, CTR ctr);

    CTR insertNewR(int i);

    CTR addNewR();

    void removeR(int i);

    List<CTProofErr> getProofErrList();

    CTProofErr[] getProofErrArray();

    CTProofErr getProofErrArray(int i);

    int sizeOfProofErrArray();

    void setProofErrArray(CTProofErr[] cTProofErrArr);

    void setProofErrArray(int i, CTProofErr cTProofErr);

    CTProofErr insertNewProofErr(int i);

    CTProofErr addNewProofErr();

    void removeProofErr(int i);

    List<CTPermStart> getPermStartList();

    CTPermStart[] getPermStartArray();

    CTPermStart getPermStartArray(int i);

    int sizeOfPermStartArray();

    void setPermStartArray(CTPermStart[] cTPermStartArr);

    void setPermStartArray(int i, CTPermStart cTPermStart);

    CTPermStart insertNewPermStart(int i);

    CTPermStart addNewPermStart();

    void removePermStart(int i);

    List<CTPerm> getPermEndList();

    CTPerm[] getPermEndArray();

    CTPerm getPermEndArray(int i);

    int sizeOfPermEndArray();

    void setPermEndArray(CTPerm[] cTPermArr);

    void setPermEndArray(int i, CTPerm cTPerm);

    CTPerm insertNewPermEnd(int i);

    CTPerm addNewPermEnd();

    void removePermEnd(int i);

    List<CTBookmark> getBookmarkStartList();

    CTBookmark[] getBookmarkStartArray();

    CTBookmark getBookmarkStartArray(int i);

    int sizeOfBookmarkStartArray();

    void setBookmarkStartArray(CTBookmark[] cTBookmarkArr);

    void setBookmarkStartArray(int i, CTBookmark cTBookmark);

    CTBookmark insertNewBookmarkStart(int i);

    CTBookmark addNewBookmarkStart();

    void removeBookmarkStart(int i);

    List<CTMarkupRange> getBookmarkEndList();

    CTMarkupRange[] getBookmarkEndArray();

    CTMarkupRange getBookmarkEndArray(int i);

    int sizeOfBookmarkEndArray();

    void setBookmarkEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setBookmarkEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewBookmarkEnd(int i);

    CTMarkupRange addNewBookmarkEnd();

    void removeBookmarkEnd(int i);

    List<CTMoveBookmark> getMoveFromRangeStartList();

    CTMoveBookmark[] getMoveFromRangeStartArray();

    CTMoveBookmark getMoveFromRangeStartArray(int i);

    int sizeOfMoveFromRangeStartArray();

    void setMoveFromRangeStartArray(CTMoveBookmark[] cTMoveBookmarkArr);

    void setMoveFromRangeStartArray(int i, CTMoveBookmark cTMoveBookmark);

    CTMoveBookmark insertNewMoveFromRangeStart(int i);

    CTMoveBookmark addNewMoveFromRangeStart();

    void removeMoveFromRangeStart(int i);

    List<CTMarkupRange> getMoveFromRangeEndList();

    CTMarkupRange[] getMoveFromRangeEndArray();

    CTMarkupRange getMoveFromRangeEndArray(int i);

    int sizeOfMoveFromRangeEndArray();

    void setMoveFromRangeEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setMoveFromRangeEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewMoveFromRangeEnd(int i);

    CTMarkupRange addNewMoveFromRangeEnd();

    void removeMoveFromRangeEnd(int i);

    List<CTMoveBookmark> getMoveToRangeStartList();

    CTMoveBookmark[] getMoveToRangeStartArray();

    CTMoveBookmark getMoveToRangeStartArray(int i);

    int sizeOfMoveToRangeStartArray();

    void setMoveToRangeStartArray(CTMoveBookmark[] cTMoveBookmarkArr);

    void setMoveToRangeStartArray(int i, CTMoveBookmark cTMoveBookmark);

    CTMoveBookmark insertNewMoveToRangeStart(int i);

    CTMoveBookmark addNewMoveToRangeStart();

    void removeMoveToRangeStart(int i);

    List<CTMarkupRange> getMoveToRangeEndList();

    CTMarkupRange[] getMoveToRangeEndArray();

    CTMarkupRange getMoveToRangeEndArray(int i);

    int sizeOfMoveToRangeEndArray();

    void setMoveToRangeEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setMoveToRangeEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewMoveToRangeEnd(int i);

    CTMarkupRange addNewMoveToRangeEnd();

    void removeMoveToRangeEnd(int i);

    List<CTMarkupRange> getCommentRangeStartList();

    CTMarkupRange[] getCommentRangeStartArray();

    CTMarkupRange getCommentRangeStartArray(int i);

    int sizeOfCommentRangeStartArray();

    void setCommentRangeStartArray(CTMarkupRange[] cTMarkupRangeArr);

    void setCommentRangeStartArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewCommentRangeStart(int i);

    CTMarkupRange addNewCommentRangeStart();

    void removeCommentRangeStart(int i);

    List<CTMarkupRange> getCommentRangeEndList();

    CTMarkupRange[] getCommentRangeEndArray();

    CTMarkupRange getCommentRangeEndArray(int i);

    int sizeOfCommentRangeEndArray();

    void setCommentRangeEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setCommentRangeEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewCommentRangeEnd(int i);

    CTMarkupRange addNewCommentRangeEnd();

    void removeCommentRangeEnd(int i);

    List<CTTrackChange> getCustomXmlInsRangeStartList();

    CTTrackChange[] getCustomXmlInsRangeStartArray();

    CTTrackChange getCustomXmlInsRangeStartArray(int i);

    int sizeOfCustomXmlInsRangeStartArray();

    void setCustomXmlInsRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlInsRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlInsRangeStart(int i);

    CTTrackChange addNewCustomXmlInsRangeStart();

    void removeCustomXmlInsRangeStart(int i);

    List<CTMarkup> getCustomXmlInsRangeEndList();

    CTMarkup[] getCustomXmlInsRangeEndArray();

    CTMarkup getCustomXmlInsRangeEndArray(int i);

    int sizeOfCustomXmlInsRangeEndArray();

    void setCustomXmlInsRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlInsRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlInsRangeEnd(int i);

    CTMarkup addNewCustomXmlInsRangeEnd();

    void removeCustomXmlInsRangeEnd(int i);

    List<CTTrackChange> getCustomXmlDelRangeStartList();

    CTTrackChange[] getCustomXmlDelRangeStartArray();

    CTTrackChange getCustomXmlDelRangeStartArray(int i);

    int sizeOfCustomXmlDelRangeStartArray();

    void setCustomXmlDelRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlDelRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlDelRangeStart(int i);

    CTTrackChange addNewCustomXmlDelRangeStart();

    void removeCustomXmlDelRangeStart(int i);

    List<CTMarkup> getCustomXmlDelRangeEndList();

    CTMarkup[] getCustomXmlDelRangeEndArray();

    CTMarkup getCustomXmlDelRangeEndArray(int i);

    int sizeOfCustomXmlDelRangeEndArray();

    void setCustomXmlDelRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlDelRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlDelRangeEnd(int i);

    CTMarkup addNewCustomXmlDelRangeEnd();

    void removeCustomXmlDelRangeEnd(int i);

    List<CTTrackChange> getCustomXmlMoveFromRangeStartList();

    CTTrackChange[] getCustomXmlMoveFromRangeStartArray();

    CTTrackChange getCustomXmlMoveFromRangeStartArray(int i);

    int sizeOfCustomXmlMoveFromRangeStartArray();

    void setCustomXmlMoveFromRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlMoveFromRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlMoveFromRangeStart(int i);

    CTTrackChange addNewCustomXmlMoveFromRangeStart();

    void removeCustomXmlMoveFromRangeStart(int i);

    List<CTMarkup> getCustomXmlMoveFromRangeEndList();

    CTMarkup[] getCustomXmlMoveFromRangeEndArray();

    CTMarkup getCustomXmlMoveFromRangeEndArray(int i);

    int sizeOfCustomXmlMoveFromRangeEndArray();

    void setCustomXmlMoveFromRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlMoveFromRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlMoveFromRangeEnd(int i);

    CTMarkup addNewCustomXmlMoveFromRangeEnd();

    void removeCustomXmlMoveFromRangeEnd(int i);

    List<CTTrackChange> getCustomXmlMoveToRangeStartList();

    CTTrackChange[] getCustomXmlMoveToRangeStartArray();

    CTTrackChange getCustomXmlMoveToRangeStartArray(int i);

    int sizeOfCustomXmlMoveToRangeStartArray();

    void setCustomXmlMoveToRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlMoveToRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlMoveToRangeStart(int i);

    CTTrackChange addNewCustomXmlMoveToRangeStart();

    void removeCustomXmlMoveToRangeStart(int i);

    List<CTMarkup> getCustomXmlMoveToRangeEndList();

    CTMarkup[] getCustomXmlMoveToRangeEndArray();

    CTMarkup getCustomXmlMoveToRangeEndArray(int i);

    int sizeOfCustomXmlMoveToRangeEndArray();

    void setCustomXmlMoveToRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlMoveToRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlMoveToRangeEnd(int i);

    CTMarkup addNewCustomXmlMoveToRangeEnd();

    void removeCustomXmlMoveToRangeEnd(int i);

    List<CTRunTrackChange> getInsList();

    CTRunTrackChange[] getInsArray();

    CTRunTrackChange getInsArray(int i);

    int sizeOfInsArray();

    void setInsArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setInsArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewIns(int i);

    CTRunTrackChange addNewIns();

    void removeIns(int i);

    List<CTRunTrackChange> getDelList();

    CTRunTrackChange[] getDelArray();

    CTRunTrackChange getDelArray(int i);

    int sizeOfDelArray();

    void setDelArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setDelArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewDel(int i);

    CTRunTrackChange addNewDel();

    void removeDel(int i);

    List<CTRunTrackChange> getMoveFromList();

    CTRunTrackChange[] getMoveFromArray();

    CTRunTrackChange getMoveFromArray(int i);

    int sizeOfMoveFromArray();

    void setMoveFromArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setMoveFromArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewMoveFrom(int i);

    CTRunTrackChange addNewMoveFrom();

    void removeMoveFrom(int i);

    List<CTRunTrackChange> getMoveToList();

    CTRunTrackChange[] getMoveToArray();

    CTRunTrackChange getMoveToArray(int i);

    int sizeOfMoveToArray();

    void setMoveToArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setMoveToArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewMoveTo(int i);

    CTRunTrackChange addNewMoveTo();

    void removeMoveTo(int i);

    List<CTOMathPara> getOMathParaList();

    CTOMathPara[] getOMathParaArray();

    CTOMathPara getOMathParaArray(int i);

    int sizeOfOMathParaArray();

    void setOMathParaArray(CTOMathPara[] cTOMathParaArr);

    void setOMathParaArray(int i, CTOMathPara cTOMathPara);

    CTOMathPara insertNewOMathPara(int i);

    CTOMathPara addNewOMathPara();

    void removeOMathPara(int i);

    List<CTOMath> getOMathList();

    CTOMath[] getOMathArray();

    CTOMath getOMathArray(int i);

    int sizeOfOMathArray();

    void setOMathArray(CTOMath[] cTOMathArr);

    void setOMathArray(int i, CTOMath cTOMath);

    CTOMath insertNewOMath(int i);

    CTOMath addNewOMath();

    void removeOMath(int i);

    List<CTAcc> getAccList();

    CTAcc[] getAccArray();

    CTAcc getAccArray(int i);

    int sizeOfAccArray();

    void setAccArray(CTAcc[] cTAccArr);

    void setAccArray(int i, CTAcc cTAcc);

    CTAcc insertNewAcc(int i);

    CTAcc addNewAcc();

    void removeAcc(int i);

    List<CTBar> getBarList();

    CTBar[] getBarArray();

    CTBar getBarArray(int i);

    int sizeOfBarArray();

    void setBarArray(CTBar[] cTBarArr);

    void setBarArray(int i, CTBar cTBar);

    CTBar insertNewBar(int i);

    CTBar addNewBar();

    void removeBar(int i);

    List<CTBox> getBoxList();

    CTBox[] getBoxArray();

    CTBox getBoxArray(int i);

    int sizeOfBoxArray();

    void setBoxArray(CTBox[] cTBoxArr);

    void setBoxArray(int i, CTBox cTBox);

    CTBox insertNewBox(int i);

    CTBox addNewBox();

    void removeBox(int i);

    List<CTBorderBox> getBorderBoxList();

    CTBorderBox[] getBorderBoxArray();

    CTBorderBox getBorderBoxArray(int i);

    int sizeOfBorderBoxArray();

    void setBorderBoxArray(CTBorderBox[] cTBorderBoxArr);

    void setBorderBoxArray(int i, CTBorderBox cTBorderBox);

    CTBorderBox insertNewBorderBox(int i);

    CTBorderBox addNewBorderBox();

    void removeBorderBox(int i);

    List<CTD> getDList();

    CTD[] getDArray();

    CTD getDArray(int i);

    int sizeOfDArray();

    void setDArray(CTD[] ctdArr);

    void setDArray(int i, CTD ctd);

    CTD insertNewD(int i);

    CTD addNewD();

    void removeD(int i);

    List<CTEqArr> getEqArrList();

    CTEqArr[] getEqArrArray();

    CTEqArr getEqArrArray(int i);

    int sizeOfEqArrArray();

    void setEqArrArray(CTEqArr[] cTEqArrArr);

    void setEqArrArray(int i, CTEqArr cTEqArr);

    CTEqArr insertNewEqArr(int i);

    CTEqArr addNewEqArr();

    void removeEqArr(int i);

    List<CTF> getFList();

    CTF[] getFArray();

    CTF getFArray(int i);

    int sizeOfFArray();

    void setFArray(CTF[] ctfArr);

    void setFArray(int i, CTF ctf);

    CTF insertNewF(int i);

    CTF addNewF();

    void removeF(int i);

    List<CTFunc> getFuncList();

    CTFunc[] getFuncArray();

    CTFunc getFuncArray(int i);

    int sizeOfFuncArray();

    void setFuncArray(CTFunc[] cTFuncArr);

    void setFuncArray(int i, CTFunc cTFunc);

    CTFunc insertNewFunc(int i);

    CTFunc addNewFunc();

    void removeFunc(int i);

    List<CTGroupChr> getGroupChrList();

    CTGroupChr[] getGroupChrArray();

    CTGroupChr getGroupChrArray(int i);

    int sizeOfGroupChrArray();

    void setGroupChrArray(CTGroupChr[] cTGroupChrArr);

    void setGroupChrArray(int i, CTGroupChr cTGroupChr);

    CTGroupChr insertNewGroupChr(int i);

    CTGroupChr addNewGroupChr();

    void removeGroupChr(int i);

    List<CTLimLow> getLimLowList();

    CTLimLow[] getLimLowArray();

    CTLimLow getLimLowArray(int i);

    int sizeOfLimLowArray();

    void setLimLowArray(CTLimLow[] cTLimLowArr);

    void setLimLowArray(int i, CTLimLow cTLimLow);

    CTLimLow insertNewLimLow(int i);

    CTLimLow addNewLimLow();

    void removeLimLow(int i);

    List<CTLimUpp> getLimUppList();

    CTLimUpp[] getLimUppArray();

    CTLimUpp getLimUppArray(int i);

    int sizeOfLimUppArray();

    void setLimUppArray(CTLimUpp[] cTLimUppArr);

    void setLimUppArray(int i, CTLimUpp cTLimUpp);

    CTLimUpp insertNewLimUpp(int i);

    CTLimUpp addNewLimUpp();

    void removeLimUpp(int i);

    List<CTM> getMList();

    CTM[] getMArray();

    CTM getMArray(int i);

    int sizeOfMArray();

    void setMArray(CTM[] ctmArr);

    void setMArray(int i, CTM ctm);

    CTM insertNewM(int i);

    CTM addNewM();

    void removeM(int i);

    List<CTNary> getNaryList();

    CTNary[] getNaryArray();

    CTNary getNaryArray(int i);

    int sizeOfNaryArray();

    void setNaryArray(CTNary[] cTNaryArr);

    void setNaryArray(int i, CTNary cTNary);

    CTNary insertNewNary(int i);

    CTNary addNewNary();

    void removeNary(int i);

    List<CTPhant> getPhantList();

    CTPhant[] getPhantArray();

    CTPhant getPhantArray(int i);

    int sizeOfPhantArray();

    void setPhantArray(CTPhant[] cTPhantArr);

    void setPhantArray(int i, CTPhant cTPhant);

    CTPhant insertNewPhant(int i);

    CTPhant addNewPhant();

    void removePhant(int i);

    List<CTRad> getRadList();

    CTRad[] getRadArray();

    CTRad getRadArray(int i);

    int sizeOfRadArray();

    void setRadArray(CTRad[] cTRadArr);

    void setRadArray(int i, CTRad cTRad);

    CTRad insertNewRad(int i);

    CTRad addNewRad();

    void removeRad(int i);

    List<CTSPre> getSPreList();

    CTSPre[] getSPreArray();

    CTSPre getSPreArray(int i);

    int sizeOfSPreArray();

    void setSPreArray(CTSPre[] cTSPreArr);

    void setSPreArray(int i, CTSPre cTSPre);

    CTSPre insertNewSPre(int i);

    CTSPre addNewSPre();

    void removeSPre(int i);

    List<CTSSub> getSSubList();

    CTSSub[] getSSubArray();

    CTSSub getSSubArray(int i);

    int sizeOfSSubArray();

    void setSSubArray(CTSSub[] cTSSubArr);

    void setSSubArray(int i, CTSSub cTSSub);

    CTSSub insertNewSSub(int i);

    CTSSub addNewSSub();

    void removeSSub(int i);

    List<CTSSubSup> getSSubSupList();

    CTSSubSup[] getSSubSupArray();

    CTSSubSup getSSubSupArray(int i);

    int sizeOfSSubSupArray();

    void setSSubSupArray(CTSSubSup[] cTSSubSupArr);

    void setSSubSupArray(int i, CTSSubSup cTSSubSup);

    CTSSubSup insertNewSSubSup(int i);

    CTSSubSup addNewSSubSup();

    void removeSSubSup(int i);

    List<CTSSup> getSSupList();

    CTSSup[] getSSupArray();

    CTSSup getSSupArray(int i);

    int sizeOfSSupArray();

    void setSSupArray(CTSSup[] cTSSupArr);

    void setSSupArray(int i, CTSSup cTSSup);

    CTSSup insertNewSSup(int i);

    CTSSup addNewSSup();

    void removeSSup(int i);

    List<org.openxmlformats.schemas.officeDocument.x2006.math.CTR> getR2List();

    org.openxmlformats.schemas.officeDocument.x2006.math.CTR[] getR2Array();

    org.openxmlformats.schemas.officeDocument.x2006.math.CTR getR2Array(int i);

    int sizeOfR2Array();

    void setR2Array(org.openxmlformats.schemas.officeDocument.x2006.math.CTR[] ctrArr);

    void setR2Array(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTR ctr);

    org.openxmlformats.schemas.officeDocument.x2006.math.CTR insertNewR2(int i);

    org.openxmlformats.schemas.officeDocument.x2006.math.CTR addNewR2();

    void removeR2(int i);
}
