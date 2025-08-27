package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/zip/ZipEncoding.class */
public interface ZipEncoding {
    boolean canEncode(String str);

    ByteBuffer encode(String str) throws IOException;

    String decode(byte[] bArr) throws IOException;
}
