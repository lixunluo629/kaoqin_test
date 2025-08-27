package com.itextpdf.kernel.colors;

import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/Indexed.class */
public class Indexed extends Color {
    private static final long serialVersionUID = 5374740389023596345L;

    public Indexed(PdfColorSpace colorSpace) {
        this(colorSpace, 0);
    }

    public Indexed(PdfColorSpace colorSpace, int colorValue) {
        super(colorSpace, new float[]{colorValue});
    }
}
