package org.apache.coyote.http11;

import org.apache.coyote.Response;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/OutputFilter.class */
public interface OutputFilter extends HttpOutputBuffer {
    void setResponse(Response response);

    void recycle();

    void setBuffer(HttpOutputBuffer httpOutputBuffer);
}
