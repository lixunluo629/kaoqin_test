package com.itextpdf.kernel.pdf.colorspace;

import com.itextpdf.io.colors.IccProfile;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfStream;
import java.io.InputStream;
import java.util.ArrayList;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfCieBasedCs.class */
public abstract class PdfCieBasedCs extends PdfColorSpace {
    private static final long serialVersionUID = 7803780450619297557L;

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    protected PdfCieBasedCs(PdfArray pdfObject) {
        super(pdfObject);
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfCieBasedCs$CalGray.class */
    public static class CalGray extends PdfCieBasedCs {
        private static final long serialVersionUID = -3974274460820215173L;

        public CalGray(PdfArray pdfObject) {
            super(pdfObject);
        }

        public CalGray(float[] whitePoint) {
            this(getInitialPdfArray());
            if (whitePoint == null || whitePoint.length != 3) {
                throw new PdfException(PdfException.WhitePointIsIncorrectlySpecified, this);
            }
            PdfDictionary d = ((PdfArray) getPdfObject()).getAsDictionary(1);
            d.put(PdfName.WhitePoint, new PdfArray(whitePoint));
        }

        public CalGray(float[] whitePoint, float[] blackPoint, float gamma) {
            this(whitePoint);
            PdfDictionary d = ((PdfArray) getPdfObject()).getAsDictionary(1);
            if (blackPoint != null) {
                d.put(PdfName.BlackPoint, new PdfArray(blackPoint));
            }
            if (gamma != Float.NaN) {
                d.put(PdfName.Gamma, new PdfNumber(gamma));
            }
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 1;
        }

        private static PdfArray getInitialPdfArray() {
            ArrayList<PdfObject> tempArray = new ArrayList<>(2);
            tempArray.add(PdfName.CalGray);
            tempArray.add(new PdfDictionary());
            return new PdfArray(tempArray);
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfCieBasedCs$CalRgb.class */
    public static class CalRgb extends PdfCieBasedCs {
        private static final long serialVersionUID = -2926074370411556426L;

        public CalRgb(PdfArray pdfObject) {
            super(pdfObject);
        }

        public CalRgb(float[] whitePoint) {
            this(getInitialPdfArray());
            if (whitePoint == null || whitePoint.length != 3) {
                throw new PdfException(PdfException.WhitePointIsIncorrectlySpecified, this);
            }
            PdfDictionary d = ((PdfArray) getPdfObject()).getAsDictionary(1);
            d.put(PdfName.WhitePoint, new PdfArray(whitePoint));
        }

        public CalRgb(float[] whitePoint, float[] blackPoint, float[] gamma, float[] matrix) {
            this(whitePoint);
            PdfDictionary d = ((PdfArray) getPdfObject()).getAsDictionary(1);
            if (blackPoint != null) {
                d.put(PdfName.BlackPoint, new PdfArray(blackPoint));
            }
            if (gamma != null) {
                d.put(PdfName.Gamma, new PdfArray(gamma));
            }
            if (matrix != null) {
                d.put(PdfName.Matrix, new PdfArray(matrix));
            }
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 3;
        }

        private static PdfArray getInitialPdfArray() {
            ArrayList<PdfObject> tempArray = new ArrayList<>(2);
            tempArray.add(PdfName.CalRGB);
            tempArray.add(new PdfDictionary());
            return new PdfArray(tempArray);
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfCieBasedCs$Lab.class */
    public static class Lab extends PdfCieBasedCs {
        private static final long serialVersionUID = 7067722970343880433L;

        public Lab(PdfArray pdfObject) {
            super(pdfObject);
        }

        public Lab(float[] whitePoint) {
            this(getInitialPdfArray());
            if (whitePoint == null || whitePoint.length != 3) {
                throw new PdfException(PdfException.WhitePointIsIncorrectlySpecified, this);
            }
            PdfDictionary d = ((PdfArray) getPdfObject()).getAsDictionary(1);
            d.put(PdfName.WhitePoint, new PdfArray(whitePoint));
        }

        public Lab(float[] whitePoint, float[] blackPoint, float[] range) {
            this(whitePoint);
            PdfDictionary d = ((PdfArray) getPdfObject()).getAsDictionary(1);
            if (blackPoint != null) {
                d.put(PdfName.BlackPoint, new PdfArray(blackPoint));
            }
            if (range != null) {
                d.put(PdfName.Range, new PdfArray(range));
            }
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 3;
        }

        private static PdfArray getInitialPdfArray() {
            ArrayList<PdfObject> tempArray = new ArrayList<>(2);
            tempArray.add(PdfName.Lab);
            tempArray.add(new PdfDictionary());
            return new PdfArray(tempArray);
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfCieBasedCs$IccBased.class */
    public static class IccBased extends PdfCieBasedCs {
        private static final long serialVersionUID = 3265273715107224067L;

        public IccBased(PdfArray pdfObject) {
            super(pdfObject);
        }

        public IccBased(InputStream iccStream) {
            this(getInitialPdfArray(iccStream, null));
        }

        public IccBased(InputStream iccStream, float[] range) {
            this(getInitialPdfArray(iccStream, range));
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return ((PdfArray) getPdfObject()).getAsStream(1).getAsInt(PdfName.N).intValue();
        }

        public static PdfStream getIccProfileStream(InputStream iccStream) {
            IccProfile iccProfile = IccProfile.getInstance(iccStream);
            return getIccProfileStream(iccProfile);
        }

        public static PdfStream getIccProfileStream(InputStream iccStream, float[] range) {
            IccProfile iccProfile = IccProfile.getInstance(iccStream);
            return getIccProfileStream(iccProfile, range);
        }

        public static PdfStream getIccProfileStream(IccProfile iccProfile) {
            PdfStream stream = new PdfStream(iccProfile.getData());
            stream.put(PdfName.N, new PdfNumber(iccProfile.getNumComponents()));
            switch (iccProfile.getNumComponents()) {
                case 1:
                    stream.put(PdfName.Alternate, PdfName.DeviceGray);
                    break;
                case 3:
                    stream.put(PdfName.Alternate, PdfName.DeviceRGB);
                    break;
                case 4:
                    stream.put(PdfName.Alternate, PdfName.DeviceCMYK);
                    break;
            }
            return stream;
        }

        public static PdfStream getIccProfileStream(IccProfile iccProfile, float[] range) {
            PdfStream stream = getIccProfileStream(iccProfile);
            stream.put(PdfName.Range, new PdfArray(range));
            return stream;
        }

        private static PdfArray getInitialPdfArray(InputStream iccStream, float[] range) {
            ArrayList<PdfObject> tempArray = new ArrayList<>(2);
            tempArray.add(PdfName.ICCBased);
            tempArray.add(range == null ? getIccProfileStream(iccStream) : getIccProfileStream(iccStream, range));
            return new PdfArray(tempArray);
        }
    }
}
