package com.itextpdf.layout.layout;

import com.itextpdf.io.util.HashCode;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/layout/LayoutArea.class */
public class LayoutArea implements Cloneable {
    protected int pageNumber;
    protected Rectangle bBox;

    public LayoutArea(int pageNumber, Rectangle bBox) {
        this.pageNumber = pageNumber;
        this.bBox = bBox;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public Rectangle getBBox() {
        return this.bBox;
    }

    public void setBBox(Rectangle bbox) {
        this.bBox = bbox;
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LayoutArea mo950clone() {
        try {
            LayoutArea clone = (LayoutArea) super.clone();
            clone.bBox = this.bBox.mo825clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        LayoutArea that = (LayoutArea) obj;
        return this.pageNumber == that.pageNumber && this.bBox.equalsWithEpsilon(that.bBox);
    }

    public int hashCode() {
        HashCode hashCode = new HashCode();
        hashCode.append(this.pageNumber).append(this.bBox.hashCode());
        return hashCode.hashCode();
    }

    public String toString() {
        return MessageFormatUtil.format("{0}, page {1}", this.bBox.toString(), Integer.valueOf(this.pageNumber));
    }
}
