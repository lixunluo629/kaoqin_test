package com.itextpdf.io.font;

import java.util.HashSet;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontProgramDescriptor.class */
public class FontProgramDescriptor {
    private final String fontName;
    private final String fullNameLowerCase;
    private final String fontNameLowerCase;
    private final String familyNameLowerCase;
    private final String style;
    private final int macStyle;
    private final int weight;
    private final float italicAngle;
    private final boolean isMonospace;
    private final Set<String> fullNamesAllLangs;
    private final Set<String> fullNamesEnglishOpenType;
    private final String familyNameEnglishOpenType;
    private static final String[] TT_FAMILY_ORDER = {"3", "1", "1033", "3", "0", "1033", "1", "0", "0", "0", "3", "0"};

    FontProgramDescriptor(FontNames fontNames, float italicAngle, boolean isMonospace) {
        this.fontName = fontNames.getFontName();
        this.fontNameLowerCase = this.fontName.toLowerCase();
        this.fullNameLowerCase = fontNames.getFullName()[0][3].toLowerCase();
        this.familyNameLowerCase = (fontNames.getFamilyName() == null || fontNames.getFamilyName()[0][3] == null) ? null : fontNames.getFamilyName()[0][3].toLowerCase();
        this.style = fontNames.getStyle();
        this.weight = fontNames.getFontWeight();
        this.macStyle = fontNames.getMacStyle();
        this.italicAngle = italicAngle;
        this.isMonospace = isMonospace;
        this.familyNameEnglishOpenType = extractFamilyNameEnglishOpenType(fontNames);
        this.fullNamesAllLangs = extractFullFontNames(fontNames);
        this.fullNamesEnglishOpenType = extractFullNamesEnglishOpenType(fontNames);
    }

    FontProgramDescriptor(FontNames fontNames, FontMetrics fontMetrics) {
        this(fontNames, fontMetrics.getItalicAngle(), fontMetrics.isFixedPitch());
    }

    public String getFontName() {
        return this.fontName;
    }

    public String getStyle() {
        return this.style;
    }

    public int getFontWeight() {
        return this.weight;
    }

    public float getItalicAngle() {
        return this.italicAngle;
    }

    public boolean isMonospace() {
        return this.isMonospace;
    }

    public boolean isBold() {
        return (this.macStyle & 1) != 0;
    }

    public boolean isItalic() {
        return (this.macStyle & 2) != 0;
    }

    public String getFullNameLowerCase() {
        return this.fullNameLowerCase;
    }

    public String getFontNameLowerCase() {
        return this.fontNameLowerCase;
    }

    public String getFamilyNameLowerCase() {
        return this.familyNameLowerCase;
    }

    public Set<String> getFullNameAllLangs() {
        return this.fullNamesAllLangs;
    }

    public Set<String> getFullNamesEnglishOpenType() {
        return this.fullNamesEnglishOpenType;
    }

    String getFamilyNameEnglishOpenType() {
        return this.familyNameEnglishOpenType;
    }

    private Set<String> extractFullFontNames(FontNames fontNames) {
        Set<String> uniqueFullNames = new HashSet<>();
        for (String[] fullName : fontNames.getFullName()) {
            uniqueFullNames.add(fullName[3].toLowerCase());
        }
        return uniqueFullNames;
    }

    private String extractFamilyNameEnglishOpenType(FontNames fontNames) {
        if (fontNames.getFamilyName() != null) {
            for (int k = 0; k < TT_FAMILY_ORDER.length; k += 3) {
                for (String[] name : fontNames.getFamilyName()) {
                    if (TT_FAMILY_ORDER[k].equals(name[0]) && TT_FAMILY_ORDER[k + 1].equals(name[1]) && TT_FAMILY_ORDER[k + 2].equals(name[2])) {
                        return name[3].toLowerCase();
                    }
                }
            }
            return null;
        }
        return null;
    }

    private Set<String> extractFullNamesEnglishOpenType(FontNames fontNames) {
        if (this.familyNameEnglishOpenType != null) {
            Set<String> uniqueTtfSuitableFullNames = new HashSet<>();
            String[][] names = fontNames.getFullName();
            for (String[] name : names) {
                int k = 0;
                while (true) {
                    if (k >= TT_FAMILY_ORDER.length) {
                        break;
                    }
                    if (!TT_FAMILY_ORDER[k].equals(name[0]) || !TT_FAMILY_ORDER[k + 1].equals(name[1]) || !TT_FAMILY_ORDER[k + 2].equals(name[2])) {
                        k += 3;
                    } else {
                        uniqueTtfSuitableFullNames.add(name[3]);
                        break;
                    }
                }
            }
            return uniqueTtfSuitableFullNames;
        }
        return new HashSet();
    }
}
