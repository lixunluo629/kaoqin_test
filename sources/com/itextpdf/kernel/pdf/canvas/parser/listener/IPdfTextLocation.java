package com.itextpdf.kernel.pdf.canvas.parser.listener;

import com.itextpdf.kernel.geom.Rectangle;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/listener/IPdfTextLocation.class */
public interface IPdfTextLocation {
    Rectangle getRectangle();

    String getText();

    int getPageNumber();
}
