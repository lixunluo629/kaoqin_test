package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ExtraFieldParsingBehavior.class */
public interface ExtraFieldParsingBehavior extends UnparseableExtraFieldBehavior {
    ZipExtraField createExtraField(ZipShort zipShort) throws IllegalAccessException, ZipException, InstantiationException;

    ZipExtraField fill(ZipExtraField zipExtraField, byte[] bArr, int i, int i2, boolean z) throws ZipException;
}
