package com.itextpdf.kernel.colors;

import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/PatternColor.class */
public class PatternColor extends Color {
    private static final long serialVersionUID = -2405470180325720440L;
    private PdfPattern pattern;
    private Color underlyingColor;

    public PatternColor(PdfPattern coloredPattern) {
        super(new PdfSpecialCs.Pattern(), null);
        this.pattern = coloredPattern;
    }

    public PatternColor(PdfPattern.Tiling uncoloredPattern, Color color) {
        this(uncoloredPattern, color.getColorSpace(), color.getColorValue());
    }

    public PatternColor(PdfPattern.Tiling uncoloredPattern, PdfColorSpace underlyingCS, float[] colorValue) {
        this(uncoloredPattern, new PdfSpecialCs.UncoloredTilingPattern(ensureNotPatternCs(underlyingCS)), colorValue);
    }

    public PatternColor(PdfPattern.Tiling uncoloredPattern, PdfSpecialCs.UncoloredTilingPattern uncoloredTilingCS, float[] colorValue) {
        super(uncoloredTilingCS, colorValue);
        this.pattern = uncoloredPattern;
        this.underlyingColor = makeColor(uncoloredTilingCS.getUnderlyingColorSpace(), colorValue);
    }

    public PdfPattern getPattern() {
        return this.pattern;
    }

    @Override // com.itextpdf.kernel.colors.Color
    public void setColorValue(float[] value) {
        super.setColorValue(value);
        this.underlyingColor.setColorValue(value);
    }

    @Deprecated
    public void setPattern(PdfPattern pattern) {
        this.pattern = pattern;
    }

    @Override // com.itextpdf.kernel.colors.Color
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        PatternColor color = (PatternColor) o;
        return this.pattern.equals(color.pattern) && (this.underlyingColor == null ? color.underlyingColor == null : this.underlyingColor.equals(color.underlyingColor));
    }

    private static PdfColorSpace ensureNotPatternCs(PdfColorSpace underlyingCS) {
        if (underlyingCS instanceof PdfSpecialCs.Pattern) {
            throw new IllegalArgumentException("underlyingCS");
        }
        return underlyingCS;
    }
}
