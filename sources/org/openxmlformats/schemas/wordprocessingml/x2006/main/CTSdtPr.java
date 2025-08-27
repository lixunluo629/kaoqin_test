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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSdtPr.class */
public interface CTSdtPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSdtPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsdtpre24dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSdtPr$Factory.class */
    public static final class Factory {
        public static CTSdtPr newInstance() {
            return (CTSdtPr) POIXMLTypeLoader.newInstance(CTSdtPr.type, null);
        }

        public static CTSdtPr newInstance(XmlOptions xmlOptions) {
            return (CTSdtPr) POIXMLTypeLoader.newInstance(CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(String str) throws XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(str, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(str, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(File file) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(file, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(file, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(URL url) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(url, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(url, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(inputStream, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(inputStream, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(Reader reader) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(reader, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtPr) POIXMLTypeLoader.parse(reader, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(Node node) throws XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(node, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(node, CTSdtPr.type, xmlOptions);
        }

        public static CTSdtPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(xMLInputStream, CTSdtPr.type, (XmlOptions) null);
        }

        public static CTSdtPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSdtPr) POIXMLTypeLoader.parse(xMLInputStream, CTSdtPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSdtPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSdtPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTRPr> getRPrList();

    CTRPr[] getRPrArray();

    CTRPr getRPrArray(int i);

    int sizeOfRPrArray();

    void setRPrArray(CTRPr[] cTRPrArr);

    void setRPrArray(int i, CTRPr cTRPr);

    CTRPr insertNewRPr(int i);

    CTRPr addNewRPr();

    void removeRPr(int i);

    List<CTString> getAliasList();

    CTString[] getAliasArray();

    CTString getAliasArray(int i);

    int sizeOfAliasArray();

    void setAliasArray(CTString[] cTStringArr);

    void setAliasArray(int i, CTString cTString);

    CTString insertNewAlias(int i);

    CTString addNewAlias();

    void removeAlias(int i);

    List<CTLock> getLockList();

    CTLock[] getLockArray();

    CTLock getLockArray(int i);

    int sizeOfLockArray();

    void setLockArray(CTLock[] cTLockArr);

    void setLockArray(int i, CTLock cTLock);

    CTLock insertNewLock(int i);

    CTLock addNewLock();

    void removeLock(int i);

    List<CTPlaceholder> getPlaceholderList();

    CTPlaceholder[] getPlaceholderArray();

    CTPlaceholder getPlaceholderArray(int i);

    int sizeOfPlaceholderArray();

    void setPlaceholderArray(CTPlaceholder[] cTPlaceholderArr);

    void setPlaceholderArray(int i, CTPlaceholder cTPlaceholder);

    CTPlaceholder insertNewPlaceholder(int i);

    CTPlaceholder addNewPlaceholder();

    void removePlaceholder(int i);

    List<CTOnOff> getShowingPlcHdrList();

    CTOnOff[] getShowingPlcHdrArray();

    CTOnOff getShowingPlcHdrArray(int i);

    int sizeOfShowingPlcHdrArray();

    void setShowingPlcHdrArray(CTOnOff[] cTOnOffArr);

    void setShowingPlcHdrArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewShowingPlcHdr(int i);

    CTOnOff addNewShowingPlcHdr();

    void removeShowingPlcHdr(int i);

    List<CTDataBinding> getDataBindingList();

    CTDataBinding[] getDataBindingArray();

    CTDataBinding getDataBindingArray(int i);

    int sizeOfDataBindingArray();

    void setDataBindingArray(CTDataBinding[] cTDataBindingArr);

    void setDataBindingArray(int i, CTDataBinding cTDataBinding);

    CTDataBinding insertNewDataBinding(int i);

    CTDataBinding addNewDataBinding();

    void removeDataBinding(int i);

    List<CTOnOff> getTemporaryList();

    CTOnOff[] getTemporaryArray();

    CTOnOff getTemporaryArray(int i);

    int sizeOfTemporaryArray();

    void setTemporaryArray(CTOnOff[] cTOnOffArr);

    void setTemporaryArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewTemporary(int i);

    CTOnOff addNewTemporary();

    void removeTemporary(int i);

    List<CTDecimalNumber> getIdList();

    CTDecimalNumber[] getIdArray();

    CTDecimalNumber getIdArray(int i);

    int sizeOfIdArray();

    void setIdArray(CTDecimalNumber[] cTDecimalNumberArr);

    void setIdArray(int i, CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber insertNewId(int i);

    CTDecimalNumber addNewId();

    void removeId(int i);

    List<CTString> getTagList();

    CTString[] getTagArray();

    CTString getTagArray(int i);

    int sizeOfTagArray();

    void setTagArray(CTString[] cTStringArr);

    void setTagArray(int i, CTString cTString);

    CTString insertNewTag(int i);

    CTString addNewTag();

    void removeTag(int i);

    List<CTEmpty> getEquationList();

    CTEmpty[] getEquationArray();

    CTEmpty getEquationArray(int i);

    int sizeOfEquationArray();

    void setEquationArray(CTEmpty[] cTEmptyArr);

    void setEquationArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewEquation(int i);

    CTEmpty addNewEquation();

    void removeEquation(int i);

    List<CTSdtComboBox> getComboBoxList();

    CTSdtComboBox[] getComboBoxArray();

    CTSdtComboBox getComboBoxArray(int i);

    int sizeOfComboBoxArray();

    void setComboBoxArray(CTSdtComboBox[] cTSdtComboBoxArr);

    void setComboBoxArray(int i, CTSdtComboBox cTSdtComboBox);

    CTSdtComboBox insertNewComboBox(int i);

    CTSdtComboBox addNewComboBox();

    void removeComboBox(int i);

    List<CTSdtDate> getDateList();

    CTSdtDate[] getDateArray();

    CTSdtDate getDateArray(int i);

    int sizeOfDateArray();

    void setDateArray(CTSdtDate[] cTSdtDateArr);

    void setDateArray(int i, CTSdtDate cTSdtDate);

    CTSdtDate insertNewDate(int i);

    CTSdtDate addNewDate();

    void removeDate(int i);

    List<CTSdtDocPart> getDocPartObjList();

    CTSdtDocPart[] getDocPartObjArray();

    CTSdtDocPart getDocPartObjArray(int i);

    int sizeOfDocPartObjArray();

    void setDocPartObjArray(CTSdtDocPart[] cTSdtDocPartArr);

    void setDocPartObjArray(int i, CTSdtDocPart cTSdtDocPart);

    CTSdtDocPart insertNewDocPartObj(int i);

    CTSdtDocPart addNewDocPartObj();

    void removeDocPartObj(int i);

    List<CTSdtDocPart> getDocPartListList();

    CTSdtDocPart[] getDocPartListArray();

    CTSdtDocPart getDocPartListArray(int i);

    int sizeOfDocPartListArray();

    void setDocPartListArray(CTSdtDocPart[] cTSdtDocPartArr);

    void setDocPartListArray(int i, CTSdtDocPart cTSdtDocPart);

    CTSdtDocPart insertNewDocPartList(int i);

    CTSdtDocPart addNewDocPartList();

    void removeDocPartList(int i);

    List<CTSdtDropDownList> getDropDownListList();

    CTSdtDropDownList[] getDropDownListArray();

    CTSdtDropDownList getDropDownListArray(int i);

    int sizeOfDropDownListArray();

    void setDropDownListArray(CTSdtDropDownList[] cTSdtDropDownListArr);

    void setDropDownListArray(int i, CTSdtDropDownList cTSdtDropDownList);

    CTSdtDropDownList insertNewDropDownList(int i);

    CTSdtDropDownList addNewDropDownList();

    void removeDropDownList(int i);

    List<CTEmpty> getPictureList();

    CTEmpty[] getPictureArray();

    CTEmpty getPictureArray(int i);

    int sizeOfPictureArray();

    void setPictureArray(CTEmpty[] cTEmptyArr);

    void setPictureArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewPicture(int i);

    CTEmpty addNewPicture();

    void removePicture(int i);

    List<CTEmpty> getRichTextList();

    CTEmpty[] getRichTextArray();

    CTEmpty getRichTextArray(int i);

    int sizeOfRichTextArray();

    void setRichTextArray(CTEmpty[] cTEmptyArr);

    void setRichTextArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewRichText(int i);

    CTEmpty addNewRichText();

    void removeRichText(int i);

    List<CTSdtText> getTextList();

    CTSdtText[] getTextArray();

    CTSdtText getTextArray(int i);

    int sizeOfTextArray();

    void setTextArray(CTSdtText[] cTSdtTextArr);

    void setTextArray(int i, CTSdtText cTSdtText);

    CTSdtText insertNewText(int i);

    CTSdtText addNewText();

    void removeText(int i);

    List<CTEmpty> getCitationList();

    CTEmpty[] getCitationArray();

    CTEmpty getCitationArray(int i);

    int sizeOfCitationArray();

    void setCitationArray(CTEmpty[] cTEmptyArr);

    void setCitationArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewCitation(int i);

    CTEmpty addNewCitation();

    void removeCitation(int i);

    List<CTEmpty> getGroupList();

    CTEmpty[] getGroupArray();

    CTEmpty getGroupArray(int i);

    int sizeOfGroupArray();

    void setGroupArray(CTEmpty[] cTEmptyArr);

    void setGroupArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewGroup(int i);

    CTEmpty addNewGroup();

    void removeGroup(int i);

    List<CTEmpty> getBibliographyList();

    CTEmpty[] getBibliographyArray();

    CTEmpty getBibliographyArray(int i);

    int sizeOfBibliographyArray();

    void setBibliographyArray(CTEmpty[] cTEmptyArr);

    void setBibliographyArray(int i, CTEmpty cTEmpty);

    CTEmpty insertNewBibliography(int i);

    CTEmpty addNewBibliography();

    void removeBibliography(int i);
}
