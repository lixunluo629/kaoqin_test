package javax.servlet.http;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpSessionAttributeListener.class */
public interface HttpSessionAttributeListener extends EventListener {
    void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent);

    void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent);

    void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent);
}
