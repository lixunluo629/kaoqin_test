package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/OutsetBorder.class */
public class OutsetBorder extends Border3D {
    public OutsetBorder(float width) {
        super(width);
    }

    public OutsetBorder(DeviceRgb color, float width) {
        super(color, width);
    }

    public OutsetBorder(DeviceCmyk color, float width) {
        super(color, width);
    }

    public OutsetBorder(DeviceGray color, float width) {
        super(color, width);
    }

    public OutsetBorder(DeviceRgb color, float width, float opacity) {
        super(color, width, opacity);
    }

    public OutsetBorder(DeviceCmyk color, float width, float opacity) {
        super(color, width, opacity);
    }

    public OutsetBorder(DeviceGray color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public int getType() {
        return 7;
    }

    @Override // com.itextpdf.layout.borders.Border3D
    protected void setInnerHalfColor(PdfCanvas canvas, Border.Side side) {
        switch (side) {
            case TOP:
            case LEFT:
                canvas.setFillColor(getColor());
                break;
            case BOTTOM:
            case RIGHT:
                canvas.setFillColor(getDarkerColor());
                break;
        }
    }

    @Override // com.itextpdf.layout.borders.Border3D
    protected void setOuterHalfColor(PdfCanvas canvas, Border.Side side) {
        switch (side) {
            case TOP:
            case LEFT:
                canvas.setFillColor(getColor());
                break;
            case BOTTOM:
            case RIGHT:
                canvas.setFillColor(getDarkerColor());
                break;
        }
    }
}
