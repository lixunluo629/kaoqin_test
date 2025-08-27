package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/MagicNumberFileFilter.class */
public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -547733176983104172L;
    private final byte[] magicNumbers;
    private final long byteOffset;

    public MagicNumberFileFilter(byte[] magicNumber) {
        this(magicNumber, 0L);
    }

    public MagicNumberFileFilter(byte[] magicNumbers, long offset) {
        Objects.requireNonNull(magicNumbers, "magicNumbers");
        if (magicNumbers.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        this.magicNumbers = (byte[]) magicNumbers.clone();
        this.byteOffset = offset;
    }

    public MagicNumberFileFilter(String magicNumber) {
        this(magicNumber, 0L);
    }

    public MagicNumberFileFilter(String magicNumber, long offset) {
        Objects.requireNonNull(magicNumber, "magicNumber");
        if (magicNumber.isEmpty()) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset());
        this.byteOffset = offset;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            try {
                return ((Boolean) RandomAccessFileMode.READ_ONLY.apply(file.toPath(), raf -> {
                    return Boolean.valueOf(Arrays.equals(this.magicNumbers, RandomAccessFiles.read(raf, this.byteOffset, this.magicNumbers.length)));
                })).booleanValue();
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) throws IOException {
        if (file != null && Files.isRegularFile(file, new LinkOption[0]) && Files.isReadable(file)) {
            try {
                FileChannel fileChannel = FileChannel.open(file, new OpenOption[0]);
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(this.magicNumbers.length);
                    fileChannel.position(this.byteOffset);
                    int read = fileChannel.read(byteBuffer);
                    if (read != this.magicNumbers.length) {
                        FileVisitResult fileVisitResult = FileVisitResult.TERMINATE;
                        if (fileChannel != null) {
                            fileChannel.close();
                        }
                        return fileVisitResult;
                    }
                    FileVisitResult fileVisitResult2 = toFileVisitResult(Arrays.equals(this.magicNumbers, byteBuffer.array()));
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    return fileVisitResult2;
                } finally {
                }
            } catch (IOException e) {
            }
        }
        return FileVisitResult.TERMINATE;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        return super.toString() + "(" + new String(this.magicNumbers, Charset.defaultCharset()) + "," + this.byteOffset + ")";
    }
}
