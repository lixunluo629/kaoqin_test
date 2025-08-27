package org.apache.poi.openxml4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import org.apache.poi.openxml4j.util.ZipSecureFile;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipInputStreamZipEntrySource.class */
public class ZipInputStreamZipEntrySource implements ZipEntrySource {
    private ArrayList<FakeZipEntry> zipEntries = new ArrayList<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ZipInputStreamZipEntrySource.class.desiredAssertionStatus();
    }

    public ZipInputStreamZipEntrySource(ZipSecureFile.ThresholdInputStream inp) throws IOException {
        boolean going = true;
        while (going) {
            ZipEntry zipEntry = inp.getNextEntry();
            if (zipEntry == null) {
                going = false;
            } else {
                FakeZipEntry entry = new FakeZipEntry(zipEntry, inp);
                inp.closeEntry();
                this.zipEntries.add(entry);
            }
        }
        inp.close();
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource
    public Enumeration<? extends ZipEntry> getEntries() {
        return new EntryEnumerator();
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource
    public InputStream getInputStream(ZipEntry zipEntry) {
        if (!$assertionsDisabled && !(zipEntry instanceof FakeZipEntry)) {
            throw new AssertionError();
        }
        FakeZipEntry entry = (FakeZipEntry) zipEntry;
        return entry.getInputStream();
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.zipEntries = null;
    }

    @Override // org.apache.poi.openxml4j.util.ZipEntrySource
    public boolean isClosed() {
        return this.zipEntries == null;
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipInputStreamZipEntrySource$EntryEnumerator.class */
    private class EntryEnumerator implements Enumeration<ZipEntry> {
        private Iterator<? extends ZipEntry> iterator;

        private EntryEnumerator() {
            this.iterator = ZipInputStreamZipEntrySource.this.zipEntries.iterator();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public ZipEntry nextElement() {
            return this.iterator.next();
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipInputStreamZipEntrySource$FakeZipEntry.class */
    public static class FakeZipEntry extends ZipEntry {
        private byte[] data;

        public FakeZipEntry(ZipEntry entry, InputStream inp) throws IOException {
            ByteArrayOutputStream baos;
            super(entry.getName());
            long entrySize = entry.getSize();
            if (entrySize == -1) {
                baos = new ByteArrayOutputStream();
            } else {
                if (entrySize >= 2147483647L) {
                    throw new IOException("ZIP entry size is too large");
                }
                baos = new ByteArrayOutputStream((int) entrySize);
            }
            byte[] buffer = new byte[4096];
            while (true) {
                int read = inp.read(buffer);
                if (read != -1) {
                    baos.write(buffer, 0, read);
                } else {
                    this.data = baos.toByteArray();
                    return;
                }
            }
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.data);
        }
    }
}
