package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpSession.class */
public interface HttpSession {
    long getCreationTime();

    String getId();

    long getLastAccessedTime();

    ServletContext getServletContext();

    void setMaxInactiveInterval(int i);

    int getMaxInactiveInterval();

    HttpSessionContext getSessionContext();

    Object getAttribute(String str);

    Object getValue(String str);

    Enumeration<String> getAttributeNames();

    String[] getValueNames();

    void setAttribute(String str, Object obj);

    void putValue(String str, Object obj);

    void removeAttribute(String str);

    void removeValue(String str);

    void invalidate();

    boolean isNew();
}
