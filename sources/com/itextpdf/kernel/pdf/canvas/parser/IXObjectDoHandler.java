package com.itextpdf.kernel.pdf.canvas.parser;

import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.CanvasTag;
import java.util.Stack;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/IXObjectDoHandler.class */
public interface IXObjectDoHandler {
    void handleXObject(PdfCanvasProcessor pdfCanvasProcessor, Stack<CanvasTag> stack, PdfStream pdfStream, PdfName pdfName);
}
