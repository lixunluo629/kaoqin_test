package com.microsoft.schemas.vml;

import com.microsoft.schemas.office.excel.CTClientData;
import com.microsoft.schemas.office.office.CTCallout;
import com.microsoft.schemas.office.office.CTClipPath;
import com.microsoft.schemas.office.office.CTComplex;
import com.microsoft.schemas.office.office.CTExtrusion;
import com.microsoft.schemas.office.office.CTLock;
import com.microsoft.schemas.office.office.CTSignatureLine;
import com.microsoft.schemas.office.office.CTSkew;
import com.microsoft.schemas.office.office.STBWMode;
import com.microsoft.schemas.office.office.STBWMode$Enum;
import com.microsoft.schemas.office.office.STConnectorType;
import com.microsoft.schemas.office.office.STConnectorType$Enum;
import com.microsoft.schemas.office.office.STHrAlign;
import com.microsoft.schemas.office.office.STHrAlign$Enum;
import com.microsoft.schemas.office.office.STInsetMode;
import com.microsoft.schemas.office.office.STTrueFalseBlank;
import com.microsoft.schemas.office.powerpoint.CTRel;
import com.microsoft.schemas.office.word.CTAnchorLock;
import com.microsoft.schemas.office.word.CTBorder;
import com.microsoft.schemas.office.word.CTWrap;
import com.microsoft.schemas.vml.STTrueFalse;
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
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTShapetype.class */
public interface CTShapetype extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTShapetype.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctshapetype5c6ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTShapetype$Factory.class */
    public static final class Factory {
        public static CTShapetype newInstance() {
            return (CTShapetype) POIXMLTypeLoader.newInstance(CTShapetype.type, null);
        }

        public static CTShapetype newInstance(XmlOptions xmlOptions) {
            return (CTShapetype) POIXMLTypeLoader.newInstance(CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(String str) throws XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(str, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(str, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(File file) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(file, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(file, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(URL url) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(url, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(url, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(InputStream inputStream) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(inputStream, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(inputStream, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(Reader reader) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(reader, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapetype) POIXMLTypeLoader.parse(reader, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(xMLStreamReader, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(xMLStreamReader, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(Node node) throws XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(node, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(node, CTShapetype.type, xmlOptions);
        }

        public static CTShapetype parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(xMLInputStream, CTShapetype.type, (XmlOptions) null);
        }

        public static CTShapetype parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTShapetype) POIXMLTypeLoader.parse(xMLInputStream, CTShapetype.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShapetype.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShapetype.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTPath> getPathList();

    CTPath[] getPathArray();

    CTPath getPathArray(int i);

    int sizeOfPathArray();

    void setPathArray(CTPath[] cTPathArr);

    void setPathArray(int i, CTPath cTPath);

    CTPath insertNewPath(int i);

    CTPath addNewPath();

    void removePath(int i);

    List<CTFormulas> getFormulasList();

    CTFormulas[] getFormulasArray();

    CTFormulas getFormulasArray(int i);

    int sizeOfFormulasArray();

    void setFormulasArray(CTFormulas[] cTFormulasArr);

    void setFormulasArray(int i, CTFormulas cTFormulas);

    CTFormulas insertNewFormulas(int i);

    CTFormulas addNewFormulas();

    void removeFormulas(int i);

    List<CTHandles> getHandlesList();

    CTHandles[] getHandlesArray();

    CTHandles getHandlesArray(int i);

    int sizeOfHandlesArray();

    void setHandlesArray(CTHandles[] cTHandlesArr);

    void setHandlesArray(int i, CTHandles cTHandles);

    CTHandles insertNewHandles(int i);

    CTHandles addNewHandles();

    void removeHandles(int i);

    List<CTFill> getFillList();

    CTFill[] getFillArray();

    CTFill getFillArray(int i);

    int sizeOfFillArray();

    void setFillArray(CTFill[] cTFillArr);

    void setFillArray(int i, CTFill cTFill);

    CTFill insertNewFill(int i);

    CTFill addNewFill();

    void removeFill(int i);

    List<CTStroke> getStrokeList();

    CTStroke[] getStrokeArray();

    CTStroke getStrokeArray(int i);

    int sizeOfStrokeArray();

    void setStrokeArray(CTStroke[] cTStrokeArr);

    void setStrokeArray(int i, CTStroke cTStroke);

    CTStroke insertNewStroke(int i);

    CTStroke addNewStroke();

    void removeStroke(int i);

    List<CTShadow> getShadowList();

    CTShadow[] getShadowArray();

    CTShadow getShadowArray(int i);

    int sizeOfShadowArray();

    void setShadowArray(CTShadow[] cTShadowArr);

    void setShadowArray(int i, CTShadow cTShadow);

    CTShadow insertNewShadow(int i);

    CTShadow addNewShadow();

    void removeShadow(int i);

    List<CTTextbox> getTextboxList();

    CTTextbox[] getTextboxArray();

    CTTextbox getTextboxArray(int i);

    int sizeOfTextboxArray();

    void setTextboxArray(CTTextbox[] cTTextboxArr);

    void setTextboxArray(int i, CTTextbox cTTextbox);

    CTTextbox insertNewTextbox(int i);

    CTTextbox addNewTextbox();

    void removeTextbox(int i);

    List<CTTextPath> getTextpathList();

    CTTextPath[] getTextpathArray();

    CTTextPath getTextpathArray(int i);

    int sizeOfTextpathArray();

    void setTextpathArray(CTTextPath[] cTTextPathArr);

    void setTextpathArray(int i, CTTextPath cTTextPath);

    CTTextPath insertNewTextpath(int i);

    CTTextPath addNewTextpath();

    void removeTextpath(int i);

    List<CTImageData> getImagedataList();

    CTImageData[] getImagedataArray();

    CTImageData getImagedataArray(int i);

    int sizeOfImagedataArray();

    void setImagedataArray(CTImageData[] cTImageDataArr);

    void setImagedataArray(int i, CTImageData cTImageData);

    CTImageData insertNewImagedata(int i);

    CTImageData addNewImagedata();

    void removeImagedata(int i);

    List<CTSkew> getSkewList();

    CTSkew[] getSkewArray();

    CTSkew getSkewArray(int i);

    int sizeOfSkewArray();

    void setSkewArray(CTSkew[] cTSkewArr);

    void setSkewArray(int i, CTSkew cTSkew);

    CTSkew insertNewSkew(int i);

    CTSkew addNewSkew();

    void removeSkew(int i);

    List<CTExtrusion> getExtrusionList();

    CTExtrusion[] getExtrusionArray();

    CTExtrusion getExtrusionArray(int i);

    int sizeOfExtrusionArray();

    void setExtrusionArray(CTExtrusion[] cTExtrusionArr);

    void setExtrusionArray(int i, CTExtrusion cTExtrusion);

    CTExtrusion insertNewExtrusion(int i);

    CTExtrusion addNewExtrusion();

    void removeExtrusion(int i);

    List<CTCallout> getCalloutList();

    CTCallout[] getCalloutArray();

    CTCallout getCalloutArray(int i);

    int sizeOfCalloutArray();

    void setCalloutArray(CTCallout[] cTCalloutArr);

    void setCalloutArray(int i, CTCallout cTCallout);

    CTCallout insertNewCallout(int i);

    CTCallout addNewCallout();

    void removeCallout(int i);

    List<CTLock> getLockList();

    CTLock[] getLockArray();

    CTLock getLockArray(int i);

    int sizeOfLockArray();

    void setLockArray(CTLock[] cTLockArr);

    void setLockArray(int i, CTLock cTLock);

    CTLock insertNewLock(int i);

    CTLock addNewLock();

    void removeLock(int i);

    List<CTClipPath> getClippathList();

    CTClipPath[] getClippathArray();

    CTClipPath getClippathArray(int i);

    int sizeOfClippathArray();

    void setClippathArray(CTClipPath[] cTClipPathArr);

    void setClippathArray(int i, CTClipPath cTClipPath);

    CTClipPath insertNewClippath(int i);

    CTClipPath addNewClippath();

    void removeClippath(int i);

    List<CTSignatureLine> getSignaturelineList();

    CTSignatureLine[] getSignaturelineArray();

    CTSignatureLine getSignaturelineArray(int i);

    int sizeOfSignaturelineArray();

    void setSignaturelineArray(CTSignatureLine[] cTSignatureLineArr);

    void setSignaturelineArray(int i, CTSignatureLine cTSignatureLine);

    CTSignatureLine insertNewSignatureline(int i);

    CTSignatureLine addNewSignatureline();

    void removeSignatureline(int i);

    List<CTWrap> getWrapList();

    CTWrap[] getWrapArray();

    CTWrap getWrapArray(int i);

    int sizeOfWrapArray();

    void setWrapArray(CTWrap[] cTWrapArr);

    void setWrapArray(int i, CTWrap cTWrap);

    CTWrap insertNewWrap(int i);

    CTWrap addNewWrap();

    void removeWrap(int i);

    List<CTAnchorLock> getAnchorlockList();

    CTAnchorLock[] getAnchorlockArray();

    CTAnchorLock getAnchorlockArray(int i);

    int sizeOfAnchorlockArray();

    void setAnchorlockArray(CTAnchorLock[] cTAnchorLockArr);

    void setAnchorlockArray(int i, CTAnchorLock cTAnchorLock);

    CTAnchorLock insertNewAnchorlock(int i);

    CTAnchorLock addNewAnchorlock();

    void removeAnchorlock(int i);

    List<CTBorder> getBordertopList();

    CTBorder[] getBordertopArray();

    CTBorder getBordertopArray(int i);

    int sizeOfBordertopArray();

    void setBordertopArray(CTBorder[] cTBorderArr);

    void setBordertopArray(int i, CTBorder cTBorder);

    CTBorder insertNewBordertop(int i);

    CTBorder addNewBordertop();

    void removeBordertop(int i);

    List<CTBorder> getBorderbottomList();

    CTBorder[] getBorderbottomArray();

    CTBorder getBorderbottomArray(int i);

    int sizeOfBorderbottomArray();

    void setBorderbottomArray(CTBorder[] cTBorderArr);

    void setBorderbottomArray(int i, CTBorder cTBorder);

    CTBorder insertNewBorderbottom(int i);

    CTBorder addNewBorderbottom();

    void removeBorderbottom(int i);

    List<CTBorder> getBorderleftList();

    CTBorder[] getBorderleftArray();

    CTBorder getBorderleftArray(int i);

    int sizeOfBorderleftArray();

    void setBorderleftArray(CTBorder[] cTBorderArr);

    void setBorderleftArray(int i, CTBorder cTBorder);

    CTBorder insertNewBorderleft(int i);

    CTBorder addNewBorderleft();

    void removeBorderleft(int i);

    List<CTBorder> getBorderrightList();

    CTBorder[] getBorderrightArray();

    CTBorder getBorderrightArray(int i);

    int sizeOfBorderrightArray();

    void setBorderrightArray(CTBorder[] cTBorderArr);

    void setBorderrightArray(int i, CTBorder cTBorder);

    CTBorder insertNewBorderright(int i);

    CTBorder addNewBorderright();

    void removeBorderright(int i);

    List<CTClientData> getClientDataList();

    CTClientData[] getClientDataArray();

    CTClientData getClientDataArray(int i);

    int sizeOfClientDataArray();

    void setClientDataArray(CTClientData[] cTClientDataArr);

    void setClientDataArray(int i, CTClientData cTClientData);

    CTClientData insertNewClientData(int i);

    CTClientData addNewClientData();

    void removeClientData(int i);

    List<CTRel> getTextdataList();

    CTRel[] getTextdataArray();

    CTRel getTextdataArray(int i);

    int sizeOfTextdataArray();

    void setTextdataArray(CTRel[] cTRelArr);

    void setTextdataArray(int i, CTRel cTRel);

    CTRel insertNewTextdata(int i);

    CTRel addNewTextdata();

    void removeTextdata(int i);

    CTComplex getComplex();

    boolean isSetComplex();

    void setComplex(CTComplex cTComplex);

    CTComplex addNewComplex();

    void unsetComplex();

    String getId();

    XmlString xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlString xmlString);

    void unsetId();

    String getStyle();

    XmlString xgetStyle();

    boolean isSetStyle();

    void setStyle(String str);

    void xsetStyle(XmlString xmlString);

    void unsetStyle();

    String getHref();

    XmlString xgetHref();

    boolean isSetHref();

    void setHref(String str);

    void xsetHref(XmlString xmlString);

    void unsetHref();

    String getTarget();

    XmlString xgetTarget();

    boolean isSetTarget();

    void setTarget(String str);

    void xsetTarget(XmlString xmlString);

    void unsetTarget();

    String getClass1();

    XmlString xgetClass1();

    boolean isSetClass1();

    void setClass1(String str);

    void xsetClass1(XmlString xmlString);

    void unsetClass1();

    String getTitle();

    XmlString xgetTitle();

    boolean isSetTitle();

    void setTitle(String str);

    void xsetTitle(XmlString xmlString);

    void unsetTitle();

    String getAlt();

    XmlString xgetAlt();

    boolean isSetAlt();

    void setAlt(String str);

    void xsetAlt(XmlString xmlString);

    void unsetAlt();

    String getCoordsize();

    XmlString xgetCoordsize();

    boolean isSetCoordsize();

    void setCoordsize(String str);

    void xsetCoordsize(XmlString xmlString);

    void unsetCoordsize();

    String getCoordorigin();

    XmlString xgetCoordorigin();

    boolean isSetCoordorigin();

    void setCoordorigin(String str);

    void xsetCoordorigin(XmlString xmlString);

    void unsetCoordorigin();

    String getWrapcoords();

    XmlString xgetWrapcoords();

    boolean isSetWrapcoords();

    void setWrapcoords(String str);

    void xsetWrapcoords(XmlString xmlString);

    void unsetWrapcoords();

    STTrueFalse.Enum getPrint();

    STTrueFalse xgetPrint();

    boolean isSetPrint();

    void setPrint(STTrueFalse.Enum r1);

    void xsetPrint(STTrueFalse sTTrueFalse);

    void unsetPrint();

    String getSpid();

    XmlString xgetSpid();

    boolean isSetSpid();

    void setSpid(String str);

    void xsetSpid(XmlString xmlString);

    void unsetSpid();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getOned();

    com.microsoft.schemas.office.office.STTrueFalse xgetOned();

    boolean isSetOned();

    void setOned(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetOned(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetOned();

    BigInteger getRegroupid();

    XmlInteger xgetRegroupid();

    boolean isSetRegroupid();

    void setRegroupid(BigInteger bigInteger);

    void xsetRegroupid(XmlInteger xmlInteger);

    void unsetRegroupid();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getDoubleclicknotify();

    com.microsoft.schemas.office.office.STTrueFalse xgetDoubleclicknotify();

    boolean isSetDoubleclicknotify();

    void setDoubleclicknotify(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetDoubleclicknotify(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetDoubleclicknotify();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getButton();

    com.microsoft.schemas.office.office.STTrueFalse xgetButton();

    boolean isSetButton();

    void setButton(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetButton(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetButton();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getUserhidden();

    com.microsoft.schemas.office.office.STTrueFalse xgetUserhidden();

    boolean isSetUserhidden();

    void setUserhidden(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetUserhidden(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetUserhidden();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getBullet();

    com.microsoft.schemas.office.office.STTrueFalse xgetBullet();

    boolean isSetBullet();

    void setBullet(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetBullet(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetBullet();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getHr();

    com.microsoft.schemas.office.office.STTrueFalse xgetHr();

    boolean isSetHr();

    void setHr(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetHr(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetHr();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getHrstd();

    com.microsoft.schemas.office.office.STTrueFalse xgetHrstd();

    boolean isSetHrstd();

    void setHrstd(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetHrstd(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetHrstd();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getHrnoshade();

    com.microsoft.schemas.office.office.STTrueFalse xgetHrnoshade();

    boolean isSetHrnoshade();

    void setHrnoshade(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetHrnoshade(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetHrnoshade();

    float getHrpct();

    XmlFloat xgetHrpct();

    boolean isSetHrpct();

    void setHrpct(float f);

    void xsetHrpct(XmlFloat xmlFloat);

    void unsetHrpct();

    STHrAlign$Enum getHralign();

    STHrAlign xgetHralign();

    boolean isSetHralign();

    void setHralign(STHrAlign$Enum sTHrAlign$Enum);

    void xsetHralign(STHrAlign sTHrAlign);

    void unsetHralign();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getAllowincell();

    com.microsoft.schemas.office.office.STTrueFalse xgetAllowincell();

    boolean isSetAllowincell();

    void setAllowincell(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetAllowincell(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetAllowincell();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getAllowoverlap();

    com.microsoft.schemas.office.office.STTrueFalse xgetAllowoverlap();

    boolean isSetAllowoverlap();

    void setAllowoverlap(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetAllowoverlap(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetAllowoverlap();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getUserdrawn();

    com.microsoft.schemas.office.office.STTrueFalse xgetUserdrawn();

    boolean isSetUserdrawn();

    void setUserdrawn(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetUserdrawn(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetUserdrawn();

    String getBordertopcolor();

    XmlString xgetBordertopcolor();

    boolean isSetBordertopcolor();

    void setBordertopcolor(String str);

    void xsetBordertopcolor(XmlString xmlString);

    void unsetBordertopcolor();

    String getBorderleftcolor();

    XmlString xgetBorderleftcolor();

    boolean isSetBorderleftcolor();

    void setBorderleftcolor(String str);

    void xsetBorderleftcolor(XmlString xmlString);

    void unsetBorderleftcolor();

    String getBorderbottomcolor();

    XmlString xgetBorderbottomcolor();

    boolean isSetBorderbottomcolor();

    void setBorderbottomcolor(String str);

    void xsetBorderbottomcolor(XmlString xmlString);

    void unsetBorderbottomcolor();

    String getBorderrightcolor();

    XmlString xgetBorderrightcolor();

    boolean isSetBorderrightcolor();

    void setBorderrightcolor(String str);

    void xsetBorderrightcolor(XmlString xmlString);

    void unsetBorderrightcolor();

    BigInteger getDgmlayout();

    XmlInteger xgetDgmlayout();

    boolean isSetDgmlayout();

    void setDgmlayout(BigInteger bigInteger);

    void xsetDgmlayout(XmlInteger xmlInteger);

    void unsetDgmlayout();

    BigInteger getDgmnodekind();

    XmlInteger xgetDgmnodekind();

    boolean isSetDgmnodekind();

    void setDgmnodekind(BigInteger bigInteger);

    void xsetDgmnodekind(XmlInteger xmlInteger);

    void unsetDgmnodekind();

    BigInteger getDgmlayoutmru();

    XmlInteger xgetDgmlayoutmru();

    boolean isSetDgmlayoutmru();

    void setDgmlayoutmru(BigInteger bigInteger);

    void xsetDgmlayoutmru(XmlInteger xmlInteger);

    void unsetDgmlayoutmru();

    STInsetMode.Enum getInsetmode();

    STInsetMode xgetInsetmode();

    boolean isSetInsetmode();

    void setInsetmode(STInsetMode.Enum r1);

    void xsetInsetmode(STInsetMode sTInsetMode);

    void unsetInsetmode();

    String getChromakey();

    STColorType xgetChromakey();

    boolean isSetChromakey();

    void setChromakey(String str);

    void xsetChromakey(STColorType sTColorType);

    void unsetChromakey();

    STTrueFalse.Enum getFilled();

    STTrueFalse xgetFilled();

    boolean isSetFilled();

    void setFilled(STTrueFalse.Enum r1);

    void xsetFilled(STTrueFalse sTTrueFalse);

    void unsetFilled();

    String getFillcolor();

    STColorType xgetFillcolor();

    boolean isSetFillcolor();

    void setFillcolor(String str);

    void xsetFillcolor(STColorType sTColorType);

    void unsetFillcolor();

    String getOpacity();

    XmlString xgetOpacity();

    boolean isSetOpacity();

    void setOpacity(String str);

    void xsetOpacity(XmlString xmlString);

    void unsetOpacity();

    STTrueFalse.Enum getStroked();

    STTrueFalse xgetStroked();

    boolean isSetStroked();

    void setStroked(STTrueFalse.Enum r1);

    void xsetStroked(STTrueFalse sTTrueFalse);

    void unsetStroked();

    String getStrokecolor();

    STColorType xgetStrokecolor();

    boolean isSetStrokecolor();

    void setStrokecolor(String str);

    void xsetStrokecolor(STColorType sTColorType);

    void unsetStrokecolor();

    String getStrokeweight();

    XmlString xgetStrokeweight();

    boolean isSetStrokeweight();

    void setStrokeweight(String str);

    void xsetStrokeweight(XmlString xmlString);

    void unsetStrokeweight();

    STTrueFalse.Enum getInsetpen();

    STTrueFalse xgetInsetpen();

    boolean isSetInsetpen();

    void setInsetpen(STTrueFalse.Enum r1);

    void xsetInsetpen(STTrueFalse sTTrueFalse);

    void unsetInsetpen();

    float getSpt();

    XmlFloat xgetSpt();

    boolean isSetSpt();

    void setSpt(float f);

    void xsetSpt(XmlFloat xmlFloat);

    void unsetSpt();

    STConnectorType$Enum getConnectortype();

    STConnectorType xgetConnectortype();

    boolean isSetConnectortype();

    void setConnectortype(STConnectorType$Enum sTConnectorType$Enum);

    void xsetConnectortype(STConnectorType sTConnectorType);

    void unsetConnectortype();

    STBWMode$Enum getBwmode();

    STBWMode xgetBwmode();

    boolean isSetBwmode();

    void setBwmode(STBWMode$Enum sTBWMode$Enum);

    void xsetBwmode(STBWMode sTBWMode);

    void unsetBwmode();

    STBWMode$Enum getBwpure();

    STBWMode xgetBwpure();

    boolean isSetBwpure();

    void setBwpure(STBWMode$Enum sTBWMode$Enum);

    void xsetBwpure(STBWMode sTBWMode);

    void unsetBwpure();

    STBWMode$Enum getBwnormal();

    STBWMode xgetBwnormal();

    boolean isSetBwnormal();

    void setBwnormal(STBWMode$Enum sTBWMode$Enum);

    void xsetBwnormal(STBWMode sTBWMode);

    void unsetBwnormal();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getForcedash();

    com.microsoft.schemas.office.office.STTrueFalse xgetForcedash();

    boolean isSetForcedash();

    void setForcedash(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetForcedash(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetForcedash();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getOleicon();

    com.microsoft.schemas.office.office.STTrueFalse xgetOleicon();

    boolean isSetOleicon();

    void setOleicon(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetOleicon(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetOleicon();

    com.microsoft.schemas.office.office.STTrueFalseBlank$Enum getOle();

    STTrueFalseBlank xgetOle();

    boolean isSetOle();

    void setOle(com.microsoft.schemas.office.office.STTrueFalseBlank$Enum sTTrueFalseBlank$Enum);

    void xsetOle(STTrueFalseBlank sTTrueFalseBlank);

    void unsetOle();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getPreferrelative();

    com.microsoft.schemas.office.office.STTrueFalse xgetPreferrelative();

    boolean isSetPreferrelative();

    void setPreferrelative(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetPreferrelative(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetPreferrelative();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getCliptowrap();

    com.microsoft.schemas.office.office.STTrueFalse xgetCliptowrap();

    boolean isSetCliptowrap();

    void setCliptowrap(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetCliptowrap(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetCliptowrap();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getClip();

    com.microsoft.schemas.office.office.STTrueFalse xgetClip();

    boolean isSetClip();

    void setClip(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetClip(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetClip();

    String getAdj();

    XmlString xgetAdj();

    boolean isSetAdj();

    void setAdj(String str);

    void xsetAdj(XmlString xmlString);

    void unsetAdj();

    String getPath2();

    XmlString xgetPath2();

    boolean isSetPath2();

    void setPath2(String str);

    void xsetPath2(XmlString xmlString);

    void unsetPath2();

    String getMaster();

    XmlString xgetMaster();

    boolean isSetMaster();

    void setMaster(String str);

    void xsetMaster(XmlString xmlString);

    void unsetMaster();
}
