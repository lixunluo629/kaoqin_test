package com.itextpdf.kernel.pdf.colorspace;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.function.PdfFunction;
import java.util.Arrays;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs.class */
public abstract class PdfSpecialCs extends PdfColorSpace {
    private static final long serialVersionUID = -2725455900398492836L;

    protected PdfSpecialCs(PdfArray pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs$Indexed.class */
    public static class Indexed extends PdfSpecialCs {
        private static final long serialVersionUID = -1155418938167317916L;

        public Indexed(PdfArray pdfObject) {
            super(pdfObject);
        }

        public Indexed(PdfObject base, int hival, PdfString lookup) {
            this(getIndexedCsArray(base, hival, lookup));
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 1;
        }

        public PdfColorSpace getBaseCs() {
            return makeColorSpace(((PdfArray) getPdfObject()).get(1));
        }

        private static PdfArray getIndexedCsArray(PdfObject base, int hival, PdfString lookup) {
            PdfArray indexed = new PdfArray();
            indexed.add(PdfName.Indexed);
            indexed.add(base);
            indexed.add(new PdfNumber(hival));
            indexed.add(lookup.setHexWriting(true));
            return indexed;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs$Separation.class */
    public static class Separation extends PdfSpecialCs {
        private static final long serialVersionUID = 4259327393838350842L;

        public Separation(PdfArray pdfObject) {
            super(pdfObject);
        }

        public Separation(PdfName name, PdfObject alternateSpace, PdfObject tintTransform) {
            this(getSeparationCsArray(name, alternateSpace, tintTransform));
        }

        public Separation(String name, PdfColorSpace alternateSpace, PdfFunction tintTransform) {
            this(new PdfName(name), alternateSpace.getPdfObject(), tintTransform.getPdfObject());
            if (!tintTransform.checkCompatibilityWithColorSpace(alternateSpace)) {
                throw new PdfException(PdfException.FunctionIsNotCompatibleWitColorSpace, this);
            }
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 1;
        }

        public PdfColorSpace getBaseCs() {
            return makeColorSpace(((PdfArray) getPdfObject()).get(2));
        }

        public PdfName getName() {
            return ((PdfArray) getPdfObject()).getAsName(1);
        }

        private static PdfArray getSeparationCsArray(PdfName name, PdfObject alternateSpace, PdfObject tintTransform) {
            PdfArray separation = new PdfArray();
            separation.add(PdfName.Separation);
            separation.add(name);
            separation.add(alternateSpace);
            separation.add(tintTransform);
            return separation;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs$DeviceN.class */
    public static class DeviceN extends PdfSpecialCs {
        private static final long serialVersionUID = 4051693146595260270L;
        protected int numOfComponents;

        public DeviceN(PdfArray pdfObject) {
            super(pdfObject);
            this.numOfComponents = 0;
            this.numOfComponents = pdfObject.getAsArray(1).size();
        }

        public DeviceN(PdfArray names, PdfObject alternateSpace, PdfObject tintTransform) {
            this(getDeviceNCsArray(names, alternateSpace, tintTransform));
        }

        public DeviceN(List<String> names, PdfColorSpace alternateSpace, PdfFunction tintTransform) {
            this(new PdfArray(names, true), alternateSpace.getPdfObject(), tintTransform.getPdfObject());
            if (tintTransform.getInputSize() != getNumberOfComponents() || tintTransform.getOutputSize() != alternateSpace.getNumberOfComponents()) {
                throw new PdfException(PdfException.FunctionIsNotCompatibleWitColorSpace, this);
            }
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return this.numOfComponents;
        }

        public PdfColorSpace getBaseCs() {
            return makeColorSpace(((PdfArray) getPdfObject()).get(2));
        }

        public PdfArray getNames() {
            return ((PdfArray) getPdfObject()).getAsArray(1);
        }

        protected static PdfArray getDeviceNCsArray(PdfArray names, PdfObject alternateSpace, PdfObject tintTransform) {
            PdfArray deviceN = new PdfArray();
            deviceN.add(PdfName.DeviceN);
            deviceN.add(names);
            deviceN.add(alternateSpace);
            deviceN.add(tintTransform);
            return deviceN;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs$NChannel.class */
    public static class NChannel extends DeviceN {
        private static final long serialVersionUID = 5352964946869757972L;

        public NChannel(PdfArray pdfObject) {
            super(pdfObject);
        }

        public NChannel(PdfArray names, PdfObject alternateSpace, PdfObject tintTransform, PdfDictionary attributes) {
            this(getNChannelCsArray(names, alternateSpace, tintTransform, attributes));
        }

        public NChannel(List<String> names, PdfColorSpace alternateSpace, PdfFunction tintTransform, PdfDictionary attributes) {
            this(new PdfArray(names, true), alternateSpace.getPdfObject(), tintTransform.getPdfObject(), attributes);
            if (tintTransform.getInputSize() != 1 || tintTransform.getOutputSize() != alternateSpace.getNumberOfComponents()) {
                throw new PdfException(PdfException.FunctionIsNotCompatibleWitColorSpace, this);
            }
        }

        protected static PdfArray getNChannelCsArray(PdfArray names, PdfObject alternateSpace, PdfObject tintTransform, PdfDictionary attributes) {
            PdfArray nChannel = getDeviceNCsArray(names, alternateSpace, tintTransform);
            nChannel.add(attributes);
            return nChannel;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs$Pattern.class */
    public static class Pattern extends PdfColorSpace {
        private static final long serialVersionUID = 8057478102447278706L;

        @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
        protected boolean isWrappedObjectMustBeIndirect() {
            return false;
        }

        public Pattern() {
            super(PdfName.Pattern);
        }

        protected Pattern(PdfObject pdfObj) {
            super(pdfObj);
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return 0;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfSpecialCs$UncoloredTilingPattern.class */
    public static class UncoloredTilingPattern extends Pattern {
        private static final long serialVersionUID = -9030226298201261021L;

        @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
        public void flush() {
            super.flush();
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs.Pattern, com.itextpdf.kernel.pdf.PdfObjectWrapper
        protected boolean isWrappedObjectMustBeIndirect() {
            return true;
        }

        public UncoloredTilingPattern(PdfArray pdfObject) {
            super(pdfObject);
        }

        public UncoloredTilingPattern(PdfColorSpace underlyingColorSpace) {
            super(new PdfArray((List<? extends PdfObject>) Arrays.asList(PdfName.Pattern, underlyingColorSpace.getPdfObject())));
        }

        @Override // com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs.Pattern, com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
        public int getNumberOfComponents() {
            return PdfColorSpace.makeColorSpace(((PdfArray) getPdfObject()).get(1)).getNumberOfComponents();
        }

        public PdfColorSpace getUnderlyingColorSpace() {
            return PdfColorSpace.makeColorSpace(((PdfArray) getPdfObject()).get(1));
        }
    }
}
