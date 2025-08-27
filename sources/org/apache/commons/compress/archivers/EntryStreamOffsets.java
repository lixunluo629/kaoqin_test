package org.apache.commons.compress.archivers;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/EntryStreamOffsets.class */
public interface EntryStreamOffsets {
    public static final long OFFSET_UNKNOWN = -1;

    long getDataOffset();

    boolean isStreamContiguous();
}
