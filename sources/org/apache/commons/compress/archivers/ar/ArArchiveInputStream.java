package org.apache.commons.compress.archivers.ar;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/ar/ArArchiveInputStream.class */
public class ArArchiveInputStream extends ArchiveInputStream {
    private final InputStream input;
    private static final int NAME_OFFSET = 0;
    private static final int NAME_LEN = 16;
    private static final int LAST_MODIFIED_OFFSET = 16;
    private static final int LAST_MODIFIED_LEN = 12;
    private static final int USER_ID_OFFSET = 28;
    private static final int USER_ID_LEN = 6;
    private static final int GROUP_ID_OFFSET = 34;
    private static final int GROUP_ID_LEN = 6;
    private static final int FILE_MODE_OFFSET = 40;
    private static final int FILE_MODE_LEN = 8;
    private static final int LENGTH_OFFSET = 48;
    private static final int LENGTH_LEN = 10;
    static final String BSD_LONGNAME_PREFIX = "#1/";
    private static final int BSD_LONGNAME_PREFIX_LEN = BSD_LONGNAME_PREFIX.length();
    private static final String BSD_LONGNAME_PATTERN = "^#1/\\d+";
    private static final String GNU_STRING_TABLE_NAME = "//";
    private static final String GNU_LONGNAME_PATTERN = "^/\\d+";
    private long offset = 0;
    private ArArchiveEntry currentEntry = null;
    private byte[] namebuffer = null;
    private long entryOffset = -1;
    private final byte[] metaData = new byte[58];
    private boolean closed = false;

    public ArArchiveInputStream(InputStream pInput) {
        this.input = pInput;
    }

    public ArArchiveEntry getNextArEntry() throws IOException, NumberFormatException {
        if (this.currentEntry != null) {
            long entryEnd = this.entryOffset + this.currentEntry.getLength();
            long skipped = IOUtils.skip(this.input, entryEnd - this.offset);
            trackReadBytes(skipped);
            this.currentEntry = null;
        }
        if (this.offset == 0) {
            byte[] expected = ArchiveUtils.toAsciiBytes(ArArchiveEntry.HEADER);
            byte[] realized = new byte[expected.length];
            int read = IOUtils.readFully(this.input, realized);
            trackReadBytes(read);
            if (read != expected.length) {
                throw new IOException("Failed to read header. Occured at byte: " + getBytesRead());
            }
            if (!Arrays.equals(expected, realized)) {
                throw new IOException("Invalid header " + ArchiveUtils.toAsciiString(realized));
            }
        }
        if (this.offset % 2 != 0) {
            if (this.input.read() < 0) {
                return null;
            }
            trackReadBytes(1L);
        }
        int read2 = IOUtils.readFully(this.input, this.metaData);
        trackReadBytes(read2);
        if (read2 == 0) {
            return null;
        }
        if (read2 < this.metaData.length) {
            throw new IOException("Truncated ar archive");
        }
        byte[] expected2 = ArchiveUtils.toAsciiBytes(ArArchiveEntry.TRAILER);
        byte[] realized2 = new byte[expected2.length];
        int read3 = IOUtils.readFully(this.input, realized2);
        trackReadBytes(read3);
        if (read3 != expected2.length) {
            throw new IOException("Failed to read entry trailer. Occured at byte: " + getBytesRead());
        }
        if (!Arrays.equals(expected2, realized2)) {
            throw new IOException("Invalid entry trailer. not read the content? Occured at byte: " + getBytesRead());
        }
        this.entryOffset = this.offset;
        String temp = ArchiveUtils.toAsciiString(this.metaData, 0, 16).trim();
        if (isGNUStringTable(temp)) {
            this.currentEntry = readGNUStringTable(this.metaData, 48, 10);
            return getNextArEntry();
        }
        long len = asLong(this.metaData, 48, 10);
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        } else if (isGNULongName(temp)) {
            int off = Integer.parseInt(temp.substring(1));
            temp = getExtendedName(off);
        } else if (isBSDLongName(temp)) {
            temp = getBSDLongName(temp);
            int nameLen = temp.length();
            len -= nameLen;
            this.entryOffset += nameLen;
        }
        this.currentEntry = new ArArchiveEntry(temp, len, asInt(this.metaData, 28, 6, true), asInt(this.metaData, 34, 6, true), asInt(this.metaData, 40, 8, 8), asLong(this.metaData, 16, 12));
        return this.currentEntry;
    }

    private String getExtendedName(int offset) throws IOException {
        if (this.namebuffer == null) {
            throw new IOException("Cannot process GNU long filename as no // record was found");
        }
        int i = offset;
        while (i < this.namebuffer.length) {
            if (this.namebuffer[i] != 10 && this.namebuffer[i] != 0) {
                i++;
            } else {
                if (this.namebuffer[i - 1] == 47) {
                    i--;
                }
                return ArchiveUtils.toAsciiString(this.namebuffer, offset, i - offset);
            }
        }
        throw new IOException("Failed to read entry: " + offset);
    }

    private long asLong(byte[] byteArray, int offset, int len) {
        return Long.parseLong(ArchiveUtils.toAsciiString(byteArray, offset, len).trim());
    }

    private int asInt(byte[] byteArray, int offset, int len) {
        return asInt(byteArray, offset, len, 10, false);
    }

    private int asInt(byte[] byteArray, int offset, int len, boolean treatBlankAsZero) {
        return asInt(byteArray, offset, len, 10, treatBlankAsZero);
    }

    private int asInt(byte[] byteArray, int offset, int len, int base) {
        return asInt(byteArray, offset, len, base, false);
    }

    private int asInt(byte[] byteArray, int offset, int len, int base, boolean treatBlankAsZero) {
        String string = ArchiveUtils.toAsciiString(byteArray, offset, len).trim();
        if (string.length() == 0 && treatBlankAsZero) {
            return 0;
        }
        return Integer.parseInt(string, base);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public ArchiveEntry getNextEntry() throws IOException {
        return getNextArEntry();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.input.close();
        }
        this.currentEntry = null;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.currentEntry == null) {
            throw new IllegalStateException("No current ar entry");
        }
        long entryEnd = this.entryOffset + this.currentEntry.getLength();
        if (len < 0 || this.offset >= entryEnd) {
            return -1;
        }
        int toRead = (int) Math.min(len, entryEnd - this.offset);
        int ret = this.input.read(b, off, toRead);
        trackReadBytes(ret);
        return ret;
    }

    public static boolean matches(byte[] signature, int length) {
        return length >= 8 && signature[0] == 33 && signature[1] == 60 && signature[2] == 97 && signature[3] == 114 && signature[4] == 99 && signature[5] == 104 && signature[6] == 62 && signature[7] == 10;
    }

    private static boolean isBSDLongName(String name) {
        return name != null && name.matches(BSD_LONGNAME_PATTERN);
    }

    private String getBSDLongName(String bsdLongName) throws NumberFormatException, IOException {
        int nameLen = Integer.parseInt(bsdLongName.substring(BSD_LONGNAME_PREFIX_LEN));
        byte[] name = new byte[nameLen];
        int read = IOUtils.readFully(this.input, name);
        trackReadBytes(read);
        if (read != nameLen) {
            throw new EOFException();
        }
        return ArchiveUtils.toAsciiString(name);
    }

    private static boolean isGNUStringTable(String name) {
        return GNU_STRING_TABLE_NAME.equals(name);
    }

    private void trackReadBytes(long read) {
        count(read);
        if (read > 0) {
            this.offset += read;
        }
    }

    private ArArchiveEntry readGNUStringTable(byte[] length, int offset, int len) throws IOException {
        int bufflen = asInt(length, offset, len);
        this.namebuffer = new byte[bufflen];
        int read = IOUtils.readFully(this.input, this.namebuffer, 0, bufflen);
        trackReadBytes(read);
        if (read != bufflen) {
            throw new IOException("Failed to read complete // record: expected=" + bufflen + " read=" + read);
        }
        return new ArArchiveEntry(GNU_STRING_TABLE_NAME, bufflen);
    }

    private boolean isGNULongName(String name) {
        return name != null && name.matches(GNU_LONGNAME_PATTERN);
    }
}
