package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/Units.class */
public class Units {
    public static final int EMU_PER_PIXEL = 9525;
    public static final int EMU_PER_POINT = 12700;
    public static final int EMU_PER_CENTIMETER = 360000;
    public static final int MASTER_DPI = 576;
    public static final int PIXEL_DPI = 96;
    public static final int POINT_DPI = 72;
    public static final float DEFAULT_CHARACTER_WIDTH = 7.0017f;
    public static final int EMU_PER_CHARACTER = 66691;

    public static int toEMU(double points) {
        return (int) Math.rint(12700.0d * points);
    }

    public static int pixelToEMU(int pixels) {
        return pixels * 9525;
    }

    public static double toPoints(long emu) {
        return emu / 12700.0d;
    }

    public static double fixedPointToDouble(int fixedPoint) {
        int i = fixedPoint >> 16;
        int f = fixedPoint & 65535;
        return i + (f / 65536.0d);
    }

    public static int doubleToFixedPoint(double floatPoint) {
        double fractionalPart = floatPoint % 1.0d;
        double integralPart = floatPoint - fractionalPart;
        int i = (int) Math.floor(integralPart);
        int f = (int) Math.rint(fractionalPart * 65536.0d);
        return (i << 16) | (f & 65535);
    }

    public static double masterToPoints(int masterDPI) {
        double points = masterDPI;
        return (points * 72.0d) / 576.0d;
    }

    public static int pointsToMaster(double points) {
        return (int) Math.rint((points * 576.0d) / 72.0d);
    }

    public static int pointsToPixel(double points) {
        return (int) Math.rint((points * 96.0d) / 72.0d);
    }

    public static double pixelToPoints(int pixel) {
        double points = pixel;
        return (points * 72.0d) / 96.0d;
    }

    public static int charactersToEMU(double characters) {
        return ((int) characters) * EMU_PER_CHARACTER;
    }

    public static int columnWidthToEMU(int columnWidth) {
        return charactersToEMU(columnWidth / 256.0d);
    }

    public static int TwipsToEMU(short twips) {
        return (int) ((twips / 20.0d) * 12700.0d);
    }
}
