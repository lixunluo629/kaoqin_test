package org.apache.poi.common.usermodel.fonts;

/* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/fonts/FontFamily.class */
public enum FontFamily {
    FF_DONTCARE(0),
    FF_ROMAN(1),
    FF_SWISS(2),
    FF_MODERN(3),
    FF_SCRIPT(4),
    FF_DECORATIVE(5);

    private int nativeId;

    FontFamily(int nativeId) {
        this.nativeId = nativeId;
    }

    public int getFlag() {
        return this.nativeId;
    }

    public static FontFamily valueOf(int nativeId) {
        FontFamily[] arr$ = values();
        for (FontFamily ff : arr$) {
            if (ff.nativeId == nativeId) {
                return ff;
            }
        }
        return null;
    }

    public static FontFamily valueOfPitchFamily(byte pitchAndFamily) {
        return valueOf(pitchAndFamily >>> 4);
    }
}
