package com.itextpdf.io.font.constants;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/constants/FontWeights.class */
public final class FontWeights {
    public static final int THIN = 100;
    public static final int EXTRA_LIGHT = 200;
    public static final int LIGHT = 300;
    public static final int NORMAL = 400;
    public static final int MEDIUM = 500;
    public static final int SEMI_BOLD = 600;
    public static final int BOLD = 700;
    public static final int EXTRA_BOLD = 800;
    public static final int BLACK = 900;

    private FontWeights() {
    }

    public static int fromType1FontWeight(String weight) {
        int fontWeight;
        fontWeight = 400;
        switch (weight.toLowerCase()) {
            case "ultralight":
                fontWeight = 100;
                break;
            case "thin":
            case "extralight":
                fontWeight = 200;
                break;
            case "light":
                fontWeight = 300;
                break;
            case "book":
            case "regular":
            case "normal":
                fontWeight = 400;
                break;
            case "medium":
                fontWeight = 500;
                break;
            case "demibold":
            case "semibold":
                fontWeight = 600;
                break;
            case "bold":
                fontWeight = 700;
                break;
            case "extrabold":
            case "ultrabold":
                fontWeight = 800;
                break;
            case "heavy":
            case "black":
            case "ultra":
            case "ultrablack":
                fontWeight = 900;
                break;
            case "fat":
            case "extrablack":
                fontWeight = 900;
                break;
        }
        return fontWeight;
    }

    public static int normalizeFontWeight(int fontWeight) {
        int fontWeight2 = (fontWeight / 100) * 100;
        if (fontWeight2 < 100) {
            return 100;
        }
        if (fontWeight2 > 900) {
            return 900;
        }
        return fontWeight2;
    }
}
