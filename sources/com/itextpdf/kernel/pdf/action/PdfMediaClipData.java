package com.itextpdf.kernel.pdf.action;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/action/PdfMediaClipData.class */
public class PdfMediaClipData extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -7030377585169961523L;
    private static final PdfString TEMPACCESS = new PdfString("TEMPACCESS");

    public PdfMediaClipData(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfMediaClipData(String file, PdfFileSpec fs, String mimeType) {
        this(new PdfDictionary());
        PdfDictionary dic = new PdfDictionary();
        markObjectAsIndirect(dic);
        dic.put(PdfName.TF, TEMPACCESS);
        getPdfObject().put(PdfName.Type, PdfName.MediaClip);
        getPdfObject().put(PdfName.S, PdfName.MCD);
        getPdfObject().put(PdfName.N, new PdfString(MessageFormatUtil.format("Media clip for {0}", file)));
        getPdfObject().put(PdfName.CT, new PdfString(mimeType));
        getPdfObject().put(PdfName.P, dic);
        getPdfObject().put(PdfName.D, fs.getPdfObject());
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
