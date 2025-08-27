package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/MixedAttribute.class */
public class MixedAttribute implements Attribute {
    private String baseDir;
    private boolean deleteOnExit;
    private Attribute attribute;
    private final long limitSize;
    private long maxSize;

    public MixedAttribute(String name, long limitSize) {
        this(name, limitSize, HttpConstants.DEFAULT_CHARSET);
    }

    public MixedAttribute(String name, long definedSize, long limitSize) {
        this(name, definedSize, limitSize, HttpConstants.DEFAULT_CHARSET);
    }

    public MixedAttribute(String name, long limitSize, Charset charset) {
        this(name, limitSize, charset, DiskAttribute.baseDirectory, DiskAttribute.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name, charset);
        this.baseDir = baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public MixedAttribute(String name, long definedSize, long limitSize, Charset charset) {
        this(name, definedSize, limitSize, charset, DiskAttribute.baseDirectory, DiskAttribute.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, long definedSize, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name, definedSize, charset);
        this.baseDir = baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public MixedAttribute(String name, String value, long limitSize) {
        this(name, value, limitSize, HttpConstants.DEFAULT_CHARSET, DiskAttribute.baseDirectory, DiskFileUpload.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, String value, long limitSize, Charset charset) {
        this(name, value, limitSize, charset, DiskAttribute.baseDirectory, DiskFileUpload.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, String value, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        if (value.length() > this.limitSize) {
            try {
                this.attribute = new DiskAttribute(name, value, charset, baseDir, deleteOnExit);
            } catch (IOException e) {
                try {
                    this.attribute = new MemoryAttribute(name, value, charset);
                } catch (IOException e2) {
                    throw new IllegalArgumentException(e);
                }
            }
        } else {
            try {
                this.attribute = new MemoryAttribute(name, value, charset);
            } catch (IOException e3) {
                throw new IllegalArgumentException(e3);
            }
        }
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
        this.attribute.setMaxSize(maxSize);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void checkSize(long newSize) throws IOException {
        if (this.maxSize >= 0 && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            checkSize(this.attribute.length() + buffer.readableBytes());
            if (this.attribute.length() + buffer.readableBytes() > this.limitSize) {
                DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength(), this.baseDir, this.deleteOnExit);
                diskAttribute.setMaxSize(this.maxSize);
                if (((MemoryAttribute) this.attribute).getByteBuf() != null) {
                    diskAttribute.addContent(((MemoryAttribute) this.attribute).getByteBuf(), false);
                }
                this.attribute = diskAttribute;
            }
        }
        this.attribute.addContent(buffer, last);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void delete() {
        this.attribute.delete();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public byte[] get() throws IOException {
        return this.attribute.get();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getByteBuf() throws IOException {
        return this.attribute.getByteBuf();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public Charset getCharset() {
        return this.attribute.getCharset();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString() throws IOException {
        return this.attribute.getString();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString(Charset encoding) throws IOException {
        return this.attribute.getString(encoding);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isCompleted() {
        return this.attribute.isCompleted();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isInMemory() {
        return this.attribute.isInMemory();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long length() {
        return this.attribute.length();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long definedLength() {
        return this.attribute.definedLength();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean renameTo(File dest) throws IOException {
        return this.attribute.renameTo(dest);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setCharset(Charset charset) {
        this.attribute.setCharset(charset);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(ByteBuf buffer) throws IOException {
        checkSize(buffer.readableBytes());
        if (buffer.readableBytes() > this.limitSize && (this.attribute instanceof MemoryAttribute)) {
            this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength(), this.baseDir, this.deleteOnExit);
            this.attribute.setMaxSize(this.maxSize);
        }
        this.attribute.setContent(buffer);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(File file) throws IOException {
        checkSize(file.length());
        if (file.length() > this.limitSize && (this.attribute instanceof MemoryAttribute)) {
            this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength(), this.baseDir, this.deleteOnExit);
            this.attribute.setMaxSize(this.maxSize);
        }
        this.attribute.setContent(file);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(InputStream inputStream) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength(), this.baseDir, this.deleteOnExit);
            this.attribute.setMaxSize(this.maxSize);
        }
        this.attribute.setContent(inputStream);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.attribute.getHttpDataType();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public String getName() {
        return this.attribute.getName();
    }

    public int hashCode() {
        return this.attribute.hashCode();
    }

    public boolean equals(Object obj) {
        return this.attribute.equals(obj);
    }

    @Override // java.lang.Comparable
    public int compareTo(InterfaceHttpData o) {
        return this.attribute.compareTo(o);
    }

    public String toString() {
        return "Mixed: " + this.attribute;
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public String getValue() throws IOException {
        return this.attribute.getValue();
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public void setValue(String value) throws IOException {
        if (value != null) {
            checkSize(value.getBytes().length);
        }
        this.attribute.setValue(value);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getChunk(int length) throws IOException {
        return this.attribute.getChunk(length);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public File getFile() throws IOException {
        return this.attribute.getFile();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public Attribute copy() {
        return this.attribute.copy();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public Attribute duplicate() {
        return this.attribute.duplicate();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public Attribute retainedDuplicate() {
        return this.attribute.retainedDuplicate();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public Attribute replace(ByteBuf content) {
        return this.attribute.replace(content);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.attribute.content();
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.attribute.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public Attribute retain() {
        this.attribute.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public Attribute retain(int increment) {
        this.attribute.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public Attribute touch() {
        this.attribute.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public Attribute touch(Object hint) {
        this.attribute.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.attribute.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.attribute.release(decrement);
    }
}
