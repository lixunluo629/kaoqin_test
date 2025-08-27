package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/HttpData.class */
public interface HttpData extends InterfaceHttpData, ByteBufHolder {
    long getMaxSize();

    void setMaxSize(long j);

    void checkSize(long j) throws IOException;

    void setContent(ByteBuf byteBuf) throws IOException;

    void addContent(ByteBuf byteBuf, boolean z) throws IOException;

    void setContent(File file) throws IOException;

    void setContent(InputStream inputStream) throws IOException;

    boolean isCompleted();

    long length();

    long definedLength();

    void delete();

    byte[] get() throws IOException;

    ByteBuf getByteBuf() throws IOException;

    ByteBuf getChunk(int i) throws IOException;

    String getString() throws IOException;

    String getString(Charset charset) throws IOException;

    void setCharset(Charset charset);

    Charset getCharset();

    boolean renameTo(File file) throws IOException;

    boolean isInMemory();

    File getFile() throws IOException;

    HttpData copy();

    HttpData duplicate();

    HttpData retainedDuplicate();

    HttpData replace(ByteBuf byteBuf);

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData, io.netty.util.ReferenceCounted
    HttpData retain();

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData, io.netty.util.ReferenceCounted
    HttpData retain(int i);

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData, io.netty.util.ReferenceCounted
    HttpData touch();

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData, io.netty.util.ReferenceCounted
    HttpData touch(Object obj);
}
