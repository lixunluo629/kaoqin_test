package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTBlip.class */
public interface CTBlip extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBlip.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctblip034ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTBlip$Factory.class */
    public static final class Factory {
        public static CTBlip newInstance() {
            return (CTBlip) POIXMLTypeLoader.newInstance(CTBlip.type, null);
        }

        public static CTBlip newInstance(XmlOptions xmlOptions) {
            return (CTBlip) POIXMLTypeLoader.newInstance(CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(String str) throws XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(str, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(str, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(File file) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(file, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(file, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(URL url) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(url, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(url, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(inputStream, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(inputStream, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(Reader reader) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(reader, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBlip) POIXMLTypeLoader.parse(reader, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(xMLStreamReader, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(xMLStreamReader, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(Node node) throws XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(node, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(node, CTBlip.type, xmlOptions);
        }

        public static CTBlip parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(xMLInputStream, CTBlip.type, (XmlOptions) null);
        }

        public static CTBlip parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBlip) POIXMLTypeLoader.parse(xMLInputStream, CTBlip.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBlip.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBlip.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTAlphaBiLevelEffect> getAlphaBiLevelList();

    CTAlphaBiLevelEffect[] getAlphaBiLevelArray();

    CTAlphaBiLevelEffect getAlphaBiLevelArray(int i);

    int sizeOfAlphaBiLevelArray();

    void setAlphaBiLevelArray(CTAlphaBiLevelEffect[] cTAlphaBiLevelEffectArr);

    void setAlphaBiLevelArray(int i, CTAlphaBiLevelEffect cTAlphaBiLevelEffect);

    CTAlphaBiLevelEffect insertNewAlphaBiLevel(int i);

    CTAlphaBiLevelEffect addNewAlphaBiLevel();

    void removeAlphaBiLevel(int i);

    List<CTAlphaCeilingEffect> getAlphaCeilingList();

    CTAlphaCeilingEffect[] getAlphaCeilingArray();

    CTAlphaCeilingEffect getAlphaCeilingArray(int i);

    int sizeOfAlphaCeilingArray();

    void setAlphaCeilingArray(CTAlphaCeilingEffect[] cTAlphaCeilingEffectArr);

    void setAlphaCeilingArray(int i, CTAlphaCeilingEffect cTAlphaCeilingEffect);

    CTAlphaCeilingEffect insertNewAlphaCeiling(int i);

    CTAlphaCeilingEffect addNewAlphaCeiling();

    void removeAlphaCeiling(int i);

    List<CTAlphaFloorEffect> getAlphaFloorList();

    CTAlphaFloorEffect[] getAlphaFloorArray();

    CTAlphaFloorEffect getAlphaFloorArray(int i);

    int sizeOfAlphaFloorArray();

    void setAlphaFloorArray(CTAlphaFloorEffect[] cTAlphaFloorEffectArr);

    void setAlphaFloorArray(int i, CTAlphaFloorEffect cTAlphaFloorEffect);

    CTAlphaFloorEffect insertNewAlphaFloor(int i);

    CTAlphaFloorEffect addNewAlphaFloor();

    void removeAlphaFloor(int i);

    List<CTAlphaInverseEffect> getAlphaInvList();

    CTAlphaInverseEffect[] getAlphaInvArray();

    CTAlphaInverseEffect getAlphaInvArray(int i);

    int sizeOfAlphaInvArray();

    void setAlphaInvArray(CTAlphaInverseEffect[] cTAlphaInverseEffectArr);

    void setAlphaInvArray(int i, CTAlphaInverseEffect cTAlphaInverseEffect);

    CTAlphaInverseEffect insertNewAlphaInv(int i);

    CTAlphaInverseEffect addNewAlphaInv();

    void removeAlphaInv(int i);

    List<CTAlphaModulateEffect> getAlphaModList();

    CTAlphaModulateEffect[] getAlphaModArray();

    CTAlphaModulateEffect getAlphaModArray(int i);

    int sizeOfAlphaModArray();

    void setAlphaModArray(CTAlphaModulateEffect[] cTAlphaModulateEffectArr);

    void setAlphaModArray(int i, CTAlphaModulateEffect cTAlphaModulateEffect);

    CTAlphaModulateEffect insertNewAlphaMod(int i);

    CTAlphaModulateEffect addNewAlphaMod();

    void removeAlphaMod(int i);

    List<CTAlphaModulateFixedEffect> getAlphaModFixList();

    CTAlphaModulateFixedEffect[] getAlphaModFixArray();

    CTAlphaModulateFixedEffect getAlphaModFixArray(int i);

    int sizeOfAlphaModFixArray();

    void setAlphaModFixArray(CTAlphaModulateFixedEffect[] cTAlphaModulateFixedEffectArr);

    void setAlphaModFixArray(int i, CTAlphaModulateFixedEffect cTAlphaModulateFixedEffect);

    CTAlphaModulateFixedEffect insertNewAlphaModFix(int i);

    CTAlphaModulateFixedEffect addNewAlphaModFix();

    void removeAlphaModFix(int i);

    List<CTAlphaReplaceEffect> getAlphaReplList();

    CTAlphaReplaceEffect[] getAlphaReplArray();

    CTAlphaReplaceEffect getAlphaReplArray(int i);

    int sizeOfAlphaReplArray();

    void setAlphaReplArray(CTAlphaReplaceEffect[] cTAlphaReplaceEffectArr);

    void setAlphaReplArray(int i, CTAlphaReplaceEffect cTAlphaReplaceEffect);

    CTAlphaReplaceEffect insertNewAlphaRepl(int i);

    CTAlphaReplaceEffect addNewAlphaRepl();

    void removeAlphaRepl(int i);

    List<CTBiLevelEffect> getBiLevelList();

    CTBiLevelEffect[] getBiLevelArray();

    CTBiLevelEffect getBiLevelArray(int i);

    int sizeOfBiLevelArray();

    void setBiLevelArray(CTBiLevelEffect[] cTBiLevelEffectArr);

    void setBiLevelArray(int i, CTBiLevelEffect cTBiLevelEffect);

    CTBiLevelEffect insertNewBiLevel(int i);

    CTBiLevelEffect addNewBiLevel();

    void removeBiLevel(int i);

    List<CTBlurEffect> getBlurList();

    CTBlurEffect[] getBlurArray();

    CTBlurEffect getBlurArray(int i);

    int sizeOfBlurArray();

    void setBlurArray(CTBlurEffect[] cTBlurEffectArr);

    void setBlurArray(int i, CTBlurEffect cTBlurEffect);

    CTBlurEffect insertNewBlur(int i);

    CTBlurEffect addNewBlur();

    void removeBlur(int i);

    List<CTColorChangeEffect> getClrChangeList();

    CTColorChangeEffect[] getClrChangeArray();

    CTColorChangeEffect getClrChangeArray(int i);

    int sizeOfClrChangeArray();

    void setClrChangeArray(CTColorChangeEffect[] cTColorChangeEffectArr);

    void setClrChangeArray(int i, CTColorChangeEffect cTColorChangeEffect);

    CTColorChangeEffect insertNewClrChange(int i);

    CTColorChangeEffect addNewClrChange();

    void removeClrChange(int i);

    List<CTColorReplaceEffect> getClrReplList();

    CTColorReplaceEffect[] getClrReplArray();

    CTColorReplaceEffect getClrReplArray(int i);

    int sizeOfClrReplArray();

    void setClrReplArray(CTColorReplaceEffect[] cTColorReplaceEffectArr);

    void setClrReplArray(int i, CTColorReplaceEffect cTColorReplaceEffect);

    CTColorReplaceEffect insertNewClrRepl(int i);

    CTColorReplaceEffect addNewClrRepl();

    void removeClrRepl(int i);

    List<CTDuotoneEffect> getDuotoneList();

    CTDuotoneEffect[] getDuotoneArray();

    CTDuotoneEffect getDuotoneArray(int i);

    int sizeOfDuotoneArray();

    void setDuotoneArray(CTDuotoneEffect[] cTDuotoneEffectArr);

    void setDuotoneArray(int i, CTDuotoneEffect cTDuotoneEffect);

    CTDuotoneEffect insertNewDuotone(int i);

    CTDuotoneEffect addNewDuotone();

    void removeDuotone(int i);

    List<CTFillOverlayEffect> getFillOverlayList();

    CTFillOverlayEffect[] getFillOverlayArray();

    CTFillOverlayEffect getFillOverlayArray(int i);

    int sizeOfFillOverlayArray();

    void setFillOverlayArray(CTFillOverlayEffect[] cTFillOverlayEffectArr);

    void setFillOverlayArray(int i, CTFillOverlayEffect cTFillOverlayEffect);

    CTFillOverlayEffect insertNewFillOverlay(int i);

    CTFillOverlayEffect addNewFillOverlay();

    void removeFillOverlay(int i);

    List<CTGrayscaleEffect> getGraysclList();

    CTGrayscaleEffect[] getGraysclArray();

    CTGrayscaleEffect getGraysclArray(int i);

    int sizeOfGraysclArray();

    void setGraysclArray(CTGrayscaleEffect[] cTGrayscaleEffectArr);

    void setGraysclArray(int i, CTGrayscaleEffect cTGrayscaleEffect);

    CTGrayscaleEffect insertNewGrayscl(int i);

    CTGrayscaleEffect addNewGrayscl();

    void removeGrayscl(int i);

    List<CTHSLEffect> getHslList();

    CTHSLEffect[] getHslArray();

    CTHSLEffect getHslArray(int i);

    int sizeOfHslArray();

    void setHslArray(CTHSLEffect[] cTHSLEffectArr);

    void setHslArray(int i, CTHSLEffect cTHSLEffect);

    CTHSLEffect insertNewHsl(int i);

    CTHSLEffect addNewHsl();

    void removeHsl(int i);

    List<CTLuminanceEffect> getLumList();

    CTLuminanceEffect[] getLumArray();

    CTLuminanceEffect getLumArray(int i);

    int sizeOfLumArray();

    void setLumArray(CTLuminanceEffect[] cTLuminanceEffectArr);

    void setLumArray(int i, CTLuminanceEffect cTLuminanceEffect);

    CTLuminanceEffect insertNewLum(int i);

    CTLuminanceEffect addNewLum();

    void removeLum(int i);

    List<CTTintEffect> getTintList();

    CTTintEffect[] getTintArray();

    CTTintEffect getTintArray(int i);

    int sizeOfTintArray();

    void setTintArray(CTTintEffect[] cTTintEffectArr);

    void setTintArray(int i, CTTintEffect cTTintEffect);

    CTTintEffect insertNewTint(int i);

    CTTintEffect addNewTint();

    void removeTint(int i);

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    String getEmbed();

    STRelationshipId xgetEmbed();

    boolean isSetEmbed();

    void setEmbed(String str);

    void xsetEmbed(STRelationshipId sTRelationshipId);

    void unsetEmbed();

    String getLink();

    STRelationshipId xgetLink();

    boolean isSetLink();

    void setLink(String str);

    void xsetLink(STRelationshipId sTRelationshipId);

    void unsetLink();

    STBlipCompression$Enum getCstate();

    STBlipCompression xgetCstate();

    boolean isSetCstate();

    void setCstate(STBlipCompression$Enum sTBlipCompression$Enum);

    void xsetCstate(STBlipCompression sTBlipCompression);

    void unsetCstate();
}
