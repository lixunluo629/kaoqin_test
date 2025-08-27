package org.apache.tomcat.util.net;

import java.nio.ByteBuffer;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/ApplicationBufferHandler.class */
public interface ApplicationBufferHandler {
    void setByteBuffer(ByteBuffer byteBuffer);

    ByteBuffer getByteBuffer();

    void expand(int i);
}
