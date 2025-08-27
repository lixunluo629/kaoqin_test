package com.itextpdf.kernel.pdf.canvas;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/CanvasGraphicsState.class */
public class CanvasGraphicsState implements Serializable {
    private static final long serialVersionUID = -9151840268986283292L;
    private Matrix ctm;
    private Color strokeColor;
    private Color fillColor;
    private float charSpacing;
    private float wordSpacing;
    private float scale;
    private float leading;
    private PdfFont font;
    private float fontSize;
    private int textRenderingMode;
    private float textRise;
    private boolean textKnockout;
    private float lineWidth;
    private int lineCapStyle;
    private int lineJoinStyle;
    private float miterLimit;
    private PdfArray dashPattern;
    private PdfName renderingIntent;
    private boolean automaticStrokeAdjustment;
    private PdfObject blendMode;
    private PdfObject softMask;
    private float strokeAlpha;
    private float fillAlpha;
    private boolean alphaIsShape;
    private boolean strokeOverprint;
    private boolean fillOverprint;
    private int overprintMode;
    private PdfObject blackGenerationFunction;
    private PdfObject blackGenerationFunction2;
    private PdfObject underColorRemovalFunction;
    private PdfObject underColorRemovalFunction2;
    private PdfObject transferFunction;
    private PdfObject transferFunction2;
    private PdfObject halftone;
    private float flatnessTolerance;
    private Float smoothnessTolerance;
    private PdfObject htp;

    protected CanvasGraphicsState() {
        this.ctm = new Matrix();
        this.strokeColor = DeviceGray.BLACK;
        this.fillColor = DeviceGray.BLACK;
        this.charSpacing = 0.0f;
        this.wordSpacing = 0.0f;
        this.scale = 100.0f;
        this.leading = 0.0f;
        this.textRenderingMode = 0;
        this.textRise = 0.0f;
        this.textKnockout = true;
        this.lineWidth = 1.0f;
        this.lineCapStyle = 0;
        this.lineJoinStyle = 0;
        this.miterLimit = 10.0f;
        this.dashPattern = new PdfArray((List<? extends PdfObject>) Arrays.asList(new PdfArray(), new PdfNumber(0)));
        this.renderingIntent = PdfName.RelativeColorimetric;
        this.automaticStrokeAdjustment = false;
        this.blendMode = PdfName.Normal;
        this.softMask = PdfName.None;
        this.strokeAlpha = 1.0f;
        this.fillAlpha = 1.0f;
        this.alphaIsShape = false;
        this.strokeOverprint = false;
        this.fillOverprint = false;
        this.overprintMode = 0;
        this.flatnessTolerance = 1.0f;
    }

    public CanvasGraphicsState(CanvasGraphicsState source) {
        this.ctm = new Matrix();
        this.strokeColor = DeviceGray.BLACK;
        this.fillColor = DeviceGray.BLACK;
        this.charSpacing = 0.0f;
        this.wordSpacing = 0.0f;
        this.scale = 100.0f;
        this.leading = 0.0f;
        this.textRenderingMode = 0;
        this.textRise = 0.0f;
        this.textKnockout = true;
        this.lineWidth = 1.0f;
        this.lineCapStyle = 0;
        this.lineJoinStyle = 0;
        this.miterLimit = 10.0f;
        this.dashPattern = new PdfArray((List<? extends PdfObject>) Arrays.asList(new PdfArray(), new PdfNumber(0)));
        this.renderingIntent = PdfName.RelativeColorimetric;
        this.automaticStrokeAdjustment = false;
        this.blendMode = PdfName.Normal;
        this.softMask = PdfName.None;
        this.strokeAlpha = 1.0f;
        this.fillAlpha = 1.0f;
        this.alphaIsShape = false;
        this.strokeOverprint = false;
        this.fillOverprint = false;
        this.overprintMode = 0;
        this.flatnessTolerance = 1.0f;
        copyFrom(source);
    }

    public void updateFromExtGState(PdfDictionary extGState) {
        updateFromExtGState(new PdfExtGState(extGState), extGState.getIndirectReference() == null ? null : extGState.getIndirectReference().getDocument());
    }

    public Matrix getCtm() {
        return this.ctm;
    }

    public void updateCtm(float a, float b, float c, float d, float e, float f) {
        updateCtm(new Matrix(a, b, c, d, e, f));
    }

    public void updateCtm(Matrix newCtm) {
        this.ctm = newCtm.multiply(this.ctm);
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public float getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineCapStyle() {
        return this.lineCapStyle;
    }

    public void setLineCapStyle(int lineCapStyle) {
        this.lineCapStyle = lineCapStyle;
    }

    public int getLineJoinStyle() {
        return this.lineJoinStyle;
    }

    public void setLineJoinStyle(int lineJoinStyle) {
        this.lineJoinStyle = lineJoinStyle;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public void setMiterLimit(float miterLimit) {
        this.miterLimit = miterLimit;
    }

    public PdfArray getDashPattern() {
        return this.dashPattern;
    }

    public void setDashPattern(PdfArray dashPattern) {
        this.dashPattern = dashPattern;
    }

    public PdfName getRenderingIntent() {
        return this.renderingIntent;
    }

    public void setRenderingIntent(PdfName renderingIntent) {
        this.renderingIntent = renderingIntent;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public PdfFont getFont() {
        return this.font;
    }

    public void setFont(PdfFont font) {
        this.font = font;
    }

    public int getTextRenderingMode() {
        return this.textRenderingMode;
    }

    public void setTextRenderingMode(int textRenderingMode) {
        this.textRenderingMode = textRenderingMode;
    }

    public float getTextRise() {
        return this.textRise;
    }

    public void setTextRise(float textRise) {
        this.textRise = textRise;
    }

    public float getFlatnessTolerance() {
        return this.flatnessTolerance;
    }

    public void setFlatnessTolerance(float flatnessTolerance) {
        this.flatnessTolerance = flatnessTolerance;
    }

    public void setWordSpacing(float wordSpacing) {
        this.wordSpacing = wordSpacing;
    }

    public float getWordSpacing() {
        return this.wordSpacing;
    }

    public void setCharSpacing(float characterSpacing) {
        this.charSpacing = characterSpacing;
    }

    public float getCharSpacing() {
        return this.charSpacing;
    }

    public float getLeading() {
        return this.leading;
    }

    public void setLeading(float leading) {
        this.leading = leading;
    }

    public float getHorizontalScaling() {
        return this.scale;
    }

    public void setHorizontalScaling(float scale) {
        this.scale = scale;
    }

    public boolean getStrokeOverprint() {
        return this.strokeOverprint;
    }

    public boolean getFillOverprint() {
        return this.fillOverprint;
    }

    public int getOverprintMode() {
        return this.overprintMode;
    }

    public PdfObject getBlackGenerationFunction() {
        return this.blackGenerationFunction;
    }

    public PdfObject getBlackGenerationFunction2() {
        return this.blackGenerationFunction2;
    }

    public PdfObject getUnderColorRemovalFunction() {
        return this.underColorRemovalFunction;
    }

    public PdfObject getUnderColorRemovalFunction2() {
        return this.underColorRemovalFunction2;
    }

    public PdfObject getTransferFunction() {
        return this.transferFunction;
    }

    public PdfObject getTransferFunction2() {
        return this.transferFunction2;
    }

    public PdfObject getHalftone() {
        return this.halftone;
    }

    public Float getSmoothnessTolerance() {
        return this.smoothnessTolerance;
    }

    public boolean getAutomaticStrokeAdjustment() {
        return this.automaticStrokeAdjustment;
    }

    public PdfObject getBlendMode() {
        return this.blendMode;
    }

    public PdfObject getSoftMask() {
        return this.softMask;
    }

    public float getStrokeOpacity() {
        return this.strokeAlpha;
    }

    public float getFillOpacity() {
        return this.fillAlpha;
    }

    public boolean getAlphaIsShape() {
        return this.alphaIsShape;
    }

    public boolean getTextKnockout() {
        return this.textKnockout;
    }

    public PdfObject getHTP() {
        return this.htp;
    }

    public void updateFromExtGState(PdfExtGState extGState) {
        updateFromExtGState(extGState, null);
    }

    void updateFromExtGState(PdfExtGState extGState, PdfDocument pdfDocument) {
        Float lw = extGState.getLineWidth();
        if (lw != null) {
            this.lineWidth = lw.floatValue();
        }
        Integer lc = extGState.getLineCapStyle();
        if (lc != null) {
            this.lineCapStyle = lc.intValue();
        }
        Integer lj = extGState.getLineJoinStyle();
        if (lj != null) {
            this.lineJoinStyle = lj.intValue();
        }
        Float ml = extGState.getMiterLimit();
        if (ml != null) {
            this.miterLimit = ml.floatValue();
        }
        PdfArray d = extGState.getDashPattern();
        if (d != null) {
            this.dashPattern = d;
        }
        PdfName ri = extGState.getRenderingIntent();
        if (ri != null) {
            this.renderingIntent = ri;
        }
        Boolean op = extGState.getStrokeOverprintFlag();
        if (op != null) {
            this.strokeOverprint = op.booleanValue();
        }
        Boolean op2 = extGState.getFillOverprintFlag();
        if (op2 != null) {
            this.fillOverprint = op2.booleanValue();
        }
        Integer opm = extGState.getOverprintMode();
        if (opm != null) {
            this.overprintMode = opm.intValue();
        }
        PdfArray fnt = extGState.getFont();
        if (fnt != null) {
            PdfDictionary fontDictionary = fnt.getAsDictionary(0);
            if (this.font == null || this.font.getPdfObject() != fontDictionary) {
                this.font = pdfDocument.getFont(fontDictionary);
            }
            PdfNumber fntSz = fnt.getAsNumber(1);
            if (fntSz != null) {
                this.fontSize = fntSz.floatValue();
            }
        }
        PdfObject bg = extGState.getBlackGenerationFunction();
        if (bg != null) {
            this.blackGenerationFunction = bg;
        }
        PdfObject bg2 = extGState.getBlackGenerationFunction2();
        if (bg2 != null) {
            this.blackGenerationFunction2 = bg2;
        }
        PdfObject ucr = extGState.getUndercolorRemovalFunction();
        if (ucr != null) {
            this.underColorRemovalFunction = ucr;
        }
        PdfObject ucr2 = extGState.getUndercolorRemovalFunction2();
        if (ucr2 != null) {
            this.underColorRemovalFunction2 = ucr2;
        }
        PdfObject tr = extGState.getTransferFunction();
        if (tr != null) {
            this.transferFunction = tr;
        }
        PdfObject tr2 = extGState.getTransferFunction2();
        if (tr2 != null) {
            this.transferFunction2 = tr2;
        }
        PdfObject ht = extGState.getHalftone();
        if (ht != null) {
            this.halftone = ht;
        }
        PdfObject local_htp = extGState.getPdfObject().get(PdfName.HTP);
        if (local_htp != null) {
            this.htp = local_htp;
        }
        Float fl = extGState.getFlatnessTolerance();
        if (fl != null) {
            this.flatnessTolerance = fl.floatValue();
        }
        Float sm = extGState.getSmothnessTolerance();
        if (sm != null) {
            this.smoothnessTolerance = sm;
        }
        Boolean sa = extGState.getAutomaticStrokeAdjustmentFlag();
        if (sa != null) {
            this.automaticStrokeAdjustment = sa.booleanValue();
        }
        PdfObject bm = extGState.getBlendMode();
        if (bm != null) {
            this.blendMode = bm;
        }
        PdfObject sMask = extGState.getSoftMask();
        if (sMask != null) {
            this.softMask = sMask;
        }
        Float ca = extGState.getStrokeOpacity();
        if (ca != null) {
            this.strokeAlpha = ca.floatValue();
        }
        Float ca2 = extGState.getFillOpacity();
        if (ca2 != null) {
            this.fillAlpha = ca2.floatValue();
        }
        Boolean ais = extGState.getAlphaSourceFlag();
        if (ais != null) {
            this.alphaIsShape = ais.booleanValue();
        }
        Boolean tk2 = extGState.getTextKnockoutFlag();
        if (tk2 != null) {
            this.textKnockout = tk2.booleanValue();
        }
    }

    private void copyFrom(CanvasGraphicsState source) {
        this.ctm = source.ctm;
        this.strokeColor = source.strokeColor;
        this.fillColor = source.fillColor;
        this.charSpacing = source.charSpacing;
        this.wordSpacing = source.wordSpacing;
        this.scale = source.scale;
        this.leading = source.leading;
        this.font = source.font;
        this.fontSize = source.fontSize;
        this.textRenderingMode = source.textRenderingMode;
        this.textRise = source.textRise;
        this.textKnockout = source.textKnockout;
        this.lineWidth = source.lineWidth;
        this.lineCapStyle = source.lineCapStyle;
        this.lineJoinStyle = source.lineJoinStyle;
        this.miterLimit = source.miterLimit;
        this.dashPattern = source.dashPattern;
        this.renderingIntent = source.renderingIntent;
        this.automaticStrokeAdjustment = source.automaticStrokeAdjustment;
        this.blendMode = source.blendMode;
        this.softMask = source.softMask;
        this.strokeAlpha = source.strokeAlpha;
        this.fillAlpha = source.fillAlpha;
        this.alphaIsShape = source.alphaIsShape;
        this.strokeOverprint = source.strokeOverprint;
        this.fillOverprint = source.fillOverprint;
        this.overprintMode = source.overprintMode;
        this.blackGenerationFunction = source.blackGenerationFunction;
        this.blackGenerationFunction2 = source.blackGenerationFunction2;
        this.underColorRemovalFunction = source.underColorRemovalFunction;
        this.underColorRemovalFunction2 = source.underColorRemovalFunction2;
        this.transferFunction = source.transferFunction;
        this.transferFunction2 = source.transferFunction2;
        this.halftone = source.halftone;
        this.flatnessTolerance = source.flatnessTolerance;
        this.smoothnessTolerance = source.smoothnessTolerance;
        this.htp = source.htp;
    }
}
