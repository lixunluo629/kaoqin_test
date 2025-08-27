package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.io.codec.TIFFFaxDecoder;
import com.itextpdf.io.codec.TIFFFaxDecompressor;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/CCITTFaxDecodeFilter.class */
public class CCITTFaxDecodeFilter implements IFilterHandler {
    @Override // com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        PdfNumber wn = streamDictionary.getAsNumber(PdfName.Width);
        PdfNumber hn = streamDictionary.getAsNumber(PdfName.Height);
        if (wn == null || hn == null) {
            throw new PdfException(PdfException.FilterCcittfaxdecodeIsOnlySupportedForImages);
        }
        int width = wn.intValue();
        int height = hn.intValue();
        PdfDictionary param = decodeParams instanceof PdfDictionary ? (PdfDictionary) decodeParams : null;
        int k = 0;
        boolean blackIs1 = false;
        boolean byteAlign = false;
        if (param != null) {
            PdfNumber kn = param.getAsNumber(PdfName.K);
            if (kn != null) {
                k = kn.intValue();
            }
            PdfBoolean bo = param.getAsBoolean(PdfName.BlackIs1);
            if (bo != null) {
                blackIs1 = bo.getValue();
            }
            PdfBoolean bo2 = param.getAsBoolean(PdfName.EncodedByteAlign);
            if (bo2 != null) {
                byteAlign = bo2.getValue();
            }
        }
        byte[] outBuf = new byte[((width + 7) / 8) * height];
        TIFFFaxDecompressor decoder = new TIFFFaxDecompressor();
        if (k == 0 || k > 0) {
            int tiffT4Options = (k > 0 ? 1 : 0) | (byteAlign ? 4 : 0);
            decoder.SetOptions(1, 3, tiffT4Options, 0);
            decoder.decodeRaw(outBuf, b, width, height);
            if (decoder.fails > 0) {
                byte[] outBuf2 = new byte[((width + 7) / 8) * height];
                int oldFails = decoder.fails;
                decoder.SetOptions(1, 2, tiffT4Options, 0);
                decoder.decodeRaw(outBuf2, b, width, height);
                if (decoder.fails < oldFails) {
                    outBuf = outBuf2;
                }
            }
        } else {
            long tiffT6Options = 0 | (byteAlign ? 4L : 0L);
            TIFFFaxDecoder deca = new TIFFFaxDecoder(1, width, height);
            deca.decodeT6(outBuf, b, 0, height, tiffT6Options);
        }
        if (!blackIs1) {
            int len = outBuf.length;
            for (int t = 0; t < len; t++) {
                byte[] bArr = outBuf;
                int i = t;
                bArr[i] = (byte) (bArr[i] ^ 255);
            }
        }
        return outBuf;
    }
}
