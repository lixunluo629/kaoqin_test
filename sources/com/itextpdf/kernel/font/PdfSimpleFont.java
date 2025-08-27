package com.itextpdf.kernel.font;

import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.FontMetrics;
import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.cmap.CMapToUnicode;
import com.itextpdf.io.font.constants.FontDescriptorFlags;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.util.ArrayUtil;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfOutputStream;
import com.itextpdf.kernel.pdf.PdfString;
import java.util.ArrayList;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/PdfSimpleFont.class */
public abstract class PdfSimpleFont<T extends FontProgram> extends PdfFont {
    private static final long serialVersionUID = -4942318223894676176L;
    protected FontEncoding fontEncoding;
    protected boolean forceWidthsOutput;
    protected byte[] shortTag;
    protected CMapToUnicode toUnicode;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void addFontStream(PdfDictionary pdfDictionary);

    static {
        $assertionsDisabled = !PdfSimpleFont.class.desiredAssertionStatus();
    }

    protected PdfSimpleFont(PdfDictionary fontDictionary) {
        super(fontDictionary);
        this.forceWidthsOutput = false;
        this.shortTag = new byte[256];
        this.toUnicode = FontUtil.processToUnicode(fontDictionary.get(PdfName.ToUnicode));
    }

    protected PdfSimpleFont() {
        this.forceWidthsOutput = false;
        this.shortTag = new byte[256];
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean isBuiltWith(String fontProgram, String encoding) {
        return getFontProgram().isBuiltWith(fontProgram) && this.fontEncoding.isBuiltWith(encoding);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public GlyphLine createGlyphLine(String content) {
        List<Glyph> glyphs = new ArrayList<>(content.length());
        if (this.fontEncoding.isFontSpecific()) {
            for (int i = 0; i < content.length(); i++) {
                Glyph glyph = this.fontProgram.getGlyphByCode(content.charAt(i));
                if (glyph != null) {
                    glyphs.add(glyph);
                }
            }
        } else {
            for (int i2 = 0; i2 < content.length(); i2++) {
                Glyph glyph2 = getGlyph(content.charAt(i2));
                if (glyph2 != null) {
                    glyphs.add(glyph2);
                }
            }
        }
        return new GlyphLine(glyphs);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public int appendGlyphs(String text, int from, int to, List<Glyph> glyphs) {
        Glyph glyph;
        int processed = 0;
        if (this.fontEncoding.isFontSpecific()) {
            for (int i = from; i <= to && (glyph = this.fontProgram.getGlyphByCode(text.charAt(i) & 255)) != null; i++) {
                glyphs.add(glyph);
                processed++;
            }
        } else {
            for (int i2 = from; i2 <= to; i2++) {
                Glyph glyph2 = getGlyph(text.charAt(i2));
                if (glyph2 != null && (containsGlyph(glyph2.getUnicode()) || isAppendableGlyph(glyph2))) {
                    glyphs.add(glyph2);
                } else if (glyph2 != null || !TextUtil.isWhitespaceOrNonPrintable(text.charAt(i2))) {
                    break;
                }
                processed++;
            }
        }
        return processed;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public int appendAnyGlyph(String text, int from, List<Glyph> glyphs) {
        Glyph glyph;
        if (this.fontEncoding.isFontSpecific()) {
            glyph = this.fontProgram.getGlyphByCode(text.charAt(from));
        } else {
            glyph = getGlyph(text.charAt(from));
        }
        if (glyph != null) {
            glyphs.add(glyph);
            return 1;
        }
        return 1;
    }

    private boolean isAppendableGlyph(Glyph glyph) {
        return glyph.getCode() > 0 || TextUtil.isWhitespaceOrNonPrintable(glyph.getUnicode());
    }

    public FontEncoding getFontEncoding() {
        return this.fontEncoding;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public byte[] convertToBytes(String text) {
        byte[] bytes = this.fontEncoding.convertToBytes(text);
        for (byte b : bytes) {
            this.shortTag[b & 255] = 1;
        }
        return bytes;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public byte[] convertToBytes(GlyphLine glyphLine) {
        if (glyphLine != null) {
            byte[] bytes = new byte[glyphLine.size()];
            int ptr = 0;
            if (this.fontEncoding.isFontSpecific()) {
                for (int i = 0; i < glyphLine.size(); i++) {
                    int i2 = ptr;
                    ptr++;
                    bytes[i2] = (byte) glyphLine.get(i).getCode();
                }
            } else {
                for (int i3 = 0; i3 < glyphLine.size(); i3++) {
                    if (this.fontEncoding.canEncode(glyphLine.get(i3).getUnicode())) {
                        int i4 = ptr;
                        ptr++;
                        bytes[i4] = (byte) this.fontEncoding.convertToByte(glyphLine.get(i3).getUnicode());
                    }
                }
            }
            byte[] bytes2 = ArrayUtil.shortenArray(bytes, ptr);
            for (byte b : bytes2) {
                this.shortTag[b & 255] = 1;
            }
            return bytes2;
        }
        return EMPTY_BYTES;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public byte[] convertToBytes(Glyph glyph) {
        byte[] bytes = new byte[1];
        if (this.fontEncoding.isFontSpecific()) {
            bytes[0] = (byte) glyph.getCode();
        } else if (this.fontEncoding.canEncode(glyph.getUnicode())) {
            bytes[0] = (byte) this.fontEncoding.convertToByte(glyph.getUnicode());
        } else {
            return EMPTY_BYTES;
        }
        this.shortTag[bytes[0] & 255] = 1;
        return bytes;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public void writeText(GlyphLine text, int from, int to, PdfOutputStream stream) {
        byte[] bytes = new byte[(to - from) + 1];
        int ptr = 0;
        if (this.fontEncoding.isFontSpecific()) {
            for (int i = from; i <= to; i++) {
                int i2 = ptr;
                ptr++;
                bytes[i2] = (byte) text.get(i).getCode();
            }
        } else {
            for (int i3 = from; i3 <= to; i3++) {
                if (this.fontEncoding.canEncode(text.get(i3).getUnicode())) {
                    int i4 = ptr;
                    ptr++;
                    bytes[i4] = (byte) this.fontEncoding.convertToByte(text.get(i3).getUnicode());
                }
            }
        }
        byte[] bytes2 = ArrayUtil.shortenArray(bytes, ptr);
        for (byte b : bytes2) {
            this.shortTag[b & 255] = 1;
        }
        StreamUtil.writeEscapedString(stream, bytes2);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public void writeText(String text, PdfOutputStream stream) {
        StreamUtil.writeEscapedString(stream, convertToBytes(text));
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public String decode(PdfString content) {
        return decodeIntoGlyphLine(content).toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0085 A[PHI: r13
  0x0085: PHI (r13v1 'glyph' com.itextpdf.io.font.otf.Glyph) = 
  (r13v0 'glyph' com.itextpdf.io.font.otf.Glyph)
  (r13v0 'glyph' com.itextpdf.io.font.otf.Glyph)
  (r13v5 'glyph' com.itextpdf.io.font.otf.Glyph)
 binds: [B:6:0x0037, B:8:0x0043, B:10:0x0052] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.itextpdf.kernel.font.PdfFont
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.itextpdf.io.font.otf.GlyphLine decodeIntoGlyphLine(com.itextpdf.kernel.pdf.PdfString r5) {
        /*
            r4 = this;
            r0 = r5
            byte[] r0 = r0.getValueBytes()
            r6 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r2 = r6
            int r2 = r2.length
            r1.<init>(r2)
            r7 = r0
            r0 = r6
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = 0
            r10 = r0
        L1a:
            r0 = r10
            r1 = r9
            if (r0 >= r1) goto Lca
            r0 = r8
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            r0 = r11
            r1 = 255(0xff, float:3.57E-43)
            r0 = r0 & r1
            r12 = r0
            r0 = 0
            r13 = r0
            r0 = r4
            com.itextpdf.io.font.cmap.CMapToUnicode r0 = r0.toUnicode
            if (r0 == 0) goto L85
            r0 = r4
            com.itextpdf.io.font.cmap.CMapToUnicode r0 = r0.toUnicode
            r1 = r12
            char[] r0 = r0.lookup(r1)
            if (r0 == 0) goto L85
            r0 = r4
            com.itextpdf.io.font.FontProgram r0 = r0.fontProgram
            r1 = r12
            com.itextpdf.io.font.otf.Glyph r0 = r0.getGlyphByCode(r1)
            r1 = r0
            r13 = r1
            if (r0 == 0) goto L85
            r0 = r4
            com.itextpdf.io.font.cmap.CMapToUnicode r0 = r0.toUnicode
            r1 = r12
            char[] r0 = r0.lookup(r1)
            r1 = r13
            char[] r1 = r1.getChars()
            boolean r0 = java.util.Arrays.equals(r0, r1)
            if (r0 != 0) goto Lb6
            com.itextpdf.io.font.otf.Glyph r0 = new com.itextpdf.io.font.otf.Glyph
            r1 = r0
            r2 = r13
            r1.<init>(r2)
            r13 = r0
            r0 = r13
            r1 = r4
            com.itextpdf.io.font.cmap.CMapToUnicode r1 = r1.toUnicode
            r2 = r12
            char[] r1 = r1.lookup(r2)
            r0.setChars(r1)
            goto Lb6
        L85:
            r0 = r4
            com.itextpdf.io.font.FontEncoding r0 = r0.fontEncoding
            r1 = r12
            int r0 = r0.getUnicode(r1)
            r14 = r0
            r0 = r14
            r1 = -1
            if (r0 <= r1) goto La1
            r0 = r4
            r1 = r14
            com.itextpdf.io.font.otf.Glyph r0 = r0.getGlyph(r1)
            r13 = r0
            goto Lb6
        La1:
            r0 = r4
            com.itextpdf.io.font.FontEncoding r0 = r0.fontEncoding
            java.lang.String r0 = r0.getBaseEncoding()
            if (r0 != 0) goto Lb6
            r0 = r4
            com.itextpdf.io.font.FontProgram r0 = r0.fontProgram
            r1 = r12
            com.itextpdf.io.font.otf.Glyph r0 = r0.getGlyphByCode(r1)
            r13 = r0
        Lb6:
            r0 = r13
            if (r0 == 0) goto Lc4
            r0 = r7
            r1 = r13
            boolean r0 = r0.add(r1)
        Lc4:
            int r10 = r10 + 1
            goto L1a
        Lca:
            com.itextpdf.io.font.otf.GlyphLine r0 = new com.itextpdf.io.font.otf.GlyphLine
            r1 = r0
            r2 = r7
            r1.<init>(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.kernel.font.PdfSimpleFont.decodeIntoGlyphLine(com.itextpdf.kernel.pdf.PdfString):com.itextpdf.io.font.otf.GlyphLine");
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

    public boolean isForceWidthsOutput() {
        return this.forceWidthsOutput;
    }

    public void setForceWidthsOutput(boolean forceWidthsOutput) {
        this.forceWidthsOutput = forceWidthsOutput;
    }

    protected void flushFontData(String fontName, PdfName subtype) {
        getPdfObject().put(PdfName.Subtype, subtype);
        if (fontName != null && fontName.length() > 0) {
            getPdfObject().put(PdfName.BaseFont, new PdfName(fontName));
        }
        int firstChar = 0;
        while (firstChar < 256 && this.shortTag[firstChar] == 0) {
            firstChar++;
        }
        int lastChar = 255;
        while (lastChar >= firstChar && this.shortTag[lastChar] == 0) {
            lastChar--;
        }
        if (firstChar > 255) {
            firstChar = 255;
            lastChar = 255;
        }
        if (!isSubset() || !isEmbedded()) {
            firstChar = 0;
            lastChar = this.shortTag.length - 1;
            for (int k = 0; k < this.shortTag.length; k++) {
                if (this.fontEncoding.canDecode(k)) {
                    this.shortTag[k] = 1;
                } else if (!this.fontEncoding.hasDifferences() && this.fontProgram.getGlyphByCode(k) != null) {
                    this.shortTag[k] = 1;
                } else {
                    this.shortTag[k] = 0;
                }
            }
        }
        if (this.fontEncoding.hasDifferences()) {
            int k2 = firstChar;
            while (true) {
                if (k2 > lastChar) {
                    break;
                }
                if (FontEncoding.NOTDEF.equals(this.fontEncoding.getDifference(k2))) {
                    k2++;
                } else {
                    firstChar = k2;
                    break;
                }
            }
            int k3 = lastChar;
            while (true) {
                if (k3 < firstChar) {
                    break;
                }
                if (!FontEncoding.NOTDEF.equals(this.fontEncoding.getDifference(k3))) {
                    lastChar = k3;
                    break;
                }
                k3--;
            }
            PdfDictionary enc = new PdfDictionary();
            enc.put(PdfName.Type, PdfName.Encoding);
            PdfArray diff = new PdfArray();
            boolean gap = true;
            for (int k4 = firstChar; k4 <= lastChar; k4++) {
                if (this.shortTag[k4] != 0) {
                    if (gap) {
                        diff.add(new PdfNumber(k4));
                        gap = false;
                    }
                    diff.add(new PdfName(this.fontEncoding.getDifference(k4)));
                } else {
                    gap = true;
                }
            }
            enc.put(PdfName.Differences, diff);
            getPdfObject().put(PdfName.Encoding, enc);
        } else if (!this.fontEncoding.isFontSpecific()) {
            getPdfObject().put(PdfName.Encoding, "Cp1252".equals(this.fontEncoding.getBaseEncoding()) ? PdfName.WinAnsiEncoding : PdfName.MacRomanEncoding);
        }
        if (isForceWidthsOutput() || !isBuiltInFont() || this.fontEncoding.hasDifferences()) {
            getPdfObject().put(PdfName.FirstChar, new PdfNumber(firstChar));
            getPdfObject().put(PdfName.LastChar, new PdfNumber(lastChar));
            PdfArray wd = new PdfArray();
            for (int k5 = firstChar; k5 <= lastChar; k5++) {
                if (this.shortTag[k5] == 0) {
                    wd.add(new PdfNumber(0));
                } else {
                    int uni = this.fontEncoding.getUnicode(k5);
                    Glyph glyph = uni > -1 ? getGlyph(uni) : this.fontProgram.getGlyphByCode(k5);
                    wd.add(new PdfNumber(glyph != null ? glyph.getWidth() : 0));
                }
            }
            getPdfObject().put(PdfName.Widths, wd);
        }
        PdfDictionary fontDescriptor = !isBuiltInFont() ? getFontDescriptor(fontName) : null;
        if (fontDescriptor != null) {
            getPdfObject().put(PdfName.FontDescriptor, fontDescriptor);
            if (fontDescriptor.getIndirectReference() != null) {
                fontDescriptor.flush();
            }
        }
    }

    protected boolean isBuiltInFont() {
        return false;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    protected PdfDictionary getFontDescriptor(String fontName) {
        if (!$assertionsDisabled && (fontName == null || fontName.length() <= 0)) {
            throw new AssertionError();
        }
        FontMetrics fontMetrics = this.fontProgram.getFontMetrics();
        FontNames fontNames = this.fontProgram.getFontNames();
        PdfDictionary fontDescriptor = new PdfDictionary();
        makeObjectIndirect(fontDescriptor);
        fontDescriptor.put(PdfName.Type, PdfName.FontDescriptor);
        fontDescriptor.put(PdfName.FontName, new PdfName(fontName));
        fontDescriptor.put(PdfName.Ascent, new PdfNumber(fontMetrics.getTypoAscender()));
        fontDescriptor.put(PdfName.CapHeight, new PdfNumber(fontMetrics.getCapHeight()));
        fontDescriptor.put(PdfName.Descent, new PdfNumber(fontMetrics.getTypoDescender()));
        fontDescriptor.put(PdfName.FontBBox, new PdfArray(ArrayUtil.cloneArray(fontMetrics.getBbox())));
        fontDescriptor.put(PdfName.ItalicAngle, new PdfNumber(fontMetrics.getItalicAngle()));
        fontDescriptor.put(PdfName.StemV, new PdfNumber(fontMetrics.getStemV()));
        if (fontMetrics.getXHeight() > 0) {
            fontDescriptor.put(PdfName.XHeight, new PdfNumber(fontMetrics.getXHeight()));
        }
        if (fontMetrics.getStemH() > 0) {
            fontDescriptor.put(PdfName.StemH, new PdfNumber(fontMetrics.getStemH()));
        }
        if (fontNames.getFontWeight() > 0) {
            fontDescriptor.put(PdfName.FontWeight, new PdfNumber(fontNames.getFontWeight()));
        }
        if (fontNames.getFamilyName() != null && fontNames.getFamilyName().length > 0 && fontNames.getFamilyName()[0].length >= 4) {
            fontDescriptor.put(PdfName.FontFamily, new PdfString(fontNames.getFamilyName()[0][3]));
        }
        addFontStream(fontDescriptor);
        int flags = this.fontProgram.getPdfFontFlags();
        fontDescriptor.put(PdfName.Flags, new PdfNumber((flags & ((FontDescriptorFlags.Symbolic | FontDescriptorFlags.Nonsymbolic) ^ (-1))) | (this.fontEncoding.isFontSpecific() ? FontDescriptorFlags.Symbolic : FontDescriptorFlags.Nonsymbolic)));
        return fontDescriptor;
    }

    protected void setFontProgram(T fontProgram) {
        this.fontProgram = fontProgram;
    }
}
