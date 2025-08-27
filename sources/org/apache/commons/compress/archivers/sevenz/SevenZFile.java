package org.apache.commons.compress.archivers.sevenz;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.zip.CRC32;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.InputStreamStatistics;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/sevenz/SevenZFile.class */
public class SevenZFile implements Closeable {
    static final int SIGNATURE_HEADER_SIZE = 32;
    private static final String DEFAULT_FILE_NAME = "unknown archive";
    private final String fileName;
    private SeekableByteChannel channel;
    private final Archive archive;
    private int currentEntryIndex;
    private int currentFolderIndex;
    private InputStream currentFolderInputStream;
    private byte[] password;
    private final SevenZFileOptions options;
    private long compressedBytesReadFromCurrentEntry;
    private long uncompressedBytesReadFromCurrentEntry;
    private final ArrayList<InputStream> deferredBlockStreams;
    static final byte[] sevenZSignature = {55, 122, -68, -81, 39, 28};
    private static final CharsetEncoder PASSWORD_ENCODER = StandardCharsets.UTF_16LE.newEncoder();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$002(org.apache.commons.compress.archivers.sevenz.SevenZFile r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.compressedBytesReadFromCurrentEntry = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZFile.access$002(org.apache.commons.compress.archivers.sevenz.SevenZFile, long):long");
    }

    static {
    }

    public SevenZFile(File fileName, char[] password) throws IOException {
        this(fileName, password, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(File fileName, char[] password, SevenZFileOptions options) throws IOException {
        this(Files.newByteChannel(fileName.toPath(), EnumSet.of(StandardOpenOption.READ), new FileAttribute[0]), fileName.getAbsolutePath(), utf16Decode(password), true, options);
    }

    public SevenZFile(File fileName, byte[] password) throws IOException {
        this(Files.newByteChannel(fileName.toPath(), EnumSet.of(StandardOpenOption.READ), new FileAttribute[0]), fileName.getAbsolutePath(), password, true, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel) throws IOException {
        this(channel, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, SevenZFileOptions options) throws IOException {
        this(channel, DEFAULT_FILE_NAME, (char[]) null, options);
    }

    public SevenZFile(SeekableByteChannel channel, char[] password) throws IOException {
        this(channel, password, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, char[] password, SevenZFileOptions options) throws IOException {
        this(channel, DEFAULT_FILE_NAME, password, options);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, char[] password) throws IOException {
        this(channel, fileName, password, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, char[] password, SevenZFileOptions options) throws IOException {
        this(channel, fileName, utf16Decode(password), false, options);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName) throws IOException {
        this(channel, fileName, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, SevenZFileOptions options) throws IOException {
        this(channel, fileName, null, false, options);
    }

    public SevenZFile(SeekableByteChannel channel, byte[] password) throws IOException {
        this(channel, DEFAULT_FILE_NAME, password);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, byte[] password) throws IOException {
        this(channel, fileName, password, false, SevenZFileOptions.DEFAULT);
    }

    private SevenZFile(SeekableByteChannel channel, String filename, byte[] password, boolean closeOnError, SevenZFileOptions options) throws IOException {
        this.currentEntryIndex = -1;
        this.currentFolderIndex = -1;
        this.currentFolderInputStream = null;
        this.deferredBlockStreams = new ArrayList<>();
        boolean succeeded = false;
        this.channel = channel;
        this.fileName = filename;
        this.options = options;
        try {
            this.archive = readHeaders(password);
            if (password != null) {
                this.password = Arrays.copyOf(password, password.length);
            } else {
                this.password = null;
            }
            succeeded = true;
        } finally {
            if (!succeeded && closeOnError) {
                this.channel.close();
            }
        }
    }

    public SevenZFile(File fileName) throws IOException {
        this(fileName, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(File fileName, SevenZFileOptions options) throws IOException {
        this(fileName, (char[]) null, options);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.channel != null) {
            try {
                this.channel.close();
            } finally {
                this.channel = null;
                if (this.password != null) {
                    Arrays.fill(this.password, (byte) 0);
                }
                this.password = null;
            }
        }
    }

    public SevenZArchiveEntry getNextEntry() throws IOException {
        if (this.currentEntryIndex >= this.archive.files.length - 1) {
            return null;
        }
        this.currentEntryIndex++;
        SevenZArchiveEntry entry = this.archive.files[this.currentEntryIndex];
        if (entry.getName() == null && this.options.getUseDefaultNameForUnnamedEntries()) {
            entry.setName(getDefaultName());
        }
        buildDecodingStream();
        this.compressedBytesReadFromCurrentEntry = 0L;
        this.uncompressedBytesReadFromCurrentEntry = 0L;
        return entry;
    }

    public Iterable<SevenZArchiveEntry> getEntries() {
        return Arrays.asList(this.archive.files);
    }

    private Archive readHeaders(byte[] password) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
        readFully(buf);
        byte[] signature = new byte[6];
        buf.get(signature);
        if (!Arrays.equals(signature, sevenZSignature)) {
            throw new IOException("Bad 7z signature");
        }
        byte archiveVersionMajor = buf.get();
        byte archiveVersionMinor = buf.get();
        if (archiveVersionMajor != 0) {
            throw new IOException(String.format("Unsupported 7z version (%d,%d)", Byte.valueOf(archiveVersionMajor), Byte.valueOf(archiveVersionMinor)));
        }
        long startHeaderCrc = 4294967295L & buf.getInt();
        StartHeader startHeader = readStartHeader(startHeaderCrc);
        assertFitsIntoInt("nextHeaderSize", startHeader.nextHeaderSize);
        int nextHeaderSizeInt = (int) startHeader.nextHeaderSize;
        this.channel.position(32 + startHeader.nextHeaderOffset);
        ByteBuffer buf2 = ByteBuffer.allocate(nextHeaderSizeInt).order(ByteOrder.LITTLE_ENDIAN);
        readFully(buf2);
        CRC32 crc = new CRC32();
        crc.update(buf2.array());
        if (startHeader.nextHeaderCrc != crc.getValue()) {
            throw new IOException("NextHeader CRC mismatch");
        }
        Archive archive = new Archive();
        int nid = getUnsignedByte(buf2);
        if (nid == 23) {
            buf2 = readEncodedHeader(buf2, archive, password);
            archive = new Archive();
            nid = getUnsignedByte(buf2);
        }
        if (nid == 1) {
            readHeader(buf2, archive);
            return archive;
        }
        throw new IOException("Broken or unsupported archive: no Header");
    }

    private StartHeader readStartHeader(long startHeaderCrc) throws IOException {
        StartHeader startHeader = new StartHeader();
        DataInputStream dataInputStream = new DataInputStream(new CRC32VerifyingInputStream(new BoundedSeekableByteChannelInputStream(this.channel, 20L), 20L, startHeaderCrc));
        Throwable th = null;
        try {
            try {
                startHeader.nextHeaderOffset = Long.reverseBytes(dataInputStream.readLong());
                startHeader.nextHeaderSize = Long.reverseBytes(dataInputStream.readLong());
                startHeader.nextHeaderCrc = 4294967295L & Integer.reverseBytes(dataInputStream.readInt());
                if (dataInputStream != null) {
                    if (0 != 0) {
                        try {
                            dataInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        dataInputStream.close();
                    }
                }
                return startHeader;
            } finally {
            }
        } catch (Throwable th3) {
            if (dataInputStream != null) {
                if (th != null) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    dataInputStream.close();
                }
            }
            throw th3;
        }
    }

    private void readHeader(ByteBuffer header, Archive archive) throws IOException {
        int nid = getUnsignedByte(header);
        if (nid == 2) {
            readArchiveProperties(header);
            nid = getUnsignedByte(header);
        }
        if (nid == 3) {
            throw new IOException("Additional streams unsupported");
        }
        if (nid == 4) {
            readStreamsInfo(header, archive);
            nid = getUnsignedByte(header);
        }
        if (nid == 5) {
            readFilesInfo(header, archive);
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated header, found " + nid);
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0006 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readArchiveProperties(java.nio.ByteBuffer r5) throws java.io.IOException {
        /*
            r4 = this;
            r0 = r5
            int r0 = getUnsignedByte(r0)
            r6 = r0
        L5:
            r0 = r6
            if (r0 == 0) goto L29
            r0 = r5
            long r0 = readUint64(r0)
            r7 = r0
            java.lang.String r0 = "propertySize"
            r1 = r7
            assertFitsIntoInt(r0, r1)
            r0 = r7
            int r0 = (int) r0
            byte[] r0 = new byte[r0]
            r9 = r0
            r0 = r5
            r1 = r9
            java.nio.ByteBuffer r0 = r0.get(r1)
            r0 = r5
            int r0 = getUnsignedByte(r0)
            r6 = r0
            goto L5
        L29:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZFile.readArchiveProperties(java.nio.ByteBuffer):void");
    }

    private ByteBuffer readEncodedHeader(ByteBuffer header, Archive archive, byte[] password) throws IOException {
        readStreamsInfo(header, archive);
        Folder folder = archive.folders[0];
        long folderOffset = 32 + archive.packPos + 0;
        this.channel.position(folderOffset);
        InputStream inputStreamStack = new BoundedSeekableByteChannelInputStream(this.channel, archive.packSizes[0]);
        for (Coder coder : folder.getOrderedCoders()) {
            if (coder.numInStreams != 1 || coder.numOutStreams != 1) {
                throw new IOException("Multi input/output stream coders are not yet supported");
            }
            inputStreamStack = Coders.addDecoder(this.fileName, inputStreamStack, folder.getUnpackSizeForCoder(coder), coder, password, this.options.getMaxMemoryLimitInKb());
        }
        if (folder.hasCrc) {
            inputStreamStack = new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc);
        }
        assertFitsIntoInt("unpackSize", folder.getUnpackSize());
        byte[] nextHeader = new byte[(int) folder.getUnpackSize()];
        DataInputStream nextHeaderInputStream = new DataInputStream(inputStreamStack);
        Throwable th = null;
        try {
            nextHeaderInputStream.readFully(nextHeader);
            if (nextHeaderInputStream != null) {
                if (0 != 0) {
                    try {
                        nextHeaderInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    nextHeaderInputStream.close();
                }
            }
            return ByteBuffer.wrap(nextHeader).order(ByteOrder.LITTLE_ENDIAN);
        } catch (Throwable th3) {
            if (nextHeaderInputStream != null) {
                if (0 != 0) {
                    try {
                        nextHeaderInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    nextHeaderInputStream.close();
                }
            }
            throw th3;
        }
    }

    private void readStreamsInfo(ByteBuffer header, Archive archive) throws IOException {
        int nid = getUnsignedByte(header);
        if (nid == 6) {
            readPackInfo(header, archive);
            nid = getUnsignedByte(header);
        }
        if (nid == 7) {
            readUnpackInfo(header, archive);
            nid = getUnsignedByte(header);
        } else {
            archive.folders = new Folder[0];
        }
        if (nid == 8) {
            readSubStreamsInfo(header, archive);
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated StreamsInfo");
        }
    }

    private void readPackInfo(ByteBuffer header, Archive archive) throws IOException {
        archive.packPos = readUint64(header);
        long numPackStreams = readUint64(header);
        assertFitsIntoInt("numPackStreams", numPackStreams);
        int numPackStreamsInt = (int) numPackStreams;
        int nid = getUnsignedByte(header);
        if (nid == 9) {
            archive.packSizes = new long[numPackStreamsInt];
            for (int i = 0; i < archive.packSizes.length; i++) {
                archive.packSizes[i] = readUint64(header);
            }
            nid = getUnsignedByte(header);
        }
        if (nid == 10) {
            archive.packCrcsDefined = readAllOrBits(header, numPackStreamsInt);
            archive.packCrcs = new long[numPackStreamsInt];
            for (int i2 = 0; i2 < numPackStreamsInt; i2++) {
                if (archive.packCrcsDefined.get(i2)) {
                    archive.packCrcs[i2] = 4294967295L & header.getInt();
                }
            }
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated PackInfo (" + nid + ")");
        }
    }

    private void readUnpackInfo(ByteBuffer header, Archive archive) throws IOException {
        int nid = getUnsignedByte(header);
        if (nid != 11) {
            throw new IOException("Expected kFolder, got " + nid);
        }
        long numFolders = readUint64(header);
        assertFitsIntoInt("numFolders", numFolders);
        int numFoldersInt = (int) numFolders;
        Folder[] folders = new Folder[numFoldersInt];
        archive.folders = folders;
        int external = getUnsignedByte(header);
        if (external != 0) {
            throw new IOException("External unsupported");
        }
        for (int i = 0; i < numFoldersInt; i++) {
            folders[i] = readFolder(header);
        }
        int nid2 = getUnsignedByte(header);
        if (nid2 != 12) {
            throw new IOException("Expected kCodersUnpackSize, got " + nid2);
        }
        for (Folder folder : folders) {
            assertFitsIntoInt("totalOutputStreams", folder.totalOutputStreams);
            folder.unpackSizes = new long[(int) folder.totalOutputStreams];
            for (int i2 = 0; i2 < folder.totalOutputStreams; i2++) {
                folder.unpackSizes[i2] = readUint64(header);
            }
        }
        int nid3 = getUnsignedByte(header);
        if (nid3 == 10) {
            BitSet crcsDefined = readAllOrBits(header, numFoldersInt);
            for (int i3 = 0; i3 < numFoldersInt; i3++) {
                if (crcsDefined.get(i3)) {
                    folders[i3].hasCrc = true;
                    folders[i3].crc = 4294967295L & header.getInt();
                } else {
                    folders[i3].hasCrc = false;
                }
            }
            nid3 = getUnsignedByte(header);
        }
        if (nid3 != 0) {
            throw new IOException("Badly terminated UnpackInfo");
        }
    }

    private void readSubStreamsInfo(ByteBuffer header, Archive archive) throws IOException {
        for (Folder folder : archive.folders) {
            folder.numUnpackSubStreams = 1;
        }
        int totalUnpackStreams = archive.folders.length;
        int nid = getUnsignedByte(header);
        if (nid == 13) {
            totalUnpackStreams = 0;
            for (Folder folder2 : archive.folders) {
                long numStreams = readUint64(header);
                assertFitsIntoInt("numStreams", numStreams);
                folder2.numUnpackSubStreams = (int) numStreams;
                totalUnpackStreams = (int) (totalUnpackStreams + numStreams);
            }
            nid = getUnsignedByte(header);
        }
        SubStreamsInfo subStreamsInfo = new SubStreamsInfo();
        subStreamsInfo.unpackSizes = new long[totalUnpackStreams];
        subStreamsInfo.hasCrc = new BitSet(totalUnpackStreams);
        subStreamsInfo.crcs = new long[totalUnpackStreams];
        int nextUnpackStream = 0;
        for (Folder folder3 : archive.folders) {
            if (folder3.numUnpackSubStreams != 0) {
                long sum = 0;
                if (nid == 9) {
                    for (int i = 0; i < folder3.numUnpackSubStreams - 1; i++) {
                        long size = readUint64(header);
                        int i2 = nextUnpackStream;
                        nextUnpackStream++;
                        subStreamsInfo.unpackSizes[i2] = size;
                        sum += size;
                    }
                }
                int i3 = nextUnpackStream;
                nextUnpackStream++;
                subStreamsInfo.unpackSizes[i3] = folder3.getUnpackSize() - sum;
            }
        }
        if (nid == 9) {
            nid = getUnsignedByte(header);
        }
        int numDigests = 0;
        for (Folder folder4 : archive.folders) {
            if (folder4.numUnpackSubStreams != 1 || !folder4.hasCrc) {
                numDigests += folder4.numUnpackSubStreams;
            }
        }
        if (nid == 10) {
            BitSet hasMissingCrc = readAllOrBits(header, numDigests);
            long[] missingCrcs = new long[numDigests];
            for (int i4 = 0; i4 < numDigests; i4++) {
                if (hasMissingCrc.get(i4)) {
                    missingCrcs[i4] = 4294967295L & header.getInt();
                }
            }
            int nextCrc = 0;
            int nextMissingCrc = 0;
            for (Folder folder5 : archive.folders) {
                if (folder5.numUnpackSubStreams == 1 && folder5.hasCrc) {
                    subStreamsInfo.hasCrc.set(nextCrc, true);
                    subStreamsInfo.crcs[nextCrc] = folder5.crc;
                    nextCrc++;
                } else {
                    for (int i5 = 0; i5 < folder5.numUnpackSubStreams; i5++) {
                        subStreamsInfo.hasCrc.set(nextCrc, hasMissingCrc.get(nextMissingCrc));
                        subStreamsInfo.crcs[nextCrc] = missingCrcs[nextMissingCrc];
                        nextCrc++;
                        nextMissingCrc++;
                    }
                }
            }
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated SubStreamsInfo");
        }
        archive.subStreamsInfo = subStreamsInfo;
    }

    private Folder readFolder(ByteBuffer header) throws IOException {
        Folder folder = new Folder();
        long numCoders = readUint64(header);
        assertFitsIntoInt("numCoders", numCoders);
        Coder[] coders = new Coder[(int) numCoders];
        long totalInStreams = 0;
        long totalOutStreams = 0;
        for (int i = 0; i < coders.length; i++) {
            coders[i] = new Coder();
            int bits = getUnsignedByte(header);
            int idSize = bits & 15;
            boolean isSimple = (bits & 16) == 0;
            boolean hasAttributes = (bits & 32) != 0;
            boolean moreAlternativeMethods = (bits & 128) != 0;
            coders[i].decompressionMethodId = new byte[idSize];
            header.get(coders[i].decompressionMethodId);
            if (isSimple) {
                coders[i].numInStreams = 1L;
                coders[i].numOutStreams = 1L;
            } else {
                coders[i].numInStreams = readUint64(header);
                coders[i].numOutStreams = readUint64(header);
            }
            totalInStreams += coders[i].numInStreams;
            totalOutStreams += coders[i].numOutStreams;
            if (hasAttributes) {
                long propertiesSize = readUint64(header);
                assertFitsIntoInt("propertiesSize", propertiesSize);
                coders[i].properties = new byte[(int) propertiesSize];
                header.get(coders[i].properties);
            }
            if (moreAlternativeMethods) {
                throw new IOException("Alternative methods are unsupported, please report. The reference implementation doesn't support them either.");
            }
        }
        folder.coders = coders;
        assertFitsIntoInt("totalInStreams", totalInStreams);
        folder.totalInputStreams = totalInStreams;
        assertFitsIntoInt("totalOutStreams", totalOutStreams);
        folder.totalOutputStreams = totalOutStreams;
        if (totalOutStreams == 0) {
            throw new IOException("Total output streams can't be 0");
        }
        long numBindPairs = totalOutStreams - 1;
        assertFitsIntoInt("numBindPairs", numBindPairs);
        BindPair[] bindPairs = new BindPair[(int) numBindPairs];
        for (int i2 = 0; i2 < bindPairs.length; i2++) {
            bindPairs[i2] = new BindPair();
            bindPairs[i2].inIndex = readUint64(header);
            bindPairs[i2].outIndex = readUint64(header);
        }
        folder.bindPairs = bindPairs;
        if (totalInStreams < numBindPairs) {
            throw new IOException("Total input streams can't be less than the number of bind pairs");
        }
        long numPackedStreams = totalInStreams - numBindPairs;
        assertFitsIntoInt("numPackedStreams", numPackedStreams);
        long[] packedStreams = new long[(int) numPackedStreams];
        if (numPackedStreams == 1) {
            int i3 = 0;
            while (i3 < ((int) totalInStreams) && folder.findBindPairForInStream(i3) >= 0) {
                i3++;
            }
            if (i3 == ((int) totalInStreams)) {
                throw new IOException("Couldn't find stream's bind pair index");
            }
            packedStreams[0] = i3;
        } else {
            for (int i4 = 0; i4 < ((int) numPackedStreams); i4++) {
                packedStreams[i4] = readUint64(header);
            }
        }
        folder.packedStreams = packedStreams;
        return folder;
    }

    private BitSet readAllOrBits(ByteBuffer header, int size) throws IOException {
        BitSet bits;
        int areAllDefined = getUnsignedByte(header);
        if (areAllDefined != 0) {
            bits = new BitSet(size);
            for (int i = 0; i < size; i++) {
                bits.set(i, true);
            }
        } else {
            bits = readBits(header, size);
        }
        return bits;
    }

    private BitSet readBits(ByteBuffer header, int size) throws IOException {
        BitSet bits = new BitSet(size);
        int mask = 0;
        int cache = 0;
        for (int i = 0; i < size; i++) {
            if (mask == 0) {
                mask = 128;
                cache = getUnsignedByte(header);
            }
            bits.set(i, (cache & mask) != 0);
            mask >>>= 1;
        }
        return bits;
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x017d, code lost:
    
        throw new java.io.IOException("Error parsing file names");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readFilesInfo(java.nio.ByteBuffer r9, org.apache.commons.compress.archivers.sevenz.Archive r10) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1044
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZFile.readFilesInfo(java.nio.ByteBuffer, org.apache.commons.compress.archivers.sevenz.Archive):void");
    }

    private void calculateStreamMap(Archive archive) throws IOException {
        StreamMap streamMap = new StreamMap();
        int nextFolderPackStreamIndex = 0;
        int numFolders = archive.folders != null ? archive.folders.length : 0;
        streamMap.folderFirstPackStreamIndex = new int[numFolders];
        for (int i = 0; i < numFolders; i++) {
            streamMap.folderFirstPackStreamIndex[i] = nextFolderPackStreamIndex;
            nextFolderPackStreamIndex += archive.folders[i].packedStreams.length;
        }
        long nextPackStreamOffset = 0;
        int numPackSizes = archive.packSizes != null ? archive.packSizes.length : 0;
        streamMap.packStreamOffsets = new long[numPackSizes];
        for (int i2 = 0; i2 < numPackSizes; i2++) {
            streamMap.packStreamOffsets[i2] = nextPackStreamOffset;
            nextPackStreamOffset += archive.packSizes[i2];
        }
        streamMap.folderFirstFileIndex = new int[numFolders];
        streamMap.fileFolderIndex = new int[archive.files.length];
        int nextFolderIndex = 0;
        int nextFolderUnpackStreamIndex = 0;
        for (int i3 = 0; i3 < archive.files.length; i3++) {
            if (!archive.files[i3].hasStream() && nextFolderUnpackStreamIndex == 0) {
                streamMap.fileFolderIndex[i3] = -1;
            } else {
                if (nextFolderUnpackStreamIndex == 0) {
                    while (nextFolderIndex < archive.folders.length) {
                        streamMap.folderFirstFileIndex[nextFolderIndex] = i3;
                        if (archive.folders[nextFolderIndex].numUnpackSubStreams > 0) {
                            break;
                        } else {
                            nextFolderIndex++;
                        }
                    }
                    if (nextFolderIndex >= archive.folders.length) {
                        throw new IOException("Too few folders in archive");
                    }
                }
                streamMap.fileFolderIndex[i3] = nextFolderIndex;
                if (archive.files[i3].hasStream()) {
                    nextFolderUnpackStreamIndex++;
                    if (nextFolderUnpackStreamIndex >= archive.folders[nextFolderIndex].numUnpackSubStreams) {
                        nextFolderIndex++;
                        nextFolderUnpackStreamIndex = 0;
                    }
                }
            }
        }
        archive.streamMap = streamMap;
    }

    private void buildDecodingStream() throws IOException {
        int folderIndex = this.archive.streamMap.fileFolderIndex[this.currentEntryIndex];
        if (folderIndex < 0) {
            this.deferredBlockStreams.clear();
            return;
        }
        SevenZArchiveEntry file = this.archive.files[this.currentEntryIndex];
        if (this.currentFolderIndex == folderIndex) {
            file.setContentMethods(this.archive.files[this.currentEntryIndex - 1].getContentMethods());
        } else {
            this.currentFolderIndex = folderIndex;
            this.deferredBlockStreams.clear();
            if (this.currentFolderInputStream != null) {
                this.currentFolderInputStream.close();
                this.currentFolderInputStream = null;
            }
            Folder folder = this.archive.folders[folderIndex];
            int firstPackStreamIndex = this.archive.streamMap.folderFirstPackStreamIndex[folderIndex];
            long folderOffset = 32 + this.archive.packPos + this.archive.streamMap.packStreamOffsets[firstPackStreamIndex];
            this.currentFolderInputStream = buildDecoderStack(folder, folderOffset, firstPackStreamIndex, file);
        }
        InputStream fileStream = new BoundedInputStream(this.currentFolderInputStream, file.getSize());
        if (file.getHasCrc()) {
            fileStream = new CRC32VerifyingInputStream(fileStream, file.getSize(), file.getCrcValue());
        }
        this.deferredBlockStreams.add(fileStream);
    }

    private InputStream buildDecoderStack(Folder folder, long folderOffset, int firstPackStreamIndex, SevenZArchiveEntry entry) throws IOException {
        this.channel.position(folderOffset);
        InputStream inputStreamStack = new FilterInputStream(new BufferedInputStream(new BoundedSeekableByteChannelInputStream(this.channel, this.archive.packSizes[firstPackStreamIndex]))) { // from class: org.apache.commons.compress.archivers.sevenz.SevenZFile.1
            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read() throws IOException {
                int r = this.in.read();
                if (r >= 0) {
                    count(1);
                }
                return r;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read(byte[] b) throws IOException {
                return read(b, 0, b.length);
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read(byte[] b, int off, int len) throws IOException {
                int r = this.in.read(b, off, len);
                if (r >= 0) {
                    count(r);
                }
                return r;
            }

            /* JADX WARN: Failed to check method for inline after forced processorg.apache.commons.compress.archivers.sevenz.SevenZFile.access$002(org.apache.commons.compress.archivers.sevenz.SevenZFile, long):long */
            private void count(int c) {
                SevenZFile.access$002(SevenZFile.this, SevenZFile.this.compressedBytesReadFromCurrentEntry + c);
            }
        };
        LinkedList<SevenZMethodConfiguration> methods = new LinkedList<>();
        for (Coder coder : folder.getOrderedCoders()) {
            if (coder.numInStreams != 1 || coder.numOutStreams != 1) {
                throw new IOException("Multi input/output stream coders are not yet supported");
            }
            SevenZMethod method = SevenZMethod.byId(coder.decompressionMethodId);
            inputStreamStack = Coders.addDecoder(this.fileName, inputStreamStack, folder.getUnpackSizeForCoder(coder), coder, this.password, this.options.getMaxMemoryLimitInKb());
            methods.addFirst(new SevenZMethodConfiguration(method, Coders.findByMethod(method).getOptionsFromCoder(coder, inputStreamStack)));
        }
        entry.setContentMethods(methods);
        if (folder.hasCrc) {
            return new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc);
        }
        return inputStreamStack;
    }

    public int read() throws IOException {
        int b = getCurrentStream().read();
        if (b >= 0) {
            this.uncompressedBytesReadFromCurrentEntry++;
        }
        return b;
    }

    private InputStream getCurrentStream() throws IOException {
        if (this.archive.files[this.currentEntryIndex].getSize() == 0) {
            return new ByteArrayInputStream(new byte[0]);
        }
        if (this.deferredBlockStreams.isEmpty()) {
            throw new IllegalStateException("No current 7z entry (call getNextEntry() first).");
        }
        while (this.deferredBlockStreams.size() > 1) {
            InputStream stream = this.deferredBlockStreams.remove(0);
            Throwable th = null;
            try {
                try {
                    IOUtils.skip(stream, Long.MAX_VALUE);
                    if (stream != null) {
                        if (0 != 0) {
                            try {
                                stream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            stream.close();
                        }
                    }
                    this.compressedBytesReadFromCurrentEntry = 0L;
                } finally {
                }
            } catch (Throwable th3) {
                if (stream != null) {
                    if (th != null) {
                        try {
                            stream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        stream.close();
                    }
                }
                throw th3;
            }
        }
        return this.deferredBlockStreams.get(0);
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int cnt = getCurrentStream().read(b, off, len);
        if (cnt > 0) {
            this.uncompressedBytesReadFromCurrentEntry += cnt;
        }
        return cnt;
    }

    public InputStreamStatistics getStatisticsForCurrentEntry() {
        return new InputStreamStatistics() { // from class: org.apache.commons.compress.archivers.sevenz.SevenZFile.2
            @Override // org.apache.commons.compress.utils.InputStreamStatistics
            public long getCompressedCount() {
                return SevenZFile.this.compressedBytesReadFromCurrentEntry;
            }

            @Override // org.apache.commons.compress.utils.InputStreamStatistics
            public long getUncompressedCount() {
                return SevenZFile.this.uncompressedBytesReadFromCurrentEntry;
            }
        };
    }

    private static long readUint64(ByteBuffer in) throws IOException {
        long firstByte = getUnsignedByte(in);
        int mask = 128;
        long value = 0;
        for (int i = 0; i < 8; i++) {
            if ((firstByte & mask) == 0) {
                return value | ((firstByte & (mask - 1)) << (8 * i));
            }
            long nextByte = getUnsignedByte(in);
            value |= nextByte << (8 * i);
            mask >>>= 1;
        }
        return value;
    }

    private static int getUnsignedByte(ByteBuffer buf) {
        return buf.get() & 255;
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < sevenZSignature.length) {
            return false;
        }
        for (int i = 0; i < sevenZSignature.length; i++) {
            if (signature[i] != sevenZSignature[i]) {
                return false;
            }
        }
        return true;
    }

    private static long skipBytesFully(ByteBuffer input, long bytesToSkip) throws IOException {
        if (bytesToSkip < 1) {
            return 0L;
        }
        int current = input.position();
        int maxSkip = input.remaining();
        if (maxSkip < bytesToSkip) {
            bytesToSkip = maxSkip;
        }
        input.position(current + ((int) bytesToSkip));
        return bytesToSkip;
    }

    private void readFully(ByteBuffer buf) throws IOException {
        buf.rewind();
        IOUtils.readFully(this.channel, buf);
        buf.flip();
    }

    public String toString() {
        return this.archive.toString();
    }

    public String getDefaultName() {
        if (DEFAULT_FILE_NAME.equals(this.fileName) || this.fileName == null) {
            return null;
        }
        String lastSegment = new File(this.fileName).getName();
        int dotPos = lastSegment.lastIndexOf(".");
        if (dotPos > 0) {
            return lastSegment.substring(0, dotPos);
        }
        return lastSegment + "~";
    }

    private static byte[] utf16Decode(char[] chars) throws IOException {
        if (chars == null) {
            return null;
        }
        ByteBuffer encoded = PASSWORD_ENCODER.encode(CharBuffer.wrap(chars));
        if (encoded.hasArray()) {
            return encoded.array();
        }
        byte[] e = new byte[encoded.remaining()];
        encoded.get(e);
        return e;
    }

    private static void assertFitsIntoInt(String what, long value) throws IOException {
        if (value > 2147483647L) {
            throw new IOException("Cannot handle " + what + value);
        }
    }
}
