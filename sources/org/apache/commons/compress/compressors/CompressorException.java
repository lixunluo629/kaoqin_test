package org.apache.commons.compress.compressors;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/compressors/CompressorException.class */
public class CompressorException extends Exception {
    private static final long serialVersionUID = -2932901310255908814L;

    public CompressorException(String message) {
        super(message);
    }

    public CompressorException(String message, Throwable cause) {
        super(message, cause);
    }
}
