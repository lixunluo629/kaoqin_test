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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRPr.class */
public interface CTRPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrpr097etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRPr$Factory.class */
    public static final class Factory {
        public static CTRPr newInstance() {
            return (CTRPr) POIXMLTypeLoader.newInstance(CTRPr.type, null);
        }

        public static CTRPr newInstance(XmlOptions xmlOptions) {
            return (CTRPr) POIXMLTypeLoader.newInstance(CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(String str) throws XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(str, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(str, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(File file) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(file, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(file, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(URL url) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(url, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(url, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(inputStream, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(inputStream, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(Reader reader) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(reader, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPr) POIXMLTypeLoader.parse(reader, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(xMLStreamReader, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(xMLStreamReader, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(Node node) throws XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(node, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(node, CTRPr.type, xmlOptions);
        }

        public static CTRPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(xMLInputStream, CTRPr.type, (XmlOptions) null);
        }

        public static CTRPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRPr) POIXMLTypeLoader.parse(xMLInputStream, CTRPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTString getRStyle();

    boolean isSetRStyle();

    void setRStyle(CTString cTString);

    CTString addNewRStyle();

    void unsetRStyle();

    CTFonts getRFonts();

    boolean isSetRFonts();

    void setRFonts(CTFonts cTFonts);

    CTFonts addNewRFonts();

    void unsetRFonts();

    CTOnOff getB();

    boolean isSetB();

    void setB(CTOnOff cTOnOff);

    CTOnOff addNewB();

    void unsetB();

    CTOnOff getBCs();

    boolean isSetBCs();

    void setBCs(CTOnOff cTOnOff);

    CTOnOff addNewBCs();

    void unsetBCs();

    CTOnOff getI();

    boolean isSetI();

    void setI(CTOnOff cTOnOff);

    CTOnOff addNewI();

    void unsetI();

    CTOnOff getICs();

    boolean isSetICs();

    void setICs(CTOnOff cTOnOff);

    CTOnOff addNewICs();

    void unsetICs();

    CTOnOff getCaps();

    boolean isSetCaps();

    void setCaps(CTOnOff cTOnOff);

    CTOnOff addNewCaps();

    void unsetCaps();

    CTOnOff getSmallCaps();

    boolean isSetSmallCaps();

    void setSmallCaps(CTOnOff cTOnOff);

    CTOnOff addNewSmallCaps();

    void unsetSmallCaps();

    CTOnOff getStrike();

    boolean isSetStrike();

    void setStrike(CTOnOff cTOnOff);

    CTOnOff addNewStrike();

    void unsetStrike();

    CTOnOff getDstrike();

    boolean isSetDstrike();

    void setDstrike(CTOnOff cTOnOff);

    CTOnOff addNewDstrike();

    void unsetDstrike();

    CTOnOff getOutline();

    boolean isSetOutline();

    void setOutline(CTOnOff cTOnOff);

    CTOnOff addNewOutline();

    void unsetOutline();

    CTOnOff getShadow();

    boolean isSetShadow();

    void setShadow(CTOnOff cTOnOff);

    CTOnOff addNewShadow();

    void unsetShadow();

    CTOnOff getEmboss();

    boolean isSetEmboss();

    void setEmboss(CTOnOff cTOnOff);

    CTOnOff addNewEmboss();

    void unsetEmboss();

    CTOnOff getImprint();

    boolean isSetImprint();

    void setImprint(CTOnOff cTOnOff);

    CTOnOff addNewImprint();

    void unsetImprint();

    CTOnOff getNoProof();

    boolean isSetNoProof();

    void setNoProof(CTOnOff cTOnOff);

    CTOnOff addNewNoProof();

    void unsetNoProof();

    CTOnOff getSnapToGrid();

    boolean isSetSnapToGrid();

    void setSnapToGrid(CTOnOff cTOnOff);

    CTOnOff addNewSnapToGrid();

    void unsetSnapToGrid();

    CTOnOff getVanish();

    boolean isSetVanish();

    void setVanish(CTOnOff cTOnOff);

    CTOnOff addNewVanish();

    void unsetVanish();

    CTOnOff getWebHidden();

    boolean isSetWebHidden();

    void setWebHidden(CTOnOff cTOnOff);

    CTOnOff addNewWebHidden();

    void unsetWebHidden();

    CTColor getColor();

    boolean isSetColor();

    void setColor(CTColor cTColor);

    CTColor addNewColor();

    void unsetColor();

    CTSignedTwipsMeasure getSpacing();

    boolean isSetSpacing();

    void setSpacing(CTSignedTwipsMeasure cTSignedTwipsMeasure);

    CTSignedTwipsMeasure addNewSpacing();

    void unsetSpacing();

    CTTextScale getW();

    boolean isSetW();

    void setW(CTTextScale cTTextScale);

    CTTextScale addNewW();

    void unsetW();

    CTHpsMeasure getKern();

    boolean isSetKern();

    void setKern(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewKern();

    void unsetKern();

    CTSignedHpsMeasure getPosition();

    boolean isSetPosition();

    void setPosition(CTSignedHpsMeasure cTSignedHpsMeasure);

    CTSignedHpsMeasure addNewPosition();

    void unsetPosition();

    CTHpsMeasure getSz();

    boolean isSetSz();

    void setSz(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewSz();

    void unsetSz();

    CTHpsMeasure getSzCs();

    boolean isSetSzCs();

    void setSzCs(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewSzCs();

    void unsetSzCs();

    CTHighlight getHighlight();

    boolean isSetHighlight();

    void setHighlight(CTHighlight cTHighlight);

    CTHighlight addNewHighlight();

    void unsetHighlight();

    CTUnderline getU();

    boolean isSetU();

    void setU(CTUnderline cTUnderline);

    CTUnderline addNewU();

    void unsetU();

    CTTextEffect getEffect();

    boolean isSetEffect();

    void setEffect(CTTextEffect cTTextEffect);

    CTTextEffect addNewEffect();

    void unsetEffect();

    CTBorder getBdr();

    boolean isSetBdr();

    void setBdr(CTBorder cTBorder);

    CTBorder addNewBdr();

    void unsetBdr();

    CTShd getShd();

    boolean isSetShd();

    void setShd(CTShd cTShd);

    CTShd addNewShd();

    void unsetShd();

    CTFitText getFitText();

    boolean isSetFitText();

    void setFitText(CTFitText cTFitText);

    CTFitText addNewFitText();

    void unsetFitText();

    CTVerticalAlignRun getVertAlign();

    boolean isSetVertAlign();

    void setVertAlign(CTVerticalAlignRun cTVerticalAlignRun);

    CTVerticalAlignRun addNewVertAlign();

    void unsetVertAlign();

    CTOnOff getRtl();

    boolean isSetRtl();

    void setRtl(CTOnOff cTOnOff);

    CTOnOff addNewRtl();

    void unsetRtl();

    CTOnOff getCs();

    boolean isSetCs();

    void setCs(CTOnOff cTOnOff);

    CTOnOff addNewCs();

    void unsetCs();

    CTEm getEm();

    boolean isSetEm();

    void setEm(CTEm cTEm);

    CTEm addNewEm();

    void unsetEm();

    CTLanguage getLang();

    boolean isSetLang();

    void setLang(CTLanguage cTLanguage);

    CTLanguage addNewLang();

    void unsetLang();

    CTEastAsianLayout getEastAsianLayout();

    boolean isSetEastAsianLayout();

    void setEastAsianLayout(CTEastAsianLayout cTEastAsianLayout);

    CTEastAsianLayout addNewEastAsianLayout();

    void unsetEastAsianLayout();

    CTOnOff getSpecVanish();

    boolean isSetSpecVanish();

    void setSpecVanish(CTOnOff cTOnOff);

    CTOnOff addNewSpecVanish();

    void unsetSpecVanish();

    CTOnOff getOMath();

    boolean isSetOMath();

    void setOMath(CTOnOff cTOnOff);

    CTOnOff addNewOMath();

    void unsetOMath();

    CTRPrChange getRPrChange();

    boolean isSetRPrChange();

    void setRPrChange(CTRPrChange cTRPrChange);

    CTRPrChange addNewRPrChange();

    void unsetRPrChange();
}
