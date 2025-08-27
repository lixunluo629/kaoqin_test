package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.pdf.filters.IFilterHandler;
import java.io.ByteArrayOutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/MemoryLimitsAwareFilter.class */
public abstract class MemoryLimitsAwareFilter implements IFilterHandler {
    public ByteArrayOutputStream enableMemoryLimitsAwareHandler(PdfDictionary streamDictionary) {
        MemoryLimitsAwareHandler memoryLimitsAwareHandler;
        MemoryLimitsAwareOutputStream outputStream = new MemoryLimitsAwareOutputStream();
        if (null != streamDictionary.getIndirectReference()) {
            memoryLimitsAwareHandler = streamDictionary.getIndirectReference().getDocument().memoryLimitsAwareHandler;
        } else {
            memoryLimitsAwareHandler = new MemoryLimitsAwareHandler();
        }
        if (null != memoryLimitsAwareHandler && memoryLimitsAwareHandler.considerCurrentPdfStream) {
            outputStream.setMaxStreamSize(memoryLimitsAwareHandler.getMaxSizeOfSingleDecompressedPdfStream());
        }
        return outputStream;
    }
}
