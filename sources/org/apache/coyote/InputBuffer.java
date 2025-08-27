package org.apache.coyote;

import java.io.IOException;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.net.ApplicationBufferHandler;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/InputBuffer.class */
public interface InputBuffer {
    @Deprecated
    int doRead(ByteChunk byteChunk) throws IOException;

    int doRead(ApplicationBufferHandler applicationBufferHandler) throws IOException;
}
