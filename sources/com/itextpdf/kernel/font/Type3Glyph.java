package com.itextpdf.kernel.font;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.nio.charset.StandardCharsets;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/Type3Glyph.class */
public final class Type3Glyph extends PdfCanvas {
    private static final long serialVersionUID = 5811604071799271336L;
    private float wx;
    private float llx;
    private float lly;
    private float urx;
    private float ury;
    private boolean isColor;
    private static final String D_0_STR = "d0\n";
    private static final byte[] d0 = ByteUtils.getIsoBytes(D_0_STR);
    private static final String D_1_STR = "d1\n";
    private static final byte[] d1 = ByteUtils.getIsoBytes(D_1_STR);

    Type3Glyph(PdfDocument pdfDocument, float wx, float llx, float lly, float urx, float ury, boolean isColor) {
        super((PdfStream) new PdfStream().makeIndirect(pdfDocument), null, pdfDocument);
        this.isColor = false;
        writeMetrics(wx, llx, lly, urx, ury, isColor);
    }

    Type3Glyph(PdfStream pdfStream, PdfDocument document) {
        super(pdfStream, null, document);
        this.isColor = false;
        if (pdfStream.getBytes() != null) {
            fillBBFromBytes(pdfStream.getBytes());
        }
    }

    public float getWx() {
        return this.wx;
    }

    public float getLlx() {
        return this.llx;
    }

    public float getLly() {
        return this.lly;
    }

    public float getUrx() {
        return this.urx;
    }

    public float getUry() {
        return this.ury;
    }

    public boolean isColor() {
        return this.isColor;
    }

    private void writeMetrics(float wx, float llx, float lly, float urx, float ury, boolean isColor) {
        this.isColor = isColor;
        this.wx = wx;
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;
        if (isColor) {
            this.contentStream.getOutputStream().writeFloat(wx).writeSpace().writeFloat(0.0f).writeSpace().writeBytes(d0);
        } else {
            this.contentStream.getOutputStream().writeFloat(wx).writeSpace().writeFloat(0.0f).writeSpace().writeFloat(llx).writeSpace().writeFloat(lly).writeSpace().writeFloat(urx).writeSpace().writeFloat(ury).writeSpace().writeBytes(d1);
        }
    }

    @Override // com.itextpdf.kernel.pdf.canvas.PdfCanvas
    public PdfXObject addImage(ImageData image, float a, float b, float c, float d, float e, float f, boolean inlineImage) {
        if (!this.isColor && (!image.isMask() || (image.getBpc() != 1 && image.getBpc() <= 255))) {
            throw new PdfException("Not colorized type3 fonts accept only mask images.");
        }
        return super.addImage(image, a, b, c, d, e, f, inlineImage);
    }

    private void fillBBFromBytes(byte[] bytes) {
        String str = new String(bytes, StandardCharsets.ISO_8859_1);
        int d0Pos = str.indexOf(D_0_STR);
        int d1Pos = str.indexOf(D_1_STR);
        if (d0Pos != -1) {
            this.isColor = true;
            String[] bbArray = str.substring(0, d0Pos - 1).split(SymbolConstants.SPACE_SYMBOL);
            if (bbArray.length == 2) {
                this.wx = Float.parseFloat(bbArray[0]);
                return;
            }
            return;
        }
        if (d1Pos != -1) {
            this.isColor = false;
            String[] bbArray2 = str.substring(0, d1Pos - 1).split(SymbolConstants.SPACE_SYMBOL);
            if (bbArray2.length == 6) {
                this.wx = Float.parseFloat(bbArray2[0]);
                this.llx = Float.parseFloat(bbArray2[2]);
                this.lly = Float.parseFloat(bbArray2[3]);
                this.urx = Float.parseFloat(bbArray2[4]);
                this.ury = Float.parseFloat(bbArray2[5]);
            }
        }
    }
}
