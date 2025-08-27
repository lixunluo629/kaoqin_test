package com.itextpdf.kernel.pdf.xobject;

import com.itextpdf.io.codec.PngWriter;
import com.itextpdf.io.codec.TiffWriter;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/xobject/ImagePdfBytesInfo.class */
class ImagePdfBytesInfo {
    private int pngBitDepth;
    private int bpc;
    private int width;
    private int height;
    private PdfObject colorspace;
    private PdfArray decode;
    private int pngColorType = -1;
    private byte[] palette = null;
    private byte[] icc = null;
    private int stride = 0;

    public ImagePdfBytesInfo(PdfImageXObject imageXObject) {
        this.bpc = imageXObject.getPdfObject().getAsNumber(PdfName.BitsPerComponent).intValue();
        this.pngBitDepth = this.bpc;
        this.width = (int) imageXObject.getWidth();
        this.height = (int) imageXObject.getHeight();
        this.colorspace = imageXObject.getPdfObject().get(PdfName.ColorSpace);
        this.decode = imageXObject.getPdfObject().getAsArray(PdfName.Decode);
        findColorspace(this.colorspace, true);
    }

    public int getPngColorType() {
        return this.pngColorType;
    }

    public byte[] decodeTiffAndPngBytes(byte[] imageBytes) throws IOException {
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        if (this.pngColorType < 0) {
            if (this.bpc != 8) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.ColorDepthIsNotSupported).setMessageParams(Integer.valueOf(this.bpc));
            }
            if (this.colorspace instanceof PdfArray) {
                PdfArray ca = (PdfArray) this.colorspace;
                PdfObject tyca = ca.get(0);
                if (!PdfName.ICCBased.equals(tyca)) {
                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.ColorSpaceIsNotSupported).setMessageParams(tyca.toString());
                }
                PdfStream pr = (PdfStream) ca.get(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n != 4) {
                    throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.NValueIsNotSupported).setMessageParams(Integer.valueOf(n));
                }
                this.icc = pr.getBytes();
            } else if (!PdfName.DeviceCMYK.equals(this.colorspace)) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.ColorSpaceIsNotSupported).setMessageParams(this.colorspace.toString());
            }
            this.stride = 4 * this.width;
            TiffWriter wr = new TiffWriter();
            wr.addField(new TiffWriter.FieldShort(277, 4));
            wr.addField(new TiffWriter.FieldShort(258, new int[]{8, 8, 8, 8}));
            wr.addField(new TiffWriter.FieldShort(262, 5));
            wr.addField(new TiffWriter.FieldLong(256, this.width));
            wr.addField(new TiffWriter.FieldLong(257, this.height));
            wr.addField(new TiffWriter.FieldShort(259, 5));
            wr.addField(new TiffWriter.FieldShort(317, 2));
            wr.addField(new TiffWriter.FieldLong(278, this.height));
            wr.addField(new TiffWriter.FieldRational(282, new int[]{300, 1}));
            wr.addField(new TiffWriter.FieldRational(283, new int[]{300, 1}));
            wr.addField(new TiffWriter.FieldShort(296, 2));
            wr.addField(new TiffWriter.FieldAscii(305, Version.getInstance().getVersion()));
            ByteArrayOutputStream comp = new ByteArrayOutputStream();
            TiffWriter.compressLZW(comp, 2, imageBytes, this.height, 4, this.stride);
            byte[] buf = comp.toByteArray();
            wr.addField(new TiffWriter.FieldImage(buf));
            wr.addField(new TiffWriter.FieldLong(279, buf.length));
            if (this.icc != null) {
                wr.addField(new TiffWriter.FieldUndefined(34675, this.icc));
            }
            wr.writeFile(ms);
            return ms.toByteArray();
        }
        PngWriter png = new PngWriter(ms);
        if (this.decode != null && this.pngBitDepth == 1 && this.decode.getAsNumber(0).intValue() == 1 && this.decode.getAsNumber(1).intValue() == 0) {
            int len = imageBytes.length;
            for (int t = 0; t < len; t++) {
                int i = t;
                imageBytes[i] = (byte) (imageBytes[i] ^ 255);
            }
        }
        png.writeHeader(this.width, this.height, this.pngBitDepth, this.pngColorType);
        if (this.icc != null) {
            png.writeIccProfile(this.icc);
        }
        if (this.palette != null) {
            png.writePalette(this.palette);
        }
        png.writeData(imageBytes, this.stride);
        png.writeEnd();
        return ms.toByteArray();
    }

    private void findColorspace(PdfObject csObj, boolean allowIndexed) {
        if (PdfName.DeviceGray.equals(csObj) || (csObj == null && this.bpc == 1)) {
            this.stride = ((this.width * this.bpc) + 7) / 8;
            this.pngColorType = 0;
            return;
        }
        if (PdfName.DeviceRGB.equals(csObj)) {
            if (this.bpc == 8 || this.bpc == 16) {
                this.stride = (((this.width * this.bpc) * 3) + 7) / 8;
                this.pngColorType = 2;
                return;
            }
            return;
        }
        if (csObj instanceof PdfArray) {
            PdfArray ca = (PdfArray) csObj;
            PdfObject tyca = ca.get(0);
            if (PdfName.CalGray.equals(tyca)) {
                this.stride = ((this.width * this.bpc) + 7) / 8;
                this.pngColorType = 0;
                return;
            }
            if (PdfName.CalRGB.equals(tyca)) {
                if (this.bpc == 8 || this.bpc == 16) {
                    this.stride = (((this.width * this.bpc) * 3) + 7) / 8;
                    this.pngColorType = 2;
                    return;
                }
                return;
            }
            if (PdfName.ICCBased.equals(tyca)) {
                PdfStream pr = (PdfStream) ca.get(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n == 1) {
                    this.stride = ((this.width * this.bpc) + 7) / 8;
                    this.pngColorType = 0;
                    this.icc = pr.getBytes();
                    return;
                } else {
                    if (n == 3) {
                        this.stride = (((this.width * this.bpc) * 3) + 7) / 8;
                        this.pngColorType = 2;
                        this.icc = pr.getBytes();
                        return;
                    }
                    return;
                }
            }
            if (allowIndexed && PdfName.Indexed.equals(tyca)) {
                findColorspace(ca.get(1), false);
                if (this.pngColorType == 2) {
                    PdfObject id2 = ca.get(3);
                    if (id2 instanceof PdfString) {
                        this.palette = ((PdfString) id2).getValueBytes();
                    } else if (id2 instanceof PdfStream) {
                        this.palette = ((PdfStream) id2).getBytes();
                    }
                    this.stride = ((this.width * this.bpc) + 7) / 8;
                    this.pngColorType = 3;
                }
            }
        }
    }
}
