package io.netty.handler.codec.http.multipart;

import com.alibaba.excel.constant.ExcelXmlConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/AbstractDiskHttpData.class */
public abstract class AbstractDiskHttpData extends AbstractHttpData {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractDiskHttpData.class);
    private File file;
    private boolean isRenamed;
    private FileChannel fileChannel;

    protected abstract String getDiskFilename();

    protected abstract String getPrefix();

    protected abstract String getBaseDirectory();

    protected abstract String getPostfix();

    protected abstract boolean deleteOnExit();

    protected AbstractDiskHttpData(String name, Charset charset, long size) {
        super(name, charset, size);
    }

    private File tempFile() throws IOException {
        String newpostfix;
        File tmpFile;
        String diskFilename = getDiskFilename();
        if (diskFilename != null) {
            newpostfix = '_' + diskFilename;
        } else {
            newpostfix = getPostfix();
        }
        if (getBaseDirectory() == null) {
            tmpFile = File.createTempFile(getPrefix(), newpostfix);
        } else {
            tmpFile = File.createTempFile(getPrefix(), newpostfix, new File(getBaseDirectory()));
        }
        if (deleteOnExit()) {
            tmpFile.deleteOnExit();
        }
        return tmpFile;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(ByteBuf buffer) throws IOException {
        ObjectUtil.checkNotNull(buffer, "buffer");
        try {
            this.size = buffer.readableBytes();
            checkSize(this.size);
            if (this.definedSize > 0 && this.definedSize < this.size) {
                throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
            }
            if (this.file == null) {
                this.file = tempFile();
            }
            if (buffer.readableBytes() == 0) {
                if (!this.file.createNewFile()) {
                    if (this.file.length() == 0) {
                        return;
                    }
                    if (!this.file.delete() || !this.file.createNewFile()) {
                        throw new IOException("file exists already: " + this.file);
                    }
                }
                buffer.release();
                return;
            }
            RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
            try {
                accessFile.setLength(0L);
                FileChannel localfileChannel = accessFile.getChannel();
                ByteBuffer byteBuffer = buffer.nioBuffer();
                int written = 0;
                while (written < this.size) {
                    written += localfileChannel.write(byteBuffer);
                }
                buffer.readerIndex(buffer.readerIndex() + written);
                localfileChannel.force(false);
                accessFile.close();
                setCompleted();
                buffer.release();
            } catch (Throwable th) {
                accessFile.close();
                throw th;
            }
        } finally {
            buffer.release();
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        if (buffer != null) {
            try {
                int localsize = buffer.readableBytes();
                checkSize(this.size + localsize);
                if (this.definedSize > 0 && this.definedSize < this.size + localsize) {
                    throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
                }
                ByteBuffer byteBuffer = buffer.nioBufferCount() == 1 ? buffer.nioBuffer() : buffer.copy().nioBuffer();
                int written = 0;
                if (this.file == null) {
                    this.file = tempFile();
                }
                if (this.fileChannel == null) {
                    RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
                    this.fileChannel = accessFile.getChannel();
                }
                while (written < localsize) {
                    written += this.fileChannel.write(byteBuffer);
                }
                this.size += localsize;
                buffer.readerIndex(buffer.readerIndex() + written);
                buffer.release();
            } catch (Throwable th) {
                buffer.release();
                throw th;
            }
        }
        if (last) {
            if (this.file == null) {
                this.file = tempFile();
            }
            if (this.fileChannel == null) {
                RandomAccessFile accessFile2 = new RandomAccessFile(this.file, "rw");
                this.fileChannel = accessFile2.getChannel();
            }
            try {
                this.fileChannel.force(false);
                this.fileChannel.close();
                this.fileChannel = null;
                setCompleted();
                return;
            } catch (Throwable th2) {
                this.fileChannel.close();
                throw th2;
            }
        }
        ObjectUtil.checkNotNull(buffer, "buffer");
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(File file) throws IOException {
        long size = file.length();
        checkSize(size);
        this.size = size;
        if (this.file != null) {
            delete();
        }
        this.file = file;
        this.isRenamed = true;
        setCompleted();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(InputStream inputStream) throws IOException {
        ObjectUtil.checkNotNull(inputStream, "inputStream");
        if (this.file != null) {
            delete();
        }
        this.file = tempFile();
        RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
        int written = 0;
        try {
            accessFile.setLength(0L);
            FileChannel localfileChannel = accessFile.getChannel();
            byte[] bytes = new byte[16384];
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            int read = inputStream.read(bytes);
            while (read > 0) {
                byteBuffer.position(read).flip();
                written += localfileChannel.write(byteBuffer);
                checkSize(written);
                read = inputStream.read(bytes);
            }
            localfileChannel.force(false);
            accessFile.close();
            this.size = written;
            if (this.definedSize > 0 && this.definedSize < this.size) {
                if (!this.file.delete()) {
                    logger.warn("Failed to delete: {}", this.file);
                }
                this.file = null;
                throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
            }
            this.isRenamed = true;
            setCompleted();
        } catch (Throwable th) {
            accessFile.close();
            throw th;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void delete() {
        if (this.fileChannel != null) {
            try {
                try {
                    this.fileChannel.force(false);
                } finally {
                    try {
                        this.fileChannel.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close a file.", (Throwable) e);
                    }
                }
            } catch (IOException e2) {
                logger.warn("Failed to force.", (Throwable) e2);
                try {
                    this.fileChannel.close();
                } catch (IOException e3) {
                    logger.warn("Failed to close a file.", (Throwable) e3);
                }
            }
            this.fileChannel = null;
        }
        if (!this.isRenamed) {
            if (this.file != null && this.file.exists() && !this.file.delete()) {
                logger.warn("Failed to delete: {}", this.file);
            }
            this.file = null;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public byte[] get() throws IOException {
        if (this.file == null) {
            return EmptyArrays.EMPTY_BYTES;
        }
        return readFrom(this.file);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getByteBuf() throws IOException {
        if (this.file == null) {
            return Unpooled.EMPTY_BUFFER;
        }
        byte[] array = readFrom(this.file);
        return Unpooled.wrappedBuffer(array);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getChunk(int length) throws IOException {
        if (this.file == null || length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (this.fileChannel == null) {
            RandomAccessFile accessFile = new RandomAccessFile(this.file, ExcelXmlConstants.POSITION);
            this.fileChannel = accessFile.getChannel();
        }
        int read = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        while (true) {
            if (read >= length) {
                break;
            }
            int readnow = this.fileChannel.read(byteBuffer);
            if (readnow == -1) {
                this.fileChannel.close();
                this.fileChannel = null;
                break;
            }
            read += readnow;
        }
        if (read == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        byteBuffer.flip();
        ByteBuf buffer = Unpooled.wrappedBuffer(byteBuffer);
        buffer.readerIndex(0);
        buffer.writerIndex(read);
        return buffer;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString() throws IOException {
        return getString(HttpConstants.DEFAULT_CHARSET);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString(Charset encoding) throws IOException {
        if (this.file == null) {
            return "";
        }
        if (encoding == null) {
            byte[] array = readFrom(this.file);
            return new String(array, HttpConstants.DEFAULT_CHARSET.name());
        }
        byte[] array2 = readFrom(this.file);
        return new String(array2, encoding.name());
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isInMemory() {
        return false;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean renameTo(File dest) throws IOException {
        ObjectUtil.checkNotNull(dest, "dest");
        if (this.file == null) {
            throw new IOException("No file defined so cannot be renamed");
        }
        if (this.file.renameTo(dest)) {
            this.file = dest;
            this.isRenamed = true;
            return true;
        }
        IOException exception = null;
        RandomAccessFile inputAccessFile = null;
        RandomAccessFile outputAccessFile = null;
        long chunkSize = 8196;
        long position = 0;
        try {
            inputAccessFile = new RandomAccessFile(this.file, ExcelXmlConstants.POSITION);
            outputAccessFile = new RandomAccessFile(dest, "rw");
            FileChannel in = inputAccessFile.getChannel();
            FileChannel out = outputAccessFile.getChannel();
            while (position < this.size) {
                if (chunkSize < this.size - position) {
                    chunkSize = this.size - position;
                }
                position += in.transferTo(position, chunkSize, out);
            }
            if (inputAccessFile != null) {
                try {
                    inputAccessFile.close();
                } catch (IOException e) {
                    if (0 == 0) {
                        exception = e;
                    } else {
                        logger.warn("Multiple exceptions detected, the following will be suppressed {}", (Throwable) e);
                    }
                }
            }
            if (outputAccessFile != null) {
                try {
                    outputAccessFile.close();
                } catch (IOException e2) {
                    if (exception == null) {
                        exception = e2;
                    } else {
                        logger.warn("Multiple exceptions detected, the following will be suppressed {}", (Throwable) e2);
                    }
                }
            }
        } catch (IOException e3) {
            exception = e3;
            if (inputAccessFile != null) {
                try {
                    inputAccessFile.close();
                } catch (IOException e4) {
                    if (exception == null) {
                        exception = e4;
                    } else {
                        logger.warn("Multiple exceptions detected, the following will be suppressed {}", (Throwable) e4);
                    }
                }
            }
            if (outputAccessFile != null) {
                try {
                    outputAccessFile.close();
                } catch (IOException e5) {
                    if (exception == null) {
                        exception = e5;
                    } else {
                        logger.warn("Multiple exceptions detected, the following will be suppressed {}", (Throwable) e5);
                    }
                }
            }
        } catch (Throwable th) {
            if (inputAccessFile != null) {
                try {
                    inputAccessFile.close();
                } catch (IOException e6) {
                    if (0 == 0) {
                        exception = e6;
                    } else {
                        logger.warn("Multiple exceptions detected, the following will be suppressed {}", (Throwable) e6);
                    }
                }
            }
            if (outputAccessFile != null) {
                try {
                    outputAccessFile.close();
                } catch (IOException e7) {
                    if (exception != null) {
                        logger.warn("Multiple exceptions detected, the following will be suppressed {}", (Throwable) e7);
                    }
                }
            }
            throw th;
        }
        if (exception != null) {
            throw exception;
        }
        if (position != this.size) {
            if (dest.delete()) {
                return false;
            }
            logger.warn("Failed to delete: {}", dest);
            return false;
        }
        if (!this.file.delete()) {
            logger.warn("Failed to delete: {}", this.file);
        }
        this.file = dest;
        this.isRenamed = true;
        return true;
    }

    private static byte[] readFrom(File src) throws IOException {
        long srcsize = src.length();
        if (srcsize > 2147483647L) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        RandomAccessFile accessFile = new RandomAccessFile(src, ExcelXmlConstants.POSITION);
        byte[] array = new byte[(int) srcsize];
        try {
            FileChannel fileChannel = accessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(array);
            for (int read = 0; read < srcsize; read += fileChannel.read(byteBuffer)) {
            }
            return array;
        } finally {
            accessFile.close();
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public File getFile() throws IOException {
        return this.file;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HttpData touch() {
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.ReferenceCounted
    public HttpData touch(Object hint) {
        return this;
    }
}
