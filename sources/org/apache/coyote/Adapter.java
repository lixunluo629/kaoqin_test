package org.apache.coyote;

import org.apache.tomcat.util.net.SocketEvent;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/Adapter.class */
public interface Adapter {
    void service(Request request, Response response) throws Exception;

    boolean prepare(Request request, Response response) throws Exception;

    boolean asyncDispatch(Request request, Response response, SocketEvent socketEvent) throws Exception;

    void log(Request request, Response response, long j);

    void checkRecycled(Request request, Response response);

    String getDomain();
}
