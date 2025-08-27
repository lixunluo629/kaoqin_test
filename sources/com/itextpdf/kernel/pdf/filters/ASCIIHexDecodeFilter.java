package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.MemoryLimitsAwareFilter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import java.io.ByteArrayOutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/ASCIIHexDecodeFilter.class */
public class ASCIIHexDecodeFilter extends MemoryLimitsAwareFilter {
    @Override // com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        ByteArrayOutputStream outputStream = enableMemoryLimitsAwareHandler(streamDictionary);
        return ASCIIHexDecode(b, outputStream);
    }

    public static byte[] ASCIIHexDecode(byte[] in) {
        return ASCIIHexDecode(in, new ByteArrayOutputStream());
    }

    private static byte[] ASCIIHexDecode(byte[] in, ByteArrayOutputStream out) {
        int ch2;
        boolean first = true;
        int n1 = 0;
        for (int k = 0; k < in.length && (ch2 = in[k] & 255) != 62; k++) {
            if (!PdfTokenizer.isWhitespace(ch2)) {
                int n = ByteBuffer.getHex(ch2);
                if (n == -1) {
                    throw new PdfException(PdfException.IllegalCharacterInAsciihexdecode);
                }
                if (first) {
                    n1 = n;
                } else {
                    out.write((byte) ((n1 << 4) + n));
                }
                first = !first;
            }
        }
        if (!first) {
            out.write((byte) (n1 << 4));
        }
        return out.toByteArray();
    }
}
