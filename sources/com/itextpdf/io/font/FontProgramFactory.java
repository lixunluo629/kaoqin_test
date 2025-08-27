package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.woff2.FontCompressionException;
import com.itextpdf.io.font.woff2.Woff2Converter;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.IOException;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontProgramFactory.class */
public final class FontProgramFactory {
    private static boolean DEFAULT_CACHED = true;
    private static FontRegisterProvider fontRegisterProvider = new FontRegisterProvider();

    private FontProgramFactory() {
    }

    public static FontProgram createFont() throws IOException {
        return createFont("Helvetica");
    }

    public static FontProgram createFont(String fontProgram) throws IOException {
        return createFont(fontProgram, (byte[]) null, DEFAULT_CACHED);
    }

    public static FontProgram createFont(String fontProgram, boolean cached) throws IOException {
        return createFont(fontProgram, (byte[]) null, cached);
    }

    public static FontProgram createFont(byte[] fontProgram) throws IOException {
        return createFont((String) null, fontProgram, DEFAULT_CACHED);
    }

    public static FontProgram createFont(byte[] fontProgram, boolean cached) throws IOException {
        return createFont((String) null, fontProgram, cached);
    }

    private static FontProgram createFont(String name, byte[] fontProgram, boolean cached) throws NumberFormatException, IOException {
        byte[] fontProgram2;
        String baseName = FontProgram.trimFontStyle(name);
        boolean isBuiltinFonts14 = StandardFonts.isStandardFont(name);
        boolean isCidFont = !isBuiltinFonts14 && FontCache.isPredefinedCidFont(baseName);
        FontCacheKey fontKey = null;
        if (cached) {
            fontKey = createFontCacheKey(name, fontProgram);
            FontProgram fontFound = FontCache.getFont(fontKey);
            if (fontFound != null) {
                return fontFound;
            }
        }
        FontProgram fontBuilt = null;
        if (name == null) {
            if (fontProgram != null) {
                try {
                    if (WoffConverter.isWoffFont(fontProgram)) {
                        fontProgram = WoffConverter.convert(fontProgram);
                    } else if (Woff2Converter.isWoff2Font(fontProgram)) {
                        fontProgram = Woff2Converter.convert(fontProgram);
                    }
                    fontBuilt = new TrueTypeFont(fontProgram);
                } catch (Exception e) {
                }
                if (fontBuilt == null) {
                    try {
                        fontBuilt = new Type1Font(null, null, fontProgram, null);
                    } catch (Exception e2) {
                    }
                }
            }
        } else {
            String fontFileExtension = null;
            int extensionBeginIndex = baseName.lastIndexOf(46);
            if (extensionBeginIndex > 0) {
                fontFileExtension = baseName.substring(extensionBeginIndex).toLowerCase();
            }
            if (isBuiltinFonts14 || ".afm".equals(fontFileExtension) || ".pfm".equals(fontFileExtension)) {
                fontBuilt = new Type1Font(name, null, null, null);
            } else if (isCidFont) {
                fontBuilt = new CidFont(name, FontCache.getCompatibleCmaps(baseName));
            } else if (".ttf".equals(fontFileExtension) || ".otf".equals(fontFileExtension)) {
                fontBuilt = fontProgram != null ? new TrueTypeFont(fontProgram) : new TrueTypeFont(name);
            } else if (".woff".equals(fontFileExtension) || ".woff2".equals(fontFileExtension)) {
                if (fontProgram == null) {
                    fontProgram = readFontBytesFromPath(baseName);
                }
                if (".woff".equals(fontFileExtension)) {
                    try {
                        fontProgram2 = WoffConverter.convert(fontProgram);
                    } catch (IllegalArgumentException woffException) {
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidWoffFile, (Throwable) woffException);
                    }
                } else {
                    try {
                        fontProgram2 = Woff2Converter.convert(fontProgram);
                    } catch (FontCompressionException woff2Exception) {
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.InvalidWoff2File, (Throwable) woff2Exception);
                    }
                }
                fontBuilt = new TrueTypeFont(fontProgram2);
            } else {
                int ttcSplit = baseName.toLowerCase().indexOf(".ttc,");
                if (ttcSplit > 0) {
                    try {
                        String ttcName = baseName.substring(0, ttcSplit + 4);
                        int ttcIndex = Integer.parseInt(baseName.substring(ttcSplit + 5));
                        fontBuilt = new TrueTypeFont(ttcName, ttcIndex);
                    } catch (NumberFormatException nfe) {
                        throw new com.itextpdf.io.IOException(nfe.getMessage(), (Throwable) nfe);
                    }
                }
            }
        }
        if (fontBuilt != null) {
            return cached ? FontCache.saveFont(fontBuilt, fontKey) : fontBuilt;
        }
        if (name != null) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TypeOfFont1IsNotRecognized).setMessageParams(name);
        }
        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TypeOfFontIsNotRecognized);
    }

    public static FontProgram createType1Font(byte[] afm, byte[] pfb) throws IOException {
        return createType1Font(afm, pfb, DEFAULT_CACHED);
    }

    public static FontProgram createType1Font(byte[] afm, byte[] pfb, boolean cached) throws IOException {
        return createType1Font(null, null, afm, pfb, cached);
    }

    public static FontProgram createType1Font(String metricsPath, String binaryPath) throws IOException {
        return createType1Font(metricsPath, binaryPath, DEFAULT_CACHED);
    }

    public static FontProgram createType1Font(String metricsPath, String binaryPath, boolean cached) throws IOException {
        return createType1Font(metricsPath, binaryPath, null, null, cached);
    }

    public static FontProgram createFont(String ttc, int ttcIndex, boolean cached) throws IOException {
        FontProgram fontFound;
        FontCacheKey fontCacheKey = FontCacheKey.create(ttc, ttcIndex);
        if (cached && (fontFound = FontCache.getFont(fontCacheKey)) != null) {
            return fontFound;
        }
        FontProgram fontBuilt = new TrueTypeFont(ttc, ttcIndex);
        return cached ? FontCache.saveFont(fontBuilt, fontCacheKey) : fontBuilt;
    }

    public static FontProgram createFont(byte[] ttc, int ttcIndex, boolean cached) throws IOException {
        FontProgram fontFound;
        FontCacheKey fontKey = FontCacheKey.create(ttc, ttcIndex);
        if (cached && (fontFound = FontCache.getFont(fontKey)) != null) {
            return fontFound;
        }
        FontProgram fontBuilt = new TrueTypeFont(ttc, ttcIndex);
        return cached ? FontCache.saveFont(fontBuilt, fontKey) : fontBuilt;
    }

    public static FontProgram createRegisteredFont(String fontName, int style, boolean cached) throws IOException {
        return fontRegisterProvider.getFont(fontName, style, cached);
    }

    public static FontProgram createRegisteredFont(String fontName, int style) throws IOException {
        return fontRegisterProvider.getFont(fontName, style);
    }

    public static FontProgram createRegisteredFont(String fontName) throws IOException {
        return fontRegisterProvider.getFont(fontName, -1);
    }

    public static void registerFontFamily(String familyName, String fullName, String path) {
        fontRegisterProvider.registerFontFamily(familyName, fullName, path);
    }

    public static void registerFont(String path) {
        registerFont(path, null);
    }

    public static void registerFont(String path, String alias) {
        fontRegisterProvider.registerFont(path, alias);
    }

    public static int registerFontDirectory(String dir) {
        return fontRegisterProvider.registerFontDirectory(dir);
    }

    public static int registerSystemFontDirectories() {
        return fontRegisterProvider.registerSystemFontDirectories();
    }

    public static Set<String> getRegisteredFonts() {
        return fontRegisterProvider.getRegisteredFonts();
    }

    public static Set<String> getRegisteredFontFamilies() {
        return fontRegisterProvider.getRegisteredFontFamilies();
    }

    public static boolean isRegisteredFont(String fontName) {
        return fontRegisterProvider.isRegisteredFont(fontName);
    }

    private static FontProgram createType1Font(String metricsPath, String binaryPath, byte[] afm, byte[] pfb, boolean cached) throws IOException {
        FontCacheKey fontKey = null;
        if (cached) {
            fontKey = createFontCacheKey(metricsPath, afm);
            FontProgram fontProgram = FontCache.getFont(fontKey);
            if (fontProgram != null) {
                return fontProgram;
            }
        }
        FontProgram fontProgram2 = new Type1Font(metricsPath, binaryPath, afm, pfb);
        return cached ? FontCache.saveFont(fontProgram2, fontKey) : fontProgram2;
    }

    private static FontCacheKey createFontCacheKey(String name, byte[] fontProgram) {
        FontCacheKey key;
        if (name != null) {
            key = FontCacheKey.create(name);
        } else {
            key = FontCacheKey.create(fontProgram);
        }
        return key;
    }

    public static void clearRegisteredFonts() {
        fontRegisterProvider.clearRegisteredFonts();
    }

    public static void clearRegisteredFontFamilies() {
        fontRegisterProvider.clearRegisteredFontFamilies();
    }

    static byte[] readFontBytesFromPath(String path) throws IOException {
        RandomAccessFileOrArray raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(path));
        int bufLen = (int) raf.length();
        if (bufLen < raf.length()) {
            throw new com.itextpdf.io.IOException(MessageFormatUtil.format("Source data from \"{0}\" is bigger than byte array can hold.", path));
        }
        byte[] buf = new byte[bufLen];
        raf.readFully(buf);
        return buf;
    }
}
