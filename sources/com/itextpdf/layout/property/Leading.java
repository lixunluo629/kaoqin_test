package com.itextpdf.layout.property;

import java.util.Objects;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/Leading.class */
public class Leading {
    public static final int FIXED = 1;
    public static final int MULTIPLIED = 2;
    protected int type;
    protected float value;

    public Leading(int type, float value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public float getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && this.type == ((Leading) obj).type && this.value == ((Leading) obj).value;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.type), Float.valueOf(this.value));
    }
}
