package org.apache.coyote.http2;

import org.apache.tomcat.util.net.SocketEvent;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/StreamRunnable.class */
class StreamRunnable implements Runnable {
    private final StreamProcessor processor;
    private final SocketEvent event;

    public StreamRunnable(StreamProcessor processor, SocketEvent event) {
        this.processor = processor;
        this.event = event;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.processor.process(this.event);
    }
}
