package javax.servlet.http;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpSessionBindingListener.class */
public interface HttpSessionBindingListener extends EventListener {
    void valueBound(HttpSessionBindingEvent httpSessionBindingEvent);

    void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent);
}
