package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;
import org.aspectj.apache.bcel.Constants;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/Border3D.class */
public abstract class Border3D extends Border {
    private static final DeviceRgb GRAY = new DeviceRgb(Constants.GETSTATIC2_QUICK, 208, 200);

    protected abstract void setInnerHalfColor(PdfCanvas pdfCanvas, Border.Side side);

    protected abstract void setOuterHalfColor(PdfCanvas pdfCanvas, Border.Side side);

    protected Border3D(float width) {
        this(GRAY, width);
    }

    protected Border3D(DeviceRgb color, float width) {
        super(color, width);
    }

    protected Border3D(DeviceCmyk color, float width) {
        super(color, width);
    }

    protected Border3D(DeviceGray color, float width) {
        super(color, width);
    }

    protected Border3D(DeviceRgb color, float width, float opacity) {
        super(color, width, opacity);
    }

    protected Border3D(DeviceCmyk color, float width, float opacity) {
        super(color, width, opacity);
    }

    protected Border3D(DeviceGray color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide, float borderWidthBefore, float borderWidthAfter) {
        float x3 = 0.0f;
        float y3 = 0.0f;
        float x4 = 0.0f;
        float y4 = 0.0f;
        float widthHalf = this.width / 2.0f;
        float halfOfWidthBefore = borderWidthBefore / 2.0f;
        float halfOfWidthAfter = borderWidthAfter / 2.0f;
        Border.Side borderSide = getBorderSide(x1, y1, x2, y2, defaultSide);
        switch (borderSide) {
            case TOP:
                x3 = x2 + halfOfWidthAfter;
                y3 = y2 + widthHalf;
                x4 = x1 - halfOfWidthBefore;
                y4 = y1 + widthHalf;
                break;
            case RIGHT:
                x3 = x2 + widthHalf;
                y3 = y2 - halfOfWidthAfter;
                x4 = x1 + widthHalf;
                y4 = y1 + halfOfWidthBefore;
                break;
            case BOTTOM:
                x3 = x2 - halfOfWidthAfter;
                y3 = y2 - widthHalf;
                x4 = x1 + halfOfWidthBefore;
                y4 = y1 - widthHalf;
                break;
            case LEFT:
                x3 = x2 - widthHalf;
                y3 = y2 + halfOfWidthAfter;
                x4 = x1 - widthHalf;
                y4 = y1 - halfOfWidthBefore;
                break;
        }
        canvas.saveState();
        this.transparentColor.applyFillTransparency(canvas);
        setInnerHalfColor(canvas, borderSide);
        canvas.moveTo(x1, y1).lineTo(x2, y2).lineTo(x3, y3).lineTo(x4, y4).lineTo(x1, y1).fill();
        switch (borderSide) {
            case TOP:
                x2 += borderWidthAfter;
                y2 += this.width;
                x1 -= borderWidthBefore;
                y1 += this.width;
                break;
            case RIGHT:
                x2 += this.width;
                y2 -= borderWidthAfter;
                x1 += this.width;
                y1 += borderWidthBefore;
                break;
            case BOTTOM:
                x2 -= borderWidthAfter;
                y2 -= this.width;
                x1 += borderWidthBefore;
                y1 -= this.width;
                break;
            case LEFT:
                x2 -= this.width;
                y2 += borderWidthAfter;
                x1 -= this.width;
                y1 -= borderWidthBefore;
                break;
        }
        setOuterHalfColor(canvas, borderSide);
        canvas.moveTo(x1, y1).lineTo(x2, y2).lineTo(x3, y3).lineTo(x4, y4).lineTo(x1, y1).fill();
        canvas.restoreState();
    }

    @Override // com.itextpdf.layout.borders.Border
    public void drawCellBorder(PdfCanvas canvas, float x1, float y1, float x2, float y2, Border.Side defaultSide) {
        canvas.saveState().setStrokeColor(this.transparentColor.getColor());
        this.transparentColor.applyStrokeTransparency(canvas);
        canvas.setLineWidth(this.width).moveTo(x1, y1).lineTo(x2, y2).stroke().restoreState();
    }

    protected Color getDarkerColor() {
        Color color = this.transparentColor.getColor();
        if (color instanceof DeviceRgb) {
            return DeviceRgb.makeDarker((DeviceRgb) color);
        }
        if (color instanceof DeviceCmyk) {
            return DeviceCmyk.makeDarker((DeviceCmyk) color);
        }
        if (color instanceof DeviceGray) {
            return DeviceGray.makeDarker((DeviceGray) color);
        }
        return color;
    }
}
