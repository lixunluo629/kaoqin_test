package com.itextpdf.kernel.pdf.canvas.parser.data;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Path;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/data/ClippingPathInfo.class */
public class ClippingPathInfo extends AbstractRenderInfo {
    private Path path;
    private Matrix ctm;

    public ClippingPathInfo(CanvasGraphicsState gs, Path path, Matrix ctm) {
        super(gs);
        this.path = path;
        this.ctm = ctm;
    }

    public Path getClippingPath() {
        return this.path;
    }

    public Matrix getCtm() {
        return this.ctm;
    }
}
