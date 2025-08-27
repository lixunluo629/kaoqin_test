package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.STPercentage;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTPresentation.class */
public interface CTPresentation extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPresentation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpresentation56cbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTPresentation$Factory.class */
    public static final class Factory {
        public static CTPresentation newInstance() {
            return (CTPresentation) POIXMLTypeLoader.newInstance(CTPresentation.type, null);
        }

        public static CTPresentation newInstance(XmlOptions xmlOptions) {
            return (CTPresentation) POIXMLTypeLoader.newInstance(CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(String str) throws XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(str, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(str, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(File file) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(file, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(file, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(URL url) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(url, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(url, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(inputStream, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(inputStream, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(Reader reader) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(reader, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPresentation) POIXMLTypeLoader.parse(reader, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(xMLStreamReader, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(xMLStreamReader, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(Node node) throws XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(node, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(node, CTPresentation.type, xmlOptions);
        }

        public static CTPresentation parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(xMLInputStream, CTPresentation.type, (XmlOptions) null);
        }

        public static CTPresentation parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPresentation) POIXMLTypeLoader.parse(xMLInputStream, CTPresentation.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPresentation.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPresentation.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTSlideMasterIdList getSldMasterIdLst();

    boolean isSetSldMasterIdLst();

    void setSldMasterIdLst(CTSlideMasterIdList cTSlideMasterIdList);

    CTSlideMasterIdList addNewSldMasterIdLst();

    void unsetSldMasterIdLst();

    CTNotesMasterIdList getNotesMasterIdLst();

    boolean isSetNotesMasterIdLst();

    void setNotesMasterIdLst(CTNotesMasterIdList cTNotesMasterIdList);

    CTNotesMasterIdList addNewNotesMasterIdLst();

    void unsetNotesMasterIdLst();

    CTHandoutMasterIdList getHandoutMasterIdLst();

    boolean isSetHandoutMasterIdLst();

    void setHandoutMasterIdLst(CTHandoutMasterIdList cTHandoutMasterIdList);

    CTHandoutMasterIdList addNewHandoutMasterIdLst();

    void unsetHandoutMasterIdLst();

    CTSlideIdList getSldIdLst();

    boolean isSetSldIdLst();

    void setSldIdLst(CTSlideIdList cTSlideIdList);

    CTSlideIdList addNewSldIdLst();

    void unsetSldIdLst();

    CTSlideSize getSldSz();

    boolean isSetSldSz();

    void setSldSz(CTSlideSize cTSlideSize);

    CTSlideSize addNewSldSz();

    void unsetSldSz();

    CTPositiveSize2D getNotesSz();

    void setNotesSz(CTPositiveSize2D cTPositiveSize2D);

    CTPositiveSize2D addNewNotesSz();

    CTSmartTags getSmartTags();

    boolean isSetSmartTags();

    void setSmartTags(CTSmartTags cTSmartTags);

    CTSmartTags addNewSmartTags();

    void unsetSmartTags();

    CTEmbeddedFontList getEmbeddedFontLst();

    boolean isSetEmbeddedFontLst();

    void setEmbeddedFontLst(CTEmbeddedFontList cTEmbeddedFontList);

    CTEmbeddedFontList addNewEmbeddedFontLst();

    void unsetEmbeddedFontLst();

    CTCustomShowList getCustShowLst();

    boolean isSetCustShowLst();

    void setCustShowLst(CTCustomShowList cTCustomShowList);

    CTCustomShowList addNewCustShowLst();

    void unsetCustShowLst();

    CTPhotoAlbum getPhotoAlbum();

    boolean isSetPhotoAlbum();

    void setPhotoAlbum(CTPhotoAlbum cTPhotoAlbum);

    CTPhotoAlbum addNewPhotoAlbum();

    void unsetPhotoAlbum();

    CTCustomerDataList getCustDataLst();

    boolean isSetCustDataLst();

    void setCustDataLst(CTCustomerDataList cTCustomerDataList);

    CTCustomerDataList addNewCustDataLst();

    void unsetCustDataLst();

    CTKinsoku getKinsoku();

    boolean isSetKinsoku();

    void setKinsoku(CTKinsoku cTKinsoku);

    CTKinsoku addNewKinsoku();

    void unsetKinsoku();

    CTTextListStyle getDefaultTextStyle();

    boolean isSetDefaultTextStyle();

    void setDefaultTextStyle(CTTextListStyle cTTextListStyle);

    CTTextListStyle addNewDefaultTextStyle();

    void unsetDefaultTextStyle();

    CTModifyVerifier getModifyVerifier();

    boolean isSetModifyVerifier();

    void setModifyVerifier(CTModifyVerifier cTModifyVerifier);

    CTModifyVerifier addNewModifyVerifier();

    void unsetModifyVerifier();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    int getServerZoom();

    STPercentage xgetServerZoom();

    boolean isSetServerZoom();

    void setServerZoom(int i);

    void xsetServerZoom(STPercentage sTPercentage);

    void unsetServerZoom();

    int getFirstSlideNum();

    XmlInt xgetFirstSlideNum();

    boolean isSetFirstSlideNum();

    void setFirstSlideNum(int i);

    void xsetFirstSlideNum(XmlInt xmlInt);

    void unsetFirstSlideNum();

    boolean getShowSpecialPlsOnTitleSld();

    XmlBoolean xgetShowSpecialPlsOnTitleSld();

    boolean isSetShowSpecialPlsOnTitleSld();

    void setShowSpecialPlsOnTitleSld(boolean z);

    void xsetShowSpecialPlsOnTitleSld(XmlBoolean xmlBoolean);

    void unsetShowSpecialPlsOnTitleSld();

    boolean getRtl();

    XmlBoolean xgetRtl();

    boolean isSetRtl();

    void setRtl(boolean z);

    void xsetRtl(XmlBoolean xmlBoolean);

    void unsetRtl();

    boolean getRemovePersonalInfoOnSave();

    XmlBoolean xgetRemovePersonalInfoOnSave();

    boolean isSetRemovePersonalInfoOnSave();

    void setRemovePersonalInfoOnSave(boolean z);

    void xsetRemovePersonalInfoOnSave(XmlBoolean xmlBoolean);

    void unsetRemovePersonalInfoOnSave();

    boolean getCompatMode();

    XmlBoolean xgetCompatMode();

    boolean isSetCompatMode();

    void setCompatMode(boolean z);

    void xsetCompatMode(XmlBoolean xmlBoolean);

    void unsetCompatMode();

    boolean getStrictFirstAndLastChars();

    XmlBoolean xgetStrictFirstAndLastChars();

    boolean isSetStrictFirstAndLastChars();

    void setStrictFirstAndLastChars(boolean z);

    void xsetStrictFirstAndLastChars(XmlBoolean xmlBoolean);

    void unsetStrictFirstAndLastChars();

    boolean getEmbedTrueTypeFonts();

    XmlBoolean xgetEmbedTrueTypeFonts();

    boolean isSetEmbedTrueTypeFonts();

    void setEmbedTrueTypeFonts(boolean z);

    void xsetEmbedTrueTypeFonts(XmlBoolean xmlBoolean);

    void unsetEmbedTrueTypeFonts();

    boolean getSaveSubsetFonts();

    XmlBoolean xgetSaveSubsetFonts();

    boolean isSetSaveSubsetFonts();

    void setSaveSubsetFonts(boolean z);

    void xsetSaveSubsetFonts(XmlBoolean xmlBoolean);

    void unsetSaveSubsetFonts();

    boolean getAutoCompressPictures();

    XmlBoolean xgetAutoCompressPictures();

    boolean isSetAutoCompressPictures();

    void setAutoCompressPictures(boolean z);

    void xsetAutoCompressPictures(XmlBoolean xmlBoolean);

    void unsetAutoCompressPictures();

    long getBookmarkIdSeed();

    STBookmarkIdSeed xgetBookmarkIdSeed();

    boolean isSetBookmarkIdSeed();

    void setBookmarkIdSeed(long j);

    void xsetBookmarkIdSeed(STBookmarkIdSeed sTBookmarkIdSeed);

    void unsetBookmarkIdSeed();
}
