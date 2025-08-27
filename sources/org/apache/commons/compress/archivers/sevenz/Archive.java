package org.apache.commons.compress.archivers.sevenz;

import java.util.BitSet;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/sevenz/Archive.class */
class Archive {
    long packPos;
    long[] packSizes;
    BitSet packCrcsDefined;
    long[] packCrcs;
    Folder[] folders;
    SubStreamsInfo subStreamsInfo;
    SevenZArchiveEntry[] files;
    StreamMap streamMap;

    Archive() {
    }

    public String toString() {
        return "Archive with packed streams starting at offset " + this.packPos + ", " + lengthOf(this.packSizes) + " pack sizes, " + lengthOf(this.packCrcs) + " CRCs, " + lengthOf(this.folders) + " folders, " + lengthOf(this.files) + " files and " + this.streamMap;
    }

    private static String lengthOf(long[] a) {
        return a == null ? "(null)" : String.valueOf(a.length);
    }

    private static String lengthOf(Object[] a) {
        return a == null ? "(null)" : String.valueOf(a.length);
    }
}
