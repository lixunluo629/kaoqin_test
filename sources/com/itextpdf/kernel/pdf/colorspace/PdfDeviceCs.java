package com.itextpdf.kernel.pdf.colorspace;

import com.itextpdf.kernel.pdf.PdfName;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfDeviceCs.class */
public abstract class PdfDeviceCs extends PdfColorSpace {
    private static final long serialVersionUID = 6884911248656287064L;

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }

    protected PdfDeviceCs(PdfName pdfObject) {
        super(pdfObject);
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfDeviceCs$Gray.class */
    public static class Gray extends PdfDeviceCs {
        private static final long serialVersionUID = 2722906212276665191L;

        public Gray() {
            super(PdfName.DeviceGray);
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 1;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfDeviceCs$Rgb.class */
    public static class Rgb extends PdfDeviceCs {
        private static final long serialVersionUID = -1605044540582561428L;

        public Rgb() {
            super(PdfName.DeviceRGB);
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 3;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfDeviceCs$Cmyk.class */
    public static class Cmyk extends PdfDeviceCs {
        private static final long serialVersionUID = 2615036909699704719L;

        public Cmyk() {
            super(PdfName.DeviceCMYK);
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 4;
        }
    }
}
