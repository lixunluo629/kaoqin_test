package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/PolyNode.class */
public class PolyNode {
    private PolyNode parent;
    private int index;
    private IClipper.JoinType joinType;
    private IClipper.EndType endType;
    private boolean isOpen;
    private final Path polygon = new Path();
    protected final List<PolyNode> childs = new ArrayList();

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/PolyNode$NodeType.class */
    enum NodeType {
        ANY,
        OPEN,
        CLOSED
    }

    public void addChild(PolyNode child) {
        int cnt = this.childs.size();
        this.childs.add(child);
        child.parent = this;
        child.index = cnt;
    }

    public int getChildCount() {
        return this.childs.size();
    }

    public List<PolyNode> getChilds() {
        return Collections.unmodifiableList(this.childs);
    }

    public List<Point.LongPoint> getContour() {
        return this.polygon;
    }

    public IClipper.EndType getEndType() {
        return this.endType;
    }

    public IClipper.JoinType getJoinType() {
        return this.joinType;
    }

    public PolyNode getNext() {
        if (!this.childs.isEmpty()) {
            return this.childs.get(0);
        }
        return getNextSiblingUp();
    }

    private PolyNode getNextSiblingUp() {
        if (this.parent == null) {
            return null;
        }
        if (this.index == this.parent.childs.size() - 1) {
            return this.parent.getNextSiblingUp();
        }
        return this.parent.childs.get(this.index + 1);
    }

    public PolyNode getParent() {
        return this.parent;
    }

    public Path getPolygon() {
        return this.polygon;
    }

    public boolean isHole() {
        return isHoleNode();
    }

    private boolean isHoleNode() {
        boolean result = true;
        PolyNode polyNode = this.parent;
        while (true) {
            PolyNode node = polyNode;
            if (node != null) {
                result = !result;
                polyNode = node.parent;
            } else {
                return result;
            }
        }
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setEndType(IClipper.EndType value) {
        this.endType = value;
    }

    public void setJoinType(IClipper.JoinType value) {
        this.joinType = value;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setParent(PolyNode n) {
        this.parent = n;
    }
}
