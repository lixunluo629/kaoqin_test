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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTScRgbColor.class */
public interface CTScRgbColor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTScRgbColor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctscrgbcolorf3e1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTScRgbColor$Factory.class */
    public static final class Factory {
        public static CTScRgbColor newInstance() {
            return (CTScRgbColor) POIXMLTypeLoader.newInstance(CTScRgbColor.type, null);
        }

        public static CTScRgbColor newInstance(XmlOptions xmlOptions) {
            return (CTScRgbColor) POIXMLTypeLoader.newInstance(CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(String str) throws XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(str, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(str, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(File file) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(file, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(file, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(URL url) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(url, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(url, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(inputStream, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(inputStream, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(Reader reader) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(reader, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(reader, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(xMLStreamReader, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(xMLStreamReader, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(Node node) throws XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(node, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(node, CTScRgbColor.type, xmlOptions);
        }

        public static CTScRgbColor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(xMLInputStream, CTScRgbColor.type, (XmlOptions) null);
        }

        public static CTScRgbColor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTScRgbColor) POIXMLTypeLoader.parse(xMLInputStream, CTScRgbColor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScRgbColor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScRgbColor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTPositiveFixedPercentage> getTintList();

    CTPositiveFixedPercentage[] getTintArray();

    CTPositiveFixedPercentage getTintArray(int i);

    int sizeOfTintArray();

    void setTintArray(CTPositiveFixedPercentage[] cTPositiveFixedPercentageArr);

    void setTintArray(int i, CTPositiveFixedPercentage cTPositiveFixedPercentage);

    CTPositiveFixedPercentage insertNewTint(int i);

    CTPositiveFixedPercentage addNewTint();

    void removeTint(int i);

    List<CTPositiveFixedPercentage> getShadeList();

    CTPositiveFixedPercentage[] getShadeArray();

    CTPositiveFixedPercentage getShadeArray(int i);

    int sizeOfShadeArray();

    void setShadeArray(CTPositiveFixedPercentage[] cTPositiveFixedPercentageArr);

    void setShadeArray(int i, CTPositiveFixedPercentage cTPositiveFixedPercentage);

    CTPositiveFixedPercentage insertNewShade(int i);

    CTPositiveFixedPercentage addNewShade();

    void removeShade(int i);

    List<CTComplementTransform> getCompList();

    CTComplementTransform[] getCompArray();

    CTComplementTransform getCompArray(int i);

    int sizeOfCompArray();

    void setCompArray(CTComplementTransform[] cTComplementTransformArr);

    void setCompArray(int i, CTComplementTransform cTComplementTransform);

    CTComplementTransform insertNewComp(int i);

    CTComplementTransform addNewComp();

    void removeComp(int i);

    List<CTInverseTransform> getInvList();

    CTInverseTransform[] getInvArray();

    CTInverseTransform getInvArray(int i);

    int sizeOfInvArray();

    void setInvArray(CTInverseTransform[] cTInverseTransformArr);

    void setInvArray(int i, CTInverseTransform cTInverseTransform);

    CTInverseTransform insertNewInv(int i);

    CTInverseTransform addNewInv();

    void removeInv(int i);

    List<CTGrayscaleTransform> getGrayList();

    CTGrayscaleTransform[] getGrayArray();

    CTGrayscaleTransform getGrayArray(int i);

    int sizeOfGrayArray();

    void setGrayArray(CTGrayscaleTransform[] cTGrayscaleTransformArr);

    void setGrayArray(int i, CTGrayscaleTransform cTGrayscaleTransform);

    CTGrayscaleTransform insertNewGray(int i);

    CTGrayscaleTransform addNewGray();

    void removeGray(int i);

    List<CTPositiveFixedPercentage> getAlphaList();

    CTPositiveFixedPercentage[] getAlphaArray();

    CTPositiveFixedPercentage getAlphaArray(int i);

    int sizeOfAlphaArray();

    void setAlphaArray(CTPositiveFixedPercentage[] cTPositiveFixedPercentageArr);

    void setAlphaArray(int i, CTPositiveFixedPercentage cTPositiveFixedPercentage);

    CTPositiveFixedPercentage insertNewAlpha(int i);

    CTPositiveFixedPercentage addNewAlpha();

    void removeAlpha(int i);

    List<CTFixedPercentage> getAlphaOffList();

    CTFixedPercentage[] getAlphaOffArray();

    CTFixedPercentage getAlphaOffArray(int i);

    int sizeOfAlphaOffArray();

    void setAlphaOffArray(CTFixedPercentage[] cTFixedPercentageArr);

    void setAlphaOffArray(int i, CTFixedPercentage cTFixedPercentage);

    CTFixedPercentage insertNewAlphaOff(int i);

    CTFixedPercentage addNewAlphaOff();

    void removeAlphaOff(int i);

    List<CTPositivePercentage> getAlphaModList();

    CTPositivePercentage[] getAlphaModArray();

    CTPositivePercentage getAlphaModArray(int i);

    int sizeOfAlphaModArray();

    void setAlphaModArray(CTPositivePercentage[] cTPositivePercentageArr);

    void setAlphaModArray(int i, CTPositivePercentage cTPositivePercentage);

    CTPositivePercentage insertNewAlphaMod(int i);

    CTPositivePercentage addNewAlphaMod();

    void removeAlphaMod(int i);

    List<CTPositiveFixedAngle> getHueList();

    CTPositiveFixedAngle[] getHueArray();

    CTPositiveFixedAngle getHueArray(int i);

    int sizeOfHueArray();

    void setHueArray(CTPositiveFixedAngle[] cTPositiveFixedAngleArr);

    void setHueArray(int i, CTPositiveFixedAngle cTPositiveFixedAngle);

    CTPositiveFixedAngle insertNewHue(int i);

    CTPositiveFixedAngle addNewHue();

    void removeHue(int i);

    List<CTAngle> getHueOffList();

    CTAngle[] getHueOffArray();

    CTAngle getHueOffArray(int i);

    int sizeOfHueOffArray();

    void setHueOffArray(CTAngle[] cTAngleArr);

    void setHueOffArray(int i, CTAngle cTAngle);

    CTAngle insertNewHueOff(int i);

    CTAngle addNewHueOff();

    void removeHueOff(int i);

    List<CTPositivePercentage> getHueModList();

    CTPositivePercentage[] getHueModArray();

    CTPositivePercentage getHueModArray(int i);

    int sizeOfHueModArray();

    void setHueModArray(CTPositivePercentage[] cTPositivePercentageArr);

    void setHueModArray(int i, CTPositivePercentage cTPositivePercentage);

    CTPositivePercentage insertNewHueMod(int i);

    CTPositivePercentage addNewHueMod();

    void removeHueMod(int i);

    List<CTPercentage> getSatList();

    CTPercentage[] getSatArray();

    CTPercentage getSatArray(int i);

    int sizeOfSatArray();

    void setSatArray(CTPercentage[] cTPercentageArr);

    void setSatArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewSat(int i);

    CTPercentage addNewSat();

    void removeSat(int i);

    List<CTPercentage> getSatOffList();

    CTPercentage[] getSatOffArray();

    CTPercentage getSatOffArray(int i);

    int sizeOfSatOffArray();

    void setSatOffArray(CTPercentage[] cTPercentageArr);

    void setSatOffArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewSatOff(int i);

    CTPercentage addNewSatOff();

    void removeSatOff(int i);

    List<CTPercentage> getSatModList();

    CTPercentage[] getSatModArray();

    CTPercentage getSatModArray(int i);

    int sizeOfSatModArray();

    void setSatModArray(CTPercentage[] cTPercentageArr);

    void setSatModArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewSatMod(int i);

    CTPercentage addNewSatMod();

    void removeSatMod(int i);

    List<CTPercentage> getLumList();

    CTPercentage[] getLumArray();

    CTPercentage getLumArray(int i);

    int sizeOfLumArray();

    void setLumArray(CTPercentage[] cTPercentageArr);

    void setLumArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewLum(int i);

    CTPercentage addNewLum();

    void removeLum(int i);

    List<CTPercentage> getLumOffList();

    CTPercentage[] getLumOffArray();

    CTPercentage getLumOffArray(int i);

    int sizeOfLumOffArray();

    void setLumOffArray(CTPercentage[] cTPercentageArr);

    void setLumOffArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewLumOff(int i);

    CTPercentage addNewLumOff();

    void removeLumOff(int i);

    List<CTPercentage> getLumModList();

    CTPercentage[] getLumModArray();

    CTPercentage getLumModArray(int i);

    int sizeOfLumModArray();

    void setLumModArray(CTPercentage[] cTPercentageArr);

    void setLumModArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewLumMod(int i);

    CTPercentage addNewLumMod();

    void removeLumMod(int i);

    List<CTPercentage> getRedList();

    CTPercentage[] getRedArray();

    CTPercentage getRedArray(int i);

    int sizeOfRedArray();

    void setRedArray(CTPercentage[] cTPercentageArr);

    void setRedArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewRed(int i);

    CTPercentage addNewRed();

    void removeRed(int i);

    List<CTPercentage> getRedOffList();

    CTPercentage[] getRedOffArray();

    CTPercentage getRedOffArray(int i);

    int sizeOfRedOffArray();

    void setRedOffArray(CTPercentage[] cTPercentageArr);

    void setRedOffArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewRedOff(int i);

    CTPercentage addNewRedOff();

    void removeRedOff(int i);

    List<CTPercentage> getRedModList();

    CTPercentage[] getRedModArray();

    CTPercentage getRedModArray(int i);

    int sizeOfRedModArray();

    void setRedModArray(CTPercentage[] cTPercentageArr);

    void setRedModArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewRedMod(int i);

    CTPercentage addNewRedMod();

    void removeRedMod(int i);

    List<CTPercentage> getGreenList();

    CTPercentage[] getGreenArray();

    CTPercentage getGreenArray(int i);

    int sizeOfGreenArray();

    void setGreenArray(CTPercentage[] cTPercentageArr);

    void setGreenArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewGreen(int i);

    CTPercentage addNewGreen();

    void removeGreen(int i);

    List<CTPercentage> getGreenOffList();

    CTPercentage[] getGreenOffArray();

    CTPercentage getGreenOffArray(int i);

    int sizeOfGreenOffArray();

    void setGreenOffArray(CTPercentage[] cTPercentageArr);

    void setGreenOffArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewGreenOff(int i);

    CTPercentage addNewGreenOff();

    void removeGreenOff(int i);

    List<CTPercentage> getGreenModList();

    CTPercentage[] getGreenModArray();

    CTPercentage getGreenModArray(int i);

    int sizeOfGreenModArray();

    void setGreenModArray(CTPercentage[] cTPercentageArr);

    void setGreenModArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewGreenMod(int i);

    CTPercentage addNewGreenMod();

    void removeGreenMod(int i);

    List<CTPercentage> getBlueList();

    CTPercentage[] getBlueArray();

    CTPercentage getBlueArray(int i);

    int sizeOfBlueArray();

    void setBlueArray(CTPercentage[] cTPercentageArr);

    void setBlueArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewBlue(int i);

    CTPercentage addNewBlue();

    void removeBlue(int i);

    List<CTPercentage> getBlueOffList();

    CTPercentage[] getBlueOffArray();

    CTPercentage getBlueOffArray(int i);

    int sizeOfBlueOffArray();

    void setBlueOffArray(CTPercentage[] cTPercentageArr);

    void setBlueOffArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewBlueOff(int i);

    CTPercentage addNewBlueOff();

    void removeBlueOff(int i);

    List<CTPercentage> getBlueModList();

    CTPercentage[] getBlueModArray();

    CTPercentage getBlueModArray(int i);

    int sizeOfBlueModArray();

    void setBlueModArray(CTPercentage[] cTPercentageArr);

    void setBlueModArray(int i, CTPercentage cTPercentage);

    CTPercentage insertNewBlueMod(int i);

    CTPercentage addNewBlueMod();

    void removeBlueMod(int i);

    List<CTGammaTransform> getGammaList();

    CTGammaTransform[] getGammaArray();

    CTGammaTransform getGammaArray(int i);

    int sizeOfGammaArray();

    void setGammaArray(CTGammaTransform[] cTGammaTransformArr);

    void setGammaArray(int i, CTGammaTransform cTGammaTransform);

    CTGammaTransform insertNewGamma(int i);

    CTGammaTransform addNewGamma();

    void removeGamma(int i);

    List<CTInverseGammaTransform> getInvGammaList();

    CTInverseGammaTransform[] getInvGammaArray();

    CTInverseGammaTransform getInvGammaArray(int i);

    int sizeOfInvGammaArray();

    void setInvGammaArray(CTInverseGammaTransform[] cTInverseGammaTransformArr);

    void setInvGammaArray(int i, CTInverseGammaTransform cTInverseGammaTransform);

    CTInverseGammaTransform insertNewInvGamma(int i);

    CTInverseGammaTransform addNewInvGamma();

    void removeInvGamma(int i);

    int getR();

    STPercentage xgetR();

    void setR(int i);

    void xsetR(STPercentage sTPercentage);

    int getG();

    STPercentage xgetG();

    void setG(int i);

    void xsetG(STPercentage sTPercentage);

    int getB();

    STPercentage xgetB();

    void setB(int i);

    void xsetB(STPercentage sTPercentage);
}
