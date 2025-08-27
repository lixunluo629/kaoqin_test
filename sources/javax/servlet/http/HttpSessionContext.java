package javax.servlet.http;

import java.util.Enumeration;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpSessionContext.class */
public interface HttpSessionContext {
    HttpSession getSession(String str);

    Enumeration<String> getIds();
}
