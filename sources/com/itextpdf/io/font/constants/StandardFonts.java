package com.itextpdf.io.font.constants;

import java.util.HashSet;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/constants/StandardFonts.class */
public final class StandardFonts {
    private static final Set<String> BUILTIN_FONTS = new HashSet();
    public static final String COURIER = "Courier";
    public static final String COURIER_BOLD = "Courier-Bold";
    public static final String COURIER_OBLIQUE = "Courier-Oblique";
    public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";
    public static final String HELVETICA = "Helvetica";
    public static final String HELVETICA_BOLD = "Helvetica-Bold";
    public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";
    public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";
    public static final String SYMBOL = "Symbol";
    public static final String TIMES_ROMAN = "Times-Roman";
    public static final String TIMES_BOLD = "Times-Bold";
    public static final String TIMES_ITALIC = "Times-Italic";
    public static final String TIMES_BOLDITALIC = "Times-BoldItalic";
    public static final String ZAPFDINGBATS = "ZapfDingbats";

    private StandardFonts() {
    }

    static {
        BUILTIN_FONTS.add("Courier");
        BUILTIN_FONTS.add("Courier-Bold");
        BUILTIN_FONTS.add("Courier-BoldOblique");
        BUILTIN_FONTS.add("Courier-Oblique");
        BUILTIN_FONTS.add("Helvetica");
        BUILTIN_FONTS.add("Helvetica-Bold");
        BUILTIN_FONTS.add("Helvetica-BoldOblique");
        BUILTIN_FONTS.add("Helvetica-Oblique");
        BUILTIN_FONTS.add("Symbol");
        BUILTIN_FONTS.add("Times-Roman");
        BUILTIN_FONTS.add("Times-Bold");
        BUILTIN_FONTS.add("Times-BoldItalic");
        BUILTIN_FONTS.add("Times-Italic");
        BUILTIN_FONTS.add("ZapfDingbats");
    }

    public static boolean isStandardFont(String fontName) {
        return BUILTIN_FONTS.contains(fontName);
    }
}
