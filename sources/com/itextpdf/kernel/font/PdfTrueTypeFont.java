package com.itextpdf.kernel.font;

import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/PdfTrueTypeFont.class */
public class PdfTrueTypeFont extends PdfSimpleFont<TrueTypeFont> {
    private static final long serialVersionUID = -8152778382960290571L;

    PdfTrueTypeFont(TrueTypeFont ttf, String encoding, boolean embedded) {
        setFontProgram(ttf);
        this.embedded = embedded;
        FontNames fontNames = ttf.getFontNames();
        if (embedded && !fontNames.allowEmbedding()) {
            throw new PdfException(PdfException.CannotBeEmbeddedDueToLicensingRestrictions).setMessageParams(fontNames.getFontName());
        }
        if ((encoding == null || encoding.length() == 0) && ttf.isFontSpecific()) {
            encoding = FontEncoding.FONT_SPECIFIC;
        }
        if (encoding != null && FontEncoding.FONT_SPECIFIC.toLowerCase().equals(encoding.toLowerCase())) {
            this.fontEncoding = FontEncoding.createFontSpecificEncoding();
        } else {
            this.fontEncoding = FontEncoding.createFontEncoding(encoding);
        }
    }

    PdfTrueTypeFont(PdfDictionary fontDictionary) {
        super(fontDictionary);
        this.newFont = false;
        this.fontEncoding = DocFontEncoding.createDocFontEncoding(fontDictionary.get(PdfName.Encoding), this.toUnicode);
        this.fontProgram = DocTrueTypeFont.createFontProgram(fontDictionary, this.fontEncoding, this.toUnicode);
        this.embedded = ((IDocFontProgram) this.fontProgram).getFontFile() != null;
        this.subset = false;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public Glyph getGlyph(int unicode) {
        if (this.fontEncoding.canEncode(unicode)) {
            Glyph glyph = getFontProgram().getGlyph(this.fontEncoding.getUnicodeDifference(unicode));
            if (glyph == null) {
                Glyph glyph2 = this.notdefGlyphs.get(Integer.valueOf(unicode));
                glyph = glyph2;
                if (glyph2 == null) {
                    Glyph notdef = getFontProgram().getGlyphByCode(0);
                    if (notdef != null) {
                        glyph = new Glyph(getFontProgram().getGlyphByCode(0), unicode);
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
        return this.fontEncoding.isFontSpecific() ? this.fontProgram.getGlyphByCode(unicode) != null : this.fontEncoding.canEncode(unicode) && getFontProgram().getGlyph(this.fontEncoding.getUnicodeDifference(unicode)) != null;
    }

    @Override // com.itextpdf.kernel.font.PdfFont, com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        PdfName subtype;
        String fontName;
        if (isFlushed()) {
            return;
        }
        ensureUnderlyingObjectHasIndirectReference();
        if (this.newFont) {
            if (((TrueTypeFont) getFontProgram()).isCff()) {
                subtype = PdfName.Type1;
                fontName = this.fontProgram.getFontNames().getFontName();
            } else {
                subtype = PdfName.TrueType;
                fontName = updateSubsetPrefix(this.fontProgram.getFontNames().getFontName(), this.subset, this.embedded);
            }
            flushFontData(fontName, subtype);
        }
        super.flush();
    }

    @Deprecated
    protected void addRangeUni(Set<Integer> longTag) {
        ((TrueTypeFont) getFontProgram()).updateUsedGlyphs((SortedSet) longTag, this.subset, this.subsetRanges);
    }

    @Override // com.itextpdf.kernel.font.PdfSimpleFont
    protected void addFontStream(PdfDictionary fontDescriptor) {
        PdfName fontFileName;
        PdfStream fontStream;
        byte[] fontStreamBytes;
        if (this.embedded) {
            if (this.fontProgram instanceof IDocFontProgram) {
                fontFileName = ((IDocFontProgram) this.fontProgram).getFontFileName();
                fontStream = ((IDocFontProgram) this.fontProgram).getFontFile();
            } else if (((TrueTypeFont) getFontProgram()).isCff()) {
                fontFileName = PdfName.FontFile3;
                try {
                    byte[] fontStreamBytes2 = ((TrueTypeFont) getFontProgram()).getFontStreamBytes();
                    fontStream = getPdfFontStream(fontStreamBytes2, new int[]{fontStreamBytes2.length});
                    fontStream.put(PdfName.Subtype, new PdfName("Type1C"));
                } catch (PdfException e) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) PdfTrueTypeFont.class);
                    logger.error(e.getMessage());
                    fontStream = null;
                }
            } else {
                fontFileName = PdfName.FontFile2;
                SortedSet<Integer> glyphs = new TreeSet<>();
                for (int k = 0; k < this.shortTag.length; k++) {
                    if (this.shortTag[k] != 0) {
                        int uni = this.fontEncoding.getUnicode(k);
                        Glyph glyph = uni > -1 ? this.fontProgram.getGlyph(uni) : this.fontProgram.getGlyphByCode(k);
                        if (glyph != null) {
                            glyphs.add(Integer.valueOf(glyph.getCode()));
                        }
                    }
                }
                ((TrueTypeFont) getFontProgram()).updateUsedGlyphs(glyphs, this.subset, this.subsetRanges);
                try {
                    if (this.subset || ((TrueTypeFont) getFontProgram()).getDirectoryOffset() > 0) {
                        fontStreamBytes = ((TrueTypeFont) getFontProgram()).getSubset(glyphs, this.subset);
                    } else {
                        fontStreamBytes = ((TrueTypeFont) getFontProgram()).getFontStreamBytes();
                    }
                    fontStream = getPdfFontStream(fontStreamBytes, new int[]{fontStreamBytes.length});
                } catch (PdfException e2) {
                    Logger logger2 = LoggerFactory.getLogger((Class<?>) PdfTrueTypeFont.class);
                    logger2.error(e2.getMessage());
                    fontStream = null;
                }
            }
            if (fontStream != null) {
                fontDescriptor.put(fontFileName, fontStream);
                if (fontStream.getIndirectReference() != null) {
                    fontStream.flush();
                }
            }
        }
    }
}
