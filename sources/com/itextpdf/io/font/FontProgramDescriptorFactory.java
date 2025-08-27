package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.woff2.Woff2Converter;
import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontProgramDescriptorFactory.class */
public final class FontProgramDescriptorFactory {
    private static boolean FETCH_CACHED_FIRST = true;

    public static FontProgramDescriptor fetchDescriptor(String fontName) {
        FontProgramDescriptor fontDescriptor;
        byte[] fontProgram;
        FontProgramDescriptor fontDescriptor2;
        if (fontName == null || fontName.length() == 0) {
            return null;
        }
        String baseName = FontProgram.trimFontStyle(fontName);
        boolean isBuiltinFonts14 = StandardFonts.isStandardFont(fontName);
        boolean isCidFont = !isBuiltinFonts14 && FontCache.isPredefinedCidFont(baseName);
        if (FETCH_CACHED_FIRST && (fontDescriptor2 = fetchCachedDescriptor(fontName, null)) != null) {
            return fontDescriptor2;
        }
        try {
            String fontNameLowerCase = baseName.toLowerCase();
            if (isBuiltinFonts14 || fontNameLowerCase.endsWith(".afm") || fontNameLowerCase.endsWith(".pfm")) {
                fontDescriptor = fetchType1FontDescriptor(fontName, null);
            } else if (isCidFont) {
                fontDescriptor = fetchCidFontDescriptor(fontName);
            } else if (fontNameLowerCase.endsWith(".ttf") || fontNameLowerCase.endsWith(".otf")) {
                fontDescriptor = fetchTrueTypeFontDescriptor(fontName);
            } else if (fontNameLowerCase.endsWith(".woff") || fontNameLowerCase.endsWith(".woff2")) {
                if (fontNameLowerCase.endsWith(".woff")) {
                    fontProgram = WoffConverter.convert(FontProgramFactory.readFontBytesFromPath(baseName));
                } else {
                    fontProgram = Woff2Converter.convert(FontProgramFactory.readFontBytesFromPath(baseName));
                }
                fontDescriptor = fetchTrueTypeFontDescriptor(fontProgram);
            } else {
                fontDescriptor = fetchTTCDescriptor(baseName);
            }
        } catch (Exception e) {
            fontDescriptor = null;
        }
        return fontDescriptor;
    }

    public static FontProgramDescriptor fetchDescriptor(byte[] fontProgram) {
        if (fontProgram == null || fontProgram.length == 0) {
            return null;
        }
        FontProgramDescriptor fontDescriptor = null;
        if (FETCH_CACHED_FIRST) {
            fontDescriptor = fetchCachedDescriptor(null, fontProgram);
            if (fontDescriptor != null) {
                return fontDescriptor;
            }
        }
        try {
            fontDescriptor = fetchTrueTypeFontDescriptor(fontProgram);
        } catch (Exception e) {
        }
        if (fontDescriptor == null) {
            try {
                fontDescriptor = fetchType1FontDescriptor(null, fontProgram);
            } catch (Exception e2) {
            }
        }
        return fontDescriptor;
    }

    public static FontProgramDescriptor fetchDescriptor(FontProgram fontProgram) {
        return fetchDescriptorFromFontProgram(fontProgram);
    }

    private static FontProgramDescriptor fetchCachedDescriptor(String fontName, byte[] fontProgram) {
        FontCacheKey key;
        if (fontName != null) {
            key = FontCacheKey.create(fontName);
        } else {
            key = FontCacheKey.create(fontProgram);
        }
        FontProgram fontFound = FontCache.getFont(key);
        if (fontFound != null) {
            return fetchDescriptorFromFontProgram(fontFound);
        }
        return null;
    }

    private static FontProgramDescriptor fetchTTCDescriptor(String baseName) throws NumberFormatException, IOException {
        int ttcSplit = baseName.toLowerCase().indexOf(".ttc,");
        if (ttcSplit > 0) {
            try {
                String ttcName = baseName.substring(0, ttcSplit + 4);
                int ttcIndex = Integer.parseInt(baseName.substring(ttcSplit + 5));
                OpenTypeParser parser = new OpenTypeParser(ttcName, ttcIndex);
                FontProgramDescriptor descriptor = fetchOpenTypeFontDescriptor(parser);
                parser.close();
                return descriptor;
            } catch (NumberFormatException nfe) {
                throw new com.itextpdf.io.IOException(nfe.getMessage(), (Throwable) nfe);
            }
        }
        return null;
    }

    private static FontProgramDescriptor fetchTrueTypeFontDescriptor(String fontName) throws IOException {
        OpenTypeParser parser = new OpenTypeParser(fontName);
        Throwable th = null;
        try {
            FontProgramDescriptor fontProgramDescriptorFetchOpenTypeFontDescriptor = fetchOpenTypeFontDescriptor(parser);
            if (parser != null) {
                if (0 != 0) {
                    try {
                        parser.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    parser.close();
                }
            }
            return fontProgramDescriptorFetchOpenTypeFontDescriptor;
        } catch (Throwable th3) {
            if (parser != null) {
                if (0 != 0) {
                    try {
                        parser.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    parser.close();
                }
            }
            throw th3;
        }
    }

    private static FontProgramDescriptor fetchTrueTypeFontDescriptor(byte[] fontProgram) throws IOException {
        OpenTypeParser parser = new OpenTypeParser(fontProgram);
        Throwable th = null;
        try {
            FontProgramDescriptor fontProgramDescriptorFetchOpenTypeFontDescriptor = fetchOpenTypeFontDescriptor(parser);
            if (parser != null) {
                if (0 != 0) {
                    try {
                        parser.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    parser.close();
                }
            }
            return fontProgramDescriptorFetchOpenTypeFontDescriptor;
        } catch (Throwable th3) {
            if (parser != null) {
                if (0 != 0) {
                    try {
                        parser.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    parser.close();
                }
            }
            throw th3;
        }
    }

    private static FontProgramDescriptor fetchOpenTypeFontDescriptor(OpenTypeParser fontParser) throws IOException {
        fontParser.loadTables(false);
        return new FontProgramDescriptor(fontParser.getFontNames(), fontParser.getPostTable().italicAngle, fontParser.getPostTable().isFixedPitch);
    }

    private static FontProgramDescriptor fetchType1FontDescriptor(String fontName, byte[] afm) throws IOException {
        Type1Font fp = new Type1Font(fontName, null, afm, null);
        return new FontProgramDescriptor(fp.getFontNames(), fp.getFontMetrics());
    }

    private static FontProgramDescriptor fetchCidFontDescriptor(String fontName) {
        CidFont font = new CidFont(fontName, null);
        return new FontProgramDescriptor(font.getFontNames(), font.getFontMetrics());
    }

    private static FontProgramDescriptor fetchDescriptorFromFontProgram(FontProgram fontProgram) {
        return new FontProgramDescriptor(fontProgram.getFontNames(), fontProgram.getFontMetrics());
    }
}
