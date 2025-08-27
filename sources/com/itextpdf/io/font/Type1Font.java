package com.itextpdf.io.font;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.constants.FontWeights;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/Type1Font.class */
public class Type1Font extends FontProgram {
    private static final long serialVersionUID = -1078208220942939920L;
    private Type1Parser fontParser;
    private String characterSet;
    private Map<Long, Integer> kernPairs;
    private static final int[] PFB_TYPES = {1, 2, 1};
    private byte[] fontStreamBytes;
    private int[] fontStreamLengths;

    protected static Type1Font createStandardFont(String name) throws IOException {
        if (StandardFonts.isStandardFont(name)) {
            return new Type1Font(name, null, null, null);
        }
        throw new com.itextpdf.io.IOException("{0} is not a standard type1 font.").setMessageParams(name);
    }

    protected Type1Font() {
        this.kernPairs = new HashMap();
        this.fontNames = new FontNames();
    }

    protected Type1Font(String metricsPath, String binaryPath, byte[] afm, byte[] pfb) throws IOException, NumberFormatException {
        this();
        this.fontParser = new Type1Parser(metricsPath, binaryPath, afm, pfb);
        process();
    }

    protected Type1Font(String baseFont) {
        this();
        getFontNames().setFontName(baseFont);
    }

    public boolean isBuiltInFont() {
        return this.fontParser != null && this.fontParser.isBuiltInFont();
    }

    @Override // com.itextpdf.io.font.FontProgram
    public int getPdfFontFlags() {
        int flags = 0;
        if (this.fontMetrics.isFixedPitch()) {
            flags = 0 | 1;
        }
        int flags2 = flags | (isFontSpecific() ? 4 : 32);
        if (this.fontMetrics.getItalicAngle() < 0.0f) {
            flags2 |= 64;
        }
        if (this.fontNames.getFontName().contains("Caps") || this.fontNames.getFontName().endsWith("SC")) {
            flags2 |= 131072;
        }
        if (this.fontNames.isBold() || this.fontNames.getFontWeight() > 500) {
            flags2 |= 262144;
        }
        return flags2;
    }

    public String getCharacterSet() {
        return this.characterSet;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public boolean hasKernPairs() {
        return this.kernPairs.size() > 0;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public int getKerning(Glyph first, Glyph second) {
        if (first.hasValidUnicode() && second.hasValidUnicode()) {
            long record = (first.getUnicode() << 32) + second.getUnicode();
            if (this.kernPairs.containsKey(Long.valueOf(record))) {
                return this.kernPairs.get(Long.valueOf(record)).intValue();
            }
            return 0;
        }
        return 0;
    }

    public boolean setKerning(int first, int second, int kern) {
        long record = (first << 32) + second;
        this.kernPairs.put(Long.valueOf(record), Integer.valueOf(kern));
        return true;
    }

    public Glyph getGlyph(String name) {
        int unicode = AdobeGlyphList.nameToUnicode(name);
        if (unicode != -1) {
            return getGlyph(unicode);
        }
        return null;
    }

    public byte[] getFontStreamBytes() {
        if (this.fontParser.isBuiltInFont()) {
            return null;
        }
        if (this.fontStreamBytes != null) {
            return this.fontStreamBytes;
        }
        RandomAccessFileOrArray raf = null;
        try {
            try {
                RandomAccessFileOrArray raf2 = this.fontParser.getPostscriptBinary();
                int fileLength = (int) raf2.length();
                this.fontStreamBytes = new byte[fileLength - 18];
                this.fontStreamLengths = new int[3];
                int bytePtr = 0;
                for (int k = 0; k < 3; k++) {
                    if (raf2.read() != 128) {
                        Logger logger = LoggerFactory.getLogger((Class<?>) Type1Font.class);
                        logger.error(LogMessageConstant.START_MARKER_MISSING_IN_PFB_FILE);
                        if (raf2 != null) {
                            try {
                                raf2.close();
                            } catch (Exception e) {
                            }
                        }
                        return null;
                    }
                    if (raf2.read() != PFB_TYPES[k]) {
                        Logger logger2 = LoggerFactory.getLogger((Class<?>) Type1Font.class);
                        logger2.error("incorrect.segment.type.in.pfb.file");
                        if (raf2 != null) {
                            try {
                                raf2.close();
                            } catch (Exception e2) {
                            }
                        }
                        return null;
                    }
                    int size = raf2.read() + (raf2.read() << 8) + (raf2.read() << 16) + (raf2.read() << 24);
                    this.fontStreamLengths[k] = size;
                    while (size != 0) {
                        int got = raf2.read(this.fontStreamBytes, bytePtr, size);
                        if (got < 0) {
                            Logger logger3 = LoggerFactory.getLogger((Class<?>) Type1Font.class);
                            logger3.error("premature.end.in.pfb.file");
                            if (raf2 != null) {
                                try {
                                    raf2.close();
                                } catch (Exception e3) {
                                }
                            }
                            return null;
                        }
                        bytePtr += got;
                        size -= got;
                    }
                }
                byte[] bArr = this.fontStreamBytes;
                if (raf2 != null) {
                    try {
                        raf2.close();
                    } catch (Exception e4) {
                    }
                }
                return bArr;
            } catch (Exception e5) {
                Logger logger4 = LoggerFactory.getLogger((Class<?>) Type1Font.class);
                logger4.error("type1.font.file.exception");
                if (0 != 0) {
                    try {
                        raf.close();
                    } catch (Exception e6) {
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    raf.close();
                } catch (Exception e7) {
                }
            }
            throw th;
        }
    }

    public int[] getFontStreamLengths() {
        return this.fontStreamLengths;
    }

    @Override // com.itextpdf.io.font.FontProgram
    public boolean isBuiltWith(String fontProgram) {
        return Objects.equals(this.fontParser.getAfmPath(), fontProgram);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v127, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v131, types: [java.lang.String[], java.lang.String[][]] */
    protected void process() throws IOException, NumberFormatException {
        Glyph space;
        String line;
        RandomAccessFileOrArray raf = this.fontParser.getMetricsFile();
        boolean startKernPairs = false;
        while (!startKernPairs && (line = raf.readLine()) != null) {
            StringTokenizer tok = new StringTokenizer(line, " ,\n\r\t\f");
            if (tok.hasMoreTokens()) {
                switch (tok.nextToken()) {
                    case "FontName":
                        this.fontNames.setFontName(tok.nextToken("ÿ").substring(1));
                        break;
                    case "FullName":
                        String fullName = tok.nextToken("ÿ").substring(1);
                        this.fontNames.setFullName((String[][]) new String[]{new String[]{"", "", "", fullName}});
                        break;
                    case "FamilyName":
                        String familyName = tok.nextToken("ÿ").substring(1);
                        this.fontNames.setFamilyName((String[][]) new String[]{new String[]{"", "", "", familyName}});
                        break;
                    case "Weight":
                        this.fontNames.setFontWeight(FontWeights.fromType1FontWeight(tok.nextToken("ÿ").substring(1)));
                        break;
                    case "ItalicAngle":
                        this.fontMetrics.setItalicAngle(Float.parseFloat(tok.nextToken()));
                        break;
                    case "IsFixedPitch":
                        this.fontMetrics.setIsFixedPitch(tok.nextToken().equals("true"));
                        break;
                    case "CharacterSet":
                        this.characterSet = tok.nextToken("ÿ").substring(1);
                        break;
                    case "FontBBox":
                        int llx = (int) Float.parseFloat(tok.nextToken());
                        int lly = (int) Float.parseFloat(tok.nextToken());
                        int urx = (int) Float.parseFloat(tok.nextToken());
                        int ury = (int) Float.parseFloat(tok.nextToken());
                        this.fontMetrics.setBbox(llx, lly, urx, ury);
                        break;
                    case "UnderlinePosition":
                        this.fontMetrics.setUnderlinePosition((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "UnderlineThickness":
                        this.fontMetrics.setUnderlineThickness((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "EncodingScheme":
                        this.encodingScheme = tok.nextToken("ÿ").substring(1).trim();
                        break;
                    case "CapHeight":
                        this.fontMetrics.setCapHeight((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "XHeight":
                        this.fontMetrics.setXHeight((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "Ascender":
                        this.fontMetrics.setTypoAscender((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "Descender":
                        this.fontMetrics.setTypoDescender((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "StdHW":
                        this.fontMetrics.setStemH((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "StdVW":
                        this.fontMetrics.setStemV((int) Float.parseFloat(tok.nextToken()));
                        break;
                    case "StartCharMetrics":
                        startKernPairs = true;
                        break;
                }
            }
        }
        if (!startKernPairs) {
            String metricsPath = this.fontParser.getAfmPath();
            if (metricsPath == null) {
                throw new com.itextpdf.io.IOException("startcharmetrics is missing in the metrics file.");
            }
            throw new com.itextpdf.io.IOException("startcharmetrics is missing in {0}.").setMessageParams(metricsPath);
        }
        this.avgWidth = 0;
        int widthCount = 0;
        while (true) {
            String line2 = raf.readLine();
            if (line2 != null) {
                StringTokenizer tok2 = new StringTokenizer(line2);
                if (tok2.hasMoreTokens()) {
                    if (tok2.nextToken().equals("EndCharMetrics")) {
                        startKernPairs = false;
                    } else {
                        int C = -1;
                        int WX = 250;
                        String N = "";
                        int[] B = null;
                        StringTokenizer tok3 = new StringTokenizer(line2, ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                        while (tok3.hasMoreTokens()) {
                            StringTokenizer tokc = new StringTokenizer(tok3.nextToken());
                            if (tokc.hasMoreTokens()) {
                                switch (tokc.nextToken()) {
                                    case "C":
                                        C = Integer.parseInt(tokc.nextToken());
                                        break;
                                    case "WX":
                                        WX = (int) Float.parseFloat(tokc.nextToken());
                                        break;
                                    case "N":
                                        N = tokc.nextToken();
                                        break;
                                    case "B":
                                        B = new int[]{Integer.parseInt(tokc.nextToken()), Integer.parseInt(tokc.nextToken()), Integer.parseInt(tokc.nextToken()), Integer.parseInt(tokc.nextToken())};
                                        break;
                                }
                            }
                        }
                        int unicode = AdobeGlyphList.nameToUnicode(N);
                        Glyph glyph = new Glyph(C, WX, unicode, B);
                        if (C >= 0) {
                            this.codeToGlyph.put(Integer.valueOf(C), glyph);
                        }
                        if (unicode != -1) {
                            this.unicodeToGlyph.put(Integer.valueOf(unicode), glyph);
                        }
                        this.avgWidth += WX;
                        widthCount++;
                    }
                }
            }
        }
        if (widthCount != 0) {
            this.avgWidth /= widthCount;
        }
        if (startKernPairs) {
            String metricsPath2 = this.fontParser.getAfmPath();
            if (metricsPath2 == null) {
                throw new com.itextpdf.io.IOException("endcharmetrics is missing in the metrics file.");
            }
            throw new com.itextpdf.io.IOException("endcharmetrics is missing in {0}.").setMessageParams(metricsPath2);
        }
        if (!this.unicodeToGlyph.containsKey(160) && (space = this.unicodeToGlyph.get(32)) != null) {
            this.unicodeToGlyph.put(160, new Glyph(space.getCode(), space.getWidth(), 160, space.getBbox()));
        }
        boolean endOfMetrics = false;
        while (true) {
            String line3 = raf.readLine();
            if (line3 != null) {
                StringTokenizer tok4 = new StringTokenizer(line3);
                if (tok4.hasMoreTokens()) {
                    String ident = tok4.nextToken();
                    if (ident.equals("EndFontMetrics")) {
                        endOfMetrics = true;
                    } else if (ident.equals("StartKernPairs")) {
                        startKernPairs = true;
                    }
                }
            }
        }
        if (startKernPairs) {
            while (true) {
                String line4 = raf.readLine();
                if (line4 != null) {
                    StringTokenizer tok5 = new StringTokenizer(line4);
                    if (tok5.hasMoreTokens()) {
                        String ident2 = tok5.nextToken();
                        if (ident2.equals("KPX")) {
                            String first = tok5.nextToken();
                            String second = tok5.nextToken();
                            Integer width = Integer.valueOf((int) Float.parseFloat(tok5.nextToken()));
                            int firstUni = AdobeGlyphList.nameToUnicode(first);
                            int secondUni = AdobeGlyphList.nameToUnicode(second);
                            if (firstUni != -1 && secondUni != -1) {
                                long record = (firstUni << 32) + secondUni;
                                this.kernPairs.put(Long.valueOf(record), width);
                            }
                        } else if (ident2.equals("EndKernPairs")) {
                            startKernPairs = false;
                        }
                    }
                }
            }
        } else if (!endOfMetrics) {
            String metricsPath3 = this.fontParser.getAfmPath();
            if (metricsPath3 == null) {
                throw new com.itextpdf.io.IOException("endfontmetrics is missing in the metrics file.");
            }
            throw new com.itextpdf.io.IOException("endfontmetrics is missing in {0}.").setMessageParams(metricsPath3);
        }
        if (startKernPairs) {
            String metricsPath4 = this.fontParser.getAfmPath();
            if (metricsPath4 == null) {
                throw new com.itextpdf.io.IOException("endkernpairs is missing in the metrics file.");
            }
            throw new com.itextpdf.io.IOException("endkernpairs is missing in {0}.").setMessageParams(metricsPath4);
        }
        raf.close();
        this.isFontSpecific = (this.encodingScheme.equals("AdobeStandardEncoding") || this.encodingScheme.equals("StandardEncoding")) ? false : true;
    }
}
