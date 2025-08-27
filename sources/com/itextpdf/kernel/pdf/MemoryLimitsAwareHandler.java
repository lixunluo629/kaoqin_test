package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.PdfException;
import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/MemoryLimitsAwareHandler.class */
public class MemoryLimitsAwareHandler implements Serializable {
    private static final long serialVersionUID = 2499046471291214639L;
    private static final int SINGLE_SCALE_COEFFICIENT = 100;
    private static final int SUM_SCALE_COEFFICIENT = 500;
    private static final int SINGLE_DECOMPRESSED_PDF_STREAM_MIN_SIZE = 21474836;
    private static final long SUM_OF_DECOMPRESSED_PDF_STREAMW_MIN_SIZE = 107374182;
    private int maxSizeOfSingleDecompressedPdfStream;
    private long maxSizeOfDecompressedPdfStreamsSum;
    private long allMemoryUsedForDecompression;
    private long memoryUsedForCurrentPdfStreamDecompression;
    boolean considerCurrentPdfStream;

    public MemoryLimitsAwareHandler() {
        this.allMemoryUsedForDecompression = 0L;
        this.memoryUsedForCurrentPdfStreamDecompression = 0L;
        this.considerCurrentPdfStream = false;
        this.maxSizeOfSingleDecompressedPdfStream = SINGLE_DECOMPRESSED_PDF_STREAM_MIN_SIZE;
        this.maxSizeOfDecompressedPdfStreamsSum = SUM_OF_DECOMPRESSED_PDF_STREAMW_MIN_SIZE;
    }

    public MemoryLimitsAwareHandler(long documentSize) {
        this.allMemoryUsedForDecompression = 0L;
        this.memoryUsedForCurrentPdfStreamDecompression = 0L;
        this.considerCurrentPdfStream = false;
        this.maxSizeOfSingleDecompressedPdfStream = (int) calculateDefaultParameter(documentSize, 100, 21474836L);
        this.maxSizeOfDecompressedPdfStreamsSum = calculateDefaultParameter(documentSize, 500, SUM_OF_DECOMPRESSED_PDF_STREAMW_MIN_SIZE);
    }

    public int getMaxSizeOfSingleDecompressedPdfStream() {
        return this.maxSizeOfSingleDecompressedPdfStream;
    }

    public MemoryLimitsAwareHandler setMaxSizeOfSingleDecompressedPdfStream(int maxSizeOfSingleDecompressedPdfStream) {
        this.maxSizeOfSingleDecompressedPdfStream = maxSizeOfSingleDecompressedPdfStream;
        return this;
    }

    public long getMaxSizeOfDecompressedPdfStreamsSum() {
        return this.maxSizeOfDecompressedPdfStreamsSum;
    }

    public MemoryLimitsAwareHandler setMaxSizeOfDecompressedPdfStreamsSum(long maxSizeOfDecompressedPdfStreamsSum) {
        this.maxSizeOfDecompressedPdfStreamsSum = maxSizeOfDecompressedPdfStreamsSum;
        return this;
    }

    MemoryLimitsAwareHandler considerBytesOccupiedByDecompressedPdfStream(long numOfOccupiedBytes) {
        if (this.considerCurrentPdfStream && this.memoryUsedForCurrentPdfStreamDecompression < numOfOccupiedBytes) {
            this.memoryUsedForCurrentPdfStreamDecompression = numOfOccupiedBytes;
            if (this.memoryUsedForCurrentPdfStreamDecompression > this.maxSizeOfSingleDecompressedPdfStream) {
                throw new MemoryLimitsAwareException(PdfException.DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed);
            }
        }
        return this;
    }

    MemoryLimitsAwareHandler beginDecompressedPdfStreamProcessing() {
        ensureCurrentStreamIsReset();
        this.considerCurrentPdfStream = true;
        return this;
    }

    MemoryLimitsAwareHandler endDecompressedPdfStreamProcessing() {
        this.allMemoryUsedForDecompression += this.memoryUsedForCurrentPdfStreamDecompression;
        if (this.allMemoryUsedForDecompression > this.maxSizeOfDecompressedPdfStreamsSum) {
            throw new MemoryLimitsAwareException(PdfException.DuringDecompressionMultipleStreamsInSumOccupiedMoreMemoryThanAllowed);
        }
        ensureCurrentStreamIsReset();
        this.considerCurrentPdfStream = false;
        return this;
    }

    long getAllMemoryUsedForDecompression() {
        return this.allMemoryUsedForDecompression;
    }

    private static long calculateDefaultParameter(long documentSize, int scale, long min) {
        long result = documentSize * scale;
        if (result < min) {
            result = min;
        }
        if (result > min * scale) {
            result = min * scale;
        }
        return result;
    }

    private void ensureCurrentStreamIsReset() {
        this.memoryUsedForCurrentPdfStreamDecompression = 0L;
    }
}
