package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/Zip64RequiredException.class */
public class Zip64RequiredException extends ZipException {
    private static final long serialVersionUID = 20110809;
    static final String ARCHIVE_TOO_BIG_MESSAGE = "Archive's size exceeds the limit of 4GByte.";
    static final String TOO_MANY_ENTRIES_MESSAGE = "Archive contains more than 65535 entries.";

    static String getEntryTooBigMessage(ZipArchiveEntry ze) {
        return ze.getName() + "'s size exceeds the limit of 4GByte.";
    }

    public Zip64RequiredException(String reason) {
        super(reason);
    }
}
