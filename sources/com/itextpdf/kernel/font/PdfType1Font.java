package com.itextpdf.kernel.font;

import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.Type1Font;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/PdfType1Font.class */
public class PdfType1Font extends PdfSimpleFont<Type1Font> {
    private static final long serialVersionUID = 7009919945291639441L;

    PdfType1Font(Type1Font type1Font, String encoding, boolean embedded) {
        setFontProgram(type1Font);
        this.embedded = embedded && !type1Font.isBuiltInFont();
        if ((encoding == null || encoding.length() == 0) && type1Font.isFontSpecific()) {
            encoding = FontEncoding.FONT_SPECIFIC;
        }
        if (encoding != null && FontEncoding.FONT_SPECIFIC.toLowerCase().equals(encoding.toLowerCase())) {
            this.fontEncoding = FontEncoding.createFontSpecificEncoding();
        } else {
            this.fontEncoding = FontEncoding.createFontEncoding(encoding);
        }
    }

    PdfType1Font(Type1Font type1Font, String encoding) {
        this(type1Font, encoding, false);
    }

    PdfType1Font(PdfDictionary fontDictionary) {
        super(fontDictionary);
        this.newFont = false;
        this.fontEncoding = DocFontEncoding.createDocFontEncoding(fontDictionary.get(PdfName.Encoding), this.toUnicode);
        this.fontProgram = DocType1Font.createFontProgram(fontDictionary, this.fontEncoding, this.toUnicode);
        if (this.fontProgram instanceof IDocFontProgram) {
            this.embedded = ((IDocFontProgram) this.fontProgram).getFontFile() != null;
        }
        this.subset = false;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean isSubset() {
        return this.subset;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public void setSubset(boolean subset) {
        this.subset = subset;
    }

    @Override // com.itextpdf.kernel.font.PdfFont, com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        if (isFlushed()) {
            return;
        }
        ensureUnderlyingObjectHasIndirectReference();
        if (this.newFont) {
            flushFontData(this.fontProgram.getFontNames().getFontName(), PdfName.Type1);
        }
        super.flush();
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public Glyph getGlyph(int unicode) {
        Glyph glyph;
        if (this.fontEncoding.canEncode(unicode)) {
            if (this.fontEncoding.isFontSpecific()) {
                glyph = getFontProgram().getGlyphByCode(unicode);
            } else {
                glyph = getFontProgram().getGlyph(this.fontEncoding.getUnicodeDifference(unicode));
                if (glyph == null) {
                    Glyph glyph2 = this.notdefGlyphs.get(Integer.valueOf(unicode));
                    glyph = glyph2;
                    if (glyph2 == null) {
                        glyph = new Glyph(-1, 0, unicode);
                        this.notdefGlyphs.put(Integer.valueOf(unicode), glyph);
                    }
                }
            }
            return glyph;
        }
        return null;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean containsGlyph(int unicode) {
        if (this.fontEncoding.canEncode(unicode)) {
            return this.fontEncoding.isFontSpecific() ? getFontProgram().getGlyphByCode(unicode) != null : getFontProgram().getGlyph(this.fontEncoding.getUnicodeDifference(unicode)) != null;
        }
        return false;
    }

    @Override // com.itextpdf.kernel.font.PdfSimpleFont
    protected boolean isBuiltInFont() {
        return ((Type1Font) getFontProgram()).isBuiltInFont();
    }

    @Override // com.itextpdf.kernel.font.PdfSimpleFont
    protected void addFontStream(PdfDictionary fontDescriptor) {
        if (this.embedded) {
            if (this.fontProgram instanceof IDocFontProgram) {
                IDocFontProgram docType1Font = (IDocFontProgram) this.fontProgram;
                fontDescriptor.put(docType1Font.getFontFileName(), docType1Font.getFontFile());
                docType1Font.getFontFile().flush();
                if (docType1Font.getSubtype() != null) {
                    fontDescriptor.put(PdfName.Subtype, docType1Font.getSubtype());
                    return;
                }
                return;
            }
            byte[] fontStreamBytes = ((Type1Font) getFontProgram()).getFontStreamBytes();
            if (fontStreamBytes != null) {
                PdfStream fontStream = new PdfStream(fontStreamBytes);
                int[] fontStreamLengths = ((Type1Font) getFontProgram()).getFontStreamLengths();
                for (int k = 0; k < fontStreamLengths.length; k++) {
                    fontStream.put(new PdfName("Length" + (k + 1)), new PdfNumber(fontStreamLengths[k]));
                }
                fontDescriptor.put(PdfName.FontFile, fontStream);
                if (makeObjectIndirect(fontStream)) {
                    fontStream.flush();
                }
            }
        }
    }
}
