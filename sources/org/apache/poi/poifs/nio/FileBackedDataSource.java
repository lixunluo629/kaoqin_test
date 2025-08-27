package org.apache.poi.poifs.nio;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.SuppressForbidden;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/nio/FileBackedDataSource.class */
public class FileBackedDataSource extends DataSource {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) FileBackedDataSource.class);
    private FileChannel channel;
    private boolean writable;
    private RandomAccessFile srcFile;
    private List<ByteBuffer> buffersToClean;

    public FileBackedDataSource(File file) throws FileNotFoundException {
        this(newSrcFile(file, ExcelXmlConstants.POSITION), true);
    }

    public FileBackedDataSource(File file, boolean readOnly) throws FileNotFoundException {
        this(newSrcFile(file, readOnly ? ExcelXmlConstants.POSITION : "rw"), readOnly);
    }

    public FileBackedDataSource(RandomAccessFile srcFile, boolean readOnly) {
        this(srcFile.getChannel(), readOnly);
        this.srcFile = srcFile;
    }

    public FileBackedDataSource(FileChannel channel, boolean readOnly) {
        this.buffersToClean = new ArrayList();
        this.channel = channel;
        this.writable = !readOnly;
    }

    public boolean isWriteable() {
        return this.writable;
    }

    public FileChannel getChannel() {
        return this.channel;
    }

    @Override // org.apache.poi.poifs.nio.DataSource
    public ByteBuffer read(int length, long position) throws IOException {
        ByteBuffer dst;
        if (position >= size()) {
            throw new IndexOutOfBoundsException("Position " + position + " past the end of the file");
        }
        if (this.writable) {
            dst = this.channel.map(FileChannel.MapMode.READ_WRITE, position, length);
            this.buffersToClean.add(dst);
        } else {
            this.channel.position(position);
            dst = ByteBuffer.allocate(length);
            int worked = IOUtils.readFully(this.channel, dst);
            if (worked == -1) {
                throw new IndexOutOfBoundsException("Position " + position + " past the end of the file");
            }
        }
        dst.position(0);
        return dst;
    }

    @Override // org.apache.poi.poifs.nio.DataSource
    public void write(ByteBuffer src, long position) throws IOException {
        this.channel.write(src, position);
    }

    @Override // org.apache.poi.poifs.nio.DataSource
    public void copyTo(OutputStream stream) throws IOException {
        WritableByteChannel out = Channels.newChannel(stream);
        this.channel.transferTo(0L, this.channel.size(), out);
    }

    @Override // org.apache.poi.poifs.nio.DataSource
    public long size() throws IOException {
        return this.channel.size();
    }

    @Override // org.apache.poi.poifs.nio.DataSource
    public void close() throws IOException {
        for (ByteBuffer buffer : this.buffersToClean) {
            unmap(buffer);
        }
        this.buffersToClean.clear();
        if (this.srcFile != null) {
            this.srcFile.close();
        } else {
            this.channel.close();
        }
    }

    private static RandomAccessFile newSrcFile(File file, String mode) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        return new RandomAccessFile(file, mode);
    }

    private static void unmap(final ByteBuffer buffer) {
        if (buffer.getClass().getName().endsWith("HeapByteBuffer")) {
            return;
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.apache.poi.poifs.nio.FileBackedDataSource.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressForbidden("Java 9 Jigsaw whitelists access to sun.misc.Cleaner, so setAccessible works")
            public Void run() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    Object cleaner = getCleanerMethod.invoke(buffer, new Object[0]);
                    if (cleaner != null) {
                        cleaner.getClass().getMethod("clean", new Class[0]).invoke(cleaner, new Object[0]);
                    }
                    return null;
                } catch (Exception e) {
                    FileBackedDataSource.logger.log(5, "Unable to unmap memory mapped ByteBuffer.", e);
                    return null;
                }
            }
        });
    }
}
