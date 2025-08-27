package com.itextpdf.kernel.colors;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.colorspace.PdfCieBasedCs;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import java.io.Serializable;
import java.util.Arrays;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/Color.class */
public class Color implements Serializable {
    private static final long serialVersionUID = -6639782922289701126L;
    protected PdfColorSpace colorSpace;
    protected float[] colorValue;

    protected Color(PdfColorSpace colorSpace, float[] colorValue) {
        this.colorSpace = colorSpace;
        if (colorValue == null) {
            this.colorValue = new float[colorSpace.getNumberOfComponents()];
        } else {
            this.colorValue = colorValue;
        }
    }

    public static Color makeColor(PdfColorSpace colorSpace) {
        return makeColor(colorSpace, null);
    }

    public static Color makeColor(PdfColorSpace colorSpace, float[] colorValue) {
        Color c = null;
        boolean unknownColorSpace = false;
        if (colorSpace instanceof PdfDeviceCs) {
            if (colorSpace instanceof PdfDeviceCs.Gray) {
                c = colorValue != null ? new DeviceGray(colorValue[0]) : new DeviceGray();
            } else if (colorSpace instanceof PdfDeviceCs.Rgb) {
                c = colorValue != null ? new DeviceRgb(colorValue[0], colorValue[1], colorValue[2]) : new DeviceRgb();
            } else if (colorSpace instanceof PdfDeviceCs.Cmyk) {
                c = colorValue != null ? new DeviceCmyk(colorValue[0], colorValue[1], colorValue[2], colorValue[3]) : new DeviceCmyk();
            } else {
                unknownColorSpace = true;
            }
        } else if (colorSpace instanceof PdfCieBasedCs) {
            if (colorSpace instanceof PdfCieBasedCs.CalGray) {
                PdfCieBasedCs.CalGray calGray = (PdfCieBasedCs.CalGray) colorSpace;
                c = colorValue != null ? new CalGray(calGray, colorValue[0]) : new CalGray(calGray);
            } else if (colorSpace instanceof PdfCieBasedCs.CalRgb) {
                PdfCieBasedCs.CalRgb calRgb = (PdfCieBasedCs.CalRgb) colorSpace;
                c = colorValue != null ? new CalRgb(calRgb, colorValue) : new CalRgb(calRgb);
            } else if (colorSpace instanceof PdfCieBasedCs.IccBased) {
                PdfCieBasedCs.IccBased iccBased = (PdfCieBasedCs.IccBased) colorSpace;
                c = colorValue != null ? new IccBased(iccBased, colorValue) : new IccBased(iccBased);
            } else if (colorSpace instanceof PdfCieBasedCs.Lab) {
                PdfCieBasedCs.Lab lab = (PdfCieBasedCs.Lab) colorSpace;
                c = colorValue != null ? new Lab(lab, colorValue) : new Lab(lab);
            } else {
                unknownColorSpace = true;
            }
        } else if (colorSpace instanceof PdfSpecialCs) {
            if (colorSpace instanceof PdfSpecialCs.Separation) {
                PdfSpecialCs.Separation separation = (PdfSpecialCs.Separation) colorSpace;
                c = colorValue != null ? new Separation(separation, colorValue[0]) : new Separation(separation);
            } else if (colorSpace instanceof PdfSpecialCs.DeviceN) {
                PdfSpecialCs.DeviceN deviceN = (PdfSpecialCs.DeviceN) colorSpace;
                c = colorValue != null ? new DeviceN(deviceN, colorValue) : new DeviceN(deviceN);
            } else if (colorSpace instanceof PdfSpecialCs.Indexed) {
                c = colorValue != null ? new Indexed(colorSpace, (int) colorValue[0]) : new Indexed(colorSpace);
            } else {
                unknownColorSpace = true;
            }
        } else if (colorSpace instanceof PdfSpecialCs.Pattern) {
            c = new Color(colorSpace, colorValue);
        } else {
            unknownColorSpace = true;
        }
        if (unknownColorSpace) {
            throw new PdfException("Unknown color space.");
        }
        return c;
    }

    public static DeviceRgb convertCmykToRgb(DeviceCmyk cmykColor) {
        float cyanComp = 1.0f - cmykColor.getColorValue()[0];
        float magentaComp = 1.0f - cmykColor.getColorValue()[1];
        float yellowComp = 1.0f - cmykColor.getColorValue()[2];
        float blackComp = 1.0f - cmykColor.getColorValue()[3];
        float r = cyanComp * blackComp;
        float g = magentaComp * blackComp;
        float b = yellowComp * blackComp;
        return new DeviceRgb(r, g, b);
    }

    public static DeviceCmyk convertRgbToCmyk(DeviceRgb rgbColor) {
        float redComp = rgbColor.getColorValue()[0];
        float greenComp = rgbColor.getColorValue()[1];
        float blueComp = rgbColor.getColorValue()[2];
        float k = 1.0f - Math.max(Math.max(redComp, greenComp), blueComp);
        float c = ((1.0f - redComp) - k) / (1.0f - k);
        float m = ((1.0f - greenComp) - k) / (1.0f - k);
        float y = ((1.0f - blueComp) - k) / (1.0f - k);
        return new DeviceCmyk(c, m, y, k);
    }

    public int getNumberOfComponents() {
        return this.colorValue.length;
    }

    public PdfColorSpace getColorSpace() {
        return this.colorSpace;
    }

    public float[] getColorValue() {
        return this.colorValue;
    }

    public void setColorValue(float[] value) {
        if (this.colorValue.length != value.length) {
            throw new PdfException(PdfException.IncorrectNumberOfComponents, this);
        }
        this.colorValue = value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Color color = (Color) o;
        if (this.colorSpace == null ? color.colorSpace == null : this.colorSpace.getPdfObject().equals(color.colorSpace.getPdfObject())) {
            if (Arrays.equals(this.colorValue, color.colorValue)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = this.colorSpace != null ? this.colorSpace.hashCode() : 0;
        return (31 * result) + (this.colorValue != null ? Arrays.hashCode(this.colorValue) : 0);
    }
}
