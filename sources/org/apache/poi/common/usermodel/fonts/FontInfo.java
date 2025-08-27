package org.apache.poi.common.usermodel.fonts;

/* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/fonts/FontInfo.class */
public interface FontInfo {
    Integer getIndex();

    void setIndex(int i);

    String getTypeface();

    void setTypeface(String str);

    FontCharset getCharset();

    void setCharset(FontCharset fontCharset);

    FontFamily getFamily();

    void setFamily(FontFamily fontFamily);

    FontPitch getPitch();

    void setPitch(FontPitch fontPitch);
}
