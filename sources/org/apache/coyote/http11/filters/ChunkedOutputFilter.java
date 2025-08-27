package org.apache.coyote.http11.filters;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.coyote.Response;
import org.apache.coyote.http11.HttpOutputBuffer;
import org.apache.coyote.http11.OutputFilter;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.HexUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/filters/ChunkedOutputFilter.class */
public class ChunkedOutputFilter implements OutputFilter {
    private static final byte[] END_CHUNK_BYTES = {48, 13, 10, 13, 10};
    protected HttpOutputBuffer buffer;
    protected final ByteBuffer chunkHeader = ByteBuffer.allocate(10);
    protected final ByteBuffer endChunk = ByteBuffer.wrap(END_CHUNK_BYTES);

    public ChunkedOutputFilter() {
        this.chunkHeader.put(8, (byte) 13);
        this.chunkHeader.put(9, (byte) 10);
    }

    @Override // org.apache.coyote.OutputBuffer
    @Deprecated
    public int doWrite(ByteChunk chunk) throws IOException {
        int result = chunk.getLength();
        if (result <= 0) {
            return 0;
        }
        int pos = calculateChunkHeader(result);
        this.chunkHeader.position(pos + 1).limit((this.chunkHeader.position() + 9) - pos);
        this.buffer.doWrite(this.chunkHeader);
        this.buffer.doWrite(chunk);
        this.chunkHeader.position(8).limit(10);
        this.buffer.doWrite(this.chunkHeader);
        return result;
    }

    @Override // org.apache.coyote.OutputBuffer
    public int doWrite(ByteBuffer chunk) throws IOException {
        int result = chunk.remaining();
        if (result <= 0) {
            return 0;
        }
        int pos = calculateChunkHeader(result);
        this.chunkHeader.position(pos + 1).limit((this.chunkHeader.position() + 9) - pos);
        this.buffer.doWrite(this.chunkHeader);
        this.buffer.doWrite(chunk);
        this.chunkHeader.position(8).limit(10);
        this.buffer.doWrite(this.chunkHeader);
        return result;
    }

    private int calculateChunkHeader(int len) {
        int pos = 7;
        int current = len;
        while (current > 0) {
            int digit = current % 16;
            current /= 16;
            int i = pos;
            pos--;
            this.chunkHeader.put(i, HexUtils.getHex(digit));
        }
        return pos;
    }

    @Override // org.apache.coyote.OutputBuffer
    public long getBytesWritten() {
        return this.buffer.getBytesWritten();
    }

    @Override // org.apache.coyote.http11.OutputFilter
    public void setResponse(Response response) {
    }

    @Override // org.apache.coyote.http11.OutputFilter
    public void setBuffer(HttpOutputBuffer buffer) {
        this.buffer = buffer;
    }

    @Override // org.apache.coyote.http11.HttpOutputBuffer
    public void flush() throws IOException {
        this.buffer.flush();
    }

    @Override // org.apache.coyote.http11.HttpOutputBuffer
    public void end() throws IOException {
        this.buffer.doWrite(this.endChunk);
        this.endChunk.position(0).limit(this.endChunk.capacity());
        this.buffer.end();
    }

    @Override // org.apache.coyote.http11.OutputFilter
    public void recycle() {
    }
}
