package org.apache.coyote.http11;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.NioChannel;
import org.apache.tomcat.util.net.NioEndpoint;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11NioProtocol.class */
public class Http11NioProtocol extends AbstractHttp11JsseProtocol<NioChannel> {
    private static final Log log = LogFactory.getLog((Class<?>) Http11NioProtocol.class);

    public Http11NioProtocol() {
        super(new NioEndpoint());
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected Log getLog() {
        return log;
    }

    public void setPollerThreadCount(int count) {
        ((NioEndpoint) getEndpoint()).setPollerThreadCount(count);
    }

    public int getPollerThreadCount() {
        return ((NioEndpoint) getEndpoint()).getPollerThreadCount();
    }

    public void setSelectorTimeout(long timeout) {
        ((NioEndpoint) getEndpoint()).setSelectorTimeout(timeout);
    }

    public long getSelectorTimeout() {
        return ((NioEndpoint) getEndpoint()).getSelectorTimeout();
    }

    public void setPollerThreadPriority(int threadPriority) {
        ((NioEndpoint) getEndpoint()).setPollerThreadPriority(threadPriority);
    }

    public int getPollerThreadPriority() {
        return ((NioEndpoint) getEndpoint()).getPollerThreadPriority();
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected String getNamePrefix() {
        if (isSSLEnabled()) {
            return "https-" + getSslImplementationShortName() + "-nio";
        }
        return "http-nio";
    }
}
