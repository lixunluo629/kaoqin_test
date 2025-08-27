package com.itextpdf.kernel.colors;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/DeviceRgb.class */
public class DeviceRgb extends Color {
    public static final Color BLACK = new DeviceRgb(0, 0, 0);
    public static final Color WHITE = new DeviceRgb(255, 255, 255);
    public static final Color RED = new DeviceRgb(255, 0, 0);
    public static final Color GREEN = new DeviceRgb(0, 255, 0);
    public static final Color BLUE = new DeviceRgb(0, 0, 255);
    private static final long serialVersionUID = 7172400358137528030L;

    public DeviceRgb(int r, int g, int b) {
        this(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public DeviceRgb(float r, float g, float b) {
        PdfDeviceCs.Rgb rgb = new PdfDeviceCs.Rgb();
        float[] fArr = new float[3];
        fArr[0] = r > 1.0f ? 1.0f : r > 0.0f ? r : 0.0f;
        fArr[1] = g > 1.0f ? 1.0f : g > 0.0f ? g : 0.0f;
        fArr[2] = b > 1.0f ? 1.0f : b > 0.0f ? b : 0.0f;
        super(rgb, fArr);
        if (r > 1.0f || r < 0.0f || g > 1.0f || g < 0.0f || b > 1.0f || b < 0.0f) {
            Logger LOGGER = LoggerFactory.getLogger((Class<?>) DeviceRgb.class);
            LOGGER.warn(LogMessageConstant.COLORANT_INTENSITIES_INVALID);
        }
    }

    public DeviceRgb(java.awt.Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue());
        if (color.getAlpha() != 255) {
            Logger LOGGER = LoggerFactory.getLogger((Class<?>) DeviceRgb.class);
            LOGGER.warn(MessageFormatUtil.format(LogMessageConstant.COLOR_ALPHA_CHANNEL_IS_IGNORED, Integer.valueOf(color.getAlpha())));
        }
    }

    public DeviceRgb() {
        this(0.0f, 0.0f, 0.0f);
    }

    public static DeviceRgb makeLighter(DeviceRgb rgbColor) {
        float r = rgbColor.getColorValue()[0];
        float g = rgbColor.getColorValue()[1];
        float b = rgbColor.getColorValue()[2];
        float v = Math.max(r, Math.max(g, b));
        if (v == 0.0f) {
            return new DeviceRgb(84, 84, 84);
        }
        float multiplier = Math.min(1.0f, v + 0.33f) / v;
        return new DeviceRgb(multiplier * r, multiplier * g, multiplier * b);
    }

    public static DeviceRgb makeDarker(DeviceRgb rgbColor) {
        float r = rgbColor.getColorValue()[0];
        float g = rgbColor.getColorValue()[1];
        float b = rgbColor.getColorValue()[2];
        float v = Math.max(r, Math.max(g, b));
        float multiplier = Math.max(0.0f, (v - 0.33f) / v);
        return new DeviceRgb(multiplier * r, multiplier * g, multiplier * b);
    }
}
