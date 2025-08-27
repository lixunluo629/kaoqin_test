package org.apache.poi.openxml4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipFileZipEntrySource.class */
public class ZipFileZipEntrySource implements ZipEntrySource {
    private ZipFile zipArchive;

    public ZipFileZipEntrySource(ZipFile zipFile) {
        this.zipArchive = zipFile;
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.zipArchive != null) {
            this.zipArchive.close();
        }
        this.zipArchive = null;
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource
    public boolean isClosed() {
        return this.zipArchive == null;
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource
    public Enumeration<? extends ZipEntry> getEntries() {
        if (this.zipArchive == null) {
            throw new IllegalStateException("Zip File is closed");
        }
        return this.zipArchive.entries();
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource
    public InputStream getInputStream(ZipEntry entry) throws IOException {
        if (this.zipArchive == null) {
            throw new IllegalStateException("Zip File is closed");
        }
        return this.zipArchive.getInputStream(entry);
    }
}
