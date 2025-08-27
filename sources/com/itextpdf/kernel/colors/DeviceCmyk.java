package com.itextpdf.kernel.colors;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/DeviceCmyk.class */
public class DeviceCmyk extends Color {
    public static final DeviceCmyk CYAN = new DeviceCmyk(100, 0, 0, 0);
    public static final DeviceCmyk MAGENTA = new DeviceCmyk(0, 100, 0, 0);
    public static final DeviceCmyk YELLOW = new DeviceCmyk(0, 0, 100, 0);
    public static final DeviceCmyk BLACK = new DeviceCmyk(0, 0, 0, 100);
    private static final long serialVersionUID = 5466518014595706050L;

    public DeviceCmyk() {
        this(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public DeviceCmyk(int c, int m, int y, int k) {
        this(c / 100.0f, m / 100.0f, y / 100.0f, k / 100.0f);
    }

    public DeviceCmyk(float c, float m, float y, float k) {
        PdfDeviceCs.Cmyk cmyk = new PdfDeviceCs.Cmyk();
        float[] fArr = new float[4];
        fArr[0] = c > 1.0f ? 1.0f : c > 0.0f ? c : 0.0f;
        fArr[1] = m > 1.0f ? 1.0f : m > 0.0f ? m : 0.0f;
        fArr[2] = y > 1.0f ? 1.0f : y > 0.0f ? y : 0.0f;
        fArr[3] = k > 1.0f ? 1.0f : k > 0.0f ? k : 0.0f;
        super(cmyk, fArr);
        if (c > 1.0f || c < 0.0f || m > 1.0f || m < 0.0f || y > 1.0f || y < 0.0f || k > 1.0f || k < 0.0f) {
            Logger LOGGER = LoggerFactory.getLogger((Class<?>) DeviceCmyk.class);
            LOGGER.warn(LogMessageConstant.COLORANT_INTENSITIES_INVALID);
        }
    }

    public static DeviceCmyk makeLighter(DeviceCmyk cmykColor) {
        DeviceRgb rgbEquivalent = convertCmykToRgb(cmykColor);
        DeviceRgb lighterRgb = DeviceRgb.makeLighter(rgbEquivalent);
        return convertRgbToCmyk(lighterRgb);
    }

    public static DeviceCmyk makeDarker(DeviceCmyk cmykColor) {
        DeviceRgb rgbEquivalent = convertCmykToRgb(cmykColor);
        DeviceRgb darkerRgb = DeviceRgb.makeDarker(rgbEquivalent);
        return convertRgbToCmyk(darkerRgb);
    }
}
