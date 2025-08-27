package org.apache.poi.openxml4j.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipEntrySource.class */
public interface ZipEntrySource extends Closeable {
    Enumeration<? extends ZipEntry> getEntries();

    InputStream getInputStream(ZipEntry zipEntry) throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    boolean isClosed();
}
