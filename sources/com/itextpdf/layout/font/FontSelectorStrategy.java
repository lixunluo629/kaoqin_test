package com.itextpdf.layout.font;

import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.kernel.font.PdfFont;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSelectorStrategy.class */
public abstract class FontSelectorStrategy {
    protected String text;
    protected int index = 0;
    protected final FontProvider provider;
    protected final FontSet tempFonts;

    public abstract PdfFont getCurrentFont();

    public abstract List<Glyph> nextGlyphs();

    protected FontSelectorStrategy(String text, FontProvider provider, FontSet tempFonts) {
        this.text = text;
        this.provider = provider;
        this.tempFonts = tempFonts;
    }

    public boolean endOfText() {
        return this.text == null || this.index >= this.text.length();
    }

    protected PdfFont getPdfFont(FontInfo fontInfo) {
        return this.provider.getPdfFont(fontInfo, this.tempFonts);
    }
}
