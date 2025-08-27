package org.apache.catalina;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/SessionListener.class */
public interface SessionListener extends EventListener {
    void sessionEvent(SessionEvent sessionEvent);
}
