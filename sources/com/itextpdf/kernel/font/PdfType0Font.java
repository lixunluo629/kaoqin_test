package com.itextpdf.kernel.font;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.CFFFontSubset;
import com.itextpdf.io.font.CMapEncoding;
import com.itextpdf.io.font.CidFont;
import com.itextpdf.io.font.CidFontProperties;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.io.font.cmap.CMapContentParser;
import com.itextpdf.io.font.cmap.CMapToUnicode;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.source.OutputStream;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfOutputStream;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/PdfType0Font.class */
public class PdfType0Font extends PdfFont {
    private static final long serialVersionUID = -8033620300884193397L;
    private static final byte[] rotbits;
    protected static final int CID_FONT_TYPE_0 = 0;
    protected static final int CID_FONT_TYPE_2 = 2;
    protected boolean vertical;
    protected CMapEncoding cmapEncoding;
    protected Set<Integer> longTag;
    protected int cidFontType;
    protected char[] specificUnicodeDifferences;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfType0Font.class.desiredAssertionStatus();
        rotbits = new byte[]{Byte.MIN_VALUE, 64, 32, 16, 8, 4, 2, 1};
    }

    PdfType0Font(TrueTypeFont ttf, String cmap) {
        if (!PdfEncodings.IDENTITY_H.equals(cmap) && !PdfEncodings.IDENTITY_V.equals(cmap)) {
            throw new PdfException(PdfException.OnlyIdentityCMapsSupportsWithTrueType);
        }
        if (!ttf.getFontNames().allowEmbedding()) {
            throw new PdfException(PdfException.CannotBeEmbeddedDueToLicensingRestrictions).setMessageParams(ttf.getFontNames().getFontName() + ttf.getFontNames().getStyle());
        }
        this.fontProgram = ttf;
        this.embedded = true;
        this.vertical = cmap.endsWith("V");
        this.cmapEncoding = new CMapEncoding(cmap);
        this.longTag = new TreeSet();
        this.cidFontType = 2;
        if (ttf.isFontSpecific()) {
            this.specificUnicodeDifferences = new char[256];
            byte[] bytes = new byte[1];
            for (int k = 0; k < 256; k++) {
                bytes[0] = (byte) k;
                String s = PdfEncodings.convertToString(bytes, null);
                char ch2 = s.length() > 0 ? s.charAt(0) : '?';
                this.specificUnicodeDifferences[k] = ch2;
            }
        }
    }

    PdfType0Font(CidFont font, String cmap) {
        if (!CidFontProperties.isCidFont(font.getFontNames().getFontName(), cmap)) {
            throw new PdfException("Font {0} with {1} encoding is not a cjk font.").setMessageParams(font.getFontNames().getFontName(), cmap);
        }
        this.fontProgram = font;
        this.vertical = cmap.endsWith("V");
        String uniMap = getCompatibleUniMap(this.fontProgram.getRegistry());
        this.cmapEncoding = new CMapEncoding(cmap, uniMap);
        this.longTag = new TreeSet();
        this.cidFontType = 0;
    }

    PdfType0Font(PdfDictionary fontDictionary) {
        super(fontDictionary);
        this.newFont = false;
        PdfDictionary cidFont = fontDictionary.getAsArray(PdfName.DescendantFonts).getAsDictionary(0);
        PdfObject cmap = fontDictionary.get(PdfName.Encoding);
        PdfObject toUnicode = fontDictionary.get(PdfName.ToUnicode);
        CMapToUnicode toUnicodeCMap = FontUtil.processToUnicode(toUnicode);
        if (cmap.isName() && (PdfEncodings.IDENTITY_H.equals(((PdfName) cmap).getValue()) || PdfEncodings.IDENTITY_V.equals(((PdfName) cmap).getValue()))) {
            if (toUnicodeCMap == null) {
                String uniMap = getUniMapFromOrdering(getOrdering(cidFont));
                toUnicodeCMap = FontUtil.getToUnicodeFromUniMap(uniMap);
                if (toUnicodeCMap == null) {
                    toUnicodeCMap = FontUtil.getToUnicodeFromUniMap(PdfEncodings.IDENTITY_H);
                    Logger logger = LoggerFactory.getLogger((Class<?>) PdfType0Font.class);
                    logger.error(MessageFormatUtil.format(LogMessageConstant.UNKNOWN_CMAP, uniMap));
                }
            }
            this.fontProgram = DocTrueTypeFont.createFontProgram(cidFont, toUnicodeCMap);
            this.cmapEncoding = createCMap(cmap, null);
            if (!$assertionsDisabled && !(this.fontProgram instanceof IDocFontProgram)) {
                throw new AssertionError();
            }
            this.embedded = ((IDocFontProgram) this.fontProgram).getFontFile() != null;
        } else {
            String cidFontName = cidFont.getAsName(PdfName.BaseFont).getValue();
            String uniMap2 = getUniMapFromOrdering(getOrdering(cidFont));
            if (uniMap2 != null && uniMap2.startsWith("Uni") && CidFontProperties.isCidFont(cidFontName, uniMap2)) {
                try {
                    this.fontProgram = FontProgramFactory.createFont(cidFontName);
                    this.cmapEncoding = createCMap(cmap, uniMap2);
                    this.embedded = false;
                } catch (IOException e) {
                    this.fontProgram = null;
                    this.cmapEncoding = null;
                }
            } else {
                toUnicodeCMap = toUnicodeCMap == null ? FontUtil.getToUnicodeFromUniMap(uniMap2) : toUnicodeCMap;
                if (toUnicodeCMap != null) {
                    this.fontProgram = DocTrueTypeFont.createFontProgram(cidFont, toUnicodeCMap);
                    this.cmapEncoding = createCMap(cmap, uniMap2);
                }
            }
            if (this.fontProgram == null) {
                throw new PdfException(MessageFormatUtil.format(PdfException.CannotRecogniseDocumentFontWithEncoding, cidFontName, cmap));
            }
        }
        PdfDictionary cidFontDictionary = fontDictionary.getAsArray(PdfName.DescendantFonts).getAsDictionary(0);
        PdfName subtype = cidFontDictionary.getAsName(PdfName.Subtype);
        if (PdfName.CIDFontType0.equals(subtype)) {
            this.cidFontType = 0;
        } else if (PdfName.CIDFontType2.equals(subtype)) {
            this.cidFontType = 2;
        } else {
            LoggerFactory.getLogger(getClass()).error(LogMessageConstant.FAILED_TO_DETERMINE_CID_FONT_SUBTYPE);
        }
        this.longTag = new TreeSet();
        this.subset = false;
    }

    public static String getUniMapFromOrdering(String ordering) {
        switch (ordering) {
            case "CNS1":
                return "UniCNS-UTF16-H";
            case "Japan1":
                return "UniJIS-UTF16-H";
            case "Korea1":
                return "UniKS-UTF16-H";
            case "GB1":
                return "UniGB-UTF16-H";
            case "Identity":
                return PdfEncodings.IDENTITY_H;
            default:
                return null;
        }
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public Glyph getGlyph(int unicode) {
        Glyph glyph = getFontProgram().getGlyph(unicode);
        if (glyph == null) {
            Glyph glyph2 = this.notdefGlyphs.get(Integer.valueOf(unicode));
            glyph = glyph2;
            if (glyph2 == null) {
                Glyph notdef = getFontProgram().getGlyphByCode(0);
                if (notdef != null) {
                    glyph = new Glyph(notdef, unicode);
                } else {
                    glyph = new Glyph(-1, 0, unicode);
                }
                this.notdefGlyphs.put(Integer.valueOf(unicode), glyph);
            }
        }
        return glyph;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean containsGlyph(int unicode) {
        if (this.cidFontType == 0) {
            return this.cmapEncoding.isDirect() ? this.fontProgram.getGlyphByCode(unicode) != null : getFontProgram().getGlyph(unicode) != null;
        }
        if (this.cidFontType == 2) {
            if (!this.fontProgram.isFontSpecific()) {
                return getFontProgram().getGlyph(unicode) != null;
            }
            byte[] b = PdfEncodings.convertToBytes((char) unicode, "symboltt");
            return b.length > 0 && this.fontProgram.getGlyph(b[0] & 255) != null;
        }
        throw new PdfException("Invalid CID font type: " + this.cidFontType);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public byte[] convertToBytes(String text) {
        int val;
        int len = text.length();
        ByteBuffer buffer = new ByteBuffer();
        if (this.fontProgram.isFontSpecific()) {
            byte[] b = PdfEncodings.convertToBytes(text, "symboltt");
            for (byte b2 : b) {
                Glyph glyph = this.fontProgram.getGlyph(b2 & 255);
                if (glyph != null) {
                    convertToBytes(glyph, buffer);
                }
            }
        } else {
            int k = 0;
            while (k < len) {
                if (TextUtil.isSurrogatePair(text, k)) {
                    val = TextUtil.convertToUtf32(text, k);
                    k++;
                } else {
                    val = text.charAt(k);
                }
                Glyph glyph2 = getGlyph(val);
                if (glyph2.getCode() > 0) {
                    convertToBytes(glyph2, buffer);
                } else {
                    int nullCode = this.cmapEncoding.getCmapCode(0);
                    buffer.append(nullCode >> 8);
                    buffer.append(nullCode);
                }
                k++;
            }
        }
        return buffer.toByteArray();
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public byte[] convertToBytes(GlyphLine glyphLine) {
        if (glyphLine != null) {
            int totalByteCount = 0;
            for (int i = glyphLine.start; i < glyphLine.end; i++) {
                totalByteCount += this.cmapEncoding.getCmapBytesLength(glyphLine.get(i).getCode());
            }
            byte[] bytes = new byte[totalByteCount];
            int offset = 0;
            for (int i2 = glyphLine.start; i2 < glyphLine.end; i2++) {
                this.longTag.add(Integer.valueOf(glyphLine.get(i2).getCode()));
                offset = this.cmapEncoding.fillCmapBytes(glyphLine.get(i2).getCode(), bytes, offset);
            }
            return bytes;
        }
        return null;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public byte[] convertToBytes(Glyph glyph) {
        this.longTag.add(Integer.valueOf(glyph.getCode()));
        return this.cmapEncoding.getCmapBytes(glyph.getCode());
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public void writeText(GlyphLine text, int from, int to, PdfOutputStream stream) throws IOException {
        int len = (to - from) + 1;
        if (len > 0) {
            byte[] bytes = convertToBytes(new GlyphLine(text, from, to + 1));
            StreamUtil.writeHexedString(stream, bytes);
        }
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public void writeText(String text, PdfOutputStream stream) throws IOException {
        StreamUtil.writeHexedString(stream, convertToBytes(text));
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public GlyphLine createGlyphLine(String content) {
        int val;
        int ch2;
        List<Glyph> glyphs = new ArrayList<>();
        if (this.cidFontType == 0) {
            int len = content.length();
            if (this.cmapEncoding.isDirect()) {
                for (int k = 0; k < len; k++) {
                    Glyph glyph = this.fontProgram.getGlyphByCode(content.charAt(k));
                    if (glyph != null) {
                        glyphs.add(glyph);
                    }
                }
            } else {
                int k2 = 0;
                while (k2 < len) {
                    if (TextUtil.isSurrogatePair(content, k2)) {
                        ch2 = TextUtil.convertToUtf32(content, k2);
                        k2++;
                    } else {
                        ch2 = content.charAt(k2);
                    }
                    glyphs.add(getGlyph(ch2));
                    k2++;
                }
            }
        } else if (this.cidFontType == 2) {
            int len2 = content.length();
            if (this.fontProgram.isFontSpecific()) {
                byte[] b = PdfEncodings.convertToBytes(content, "symboltt");
                for (byte b2 : b) {
                    Glyph glyph2 = this.fontProgram.getGlyph(b2 & 255);
                    if (glyph2 != null) {
                        glyphs.add(glyph2);
                    }
                }
            } else {
                int k3 = 0;
                while (k3 < len2) {
                    if (TextUtil.isSurrogatePair(content, k3)) {
                        val = TextUtil.convertToUtf32(content, k3);
                        k3++;
                    } else {
                        val = content.charAt(k3);
                    }
                    glyphs.add(getGlyph(val));
                    k3++;
                }
            }
        } else {
            throw new PdfException("Font has no suitable cmap.");
        }
        return new GlyphLine(glyphs);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public int appendGlyphs(String text, int from, int to, List<Glyph> glyphs) {
        Glyph glyph;
        Glyph glyph2;
        if (this.cidFontType == 0) {
            if (this.cmapEncoding.isDirect()) {
                int processed = 0;
                for (int k = from; k <= to && (glyph2 = this.fontProgram.getGlyphByCode(text.charAt(k))) != null && isAppendableGlyph(glyph2); k++) {
                    glyphs.add(glyph2);
                    processed++;
                }
                return processed;
            }
            return appendUniGlyphs(text, from, to, glyphs);
        }
        if (this.cidFontType == 2) {
            if (this.fontProgram.isFontSpecific()) {
                int processed2 = 0;
                for (int k2 = from; k2 <= to && (glyph = this.fontProgram.getGlyph(text.charAt(k2) & 255)) != null && isAppendableGlyph(glyph); k2++) {
                    glyphs.add(glyph);
                    processed2++;
                }
                return processed2;
            }
            return appendUniGlyphs(text, from, to, glyphs);
        }
        throw new PdfException("Font has no suitable cmap.");
    }

    private int appendUniGlyphs(String text, int from, int to, List<Glyph> glyphs) {
        int val;
        int processed = 0;
        int k = from;
        while (true) {
            if (k > to) {
                break;
            }
            int currentlyProcessed = processed;
            if (TextUtil.isSurrogatePair(text, k)) {
                val = TextUtil.convertToUtf32(text, k);
                processed += 2;
            } else {
                val = text.charAt(k);
                processed++;
            }
            Glyph glyph = getGlyph(val);
            if (isAppendableGlyph(glyph)) {
                glyphs.add(glyph);
                k++;
            } else {
                processed = currentlyProcessed;
                break;
            }
        }
        return processed;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public int appendAnyGlyph(String text, int from, List<Glyph> glyphs) {
        int ch2;
        Glyph glyph;
        int ch3;
        int process = 1;
        if (this.cidFontType == 0) {
            if (this.cmapEncoding.isDirect()) {
                Glyph glyph2 = this.fontProgram.getGlyphByCode(text.charAt(from));
                if (glyph2 != null) {
                    glyphs.add(glyph2);
                }
            } else {
                if (TextUtil.isSurrogatePair(text, from)) {
                    ch3 = TextUtil.convertToUtf32(text, from);
                    process = 2;
                } else {
                    ch3 = text.charAt(from);
                }
                glyphs.add(getGlyph(ch3));
            }
        } else if (this.cidFontType == 2) {
            TrueTypeFont ttf = (TrueTypeFont) this.fontProgram;
            if (ttf.isFontSpecific()) {
                byte[] b = PdfEncodings.convertToBytes(text, "symboltt");
                if (b.length > 0 && (glyph = this.fontProgram.getGlyph(b[0] & 255)) != null) {
                    glyphs.add(glyph);
                }
            } else {
                if (TextUtil.isSurrogatePair(text, from)) {
                    ch2 = TextUtil.convertToUtf32(text, from);
                    process = 2;
                } else {
                    ch2 = text.charAt(from);
                }
                glyphs.add(getGlyph(ch2));
            }
        } else {
            throw new PdfException("Font has no suitable cmap.");
        }
        return process;
    }

    private boolean isAppendableGlyph(Glyph glyph) {
        return glyph.getCode() > 0 || TextUtil.isWhitespaceOrNonPrintable(glyph.getUnicode());
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public String decode(PdfString content) {
        return decodeIntoGlyphLine(content).toString();
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public GlyphLine decodeIntoGlyphLine(PdfString content) {
        String cids = content.getValue();
        List<Glyph> glyphs = new ArrayList<>();
        int i = 0;
        while (i < cids.length()) {
            int code = 0;
            Glyph glyph = null;
            int codeSpaceMatchedLength = 1;
            int codeLength = 1;
            while (true) {
                if (codeLength > 4 || i + codeLength > cids.length()) {
                    break;
                }
                code = (code << 8) + cids.charAt((i + codeLength) - 1);
                if (this.cmapEncoding.containsCodeInCodeSpaceRange(code, codeLength)) {
                    codeSpaceMatchedLength = codeLength;
                    int glyphCode = this.cmapEncoding.getCidCode(code);
                    glyph = this.fontProgram.getGlyphByCode(glyphCode);
                    if (glyph != null) {
                        i += codeLength - 1;
                        break;
                    }
                }
                codeLength++;
            }
            if (glyph == null) {
                StringBuilder failedCodes = new StringBuilder();
                for (int codeLength2 = 1; codeLength2 <= 4 && i + codeLength2 <= cids.length(); codeLength2++) {
                    failedCodes.append((int) cids.charAt((i + codeLength2) - 1)).append(SymbolConstants.SPACE_SYMBOL);
                }
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfType0Font.class);
                logger.warn(MessageFormatUtil.format(LogMessageConstant.COULD_NOT_FIND_GLYPH_WITH_CODE, failedCodes.toString()));
                i += codeSpaceMatchedLength - 1;
            }
            if (glyph != null && glyph.getChars() != null) {
                glyphs.add(glyph);
            } else {
                glyphs.add(new Glyph(0, this.fontProgram.getGlyphByCode(0).getWidth(), -1));
            }
            i++;
        }
        return new GlyphLine(glyphs);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public float getContentWidth(PdfString content) {
        float width = 0.0f;
        GlyphLine glyphLine = decodeIntoGlyphLine(content);
        for (int i = glyphLine.start; i < glyphLine.end; i++) {
            width += glyphLine.get(i).getWidth();
        }
        return width;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean isBuiltWith(String fontProgram, String encoding) {
        return getFontProgram().isBuiltWith(fontProgram) && this.cmapEncoding.isBuiltWith(encoding);
    }

    @Override // com.itextpdf.kernel.font.PdfFont, com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        if (isFlushed()) {
            return;
        }
        ensureUnderlyingObjectHasIndirectReference();
        if (this.newFont) {
            flushFontData();
        }
        super.flush();
    }

    public CMapEncoding getCmap() {
        return this.cmapEncoding;
    }

    @Deprecated
    public PdfStream getToUnicode(Object[] metrics) {
        return getToUnicode();
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    protected PdfDictionary getFontDescriptor(String fontName) {
        PdfDictionary fontDescriptor = new PdfDictionary();
        makeObjectIndirect(fontDescriptor);
        fontDescriptor.put(PdfName.Type, PdfName.FontDescriptor);
        fontDescriptor.put(PdfName.FontName, new PdfName(fontName));
        fontDescriptor.put(PdfName.FontBBox, new PdfArray(getFontProgram().getFontMetrics().getBbox()));
        fontDescriptor.put(PdfName.Ascent, new PdfNumber(getFontProgram().getFontMetrics().getTypoAscender()));
        fontDescriptor.put(PdfName.Descent, new PdfNumber(getFontProgram().getFontMetrics().getTypoDescender()));
        fontDescriptor.put(PdfName.CapHeight, new PdfNumber(getFontProgram().getFontMetrics().getCapHeight()));
        fontDescriptor.put(PdfName.ItalicAngle, new PdfNumber(getFontProgram().getFontMetrics().getItalicAngle()));
        fontDescriptor.put(PdfName.StemV, new PdfNumber(getFontProgram().getFontMetrics().getStemV()));
        fontDescriptor.put(PdfName.Flags, new PdfNumber(getFontProgram().getPdfFontFlags()));
        if (this.fontProgram.getFontIdentification().getPanose() != null) {
            PdfDictionary styleDictionary = new PdfDictionary();
            styleDictionary.put(PdfName.Panose, new PdfString(this.fontProgram.getFontIdentification().getPanose()).setHexWriting(true));
            fontDescriptor.put(PdfName.Style, styleDictionary);
        }
        return fontDescriptor;
    }

    @Deprecated
    protected PdfDictionary getCidFontType2(TrueTypeFont ttf, PdfDictionary fontDescriptor, String fontName, int[][] metrics) {
        return getCidFont(fontDescriptor, fontName, (ttf == null || ttf.isCff()) ? false : true);
    }

    @Deprecated
    protected void addRangeUni(TrueTypeFont ttf, Map<Integer, int[]> longTag, boolean includeMetrics) {
        addRangeUni(ttf, longTag.keySet());
    }

    private void convertToBytes(Glyph glyph, ByteBuffer result) {
        int code = glyph.getCode();
        this.longTag.add(Integer.valueOf(code));
        this.cmapEncoding.fillCmapBytes(code, result);
    }

    private static String getOrdering(PdfDictionary cidFont) {
        PdfDictionary cidinfo = cidFont.getAsDictionary(PdfName.CIDSystemInfo);
        if (cidinfo != null && cidinfo.containsKey(PdfName.Ordering)) {
            return cidinfo.get(PdfName.Ordering).toString();
        }
        return null;
    }

    private void flushFontData() {
        PdfStream fontStream;
        byte[] cffBytes;
        if (this.cidFontType == 0) {
            getPdfObject().put(PdfName.Type, PdfName.Font);
            getPdfObject().put(PdfName.Subtype, PdfName.Type0);
            String name = this.fontProgram.getFontNames().getFontName();
            String style = this.fontProgram.getFontNames().getStyle();
            if (style.length() > 0) {
                name = name + "-" + style;
            }
            getPdfObject().put(PdfName.BaseFont, new PdfName(MessageFormatUtil.format("{0}-{1}", name, this.cmapEncoding.getCmapName())));
            getPdfObject().put(PdfName.Encoding, new PdfName(this.cmapEncoding.getCmapName()));
            PdfDictionary fontDescriptor = getFontDescriptor(name);
            PdfDictionary cidFont = getCidFont(fontDescriptor, this.fontProgram.getFontNames().getFontName(), false);
            getPdfObject().put(PdfName.DescendantFonts, new PdfArray(cidFont));
            fontDescriptor.flush();
            cidFont.flush();
            return;
        }
        if (this.cidFontType == 2) {
            TrueTypeFont ttf = (TrueTypeFont) getFontProgram();
            String fontName = updateSubsetPrefix(ttf.getFontNames().getFontName(), this.subset, this.embedded);
            PdfDictionary fontDescriptor2 = getFontDescriptor(fontName);
            ttf.updateUsedGlyphs((SortedSet) this.longTag, this.subset, this.subsetRanges);
            if (ttf.isCff()) {
                if (this.subset) {
                    cffBytes = new CFFFontSubset(ttf.getFontStreamBytes(), this.longTag).Process();
                } else {
                    cffBytes = ttf.getFontStreamBytes();
                }
                fontStream = getPdfFontStream(cffBytes, new int[]{cffBytes.length});
                fontStream.put(PdfName.Subtype, new PdfName("CIDFontType0C"));
                getPdfObject().put(PdfName.BaseFont, new PdfName(MessageFormatUtil.format("{0}-{1}", fontName, this.cmapEncoding.getCmapName())));
                fontDescriptor2.put(PdfName.FontFile3, fontStream);
            } else {
                byte[] ttfBytes = null;
                if (this.subset || ttf.getDirectoryOffset() > 0) {
                    try {
                        ttfBytes = ttf.getSubset(this.longTag, this.subset);
                    } catch (com.itextpdf.io.IOException e) {
                        Logger logger = LoggerFactory.getLogger((Class<?>) PdfType0Font.class);
                        logger.warn(LogMessageConstant.FONT_SUBSET_ISSUE);
                        ttfBytes = null;
                    }
                }
                if (ttfBytes == null) {
                    ttfBytes = ttf.getFontStreamBytes();
                }
                fontStream = getPdfFontStream(ttfBytes, new int[]{ttfBytes.length});
                getPdfObject().put(PdfName.BaseFont, new PdfName(fontName));
                fontDescriptor2.put(PdfName.FontFile2, fontStream);
            }
            int numOfGlyphs = ttf.getFontMetrics().getNumberOfGlyphs();
            byte[] cidSetBytes = new byte[(ttf.getFontMetrics().getNumberOfGlyphs() / 8) + 1];
            for (int i = 0; i < numOfGlyphs / 8; i++) {
                int i2 = i;
                cidSetBytes[i2] = (byte) (cidSetBytes[i2] | 255);
            }
            for (int i3 = 0; i3 < numOfGlyphs % 8; i3++) {
                int length = cidSetBytes.length - 1;
                cidSetBytes[length] = (byte) (cidSetBytes[length] | rotbits[i3]);
            }
            fontDescriptor2.put(PdfName.CIDSet, new PdfStream(cidSetBytes));
            PdfDictionary cidFont2 = getCidFont(fontDescriptor2, fontName, !ttf.isCff());
            getPdfObject().put(PdfName.Type, PdfName.Font);
            getPdfObject().put(PdfName.Subtype, PdfName.Type0);
            getPdfObject().put(PdfName.Encoding, new PdfName(this.cmapEncoding.getCmapName()));
            getPdfObject().put(PdfName.DescendantFonts, new PdfArray(cidFont2));
            PdfStream toUnicode = getToUnicode();
            if (toUnicode != null) {
                getPdfObject().put(PdfName.ToUnicode, toUnicode);
                if (toUnicode.getIndirectReference() != null) {
                    toUnicode.flush();
                }
            }
            if (getPdfObject().getIndirectReference().getDocument().getPdfVersion().compareTo(PdfVersion.PDF_2_0) >= 0) {
                fontDescriptor2.remove(PdfName.CIDSet);
            }
            fontDescriptor2.flush();
            cidFont2.flush();
            fontStream.flush();
            return;
        }
        throw new IllegalStateException("Unsupported CID Font");
    }

    @Deprecated
    protected PdfDictionary getCidFontType2(TrueTypeFont ttf, PdfDictionary fontDescriptor, String fontName, int[] glyphIds) {
        return getCidFont(fontDescriptor, fontName, (ttf == null || ttf.isCff()) ? false : true);
    }

    protected PdfDictionary getCidFont(PdfDictionary fontDescriptor, String fontName, boolean isType2) {
        PdfDictionary cidFont = new PdfDictionary();
        markObjectAsIndirect(cidFont);
        cidFont.put(PdfName.Type, PdfName.Font);
        cidFont.put(PdfName.FontDescriptor, fontDescriptor);
        if (isType2) {
            cidFont.put(PdfName.Subtype, PdfName.CIDFontType2);
            cidFont.put(PdfName.CIDToGIDMap, PdfName.Identity);
        } else {
            cidFont.put(PdfName.Subtype, PdfName.CIDFontType0);
        }
        cidFont.put(PdfName.BaseFont, new PdfName(fontName));
        PdfDictionary cidInfo = new PdfDictionary();
        cidInfo.put(PdfName.Registry, new PdfString(this.cmapEncoding.getRegistry()));
        cidInfo.put(PdfName.Ordering, new PdfString(this.cmapEncoding.getOrdering()));
        cidInfo.put(PdfName.Supplement, new PdfNumber(this.cmapEncoding.getSupplement()));
        cidFont.put(PdfName.CIDSystemInfo, cidInfo);
        if (!this.vertical) {
            cidFont.put(PdfName.DW, new PdfNumber(1000));
            PdfObject widthsArray = generateWidthsArray();
            if (widthsArray != null) {
                cidFont.put(PdfName.W, widthsArray);
            }
        } else {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfType0Font.class);
            logger.warn("Vertical writing has not been implemented yet.");
        }
        return cidFont;
    }

    private PdfObject generateWidthsArray() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        OutputStream<ByteArrayOutputStream> stream = new OutputStream<>(bytes);
        stream.writeByte(91);
        int lastNumber = -10;
        boolean firstTime = true;
        Iterator<Integer> it = this.longTag.iterator();
        while (it.hasNext()) {
            int code = it.next().intValue();
            Glyph glyph = this.fontProgram.getGlyphByCode(code);
            if (glyph.getWidth() != 1000) {
                if (glyph.getCode() == lastNumber + 1) {
                    stream.writeByte(32);
                } else {
                    if (!firstTime) {
                        stream.writeByte(93);
                    }
                    firstTime = false;
                    stream.writeInteger(glyph.getCode());
                    stream.writeByte(91);
                }
                stream.writeInteger(glyph.getWidth());
                lastNumber = glyph.getCode();
            }
        }
        if (stream.getCurrentPos() > 1) {
            stream.writeString("]]");
            return new PdfLiteral(bytes.toByteArray());
        }
        return null;
    }

    public PdfStream getToUnicode() {
        OutputStream<ByteArrayOutputStream> stream = new OutputStream<>(new ByteArrayOutputStream());
        stream.writeString("/CIDInit /ProcSet findresource begin\n12 dict begin\nbegincmap\n/CIDSystemInfo\n<< /Registry (Adobe)\n/Ordering (UCS)\n/Supplement 0\n>> def\n/CMapName /Adobe-Identity-UCS def\n/CMapType 2 def\n1 begincodespacerange\n<0000><FFFF>\nendcodespacerange\n");
        ArrayList<Glyph> glyphGroup = new ArrayList<>(100);
        int bfranges = 0;
        for (Integer glyphId : this.longTag) {
            Glyph glyph = this.fontProgram.getGlyphByCode(glyphId.intValue());
            if (glyph.getChars() != null) {
                glyphGroup.add(glyph);
                if (glyphGroup.size() == 100) {
                    bfranges += writeBfrange(stream, glyphGroup);
                }
            }
        }
        if (bfranges + writeBfrange(stream, glyphGroup) == 0) {
            return null;
        }
        stream.writeString("endcmap\nCMapName currentdict /CMap defineresource pop\nend end\n");
        return new PdfStream(((ByteArrayOutputStream) stream.getOutputStream()).toByteArray());
    }

    private int writeBfrange(OutputStream<ByteArrayOutputStream> stream, List<Glyph> range) {
        if (range.isEmpty()) {
            return 0;
        }
        stream.writeInteger(range.size());
        stream.writeString(" beginbfrange\n");
        for (Glyph glyph : range) {
            String fromTo = CMapContentParser.toHex(glyph.getCode());
            stream.writeString(fromTo);
            stream.writeString(fromTo);
            stream.writeByte(60);
            for (char ch2 : glyph.getChars()) {
                stream.writeString(toHex4(ch2));
            }
            stream.writeByte(62);
            stream.writeByte(10);
        }
        stream.writeString("endbfrange\n");
        range.clear();
        return 1;
    }

    private static String toHex4(char ch2) {
        String s = "0000" + Integer.toHexString(ch2);
        return s.substring(s.length() - 4);
    }

    @Deprecated
    protected void addRangeUni(TrueTypeFont ttf, Set<Integer> longTag) {
        ttf.updateUsedGlyphs((SortedSet) longTag, this.subset, this.subsetRanges);
    }

    private String getCompatibleUniMap(String registry) {
        String uniMap = "";
        for (String name : CidFontProperties.getRegistryNames().get(registry + "_Uni")) {
            uniMap = name;
            if ((name.endsWith("V") && this.vertical) || (!name.endsWith("V") && !this.vertical)) {
                break;
            }
        }
        return uniMap;
    }

    private static CMapEncoding createCMap(PdfObject cmap, String uniMap) {
        if (cmap.isStream()) {
            PdfStream cmapStream = (PdfStream) cmap;
            byte[] cmapBytes = cmapStream.getBytes();
            return new CMapEncoding(cmapStream.getAsName(PdfName.CMapName).getValue(), cmapBytes);
        }
        String cmapName = ((PdfName) cmap).getValue();
        if (PdfEncodings.IDENTITY_H.equals(cmapName) || PdfEncodings.IDENTITY_V.equals(cmapName)) {
            return new CMapEncoding(cmapName);
        }
        return new CMapEncoding(cmapName, uniMap);
    }
}
