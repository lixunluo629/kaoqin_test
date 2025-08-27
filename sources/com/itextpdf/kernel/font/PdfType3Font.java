package com.itextpdf.kernel.font;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.AdobeGlyphList;
import com.itextpdf.io.font.FontEncoding;
import com.itextpdf.io.font.FontMetrics;
import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.constants.FontDescriptorFlags;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfString;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/PdfType3Font.class */
public class PdfType3Font extends PdfSimpleFont<Type3Font> {
    private static final long serialVersionUID = 4940119184993066859L;
    private double[] fontMatrix;

    PdfType3Font(PdfDocument document, boolean colorized) {
        this.fontMatrix = DEFAULT_FONT_MATRIX;
        makeIndirect(document);
        this.subset = true;
        this.embedded = true;
        this.fontProgram = new Type3Font(colorized);
        this.fontEncoding = FontEncoding.createEmptyFontEncoding();
    }

    PdfType3Font(PdfDocument document, String fontName, String fontFamily, boolean colorized) {
        this(document, colorized);
        ((Type3Font) this.fontProgram).setFontName(fontName);
        ((Type3Font) this.fontProgram).setFontFamily(fontFamily);
    }

    PdfType3Font(PdfDictionary fontDictionary) {
        super(fontDictionary);
        this.fontMatrix = DEFAULT_FONT_MATRIX;
        this.subset = true;
        this.embedded = true;
        this.fontProgram = new Type3Font(false);
        this.fontEncoding = DocFontEncoding.createDocFontEncoding(fontDictionary.get(PdfName.Encoding), this.toUnicode);
        PdfDictionary charProcsDic = getPdfObject().getAsDictionary(PdfName.CharProcs);
        PdfArray fontMatrixArray = getPdfObject().getAsArray(PdfName.FontMatrix);
        if (getPdfObject().containsKey(PdfName.FontBBox)) {
            PdfArray fontBBox = getPdfObject().getAsArray(PdfName.FontBBox);
            this.fontProgram.getFontMetrics().setBbox(fontBBox.getAsNumber(0).intValue(), fontBBox.getAsNumber(1).intValue(), fontBBox.getAsNumber(2).intValue(), fontBBox.getAsNumber(3).intValue());
        } else {
            this.fontProgram.getFontMetrics().setBbox(0, 0, 0, 0);
        }
        int firstChar = normalizeFirstLastChar(fontDictionary.getAsNumber(PdfName.FirstChar), 0);
        int lastChar = normalizeFirstLastChar(fontDictionary.getAsNumber(PdfName.LastChar), 255);
        for (int i = firstChar; i <= lastChar; i++) {
            this.shortTag[i] = 1;
        }
        int[] widths = FontUtil.convertSimpleWidthsArray(fontDictionary.getAsArray(PdfName.Widths), firstChar, 0);
        double[] fontMatrix = new double[6];
        for (int i2 = 0; i2 < fontMatrixArray.size(); i2++) {
            fontMatrix[i2] = ((PdfNumber) fontMatrixArray.get(i2)).getValue();
        }
        setFontMatrix(fontMatrix);
        if (this.toUnicode != null && this.toUnicode.hasByteMappings() && this.fontEncoding.hasDifferences()) {
            for (int i3 = 0; i3 < 256; i3++) {
                int unicode = this.fontEncoding.getUnicode(i3);
                PdfName glyphName = new PdfName(this.fontEncoding.getDifference(i3));
                if (unicode != -1 && !FontEncoding.NOTDEF.equals(glyphName.getValue()) && charProcsDic.containsKey(glyphName)) {
                    ((Type3Font) getFontProgram()).addGlyph(i3, unicode, widths[i3], null, new Type3Glyph(charProcsDic.getAsStream(glyphName), getDocument()));
                }
            }
        }
        Map<Integer, Integer> unicodeToCode = null;
        if (this.toUnicode != null) {
            try {
                unicodeToCode = this.toUnicode.createReverseMapping();
            } catch (Exception e) {
            }
        }
        for (PdfName glyphName2 : charProcsDic.keySet()) {
            int unicode2 = AdobeGlyphList.nameToUnicode(glyphName2.getValue());
            int code = -1;
            if (this.fontEncoding.canEncode(unicode2)) {
                code = this.fontEncoding.convertToByte(unicode2);
            } else if (unicodeToCode != null && unicodeToCode.containsKey(Integer.valueOf(unicode2))) {
                code = unicodeToCode.get(Integer.valueOf(unicode2)).intValue();
            }
            if (code != -1 && getFontProgram().getGlyphByCode(code) == null) {
                ((Type3Font) getFontProgram()).addGlyph(code, unicode2, widths[code], null, new Type3Glyph(charProcsDic.getAsStream(glyphName2), getDocument()));
            }
        }
        fillFontDescriptor(fontDictionary.getAsDictionary(PdfName.FontDescriptor));
    }

    public void setFontName(String fontName) {
        ((Type3Font) this.fontProgram).setFontName(fontName);
    }

    public void setFontFamily(String fontFamily) {
        ((Type3Font) this.fontProgram).setFontFamily(fontFamily);
    }

    public void setFontWeight(int fontWeight) {
        ((Type3Font) this.fontProgram).setFontWeight(fontWeight);
    }

    public void setItalicAngle(int italicAngle) {
        ((Type3Font) this.fontProgram).setItalicAngle(italicAngle);
    }

    public void setFontStretch(String fontWidth) {
        ((Type3Font) this.fontProgram).setFontStretch(fontWidth);
    }

    public void setPdfFontFlags(int flags) {
        ((Type3Font) this.fontProgram).setPdfFontFlags(flags);
    }

    public Type3Glyph getType3Glyph(int unicode) {
        return ((Type3Font) getFontProgram()).getType3Glyph(unicode);
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean isSubset() {
        return true;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean isEmbedded() {
        return true;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public double[] getFontMatrix() {
        return this.fontMatrix;
    }

    public void setFontMatrix(double[] fontMatrix) {
        this.fontMatrix = fontMatrix;
    }

    public int getNumberOfGlyphs() {
        return ((Type3Font) getFontProgram()).getNumberOfGlyphs();
    }

    public Type3Glyph addGlyph(char c, int wx, int llx, int lly, int urx, int ury) {
        Type3Glyph glyph = getType3Glyph(c);
        if (glyph != null) {
            return glyph;
        }
        int code = getFirstEmptyCode();
        Type3Glyph glyph2 = new Type3Glyph(getDocument(), wx, llx, lly, urx, ury, ((Type3Font) getFontProgram()).isColorized());
        ((Type3Font) getFontProgram()).addGlyph(code, c, wx, new int[]{llx, lly, urx, ury}, glyph2);
        this.fontEncoding.addSymbol((byte) code, c);
        if (!((Type3Font) getFontProgram()).isColorized()) {
            if (this.fontProgram.countOfGlyphs() == 0) {
                this.fontProgram.getFontMetrics().setBbox(llx, lly, urx, ury);
            } else {
                int[] bbox = this.fontProgram.getFontMetrics().getBbox();
                int newLlx = Math.min(bbox[0], llx);
                int newLly = Math.min(bbox[1], lly);
                int newUrx = Math.max(bbox[2], urx);
                int newUry = Math.max(bbox[3], ury);
                this.fontProgram.getFontMetrics().setBbox(newLlx, newLly, newUrx, newUry);
            }
        }
        return glyph2;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public Glyph getGlyph(int unicode) {
        if (this.fontEncoding.canEncode(unicode) || unicode < 33) {
            Glyph glyph = getFontProgram().getGlyph(this.fontEncoding.getUnicodeDifference(unicode));
            if (glyph == null) {
                Glyph glyph2 = this.notdefGlyphs.get(Integer.valueOf(unicode));
                glyph = glyph2;
                if (glyph2 == null) {
                    glyph = new Glyph(-1, 0, unicode);
                    this.notdefGlyphs.put(Integer.valueOf(unicode), glyph);
                }
            }
            return glyph;
        }
        return null;
    }

    @Override // com.itextpdf.kernel.font.PdfFont
    public boolean containsGlyph(int unicode) {
        return (this.fontEncoding.canEncode(unicode) || unicode < 33) && getFontProgram().getGlyph(this.fontEncoding.getUnicodeDifference(unicode)) != null;
    }

    @Override // com.itextpdf.kernel.font.PdfFont, com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        Type3Glyph glyph;
        if (isFlushed()) {
            return;
        }
        ensureUnderlyingObjectHasIndirectReference();
        if (((Type3Font) getFontProgram()).getNumberOfGlyphs() < 1) {
            throw new PdfException("No glyphs defined for type3 font.");
        }
        PdfDictionary charProcs = new PdfDictionary();
        for (int i = 0; i < 256; i++) {
            if (this.fontEncoding.canDecode(i) && (glyph = getType3Glyph(this.fontEncoding.getUnicode(i))) != null) {
                charProcs.put(new PdfName(this.fontEncoding.getDifference(i)), glyph.getContentStream());
                glyph.getContentStream().flush();
            }
        }
        getPdfObject().put(PdfName.CharProcs, charProcs);
        getPdfObject().put(PdfName.FontMatrix, new PdfArray(getFontMatrix()));
        getPdfObject().put(PdfName.FontBBox, new PdfArray(this.fontProgram.getFontMetrics().getBbox()));
        String fontName = this.fontProgram.getFontNames().getFontName();
        super.flushFontData(fontName, PdfName.Type3);
        getPdfObject().remove(PdfName.BaseFont);
        super.flush();
    }

    @Override // com.itextpdf.kernel.font.PdfSimpleFont, com.itextpdf.kernel.font.PdfFont
    protected PdfDictionary getFontDescriptor(String fontName) {
        if (fontName != null && fontName.length() > 0) {
            PdfDictionary fontDescriptor = new PdfDictionary();
            makeObjectIndirect(fontDescriptor);
            fontDescriptor.put(PdfName.Type, PdfName.FontDescriptor);
            FontMetrics fontMetrics = this.fontProgram.getFontMetrics();
            fontDescriptor.put(PdfName.CapHeight, new PdfNumber(fontMetrics.getCapHeight()));
            fontDescriptor.put(PdfName.ItalicAngle, new PdfNumber(fontMetrics.getItalicAngle()));
            FontNames fontNames = this.fontProgram.getFontNames();
            fontDescriptor.put(PdfName.FontWeight, new PdfNumber(fontNames.getFontWeight()));
            fontDescriptor.put(PdfName.FontName, new PdfName(fontName));
            if (fontNames.getFamilyName() != null && fontNames.getFamilyName().length > 0 && fontNames.getFamilyName()[0].length >= 4) {
                fontDescriptor.put(PdfName.FontFamily, new PdfString(fontNames.getFamilyName()[0][3]));
            }
            int flags = this.fontProgram.getPdfFontFlags();
            fontDescriptor.put(PdfName.Flags, new PdfNumber((flags & ((FontDescriptorFlags.Symbolic | FontDescriptorFlags.Nonsymbolic) ^ (-1))) | (this.fontEncoding.isFontSpecific() ? FontDescriptorFlags.Symbolic : FontDescriptorFlags.Nonsymbolic)));
            return fontDescriptor;
        }
        if (((PdfDictionary) getPdfObject()).getIndirectReference() != null && ((PdfDictionary) getPdfObject()).getIndirectReference().getDocument().isTagged()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfType3Font.class);
            logger.warn(LogMessageConstant.TYPE3_FONT_ISSUE_TAGGED_PDF);
            return null;
        }
        return null;
    }

    @Override // com.itextpdf.kernel.font.PdfSimpleFont
    protected void addFontStream(PdfDictionary fontDescriptor) {
    }

    protected PdfDocument getDocument() {
        return getPdfObject().getIndirectReference().getDocument();
    }

    private int getFirstEmptyCode() {
        for (int i = 1; i < 256; i++) {
            if (!this.fontEncoding.canDecode(i)) {
                return i;
            }
        }
        return -1;
    }

    private void fillFontDescriptor(PdfDictionary fontDesc) {
        if (fontDesc == null) {
            return;
        }
        PdfNumber v = fontDesc.getAsNumber(PdfName.ItalicAngle);
        if (v != null) {
            setItalicAngle(v.intValue());
        }
        PdfNumber v2 = fontDesc.getAsNumber(PdfName.FontWeight);
        if (v2 != null) {
            setFontWeight(v2.intValue());
        }
        PdfName fontStretch = fontDesc.getAsName(PdfName.FontStretch);
        if (fontStretch != null) {
            setFontStretch(fontStretch.getValue());
        }
        PdfName fontName = fontDesc.getAsName(PdfName.FontName);
        if (fontName != null) {
            setFontName(fontName.getValue());
        }
        PdfString fontFamily = fontDesc.getAsString(PdfName.FontFamily);
        if (fontFamily != null) {
            setFontFamily(fontFamily.getValue());
        }
    }

    private int normalizeFirstLastChar(PdfNumber firstLast, int defaultValue) {
        if (firstLast == null) {
            return defaultValue;
        }
        int result = firstLast.intValue();
        return (result < 0 || result > 255) ? defaultValue : result;
    }
}
