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
import org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr;
import org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSettings.class */
public interface CTSettings extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSettings.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsettingsd6a5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSettings$Factory.class */
    public static final class Factory {
        public static CTSettings newInstance() {
            return (CTSettings) POIXMLTypeLoader.newInstance(CTSettings.type, null);
        }

        public static CTSettings newInstance(XmlOptions xmlOptions) {
            return (CTSettings) POIXMLTypeLoader.newInstance(CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(String str) throws XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(str, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(str, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(File file) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(file, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(file, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(URL url) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(url, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(url, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(inputStream, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(inputStream, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(Reader reader) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(reader, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSettings) POIXMLTypeLoader.parse(reader, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(xMLStreamReader, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(xMLStreamReader, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(Node node) throws XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(node, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(node, CTSettings.type, xmlOptions);
        }

        public static CTSettings parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(xMLInputStream, CTSettings.type, (XmlOptions) null);
        }

        public static CTSettings parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSettings) POIXMLTypeLoader.parse(xMLInputStream, CTSettings.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSettings.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSettings.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTWriteProtection getWriteProtection();

    boolean isSetWriteProtection();

    void setWriteProtection(CTWriteProtection cTWriteProtection);

    CTWriteProtection addNewWriteProtection();

    void unsetWriteProtection();

    CTView getView();

    boolean isSetView();

    void setView(CTView cTView);

    CTView addNewView();

    void unsetView();

    CTZoom getZoom();

    boolean isSetZoom();

    void setZoom(CTZoom cTZoom);

    CTZoom addNewZoom();

    void unsetZoom();

    CTOnOff getRemovePersonalInformation();

    boolean isSetRemovePersonalInformation();

    void setRemovePersonalInformation(CTOnOff cTOnOff);

    CTOnOff addNewRemovePersonalInformation();

    void unsetRemovePersonalInformation();

    CTOnOff getRemoveDateAndTime();

    boolean isSetRemoveDateAndTime();

    void setRemoveDateAndTime(CTOnOff cTOnOff);

    CTOnOff addNewRemoveDateAndTime();

    void unsetRemoveDateAndTime();

    CTOnOff getDoNotDisplayPageBoundaries();

    boolean isSetDoNotDisplayPageBoundaries();

    void setDoNotDisplayPageBoundaries(CTOnOff cTOnOff);

    CTOnOff addNewDoNotDisplayPageBoundaries();

    void unsetDoNotDisplayPageBoundaries();

    CTOnOff getDisplayBackgroundShape();

    boolean isSetDisplayBackgroundShape();

    void setDisplayBackgroundShape(CTOnOff cTOnOff);

    CTOnOff addNewDisplayBackgroundShape();

    void unsetDisplayBackgroundShape();

    CTOnOff getPrintPostScriptOverText();

    boolean isSetPrintPostScriptOverText();

    void setPrintPostScriptOverText(CTOnOff cTOnOff);

    CTOnOff addNewPrintPostScriptOverText();

    void unsetPrintPostScriptOverText();

    CTOnOff getPrintFractionalCharacterWidth();

    boolean isSetPrintFractionalCharacterWidth();

    void setPrintFractionalCharacterWidth(CTOnOff cTOnOff);

    CTOnOff addNewPrintFractionalCharacterWidth();

    void unsetPrintFractionalCharacterWidth();

    CTOnOff getPrintFormsData();

    boolean isSetPrintFormsData();

    void setPrintFormsData(CTOnOff cTOnOff);

    CTOnOff addNewPrintFormsData();

    void unsetPrintFormsData();

    CTOnOff getEmbedTrueTypeFonts();

    boolean isSetEmbedTrueTypeFonts();

    void setEmbedTrueTypeFonts(CTOnOff cTOnOff);

    CTOnOff addNewEmbedTrueTypeFonts();

    void unsetEmbedTrueTypeFonts();

    CTOnOff getEmbedSystemFonts();

    boolean isSetEmbedSystemFonts();

    void setEmbedSystemFonts(CTOnOff cTOnOff);

    CTOnOff addNewEmbedSystemFonts();

    void unsetEmbedSystemFonts();

    CTOnOff getSaveSubsetFonts();

    boolean isSetSaveSubsetFonts();

    void setSaveSubsetFonts(CTOnOff cTOnOff);

    CTOnOff addNewSaveSubsetFonts();

    void unsetSaveSubsetFonts();

    CTOnOff getSaveFormsData();

    boolean isSetSaveFormsData();

    void setSaveFormsData(CTOnOff cTOnOff);

    CTOnOff addNewSaveFormsData();

    void unsetSaveFormsData();

    CTOnOff getMirrorMargins();

    boolean isSetMirrorMargins();

    void setMirrorMargins(CTOnOff cTOnOff);

    CTOnOff addNewMirrorMargins();

    void unsetMirrorMargins();

    CTOnOff getAlignBordersAndEdges();

    boolean isSetAlignBordersAndEdges();

    void setAlignBordersAndEdges(CTOnOff cTOnOff);

    CTOnOff addNewAlignBordersAndEdges();

    void unsetAlignBordersAndEdges();

    CTOnOff getBordersDoNotSurroundHeader();

    boolean isSetBordersDoNotSurroundHeader();

    void setBordersDoNotSurroundHeader(CTOnOff cTOnOff);

    CTOnOff addNewBordersDoNotSurroundHeader();

    void unsetBordersDoNotSurroundHeader();

    CTOnOff getBordersDoNotSurroundFooter();

    boolean isSetBordersDoNotSurroundFooter();

    void setBordersDoNotSurroundFooter(CTOnOff cTOnOff);

    CTOnOff addNewBordersDoNotSurroundFooter();

    void unsetBordersDoNotSurroundFooter();

    CTOnOff getGutterAtTop();

    boolean isSetGutterAtTop();

    void setGutterAtTop(CTOnOff cTOnOff);

    CTOnOff addNewGutterAtTop();

    void unsetGutterAtTop();

    CTOnOff getHideSpellingErrors();

    boolean isSetHideSpellingErrors();

    void setHideSpellingErrors(CTOnOff cTOnOff);

    CTOnOff addNewHideSpellingErrors();

    void unsetHideSpellingErrors();

    CTOnOff getHideGrammaticalErrors();

    boolean isSetHideGrammaticalErrors();

    void setHideGrammaticalErrors(CTOnOff cTOnOff);

    CTOnOff addNewHideGrammaticalErrors();

    void unsetHideGrammaticalErrors();

    List<CTWritingStyle> getActiveWritingStyleList();

    CTWritingStyle[] getActiveWritingStyleArray();

    CTWritingStyle getActiveWritingStyleArray(int i);

    int sizeOfActiveWritingStyleArray();

    void setActiveWritingStyleArray(CTWritingStyle[] cTWritingStyleArr);

    void setActiveWritingStyleArray(int i, CTWritingStyle cTWritingStyle);

    CTWritingStyle insertNewActiveWritingStyle(int i);

    CTWritingStyle addNewActiveWritingStyle();

    void removeActiveWritingStyle(int i);

    CTProof getProofState();

    boolean isSetProofState();

    void setProofState(CTProof cTProof);

    CTProof addNewProofState();

    void unsetProofState();

    CTOnOff getFormsDesign();

    boolean isSetFormsDesign();

    void setFormsDesign(CTOnOff cTOnOff);

    CTOnOff addNewFormsDesign();

    void unsetFormsDesign();

    CTRel getAttachedTemplate();

    boolean isSetAttachedTemplate();

    void setAttachedTemplate(CTRel cTRel);

    CTRel addNewAttachedTemplate();

    void unsetAttachedTemplate();

    CTOnOff getLinkStyles();

    boolean isSetLinkStyles();

    void setLinkStyles(CTOnOff cTOnOff);

    CTOnOff addNewLinkStyles();

    void unsetLinkStyles();

    CTShortHexNumber getStylePaneFormatFilter();

    boolean isSetStylePaneFormatFilter();

    void setStylePaneFormatFilter(CTShortHexNumber cTShortHexNumber);

    CTShortHexNumber addNewStylePaneFormatFilter();

    void unsetStylePaneFormatFilter();

    CTShortHexNumber getStylePaneSortMethod();

    boolean isSetStylePaneSortMethod();

    void setStylePaneSortMethod(CTShortHexNumber cTShortHexNumber);

    CTShortHexNumber addNewStylePaneSortMethod();

    void unsetStylePaneSortMethod();

    CTDocType getDocumentType();

    boolean isSetDocumentType();

    void setDocumentType(CTDocType cTDocType);

    CTDocType addNewDocumentType();

    void unsetDocumentType();

    CTMailMerge getMailMerge();

    boolean isSetMailMerge();

    void setMailMerge(CTMailMerge cTMailMerge);

    CTMailMerge addNewMailMerge();

    void unsetMailMerge();

    CTTrackChangesView getRevisionView();

    boolean isSetRevisionView();

    void setRevisionView(CTTrackChangesView cTTrackChangesView);

    CTTrackChangesView addNewRevisionView();

    void unsetRevisionView();

    CTOnOff getTrackRevisions();

    boolean isSetTrackRevisions();

    void setTrackRevisions(CTOnOff cTOnOff);

    CTOnOff addNewTrackRevisions();

    void unsetTrackRevisions();

    CTOnOff getDoNotTrackMoves();

    boolean isSetDoNotTrackMoves();

    void setDoNotTrackMoves(CTOnOff cTOnOff);

    CTOnOff addNewDoNotTrackMoves();

    void unsetDoNotTrackMoves();

    CTOnOff getDoNotTrackFormatting();

    boolean isSetDoNotTrackFormatting();

    void setDoNotTrackFormatting(CTOnOff cTOnOff);

    CTOnOff addNewDoNotTrackFormatting();

    void unsetDoNotTrackFormatting();

    CTDocProtect getDocumentProtection();

    boolean isSetDocumentProtection();

    void setDocumentProtection(CTDocProtect cTDocProtect);

    CTDocProtect addNewDocumentProtection();

    void unsetDocumentProtection();

    CTOnOff getAutoFormatOverride();

    boolean isSetAutoFormatOverride();

    void setAutoFormatOverride(CTOnOff cTOnOff);

    CTOnOff addNewAutoFormatOverride();

    void unsetAutoFormatOverride();

    CTOnOff getStyleLockTheme();

    boolean isSetStyleLockTheme();

    void setStyleLockTheme(CTOnOff cTOnOff);

    CTOnOff addNewStyleLockTheme();

    void unsetStyleLockTheme();

    CTOnOff getStyleLockQFSet();

    boolean isSetStyleLockQFSet();

    void setStyleLockQFSet(CTOnOff cTOnOff);

    CTOnOff addNewStyleLockQFSet();

    void unsetStyleLockQFSet();

    CTTwipsMeasure getDefaultTabStop();

    boolean isSetDefaultTabStop();

    void setDefaultTabStop(CTTwipsMeasure cTTwipsMeasure);

    CTTwipsMeasure addNewDefaultTabStop();

    void unsetDefaultTabStop();

    CTOnOff getAutoHyphenation();

    boolean isSetAutoHyphenation();

    void setAutoHyphenation(CTOnOff cTOnOff);

    CTOnOff addNewAutoHyphenation();

    void unsetAutoHyphenation();

    CTDecimalNumber getConsecutiveHyphenLimit();

    boolean isSetConsecutiveHyphenLimit();

    void setConsecutiveHyphenLimit(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewConsecutiveHyphenLimit();

    void unsetConsecutiveHyphenLimit();

    CTTwipsMeasure getHyphenationZone();

    boolean isSetHyphenationZone();

    void setHyphenationZone(CTTwipsMeasure cTTwipsMeasure);

    CTTwipsMeasure addNewHyphenationZone();

    void unsetHyphenationZone();

    CTOnOff getDoNotHyphenateCaps();

    boolean isSetDoNotHyphenateCaps();

    void setDoNotHyphenateCaps(CTOnOff cTOnOff);

    CTOnOff addNewDoNotHyphenateCaps();

    void unsetDoNotHyphenateCaps();

    CTOnOff getShowEnvelope();

    boolean isSetShowEnvelope();

    void setShowEnvelope(CTOnOff cTOnOff);

    CTOnOff addNewShowEnvelope();

    void unsetShowEnvelope();

    CTDecimalNumber getSummaryLength();

    boolean isSetSummaryLength();

    void setSummaryLength(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewSummaryLength();

    void unsetSummaryLength();

    CTString getClickAndTypeStyle();

    boolean isSetClickAndTypeStyle();

    void setClickAndTypeStyle(CTString cTString);

    CTString addNewClickAndTypeStyle();

    void unsetClickAndTypeStyle();

    CTString getDefaultTableStyle();

    boolean isSetDefaultTableStyle();

    void setDefaultTableStyle(CTString cTString);

    CTString addNewDefaultTableStyle();

    void unsetDefaultTableStyle();

    CTOnOff getEvenAndOddHeaders();

    boolean isSetEvenAndOddHeaders();

    void setEvenAndOddHeaders(CTOnOff cTOnOff);

    CTOnOff addNewEvenAndOddHeaders();

    void unsetEvenAndOddHeaders();

    CTOnOff getBookFoldRevPrinting();

    boolean isSetBookFoldRevPrinting();

    void setBookFoldRevPrinting(CTOnOff cTOnOff);

    CTOnOff addNewBookFoldRevPrinting();

    void unsetBookFoldRevPrinting();

    CTOnOff getBookFoldPrinting();

    boolean isSetBookFoldPrinting();

    void setBookFoldPrinting(CTOnOff cTOnOff);

    CTOnOff addNewBookFoldPrinting();

    void unsetBookFoldPrinting();

    CTDecimalNumber getBookFoldPrintingSheets();

    boolean isSetBookFoldPrintingSheets();

    void setBookFoldPrintingSheets(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewBookFoldPrintingSheets();

    void unsetBookFoldPrintingSheets();

    CTTwipsMeasure getDrawingGridHorizontalSpacing();

    boolean isSetDrawingGridHorizontalSpacing();

    void setDrawingGridHorizontalSpacing(CTTwipsMeasure cTTwipsMeasure);

    CTTwipsMeasure addNewDrawingGridHorizontalSpacing();

    void unsetDrawingGridHorizontalSpacing();

    CTTwipsMeasure getDrawingGridVerticalSpacing();

    boolean isSetDrawingGridVerticalSpacing();

    void setDrawingGridVerticalSpacing(CTTwipsMeasure cTTwipsMeasure);

    CTTwipsMeasure addNewDrawingGridVerticalSpacing();

    void unsetDrawingGridVerticalSpacing();

    CTDecimalNumber getDisplayHorizontalDrawingGridEvery();

    boolean isSetDisplayHorizontalDrawingGridEvery();

    void setDisplayHorizontalDrawingGridEvery(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewDisplayHorizontalDrawingGridEvery();

    void unsetDisplayHorizontalDrawingGridEvery();

    CTDecimalNumber getDisplayVerticalDrawingGridEvery();

    boolean isSetDisplayVerticalDrawingGridEvery();

    void setDisplayVerticalDrawingGridEvery(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewDisplayVerticalDrawingGridEvery();

    void unsetDisplayVerticalDrawingGridEvery();

    CTOnOff getDoNotUseMarginsForDrawingGridOrigin();

    boolean isSetDoNotUseMarginsForDrawingGridOrigin();

    void setDoNotUseMarginsForDrawingGridOrigin(CTOnOff cTOnOff);

    CTOnOff addNewDoNotUseMarginsForDrawingGridOrigin();

    void unsetDoNotUseMarginsForDrawingGridOrigin();

    CTTwipsMeasure getDrawingGridHorizontalOrigin();

    boolean isSetDrawingGridHorizontalOrigin();

    void setDrawingGridHorizontalOrigin(CTTwipsMeasure cTTwipsMeasure);

    CTTwipsMeasure addNewDrawingGridHorizontalOrigin();

    void unsetDrawingGridHorizontalOrigin();

    CTTwipsMeasure getDrawingGridVerticalOrigin();

    boolean isSetDrawingGridVerticalOrigin();

    void setDrawingGridVerticalOrigin(CTTwipsMeasure cTTwipsMeasure);

    CTTwipsMeasure addNewDrawingGridVerticalOrigin();

    void unsetDrawingGridVerticalOrigin();

    CTOnOff getDoNotShadeFormData();

    boolean isSetDoNotShadeFormData();

    void setDoNotShadeFormData(CTOnOff cTOnOff);

    CTOnOff addNewDoNotShadeFormData();

    void unsetDoNotShadeFormData();

    CTOnOff getNoPunctuationKerning();

    boolean isSetNoPunctuationKerning();

    void setNoPunctuationKerning(CTOnOff cTOnOff);

    CTOnOff addNewNoPunctuationKerning();

    void unsetNoPunctuationKerning();

    CTCharacterSpacing getCharacterSpacingControl();

    boolean isSetCharacterSpacingControl();

    void setCharacterSpacingControl(CTCharacterSpacing cTCharacterSpacing);

    CTCharacterSpacing addNewCharacterSpacingControl();

    void unsetCharacterSpacingControl();

    CTOnOff getPrintTwoOnOne();

    boolean isSetPrintTwoOnOne();

    void setPrintTwoOnOne(CTOnOff cTOnOff);

    CTOnOff addNewPrintTwoOnOne();

    void unsetPrintTwoOnOne();

    CTOnOff getStrictFirstAndLastChars();

    boolean isSetStrictFirstAndLastChars();

    void setStrictFirstAndLastChars(CTOnOff cTOnOff);

    CTOnOff addNewStrictFirstAndLastChars();

    void unsetStrictFirstAndLastChars();

    CTKinsoku getNoLineBreaksAfter();

    boolean isSetNoLineBreaksAfter();

    void setNoLineBreaksAfter(CTKinsoku cTKinsoku);

    CTKinsoku addNewNoLineBreaksAfter();

    void unsetNoLineBreaksAfter();

    CTKinsoku getNoLineBreaksBefore();

    boolean isSetNoLineBreaksBefore();

    void setNoLineBreaksBefore(CTKinsoku cTKinsoku);

    CTKinsoku addNewNoLineBreaksBefore();

    void unsetNoLineBreaksBefore();

    CTOnOff getSavePreviewPicture();

    boolean isSetSavePreviewPicture();

    void setSavePreviewPicture(CTOnOff cTOnOff);

    CTOnOff addNewSavePreviewPicture();

    void unsetSavePreviewPicture();

    CTOnOff getDoNotValidateAgainstSchema();

    boolean isSetDoNotValidateAgainstSchema();

    void setDoNotValidateAgainstSchema(CTOnOff cTOnOff);

    CTOnOff addNewDoNotValidateAgainstSchema();

    void unsetDoNotValidateAgainstSchema();

    CTOnOff getSaveInvalidXml();

    boolean isSetSaveInvalidXml();

    void setSaveInvalidXml(CTOnOff cTOnOff);

    CTOnOff addNewSaveInvalidXml();

    void unsetSaveInvalidXml();

    CTOnOff getIgnoreMixedContent();

    boolean isSetIgnoreMixedContent();

    void setIgnoreMixedContent(CTOnOff cTOnOff);

    CTOnOff addNewIgnoreMixedContent();

    void unsetIgnoreMixedContent();

    CTOnOff getAlwaysShowPlaceholderText();

    boolean isSetAlwaysShowPlaceholderText();

    void setAlwaysShowPlaceholderText(CTOnOff cTOnOff);

    CTOnOff addNewAlwaysShowPlaceholderText();

    void unsetAlwaysShowPlaceholderText();

    CTOnOff getDoNotDemarcateInvalidXml();

    boolean isSetDoNotDemarcateInvalidXml();

    void setDoNotDemarcateInvalidXml(CTOnOff cTOnOff);

    CTOnOff addNewDoNotDemarcateInvalidXml();

    void unsetDoNotDemarcateInvalidXml();

    CTOnOff getSaveXmlDataOnly();

    boolean isSetSaveXmlDataOnly();

    void setSaveXmlDataOnly(CTOnOff cTOnOff);

    CTOnOff addNewSaveXmlDataOnly();

    void unsetSaveXmlDataOnly();

    CTOnOff getUseXSLTWhenSaving();

    boolean isSetUseXSLTWhenSaving();

    void setUseXSLTWhenSaving(CTOnOff cTOnOff);

    CTOnOff addNewUseXSLTWhenSaving();

    void unsetUseXSLTWhenSaving();

    CTSaveThroughXslt getSaveThroughXslt();

    boolean isSetSaveThroughXslt();

    void setSaveThroughXslt(CTSaveThroughXslt cTSaveThroughXslt);

    CTSaveThroughXslt addNewSaveThroughXslt();

    void unsetSaveThroughXslt();

    CTOnOff getShowXMLTags();

    boolean isSetShowXMLTags();

    void setShowXMLTags(CTOnOff cTOnOff);

    CTOnOff addNewShowXMLTags();

    void unsetShowXMLTags();

    CTOnOff getAlwaysMergeEmptyNamespace();

    boolean isSetAlwaysMergeEmptyNamespace();

    void setAlwaysMergeEmptyNamespace(CTOnOff cTOnOff);

    CTOnOff addNewAlwaysMergeEmptyNamespace();

    void unsetAlwaysMergeEmptyNamespace();

    CTOnOff getUpdateFields();

    boolean isSetUpdateFields();

    void setUpdateFields(CTOnOff cTOnOff);

    CTOnOff addNewUpdateFields();

    void unsetUpdateFields();

    CTShapeDefaults getHdrShapeDefaults();

    boolean isSetHdrShapeDefaults();

    void setHdrShapeDefaults(CTShapeDefaults cTShapeDefaults);

    CTShapeDefaults addNewHdrShapeDefaults();

    void unsetHdrShapeDefaults();

    CTFtnDocProps getFootnotePr();

    boolean isSetFootnotePr();

    void setFootnotePr(CTFtnDocProps cTFtnDocProps);

    CTFtnDocProps addNewFootnotePr();

    void unsetFootnotePr();

    CTEdnDocProps getEndnotePr();

    boolean isSetEndnotePr();

    void setEndnotePr(CTEdnDocProps cTEdnDocProps);

    CTEdnDocProps addNewEndnotePr();

    void unsetEndnotePr();

    CTCompat getCompat();

    boolean isSetCompat();

    void setCompat(CTCompat cTCompat);

    CTCompat addNewCompat();

    void unsetCompat();

    CTDocVars getDocVars();

    boolean isSetDocVars();

    void setDocVars(CTDocVars cTDocVars);

    CTDocVars addNewDocVars();

    void unsetDocVars();

    CTDocRsids getRsids();

    boolean isSetRsids();

    void setRsids(CTDocRsids cTDocRsids);

    CTDocRsids addNewRsids();

    void unsetRsids();

    CTMathPr getMathPr();

    boolean isSetMathPr();

    void setMathPr(CTMathPr cTMathPr);

    CTMathPr addNewMathPr();

    void unsetMathPr();

    CTOnOff getUiCompat97To2003();

    boolean isSetUiCompat97To2003();

    void setUiCompat97To2003(CTOnOff cTOnOff);

    CTOnOff addNewUiCompat97To2003();

    void unsetUiCompat97To2003();

    List<CTString> getAttachedSchemaList();

    CTString[] getAttachedSchemaArray();

    CTString getAttachedSchemaArray(int i);

    int sizeOfAttachedSchemaArray();

    void setAttachedSchemaArray(CTString[] cTStringArr);

    void setAttachedSchemaArray(int i, CTString cTString);

    CTString insertNewAttachedSchema(int i);

    CTString addNewAttachedSchema();

    void removeAttachedSchema(int i);

    CTLanguage getThemeFontLang();

    boolean isSetThemeFontLang();

    void setThemeFontLang(CTLanguage cTLanguage);

    CTLanguage addNewThemeFontLang();

    void unsetThemeFontLang();

    CTColorSchemeMapping getClrSchemeMapping();

    boolean isSetClrSchemeMapping();

    void setClrSchemeMapping(CTColorSchemeMapping cTColorSchemeMapping);

    CTColorSchemeMapping addNewClrSchemeMapping();

    void unsetClrSchemeMapping();

    CTOnOff getDoNotIncludeSubdocsInStats();

    boolean isSetDoNotIncludeSubdocsInStats();

    void setDoNotIncludeSubdocsInStats(CTOnOff cTOnOff);

    CTOnOff addNewDoNotIncludeSubdocsInStats();

    void unsetDoNotIncludeSubdocsInStats();

    CTOnOff getDoNotAutoCompressPictures();

    boolean isSetDoNotAutoCompressPictures();

    void setDoNotAutoCompressPictures(CTOnOff cTOnOff);

    CTOnOff addNewDoNotAutoCompressPictures();

    void unsetDoNotAutoCompressPictures();

    CTEmpty getForceUpgrade();

    boolean isSetForceUpgrade();

    void setForceUpgrade(CTEmpty cTEmpty);

    CTEmpty addNewForceUpgrade();

    void unsetForceUpgrade();

    CTCaptions getCaptions();

    boolean isSetCaptions();

    void setCaptions(CTCaptions cTCaptions);

    CTCaptions addNewCaptions();

    void unsetCaptions();

    CTReadingModeInkLockDown getReadModeInkLockDown();

    boolean isSetReadModeInkLockDown();

    void setReadModeInkLockDown(CTReadingModeInkLockDown cTReadingModeInkLockDown);

    CTReadingModeInkLockDown addNewReadModeInkLockDown();

    void unsetReadModeInkLockDown();

    List<CTSmartTagType> getSmartTagTypeList();

    CTSmartTagType[] getSmartTagTypeArray();

    CTSmartTagType getSmartTagTypeArray(int i);

    int sizeOfSmartTagTypeArray();

    void setSmartTagTypeArray(CTSmartTagType[] cTSmartTagTypeArr);

    void setSmartTagTypeArray(int i, CTSmartTagType cTSmartTagType);

    CTSmartTagType insertNewSmartTagType(int i);

    CTSmartTagType addNewSmartTagType();

    void removeSmartTagType(int i);

    CTSchemaLibrary getSchemaLibrary();

    boolean isSetSchemaLibrary();

    void setSchemaLibrary(CTSchemaLibrary cTSchemaLibrary);

    CTSchemaLibrary addNewSchemaLibrary();

    void unsetSchemaLibrary();

    CTShapeDefaults getShapeDefaults();

    boolean isSetShapeDefaults();

    void setShapeDefaults(CTShapeDefaults cTShapeDefaults);

    CTShapeDefaults addNewShapeDefaults();

    void unsetShapeDefaults();

    CTOnOff getDoNotEmbedSmartTags();

    boolean isSetDoNotEmbedSmartTags();

    void setDoNotEmbedSmartTags(CTOnOff cTOnOff);

    CTOnOff addNewDoNotEmbedSmartTags();

    void unsetDoNotEmbedSmartTags();

    CTString getDecimalSymbol();

    boolean isSetDecimalSymbol();

    void setDecimalSymbol(CTString cTString);

    CTString addNewDecimalSymbol();

    void unsetDecimalSymbol();

    CTString getListSeparator();

    boolean isSetListSeparator();

    void setListSeparator(CTString cTString);

    CTString addNewListSeparator();

    void unsetListSeparator();
}
