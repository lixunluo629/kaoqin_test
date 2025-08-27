package com.itextpdf.layout.margincollapse;

import java.io.Serializable;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/margincollapse/MarginsCollapse.class */
class MarginsCollapse implements Cloneable, Serializable {
    private float maxPositiveMargin = 0.0f;
    private float minNegativeMargin = 0.0f;

    MarginsCollapse() {
    }

    void joinMargin(float margin) {
        if (this.maxPositiveMargin < margin) {
            this.maxPositiveMargin = margin;
        } else if (this.minNegativeMargin > margin) {
            this.minNegativeMargin = margin;
        }
    }

    void joinMargin(MarginsCollapse marginsCollapse) {
        joinMargin(marginsCollapse.maxPositiveMargin);
        joinMargin(marginsCollapse.minNegativeMargin);
    }

    float getCollapsedMarginsSize() {
        return this.maxPositiveMargin + this.minNegativeMargin;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public MarginsCollapse m951clone() {
        try {
            return (MarginsCollapse) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
