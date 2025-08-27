package org.apache.commons.compress.archivers.tar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/tar/TarArchiveInputStream.class */
public class TarArchiveInputStream extends ArchiveInputStream {
    private static final int SMALL_BUFFER_SIZE = 256;
    private final byte[] smallBuf;
    private final int recordSize;
    private final int blockSize;
    private boolean hasHitEOF;
    private long entrySize;
    private long entryOffset;
    private final InputStream is;
    private TarArchiveEntry currEntry;
    private final ZipEncoding zipEncoding;
    final String encoding;
    private Map<String, String> globalPaxHeaders;
    private final boolean lenient;

    public TarArchiveInputStream(InputStream is) {
        this(is, 10240, 512);
    }

    public TarArchiveInputStream(InputStream is, boolean lenient) {
        this(is, 10240, 512, null, lenient);
    }

    public TarArchiveInputStream(InputStream is, String encoding) {
        this(is, 10240, 512, encoding);
    }

    public TarArchiveInputStream(InputStream is, int blockSize) {
        this(is, blockSize, 512);
    }

    public TarArchiveInputStream(InputStream is, int blockSize, String encoding) {
        this(is, blockSize, 512, encoding);
    }

    public TarArchiveInputStream(InputStream is, int blockSize, int recordSize) {
        this(is, blockSize, recordSize, null);
    }

    public TarArchiveInputStream(InputStream is, int blockSize, int recordSize, String encoding) {
        this(is, blockSize, recordSize, encoding, false);
    }

    public TarArchiveInputStream(InputStream is, int blockSize, int recordSize, String encoding, boolean lenient) {
        this.smallBuf = new byte[256];
        this.globalPaxHeaders = new HashMap();
        this.is = is;
        this.hasHitEOF = false;
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.recordSize = recordSize;
        this.blockSize = blockSize;
        this.lenient = lenient;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.is.close();
    }

    public int getRecordSize() {
        return this.recordSize;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (isDirectory()) {
            return 0;
        }
        if (this.entrySize - this.entryOffset > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) (this.entrySize - this.entryOffset);
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (n <= 0 || isDirectory()) {
            return 0L;
        }
        long available = this.entrySize - this.entryOffset;
        long skipped = IOUtils.skip(this.is, Math.min(n, available));
        count(skipped);
        this.entryOffset += skipped;
        return skipped;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public void mark(int markLimit) {
    }

    @Override // java.io.InputStream
    public synchronized void reset() {
    }

    public TarArchiveEntry getNextTarEntry() throws IOException {
        if (isAtEOF()) {
            return null;
        }
        if (this.currEntry != null) {
            IOUtils.skip(this, Long.MAX_VALUE);
            skipRecordPadding();
        }
        byte[] headerBuf = getRecord();
        if (headerBuf == null) {
            this.currEntry = null;
            return null;
        }
        try {
            this.currEntry = new TarArchiveEntry(headerBuf, this.zipEncoding, this.lenient);
            this.entryOffset = 0L;
            this.entrySize = this.currEntry.getSize();
            if (this.currEntry.isGNULongLinkEntry()) {
                byte[] longLinkData = getLongNameData();
                if (longLinkData == null) {
                    return null;
                }
                this.currEntry.setLinkName(this.zipEncoding.decode(longLinkData));
            }
            if (this.currEntry.isGNULongNameEntry()) {
                byte[] longNameData = getLongNameData();
                if (longNameData == null) {
                    return null;
                }
                this.currEntry.setName(this.zipEncoding.decode(longNameData));
            }
            if (this.currEntry.isGlobalPaxHeader()) {
                readGlobalPaxHeaders();
            }
            if (this.currEntry.isPaxHeader()) {
                paxHeaders();
            } else if (!this.globalPaxHeaders.isEmpty()) {
                applyPaxHeadersToCurrentEntry(this.globalPaxHeaders);
            }
            if (this.currEntry.isOldGNUSparse()) {
                readOldGNUSparse();
            }
            this.entrySize = this.currEntry.getSize();
            return this.currEntry;
        } catch (IllegalArgumentException e) {
            throw new IOException("Error detected parsing the header", e);
        }
    }

    private void skipRecordPadding() throws IOException {
        if (!isDirectory() && this.entrySize > 0 && this.entrySize % this.recordSize != 0) {
            long numRecords = (this.entrySize / this.recordSize) + 1;
            long padding = (numRecords * this.recordSize) - this.entrySize;
            long skipped = IOUtils.skip(this.is, padding);
            count(skipped);
        }
    }

    protected byte[] getLongNameData() throws IOException {
        ByteArrayOutputStream longName = new ByteArrayOutputStream();
        while (true) {
            int length = read(this.smallBuf);
            if (length < 0) {
                break;
            }
            longName.write(this.smallBuf, 0, length);
        }
        getNextEntry();
        if (this.currEntry == null) {
            return null;
        }
        byte[] longNameData = longName.toByteArray();
        int length2 = longNameData.length;
        while (length2 > 0 && longNameData[length2 - 1] == 0) {
            length2--;
        }
        if (length2 != longNameData.length) {
            byte[] l = new byte[length2];
            System.arraycopy(longNameData, 0, l, 0, length2);
            longNameData = l;
        }
        return longNameData;
    }

    private byte[] getRecord() throws IOException {
        byte[] headerBuf = readRecord();
        setAtEOF(isEOFRecord(headerBuf));
        if (isAtEOF() && headerBuf != null) {
            tryToConsumeSecondEOFRecord();
            consumeRemainderOfLastBlock();
            headerBuf = null;
        }
        return headerBuf;
    }

    protected boolean isEOFRecord(byte[] record) {
        return record == null || ArchiveUtils.isArrayZero(record, this.recordSize);
    }

    protected byte[] readRecord() throws IOException {
        byte[] record = new byte[this.recordSize];
        int readNow = IOUtils.readFully(this.is, record);
        count(readNow);
        if (readNow != this.recordSize) {
            return null;
        }
        return record;
    }

    private void readGlobalPaxHeaders() throws IOException {
        this.globalPaxHeaders = parsePaxHeaders(this);
        getNextEntry();
    }

    private void paxHeaders() throws IOException {
        Map<String, String> headers = parsePaxHeaders(this);
        getNextEntry();
        applyPaxHeadersToCurrentEntry(headers);
    }

    Map<String, String> parsePaxHeaders(InputStream i) throws IOException {
        int ch2;
        Map<String, String> headers = new HashMap<>(this.globalPaxHeaders);
        do {
            int len = 0;
            int read = 0;
            while (true) {
                int i2 = i.read();
                ch2 = i2;
                if (i2 == -1) {
                    break;
                }
                read++;
                if (ch2 == 10) {
                    break;
                }
                if (ch2 == 32) {
                    ByteArrayOutputStream coll = new ByteArrayOutputStream();
                    while (true) {
                        int i3 = i.read();
                        ch2 = i3;
                        if (i3 == -1) {
                            break;
                        }
                        read++;
                        if (ch2 == 61) {
                            String keyword = coll.toString("UTF-8");
                            int restLen = len - read;
                            if (restLen == 1) {
                                headers.remove(keyword);
                            } else {
                                byte[] rest = new byte[restLen];
                                int got = IOUtils.readFully(i, rest);
                                if (got != restLen) {
                                    throw new IOException("Failed to read Paxheader. Expected " + restLen + " bytes, read " + got);
                                }
                                String value = new String(rest, 0, restLen - 1, "UTF-8");
                                headers.put(keyword, value);
                            }
                        } else {
                            coll.write((byte) ch2);
                        }
                    }
                } else {
                    len = (len * 10) + (ch2 - 48);
                }
            }
        } while (ch2 != -1);
        return headers;
    }

    private void applyPaxHeadersToCurrentEntry(Map<String, String> headers) {
        this.currEntry.updateEntryFromPaxHeaders(headers);
    }

    private void readOldGNUSparse() throws IOException {
        TarArchiveSparseEntry entry;
        if (this.currEntry.isExtended()) {
            do {
                byte[] headerBuf = getRecord();
                if (headerBuf == null) {
                    this.currEntry = null;
                    return;
                }
                entry = new TarArchiveSparseEntry(headerBuf);
            } while (entry.isExtended());
        }
    }

    private boolean isDirectory() {
        return this.currEntry != null && this.currEntry.isDirectory();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public ArchiveEntry getNextEntry() throws IOException {
        return getNextTarEntry();
    }

    private void tryToConsumeSecondEOFRecord() throws IOException {
        boolean shouldReset = true;
        boolean marked = this.is.markSupported();
        if (marked) {
            this.is.mark(this.recordSize);
        }
        try {
            shouldReset = !isEOFRecord(readRecord());
        } finally {
            if (shouldReset && marked) {
                pushedBackBytes(this.recordSize);
                this.is.reset();
            }
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] buf, int offset, int numToRead) throws IOException {
        if (isAtEOF() || isDirectory() || this.entryOffset >= this.entrySize) {
            return -1;
        }
        if (this.currEntry == null) {
            throw new IllegalStateException("No current tar entry");
        }
        int numToRead2 = Math.min(numToRead, available());
        int totalRead = this.is.read(buf, offset, numToRead2);
        if (totalRead == -1) {
            if (numToRead2 > 0) {
                throw new IOException("Truncated TAR archive");
            }
            setAtEOF(true);
        } else {
            count(totalRead);
            this.entryOffset += totalRead;
        }
        return totalRead;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public boolean canReadEntryData(ArchiveEntry ae) {
        if (ae instanceof TarArchiveEntry) {
            TarArchiveEntry te = (TarArchiveEntry) ae;
            return !te.isSparse();
        }
        return false;
    }

    public TarArchiveEntry getCurrentEntry() {
        return this.currEntry;
    }

    protected final void setCurrentEntry(TarArchiveEntry e) {
        this.currEntry = e;
    }

    protected final boolean isAtEOF() {
        return this.hasHitEOF;
    }

    protected final void setAtEOF(boolean b) {
        this.hasHitEOF = b;
    }

    private void consumeRemainderOfLastBlock() throws IOException {
        long bytesReadOfLastBlock = getBytesRead() % this.blockSize;
        if (bytesReadOfLastBlock > 0) {
            long skipped = IOUtils.skip(this.is, this.blockSize - bytesReadOfLastBlock);
            count(skipped);
        }
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < 265) {
            return false;
        }
        if (ArchiveUtils.matchAsciiBuffer("ustar��", signature, 257, 6) && ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_POSIX, signature, 263, 2)) {
            return true;
        }
        if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_GNU, signature, 257, 6) && (ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_GNU_SPACE, signature, 263, 2) || ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_GNU_ZERO, signature, 263, 2))) {
            return true;
        }
        return ArchiveUtils.matchAsciiBuffer("ustar��", signature, 257, 6) && ArchiveUtils.matchAsciiBuffer(TarConstants.VERSION_ANT, signature, 263, 2);
    }
}
