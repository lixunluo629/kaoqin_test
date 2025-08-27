package com.itextpdf.kernel.pdf.canvas.parser.data;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.CanvasTag;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/data/ImageRenderInfo.class */
public class ImageRenderInfo extends AbstractRenderInfo {
    private Matrix ctm;
    private PdfImageXObject image;
    private PdfDictionary colorSpaceDictionary;
    private boolean isInline;
    private PdfName resourceName;
    private List<CanvasTag> canvasTagHierarchy;

    public ImageRenderInfo(Stack<CanvasTag> canvasTagHierarchy, CanvasGraphicsState gs, Matrix ctm, PdfStream imageStream, PdfName resourceName, PdfDictionary colorSpaceDictionary, boolean isInline) {
        super(gs);
        this.canvasTagHierarchy = Collections.unmodifiableList(new ArrayList(canvasTagHierarchy));
        this.resourceName = resourceName;
        this.ctm = ctm;
        this.image = new PdfImageXObject(imageStream);
        this.colorSpaceDictionary = colorSpaceDictionary;
        this.isInline = isInline;
    }

    public PdfImageXObject getImage() {
        return this.image;
    }

    public PdfName getImageResourceName() {
        return this.resourceName;
    }

    public Vector getStartPoint() {
        return new Vector(0.0f, 0.0f, 1.0f).cross(this.ctm);
    }

    public Matrix getImageCtm() {
        return this.ctm;
    }

    public float getArea() {
        return this.ctm.getDeterminant();
    }

    public boolean isInline() {
        return this.isInline;
    }

    public PdfDictionary getColorSpaceDictionary() {
        return this.colorSpaceDictionary;
    }

    public List<CanvasTag> getCanvasTagHierarchy() {
        return this.canvasTagHierarchy;
    }

    public int getMcid() {
        for (CanvasTag tag : this.canvasTagHierarchy) {
            if (tag.hasMcid()) {
                return tag.getMcid();
            }
        }
        return -1;
    }

    public boolean hasMcid(int mcid) {
        return hasMcid(mcid, false);
    }

    public boolean hasMcid(int mcid, boolean checkTheTopmostLevelOnly) {
        int infoMcid;
        if (checkTheTopmostLevelOnly) {
            return (this.canvasTagHierarchy == null || (infoMcid = getMcid()) == -1 || infoMcid != mcid) ? false : true;
        }
        for (CanvasTag tag : this.canvasTagHierarchy) {
            if (tag.hasMcid() && tag.getMcid() == mcid) {
                return true;
            }
        }
        return false;
    }
}
