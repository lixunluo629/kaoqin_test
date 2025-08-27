package com.itextpdf.io.font.constants;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/constants/FontStretches.class */
public final class FontStretches {
    private static final int FWIDTH_ULTRA_CONDENSED = 1;
    private static final int FWIDTH_EXTRA_CONDENSED = 2;
    private static final int FWIDTH_CONDENSED = 3;
    private static final int FWIDTH_SEMI_CONDENSED = 4;
    private static final int FWIDTH_NORMAL = 5;
    private static final int FWIDTH_SEMI_EXPANDED = 6;
    private static final int FWIDTH_EXPANDED = 7;
    private static final int FWIDTH_EXTRA_EXPANDED = 8;
    private static final int FWIDTH_ULTRA_EXPANDED = 9;
    public static final String ULTRA_CONDENSED = "UltraCondensed";
    public static final String EXTRA_CONDENSED = "ExtraCondensed";
    public static final String CONDENSED = "Condensed";
    public static final String SEMI_CONDENSED = "SemiCondensed";
    public static final String NORMAL = "Normal";
    public static final String SEMI_EXPANDED = "SemiExpanded";
    public static final String EXPANDED = "Expanded";
    public static final String EXTRA_EXPANDED = "ExtraExpanded";
    public static final String ULTRA_EXPANDED = "UltraExpanded";

    private FontStretches() {
    }

    public static String fromOpenTypeWidthClass(int fontWidth) {
        String fontWidthValue = NORMAL;
        switch (fontWidth) {
            case 1:
                fontWidthValue = ULTRA_CONDENSED;
                break;
            case 2:
                fontWidthValue = EXTRA_CONDENSED;
                break;
            case 3:
                fontWidthValue = CONDENSED;
                break;
            case 4:
                fontWidthValue = SEMI_CONDENSED;
                break;
            case 5:
                fontWidthValue = NORMAL;
                break;
            case 6:
                fontWidthValue = SEMI_EXPANDED;
                break;
            case 7:
                fontWidthValue = EXPANDED;
                break;
            case 8:
                fontWidthValue = EXTRA_EXPANDED;
                break;
            case 9:
                fontWidthValue = ULTRA_EXPANDED;
                break;
        }
        return fontWidthValue;
    }
}
