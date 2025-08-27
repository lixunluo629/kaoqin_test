package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.PdfException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/MemoryLimitsAwareException.class */
public class MemoryLimitsAwareException extends PdfException {
    public MemoryLimitsAwareException(String message) {
        super(message);
    }

    public MemoryLimitsAwareException(Throwable cause) {
        this(PdfException.UnknownPdfException, cause);
    }

    public MemoryLimitsAwareException(String message, Throwable cause) {
        super(message, cause);
    }
}
