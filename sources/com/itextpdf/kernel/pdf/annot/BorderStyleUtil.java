package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/BorderStyleUtil.class */
class BorderStyleUtil {
    private BorderStyleUtil() {
    }

    public static final PdfDictionary setStyle(PdfDictionary bs, PdfName style) {
        if (null == bs) {
            bs = new PdfDictionary();
        }
        bs.put(PdfName.S, style);
        return bs;
    }

    public static final PdfDictionary setDashPattern(PdfDictionary bs, PdfArray dashPattern) {
        if (null == bs) {
            bs = new PdfDictionary();
        }
        bs.put(PdfName.D, dashPattern);
        return bs;
    }
}
