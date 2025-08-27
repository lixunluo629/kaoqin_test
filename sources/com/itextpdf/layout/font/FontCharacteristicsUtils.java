package com.itextpdf.layout.font;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontCharacteristicsUtils.class */
final class FontCharacteristicsUtils {
    FontCharacteristicsUtils() {
    }

    static short normalizeFontWeight(short fw) {
        short fw2 = (short) ((fw / 100) * 100);
        if (fw2 < 100) {
            return (short) 100;
        }
        if (fw2 > 900) {
            return (short) 900;
        }
        return fw2;
    }

    static short parseFontWeight(String fw) {
        String fw2;
        if (fw == null || fw.length() == 0) {
            return (short) -1;
        }
        fw2 = fw.trim().toLowerCase();
        switch (fw2) {
            case "bold":
                return (short) 700;
            case "normal":
                return (short) 400;
            default:
                try {
                    return normalizeFontWeight((short) Integer.parseInt(fw2));
                } catch (NumberFormatException e) {
                    return (short) -1;
                }
        }
    }
}
