package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/InsetBorder.class */
public class InsetBorder extends Border3D {
    public InsetBorder(float width) {
        super(width);
    }

    public InsetBorder(DeviceRgb color, float width) {
        super(color, width);
    }

    public InsetBorder(DeviceCmyk color, float width) {
        super(color, width);
    }

    public InsetBorder(DeviceGray color, float width) {
        super(color, width);
    }

    public InsetBorder(DeviceRgb color, float width, float opacity) {
        super(color, width, opacity);
    }

    public InsetBorder(DeviceCmyk color, float width, float opacity) {
        super(color, width, opacity);
    }

    public InsetBorder(DeviceGray color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public int getType() {
        return 6;
    }

    @Override // com.itextpdf.layout.borders.Border3D
    protected void setInnerHalfColor(PdfCanvas canvas, Border.Side side) {
        switch (side) {
            case TOP:
            case LEFT:
                canvas.setFillColor(getDarkerColor());
                break;
            case BOTTOM:
            case RIGHT:
                canvas.setFillColor(getColor());
                break;
        }
    }

    @Override // com.itextpdf.layout.borders.Border3D
    protected void setOuterHalfColor(PdfCanvas canvas, Border.Side side) {
        switch (side) {
            case TOP:
            case LEFT:
                canvas.setFillColor(getDarkerColor());
                break;
            case BOTTOM:
            case RIGHT:
                canvas.setFillColor(getColor());
                break;
        }
    }
}
