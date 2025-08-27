package com.itextpdf.kernel.pdf;

import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/StampingProperties.class */
public class StampingProperties extends DocumentProperties implements Serializable {
    private static final long serialVersionUID = 6108082513101777457L;
    protected boolean appendMode;
    protected boolean preserveEncryption;

    public StampingProperties() {
        this.appendMode = false;
        this.preserveEncryption = false;
    }

    public StampingProperties(StampingProperties other) {
        super(other);
        this.appendMode = false;
        this.preserveEncryption = false;
        this.appendMode = other.appendMode;
        this.preserveEncryption = other.preserveEncryption;
    }

    public StampingProperties useAppendMode() {
        this.appendMode = true;
        return this;
    }

    public StampingProperties preserveEncryption() {
        this.preserveEncryption = true;
        return this;
    }
}
