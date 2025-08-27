package org.apache.tomcat.websocket.server;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/server/WsSessionListener.class */
public class WsSessionListener implements HttpSessionListener {
    private final WsServerContainer wsServerContainer;

    public WsSessionListener(WsServerContainer wsServerContainer) {
        this.wsServerContainer = wsServerContainer;
    }

    @Override // javax.servlet.http.HttpSessionListener
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override // javax.servlet.http.HttpSessionListener
    public void sessionDestroyed(HttpSessionEvent se) {
        this.wsServerContainer.closeAuthenticatedSession(se.getSession().getId());
    }
}
