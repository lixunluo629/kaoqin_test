package com.itextpdf.kernel.geom;

import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/PageSize.class */
public class PageSize extends Rectangle implements Cloneable, Serializable {
    private static final long serialVersionUID = 485375591249386160L;
    public static PageSize A0 = new PageSize(2384.0f, 3370.0f);
    public static PageSize A1 = new PageSize(1684.0f, 2384.0f);
    public static PageSize A2 = new PageSize(1190.0f, 1684.0f);
    public static PageSize A3 = new PageSize(842.0f, 1190.0f);
    public static PageSize A4 = new PageSize(595.0f, 842.0f);
    public static PageSize A5 = new PageSize(420.0f, 595.0f);
    public static PageSize A6 = new PageSize(298.0f, 420.0f);
    public static PageSize A7 = new PageSize(210.0f, 298.0f);
    public static PageSize A8 = new PageSize(148.0f, 210.0f);
    public static PageSize A9 = new PageSize(105.0f, 547.0f);
    public static PageSize A10 = new PageSize(74.0f, 105.0f);
    public static PageSize B0 = new PageSize(2834.0f, 4008.0f);
    public static PageSize B1 = new PageSize(2004.0f, 2834.0f);
    public static PageSize B2 = new PageSize(1417.0f, 2004.0f);
    public static PageSize B3 = new PageSize(1000.0f, 1417.0f);
    public static PageSize B4 = new PageSize(708.0f, 1000.0f);
    public static PageSize B5 = new PageSize(498.0f, 708.0f);
    public static PageSize B6 = new PageSize(354.0f, 498.0f);
    public static PageSize B7 = new PageSize(249.0f, 354.0f);
    public static PageSize B8 = new PageSize(175.0f, 249.0f);
    public static PageSize B9 = new PageSize(124.0f, 175.0f);
    public static PageSize B10 = new PageSize(88.0f, 124.0f);
    public static PageSize LETTER = new PageSize(612.0f, 792.0f);
    public static PageSize LEGAL = new PageSize(612.0f, 1008.0f);
    public static PageSize TABLOID = new PageSize(792.0f, 1224.0f);
    public static PageSize LEDGER = new PageSize(1224.0f, 792.0f);
    public static PageSize EXECUTIVE = new PageSize(522.0f, 756.0f);
    public static PageSize Default = A4;

    public PageSize(float width, float height) {
        super(0.0f, 0.0f, width, height);
    }

    public PageSize(Rectangle box) {
        super(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    }

    public PageSize rotate() {
        return new PageSize(this.height, this.width);
    }

    @Override // com.itextpdf.kernel.geom.Rectangle
    /* renamed from: clone */
    public Rectangle mo825clone() {
        return super.mo825clone();
    }
}
