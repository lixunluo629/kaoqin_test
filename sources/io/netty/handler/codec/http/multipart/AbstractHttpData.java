package io.netty.handler.codec.http.multipart;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/AbstractHttpData.class */
public abstract class AbstractHttpData extends AbstractReferenceCounted implements HttpData {
    private static final Pattern STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
    private static final Pattern REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
    private final String name;
    protected long definedSize;
    protected long size;
    private boolean completed;
    private Charset charset = HttpConstants.DEFAULT_CHARSET;
    private long maxSize = -1;

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public abstract HttpData touch();

    @Override // io.netty.util.ReferenceCounted
    public abstract HttpData touch(Object obj);

    protected AbstractHttpData(String name, Charset charset, long size) {
        ObjectUtil.checkNotNull(name, "name");
        String name2 = STRIP_PATTERN.matcher(REPLACE_PATTERN.matcher(name).replaceAll(SymbolConstants.SPACE_SYMBOL)).replaceAll("");
        if (name2.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = name2;
        if (charset != null) {
            setCharset(charset);
        }
        this.definedSize = size;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long getMaxSize() {
        return this.maxSize;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void checkSize(long newSize) throws IOException {
        if (this.maxSize >= 0 && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public String getName() {
        return this.name;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isCompleted() {
        return this.completed;
    }

    protected void setCompleted() {
        this.completed = true;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public Charset getCharset() {
        return this.charset;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setCharset(Charset charset) {
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long length() {
        return this.size;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long definedLength() {
        return this.definedSize;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        try {
            return getByteBuf();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        delete();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HttpData retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HttpData retain(int increment) {
        super.retain(increment);
        return this;
    }
}
