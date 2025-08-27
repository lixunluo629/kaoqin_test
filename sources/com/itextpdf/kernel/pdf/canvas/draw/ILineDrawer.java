package com.itextpdf.kernel.pdf.canvas.draw;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/draw/ILineDrawer.class */
public interface ILineDrawer {
    void draw(PdfCanvas pdfCanvas, Rectangle rectangle);

    float getLineWidth();

    void setLineWidth(float f);

    Color getColor();

    void setColor(Color color);
}
