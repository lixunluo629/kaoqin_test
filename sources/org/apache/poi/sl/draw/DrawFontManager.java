package org.apache.poi.sl.draw;

import java.awt.Font;
import java.awt.Graphics2D;
import org.apache.poi.common.usermodel.fonts.FontInfo;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawFontManager.class */
public interface DrawFontManager {
    FontInfo getMappedFont(Graphics2D graphics2D, FontInfo fontInfo);

    FontInfo getFallbackFont(Graphics2D graphics2D, FontInfo fontInfo);

    String mapFontCharset(Graphics2D graphics2D, FontInfo fontInfo, String str);

    Font createAWTFont(Graphics2D graphics2D, FontInfo fontInfo, double d, boolean z, boolean z2);
}
