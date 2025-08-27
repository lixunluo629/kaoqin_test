package com.itextpdf.kernel.pdf;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDashPattern.class */
public class PdfDashPattern {
    private float dash;
    private float gap;
    private float phase;

    public PdfDashPattern() {
        this.dash = -1.0f;
        this.gap = -1.0f;
        this.phase = -1.0f;
    }

    public PdfDashPattern(float dash) {
        this.dash = -1.0f;
        this.gap = -1.0f;
        this.phase = -1.0f;
        this.dash = dash;
    }

    public PdfDashPattern(float dash, float gap) {
        this.dash = -1.0f;
        this.gap = -1.0f;
        this.phase = -1.0f;
        this.dash = dash;
        this.gap = gap;
    }

    public PdfDashPattern(float dash, float gap, float phase) {
        this(dash, gap);
        this.phase = phase;
    }

    public float getDash() {
        return this.dash;
    }

    public float getGap() {
        return this.gap;
    }

    public float getPhase() {
        return this.phase;
    }
}
