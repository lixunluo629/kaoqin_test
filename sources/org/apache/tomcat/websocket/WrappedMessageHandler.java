package org.apache.tomcat.websocket;

import javax.websocket.MessageHandler;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/WrappedMessageHandler.class */
public interface WrappedMessageHandler {
    long getMaxMessageSize();

    MessageHandler getWrappedHandler();
}
