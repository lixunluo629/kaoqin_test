package org.apache.commons.compress.archivers;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/StreamingNotSupportedException.class */
public class StreamingNotSupportedException extends ArchiveException {
    private static final long serialVersionUID = 1;
    private final String format;

    public StreamingNotSupportedException(String format) {
        super("The " + format + " doesn't support streaming.");
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }
}
