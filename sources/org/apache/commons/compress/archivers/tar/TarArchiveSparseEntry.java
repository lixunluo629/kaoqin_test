package org.apache.commons.compress.archivers.tar;

import java.io.IOException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/tar/TarArchiveSparseEntry.class */
public class TarArchiveSparseEntry implements TarConstants {
    private final boolean isExtended;

    public TarArchiveSparseEntry(byte[] headerBuf) throws IOException {
        int offset = 0 + 504;
        this.isExtended = TarUtils.parseBoolean(headerBuf, offset);
    }

    public boolean isExtended() {
        return this.isExtended;
    }
}
