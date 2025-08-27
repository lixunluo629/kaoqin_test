package com.itextpdf.kernel.pdf.canvas.parser.data;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Path;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.CanvasTag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/data/PathRenderInfo.class */
public class PathRenderInfo extends AbstractRenderInfo {
    public static final int NO_OP = 0;
    public static final int STROKE = 1;
    public static final int FILL = 2;
    private Path path;
    private int operation;
    private int rule;
    private boolean isClip;
    private int clippingRule;
    private List<CanvasTag> canvasTagHierarchy;

    public PathRenderInfo(Stack<CanvasTag> canvasTagHierarchy, CanvasGraphicsState gs, Path path, int operation, int rule, boolean isClip, int clipRule) {
        super(gs);
        this.canvasTagHierarchy = Collections.unmodifiableList(new ArrayList(canvasTagHierarchy));
        this.path = path;
        this.operation = operation;
        this.rule = rule;
        this.isClip = isClip;
        this.clippingRule = clipRule;
    }

    public PathRenderInfo(Stack<CanvasTag> canvasTagHierarchy, CanvasGraphicsState gs, Path path, int operation) {
        this(canvasTagHierarchy, gs, path, operation, 1, false, 1);
    }

    public Path getPath() {
        return this.path;
    }

    public int getOperation() {
        return this.operation;
    }

    public int getRule() {
        return this.rule;
    }

    public boolean isPathModifiesClippingPath() {
        return this.isClip;
    }

    public int getClippingRule() {
        return this.clippingRule;
    }

    public Matrix getCtm() {
        checkGraphicsState();
        return this.gs.getCtm();
    }

    public float getLineWidth() {
        checkGraphicsState();
        return this.gs.getLineWidth();
    }

    public int getLineCapStyle() {
        checkGraphicsState();
        return this.gs.getLineCapStyle();
    }

    public int getLineJoinStyle() {
        checkGraphicsState();
        return this.gs.getLineJoinStyle();
    }

    public float getMiterLimit() {
        checkGraphicsState();
        return this.gs.getMiterLimit();
    }

    public PdfArray getLineDashPattern() {
        checkGraphicsState();
        return this.gs.getDashPattern();
    }

    public Color getStrokeColor() {
        checkGraphicsState();
        return this.gs.getStrokeColor();
    }

    public Color getFillColor() {
        checkGraphicsState();
        return this.gs.getFillColor();
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
