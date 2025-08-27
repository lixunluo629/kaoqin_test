package com.itextpdf.kernel.pdf.annot.da;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfString;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/da/AnnotationDefaultAppearance.class */
public class AnnotationDefaultAppearance {
    private static final Map<StandardAnnotationFont, String> stdAnnotFontNames = new HashMap();
    private static final Map<ExtendedAnnotationFont, String> extAnnotFontNames = new HashMap();
    private String colorOperand = "0 g";
    private String rawFontName = "/Helv";
    private float fontSize = 0.0f;

    static {
        stdAnnotFontNames.put(StandardAnnotationFont.CourierBoldOblique, "/Courier-BoldOblique");
        stdAnnotFontNames.put(StandardAnnotationFont.CourierBold, "/Courier-Bold");
        stdAnnotFontNames.put(StandardAnnotationFont.CourierOblique, "/Courier-Oblique");
        stdAnnotFontNames.put(StandardAnnotationFont.Courier, "/Courier");
        stdAnnotFontNames.put(StandardAnnotationFont.HelveticaBoldOblique, "/Helvetica-BoldOblique");
        stdAnnotFontNames.put(StandardAnnotationFont.HelveticaBold, "/Helvetica-Bold");
        stdAnnotFontNames.put(StandardAnnotationFont.HelveticaOblique, "/Courier-Oblique");
        stdAnnotFontNames.put(StandardAnnotationFont.Helvetica, "/Helvetica");
        stdAnnotFontNames.put(StandardAnnotationFont.Symbol, "/Symbol");
        stdAnnotFontNames.put(StandardAnnotationFont.TimesBoldItalic, "/Times-BoldItalic");
        stdAnnotFontNames.put(StandardAnnotationFont.TimesBold, "/Times-Bold");
        stdAnnotFontNames.put(StandardAnnotationFont.TimesItalic, "/Times-Italic");
        stdAnnotFontNames.put(StandardAnnotationFont.TimesRoman, "/Times-Roman");
        stdAnnotFontNames.put(StandardAnnotationFont.ZapfDingbats, "/ZapfDingbats");
        extAnnotFontNames.put(ExtendedAnnotationFont.HYSMyeongJoMedium, "/HySm");
        extAnnotFontNames.put(ExtendedAnnotationFont.HYGoThicMedium, "/HyGo");
        extAnnotFontNames.put(ExtendedAnnotationFont.HeiseiKakuGoW5, "/KaGo");
        extAnnotFontNames.put(ExtendedAnnotationFont.HeiseiMinW3, "/KaMi");
        extAnnotFontNames.put(ExtendedAnnotationFont.MHeiMedium, "/MHei");
        extAnnotFontNames.put(ExtendedAnnotationFont.MSungLight, "/MSun");
        extAnnotFontNames.put(ExtendedAnnotationFont.STSongLight, "/STSo");
        extAnnotFontNames.put(ExtendedAnnotationFont.MSungStdLight, "/MSun");
        extAnnotFontNames.put(ExtendedAnnotationFont.STSongStdLight, "/STSo");
        extAnnotFontNames.put(ExtendedAnnotationFont.HYSMyeongJoStdMedium, "/HySm");
        extAnnotFontNames.put(ExtendedAnnotationFont.KozMinProRegular, "/KaMi");
    }

    public AnnotationDefaultAppearance() {
        setFont(StandardAnnotationFont.Helvetica);
        setFontSize(12.0f);
    }

    public AnnotationDefaultAppearance setFont(StandardAnnotationFont font) {
        setRawFontName(stdAnnotFontNames.get(font));
        return this;
    }

    public AnnotationDefaultAppearance setFont(ExtendedAnnotationFont font) {
        setRawFontName(extAnnotFontNames.get(font));
        return this;
    }

    public AnnotationDefaultAppearance setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public AnnotationDefaultAppearance setColor(DeviceRgb rgbColor) {
        setColorOperand(rgbColor.getColorValue(), "rg");
        return this;
    }

    public AnnotationDefaultAppearance setColor(DeviceCmyk cmykColor) {
        setColorOperand(cmykColor.getColorValue(), "k");
        return this;
    }

    public AnnotationDefaultAppearance setColor(DeviceGray grayColor) {
        setColorOperand(grayColor.getColorValue(), "g");
        return this;
    }

    public PdfString toPdfString() {
        return new PdfString(MessageFormatUtil.format("{0} {1} Tf {2}", this.rawFontName, Float.valueOf(this.fontSize), this.colorOperand));
    }

    private void setColorOperand(float[] colorValues, String operand) {
        StringBuilder builder = new StringBuilder();
        for (float value : colorValues) {
            builder.append(MessageFormatUtil.format("{0} ", Float.valueOf(value)));
        }
        builder.append(operand);
        this.colorOperand = builder.toString();
    }

    private void setRawFontName(String rawFontName) {
        if (rawFontName == null) {
            throw new IllegalArgumentException("Passed raw font name can not be null");
        }
        this.rawFontName = rawFontName;
    }
}
