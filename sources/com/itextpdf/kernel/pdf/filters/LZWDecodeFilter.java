package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.kernel.pdf.MemoryLimitsAwareFilter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/LZWDecodeFilter.class */
public class LZWDecodeFilter extends MemoryLimitsAwareFilter {
    @Override // com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        ByteArrayOutputStream outputStream = enableMemoryLimitsAwareHandler(streamDictionary);
        return FlateDecodeFilter.decodePredictor(LZWDecode(b, outputStream), decodeParams);
    }

    public static byte[] LZWDecode(byte[] in) {
        return LZWDecode(in, new ByteArrayOutputStream());
    }

    private static byte[] LZWDecode(byte[] in, ByteArrayOutputStream out) throws IOException {
        LZWDecoder lzw = new LZWDecoder();
        lzw.decode(in, out);
        return out.toByteArray();
    }
}
