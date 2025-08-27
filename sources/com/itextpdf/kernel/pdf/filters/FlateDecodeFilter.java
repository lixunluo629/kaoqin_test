package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.MemoryLimitsAwareException;
import com.itextpdf.kernel.pdf.MemoryLimitsAwareFilter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/FlateDecodeFilter.class */
public class FlateDecodeFilter extends MemoryLimitsAwareFilter {

    @Deprecated
    private boolean strictDecoding;

    public FlateDecodeFilter() {
        this(false);
    }

    @Deprecated
    public FlateDecodeFilter(boolean strictDecoding) {
        this.strictDecoding = false;
        this.strictDecoding = strictDecoding;
    }

    @Deprecated
    public boolean isStrictDecoding() {
        return this.strictDecoding;
    }

    @Deprecated
    public FlateDecodeFilter setStrictDecoding(boolean strict) {
        this.strictDecoding = strict;
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
        ByteArrayOutputStream outputStream = enableMemoryLimitsAwareHandler(streamDictionary);
        byte[] res = flateDecode(b, true, outputStream);
        if (res == null && !this.strictDecoding) {
            outputStream.reset();
            res = flateDecode(b, false, outputStream);
        }
        return decodePredictor(res, decodeParams);
    }

    public static byte[] flateDecode(byte[] in, boolean strict) {
        return flateDecode(in, strict, new ByteArrayOutputStream());
    }

    private static byte[] flateDecode(byte[] in, boolean strict, ByteArrayOutputStream out) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(in);
        InflaterInputStream zip = new InflaterInputStream(stream);
        byte[] b = new byte[strict ? 4092 : 1];
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
                if (strict) {
                    return null;
                }
                return out.toByteArray();
            }
        }
    }

    public static byte[] decodePredictor(byte[] in, PdfObject decodeParams) {
        int i;
        if (decodeParams == null || decodeParams.getType() != 3) {
            return in;
        }
        PdfDictionary dic = (PdfDictionary) decodeParams;
        PdfObject obj = dic.get(PdfName.Predictor);
        if (obj == null || obj.getType() != 8) {
            return in;
        }
        int predictor = ((PdfNumber) obj).intValue();
        if (predictor < 10 && predictor != 2) {
            return in;
        }
        int width = 1;
        PdfObject obj2 = dic.get(PdfName.Columns);
        if (obj2 != null && obj2.getType() == 8) {
            width = ((PdfNumber) obj2).intValue();
        }
        int colors = 1;
        PdfObject obj3 = dic.get(PdfName.Colors);
        if (obj3 != null && obj3.getType() == 8) {
            colors = ((PdfNumber) obj3).intValue();
        }
        int bpc = 8;
        PdfObject obj4 = dic.get(PdfName.BitsPerComponent);
        if (obj4 != null && obj4.getType() == 8) {
            bpc = ((PdfNumber) obj4).intValue();
        }
        DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(in));
        ByteArrayOutputStream fout = new ByteArrayOutputStream(in.length);
        int bytesPerPixel = (colors * bpc) / 8;
        int bytesPerRow = (((colors * width) * bpc) + 7) / 8;
        byte[] curr = new byte[bytesPerRow];
        byte[] prior = new byte[bytesPerRow];
        if (predictor == 2) {
            if (bpc == 8) {
                int numRows = in.length / bytesPerRow;
                for (int row = 0; row < numRows; row++) {
                    int rowStart = row * bytesPerRow;
                    for (int col = bytesPerPixel; col < bytesPerRow; col++) {
                        in[rowStart + col] = (byte) (in[rowStart + col] + in[(rowStart + col) - bytesPerPixel]);
                    }
                }
            }
            return in;
        }
        while (true) {
            try {
                int filter = dataStream.read();
                if (filter < 0) {
                    return fout.toByteArray();
                }
                dataStream.readFully(curr, 0, bytesPerRow);
                switch (filter) {
                    case 0:
                        break;
                    case 1:
                        for (int i2 = bytesPerPixel; i2 < bytesPerRow; i2++) {
                            byte[] bArr = curr;
                            int i3 = i2;
                            bArr[i3] = (byte) (bArr[i3] + curr[i2 - bytesPerPixel]);
                        }
                    case 2:
                        for (int i4 = 0; i4 < bytesPerRow; i4++) {
                            byte[] bArr2 = curr;
                            int i5 = i4;
                            bArr2[i5] = (byte) (bArr2[i5] + prior[i4]);
                        }
                    case 3:
                        for (int i6 = 0; i6 < bytesPerPixel; i6++) {
                            byte[] bArr3 = curr;
                            int i7 = i6;
                            bArr3[i7] = (byte) (bArr3[i7] + ((byte) (prior[i6] / 2)));
                        }
                        for (int i8 = bytesPerPixel; i8 < bytesPerRow; i8++) {
                            byte[] bArr4 = curr;
                            int i9 = i8;
                            bArr4[i9] = (byte) (bArr4[i9] + ((byte) (((curr[i8 - bytesPerPixel] & 255) + (prior[i8] & 255)) / 2)));
                        }
                    case 4:
                        for (int i10 = 0; i10 < bytesPerPixel; i10++) {
                            byte[] bArr5 = curr;
                            int i11 = i10;
                            bArr5[i11] = (byte) (bArr5[i11] + prior[i10]);
                        }
                        for (int i12 = bytesPerPixel; i12 < bytesPerRow; i12++) {
                            int a = curr[i12 - bytesPerPixel] & 255;
                            int b = prior[i12] & 255;
                            int c = prior[i12 - bytesPerPixel] & 255;
                            int p = (a + b) - c;
                            int pa = Math.abs(p - a);
                            int pb = Math.abs(p - b);
                            int pc = Math.abs(p - c);
                            if (pa <= pb && pa <= pc) {
                                i = a;
                            } else if (pb <= pc) {
                                i = b;
                            } else {
                                i = c;
                            }
                            int ret = i;
                            byte[] bArr6 = curr;
                            int i13 = i12;
                            bArr6[i13] = (byte) (bArr6[i13] + ((byte) ret));
                        }
                        break;
                    default:
                        throw new PdfException(PdfException.PngFilterUnknown);
                }
                try {
                    fout.write(curr);
                } catch (IOException e) {
                }
                byte[] tmp = prior;
                prior = curr;
                curr = tmp;
            } catch (Exception e2) {
                return fout.toByteArray();
            }
        }
    }
}
