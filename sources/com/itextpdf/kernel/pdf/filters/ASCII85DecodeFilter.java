package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.MemoryLimitsAwareFilter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import java.io.ByteArrayOutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/ASCII85DecodeFilter.class */
public class ASCII85DecodeFilter extends MemoryLimitsAwareFilter {
    @Override // com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        ByteArrayOutputStream outputStream = enableMemoryLimitsAwareHandler(streamDictionary);
        return ASCII85Decode(b, outputStream);
    }

    public static byte[] ASCII85Decode(byte[] in) {
        return ASCII85Decode(in, new ByteArrayOutputStream());
    }

    private static byte[] ASCII85Decode(byte[] in, ByteArrayOutputStream out) {
        int ch2;
        int state = 0;
        int[] chn = new int[5];
        for (int k = 0; k < in.length && (ch2 = in[k] & 255) != 126; k++) {
            if (!PdfTokenizer.isWhitespace(ch2)) {
                if (ch2 == 122 && state == 0) {
                    out.write(0);
                    out.write(0);
                    out.write(0);
                    out.write(0);
                } else {
                    if (ch2 < 33 || ch2 > 117) {
                        throw new PdfException(PdfException.IllegalCharacterInAscii85decode);
                    }
                    chn[state] = ch2 - 33;
                    state++;
                    if (state == 5) {
                        state = 0;
                        int r = 0;
                        for (int j = 0; j < 5; j++) {
                            r = (r * 85) + chn[j];
                        }
                        out.write((byte) (r >> 24));
                        out.write((byte) (r >> 16));
                        out.write((byte) (r >> 8));
                        out.write((byte) r);
                    }
                }
            }
        }
        if (state == 2) {
            out.write((byte) (((((((((chn[0] * 85) * 85) * 85) * 85) + (((chn[1] * 85) * 85) * 85)) + 614125) + 7225) + 85) >> 24));
        } else if (state == 3) {
            int r2 = (chn[0] * 85 * 85 * 85 * 85) + (chn[1] * 85 * 85 * 85) + (chn[2] * 85 * 85) + 7225 + 85;
            out.write((byte) (r2 >> 24));
            out.write((byte) (r2 >> 16));
        } else if (state == 4) {
            int r3 = (chn[0] * 85 * 85 * 85 * 85) + (chn[1] * 85 * 85 * 85) + (chn[2] * 85 * 85) + (chn[3] * 85) + 85;
            out.write((byte) (r3 >> 24));
            out.write((byte) (r3 >> 16));
            out.write((byte) (r3 >> 8));
        }
        return out.toByteArray();
    }
}
