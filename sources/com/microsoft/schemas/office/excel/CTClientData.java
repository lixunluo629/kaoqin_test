package com.microsoft.schemas.office.excel;

import com.microsoft.schemas.office.excel.STObjectType;
import com.microsoft.schemas.office.excel.STTrueFalseBlank;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/excel/CTClientData.class */
public interface CTClientData extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTClientData.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctclientdata433btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/excel/CTClientData$Factory.class */
    public static final class Factory {
        public static CTClientData newInstance() {
            return (CTClientData) POIXMLTypeLoader.newInstance(CTClientData.type, null);
        }

        public static CTClientData newInstance(XmlOptions xmlOptions) {
            return (CTClientData) POIXMLTypeLoader.newInstance(CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(String str) throws XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(str, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(str, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(File file) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(file, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(file, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(URL url) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(url, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(url, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(InputStream inputStream) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(inputStream, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(inputStream, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(Reader reader) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(reader, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTClientData) POIXMLTypeLoader.parse(reader, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(xMLStreamReader, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(xMLStreamReader, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(Node node) throws XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(node, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(node, CTClientData.type, xmlOptions);
        }

        public static CTClientData parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(xMLInputStream, CTClientData.type, (XmlOptions) null);
        }

        public static CTClientData parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTClientData) POIXMLTypeLoader.parse(xMLInputStream, CTClientData.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTClientData.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTClientData.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<STTrueFalseBlank.Enum> getMoveWithCellsList();

    STTrueFalseBlank.Enum[] getMoveWithCellsArray();

    STTrueFalseBlank.Enum getMoveWithCellsArray(int i);

    List<STTrueFalseBlank> xgetMoveWithCellsList();

    STTrueFalseBlank[] xgetMoveWithCellsArray();

    STTrueFalseBlank xgetMoveWithCellsArray(int i);

    int sizeOfMoveWithCellsArray();

    void setMoveWithCellsArray(STTrueFalseBlank.Enum[] enumArr);

    void setMoveWithCellsArray(int i, STTrueFalseBlank.Enum r2);

    void xsetMoveWithCellsArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetMoveWithCellsArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertMoveWithCells(int i, STTrueFalseBlank.Enum r2);

    void addMoveWithCells(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewMoveWithCells(int i);

    STTrueFalseBlank addNewMoveWithCells();

    void removeMoveWithCells(int i);

    List<STTrueFalseBlank.Enum> getSizeWithCellsList();

    STTrueFalseBlank.Enum[] getSizeWithCellsArray();

    STTrueFalseBlank.Enum getSizeWithCellsArray(int i);

    List<STTrueFalseBlank> xgetSizeWithCellsList();

    STTrueFalseBlank[] xgetSizeWithCellsArray();

    STTrueFalseBlank xgetSizeWithCellsArray(int i);

    int sizeOfSizeWithCellsArray();

    void setSizeWithCellsArray(STTrueFalseBlank.Enum[] enumArr);

    void setSizeWithCellsArray(int i, STTrueFalseBlank.Enum r2);

    void xsetSizeWithCellsArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetSizeWithCellsArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertSizeWithCells(int i, STTrueFalseBlank.Enum r2);

    void addSizeWithCells(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewSizeWithCells(int i);

    STTrueFalseBlank addNewSizeWithCells();

    void removeSizeWithCells(int i);

    List<String> getAnchorList();

    String[] getAnchorArray();

    String getAnchorArray(int i);

    List<XmlString> xgetAnchorList();

    XmlString[] xgetAnchorArray();

    XmlString xgetAnchorArray(int i);

    int sizeOfAnchorArray();

    void setAnchorArray(String[] strArr);

    void setAnchorArray(int i, String str);

    void xsetAnchorArray(XmlString[] xmlStringArr);

    void xsetAnchorArray(int i, XmlString xmlString);

    void insertAnchor(int i, String str);

    void addAnchor(String str);

    XmlString insertNewAnchor(int i);

    XmlString addNewAnchor();

    void removeAnchor(int i);

    List<STTrueFalseBlank.Enum> getLockedList();

    STTrueFalseBlank.Enum[] getLockedArray();

    STTrueFalseBlank.Enum getLockedArray(int i);

    List<STTrueFalseBlank> xgetLockedList();

    STTrueFalseBlank[] xgetLockedArray();

    STTrueFalseBlank xgetLockedArray(int i);

    int sizeOfLockedArray();

    void setLockedArray(STTrueFalseBlank.Enum[] enumArr);

    void setLockedArray(int i, STTrueFalseBlank.Enum r2);

    void xsetLockedArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetLockedArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertLocked(int i, STTrueFalseBlank.Enum r2);

    void addLocked(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewLocked(int i);

    STTrueFalseBlank addNewLocked();

    void removeLocked(int i);

    List<STTrueFalseBlank.Enum> getDefaultSizeList();

    STTrueFalseBlank.Enum[] getDefaultSizeArray();

    STTrueFalseBlank.Enum getDefaultSizeArray(int i);

    List<STTrueFalseBlank> xgetDefaultSizeList();

    STTrueFalseBlank[] xgetDefaultSizeArray();

    STTrueFalseBlank xgetDefaultSizeArray(int i);

    int sizeOfDefaultSizeArray();

    void setDefaultSizeArray(STTrueFalseBlank.Enum[] enumArr);

    void setDefaultSizeArray(int i, STTrueFalseBlank.Enum r2);

    void xsetDefaultSizeArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetDefaultSizeArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertDefaultSize(int i, STTrueFalseBlank.Enum r2);

    void addDefaultSize(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewDefaultSize(int i);

    STTrueFalseBlank addNewDefaultSize();

    void removeDefaultSize(int i);

    List<STTrueFalseBlank.Enum> getPrintObjectList();

    STTrueFalseBlank.Enum[] getPrintObjectArray();

    STTrueFalseBlank.Enum getPrintObjectArray(int i);

    List<STTrueFalseBlank> xgetPrintObjectList();

    STTrueFalseBlank[] xgetPrintObjectArray();

    STTrueFalseBlank xgetPrintObjectArray(int i);

    int sizeOfPrintObjectArray();

    void setPrintObjectArray(STTrueFalseBlank.Enum[] enumArr);

    void setPrintObjectArray(int i, STTrueFalseBlank.Enum r2);

    void xsetPrintObjectArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetPrintObjectArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertPrintObject(int i, STTrueFalseBlank.Enum r2);

    void addPrintObject(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewPrintObject(int i);

    STTrueFalseBlank addNewPrintObject();

    void removePrintObject(int i);

    List<STTrueFalseBlank.Enum> getDisabledList();

    STTrueFalseBlank.Enum[] getDisabledArray();

    STTrueFalseBlank.Enum getDisabledArray(int i);

    List<STTrueFalseBlank> xgetDisabledList();

    STTrueFalseBlank[] xgetDisabledArray();

    STTrueFalseBlank xgetDisabledArray(int i);

    int sizeOfDisabledArray();

    void setDisabledArray(STTrueFalseBlank.Enum[] enumArr);

    void setDisabledArray(int i, STTrueFalseBlank.Enum r2);

    void xsetDisabledArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetDisabledArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertDisabled(int i, STTrueFalseBlank.Enum r2);

    void addDisabled(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewDisabled(int i);

    STTrueFalseBlank addNewDisabled();

    void removeDisabled(int i);

    List<STTrueFalseBlank.Enum> getAutoFillList();

    STTrueFalseBlank.Enum[] getAutoFillArray();

    STTrueFalseBlank.Enum getAutoFillArray(int i);

    List<STTrueFalseBlank> xgetAutoFillList();

    STTrueFalseBlank[] xgetAutoFillArray();

    STTrueFalseBlank xgetAutoFillArray(int i);

    int sizeOfAutoFillArray();

    void setAutoFillArray(STTrueFalseBlank.Enum[] enumArr);

    void setAutoFillArray(int i, STTrueFalseBlank.Enum r2);

    void xsetAutoFillArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetAutoFillArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertAutoFill(int i, STTrueFalseBlank.Enum r2);

    void addAutoFill(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewAutoFill(int i);

    STTrueFalseBlank addNewAutoFill();

    void removeAutoFill(int i);

    List<STTrueFalseBlank.Enum> getAutoLineList();

    STTrueFalseBlank.Enum[] getAutoLineArray();

    STTrueFalseBlank.Enum getAutoLineArray(int i);

    List<STTrueFalseBlank> xgetAutoLineList();

    STTrueFalseBlank[] xgetAutoLineArray();

    STTrueFalseBlank xgetAutoLineArray(int i);

    int sizeOfAutoLineArray();

    void setAutoLineArray(STTrueFalseBlank.Enum[] enumArr);

    void setAutoLineArray(int i, STTrueFalseBlank.Enum r2);

    void xsetAutoLineArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetAutoLineArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertAutoLine(int i, STTrueFalseBlank.Enum r2);

    void addAutoLine(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewAutoLine(int i);

    STTrueFalseBlank addNewAutoLine();

    void removeAutoLine(int i);

    List<STTrueFalseBlank.Enum> getAutoPictList();

    STTrueFalseBlank.Enum[] getAutoPictArray();

    STTrueFalseBlank.Enum getAutoPictArray(int i);

    List<STTrueFalseBlank> xgetAutoPictList();

    STTrueFalseBlank[] xgetAutoPictArray();

    STTrueFalseBlank xgetAutoPictArray(int i);

    int sizeOfAutoPictArray();

    void setAutoPictArray(STTrueFalseBlank.Enum[] enumArr);

    void setAutoPictArray(int i, STTrueFalseBlank.Enum r2);

    void xsetAutoPictArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetAutoPictArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertAutoPict(int i, STTrueFalseBlank.Enum r2);

    void addAutoPict(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewAutoPict(int i);

    STTrueFalseBlank addNewAutoPict();

    void removeAutoPict(int i);

    List<String> getFmlaMacroList();

    String[] getFmlaMacroArray();

    String getFmlaMacroArray(int i);

    List<XmlString> xgetFmlaMacroList();

    XmlString[] xgetFmlaMacroArray();

    XmlString xgetFmlaMacroArray(int i);

    int sizeOfFmlaMacroArray();

    void setFmlaMacroArray(String[] strArr);

    void setFmlaMacroArray(int i, String str);

    void xsetFmlaMacroArray(XmlString[] xmlStringArr);

    void xsetFmlaMacroArray(int i, XmlString xmlString);

    void insertFmlaMacro(int i, String str);

    void addFmlaMacro(String str);

    XmlString insertNewFmlaMacro(int i);

    XmlString addNewFmlaMacro();

    void removeFmlaMacro(int i);

    List<String> getTextHAlignList();

    String[] getTextHAlignArray();

    String getTextHAlignArray(int i);

    List<XmlString> xgetTextHAlignList();

    XmlString[] xgetTextHAlignArray();

    XmlString xgetTextHAlignArray(int i);

    int sizeOfTextHAlignArray();

    void setTextHAlignArray(String[] strArr);

    void setTextHAlignArray(int i, String str);

    void xsetTextHAlignArray(XmlString[] xmlStringArr);

    void xsetTextHAlignArray(int i, XmlString xmlString);

    void insertTextHAlign(int i, String str);

    void addTextHAlign(String str);

    XmlString insertNewTextHAlign(int i);

    XmlString addNewTextHAlign();

    void removeTextHAlign(int i);

    List<String> getTextVAlignList();

    String[] getTextVAlignArray();

    String getTextVAlignArray(int i);

    List<XmlString> xgetTextVAlignList();

    XmlString[] xgetTextVAlignArray();

    XmlString xgetTextVAlignArray(int i);

    int sizeOfTextVAlignArray();

    void setTextVAlignArray(String[] strArr);

    void setTextVAlignArray(int i, String str);

    void xsetTextVAlignArray(XmlString[] xmlStringArr);

    void xsetTextVAlignArray(int i, XmlString xmlString);

    void insertTextVAlign(int i, String str);

    void addTextVAlign(String str);

    XmlString insertNewTextVAlign(int i);

    XmlString addNewTextVAlign();

    void removeTextVAlign(int i);

    List<STTrueFalseBlank.Enum> getLockTextList();

    STTrueFalseBlank.Enum[] getLockTextArray();

    STTrueFalseBlank.Enum getLockTextArray(int i);

    List<STTrueFalseBlank> xgetLockTextList();

    STTrueFalseBlank[] xgetLockTextArray();

    STTrueFalseBlank xgetLockTextArray(int i);

    int sizeOfLockTextArray();

    void setLockTextArray(STTrueFalseBlank.Enum[] enumArr);

    void setLockTextArray(int i, STTrueFalseBlank.Enum r2);

    void xsetLockTextArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetLockTextArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertLockText(int i, STTrueFalseBlank.Enum r2);

    void addLockText(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewLockText(int i);

    STTrueFalseBlank addNewLockText();

    void removeLockText(int i);

    List<STTrueFalseBlank.Enum> getJustLastXList();

    STTrueFalseBlank.Enum[] getJustLastXArray();

    STTrueFalseBlank.Enum getJustLastXArray(int i);

    List<STTrueFalseBlank> xgetJustLastXList();

    STTrueFalseBlank[] xgetJustLastXArray();

    STTrueFalseBlank xgetJustLastXArray(int i);

    int sizeOfJustLastXArray();

    void setJustLastXArray(STTrueFalseBlank.Enum[] enumArr);

    void setJustLastXArray(int i, STTrueFalseBlank.Enum r2);

    void xsetJustLastXArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetJustLastXArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertJustLastX(int i, STTrueFalseBlank.Enum r2);

    void addJustLastX(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewJustLastX(int i);

    STTrueFalseBlank addNewJustLastX();

    void removeJustLastX(int i);

    List<STTrueFalseBlank.Enum> getSecretEditList();

    STTrueFalseBlank.Enum[] getSecretEditArray();

    STTrueFalseBlank.Enum getSecretEditArray(int i);

    List<STTrueFalseBlank> xgetSecretEditList();

    STTrueFalseBlank[] xgetSecretEditArray();

    STTrueFalseBlank xgetSecretEditArray(int i);

    int sizeOfSecretEditArray();

    void setSecretEditArray(STTrueFalseBlank.Enum[] enumArr);

    void setSecretEditArray(int i, STTrueFalseBlank.Enum r2);

    void xsetSecretEditArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetSecretEditArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertSecretEdit(int i, STTrueFalseBlank.Enum r2);

    void addSecretEdit(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewSecretEdit(int i);

    STTrueFalseBlank addNewSecretEdit();

    void removeSecretEdit(int i);

    List<STTrueFalseBlank.Enum> getDefaultList();

    STTrueFalseBlank.Enum[] getDefaultArray();

    STTrueFalseBlank.Enum getDefaultArray(int i);

    List<STTrueFalseBlank> xgetDefaultList();

    STTrueFalseBlank[] xgetDefaultArray();

    STTrueFalseBlank xgetDefaultArray(int i);

    int sizeOfDefaultArray();

    void setDefaultArray(STTrueFalseBlank.Enum[] enumArr);

    void setDefaultArray(int i, STTrueFalseBlank.Enum r2);

    void xsetDefaultArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetDefaultArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertDefault(int i, STTrueFalseBlank.Enum r2);

    void addDefault(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewDefault(int i);

    STTrueFalseBlank addNewDefault();

    void removeDefault(int i);

    List<STTrueFalseBlank.Enum> getHelpList();

    STTrueFalseBlank.Enum[] getHelpArray();

    STTrueFalseBlank.Enum getHelpArray(int i);

    List<STTrueFalseBlank> xgetHelpList();

    STTrueFalseBlank[] xgetHelpArray();

    STTrueFalseBlank xgetHelpArray(int i);

    int sizeOfHelpArray();

    void setHelpArray(STTrueFalseBlank.Enum[] enumArr);

    void setHelpArray(int i, STTrueFalseBlank.Enum r2);

    void xsetHelpArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetHelpArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertHelp(int i, STTrueFalseBlank.Enum r2);

    void addHelp(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewHelp(int i);

    STTrueFalseBlank addNewHelp();

    void removeHelp(int i);

    List<STTrueFalseBlank.Enum> getCancelList();

    STTrueFalseBlank.Enum[] getCancelArray();

    STTrueFalseBlank.Enum getCancelArray(int i);

    List<STTrueFalseBlank> xgetCancelList();

    STTrueFalseBlank[] xgetCancelArray();

    STTrueFalseBlank xgetCancelArray(int i);

    int sizeOfCancelArray();

    void setCancelArray(STTrueFalseBlank.Enum[] enumArr);

    void setCancelArray(int i, STTrueFalseBlank.Enum r2);

    void xsetCancelArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetCancelArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertCancel(int i, STTrueFalseBlank.Enum r2);

    void addCancel(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewCancel(int i);

    STTrueFalseBlank addNewCancel();

    void removeCancel(int i);

    List<STTrueFalseBlank.Enum> getDismissList();

    STTrueFalseBlank.Enum[] getDismissArray();

    STTrueFalseBlank.Enum getDismissArray(int i);

    List<STTrueFalseBlank> xgetDismissList();

    STTrueFalseBlank[] xgetDismissArray();

    STTrueFalseBlank xgetDismissArray(int i);

    int sizeOfDismissArray();

    void setDismissArray(STTrueFalseBlank.Enum[] enumArr);

    void setDismissArray(int i, STTrueFalseBlank.Enum r2);

    void xsetDismissArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetDismissArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertDismiss(int i, STTrueFalseBlank.Enum r2);

    void addDismiss(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewDismiss(int i);

    STTrueFalseBlank addNewDismiss();

    void removeDismiss(int i);

    List<BigInteger> getAccelList();

    BigInteger[] getAccelArray();

    BigInteger getAccelArray(int i);

    List<XmlInteger> xgetAccelList();

    XmlInteger[] xgetAccelArray();

    XmlInteger xgetAccelArray(int i);

    int sizeOfAccelArray();

    void setAccelArray(BigInteger[] bigIntegerArr);

    void setAccelArray(int i, BigInteger bigInteger);

    void xsetAccelArray(XmlInteger[] xmlIntegerArr);

    void xsetAccelArray(int i, XmlInteger xmlInteger);

    void insertAccel(int i, BigInteger bigInteger);

    void addAccel(BigInteger bigInteger);

    XmlInteger insertNewAccel(int i);

    XmlInteger addNewAccel();

    void removeAccel(int i);

    List<BigInteger> getAccel2List();

    BigInteger[] getAccel2Array();

    BigInteger getAccel2Array(int i);

    List<XmlInteger> xgetAccel2List();

    XmlInteger[] xgetAccel2Array();

    XmlInteger xgetAccel2Array(int i);

    int sizeOfAccel2Array();

    void setAccel2Array(BigInteger[] bigIntegerArr);

    void setAccel2Array(int i, BigInteger bigInteger);

    void xsetAccel2Array(XmlInteger[] xmlIntegerArr);

    void xsetAccel2Array(int i, XmlInteger xmlInteger);

    void insertAccel2(int i, BigInteger bigInteger);

    void addAccel2(BigInteger bigInteger);

    XmlInteger insertNewAccel2(int i);

    XmlInteger addNewAccel2();

    void removeAccel2(int i);

    List<BigInteger> getRowList();

    BigInteger[] getRowArray();

    BigInteger getRowArray(int i);

    List<XmlInteger> xgetRowList();

    XmlInteger[] xgetRowArray();

    XmlInteger xgetRowArray(int i);

    int sizeOfRowArray();

    void setRowArray(BigInteger[] bigIntegerArr);

    void setRowArray(int i, BigInteger bigInteger);

    void xsetRowArray(XmlInteger[] xmlIntegerArr);

    void xsetRowArray(int i, XmlInteger xmlInteger);

    void insertRow(int i, BigInteger bigInteger);

    void addRow(BigInteger bigInteger);

    XmlInteger insertNewRow(int i);

    XmlInteger addNewRow();

    void removeRow(int i);

    List<BigInteger> getColumnList();

    BigInteger[] getColumnArray();

    BigInteger getColumnArray(int i);

    List<XmlInteger> xgetColumnList();

    XmlInteger[] xgetColumnArray();

    XmlInteger xgetColumnArray(int i);

    int sizeOfColumnArray();

    void setColumnArray(BigInteger[] bigIntegerArr);

    void setColumnArray(int i, BigInteger bigInteger);

    void xsetColumnArray(XmlInteger[] xmlIntegerArr);

    void xsetColumnArray(int i, XmlInteger xmlInteger);

    void insertColumn(int i, BigInteger bigInteger);

    void addColumn(BigInteger bigInteger);

    XmlInteger insertNewColumn(int i);

    XmlInteger addNewColumn();

    void removeColumn(int i);

    List<STTrueFalseBlank.Enum> getVisibleList();

    STTrueFalseBlank.Enum[] getVisibleArray();

    STTrueFalseBlank.Enum getVisibleArray(int i);

    List<STTrueFalseBlank> xgetVisibleList();

    STTrueFalseBlank[] xgetVisibleArray();

    STTrueFalseBlank xgetVisibleArray(int i);

    int sizeOfVisibleArray();

    void setVisibleArray(STTrueFalseBlank.Enum[] enumArr);

    void setVisibleArray(int i, STTrueFalseBlank.Enum r2);

    void xsetVisibleArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetVisibleArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertVisible(int i, STTrueFalseBlank.Enum r2);

    void addVisible(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewVisible(int i);

    STTrueFalseBlank addNewVisible();

    void removeVisible(int i);

    List<STTrueFalseBlank.Enum> getRowHiddenList();

    STTrueFalseBlank.Enum[] getRowHiddenArray();

    STTrueFalseBlank.Enum getRowHiddenArray(int i);

    List<STTrueFalseBlank> xgetRowHiddenList();

    STTrueFalseBlank[] xgetRowHiddenArray();

    STTrueFalseBlank xgetRowHiddenArray(int i);

    int sizeOfRowHiddenArray();

    void setRowHiddenArray(STTrueFalseBlank.Enum[] enumArr);

    void setRowHiddenArray(int i, STTrueFalseBlank.Enum r2);

    void xsetRowHiddenArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetRowHiddenArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertRowHidden(int i, STTrueFalseBlank.Enum r2);

    void addRowHidden(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewRowHidden(int i);

    STTrueFalseBlank addNewRowHidden();

    void removeRowHidden(int i);

    List<STTrueFalseBlank.Enum> getColHiddenList();

    STTrueFalseBlank.Enum[] getColHiddenArray();

    STTrueFalseBlank.Enum getColHiddenArray(int i);

    List<STTrueFalseBlank> xgetColHiddenList();

    STTrueFalseBlank[] xgetColHiddenArray();

    STTrueFalseBlank xgetColHiddenArray(int i);

    int sizeOfColHiddenArray();

    void setColHiddenArray(STTrueFalseBlank.Enum[] enumArr);

    void setColHiddenArray(int i, STTrueFalseBlank.Enum r2);

    void xsetColHiddenArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetColHiddenArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertColHidden(int i, STTrueFalseBlank.Enum r2);

    void addColHidden(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewColHidden(int i);

    STTrueFalseBlank addNewColHidden();

    void removeColHidden(int i);

    List<BigInteger> getVTEditList();

    BigInteger[] getVTEditArray();

    BigInteger getVTEditArray(int i);

    List<XmlInteger> xgetVTEditList();

    XmlInteger[] xgetVTEditArray();

    XmlInteger xgetVTEditArray(int i);

    int sizeOfVTEditArray();

    void setVTEditArray(BigInteger[] bigIntegerArr);

    void setVTEditArray(int i, BigInteger bigInteger);

    void xsetVTEditArray(XmlInteger[] xmlIntegerArr);

    void xsetVTEditArray(int i, XmlInteger xmlInteger);

    void insertVTEdit(int i, BigInteger bigInteger);

    void addVTEdit(BigInteger bigInteger);

    XmlInteger insertNewVTEdit(int i);

    XmlInteger addNewVTEdit();

    void removeVTEdit(int i);

    List<STTrueFalseBlank.Enum> getMultiLineList();

    STTrueFalseBlank.Enum[] getMultiLineArray();

    STTrueFalseBlank.Enum getMultiLineArray(int i);

    List<STTrueFalseBlank> xgetMultiLineList();

    STTrueFalseBlank[] xgetMultiLineArray();

    STTrueFalseBlank xgetMultiLineArray(int i);

    int sizeOfMultiLineArray();

    void setMultiLineArray(STTrueFalseBlank.Enum[] enumArr);

    void setMultiLineArray(int i, STTrueFalseBlank.Enum r2);

    void xsetMultiLineArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetMultiLineArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertMultiLine(int i, STTrueFalseBlank.Enum r2);

    void addMultiLine(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewMultiLine(int i);

    STTrueFalseBlank addNewMultiLine();

    void removeMultiLine(int i);

    List<STTrueFalseBlank.Enum> getVScrollList();

    STTrueFalseBlank.Enum[] getVScrollArray();

    STTrueFalseBlank.Enum getVScrollArray(int i);

    List<STTrueFalseBlank> xgetVScrollList();

    STTrueFalseBlank[] xgetVScrollArray();

    STTrueFalseBlank xgetVScrollArray(int i);

    int sizeOfVScrollArray();

    void setVScrollArray(STTrueFalseBlank.Enum[] enumArr);

    void setVScrollArray(int i, STTrueFalseBlank.Enum r2);

    void xsetVScrollArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetVScrollArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertVScroll(int i, STTrueFalseBlank.Enum r2);

    void addVScroll(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewVScroll(int i);

    STTrueFalseBlank addNewVScroll();

    void removeVScroll(int i);

    List<STTrueFalseBlank.Enum> getValidIdsList();

    STTrueFalseBlank.Enum[] getValidIdsArray();

    STTrueFalseBlank.Enum getValidIdsArray(int i);

    List<STTrueFalseBlank> xgetValidIdsList();

    STTrueFalseBlank[] xgetValidIdsArray();

    STTrueFalseBlank xgetValidIdsArray(int i);

    int sizeOfValidIdsArray();

    void setValidIdsArray(STTrueFalseBlank.Enum[] enumArr);

    void setValidIdsArray(int i, STTrueFalseBlank.Enum r2);

    void xsetValidIdsArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetValidIdsArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertValidIds(int i, STTrueFalseBlank.Enum r2);

    void addValidIds(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewValidIds(int i);

    STTrueFalseBlank addNewValidIds();

    void removeValidIds(int i);

    List<String> getFmlaRangeList();

    String[] getFmlaRangeArray();

    String getFmlaRangeArray(int i);

    List<XmlString> xgetFmlaRangeList();

    XmlString[] xgetFmlaRangeArray();

    XmlString xgetFmlaRangeArray(int i);

    int sizeOfFmlaRangeArray();

    void setFmlaRangeArray(String[] strArr);

    void setFmlaRangeArray(int i, String str);

    void xsetFmlaRangeArray(XmlString[] xmlStringArr);

    void xsetFmlaRangeArray(int i, XmlString xmlString);

    void insertFmlaRange(int i, String str);

    void addFmlaRange(String str);

    XmlString insertNewFmlaRange(int i);

    XmlString addNewFmlaRange();

    void removeFmlaRange(int i);

    List<BigInteger> getWidthMinList();

    BigInteger[] getWidthMinArray();

    BigInteger getWidthMinArray(int i);

    List<XmlInteger> xgetWidthMinList();

    XmlInteger[] xgetWidthMinArray();

    XmlInteger xgetWidthMinArray(int i);

    int sizeOfWidthMinArray();

    void setWidthMinArray(BigInteger[] bigIntegerArr);

    void setWidthMinArray(int i, BigInteger bigInteger);

    void xsetWidthMinArray(XmlInteger[] xmlIntegerArr);

    void xsetWidthMinArray(int i, XmlInteger xmlInteger);

    void insertWidthMin(int i, BigInteger bigInteger);

    void addWidthMin(BigInteger bigInteger);

    XmlInteger insertNewWidthMin(int i);

    XmlInteger addNewWidthMin();

    void removeWidthMin(int i);

    List<BigInteger> getSelList();

    BigInteger[] getSelArray();

    BigInteger getSelArray(int i);

    List<XmlInteger> xgetSelList();

    XmlInteger[] xgetSelArray();

    XmlInteger xgetSelArray(int i);

    int sizeOfSelArray();

    void setSelArray(BigInteger[] bigIntegerArr);

    void setSelArray(int i, BigInteger bigInteger);

    void xsetSelArray(XmlInteger[] xmlIntegerArr);

    void xsetSelArray(int i, XmlInteger xmlInteger);

    void insertSel(int i, BigInteger bigInteger);

    void addSel(BigInteger bigInteger);

    XmlInteger insertNewSel(int i);

    XmlInteger addNewSel();

    void removeSel(int i);

    List<STTrueFalseBlank.Enum> getNoThreeD2List();

    STTrueFalseBlank.Enum[] getNoThreeD2Array();

    STTrueFalseBlank.Enum getNoThreeD2Array(int i);

    List<STTrueFalseBlank> xgetNoThreeD2List();

    STTrueFalseBlank[] xgetNoThreeD2Array();

    STTrueFalseBlank xgetNoThreeD2Array(int i);

    int sizeOfNoThreeD2Array();

    void setNoThreeD2Array(STTrueFalseBlank.Enum[] enumArr);

    void setNoThreeD2Array(int i, STTrueFalseBlank.Enum r2);

    void xsetNoThreeD2Array(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetNoThreeD2Array(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertNoThreeD2(int i, STTrueFalseBlank.Enum r2);

    void addNoThreeD2(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewNoThreeD2(int i);

    STTrueFalseBlank addNewNoThreeD2();

    void removeNoThreeD2(int i);

    List<String> getSelTypeList();

    String[] getSelTypeArray();

    String getSelTypeArray(int i);

    List<XmlString> xgetSelTypeList();

    XmlString[] xgetSelTypeArray();

    XmlString xgetSelTypeArray(int i);

    int sizeOfSelTypeArray();

    void setSelTypeArray(String[] strArr);

    void setSelTypeArray(int i, String str);

    void xsetSelTypeArray(XmlString[] xmlStringArr);

    void xsetSelTypeArray(int i, XmlString xmlString);

    void insertSelType(int i, String str);

    void addSelType(String str);

    XmlString insertNewSelType(int i);

    XmlString addNewSelType();

    void removeSelType(int i);

    List<String> getMultiSelList();

    String[] getMultiSelArray();

    String getMultiSelArray(int i);

    List<XmlString> xgetMultiSelList();

    XmlString[] xgetMultiSelArray();

    XmlString xgetMultiSelArray(int i);

    int sizeOfMultiSelArray();

    void setMultiSelArray(String[] strArr);

    void setMultiSelArray(int i, String str);

    void xsetMultiSelArray(XmlString[] xmlStringArr);

    void xsetMultiSelArray(int i, XmlString xmlString);

    void insertMultiSel(int i, String str);

    void addMultiSel(String str);

    XmlString insertNewMultiSel(int i);

    XmlString addNewMultiSel();

    void removeMultiSel(int i);

    List<String> getLCTList();

    String[] getLCTArray();

    String getLCTArray(int i);

    List<XmlString> xgetLCTList();

    XmlString[] xgetLCTArray();

    XmlString xgetLCTArray(int i);

    int sizeOfLCTArray();

    void setLCTArray(String[] strArr);

    void setLCTArray(int i, String str);

    void xsetLCTArray(XmlString[] xmlStringArr);

    void xsetLCTArray(int i, XmlString xmlString);

    void insertLCT(int i, String str);

    void addLCT(String str);

    XmlString insertNewLCT(int i);

    XmlString addNewLCT();

    void removeLCT(int i);

    List<String> getListItemList();

    String[] getListItemArray();

    String getListItemArray(int i);

    List<XmlString> xgetListItemList();

    XmlString[] xgetListItemArray();

    XmlString xgetListItemArray(int i);

    int sizeOfListItemArray();

    void setListItemArray(String[] strArr);

    void setListItemArray(int i, String str);

    void xsetListItemArray(XmlString[] xmlStringArr);

    void xsetListItemArray(int i, XmlString xmlString);

    void insertListItem(int i, String str);

    void addListItem(String str);

    XmlString insertNewListItem(int i);

    XmlString addNewListItem();

    void removeListItem(int i);

    List<String> getDropStyleList();

    String[] getDropStyleArray();

    String getDropStyleArray(int i);

    List<XmlString> xgetDropStyleList();

    XmlString[] xgetDropStyleArray();

    XmlString xgetDropStyleArray(int i);

    int sizeOfDropStyleArray();

    void setDropStyleArray(String[] strArr);

    void setDropStyleArray(int i, String str);

    void xsetDropStyleArray(XmlString[] xmlStringArr);

    void xsetDropStyleArray(int i, XmlString xmlString);

    void insertDropStyle(int i, String str);

    void addDropStyle(String str);

    XmlString insertNewDropStyle(int i);

    XmlString addNewDropStyle();

    void removeDropStyle(int i);

    List<STTrueFalseBlank.Enum> getColoredList();

    STTrueFalseBlank.Enum[] getColoredArray();

    STTrueFalseBlank.Enum getColoredArray(int i);

    List<STTrueFalseBlank> xgetColoredList();

    STTrueFalseBlank[] xgetColoredArray();

    STTrueFalseBlank xgetColoredArray(int i);

    int sizeOfColoredArray();

    void setColoredArray(STTrueFalseBlank.Enum[] enumArr);

    void setColoredArray(int i, STTrueFalseBlank.Enum r2);

    void xsetColoredArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetColoredArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertColored(int i, STTrueFalseBlank.Enum r2);

    void addColored(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewColored(int i);

    STTrueFalseBlank addNewColored();

    void removeColored(int i);

    List<BigInteger> getDropLinesList();

    BigInteger[] getDropLinesArray();

    BigInteger getDropLinesArray(int i);

    List<XmlInteger> xgetDropLinesList();

    XmlInteger[] xgetDropLinesArray();

    XmlInteger xgetDropLinesArray(int i);

    int sizeOfDropLinesArray();

    void setDropLinesArray(BigInteger[] bigIntegerArr);

    void setDropLinesArray(int i, BigInteger bigInteger);

    void xsetDropLinesArray(XmlInteger[] xmlIntegerArr);

    void xsetDropLinesArray(int i, XmlInteger xmlInteger);

    void insertDropLines(int i, BigInteger bigInteger);

    void addDropLines(BigInteger bigInteger);

    XmlInteger insertNewDropLines(int i);

    XmlInteger addNewDropLines();

    void removeDropLines(int i);

    List<BigInteger> getCheckedList();

    BigInteger[] getCheckedArray();

    BigInteger getCheckedArray(int i);

    List<XmlInteger> xgetCheckedList();

    XmlInteger[] xgetCheckedArray();

    XmlInteger xgetCheckedArray(int i);

    int sizeOfCheckedArray();

    void setCheckedArray(BigInteger[] bigIntegerArr);

    void setCheckedArray(int i, BigInteger bigInteger);

    void xsetCheckedArray(XmlInteger[] xmlIntegerArr);

    void xsetCheckedArray(int i, XmlInteger xmlInteger);

    void insertChecked(int i, BigInteger bigInteger);

    void addChecked(BigInteger bigInteger);

    XmlInteger insertNewChecked(int i);

    XmlInteger addNewChecked();

    void removeChecked(int i);

    List<String> getFmlaLinkList();

    String[] getFmlaLinkArray();

    String getFmlaLinkArray(int i);

    List<XmlString> xgetFmlaLinkList();

    XmlString[] xgetFmlaLinkArray();

    XmlString xgetFmlaLinkArray(int i);

    int sizeOfFmlaLinkArray();

    void setFmlaLinkArray(String[] strArr);

    void setFmlaLinkArray(int i, String str);

    void xsetFmlaLinkArray(XmlString[] xmlStringArr);

    void xsetFmlaLinkArray(int i, XmlString xmlString);

    void insertFmlaLink(int i, String str);

    void addFmlaLink(String str);

    XmlString insertNewFmlaLink(int i);

    XmlString addNewFmlaLink();

    void removeFmlaLink(int i);

    List<String> getFmlaPictList();

    String[] getFmlaPictArray();

    String getFmlaPictArray(int i);

    List<XmlString> xgetFmlaPictList();

    XmlString[] xgetFmlaPictArray();

    XmlString xgetFmlaPictArray(int i);

    int sizeOfFmlaPictArray();

    void setFmlaPictArray(String[] strArr);

    void setFmlaPictArray(int i, String str);

    void xsetFmlaPictArray(XmlString[] xmlStringArr);

    void xsetFmlaPictArray(int i, XmlString xmlString);

    void insertFmlaPict(int i, String str);

    void addFmlaPict(String str);

    XmlString insertNewFmlaPict(int i);

    XmlString addNewFmlaPict();

    void removeFmlaPict(int i);

    List<STTrueFalseBlank.Enum> getNoThreeDList();

    STTrueFalseBlank.Enum[] getNoThreeDArray();

    STTrueFalseBlank.Enum getNoThreeDArray(int i);

    List<STTrueFalseBlank> xgetNoThreeDList();

    STTrueFalseBlank[] xgetNoThreeDArray();

    STTrueFalseBlank xgetNoThreeDArray(int i);

    int sizeOfNoThreeDArray();

    void setNoThreeDArray(STTrueFalseBlank.Enum[] enumArr);

    void setNoThreeDArray(int i, STTrueFalseBlank.Enum r2);

    void xsetNoThreeDArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetNoThreeDArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertNoThreeD(int i, STTrueFalseBlank.Enum r2);

    void addNoThreeD(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewNoThreeD(int i);

    STTrueFalseBlank addNewNoThreeD();

    void removeNoThreeD(int i);

    List<STTrueFalseBlank.Enum> getFirstButtonList();

    STTrueFalseBlank.Enum[] getFirstButtonArray();

    STTrueFalseBlank.Enum getFirstButtonArray(int i);

    List<STTrueFalseBlank> xgetFirstButtonList();

    STTrueFalseBlank[] xgetFirstButtonArray();

    STTrueFalseBlank xgetFirstButtonArray(int i);

    int sizeOfFirstButtonArray();

    void setFirstButtonArray(STTrueFalseBlank.Enum[] enumArr);

    void setFirstButtonArray(int i, STTrueFalseBlank.Enum r2);

    void xsetFirstButtonArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetFirstButtonArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertFirstButton(int i, STTrueFalseBlank.Enum r2);

    void addFirstButton(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewFirstButton(int i);

    STTrueFalseBlank addNewFirstButton();

    void removeFirstButton(int i);

    List<String> getFmlaGroupList();

    String[] getFmlaGroupArray();

    String getFmlaGroupArray(int i);

    List<XmlString> xgetFmlaGroupList();

    XmlString[] xgetFmlaGroupArray();

    XmlString xgetFmlaGroupArray(int i);

    int sizeOfFmlaGroupArray();

    void setFmlaGroupArray(String[] strArr);

    void setFmlaGroupArray(int i, String str);

    void xsetFmlaGroupArray(XmlString[] xmlStringArr);

    void xsetFmlaGroupArray(int i, XmlString xmlString);

    void insertFmlaGroup(int i, String str);

    void addFmlaGroup(String str);

    XmlString insertNewFmlaGroup(int i);

    XmlString addNewFmlaGroup();

    void removeFmlaGroup(int i);

    List<BigInteger> getValList();

    BigInteger[] getValArray();

    BigInteger getValArray(int i);

    List<XmlInteger> xgetValList();

    XmlInteger[] xgetValArray();

    XmlInteger xgetValArray(int i);

    int sizeOfValArray();

    void setValArray(BigInteger[] bigIntegerArr);

    void setValArray(int i, BigInteger bigInteger);

    void xsetValArray(XmlInteger[] xmlIntegerArr);

    void xsetValArray(int i, XmlInteger xmlInteger);

    void insertVal(int i, BigInteger bigInteger);

    void addVal(BigInteger bigInteger);

    XmlInteger insertNewVal(int i);

    XmlInteger addNewVal();

    void removeVal(int i);

    List<BigInteger> getMinList();

    BigInteger[] getMinArray();

    BigInteger getMinArray(int i);

    List<XmlInteger> xgetMinList();

    XmlInteger[] xgetMinArray();

    XmlInteger xgetMinArray(int i);

    int sizeOfMinArray();

    void setMinArray(BigInteger[] bigIntegerArr);

    void setMinArray(int i, BigInteger bigInteger);

    void xsetMinArray(XmlInteger[] xmlIntegerArr);

    void xsetMinArray(int i, XmlInteger xmlInteger);

    void insertMin(int i, BigInteger bigInteger);

    void addMin(BigInteger bigInteger);

    XmlInteger insertNewMin(int i);

    XmlInteger addNewMin();

    void removeMin(int i);

    List<BigInteger> getMaxList();

    BigInteger[] getMaxArray();

    BigInteger getMaxArray(int i);

    List<XmlInteger> xgetMaxList();

    XmlInteger[] xgetMaxArray();

    XmlInteger xgetMaxArray(int i);

    int sizeOfMaxArray();

    void setMaxArray(BigInteger[] bigIntegerArr);

    void setMaxArray(int i, BigInteger bigInteger);

    void xsetMaxArray(XmlInteger[] xmlIntegerArr);

    void xsetMaxArray(int i, XmlInteger xmlInteger);

    void insertMax(int i, BigInteger bigInteger);

    void addMax(BigInteger bigInteger);

    XmlInteger insertNewMax(int i);

    XmlInteger addNewMax();

    void removeMax(int i);

    List<BigInteger> getIncList();

    BigInteger[] getIncArray();

    BigInteger getIncArray(int i);

    List<XmlInteger> xgetIncList();

    XmlInteger[] xgetIncArray();

    XmlInteger xgetIncArray(int i);

    int sizeOfIncArray();

    void setIncArray(BigInteger[] bigIntegerArr);

    void setIncArray(int i, BigInteger bigInteger);

    void xsetIncArray(XmlInteger[] xmlIntegerArr);

    void xsetIncArray(int i, XmlInteger xmlInteger);

    void insertInc(int i, BigInteger bigInteger);

    void addInc(BigInteger bigInteger);

    XmlInteger insertNewInc(int i);

    XmlInteger addNewInc();

    void removeInc(int i);

    List<BigInteger> getPageList();

    BigInteger[] getPageArray();

    BigInteger getPageArray(int i);

    List<XmlInteger> xgetPageList();

    XmlInteger[] xgetPageArray();

    XmlInteger xgetPageArray(int i);

    int sizeOfPageArray();

    void setPageArray(BigInteger[] bigIntegerArr);

    void setPageArray(int i, BigInteger bigInteger);

    void xsetPageArray(XmlInteger[] xmlIntegerArr);

    void xsetPageArray(int i, XmlInteger xmlInteger);

    void insertPage(int i, BigInteger bigInteger);

    void addPage(BigInteger bigInteger);

    XmlInteger insertNewPage(int i);

    XmlInteger addNewPage();

    void removePage(int i);

    List<STTrueFalseBlank.Enum> getHorizList();

    STTrueFalseBlank.Enum[] getHorizArray();

    STTrueFalseBlank.Enum getHorizArray(int i);

    List<STTrueFalseBlank> xgetHorizList();

    STTrueFalseBlank[] xgetHorizArray();

    STTrueFalseBlank xgetHorizArray(int i);

    int sizeOfHorizArray();

    void setHorizArray(STTrueFalseBlank.Enum[] enumArr);

    void setHorizArray(int i, STTrueFalseBlank.Enum r2);

    void xsetHorizArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetHorizArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertHoriz(int i, STTrueFalseBlank.Enum r2);

    void addHoriz(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewHoriz(int i);

    STTrueFalseBlank addNewHoriz();

    void removeHoriz(int i);

    List<BigInteger> getDxList();

    BigInteger[] getDxArray();

    BigInteger getDxArray(int i);

    List<XmlInteger> xgetDxList();

    XmlInteger[] xgetDxArray();

    XmlInteger xgetDxArray(int i);

    int sizeOfDxArray();

    void setDxArray(BigInteger[] bigIntegerArr);

    void setDxArray(int i, BigInteger bigInteger);

    void xsetDxArray(XmlInteger[] xmlIntegerArr);

    void xsetDxArray(int i, XmlInteger xmlInteger);

    void insertDx(int i, BigInteger bigInteger);

    void addDx(BigInteger bigInteger);

    XmlInteger insertNewDx(int i);

    XmlInteger addNewDx();

    void removeDx(int i);

    List<STTrueFalseBlank.Enum> getMapOCXList();

    STTrueFalseBlank.Enum[] getMapOCXArray();

    STTrueFalseBlank.Enum getMapOCXArray(int i);

    List<STTrueFalseBlank> xgetMapOCXList();

    STTrueFalseBlank[] xgetMapOCXArray();

    STTrueFalseBlank xgetMapOCXArray(int i);

    int sizeOfMapOCXArray();

    void setMapOCXArray(STTrueFalseBlank.Enum[] enumArr);

    void setMapOCXArray(int i, STTrueFalseBlank.Enum r2);

    void xsetMapOCXArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetMapOCXArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertMapOCX(int i, STTrueFalseBlank.Enum r2);

    void addMapOCX(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewMapOCX(int i);

    STTrueFalseBlank addNewMapOCX();

    void removeMapOCX(int i);

    List<STCF$Enum> getCFList();

    STCF$Enum[] getCFArray();

    STCF$Enum getCFArray(int i);

    List<STCF> xgetCFList();

    STCF[] xgetCFArray();

    STCF xgetCFArray(int i);

    int sizeOfCFArray();

    void setCFArray(STCF$Enum[] sTCF$EnumArr);

    void setCFArray(int i, STCF$Enum sTCF$Enum);

    void xsetCFArray(STCF[] stcfArr);

    void xsetCFArray(int i, STCF stcf);

    void insertCF(int i, STCF$Enum sTCF$Enum);

    void addCF(STCF$Enum sTCF$Enum);

    STCF insertNewCF(int i);

    STCF addNewCF();

    void removeCF(int i);

    List<STTrueFalseBlank.Enum> getCameraList();

    STTrueFalseBlank.Enum[] getCameraArray();

    STTrueFalseBlank.Enum getCameraArray(int i);

    List<STTrueFalseBlank> xgetCameraList();

    STTrueFalseBlank[] xgetCameraArray();

    STTrueFalseBlank xgetCameraArray(int i);

    int sizeOfCameraArray();

    void setCameraArray(STTrueFalseBlank.Enum[] enumArr);

    void setCameraArray(int i, STTrueFalseBlank.Enum r2);

    void xsetCameraArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetCameraArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertCamera(int i, STTrueFalseBlank.Enum r2);

    void addCamera(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewCamera(int i);

    STTrueFalseBlank addNewCamera();

    void removeCamera(int i);

    List<STTrueFalseBlank.Enum> getRecalcAlwaysList();

    STTrueFalseBlank.Enum[] getRecalcAlwaysArray();

    STTrueFalseBlank.Enum getRecalcAlwaysArray(int i);

    List<STTrueFalseBlank> xgetRecalcAlwaysList();

    STTrueFalseBlank[] xgetRecalcAlwaysArray();

    STTrueFalseBlank xgetRecalcAlwaysArray(int i);

    int sizeOfRecalcAlwaysArray();

    void setRecalcAlwaysArray(STTrueFalseBlank.Enum[] enumArr);

    void setRecalcAlwaysArray(int i, STTrueFalseBlank.Enum r2);

    void xsetRecalcAlwaysArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetRecalcAlwaysArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertRecalcAlways(int i, STTrueFalseBlank.Enum r2);

    void addRecalcAlways(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewRecalcAlways(int i);

    STTrueFalseBlank addNewRecalcAlways();

    void removeRecalcAlways(int i);

    List<STTrueFalseBlank.Enum> getAutoScaleList();

    STTrueFalseBlank.Enum[] getAutoScaleArray();

    STTrueFalseBlank.Enum getAutoScaleArray(int i);

    List<STTrueFalseBlank> xgetAutoScaleList();

    STTrueFalseBlank[] xgetAutoScaleArray();

    STTrueFalseBlank xgetAutoScaleArray(int i);

    int sizeOfAutoScaleArray();

    void setAutoScaleArray(STTrueFalseBlank.Enum[] enumArr);

    void setAutoScaleArray(int i, STTrueFalseBlank.Enum r2);

    void xsetAutoScaleArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetAutoScaleArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertAutoScale(int i, STTrueFalseBlank.Enum r2);

    void addAutoScale(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewAutoScale(int i);

    STTrueFalseBlank addNewAutoScale();

    void removeAutoScale(int i);

    List<STTrueFalseBlank.Enum> getDDEList();

    STTrueFalseBlank.Enum[] getDDEArray();

    STTrueFalseBlank.Enum getDDEArray(int i);

    List<STTrueFalseBlank> xgetDDEList();

    STTrueFalseBlank[] xgetDDEArray();

    STTrueFalseBlank xgetDDEArray(int i);

    int sizeOfDDEArray();

    void setDDEArray(STTrueFalseBlank.Enum[] enumArr);

    void setDDEArray(int i, STTrueFalseBlank.Enum r2);

    void xsetDDEArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetDDEArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertDDE(int i, STTrueFalseBlank.Enum r2);

    void addDDE(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewDDE(int i);

    STTrueFalseBlank addNewDDE();

    void removeDDE(int i);

    List<STTrueFalseBlank.Enum> getUIObjList();

    STTrueFalseBlank.Enum[] getUIObjArray();

    STTrueFalseBlank.Enum getUIObjArray(int i);

    List<STTrueFalseBlank> xgetUIObjList();

    STTrueFalseBlank[] xgetUIObjArray();

    STTrueFalseBlank xgetUIObjArray(int i);

    int sizeOfUIObjArray();

    void setUIObjArray(STTrueFalseBlank.Enum[] enumArr);

    void setUIObjArray(int i, STTrueFalseBlank.Enum r2);

    void xsetUIObjArray(STTrueFalseBlank[] sTTrueFalseBlankArr);

    void xsetUIObjArray(int i, STTrueFalseBlank sTTrueFalseBlank);

    void insertUIObj(int i, STTrueFalseBlank.Enum r2);

    void addUIObj(STTrueFalseBlank.Enum r1);

    STTrueFalseBlank insertNewUIObj(int i);

    STTrueFalseBlank addNewUIObj();

    void removeUIObj(int i);

    List<String> getScriptTextList();

    String[] getScriptTextArray();

    String getScriptTextArray(int i);

    List<XmlString> xgetScriptTextList();

    XmlString[] xgetScriptTextArray();

    XmlString xgetScriptTextArray(int i);

    int sizeOfScriptTextArray();

    void setScriptTextArray(String[] strArr);

    void setScriptTextArray(int i, String str);

    void xsetScriptTextArray(XmlString[] xmlStringArr);

    void xsetScriptTextArray(int i, XmlString xmlString);

    void insertScriptText(int i, String str);

    void addScriptText(String str);

    XmlString insertNewScriptText(int i);

    XmlString addNewScriptText();

    void removeScriptText(int i);

    List<String> getScriptExtendedList();

    String[] getScriptExtendedArray();

    String getScriptExtendedArray(int i);

    List<XmlString> xgetScriptExtendedList();

    XmlString[] xgetScriptExtendedArray();

    XmlString xgetScriptExtendedArray(int i);

    int sizeOfScriptExtendedArray();

    void setScriptExtendedArray(String[] strArr);

    void setScriptExtendedArray(int i, String str);

    void xsetScriptExtendedArray(XmlString[] xmlStringArr);

    void xsetScriptExtendedArray(int i, XmlString xmlString);

    void insertScriptExtended(int i, String str);

    void addScriptExtended(String str);

    XmlString insertNewScriptExtended(int i);

    XmlString addNewScriptExtended();

    void removeScriptExtended(int i);

    List<BigInteger> getScriptLanguageList();

    BigInteger[] getScriptLanguageArray();

    BigInteger getScriptLanguageArray(int i);

    List<XmlNonNegativeInteger> xgetScriptLanguageList();

    XmlNonNegativeInteger[] xgetScriptLanguageArray();

    XmlNonNegativeInteger xgetScriptLanguageArray(int i);

    int sizeOfScriptLanguageArray();

    void setScriptLanguageArray(BigInteger[] bigIntegerArr);

    void setScriptLanguageArray(int i, BigInteger bigInteger);

    void xsetScriptLanguageArray(XmlNonNegativeInteger[] xmlNonNegativeIntegerArr);

    void xsetScriptLanguageArray(int i, XmlNonNegativeInteger xmlNonNegativeInteger);

    void insertScriptLanguage(int i, BigInteger bigInteger);

    void addScriptLanguage(BigInteger bigInteger);

    XmlNonNegativeInteger insertNewScriptLanguage(int i);

    XmlNonNegativeInteger addNewScriptLanguage();

    void removeScriptLanguage(int i);

    List<BigInteger> getScriptLocationList();

    BigInteger[] getScriptLocationArray();

    BigInteger getScriptLocationArray(int i);

    List<XmlNonNegativeInteger> xgetScriptLocationList();

    XmlNonNegativeInteger[] xgetScriptLocationArray();

    XmlNonNegativeInteger xgetScriptLocationArray(int i);

    int sizeOfScriptLocationArray();

    void setScriptLocationArray(BigInteger[] bigIntegerArr);

    void setScriptLocationArray(int i, BigInteger bigInteger);

    void xsetScriptLocationArray(XmlNonNegativeInteger[] xmlNonNegativeIntegerArr);

    void xsetScriptLocationArray(int i, XmlNonNegativeInteger xmlNonNegativeInteger);

    void insertScriptLocation(int i, BigInteger bigInteger);

    void addScriptLocation(BigInteger bigInteger);

    XmlNonNegativeInteger insertNewScriptLocation(int i);

    XmlNonNegativeInteger addNewScriptLocation();

    void removeScriptLocation(int i);

    List<String> getFmlaTxbxList();

    String[] getFmlaTxbxArray();

    String getFmlaTxbxArray(int i);

    List<XmlString> xgetFmlaTxbxList();

    XmlString[] xgetFmlaTxbxArray();

    XmlString xgetFmlaTxbxArray(int i);

    int sizeOfFmlaTxbxArray();

    void setFmlaTxbxArray(String[] strArr);

    void setFmlaTxbxArray(int i, String str);

    void xsetFmlaTxbxArray(XmlString[] xmlStringArr);

    void xsetFmlaTxbxArray(int i, XmlString xmlString);

    void insertFmlaTxbx(int i, String str);

    void addFmlaTxbx(String str);

    XmlString insertNewFmlaTxbx(int i);

    XmlString addNewFmlaTxbx();

    void removeFmlaTxbx(int i);

    STObjectType.Enum getObjectType();

    STObjectType xgetObjectType();

    void setObjectType(STObjectType.Enum r1);

    void xsetObjectType(STObjectType sTObjectType);
}
