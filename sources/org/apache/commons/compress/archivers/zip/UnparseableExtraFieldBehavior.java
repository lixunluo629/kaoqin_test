package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/UnparseableExtraFieldBehavior.class */
public interface UnparseableExtraFieldBehavior {
    ZipExtraField onUnparseableExtraField(byte[] bArr, int i, int i2, boolean z, int i3) throws ZipException;
}
