package com.itextpdf.layout.borders;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/borders/GrooveBorder.class */
public class GrooveBorder extends Border3D {
    public GrooveBorder(float width) {
        super(width);
    }

    public GrooveBorder(DeviceRgb color, float width) {
        super(color, width);
    }

    public GrooveBorder(DeviceCmyk color, float width) {
        super(color, width);
    }

    public GrooveBorder(DeviceGray color, float width) {
        super(color, width);
    }

    public GrooveBorder(DeviceRgb color, float width, float opacity) {
        super(color, width, opacity);
    }

    public GrooveBorder(DeviceCmyk color, float width, float opacity) {
        super(color, width, opacity);
    }

    public GrooveBorder(DeviceGray color, float width, float opacity) {
        super(color, width, opacity);
    }

    @Override // com.itextpdf.layout.borders.Border
    public int getType() {
        return 5;
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
                canvas.setFillColor(getDarkerColor());
                break;
            case BOTTOM:
            case RIGHT:
                canvas.setFillColor(getColor());
                break;
        }
    }
}
