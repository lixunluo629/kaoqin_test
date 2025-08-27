package org.apache.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.tomcat.util.buf.ByteChunk;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/OutputBuffer.class */
public interface OutputBuffer {
    @Deprecated
    int doWrite(ByteChunk byteChunk) throws IOException;

    int doWrite(ByteBuffer byteBuffer) throws IOException;

    long getBytesWritten();
}
