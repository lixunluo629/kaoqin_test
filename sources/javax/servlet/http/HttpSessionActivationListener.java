package javax.servlet.http;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpSessionActivationListener.class */
public interface HttpSessionActivationListener extends EventListener {
    void sessionWillPassivate(HttpSessionEvent httpSessionEvent);

    void sessionDidActivate(HttpSessionEvent httpSessionEvent);
}
