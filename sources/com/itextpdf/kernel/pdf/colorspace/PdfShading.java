package com.itextpdf.kernel.pdf.colorspace;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import com.itextpdf.kernel.pdf.function.PdfFunction;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading.class */
public abstract class PdfShading extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 4781809723744243508L;

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$ShadingType.class */
    private static class ShadingType {
        public static final int FUNCTION_BASED = 1;
        public static final int AXIAL = 2;
        public static final int RADIAL = 3;
        public static final int FREE_FORM_GOURAUD_SHADED_TRIANGLE_MESH = 4;
        public static final int LATTICE_FORM_GOURAUD_SHADED_TRIANGLE_MESH = 5;
        public static final int COONS_PATCH_MESH = 6;
        public static final int TENSOR_PRODUCT_PATCH_MESH = 7;

        private ShadingType() {
        }
    }

    public static PdfShading makeShading(PdfDictionary shadingDictionary) {
        PdfShading shading;
        if (!shadingDictionary.containsKey(PdfName.ShadingType)) {
            throw new PdfException(PdfException.ShadingTypeNotFound);
        }
        if (!shadingDictionary.containsKey(PdfName.ColorSpace)) {
            throw new PdfException(PdfException.ColorSpaceNotFound);
        }
        switch (shadingDictionary.getAsNumber(PdfName.ShadingType).intValue()) {
            case 1:
                shading = new FunctionBased(shadingDictionary);
                break;
            case 2:
                shading = new Axial(shadingDictionary);
                break;
            case 3:
                shading = new Radial(shadingDictionary);
                break;
            case 4:
                if (!shadingDictionary.isStream()) {
                    throw new PdfException(PdfException.UnexpectedShadingType);
                }
                shading = new FreeFormGouraudShadedTriangleMesh((PdfStream) shadingDictionary);
                break;
            case 5:
                if (!shadingDictionary.isStream()) {
                    throw new PdfException(PdfException.UnexpectedShadingType);
                }
                shading = new LatticeFormGouraudShadedTriangleMesh((PdfStream) shadingDictionary);
                break;
            case 6:
                if (!shadingDictionary.isStream()) {
                    throw new PdfException(PdfException.UnexpectedShadingType);
                }
                shading = new CoonsPatchMesh((PdfStream) shadingDictionary);
                break;
            case 7:
                if (!shadingDictionary.isStream()) {
                    throw new PdfException(PdfException.UnexpectedShadingType);
                }
                shading = new TensorProductPatchMesh((PdfStream) shadingDictionary);
                break;
            default:
                throw new PdfException(PdfException.UnexpectedShadingType);
        }
        return shading;
    }

    protected PdfShading(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    protected PdfShading(PdfDictionary pdfObject, int type, PdfColorSpace colorSpace) {
        super(pdfObject);
        getPdfObject().put(PdfName.ShadingType, new PdfNumber(type));
        if (colorSpace instanceof PdfSpecialCs.Pattern) {
            throw new IllegalArgumentException("colorSpace");
        }
        getPdfObject().put(PdfName.ColorSpace, colorSpace.getPdfObject());
    }

    public int getShadingType() {
        return getPdfObject().getAsInt(PdfName.ShadingType).intValue();
    }

    public PdfObject getColorSpace() {
        return getPdfObject().get(PdfName.ColorSpace);
    }

    public PdfObject getFunction() {
        return getPdfObject().get(PdfName.Function);
    }

    public void setFunction(PdfFunction function) {
        getPdfObject().put(PdfName.Function, function.getPdfObject());
        setModified();
    }

    public void setFunction(PdfFunction[] functions) {
        PdfArray arr = new PdfArray();
        for (PdfFunction func : functions) {
            arr.add(func.getPdfObject());
        }
        getPdfObject().put(PdfName.Function, arr);
        setModified();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$FunctionBased.class */
    public static class FunctionBased extends PdfShading {
        private static final long serialVersionUID = -4459197498902558052L;

        protected FunctionBased(PdfDictionary pdfDictionary) {
            super(pdfDictionary);
        }

        public FunctionBased(PdfColorSpace colorSpace, PdfFunction function) {
            this(colorSpace.getPdfObject(), function);
        }

        public FunctionBased(PdfObject colorSpace, PdfFunction function) {
            super(new PdfDictionary(), 1, PdfColorSpace.makeColorSpace(colorSpace));
            setFunction(function);
        }

        public PdfArray getDomain() {
            return getPdfObject().getAsArray(PdfName.Domain);
        }

        public void setDomain(float xmin, float xmax, float ymin, float ymax) {
            setDomain(new PdfArray(new float[]{xmin, xmax, ymin, ymax}));
        }

        public void setDomain(PdfArray domain) {
            getPdfObject().put(PdfName.Domain, domain);
            setModified();
        }

        public PdfArray getMatrix() {
            PdfArray matrix = getPdfObject().getAsArray(PdfName.Matrix);
            if (matrix == null) {
                matrix = new PdfArray(new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f});
                setMatrix(matrix);
            }
            return matrix;
        }

        public void setMatrix(float[] matrix) {
            setMatrix(new PdfArray(matrix));
        }

        public void setMatrix(PdfArray matrix) {
            getPdfObject().put(PdfName.Matrix, matrix);
            setModified();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$Axial.class */
    public static class Axial extends PdfShading {
        private static final long serialVersionUID = 5504688740677023792L;

        protected Axial(PdfDictionary pdfDictionary) {
            super(pdfDictionary);
        }

        public Axial(PdfColorSpace cs, float x0, float y0, float[] color0, float x1, float y1, float[] color1) {
            super(new PdfDictionary(), 2, cs);
            setCoords(x0, y0, x1, y1);
            PdfFunction func = new PdfFunction.Type2(new PdfArray(new float[]{0.0f, 1.0f}), null, new PdfArray(color0), new PdfArray(color1), new PdfNumber(1));
            setFunction(func);
        }

        public Axial(PdfColorSpace cs, float x0, float y0, float[] color0, float x1, float y1, float[] color1, boolean[] extend) {
            this(cs, x0, y0, color0, x1, y1, color1);
            if (extend == null || extend.length != 2) {
                throw new IllegalArgumentException("extend");
            }
            setExtend(extend[0], extend[1]);
        }

        public Axial(PdfColorSpace cs, PdfArray coords, PdfFunction function) {
            super(new PdfDictionary(), 2, cs);
            setCoords(coords);
            setFunction(function);
        }

        public PdfArray getCoords() {
            return getPdfObject().getAsArray(PdfName.Coords);
        }

        public void setCoords(float x0, float y0, float x1, float y1) {
            setCoords(new PdfArray(new float[]{x0, y0, x1, y1}));
        }

        public void setCoords(PdfArray coords) {
            getPdfObject().put(PdfName.Coords, coords);
            setModified();
        }

        public PdfArray getDomain() {
            PdfArray domain = getPdfObject().getAsArray(PdfName.Domain);
            if (domain == null) {
                domain = new PdfArray(new float[]{0.0f, 1.0f});
                setDomain(domain);
            }
            return domain;
        }

        public void setDomain(float t0, float t1) {
            setDomain(new PdfArray(new float[]{t0, t1}));
        }

        public void setDomain(PdfArray domain) {
            getPdfObject().put(PdfName.Domain, domain);
            setModified();
        }

        public PdfArray getExtend() {
            PdfArray extend = getPdfObject().getAsArray(PdfName.Extend);
            if (extend == null) {
                extend = new PdfArray(new boolean[]{false, false});
                setExtend(extend);
            }
            return extend;
        }

        public void setExtend(boolean extendStart, boolean extendEnd) {
            setExtend(new PdfArray(new boolean[]{extendStart, extendEnd}));
        }

        public void setExtend(PdfArray extend) {
            getPdfObject().put(PdfName.Extend, extend);
            setModified();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$Radial.class */
    public static class Radial extends PdfShading {
        private static final long serialVersionUID = -5012819396006804845L;

        protected Radial(PdfDictionary pdfDictionary) {
            super(pdfDictionary);
        }

        public Radial(PdfColorSpace cs, float x0, float y0, float r0, float[] color0, float x1, float y1, float r1, float[] color1) {
            super(new PdfDictionary(), 3, cs);
            setCoords(x0, y0, r0, x1, y1, r1);
            PdfFunction func = new PdfFunction.Type2(new PdfArray(new float[]{0.0f, 1.0f}), null, new PdfArray(color0), new PdfArray(color1), new PdfNumber(1));
            setFunction(func);
        }

        public Radial(PdfColorSpace cs, float x0, float y0, float r0, float[] color0, float x1, float y1, float r1, float[] color1, boolean[] extend) {
            this(cs, x0, y0, r0, color0, x1, y1, r1, color1);
            if (extend == null || extend.length != 2) {
                throw new IllegalArgumentException("extend");
            }
            setExtend(extend[0], extend[1]);
        }

        public Radial(PdfColorSpace cs, PdfArray coords, PdfFunction function) {
            super(new PdfDictionary(), 3, cs);
            setCoords(coords);
            setFunction(function);
        }

        public PdfArray getCoords() {
            return getPdfObject().getAsArray(PdfName.Coords);
        }

        public void setCoords(float x0, float y0, float r0, float x1, float y1, float r1) {
            setCoords(new PdfArray(new float[]{x0, y0, r0, x1, y1, r1}));
        }

        public void setCoords(PdfArray coords) {
            getPdfObject().put(PdfName.Coords, coords);
            setModified();
        }

        public PdfArray getDomain() {
            PdfArray domain = getPdfObject().getAsArray(PdfName.Domain);
            if (domain == null) {
                domain = new PdfArray(new float[]{0.0f, 1.0f});
                setDomain(domain);
            }
            return domain;
        }

        public void setDomain(float t0, float t1) {
            setDomain(new PdfArray(new float[]{t0, t1}));
        }

        public void setDomain(PdfArray domain) {
            getPdfObject().put(PdfName.Domain, domain);
            setModified();
        }

        public PdfArray getExtend() {
            PdfArray extend = getPdfObject().getAsArray(PdfName.Extend);
            if (extend == null) {
                extend = new PdfArray(new boolean[]{false, false});
                setExtend(extend);
            }
            return extend;
        }

        public void setExtend(boolean extendStart, boolean extendEnd) {
            setExtend(new PdfArray(new boolean[]{extendStart, extendEnd}));
        }

        public void setExtend(PdfArray extend) {
            getPdfObject().put(PdfName.Extend, extend);
            setModified();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$FreeFormGouraudShadedTriangleMesh.class */
    public static class FreeFormGouraudShadedTriangleMesh extends PdfShading {
        private static final long serialVersionUID = -2690557760051875972L;

        protected FreeFormGouraudShadedTriangleMesh(PdfStream pdfStream) {
            super(pdfStream);
        }

        public FreeFormGouraudShadedTriangleMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int bitsPerFlag, float[] decode) {
            this(cs, bitsPerCoordinate, bitsPerComponent, bitsPerFlag, new PdfArray(decode));
        }

        public FreeFormGouraudShadedTriangleMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int bitsPerFlag, PdfArray decode) {
            super(new PdfStream(), 4, cs);
            setBitsPerCoordinate(bitsPerCoordinate);
            setBitsPerComponent(bitsPerComponent);
            setBitsPerFlag(bitsPerFlag);
            setDecode(decode);
        }

        public int getBitsPerCoordinate() {
            return getPdfObject().getAsInt(PdfName.BitsPerCoordinate).intValue();
        }

        public void setBitsPerCoordinate(int bitsPerCoordinate) {
            getPdfObject().put(PdfName.BitsPerCoordinate, new PdfNumber(bitsPerCoordinate));
            setModified();
        }

        public int getBitsPerComponent() {
            return getPdfObject().getAsInt(PdfName.BitsPerComponent).intValue();
        }

        public void setBitsPerComponent(int bitsPerComponent) {
            getPdfObject().put(PdfName.BitsPerComponent, new PdfNumber(bitsPerComponent));
            setModified();
        }

        public int getBitsPerFlag() {
            return getPdfObject().getAsInt(PdfName.BitsPerFlag).intValue();
        }

        public void setBitsPerFlag(int bitsPerFlag) {
            getPdfObject().put(PdfName.BitsPerFlag, new PdfNumber(bitsPerFlag));
            setModified();
        }

        public PdfArray getDecode() {
            return getPdfObject().getAsArray(PdfName.Decode);
        }

        public void setDecode(float[] decode) {
            setDecode(new PdfArray(decode));
        }

        public void setDecode(PdfArray decode) {
            getPdfObject().put(PdfName.Decode, decode);
            setModified();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$LatticeFormGouraudShadedTriangleMesh.class */
    public static class LatticeFormGouraudShadedTriangleMesh extends PdfShading {
        private static final long serialVersionUID = -8776232978423888214L;

        protected LatticeFormGouraudShadedTriangleMesh(PdfStream pdfStream) {
            super(pdfStream);
        }

        public LatticeFormGouraudShadedTriangleMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int verticesPerRow, float[] decode) {
            this(cs, bitsPerCoordinate, bitsPerComponent, verticesPerRow, new PdfArray(decode));
        }

        public LatticeFormGouraudShadedTriangleMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int verticesPerRow, PdfArray decode) {
            super(new PdfStream(), 5, cs);
            setBitsPerCoordinate(bitsPerCoordinate);
            setBitsPerComponent(bitsPerComponent);
            setVerticesPerRow(verticesPerRow);
            setDecode(decode);
        }

        public int getBitsPerCoordinate() {
            return getPdfObject().getAsInt(PdfName.BitsPerCoordinate).intValue();
        }

        public void setBitsPerCoordinate(int bitsPerCoordinate) {
            getPdfObject().put(PdfName.BitsPerCoordinate, new PdfNumber(bitsPerCoordinate));
            setModified();
        }

        public int getBitsPerComponent() {
            return getPdfObject().getAsInt(PdfName.BitsPerComponent).intValue();
        }

        public void setBitsPerComponent(int bitsPerComponent) {
            getPdfObject().put(PdfName.BitsPerComponent, new PdfNumber(bitsPerComponent));
            setModified();
        }

        public int getVerticesPerRow() {
            return getPdfObject().getAsInt(PdfName.VerticesPerRow).intValue();
        }

        public void setVerticesPerRow(int verticesPerRow) {
            getPdfObject().put(PdfName.VerticesPerRow, new PdfNumber(verticesPerRow));
            setModified();
        }

        public PdfArray getDecode() {
            return getPdfObject().getAsArray(PdfName.Decode);
        }

        public void setDecode(float[] decode) {
            setDecode(new PdfArray(decode));
        }

        public void setDecode(PdfArray decode) {
            getPdfObject().put(PdfName.Decode, decode);
            setModified();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$CoonsPatchMesh.class */
    public static class CoonsPatchMesh extends PdfShading {
        private static final long serialVersionUID = 7296891352801419708L;

        protected CoonsPatchMesh(PdfStream pdfStream) {
            super(pdfStream);
        }

        public CoonsPatchMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int bitsPerFlag, float[] decode) {
            this(cs, bitsPerCoordinate, bitsPerComponent, bitsPerFlag, new PdfArray(decode));
        }

        public CoonsPatchMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int bitsPerFlag, PdfArray decode) {
            super(new PdfStream(), 6, cs);
            setBitsPerCoordinate(bitsPerCoordinate);
            setBitsPerComponent(bitsPerComponent);
            setBitsPerFlag(bitsPerFlag);
            setDecode(decode);
        }

        public int getBitsPerCoordinate() {
            return getPdfObject().getAsInt(PdfName.BitsPerCoordinate).intValue();
        }

        public void setBitsPerCoordinate(int bitsPerCoordinate) {
            getPdfObject().put(PdfName.BitsPerCoordinate, new PdfNumber(bitsPerCoordinate));
            setModified();
        }

        public int getBitsPerComponent() {
            return getPdfObject().getAsInt(PdfName.BitsPerComponent).intValue();
        }

        public void setBitsPerComponent(int bitsPerComponent) {
            getPdfObject().put(PdfName.BitsPerComponent, new PdfNumber(bitsPerComponent));
            setModified();
        }

        public int getBitsPerFlag() {
            return getPdfObject().getAsInt(PdfName.BitsPerFlag).intValue();
        }

        public void setBitsPerFlag(int bitsPerFlag) {
            getPdfObject().put(PdfName.BitsPerFlag, new PdfNumber(bitsPerFlag));
            setModified();
        }

        public PdfArray getDecode() {
            return getPdfObject().getAsArray(PdfName.Decode);
        }

        public void setDecode(float[] decode) {
            setDecode(new PdfArray(decode));
        }

        public void setDecode(PdfArray decode) {
            getPdfObject().put(PdfName.Decode, decode);
            setModified();
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/colorspace/PdfShading$TensorProductPatchMesh.class */
    public static class TensorProductPatchMesh extends PdfShading {
        private static final long serialVersionUID = -2750695839303504742L;

        protected TensorProductPatchMesh(PdfStream pdfStream) {
            super(pdfStream);
        }

        public TensorProductPatchMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int bitsPerFlag, float[] decode) {
            this(cs, bitsPerCoordinate, bitsPerComponent, bitsPerFlag, new PdfArray(decode));
        }

        public TensorProductPatchMesh(PdfColorSpace cs, int bitsPerCoordinate, int bitsPerComponent, int bitsPerFlag, PdfArray decode) {
            super(new PdfStream(), 7, cs);
            setBitsPerCoordinate(bitsPerCoordinate);
            setBitsPerComponent(bitsPerComponent);
            setBitsPerFlag(bitsPerFlag);
            setDecode(decode);
        }

        public int getBitsPerCoordinate() {
            return getPdfObject().getAsInt(PdfName.BitsPerCoordinate).intValue();
        }

        public void setBitsPerCoordinate(int bitsPerCoordinate) {
            getPdfObject().put(PdfName.BitsPerCoordinate, new PdfNumber(bitsPerCoordinate));
            setModified();
        }

        public int getBitsPerComponent() {
            return getPdfObject().getAsInt(PdfName.BitsPerComponent).intValue();
        }

        public void setBitsPerComponent(int bitsPerComponent) {
            getPdfObject().put(PdfName.BitsPerComponent, new PdfNumber(bitsPerComponent));
            setModified();
        }

        public int getBitsPerFlag() {
            return getPdfObject().getAsInt(PdfName.BitsPerFlag).intValue();
        }

        public void setBitsPerFlag(int bitsPerFlag) {
            getPdfObject().put(PdfName.BitsPerFlag, new PdfNumber(bitsPerFlag));
            setModified();
        }

        public PdfArray getDecode() {
            return getPdfObject().getAsArray(PdfName.Decode);
        }

        public void setDecode(float[] decode) {
            setDecode(new PdfArray(decode));
        }

        public void setDecode(PdfArray decode) {
            getPdfObject().put(PdfName.Decode, decode);
            setModified();
        }
    }
}
