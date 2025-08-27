package org.apache.poi.sl.draw.binding;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlRegistry
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _CTSRgbColorAlpha_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "alpha");
    private static final QName _CTSRgbColorLum_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "lum");
    private static final QName _CTSRgbColorGamma_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "gamma");
    private static final QName _CTSRgbColorInvGamma_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "invGamma");
    private static final QName _CTSRgbColorRedOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "redOff");
    private static final QName _CTSRgbColorAlphaMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "alphaMod");
    private static final QName _CTSRgbColorAlphaOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "alphaOff");
    private static final QName _CTSRgbColorGreenOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "greenOff");
    private static final QName _CTSRgbColorRedMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "redMod");
    private static final QName _CTSRgbColorHue_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "hue");
    private static final QName _CTSRgbColorSatOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "satOff");
    private static final QName _CTSRgbColorGreenMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "greenMod");
    private static final QName _CTSRgbColorSat_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "sat");
    private static final QName _CTSRgbColorBlue_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "blue");
    private static final QName _CTSRgbColorRed_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "red");
    private static final QName _CTSRgbColorSatMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "satMod");
    private static final QName _CTSRgbColorHueOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "hueOff");
    private static final QName _CTSRgbColorBlueMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "blueMod");
    private static final QName _CTSRgbColorShade_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "shade");
    private static final QName _CTSRgbColorLumMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "lumMod");
    private static final QName _CTSRgbColorInv_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "inv");
    private static final QName _CTSRgbColorLumOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "lumOff");
    private static final QName _CTSRgbColorTint_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "tint");
    private static final QName _CTSRgbColorGreen_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "green");
    private static final QName _CTSRgbColorComp_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "comp");
    private static final QName _CTSRgbColorBlueOff_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "blueOff");
    private static final QName _CTSRgbColorHueMod_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "hueMod");
    private static final QName _CTSRgbColorGray_QNAME = new QName(XSSFRelation.NS_DRAWINGML, "gray");

    public CTPositiveSize2D createCTPositiveSize2D() {
        return new CTPositiveSize2D();
    }

    public CTSphereCoords createCTSphereCoords() {
        return new CTSphereCoords();
    }

    public CTPositivePercentage createCTPositivePercentage() {
        return new CTPositivePercentage();
    }

    public CTAdjPoint2D createCTAdjPoint2D() {
        return new CTAdjPoint2D();
    }

    public CTPath2DCubicBezierTo createCTPath2DCubicBezierTo() {
        return new CTPath2DCubicBezierTo();
    }

    public CTEmbeddedWAVAudioFile createCTEmbeddedWAVAudioFile() {
        return new CTEmbeddedWAVAudioFile();
    }

    public CTPresetGeometry2D createCTPresetGeometry2D() {
        return new CTPresetGeometry2D();
    }

    public CTSchemeColor createCTSchemeColor() {
        return new CTSchemeColor();
    }

    public CTInverseTransform createCTInverseTransform() {
        return new CTInverseTransform();
    }

    public CTScRgbColor createCTScRgbColor() {
        return new CTScRgbColor();
    }

    public CTPositiveFixedAngle createCTPositiveFixedAngle() {
        return new CTPositiveFixedAngle();
    }

    public CTInverseGammaTransform createCTInverseGammaTransform() {
        return new CTInverseGammaTransform();
    }

    public CTColorMRU createCTColorMRU() {
        return new CTColorMRU();
    }

    public CTPath2DArcTo createCTPath2DArcTo() {
        return new CTPath2DArcTo();
    }

    public CTSystemColor createCTSystemColor() {
        return new CTSystemColor();
    }

    public CTGroupTransform2D createCTGroupTransform2D() {
        return new CTGroupTransform2D();
    }

    public CTPoint2D createCTPoint2D() {
        return new CTPoint2D();
    }

    public CTGeomRect createCTGeomRect() {
        return new CTGeomRect();
    }

    public CTScale2D createCTScale2D() {
        return new CTScale2D();
    }

    public CTGeomGuide createCTGeomGuide() {
        return new CTGeomGuide();
    }

    public CTXYAdjustHandle createCTXYAdjustHandle() {
        return new CTXYAdjustHandle();
    }

    public CTCustomGeometry2D createCTCustomGeometry2D() {
        return new CTCustomGeometry2D();
    }

    public CTOfficeArtExtension createCTOfficeArtExtension() {
        return new CTOfficeArtExtension();
    }

    public CTGrayscaleTransform createCTGrayscaleTransform() {
        return new CTGrayscaleTransform();
    }

    public CTPath2DClose createCTPath2DClose() {
        return new CTPath2DClose();
    }

    public CTComplementTransform createCTComplementTransform() {
        return new CTComplementTransform();
    }

    public CTPoint3D createCTPoint3D() {
        return new CTPoint3D();
    }

    public CTPositiveFixedPercentage createCTPositiveFixedPercentage() {
        return new CTPositiveFixedPercentage();
    }

    public CTPath2D createCTPath2D() {
        return new CTPath2D();
    }

    public CTAdjustHandleList createCTAdjustHandleList() {
        return new CTAdjustHandleList();
    }

    public CTConnectionSiteList createCTConnectionSiteList() {
        return new CTConnectionSiteList();
    }

    public CTPresetTextShape createCTPresetTextShape() {
        return new CTPresetTextShape();
    }

    public CTSRgbColor createCTSRgbColor() {
        return new CTSRgbColor();
    }

    public CTPath2DMoveTo createCTPath2DMoveTo() {
        return new CTPath2DMoveTo();
    }

    public CTRelativeRect createCTRelativeRect() {
        return new CTRelativeRect();
    }

    public CTPath2DList createCTPath2DList() {
        return new CTPath2DList();
    }

    public CTPolarAdjustHandle createCTPolarAdjustHandle() {
        return new CTPolarAdjustHandle();
    }

    public CTPercentage createCTPercentage() {
        return new CTPercentage();
    }

    public CTHslColor createCTHslColor() {
        return new CTHslColor();
    }

    public CTRatio createCTRatio() {
        return new CTRatio();
    }

    public CTGeomGuideList createCTGeomGuideList() {
        return new CTGeomGuideList();
    }

    public CTTransform2D createCTTransform2D() {
        return new CTTransform2D();
    }

    public CTGammaTransform createCTGammaTransform() {
        return new CTGammaTransform();
    }

    public CTPath2DQuadBezierTo createCTPath2DQuadBezierTo() {
        return new CTPath2DQuadBezierTo();
    }

    public CTAngle createCTAngle() {
        return new CTAngle();
    }

    public CTConnectionSite createCTConnectionSite() {
        return new CTConnectionSite();
    }

    public CTHyperlink createCTHyperlink() {
        return new CTHyperlink();
    }

    public CTFixedPercentage createCTFixedPercentage() {
        return new CTFixedPercentage();
    }

    public CTPath2DLineTo createCTPath2DLineTo() {
        return new CTPath2DLineTo();
    }

    public CTColor createCTColor() {
        return new CTColor();
    }

    public CTPresetColor createCTPresetColor() {
        return new CTPresetColor();
    }

    public CTVector3D createCTVector3D() {
        return new CTVector3D();
    }

    public CTOfficeArtExtensionList createCTOfficeArtExtensionList() {
        return new CTOfficeArtExtensionList();
    }

    public CTConnection createCTConnection() {
        return new CTConnection();
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alpha", scope = CTSRgbColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSRgbColorAlpha(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lum", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorLum(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLum_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gamma", scope = CTSRgbColor.class)
    public JAXBElement<CTGammaTransform> createCTSRgbColorGamma(CTGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "invGamma", scope = CTSRgbColor.class)
    public JAXBElement<CTInverseGammaTransform> createCTSRgbColorInvGamma(CTInverseGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redOff", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorRedOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPositivePercentage> createCTSRgbColorAlphaMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaOff", scope = CTSRgbColor.class)
    public JAXBElement<CTFixedPercentage> createCTSRgbColorAlphaOff(CTFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenOff", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorGreenOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorRedMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hue", scope = CTSRgbColor.class)
    public JAXBElement<CTPositiveFixedAngle> createCTSRgbColorHue(CTPositiveFixedAngle value) {
        return new JAXBElement<>(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satOff", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorSatOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorGreenMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "sat", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorSat(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSat_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blue", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorBlue(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "red", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorRed(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRed_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorSatMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueOff", scope = CTSRgbColor.class)
    public JAXBElement<CTAngle> createCTSRgbColorHueOff(CTAngle value) {
        return new JAXBElement<>(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorBlueMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "shade", scope = CTSRgbColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSRgbColorShade(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorLumMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "inv", scope = CTSRgbColor.class)
    public JAXBElement<CTInverseTransform> createCTSRgbColorInv(CTInverseTransform value) {
        return new JAXBElement<>(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumOff", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorLumOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "tint", scope = CTSRgbColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSRgbColorTint(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "green", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorGreen(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "comp", scope = CTSRgbColor.class)
    public JAXBElement<CTComplementTransform> createCTSRgbColorComp(CTComplementTransform value) {
        return new JAXBElement<>(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueOff", scope = CTSRgbColor.class)
    public JAXBElement<CTPercentage> createCTSRgbColorBlueOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueMod", scope = CTSRgbColor.class)
    public JAXBElement<CTPositivePercentage> createCTSRgbColorHueMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gray", scope = CTSRgbColor.class)
    public JAXBElement<CTGrayscaleTransform> createCTSRgbColorGray(CTGrayscaleTransform value) {
        return new JAXBElement<>(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTSRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lum", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorLum(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLum_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alpha", scope = CTSystemColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSystemColorAlpha(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gamma", scope = CTSystemColor.class)
    public JAXBElement<CTGammaTransform> createCTSystemColorGamma(CTGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "invGamma", scope = CTSystemColor.class)
    public JAXBElement<CTInverseGammaTransform> createCTSystemColorInvGamma(CTInverseGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaMod", scope = CTSystemColor.class)
    public JAXBElement<CTPositivePercentage> createCTSystemColorAlphaMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redOff", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorRedOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaOff", scope = CTSystemColor.class)
    public JAXBElement<CTFixedPercentage> createCTSystemColorAlphaOff(CTFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenOff", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorGreenOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redMod", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorRedMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hue", scope = CTSystemColor.class)
    public JAXBElement<CTPositiveFixedAngle> createCTSystemColorHue(CTPositiveFixedAngle value) {
        return new JAXBElement<>(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satOff", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorSatOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenMod", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorGreenMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blue", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorBlue(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "sat", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorSat(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSat_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "red", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorRed(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRed_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satMod", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorSatMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueMod", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorBlueMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueOff", scope = CTSystemColor.class)
    public JAXBElement<CTAngle> createCTSystemColorHueOff(CTAngle value) {
        return new JAXBElement<>(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "shade", scope = CTSystemColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSystemColorShade(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumMod", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorLumMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "inv", scope = CTSystemColor.class)
    public JAXBElement<CTInverseTransform> createCTSystemColorInv(CTInverseTransform value) {
        return new JAXBElement<>(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumOff", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorLumOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "tint", scope = CTSystemColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSystemColorTint(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "green", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorGreen(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "comp", scope = CTSystemColor.class)
    public JAXBElement<CTComplementTransform> createCTSystemColorComp(CTComplementTransform value) {
        return new JAXBElement<>(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueOff", scope = CTSystemColor.class)
    public JAXBElement<CTPercentage> createCTSystemColorBlueOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueMod", scope = CTSystemColor.class)
    public JAXBElement<CTPositivePercentage> createCTSystemColorHueMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gray", scope = CTSystemColor.class)
    public JAXBElement<CTGrayscaleTransform> createCTSystemColorGray(CTGrayscaleTransform value) {
        return new JAXBElement<>(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTSystemColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lum", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorLum(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLum_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alpha", scope = CTSchemeColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSchemeColorAlpha(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gamma", scope = CTSchemeColor.class)
    public JAXBElement<CTGammaTransform> createCTSchemeColorGamma(CTGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "invGamma", scope = CTSchemeColor.class)
    public JAXBElement<CTInverseGammaTransform> createCTSchemeColorInvGamma(CTInverseGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redOff", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorRedOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPositivePercentage> createCTSchemeColorAlphaMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaOff", scope = CTSchemeColor.class)
    public JAXBElement<CTFixedPercentage> createCTSchemeColorAlphaOff(CTFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenOff", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorGreenOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hue", scope = CTSchemeColor.class)
    public JAXBElement<CTPositiveFixedAngle> createCTSchemeColorHue(CTPositiveFixedAngle value) {
        return new JAXBElement<>(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorRedMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satOff", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorSatOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorGreenMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blue", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorBlue(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "sat", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorSat(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSat_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "red", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorRed(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRed_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorSatMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueOff", scope = CTSchemeColor.class)
    public JAXBElement<CTAngle> createCTSchemeColorHueOff(CTAngle value) {
        return new JAXBElement<>(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorBlueMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "shade", scope = CTSchemeColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSchemeColorShade(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorLumMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "inv", scope = CTSchemeColor.class)
    public JAXBElement<CTInverseTransform> createCTSchemeColorInv(CTInverseTransform value) {
        return new JAXBElement<>(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumOff", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorLumOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "tint", scope = CTSchemeColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTSchemeColorTint(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "green", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorGreen(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "comp", scope = CTSchemeColor.class)
    public JAXBElement<CTComplementTransform> createCTSchemeColorComp(CTComplementTransform value) {
        return new JAXBElement<>(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueOff", scope = CTSchemeColor.class)
    public JAXBElement<CTPercentage> createCTSchemeColorBlueOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueMod", scope = CTSchemeColor.class)
    public JAXBElement<CTPositivePercentage> createCTSchemeColorHueMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gray", scope = CTSchemeColor.class)
    public JAXBElement<CTGrayscaleTransform> createCTSchemeColorGray(CTGrayscaleTransform value) {
        return new JAXBElement<>(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTSchemeColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lum", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorLum(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLum_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alpha", scope = CTScRgbColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTScRgbColorAlpha(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gamma", scope = CTScRgbColor.class)
    public JAXBElement<CTGammaTransform> createCTScRgbColorGamma(CTGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "invGamma", scope = CTScRgbColor.class)
    public JAXBElement<CTInverseGammaTransform> createCTScRgbColorInvGamma(CTInverseGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redOff", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorRedOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPositivePercentage> createCTScRgbColorAlphaMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaOff", scope = CTScRgbColor.class)
    public JAXBElement<CTFixedPercentage> createCTScRgbColorAlphaOff(CTFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenOff", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorGreenOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hue", scope = CTScRgbColor.class)
    public JAXBElement<CTPositiveFixedAngle> createCTScRgbColorHue(CTPositiveFixedAngle value) {
        return new JAXBElement<>(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorRedMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satOff", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorSatOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorGreenMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "sat", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorSat(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSat_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blue", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorBlue(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "red", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorRed(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRed_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorSatMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueOff", scope = CTScRgbColor.class)
    public JAXBElement<CTAngle> createCTScRgbColorHueOff(CTAngle value) {
        return new JAXBElement<>(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorBlueMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "shade", scope = CTScRgbColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTScRgbColorShade(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorLumMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "inv", scope = CTScRgbColor.class)
    public JAXBElement<CTInverseTransform> createCTScRgbColorInv(CTInverseTransform value) {
        return new JAXBElement<>(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumOff", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorLumOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "tint", scope = CTScRgbColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTScRgbColorTint(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "green", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorGreen(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "comp", scope = CTScRgbColor.class)
    public JAXBElement<CTComplementTransform> createCTScRgbColorComp(CTComplementTransform value) {
        return new JAXBElement<>(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueOff", scope = CTScRgbColor.class)
    public JAXBElement<CTPercentage> createCTScRgbColorBlueOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueMod", scope = CTScRgbColor.class)
    public JAXBElement<CTPositivePercentage> createCTScRgbColorHueMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gray", scope = CTScRgbColor.class)
    public JAXBElement<CTGrayscaleTransform> createCTScRgbColorGray(CTGrayscaleTransform value) {
        return new JAXBElement<>(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTScRgbColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alpha", scope = CTHslColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTHslColorAlpha(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lum", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorLum(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLum_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gamma", scope = CTHslColor.class)
    public JAXBElement<CTGammaTransform> createCTHslColorGamma(CTGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "invGamma", scope = CTHslColor.class)
    public JAXBElement<CTInverseGammaTransform> createCTHslColorInvGamma(CTInverseGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaMod", scope = CTHslColor.class)
    public JAXBElement<CTPositivePercentage> createCTHslColorAlphaMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redOff", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorRedOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaOff", scope = CTHslColor.class)
    public JAXBElement<CTFixedPercentage> createCTHslColorAlphaOff(CTFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenOff", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorGreenOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hue", scope = CTHslColor.class)
    public JAXBElement<CTPositiveFixedAngle> createCTHslColorHue(CTPositiveFixedAngle value) {
        return new JAXBElement<>(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redMod", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorRedMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satOff", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorSatOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenMod", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorGreenMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blue", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorBlue(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "sat", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorSat(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSat_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "red", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorRed(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRed_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satMod", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorSatMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueMod", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorBlueMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueOff", scope = CTHslColor.class)
    public JAXBElement<CTAngle> createCTHslColorHueOff(CTAngle value) {
        return new JAXBElement<>(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "shade", scope = CTHslColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTHslColorShade(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumMod", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorLumMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "inv", scope = CTHslColor.class)
    public JAXBElement<CTInverseTransform> createCTHslColorInv(CTInverseTransform value) {
        return new JAXBElement<>(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumOff", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorLumOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "tint", scope = CTHslColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTHslColorTint(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "green", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorGreen(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "comp", scope = CTHslColor.class)
    public JAXBElement<CTComplementTransform> createCTHslColorComp(CTComplementTransform value) {
        return new JAXBElement<>(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueOff", scope = CTHslColor.class)
    public JAXBElement<CTPercentage> createCTHslColorBlueOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueMod", scope = CTHslColor.class)
    public JAXBElement<CTPositivePercentage> createCTHslColorHueMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gray", scope = CTHslColor.class)
    public JAXBElement<CTGrayscaleTransform> createCTHslColorGray(CTGrayscaleTransform value) {
        return new JAXBElement<>(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTHslColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lum", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorLum(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLum_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alpha", scope = CTPresetColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTPresetColorAlpha(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gamma", scope = CTPresetColor.class)
    public JAXBElement<CTGammaTransform> createCTPresetColorGamma(CTGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "invGamma", scope = CTPresetColor.class)
    public JAXBElement<CTInverseGammaTransform> createCTPresetColorInvGamma(CTInverseGammaTransform value) {
        return new JAXBElement<>(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redOff", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorRedOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaMod", scope = CTPresetColor.class)
    public JAXBElement<CTPositivePercentage> createCTPresetColorAlphaMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "alphaOff", scope = CTPresetColor.class)
    public JAXBElement<CTFixedPercentage> createCTPresetColorAlphaOff(CTFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenOff", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorGreenOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hue", scope = CTPresetColor.class)
    public JAXBElement<CTPositiveFixedAngle> createCTPresetColorHue(CTPositiveFixedAngle value) {
        return new JAXBElement<>(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "redMod", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorRedMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satOff", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorSatOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "greenMod", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorGreenMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blue", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorBlue(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "sat", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorSat(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSat_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "red", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorRed(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorRed_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "satMod", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorSatMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueMod", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorBlueMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueOff", scope = CTPresetColor.class)
    public JAXBElement<CTAngle> createCTPresetColorHueOff(CTAngle value) {
        return new JAXBElement<>(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "shade", scope = CTPresetColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTPresetColorShade(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumMod", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorLumMod(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "inv", scope = CTPresetColor.class)
    public JAXBElement<CTInverseTransform> createCTPresetColorInv(CTInverseTransform value) {
        return new JAXBElement<>(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "lumOff", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorLumOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "tint", scope = CTPresetColor.class)
    public JAXBElement<CTPositiveFixedPercentage> createCTPresetColorTint(CTPositiveFixedPercentage value) {
        return new JAXBElement<>(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "green", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorGreen(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "comp", scope = CTPresetColor.class)
    public JAXBElement<CTComplementTransform> createCTPresetColorComp(CTComplementTransform value) {
        return new JAXBElement<>(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "blueOff", scope = CTPresetColor.class)
    public JAXBElement<CTPercentage> createCTPresetColorBlueOff(CTPercentage value) {
        return new JAXBElement<>(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "hueMod", scope = CTPresetColor.class)
    public JAXBElement<CTPositivePercentage> createCTPresetColorHueMod(CTPositivePercentage value) {
        return new JAXBElement<>(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTPresetColor.class, value);
    }

    @XmlElementDecl(namespace = XSSFRelation.NS_DRAWINGML, name = "gray", scope = CTPresetColor.class)
    public JAXBElement<CTGrayscaleTransform> createCTPresetColorGray(CTGrayscaleTransform value) {
        return new JAXBElement<>(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTPresetColor.class, value);
    }
}
