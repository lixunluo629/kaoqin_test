package com.itextpdf.kernel.pdf.action;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/action/PdfRendition.class */
public class PdfRendition extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -726500192326824100L;

    public PdfRendition(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfRendition(String file, PdfFileSpec fs, String mimeType) {
        this(new PdfDictionary());
        getPdfObject().put(PdfName.S, PdfName.MR);
        getPdfObject().put(PdfName.N, new PdfString(MessageFormatUtil.format("Rendition for {0}", file)));
        getPdfObject().put(PdfName.C, new PdfMediaClipData(file, fs, mimeType).getPdfObject());
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }
}
