package org.apache.commons.compress.archivers.dump;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/dump/InvalidFormatException.class */
public class InvalidFormatException extends DumpArchiveException {
    private static final long serialVersionUID = 1;
    protected long offset;

    public InvalidFormatException() {
        super("there was an error decoding a tape segment");
    }

    public InvalidFormatException(long offset) {
        super("there was an error decoding a tape segment header at offset " + offset + ".");
        this.offset = offset;
    }

    public long getOffset() {
        return this.offset;
    }
}
