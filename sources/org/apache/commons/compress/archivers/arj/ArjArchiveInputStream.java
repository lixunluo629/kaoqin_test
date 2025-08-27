package org.apache.commons.compress.archivers.arj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/arj/ArjArchiveInputStream.class */
public class ArjArchiveInputStream extends ArchiveInputStream {
    private static final int ARJ_MAGIC_1 = 96;
    private static final int ARJ_MAGIC_2 = 234;
    private final DataInputStream in;
    private final String charsetName;
    private final MainHeader mainHeader;
    private LocalFileHeader currentLocalFileHeader;
    private InputStream currentInputStream;

    public ArjArchiveInputStream(InputStream inputStream, String charsetName) throws ArchiveException {
        this.currentLocalFileHeader = null;
        this.currentInputStream = null;
        this.in = new DataInputStream(inputStream);
        this.charsetName = charsetName;
        try {
            this.mainHeader = readMainHeader();
            if ((this.mainHeader.arjFlags & 1) != 0) {
                throw new ArchiveException("Encrypted ARJ files are unsupported");
            }
            if ((this.mainHeader.arjFlags & 4) != 0) {
                throw new ArchiveException("Multi-volume ARJ files are unsupported");
            }
        } catch (IOException ioException) {
            throw new ArchiveException(ioException.getMessage(), ioException);
        }
    }

    public ArjArchiveInputStream(InputStream inputStream) throws ArchiveException {
        this(inputStream, "CP437");
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    private int read8(DataInputStream dataIn) throws IOException {
        int value = dataIn.readUnsignedByte();
        count(1);
        return value;
    }

    private int read16(DataInputStream dataIn) throws IOException {
        int value = dataIn.readUnsignedShort();
        count(2);
        return Integer.reverseBytes(value) >>> 16;
    }

    private int read32(DataInputStream dataIn) throws IOException {
        int value = dataIn.readInt();
        count(4);
        return Integer.reverseBytes(value);
    }

    private String readString(DataInputStream dataIn) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Throwable th = null;
        while (true) {
            try {
                int nextByte = dataIn.readUnsignedByte();
                if (nextByte == 0) {
                    break;
                }
                buffer.write(nextByte);
            } catch (Throwable th2) {
                if (buffer != null) {
                    if (0 != 0) {
                        try {
                            buffer.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        buffer.close();
                    }
                }
                throw th2;
            }
        }
        if (this.charsetName != null) {
            String str = new String(buffer.toByteArray(), this.charsetName);
            if (buffer != null) {
                if (0 != 0) {
                    try {
                        buffer.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    buffer.close();
                }
            }
            return str;
        }
        String str2 = new String(buffer.toByteArray());
        if (buffer != null) {
            if (0 != 0) {
                try {
                    buffer.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                buffer.close();
            }
        }
        return str2;
    }

    private void readFully(DataInputStream dataIn, byte[] b) throws IOException {
        dataIn.readFully(b);
        count(b.length);
    }

    private byte[] readHeader() throws IOException {
        boolean found = false;
        byte[] basicHeaderBytes = null;
        do {
            int second = read8(this.in);
            do {
                int first = second;
                second = read8(this.in);
                if (first == 96) {
                    break;
                }
            } while (second != ARJ_MAGIC_2);
            int basicHeaderSize = read16(this.in);
            if (basicHeaderSize == 0) {
                return null;
            }
            if (basicHeaderSize <= 2600) {
                basicHeaderBytes = new byte[basicHeaderSize];
                readFully(this.in, basicHeaderBytes);
                long basicHeaderCrc32 = read32(this.in) & 4294967295L;
                CRC32 crc32 = new CRC32();
                crc32.update(basicHeaderBytes);
                if (basicHeaderCrc32 == crc32.getValue()) {
                    found = true;
                }
            }
        } while (!found);
        return basicHeaderBytes;
    }

    private MainHeader readMainHeader() throws IOException {
        byte[] basicHeaderBytes = readHeader();
        if (basicHeaderBytes == null) {
            throw new IOException("Archive ends without any headers");
        }
        DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
        int firstHeaderSize = basicHeader.readUnsignedByte();
        byte[] firstHeaderBytes = new byte[firstHeaderSize - 1];
        basicHeader.readFully(firstHeaderBytes);
        DataInputStream firstHeader = new DataInputStream(new ByteArrayInputStream(firstHeaderBytes));
        MainHeader hdr = new MainHeader();
        hdr.archiverVersionNumber = firstHeader.readUnsignedByte();
        hdr.minVersionToExtract = firstHeader.readUnsignedByte();
        hdr.hostOS = firstHeader.readUnsignedByte();
        hdr.arjFlags = firstHeader.readUnsignedByte();
        hdr.securityVersion = firstHeader.readUnsignedByte();
        hdr.fileType = firstHeader.readUnsignedByte();
        hdr.reserved = firstHeader.readUnsignedByte();
        hdr.dateTimeCreated = read32(firstHeader);
        hdr.dateTimeModified = read32(firstHeader);
        hdr.archiveSize = 4294967295L & read32(firstHeader);
        hdr.securityEnvelopeFilePosition = read32(firstHeader);
        hdr.fileSpecPosition = read16(firstHeader);
        hdr.securityEnvelopeLength = read16(firstHeader);
        pushedBackBytes(20L);
        hdr.encryptionVersion = firstHeader.readUnsignedByte();
        hdr.lastChapter = firstHeader.readUnsignedByte();
        if (firstHeaderSize >= 33) {
            hdr.arjProtectionFactor = firstHeader.readUnsignedByte();
            hdr.arjFlags2 = firstHeader.readUnsignedByte();
            firstHeader.readUnsignedByte();
            firstHeader.readUnsignedByte();
        }
        hdr.name = readString(basicHeader);
        hdr.comment = readString(basicHeader);
        int extendedHeaderSize = read16(this.in);
        if (extendedHeaderSize > 0) {
            hdr.extendedHeaderBytes = new byte[extendedHeaderSize];
            readFully(this.in, hdr.extendedHeaderBytes);
            long extendedHeaderCrc32 = 4294967295L & read32(this.in);
            CRC32 crc32 = new CRC32();
            crc32.update(hdr.extendedHeaderBytes);
            if (extendedHeaderCrc32 != crc32.getValue()) {
                throw new IOException("Extended header CRC32 verification failure");
            }
        }
        return hdr;
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x01e9: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('firstHeader' java.io.DataInputStream)]) A[TRY_LEAVE], block:B:38:0x01e9 */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x01ee: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:40:0x01ee */
    /* JADX WARN: Type inference failed for: r12v0, names: [firstHeader], types: [java.io.DataInputStream] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    private LocalFileHeader readLocalFileHeader() throws IOException {
        ?? r12;
        ?? r13;
        byte[] basicHeaderBytes = readHeader();
        if (basicHeaderBytes == null) {
            return null;
        }
        DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
        Throwable th = null;
        try {
            try {
                int unsignedByte = basicHeader.readUnsignedByte();
                byte[] bArr = new byte[unsignedByte - 1];
                basicHeader.readFully(bArr);
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
                Throwable th2 = null;
                LocalFileHeader localFileHeader = new LocalFileHeader();
                localFileHeader.archiverVersionNumber = dataInputStream.readUnsignedByte();
                localFileHeader.minVersionToExtract = dataInputStream.readUnsignedByte();
                localFileHeader.hostOS = dataInputStream.readUnsignedByte();
                localFileHeader.arjFlags = dataInputStream.readUnsignedByte();
                localFileHeader.method = dataInputStream.readUnsignedByte();
                localFileHeader.fileType = dataInputStream.readUnsignedByte();
                localFileHeader.reserved = dataInputStream.readUnsignedByte();
                localFileHeader.dateTimeModified = read32(dataInputStream);
                localFileHeader.compressedSize = 4294967295L & read32(dataInputStream);
                localFileHeader.originalSize = 4294967295L & read32(dataInputStream);
                localFileHeader.originalCrc32 = 4294967295L & read32(dataInputStream);
                localFileHeader.fileSpecPosition = read16(dataInputStream);
                localFileHeader.fileAccessMode = read16(dataInputStream);
                pushedBackBytes(20L);
                localFileHeader.firstChapter = dataInputStream.readUnsignedByte();
                localFileHeader.lastChapter = dataInputStream.readUnsignedByte();
                readExtraData(unsignedByte, dataInputStream, localFileHeader);
                localFileHeader.name = readString(basicHeader);
                localFileHeader.comment = readString(basicHeader);
                ArrayList arrayList = new ArrayList();
                while (true) {
                    int i = read16(this.in);
                    if (i > 0) {
                        byte[] bArr2 = new byte[i];
                        readFully(this.in, bArr2);
                        long j = 4294967295L & read32(this.in);
                        CRC32 crc32 = new CRC32();
                        crc32.update(bArr2);
                        if (j != crc32.getValue()) {
                            throw new IOException("Extended header CRC32 verification failure");
                        }
                        arrayList.add(bArr2);
                    } else {
                        localFileHeader.extendedHeaders = (byte[][]) arrayList.toArray((Object[]) new byte[0]);
                        if (dataInputStream != null) {
                            if (0 != 0) {
                                try {
                                    dataInputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                dataInputStream.close();
                            }
                        }
                        return localFileHeader;
                    }
                }
            } catch (Throwable th4) {
                if (r12 != 0) {
                    if (r13 != 0) {
                        try {
                            r12.close();
                        } catch (Throwable th5) {
                            r13.addSuppressed(th5);
                        }
                    } else {
                        r12.close();
                    }
                }
                throw th4;
            }
        } finally {
            if (basicHeader != null) {
                if (0 != 0) {
                    try {
                        basicHeader.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    basicHeader.close();
                }
            }
        }
    }

    private void readExtraData(int firstHeaderSize, DataInputStream firstHeader, LocalFileHeader localFileHeader) throws IOException {
        if (firstHeaderSize >= 33) {
            localFileHeader.extendedFilePosition = read32(firstHeader);
            if (firstHeaderSize >= 45) {
                localFileHeader.dateTimeAccessed = read32(firstHeader);
                localFileHeader.dateTimeCreated = read32(firstHeader);
                localFileHeader.originalSizeEvenForVolumes = read32(firstHeader);
                pushedBackBytes(12L);
            }
            pushedBackBytes(4L);
        }
    }

    public static boolean matches(byte[] signature, int length) {
        return length >= 2 && (255 & signature[0]) == 96 && (255 & signature[1]) == ARJ_MAGIC_2;
    }

    public String getArchiveName() {
        return this.mainHeader.name;
    }

    public String getArchiveComment() {
        return this.mainHeader.comment;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public ArjArchiveEntry getNextEntry() throws IOException {
        if (this.currentInputStream != null) {
            IOUtils.skip(this.currentInputStream, Long.MAX_VALUE);
            this.currentInputStream.close();
            this.currentLocalFileHeader = null;
            this.currentInputStream = null;
        }
        this.currentLocalFileHeader = readLocalFileHeader();
        if (this.currentLocalFileHeader != null) {
            this.currentInputStream = new BoundedInputStream(this.in, this.currentLocalFileHeader.compressedSize);
            if (this.currentLocalFileHeader.method == 0) {
                this.currentInputStream = new CRC32VerifyingInputStream(this.currentInputStream, this.currentLocalFileHeader.originalSize, this.currentLocalFileHeader.originalCrc32);
            }
            return new ArjArchiveEntry(this.currentLocalFileHeader);
        }
        this.currentInputStream = null;
        return null;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public boolean canReadEntryData(ArchiveEntry ae) {
        return (ae instanceof ArjArchiveEntry) && ((ArjArchiveEntry) ae).getMethod() == 0;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.currentLocalFileHeader == null) {
            throw new IllegalStateException("No current arj entry");
        }
        if (this.currentLocalFileHeader.method != 0) {
            throw new IOException("Unsupported compression method " + this.currentLocalFileHeader.method);
        }
        return this.currentInputStream.read(b, off, len);
    }
}
