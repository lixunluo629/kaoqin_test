package org.apache.commons.compress.compressors.snappy;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/compressors/snappy/FramedSnappyDialect.class */
public enum FramedSnappyDialect {
    STANDARD(true, true),
    IWORK_ARCHIVE(false, false);

    private final boolean streamIdentifier;
    private final boolean checksumWithCompressedChunks;

    FramedSnappyDialect(boolean hasStreamIdentifier, boolean usesChecksumWithCompressedChunks) {
        this.streamIdentifier = hasStreamIdentifier;
        this.checksumWithCompressedChunks = usesChecksumWithCompressedChunks;
    }

    boolean hasStreamIdentifier() {
        return this.streamIdentifier;
    }

    boolean usesChecksumWithCompressedChunks() {
        return this.checksumWithCompressedChunks;
    }
}
