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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextParagraphProperties.class */
public interface CTTextParagraphProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextParagraphProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextparagraphpropertiesdd05type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextParagraphProperties$Factory.class */
    public static final class Factory {
        public static CTTextParagraphProperties newInstance() {
            return (CTTextParagraphProperties) POIXMLTypeLoader.newInstance(CTTextParagraphProperties.type, null);
        }

        public static CTTextParagraphProperties newInstance(XmlOptions xmlOptions) {
            return (CTTextParagraphProperties) POIXMLTypeLoader.newInstance(CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(String str) throws XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(str, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(str, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(File file) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(file, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(file, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(URL url) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(url, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(url, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(inputStream, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(inputStream, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(Reader reader) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(reader, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(reader, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(Node node) throws XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(node, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(node, CTTextParagraphProperties.type, xmlOptions);
        }

        public static CTTextParagraphProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTextParagraphProperties.type, (XmlOptions) null);
        }

        public static CTTextParagraphProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextParagraphProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTextParagraphProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextParagraphProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextParagraphProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextSpacing getLnSpc();

    boolean isSetLnSpc();

    void setLnSpc(CTTextSpacing cTTextSpacing);

    CTTextSpacing addNewLnSpc();

    void unsetLnSpc();

    CTTextSpacing getSpcBef();

    boolean isSetSpcBef();

    void setSpcBef(CTTextSpacing cTTextSpacing);

    CTTextSpacing addNewSpcBef();

    void unsetSpcBef();

    CTTextSpacing getSpcAft();

    boolean isSetSpcAft();

    void setSpcAft(CTTextSpacing cTTextSpacing);

    CTTextSpacing addNewSpcAft();

    void unsetSpcAft();

    CTTextBulletColorFollowText getBuClrTx();

    boolean isSetBuClrTx();

    void setBuClrTx(CTTextBulletColorFollowText cTTextBulletColorFollowText);

    CTTextBulletColorFollowText addNewBuClrTx();

    void unsetBuClrTx();

    CTColor getBuClr();

    boolean isSetBuClr();

    void setBuClr(CTColor cTColor);

    CTColor addNewBuClr();

    void unsetBuClr();

    CTTextBulletSizeFollowText getBuSzTx();

    boolean isSetBuSzTx();

    void setBuSzTx(CTTextBulletSizeFollowText cTTextBulletSizeFollowText);

    CTTextBulletSizeFollowText addNewBuSzTx();

    void unsetBuSzTx();

    CTTextBulletSizePercent getBuSzPct();

    boolean isSetBuSzPct();

    void setBuSzPct(CTTextBulletSizePercent cTTextBulletSizePercent);

    CTTextBulletSizePercent addNewBuSzPct();

    void unsetBuSzPct();

    CTTextBulletSizePoint getBuSzPts();

    boolean isSetBuSzPts();

    void setBuSzPts(CTTextBulletSizePoint cTTextBulletSizePoint);

    CTTextBulletSizePoint addNewBuSzPts();

    void unsetBuSzPts();

    CTTextBulletTypefaceFollowText getBuFontTx();

    boolean isSetBuFontTx();

    void setBuFontTx(CTTextBulletTypefaceFollowText cTTextBulletTypefaceFollowText);

    CTTextBulletTypefaceFollowText addNewBuFontTx();

    void unsetBuFontTx();

    CTTextFont getBuFont();

    boolean isSetBuFont();

    void setBuFont(CTTextFont cTTextFont);

    CTTextFont addNewBuFont();

    void unsetBuFont();

    CTTextNoBullet getBuNone();

    boolean isSetBuNone();

    void setBuNone(CTTextNoBullet cTTextNoBullet);

    CTTextNoBullet addNewBuNone();

    void unsetBuNone();

    CTTextAutonumberBullet getBuAutoNum();

    boolean isSetBuAutoNum();

    void setBuAutoNum(CTTextAutonumberBullet cTTextAutonumberBullet);

    CTTextAutonumberBullet addNewBuAutoNum();

    void unsetBuAutoNum();

    CTTextCharBullet getBuChar();

    boolean isSetBuChar();

    void setBuChar(CTTextCharBullet cTTextCharBullet);

    CTTextCharBullet addNewBuChar();

    void unsetBuChar();

    CTTextBlipBullet getBuBlip();

    boolean isSetBuBlip();

    void setBuBlip(CTTextBlipBullet cTTextBlipBullet);

    CTTextBlipBullet addNewBuBlip();

    void unsetBuBlip();

    CTTextTabStopList getTabLst();

    boolean isSetTabLst();

    void setTabLst(CTTextTabStopList cTTextTabStopList);

    CTTextTabStopList addNewTabLst();

    void unsetTabLst();

    CTTextCharacterProperties getDefRPr();

    boolean isSetDefRPr();

    void setDefRPr(CTTextCharacterProperties cTTextCharacterProperties);

    CTTextCharacterProperties addNewDefRPr();

    void unsetDefRPr();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    int getMarL();

    STTextMargin xgetMarL();

    boolean isSetMarL();

    void setMarL(int i);

    void xsetMarL(STTextMargin sTTextMargin);

    void unsetMarL();

    int getMarR();

    STTextMargin xgetMarR();

    boolean isSetMarR();

    void setMarR(int i);

    void xsetMarR(STTextMargin sTTextMargin);

    void unsetMarR();

    int getLvl();

    STTextIndentLevelType xgetLvl();

    boolean isSetLvl();

    void setLvl(int i);

    void xsetLvl(STTextIndentLevelType sTTextIndentLevelType);

    void unsetLvl();

    int getIndent();

    STTextIndent xgetIndent();

    boolean isSetIndent();

    void setIndent(int i);

    void xsetIndent(STTextIndent sTTextIndent);

    void unsetIndent();

    STTextAlignType.Enum getAlgn();

    STTextAlignType xgetAlgn();

    boolean isSetAlgn();

    void setAlgn(STTextAlignType.Enum r1);

    void xsetAlgn(STTextAlignType sTTextAlignType);

    void unsetAlgn();

    int getDefTabSz();

    STCoordinate32 xgetDefTabSz();

    boolean isSetDefTabSz();

    void setDefTabSz(int i);

    void xsetDefTabSz(STCoordinate32 sTCoordinate32);

    void unsetDefTabSz();

    boolean getRtl();

    XmlBoolean xgetRtl();

    boolean isSetRtl();

    void setRtl(boolean z);

    void xsetRtl(XmlBoolean xmlBoolean);

    void unsetRtl();

    boolean getEaLnBrk();

    XmlBoolean xgetEaLnBrk();

    boolean isSetEaLnBrk();

    void setEaLnBrk(boolean z);

    void xsetEaLnBrk(XmlBoolean xmlBoolean);

    void unsetEaLnBrk();

    STTextFontAlignType.Enum getFontAlgn();

    STTextFontAlignType xgetFontAlgn();

    boolean isSetFontAlgn();

    void setFontAlgn(STTextFontAlignType.Enum r1);

    void xsetFontAlgn(STTextFontAlignType sTTextFontAlignType);

    void unsetFontAlgn();

    boolean getLatinLnBrk();

    XmlBoolean xgetLatinLnBrk();

    boolean isSetLatinLnBrk();

    void setLatinLnBrk(boolean z);

    void xsetLatinLnBrk(XmlBoolean xmlBoolean);

    void unsetLatinLnBrk();

    boolean getHangingPunct();

    XmlBoolean xgetHangingPunct();

    boolean isSetHangingPunct();

    void setHangingPunct(boolean z);

    void xsetHangingPunct(XmlBoolean xmlBoolean);

    void unsetHangingPunct();
}
