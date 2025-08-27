package org.apache.commons.compress.archivers.tar;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.FixedLengthBlockOutputStream;
import org.apache.commons.httpclient.auth.NTLM;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.servlet.tags.form.InputTag;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/tar/TarArchiveOutputStream.class */
public class TarArchiveOutputStream extends ArchiveOutputStream {
    public static final int LONGFILE_ERROR = 0;
    public static final int LONGFILE_TRUNCATE = 1;
    public static final int LONGFILE_GNU = 2;
    public static final int LONGFILE_POSIX = 3;
    public static final int BIGNUMBER_ERROR = 0;
    public static final int BIGNUMBER_STAR = 1;
    public static final int BIGNUMBER_POSIX = 2;
    private static final int RECORD_SIZE = 512;
    private long currSize;
    private String currName;
    private long currBytes;
    private final byte[] recordBuf;
    private int longFileMode;
    private int bigNumberMode;
    private int recordsWritten;
    private final int recordsPerBlock;
    private boolean closed;
    private boolean haveUnclosedEntry;
    private boolean finished;
    private final FixedLengthBlockOutputStream out;
    private final CountingOutputStream countingOut;
    private final ZipEncoding zipEncoding;
    final String encoding;
    private boolean addPaxHeadersForNonAsciiNames;
    private static final ZipEncoding ASCII = ZipEncodingHelper.getZipEncoding(NTLM.DEFAULT_CHARSET);
    private static final int BLOCK_SIZE_UNSPECIFIED = -511;

    public TarArchiveOutputStream(OutputStream os) {
        this(os, BLOCK_SIZE_UNSPECIFIED);
    }

    public TarArchiveOutputStream(OutputStream os, String encoding) {
        this(os, BLOCK_SIZE_UNSPECIFIED, encoding);
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize) {
        this(os, blockSize, (String) null);
    }

    @Deprecated
    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize) {
        this(os, blockSize, recordSize, null);
    }

    @Deprecated
    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize, String encoding) {
        this(os, blockSize, encoding);
        if (recordSize != 512) {
            throw new IllegalArgumentException("Tar record size must always be 512 bytes. Attempt to set size of " + recordSize);
        }
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize, String encoding) {
        int realBlockSize;
        this.longFileMode = 0;
        this.bigNumberMode = 0;
        this.closed = false;
        this.haveUnclosedEntry = false;
        this.finished = false;
        this.addPaxHeadersForNonAsciiNames = false;
        if (BLOCK_SIZE_UNSPECIFIED == blockSize) {
            realBlockSize = 512;
        } else {
            realBlockSize = blockSize;
        }
        if (realBlockSize <= 0 || realBlockSize % 512 != 0) {
            throw new IllegalArgumentException("Block size must be a multiple of 512 bytes. Attempt to use set size of " + blockSize);
        }
        CountingOutputStream countingOutputStream = new CountingOutputStream(os);
        this.countingOut = countingOutputStream;
        this.out = new FixedLengthBlockOutputStream(countingOutputStream, 512);
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.recordBuf = new byte[512];
        this.recordsPerBlock = realBlockSize / 512;
    }

    public void setLongFileMode(int longFileMode) {
        this.longFileMode = longFileMode;
    }

    public void setBigNumberMode(int bigNumberMode) {
        this.bigNumberMode = bigNumberMode;
    }

    public void setAddPaxHeadersForNonAsciiNames(boolean b) {
        this.addPaxHeadersForNonAsciiNames = b;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    @Deprecated
    public int getCount() {
        return (int) getBytesWritten();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public long getBytesWritten() {
        return this.countingOut.getBytesWritten();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.haveUnclosedEntry) {
            throw new IOException("This archive contains unclosed entries.");
        }
        writeEOFRecord();
        writeEOFRecord();
        padAsNeeded();
        this.out.flush();
        this.finished = true;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            if (!this.finished) {
                finish();
            }
        } finally {
            if (!this.closed) {
                this.out.close();
                this.closed = true;
            }
        }
    }

    @Deprecated
    public int getRecordSize() {
        return 512;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        TarArchiveEntry entry = (TarArchiveEntry) archiveEntry;
        if (entry.isGlobalPaxHeader()) {
            byte[] data = encodeExtendedPaxHeadersContents(entry.getExtraPaxHeaders());
            entry.setSize(data.length);
            entry.writeEntryHeader(this.recordBuf, this.zipEncoding, this.bigNumberMode == 1);
            writeRecord(this.recordBuf);
            this.currSize = entry.getSize();
            this.currBytes = 0L;
            this.haveUnclosedEntry = true;
            write(data);
            closeArchiveEntry();
            return;
        }
        Map<String, String> paxHeaders = new HashMap<>();
        String entryName = entry.getName();
        boolean paxHeaderContainsPath = handleLongName(entry, entryName, paxHeaders, Cookie2.PATH, (byte) 76, "file name");
        String linkName = entry.getLinkName();
        boolean paxHeaderContainsLinkPath = linkName != null && linkName.length() > 0 && handleLongName(entry, linkName, paxHeaders, "linkpath", (byte) 75, "link name");
        if (this.bigNumberMode == 2) {
            addPaxHeadersForBigNumbers(paxHeaders, entry);
        } else if (this.bigNumberMode != 1) {
            failForBigNumbers(entry);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath && !ASCII.canEncode(entryName)) {
            paxHeaders.put(Cookie2.PATH, entryName);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsLinkPath && ((entry.isLink() || entry.isSymbolicLink()) && !ASCII.canEncode(linkName))) {
            paxHeaders.put("linkpath", linkName);
        }
        paxHeaders.putAll(entry.getExtraPaxHeaders());
        if (paxHeaders.size() > 0) {
            writePaxHeaders(entry, entryName, paxHeaders);
        }
        entry.writeEntryHeader(this.recordBuf, this.zipEncoding, this.bigNumberMode == 1);
        writeRecord(this.recordBuf);
        this.currBytes = 0L;
        if (entry.isDirectory()) {
            this.currSize = 0L;
        } else {
            this.currSize = entry.getSize();
        }
        this.currName = entryName;
        this.haveUnclosedEntry = true;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (!this.haveUnclosedEntry) {
            throw new IOException("No current entry to close");
        }
        this.out.flushBlock();
        if (this.currBytes < this.currSize) {
            throw new IOException("Entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
        }
        this.recordsWritten = (int) (this.recordsWritten + (this.currSize / 512));
        if (0 != this.currSize % 512) {
            this.recordsWritten++;
        }
        this.haveUnclosedEntry = false;
    }

    @Override // java.io.OutputStream
    public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
        if (!this.haveUnclosedEntry) {
            throw new IllegalStateException("No current tar entry");
        }
        if (this.currBytes + numToWrite > this.currSize) {
            throw new IOException("Request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
        }
        this.out.write(wBuf, wOffset, numToWrite);
        this.currBytes += numToWrite;
    }

    void writePaxHeaders(TarArchiveEntry entry, String entryName, Map<String, String> headers) throws IOException {
        String name = "./PaxHeaders.X/" + stripTo7Bits(entryName);
        if (name.length() >= 100) {
            name = name.substring(0, 99);
        }
        TarArchiveEntry pex = new TarArchiveEntry(name, (byte) 120);
        transferModTime(entry, pex);
        byte[] data = encodeExtendedPaxHeadersContents(headers);
        pex.setSize(data.length);
        putArchiveEntry(pex);
        write(data);
        closeArchiveEntry();
    }

    private byte[] encodeExtendedPaxHeadersContents(Map<String, String> headers) throws UnsupportedEncodingException {
        StringWriter w = new StringWriter();
        for (Map.Entry<String, String> h : headers.entrySet()) {
            String key = h.getKey();
            String value = h.getValue();
            int len = key.length() + value.length() + 3 + 2;
            String line = len + SymbolConstants.SPACE_SYMBOL + key + SymbolConstants.EQUAL_SYMBOL + value + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
            int length = line.getBytes("UTF-8").length;
            while (true) {
                int actualLength = length;
                if (len != actualLength) {
                    len = actualLength;
                    line = len + SymbolConstants.SPACE_SYMBOL + key + SymbolConstants.EQUAL_SYMBOL + value + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
                    length = line.getBytes("UTF-8").length;
                }
            }
            w.write(line);
        }
        return w.toString().getBytes("UTF-8");
    }

    private String stripTo7Bits(String name) {
        int length = name.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char stripped = (char) (name.charAt(i) & 127);
            if (shouldBeReplaced(stripped)) {
                result.append("_");
            } else {
                result.append(stripped);
            }
        }
        return result.toString();
    }

    private boolean shouldBeReplaced(char c) {
        return c == 0 || c == '/' || c == '\\';
    }

    private void writeEOFRecord() throws IOException {
        Arrays.fill(this.recordBuf, (byte) 0);
        writeRecord(this.recordBuf);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new TarArchiveEntry(inputFile, entryName);
    }

    private void writeRecord(byte[] record) throws IOException {
        if (record.length != 512) {
            throw new IOException("Record to write has length '" + record.length + "' which is not the record size of '512'");
        }
        this.out.write(record);
        this.recordsWritten++;
    }

    private void padAsNeeded() throws IOException {
        int start = this.recordsWritten % this.recordsPerBlock;
        if (start != 0) {
            for (int i = start; i < this.recordsPerBlock; i++) {
                writeEOFRecord();
            }
        }
    }

    private void addPaxHeadersForBigNumbers(Map<String, String> paxHeaders, TarArchiveEntry entry) {
        addPaxHeaderForBigNumber(paxHeaders, InputTag.SIZE_ATTRIBUTE, entry.getSize(), TarConstants.MAXSIZE);
        addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getLongGroupId(), TarConstants.MAXID);
        addPaxHeaderForBigNumber(paxHeaders, "mtime", entry.getModTime().getTime() / 1000, TarConstants.MAXSIZE);
        addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getLongUserId(), TarConstants.MAXID);
        addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor", entry.getDevMajor(), TarConstants.MAXID);
        addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor", entry.getDevMinor(), TarConstants.MAXID);
        failForBigNumber("mode", entry.getMode(), TarConstants.MAXID);
    }

    private void addPaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, long value, long maxValue) {
        if (value < 0 || value > maxValue) {
            paxHeaders.put(header, String.valueOf(value));
        }
    }

    private void failForBigNumbers(TarArchiveEntry entry) {
        failForBigNumber("entry size", entry.getSize(), TarConstants.MAXSIZE);
        failForBigNumberWithPosixMessage("group id", entry.getLongGroupId(), TarConstants.MAXID);
        failForBigNumber("last modification time", entry.getModTime().getTime() / 1000, TarConstants.MAXSIZE);
        failForBigNumber("user id", entry.getLongUserId(), TarConstants.MAXID);
        failForBigNumber("mode", entry.getMode(), TarConstants.MAXID);
        failForBigNumber("major device number", entry.getDevMajor(), TarConstants.MAXID);
        failForBigNumber("minor device number", entry.getDevMinor(), TarConstants.MAXID);
    }

    private void failForBigNumber(String field, long value, long maxValue) {
        failForBigNumber(field, value, maxValue, "");
    }

    private void failForBigNumberWithPosixMessage(String field, long value, long maxValue) {
        failForBigNumber(field, value, maxValue, " Use STAR or POSIX extensions to overcome this limit");
    }

    private void failForBigNumber(String field, long value, long maxValue, String additionalMsg) {
        if (value < 0 || value > maxValue) {
            throw new RuntimeException(field + " '" + value + "' is too big ( > " + maxValue + " )." + additionalMsg);
        }
    }

    private boolean handleLongName(TarArchiveEntry entry, String name, Map<String, String> paxHeaders, String paxHeaderName, byte linkType, String fieldName) throws IOException {
        ByteBuffer encodedName = this.zipEncoding.encode(name);
        int len = encodedName.limit() - encodedName.position();
        if (len >= 100) {
            if (this.longFileMode == 3) {
                paxHeaders.put(paxHeaderName, name);
                return true;
            }
            if (this.longFileMode != 2) {
                if (this.longFileMode != 1) {
                    throw new RuntimeException(fieldName + " '" + name + "' is too long ( > 100 bytes)");
                }
                return false;
            }
            TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, linkType);
            longLinkEntry.setSize(len + 1);
            transferModTime(entry, longLinkEntry);
            putArchiveEntry(longLinkEntry);
            write(encodedName.array(), encodedName.arrayOffset(), len);
            write(0);
            closeArchiveEntry();
            return false;
        }
        return false;
    }

    private void transferModTime(TarArchiveEntry from, TarArchiveEntry to) {
        Date fromModTime = from.getModTime();
        long fromModTimeSeconds = fromModTime.getTime() / 1000;
        if (fromModTimeSeconds < 0 || fromModTimeSeconds > TarConstants.MAXSIZE) {
            fromModTime = new Date(0L);
        }
        to.setModTime(fromModTime);
    }
}
