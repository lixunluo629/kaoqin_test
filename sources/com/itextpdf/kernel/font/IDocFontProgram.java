package com.itextpdf.kernel.font;

import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/IDocFontProgram.class */
interface IDocFontProgram {
    PdfStream getFontFile();

    PdfName getFontFileName();

    PdfName getSubtype();
}
