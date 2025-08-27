package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.counter.event.IMetaInfo;
import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/DocumentProperties.class */
public class DocumentProperties implements Serializable {
    private static final long serialVersionUID = -6625621282242153134L;
    protected IMetaInfo metaInfo;

    public DocumentProperties() {
        this.metaInfo = null;
    }

    public DocumentProperties(DocumentProperties other) {
        this.metaInfo = null;
        this.metaInfo = other.metaInfo;
    }

    public DocumentProperties setEventCountingMetaInfo(IMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
        return this;
    }
}
