package org.apache.coyote.http11.upgrade;

import javax.servlet.http.HttpUpgradeHandler;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/upgrade/InternalHttpUpgradeHandler.class */
public interface InternalHttpUpgradeHandler extends HttpUpgradeHandler {
    AbstractEndpoint.Handler.SocketState upgradeDispatch(SocketEvent socketEvent);

    void setSocketWrapper(SocketWrapperBase<?> socketWrapperBase);

    void setSslSupport(SSLSupport sSLSupport);

    void pause();
}
