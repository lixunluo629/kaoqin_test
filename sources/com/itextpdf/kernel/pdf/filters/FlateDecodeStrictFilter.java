package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.kernel.pdf.MemoryLimitsAwareException;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/FlateDecodeStrictFilter.class */
public class FlateDecodeStrictFilter extends FlateDecodeFilter {
    @Override // com.itextpdf.kernel.pdf.filters.FlateDecodeFilter, com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
        ByteArrayOutputStream outputStream = enableMemoryLimitsAwareHandler(streamDictionary);
        byte[] res = flateDecode(b, outputStream);
        byte[] b2 = decodePredictor(res, decodeParams);
        return b2;
    }

    private static byte[] flateDecode(byte[] in, ByteArrayOutputStream out) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(in);
        InflaterInputStream zip = new InflaterInputStream(stream);
        byte[] b = new byte[4092];
        while (true) {
            try {
                int n = zip.read(b);
                if (n >= 0) {
                    out.write(b, 0, n);
                } else {
                    zip.close();
                    out.close();
                    return out.toByteArray();
                }
            } catch (MemoryLimitsAwareException e) {
                throw e;
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
