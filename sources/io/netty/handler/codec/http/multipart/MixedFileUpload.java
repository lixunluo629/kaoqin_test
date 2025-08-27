package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/MixedFileUpload.class */
public class MixedFileUpload implements FileUpload {
    private final String baseDir;
    private final boolean deleteOnExit;
    private FileUpload fileUpload;
    private final long limitSize;
    private final long definedSize;
    private long maxSize;

    public MixedFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size, long limitSize) {
        this(name, filename, contentType, contentTransferEncoding, charset, size, limitSize, DiskFileUpload.baseDirectory, DiskFileUpload.deleteOnExitTemporaryFile);
    }

    public MixedFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size, long limitSize, String baseDir, boolean deleteOnExit) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        if (size > this.limitSize) {
            this.fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
        } else {
            this.fileUpload = new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
        }
        this.definedSize = size;
        this.baseDir = baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long getMaxSize() {
        return this.maxSize;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        this.fileUpload.setMaxSize(maxSize);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void checkSize(long newSize) throws IOException {
        if (this.maxSize >= 0 && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        if (this.fileUpload instanceof MemoryFileUpload) {
            checkSize(this.fileUpload.length() + buffer.readableBytes());
            if (this.fileUpload.length() + buffer.readableBytes() > this.limitSize) {
                DiskFileUpload diskFileUpload = new DiskFileUpload(this.fileUpload.getName(), this.fileUpload.getFilename(), this.fileUpload.getContentType(), this.fileUpload.getContentTransferEncoding(), this.fileUpload.getCharset(), this.definedSize, this.baseDir, this.deleteOnExit);
                diskFileUpload.setMaxSize(this.maxSize);
                ByteBuf data = this.fileUpload.getByteBuf();
                if (data != null && data.isReadable()) {
                    diskFileUpload.addContent(data.retain(), false);
                }
                this.fileUpload.release();
                this.fileUpload = diskFileUpload;
            }
        }
        this.fileUpload.addContent(buffer, last);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void delete() {
        this.fileUpload.delete();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public byte[] get() throws IOException {
        return this.fileUpload.get();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getByteBuf() throws IOException {
        return this.fileUpload.getByteBuf();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public Charset getCharset() {
        return this.fileUpload.getCharset();
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getContentType() {
        return this.fileUpload.getContentType();
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getContentTransferEncoding() {
        return this.fileUpload.getContentTransferEncoding();
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getFilename() {
        return this.fileUpload.getFilename();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString() throws IOException {
        return this.fileUpload.getString();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString(Charset encoding) throws IOException {
        return this.fileUpload.getString(encoding);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isCompleted() {
        return this.fileUpload.isCompleted();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isInMemory() {
        return this.fileUpload.isInMemory();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long length() {
        return this.fileUpload.length();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long definedLength() {
        return this.fileUpload.definedLength();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean renameTo(File dest) throws IOException {
        return this.fileUpload.renameTo(dest);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setCharset(Charset charset) {
        this.fileUpload.setCharset(charset);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(ByteBuf buffer) throws IOException {
        checkSize(buffer.readableBytes());
        if (buffer.readableBytes() > this.limitSize && (this.fileUpload instanceof MemoryFileUpload)) {
            FileUpload memoryUpload = this.fileUpload;
            this.fileUpload = new DiskFileUpload(memoryUpload.getName(), memoryUpload.getFilename(), memoryUpload.getContentType(), memoryUpload.getContentTransferEncoding(), memoryUpload.getCharset(), this.definedSize, this.baseDir, this.deleteOnExit);
            this.fileUpload.setMaxSize(this.maxSize);
            memoryUpload.release();
        }
        this.fileUpload.setContent(buffer);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(File file) throws IOException {
        checkSize(file.length());
        if (file.length() > this.limitSize && (this.fileUpload instanceof MemoryFileUpload)) {
            FileUpload memoryUpload = this.fileUpload;
            this.fileUpload = new DiskFileUpload(memoryUpload.getName(), memoryUpload.getFilename(), memoryUpload.getContentType(), memoryUpload.getContentTransferEncoding(), memoryUpload.getCharset(), this.definedSize, this.baseDir, this.deleteOnExit);
            this.fileUpload.setMaxSize(this.maxSize);
            memoryUpload.release();
        }
        this.fileUpload.setContent(file);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(InputStream inputStream) throws IOException {
        if (this.fileUpload instanceof MemoryFileUpload) {
            FileUpload memoryUpload = this.fileUpload;
            this.fileUpload = new DiskFileUpload(this.fileUpload.getName(), this.fileUpload.getFilename(), this.fileUpload.getContentType(), this.fileUpload.getContentTransferEncoding(), this.fileUpload.getCharset(), this.definedSize, this.baseDir, this.deleteOnExit);
            this.fileUpload.setMaxSize(this.maxSize);
            memoryUpload.release();
        }
        this.fileUpload.setContent(inputStream);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setContentType(String contentType) {
        this.fileUpload.setContentType(contentType);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.fileUpload.setContentTransferEncoding(contentTransferEncoding);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setFilename(String filename) {
        this.fileUpload.setFilename(filename);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.fileUpload.getHttpDataType();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public String getName() {
        return this.fileUpload.getName();
    }

    public int hashCode() {
        return this.fileUpload.hashCode();
    }

    public boolean equals(Object obj) {
        return this.fileUpload.equals(obj);
    }

    @Override // java.lang.Comparable
    public int compareTo(InterfaceHttpData o) {
        return this.fileUpload.compareTo(o);
    }

    public String toString() {
        return "Mixed: " + this.fileUpload;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getChunk(int length) throws IOException {
        return this.fileUpload.getChunk(length);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public File getFile() throws IOException {
        return this.fileUpload.getFile();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public FileUpload copy() {
        return this.fileUpload.copy();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public FileUpload duplicate() {
        return this.fileUpload.duplicate();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public FileUpload retainedDuplicate() {
        return this.fileUpload.retainedDuplicate();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public FileUpload replace(ByteBuf content) {
        return this.fileUpload.replace(content);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.fileUpload.content();
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.fileUpload.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public FileUpload retain() {
        this.fileUpload.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FileUpload retain(int increment) {
        this.fileUpload.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FileUpload touch() {
        this.fileUpload.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FileUpload touch(Object hint) {
        this.fileUpload.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.fileUpload.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.fileUpload.release(decrement);
    }
}
