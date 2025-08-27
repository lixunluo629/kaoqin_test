package com.itextpdf.layout.font;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.Type1Font;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontProvider.class */
public class FontProvider {
    private static final String DEFAULT_FONT_FAMILY = "Helvetica";
    private final FontSet fontSet;
    private final FontSelectorCache fontSelectorCache;
    protected final String defaultFontFamily;
    protected final Map<FontInfo, PdfFont> pdfFonts;

    public FontProvider(FontSet fontSet) {
        this(fontSet, "Helvetica");
    }

    public FontProvider() {
        this(new FontSet());
    }

    public FontProvider(String defaultFontFamily) {
        this(new FontSet(), defaultFontFamily);
    }

    public FontProvider(FontSet fontSet, String defaultFontFamily) {
        this.fontSet = fontSet != null ? fontSet : new FontSet();
        this.pdfFonts = new HashMap();
        this.fontSelectorCache = new FontSelectorCache(this.fontSet);
        this.defaultFontFamily = defaultFontFamily;
    }

    public boolean addFont(FontProgram fontProgram, String encoding, Range unicodeRange) {
        return this.fontSet.addFont(fontProgram, encoding, (String) null, unicodeRange);
    }

    public boolean addFont(FontProgram fontProgram, String encoding) {
        return addFont(fontProgram, encoding, (Range) null);
    }

    public boolean addFont(FontProgram fontProgram) {
        return addFont(fontProgram, getDefaultEncoding(fontProgram));
    }

    public boolean addFont(String fontPath, String encoding, Range unicodeRange) {
        return this.fontSet.addFont(fontPath, encoding, (String) null, unicodeRange);
    }

    public boolean addFont(String fontPath, String encoding) {
        return addFont(fontPath, encoding, (Range) null);
    }

    public boolean addFont(String fontPath) {
        return addFont(fontPath, (String) null);
    }

    public boolean addFont(byte[] fontData, String encoding, Range unicodeRange) {
        return this.fontSet.addFont(fontData, encoding, (String) null, unicodeRange);
    }

    public boolean addFont(byte[] fontData, String encoding) {
        return addFont(fontData, encoding, (Range) null);
    }

    public boolean addFont(byte[] fontData) {
        return addFont(fontData, (String) null);
    }

    public int addDirectory(String dir) {
        return this.fontSet.addDirectory(dir);
    }

    public int addSystemFonts() {
        int count = 0;
        String[] withSubDirs = {FileUtil.getFontsDir(), "/usr/share/X11/fonts", "/usr/X/lib/X11/fonts", "/usr/openwin/lib/X11/fonts", "/usr/share/fonts", "/usr/X11R6/lib/X11/fonts"};
        for (String directory : withSubDirs) {
            count += this.fontSet.addDirectory(directory, true);
        }
        String[] withoutSubDirs = {"/Library/Fonts", "/System/Library/Fonts"};
        for (String directory2 : withoutSubDirs) {
            count += this.fontSet.addDirectory(directory2, false);
        }
        return count;
    }

    public int addStandardPdfFonts() {
        addFont("Courier");
        addFont("Courier-Bold");
        addFont("Courier-BoldOblique");
        addFont("Courier-Oblique");
        addFont("Helvetica");
        addFont("Helvetica-Bold");
        addFont("Helvetica-BoldOblique");
        addFont("Helvetica-Oblique");
        addFont("Symbol");
        addFont("Times-Roman");
        addFont("Times-Bold");
        addFont("Times-BoldItalic");
        addFont("Times-Italic");
        addFont("ZapfDingbats");
        return 14;
    }

    public FontSet getFontSet() {
        return this.fontSet;
    }

    public String getDefaultFontFamily() {
        return this.defaultFontFamily;
    }

    public String getDefaultEncoding(FontProgram fontProgram) {
        if (fontProgram instanceof Type1Font) {
            return "Cp1252";
        }
        return PdfEncodings.IDENTITY_H;
    }

    public boolean getDefaultCacheFlag() {
        return true;
    }

    public boolean getDefaultEmbeddingFlag() {
        return true;
    }

    public FontSelectorStrategy getStrategy(String text, List<String> fontFamilies, FontCharacteristics fc, FontSet additonalFonts) {
        return new ComplexFontSelectorStrategy(text, getFontSelector(fontFamilies, fc, additonalFonts), this, additonalFonts);
    }

    public FontSelectorStrategy getStrategy(String text, List<String> fontFamilies, FontCharacteristics fc) {
        return getStrategy(text, fontFamilies, fc, null);
    }

    public FontSelectorStrategy getStrategy(String text, List<String> fontFamilies) {
        return getStrategy(text, fontFamilies, null);
    }

    public final FontSelector getFontSelector(List<String> fontFamilies, FontCharacteristics fc) {
        FontSelectorKey key = new FontSelectorKey(fontFamilies, fc);
        FontSelector fontSelector = this.fontSelectorCache.get(key);
        if (fontSelector == null) {
            fontSelector = createFontSelector(this.fontSet.getFonts(), fontFamilies, fc);
            this.fontSelectorCache.put(key, fontSelector);
        }
        return fontSelector;
    }

    public final FontSelector getFontSelector(List<String> fontFamilies, FontCharacteristics fc, FontSet tempFonts) {
        FontSelectorKey key = new FontSelectorKey(fontFamilies, fc);
        FontSelector fontSelector = this.fontSelectorCache.get(key, tempFonts);
        if (fontSelector == null) {
            fontSelector = createFontSelector(this.fontSet.getFonts(tempFonts), fontFamilies, fc);
            this.fontSelectorCache.put(key, fontSelector, tempFonts);
        }
        return fontSelector;
    }

    protected FontSelector createFontSelector(Collection<FontInfo> fonts, List<String> fontFamilies, FontCharacteristics fc) {
        List<String> fontFamiliesToBeProcessed = new ArrayList<>(fontFamilies);
        fontFamiliesToBeProcessed.add(this.defaultFontFamily);
        return new FontSelector(fonts, fontFamiliesToBeProcessed, fc);
    }

    public PdfFont getPdfFont(FontInfo fontInfo) {
        return getPdfFont(fontInfo, null);
    }

    public PdfFont getPdfFont(FontInfo fontInfo, FontSet tempFonts) {
        if (this.pdfFonts.containsKey(fontInfo)) {
            return this.pdfFonts.get(fontInfo);
        }
        FontProgram fontProgram = null;
        if (tempFonts != null) {
            fontProgram = tempFonts.getFontProgram(fontInfo);
        }
        if (fontProgram == null) {
            fontProgram = this.fontSet.getFontProgram(fontInfo);
        }
        if (fontProgram == null) {
            try {
                if (fontInfo.getFontData() != null) {
                    fontProgram = FontProgramFactory.createFont(fontInfo.getFontData(), getDefaultCacheFlag());
                } else {
                    fontProgram = FontProgramFactory.createFont(fontInfo.getFontName(), getDefaultCacheFlag());
                }
            } catch (IOException e) {
                throw new PdfException(PdfException.IoExceptionWhileCreatingFont, (Throwable) e);
            }
        }
        String encoding = fontInfo.getEncoding();
        if (encoding == null || encoding.length() == 0) {
            encoding = getDefaultEncoding(fontProgram);
        }
        PdfFont pdfFont = PdfFontFactory.createFont(fontProgram, encoding, getDefaultEmbeddingFlag());
        this.pdfFonts.put(fontInfo, pdfFont);
        return pdfFont;
    }

    public void reset() {
        this.pdfFonts.clear();
    }
}
