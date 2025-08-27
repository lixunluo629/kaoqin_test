package org.apache.poi.sl.draw;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;
import org.apache.poi.common.usermodel.fonts.FontInfo;
import org.apache.poi.sl.draw.Drawable;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawFontManagerDefault.class */
public class DrawFontManagerDefault implements DrawFontManager {
    @Override // org.apache.poi.sl.draw.DrawFontManager
    public FontInfo getMappedFont(Graphics2D graphics, FontInfo fontInfo) {
        return getFontWithFallback(graphics, Drawable.FONT_MAP, fontInfo);
    }

    @Override // org.apache.poi.sl.draw.DrawFontManager
    public FontInfo getFallbackFont(Graphics2D graphics, FontInfo fontInfo) {
        FontInfo fi = getFontWithFallback(graphics, Drawable.FONT_FALLBACK, fontInfo);
        if (fi == null) {
            fi = new DrawFontInfo("SansSerif");
        }
        return fi;
    }

    @Override // org.apache.poi.sl.draw.DrawFontManager
    public String mapFontCharset(Graphics2D graphics, FontInfo fontInfo, String text) {
        String attStr = text;
        if (fontInfo != null && "Wingdings".equalsIgnoreCase(fontInfo.getTypeface())) {
            boolean changed = false;
            char[] chrs = attStr.toCharArray();
            for (int i = 0; i < chrs.length; i++) {
                if ((' ' <= chrs[i] && chrs[i] <= 127) || (160 <= chrs[i] && chrs[i] <= 255)) {
                    int i2 = i;
                    chrs[i2] = (char) (chrs[i2] | 61440);
                    changed = true;
                }
            }
            if (changed) {
                attStr = new String(chrs);
            }
        }
        return attStr;
    }

    @Override // org.apache.poi.sl.draw.DrawFontManager
    public Font createAWTFont(Graphics2D graphics, FontInfo fontInfo, double fontSize, boolean bold, boolean italic) {
        int style = (bold ? 1 : 0) | (italic ? 2 : 0);
        Font font = new Font(fontInfo.getTypeface(), style, 12);
        if ("Dialog".equals(font.getFamily())) {
            font = new Font("SansSerif", style, 12);
        }
        return font.deriveFont((float) fontSize);
    }

    private FontInfo getFontWithFallback(Graphics2D graphics, Drawable.DrawableHint hint, FontInfo fontInfo) {
        Map<String, String> fontMap = (Map) graphics.getRenderingHint(hint);
        if (fontMap == null) {
            return fontInfo;
        }
        String f = fontInfo != null ? fontInfo.getTypeface() : null;
        String mappedTypeface = null;
        if (fontMap.containsKey(f)) {
            mappedTypeface = fontMap.get(f);
        } else if (fontMap.containsKey("*")) {
            mappedTypeface = fontMap.get("*");
        }
        return mappedTypeface != null ? new DrawFontInfo(mappedTypeface) : fontInfo;
    }
}
