package org.apache.coyote.http11;

import java.io.IOException;
import org.apache.coyote.OutputBuffer;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/HttpOutputBuffer.class */
public interface HttpOutputBuffer extends OutputBuffer {
    void end() throws IOException;

    void flush() throws IOException;
}
