package org.apache.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/Processor.class */
public interface Processor {
    AbstractEndpoint.Handler.SocketState process(SocketWrapperBase<?> socketWrapperBase, SocketEvent socketEvent) throws IOException;

    UpgradeToken getUpgradeToken();

    boolean isUpgrade();

    boolean isAsync();

    void timeoutAsync(long j);

    Request getRequest();

    void recycle();

    void setSslSupport(SSLSupport sSLSupport);

    ByteBuffer getLeftoverInput();

    void pause();

    boolean checkAsyncTimeoutGeneration();
}
