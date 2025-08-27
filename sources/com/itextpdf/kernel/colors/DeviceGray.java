package com.itextpdf.kernel.colors;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/DeviceGray.class */
public class DeviceGray extends Color {
    public static final DeviceGray WHITE = new DeviceGray(1.0f);
    public static final DeviceGray GRAY = new DeviceGray(0.5f);
    public static final DeviceGray BLACK = new DeviceGray();
    private static final long serialVersionUID = 8307729543359242834L;

    public DeviceGray(float value) {
        PdfDeviceCs.Gray gray = new PdfDeviceCs.Gray();
        float[] fArr = new float[1];
        fArr[0] = value > 1.0f ? 1.0f : value > 0.0f ? value : 0.0f;
        super(gray, fArr);
        if (value > 1.0f || value < 0.0f) {
            Logger LOGGER = LoggerFactory.getLogger((Class<?>) DeviceGray.class);
            LOGGER.warn(LogMessageConstant.COLORANT_INTENSITIES_INVALID);
        }
    }

    public DeviceGray() {
        this(0.0f);
    }

    public static DeviceGray makeLighter(DeviceGray grayColor) {
        float v = grayColor.getColorValue()[0];
        if (v == 0.0f) {
            return new DeviceGray(0.3f);
        }
        float multiplier = Math.min(1.0f, v + 0.33f) / v;
        return new DeviceGray(v * multiplier);
    }

    public static DeviceGray makeDarker(DeviceGray grayColor) {
        float v = grayColor.getColorValue()[0];
        float multiplier = Math.max(0.0f, (v - 0.33f) / v);
        return new DeviceGray(v * multiplier);
    }
}
