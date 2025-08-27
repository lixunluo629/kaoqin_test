package com.itextpdf.kernel.pdf.canvas.parser;

import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfObject;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/IContentOperator.class */
public interface IContentOperator {
    void invoke(PdfCanvasProcessor pdfCanvasProcessor, PdfLiteral pdfLiteral, List<PdfObject> list);
}
