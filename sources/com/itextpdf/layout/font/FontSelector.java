package com.itextpdf.layout.font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSelector.class */
public class FontSelector {
    protected List<FontInfo> fonts;
    private static final int EXPECTED_FONT_IS_BOLD_AWARD = 5;
    private static final int EXPECTED_FONT_IS_NOT_BOLD_AWARD = 3;
    private static final int EXPECTED_FONT_IS_ITALIC_AWARD = 5;
    private static final int EXPECTED_FONT_IS_NOT_ITALIC_AWARD = 3;
    private static final int EXPECTED_FONT_IS_MONOSPACED_AWARD = 5;
    private static final int EXPECTED_FONT_IS_NOT_MONOSPACED_AWARD = 1;
    private static final int FONT_FAMILY_EQUALS_AWARD = 13;

    public FontSelector(Collection<FontInfo> allFonts, List<String> fontFamilies, FontCharacteristics fc) {
        this.fonts = new ArrayList(allFonts);
        Collections.sort(this.fonts, getComparator(fontFamilies, fc));
    }

    public final FontInfo bestMatch() {
        return this.fonts.get(0);
    }

    public final Iterable<FontInfo> getFonts() {
        return this.fonts;
    }

    protected Comparator<FontInfo> getComparator(List<String> fontFamilies, FontCharacteristics fc) {
        return new PdfFontComparator(fontFamilies, fc);
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSelector$PdfFontComparator.class */
    private static class PdfFontComparator implements Comparator<FontInfo> {
        List<String> fontFamilies = new ArrayList();
        List<FontCharacteristics> fontStyles = new ArrayList();

        PdfFontComparator(List<String> fontFamilies, FontCharacteristics fc) {
            if (fontFamilies != null && fontFamilies.size() > 0) {
                for (String fontFamily : fontFamilies) {
                    String lowercaseFontFamily = fontFamily.toLowerCase();
                    this.fontFamilies.add(lowercaseFontFamily);
                    this.fontStyles.add(parseFontStyle(lowercaseFontFamily, fc));
                }
                return;
            }
            this.fontStyles.add(fc);
        }

        @Override // java.util.Comparator
        public int compare(FontInfo o1, FontInfo o2) {
            int res = 0;
            int i = 0;
            while (i < this.fontFamilies.size() && res == 0) {
                FontCharacteristics fc = this.fontStyles.get(i);
                String fontFamily = this.fontFamilies.get(i);
                if ("monospace".equalsIgnoreCase(fontFamily)) {
                    fc.setMonospaceFlag(true);
                }
                boolean isLastFontFamilyToBeProcessed = i == this.fontFamilies.size() - 1;
                res = characteristicsSimilarity(fontFamily, fc, o2, isLastFontFamilyToBeProcessed) - characteristicsSimilarity(fontFamily, fc, o1, isLastFontFamilyToBeProcessed);
                i++;
            }
            return res;
        }

        private static FontCharacteristics parseFontStyle(String fontFamily, FontCharacteristics fc) {
            if (fc == null) {
                fc = new FontCharacteristics();
            }
            if (fc.isUndefined()) {
                if (fontFamily.contains("bold")) {
                    fc.setBoldFlag(true);
                }
                if (fontFamily.contains("italic") || fontFamily.contains("oblique")) {
                    fc.setItalicFlag(true);
                }
            }
            return fc;
        }

        private static int characteristicsSimilarity(String fontFamily, FontCharacteristics fc, FontInfo fontInfo, boolean isLastFontFamilyToBeProcessed) {
            boolean isFontBold = fontInfo.getDescriptor().isBold() || fontInfo.getDescriptor().getFontWeight() > 500;
            boolean isFontItalic = fontInfo.getDescriptor().isItalic() || fontInfo.getDescriptor().getItalicAngle() < 0.0f;
            boolean isFontMonospace = fontInfo.getDescriptor().isMonospace();
            int score = 0;
            boolean fontFamilySetByCharacteristics = false;
            if (fc.isMonospace()) {
                fontFamilySetByCharacteristics = true;
                if (isFontMonospace) {
                    score = 0 + 5;
                } else {
                    score = 0 - 5;
                }
            } else if (isFontMonospace) {
                score = 0 - 1;
            }
            if (!fontFamilySetByCharacteristics) {
                if (!"".equals(fontFamily) && ((null == fontInfo.getAlias() && null != fontInfo.getDescriptor().getFamilyNameLowerCase() && fontInfo.getDescriptor().getFamilyNameLowerCase().equals(fontFamily)) || (null != fontInfo.getAlias() && fontInfo.getAlias().toLowerCase().equals(fontFamily)))) {
                    score += 13;
                } else if (!isLastFontFamilyToBeProcessed) {
                    return score;
                }
            }
            if (fc.isBold()) {
                if (isFontBold) {
                    score += 5;
                } else {
                    score -= 5;
                }
            } else if (isFontBold) {
                score -= 3;
            }
            if (fc.isItalic()) {
                if (isFontItalic) {
                    score += 5;
                } else {
                    score -= 5;
                }
            } else if (isFontItalic) {
                score -= 3;
            }
            return score;
        }
    }
}
