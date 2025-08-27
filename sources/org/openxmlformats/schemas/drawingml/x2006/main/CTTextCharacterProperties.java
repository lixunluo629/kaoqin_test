package org.openxmlformats.schemas.drawingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextCharacterProperties.class */
public interface CTTextCharacterProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextCharacterProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextcharacterproperties76c0type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextCharacterProperties$Factory.class */
    public static final class Factory {
        public static CTTextCharacterProperties newInstance() {
            return (CTTextCharacterProperties) POIXMLTypeLoader.newInstance(CTTextCharacterProperties.type, null);
        }

        public static CTTextCharacterProperties newInstance(XmlOptions xmlOptions) {
            return (CTTextCharacterProperties) POIXMLTypeLoader.newInstance(CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(String str) throws XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(str, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(str, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(File file) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(file, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(file, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(URL url) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(url, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(url, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(inputStream, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(inputStream, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(Reader reader) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(reader, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(reader, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(Node node) throws XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(node, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(node, CTTextCharacterProperties.type, xmlOptions);
        }

        public static CTTextCharacterProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTextCharacterProperties.type, (XmlOptions) null);
        }

        public static CTTextCharacterProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextCharacterProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTextCharacterProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextCharacterProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextCharacterProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLineProperties getLn();

    boolean isSetLn();

    void setLn(CTLineProperties cTLineProperties);

    CTLineProperties addNewLn();

    void unsetLn();

    CTNoFillProperties getNoFill();

    boolean isSetNoFill();

    void setNoFill(CTNoFillProperties cTNoFillProperties);

    CTNoFillProperties addNewNoFill();

    void unsetNoFill();

    CTSolidColorFillProperties getSolidFill();

    boolean isSetSolidFill();

    void setSolidFill(CTSolidColorFillProperties cTSolidColorFillProperties);

    CTSolidColorFillProperties addNewSolidFill();

    void unsetSolidFill();

    CTGradientFillProperties getGradFill();

    boolean isSetGradFill();

    void setGradFill(CTGradientFillProperties cTGradientFillProperties);

    CTGradientFillProperties addNewGradFill();

    void unsetGradFill();

    CTBlipFillProperties getBlipFill();

    boolean isSetBlipFill();

    void setBlipFill(CTBlipFillProperties cTBlipFillProperties);

    CTBlipFillProperties addNewBlipFill();

    void unsetBlipFill();

    CTPatternFillProperties getPattFill();

    boolean isSetPattFill();

    void setPattFill(CTPatternFillProperties cTPatternFillProperties);

    CTPatternFillProperties addNewPattFill();

    void unsetPattFill();

    CTGroupFillProperties getGrpFill();

    boolean isSetGrpFill();

    void setGrpFill(CTGroupFillProperties cTGroupFillProperties);

    CTGroupFillProperties addNewGrpFill();

    void unsetGrpFill();

    CTEffectList getEffectLst();

    boolean isSetEffectLst();

    void setEffectLst(CTEffectList cTEffectList);

    CTEffectList addNewEffectLst();

    void unsetEffectLst();

    CTEffectContainer getEffectDag();

    boolean isSetEffectDag();

    void setEffectDag(CTEffectContainer cTEffectContainer);

    CTEffectContainer addNewEffectDag();

    void unsetEffectDag();

    CTColor getHighlight();

    boolean isSetHighlight();

    void setHighlight(CTColor cTColor);

    CTColor addNewHighlight();

    void unsetHighlight();

    CTTextUnderlineLineFollowText getULnTx();

    boolean isSetULnTx();

    void setULnTx(CTTextUnderlineLineFollowText cTTextUnderlineLineFollowText);

    CTTextUnderlineLineFollowText addNewULnTx();

    void unsetULnTx();

    CTLineProperties getULn();

    boolean isSetULn();

    void setULn(CTLineProperties cTLineProperties);

    CTLineProperties addNewULn();

    void unsetULn();

    CTTextUnderlineFillFollowText getUFillTx();

    boolean isSetUFillTx();

    void setUFillTx(CTTextUnderlineFillFollowText cTTextUnderlineFillFollowText);

    CTTextUnderlineFillFollowText addNewUFillTx();

    void unsetUFillTx();

    CTTextUnderlineFillGroupWrapper getUFill();

    boolean isSetUFill();

    void setUFill(CTTextUnderlineFillGroupWrapper cTTextUnderlineFillGroupWrapper);

    CTTextUnderlineFillGroupWrapper addNewUFill();

    void unsetUFill();

    CTTextFont getLatin();

    boolean isSetLatin();

    void setLatin(CTTextFont cTTextFont);

    CTTextFont addNewLatin();

    void unsetLatin();

    CTTextFont getEa();

    boolean isSetEa();

    void setEa(CTTextFont cTTextFont);

    CTTextFont addNewEa();

    void unsetEa();

    CTTextFont getCs();

    boolean isSetCs();

    void setCs(CTTextFont cTTextFont);

    CTTextFont addNewCs();

    void unsetCs();

    CTTextFont getSym();

    boolean isSetSym();

    void setSym(CTTextFont cTTextFont);

    CTTextFont addNewSym();

    void unsetSym();

    CTHyperlink getHlinkClick();

    boolean isSetHlinkClick();

    void setHlinkClick(CTHyperlink cTHyperlink);

    CTHyperlink addNewHlinkClick();

    void unsetHlinkClick();

    CTHyperlink getHlinkMouseOver();

    boolean isSetHlinkMouseOver();

    void setHlinkMouseOver(CTHyperlink cTHyperlink);

    CTHyperlink addNewHlinkMouseOver();

    void unsetHlinkMouseOver();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getKumimoji();

    XmlBoolean xgetKumimoji();

    boolean isSetKumimoji();

    void setKumimoji(boolean z);

    void xsetKumimoji(XmlBoolean xmlBoolean);

    void unsetKumimoji();

    String getLang();

    STTextLanguageID xgetLang();

    boolean isSetLang();

    void setLang(String str);

    void xsetLang(STTextLanguageID sTTextLanguageID);

    void unsetLang();

    String getAltLang();

    STTextLanguageID xgetAltLang();

    boolean isSetAltLang();

    void setAltLang(String str);

    void xsetAltLang(STTextLanguageID sTTextLanguageID);

    void unsetAltLang();

    int getSz();

    STTextFontSize xgetSz();

    boolean isSetSz();

    void setSz(int i);

    void xsetSz(STTextFontSize sTTextFontSize);

    void unsetSz();

    boolean getB();

    XmlBoolean xgetB();

    boolean isSetB();

    void setB(boolean z);

    void xsetB(XmlBoolean xmlBoolean);

    void unsetB();

    boolean getI();

    XmlBoolean xgetI();

    boolean isSetI();

    void setI(boolean z);

    void xsetI(XmlBoolean xmlBoolean);

    void unsetI();

    STTextUnderlineType.Enum getU();

    STTextUnderlineType xgetU();

    boolean isSetU();

    void setU(STTextUnderlineType.Enum r1);

    void xsetU(STTextUnderlineType sTTextUnderlineType);

    void unsetU();

    STTextStrikeType.Enum getStrike();

    STTextStrikeType xgetStrike();

    boolean isSetStrike();

    void setStrike(STTextStrikeType.Enum r1);

    void xsetStrike(STTextStrikeType sTTextStrikeType);

    void unsetStrike();

    int getKern();

    STTextNonNegativePoint xgetKern();

    boolean isSetKern();

    void setKern(int i);

    void xsetKern(STTextNonNegativePoint sTTextNonNegativePoint);

    void unsetKern();

    STTextCapsType.Enum getCap();

    STTextCapsType xgetCap();

    boolean isSetCap();

    void setCap(STTextCapsType.Enum r1);

    void xsetCap(STTextCapsType sTTextCapsType);

    void unsetCap();

    int getSpc();

    STTextPoint xgetSpc();

    boolean isSetSpc();

    void setSpc(int i);

    void xsetSpc(STTextPoint sTTextPoint);

    void unsetSpc();

    boolean getNormalizeH();

    XmlBoolean xgetNormalizeH();

    boolean isSetNormalizeH();

    void setNormalizeH(boolean z);

    void xsetNormalizeH(XmlBoolean xmlBoolean);

    void unsetNormalizeH();

    int getBaseline();

    STPercentage xgetBaseline();

    boolean isSetBaseline();

    void setBaseline(int i);

    void xsetBaseline(STPercentage sTPercentage);

    void unsetBaseline();

    boolean getNoProof();

    XmlBoolean xgetNoProof();

    boolean isSetNoProof();

    void setNoProof(boolean z);

    void xsetNoProof(XmlBoolean xmlBoolean);

    void unsetNoProof();

    boolean getDirty();

    XmlBoolean xgetDirty();

    boolean isSetDirty();

    void setDirty(boolean z);

    void xsetDirty(XmlBoolean xmlBoolean);

    void unsetDirty();

    boolean getErr();

    XmlBoolean xgetErr();

    boolean isSetErr();

    void setErr(boolean z);

    void xsetErr(XmlBoolean xmlBoolean);

    void unsetErr();

    boolean getSmtClean();

    XmlBoolean xgetSmtClean();

    boolean isSetSmtClean();

    void setSmtClean(boolean z);

    void xsetSmtClean(XmlBoolean xmlBoolean);

    void unsetSmtClean();

    long getSmtId();

    XmlUnsignedInt xgetSmtId();

    boolean isSetSmtId();

    void setSmtId(long j);

    void xsetSmtId(XmlUnsignedInt xmlUnsignedInt);

    void unsetSmtId();

    String getBmk();

    XmlString xgetBmk();

    boolean isSetBmk();

    void setBmk(String str);

    void xsetBmk(XmlString xmlString);

    void unsetBmk();
}
