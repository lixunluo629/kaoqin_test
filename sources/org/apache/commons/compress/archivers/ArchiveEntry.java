package org.apache.commons.compress.archivers;

import java.util.Date;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/ArchiveEntry.class */
public interface ArchiveEntry {
    public static final long SIZE_UNKNOWN = -1;

    String getName();

    long getSize();

    boolean isDirectory();

    Date getLastModifiedDate();
}
